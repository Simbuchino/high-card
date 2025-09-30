package it.sara.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sara.demo.enums.UserRole;
import it.sara.demo.service.database.FakeDatabase;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.util.StringUtil;
import it.sara.demo.web.auth.request.LoginRequest;
import it.sara.demo.web.auth.request.SetPasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringUtil stringUtil;

    @BeforeEach
    void setup() {
        FakeDatabase.TABLE_USER.clear();

        // utente admin per test login
        User admin = new User();
        admin.setEmail("admin@test.com");
        admin.setPassword(new BCryptPasswordEncoder().encode("password123"));
        admin.setRole(UserRole.ADMIN);
        admin.setGuid(java.util.UUID.randomUUID().toString());
        admin.setPasswordNeedsReset(false);
        FakeDatabase.TABLE_USER.add(admin);

        // utente con reset password
        User user = new User();
        user.setEmail("user@test.com");
        user.setPasswordNeedsReset(true);
        user.setPasswordResetToken(stringUtil.hashToken("rawToken"));
        user.setGuid(java.util.UUID.randomUUID().toString());
        user.setPasswordResetTokenExpiry(Instant.now().plusSeconds(3600));
        user.setPasswordResetTokenUsed(false);
        FakeDatabase.TABLE_USER.add(user);
    }

    @Test
    void loginSuccess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@test.com");
        request.setPassword("password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.status.code").value(200));
    }

    @Test
    void loginFailWrongPassword() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@test.com");
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void loginFailUserNeedsReset() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("anyPassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(403));

    }

    @Test
    void setPasswordSuccess() throws Exception {
        SetPasswordRequest request = new SetPasswordRequest();
        User user = FakeDatabase.TABLE_USER.stream()
                .filter(u -> u.getEmail().equals("user@test.com")).findFirst().orElseThrow();
        request.setGuid(user.getGuid());
        request.setToken("rawToken");
        request.setNewPassword("newPassword123");

        mockMvc.perform(post("/api/v1/auth/set-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200));
    }

    @Test
    void setPasswordFailInvalidToken() throws Exception {
        SetPasswordRequest request = new SetPasswordRequest();
        User user = FakeDatabase.TABLE_USER.stream()
                .filter(u -> u.getEmail().equals("user@test.com")).findFirst().orElseThrow();
        request.setGuid(user.getGuid());
        request.setToken("wrongToken");
        request.setNewPassword("newPassword123");

        mockMvc.perform(post("/api/v1/auth/set-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }
}
