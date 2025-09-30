package it.sara.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sara.demo.enums.UserRole;
import it.sara.demo.service.database.FakeDatabase;
import it.sara.demo.service.database.model.User;
import it.sara.demo.web.user.request.AddUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        FakeDatabase.TABLE_USER.clear();

        // Admin
        User admin = new User();
        admin.setGuid(UUID.randomUUID().toString());
        admin.setEmail("admin@test.com");
        admin.setPassword(new BCryptPasswordEncoder().encode("password123"));
        admin.setRole(UserRole.ADMIN);
        admin.setPasswordNeedsReset(false);
        FakeDatabase.TABLE_USER.add(admin);

        // User
        User user = new User();
        user.setGuid(UUID.randomUUID().toString());
        user.setEmail("user@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("userpass"));
        user.setRole(UserRole.USER);
        user.setPasswordNeedsReset(false);
        FakeDatabase.TABLE_USER.add(user);
    }

    // -------------------- TEST ADD USER --------------------
    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void addUserSuccessAsAdmin() throws Exception {
        AddUserRequest request = new AddUserRequest();
        request.setEmail("newuser@test.com");
        request.setFirstName("mario");
        request.setLastName("bros");
        request.setPhoneNumber("+393331234232");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status.code").value(201))
                .andExpect(jsonPath("$.status.message").value("User added."));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void addUserFailAsUser() throws Exception {
        AddUserRequest request = new AddUserRequest();
        request.setEmail("newuser@test.com");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getUserAsAdmin() throws Exception {
        User user = FakeDatabase.TABLE_USER.get(1); // utente normale
        mockMvc.perform(get("/api/v1/users/" + user.getGuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.user.email").value(user.getEmail()));
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void getUserAsUser() throws Exception {
        User user = FakeDatabase.TABLE_USER.get(0); // admin
        mockMvc.perform(get("/api/v1/users/" + user.getGuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.user.email").value(user.getEmail()));
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getUsersAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/users?offset=0&limit=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.users").isArray());
    }

    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void getUsersAsUser() throws Exception {
        mockMvc.perform(get("/api/v1/users?offset=0&limit=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.users").isArray());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void addUserFailMissingEmail() throws Exception {
        AddUserRequest request = new AddUserRequest(); // email non settata

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400)); // validation error
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getUserFailNotFound() throws Exception {
        String fakeGuid = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/v1/users/" + fakeGuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404));

    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void getUsersFailInvalidParams() throws Exception {
        mockMvc.perform(get("/api/v1/users?offset=-1&limit=200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }


}
