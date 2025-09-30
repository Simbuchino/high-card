package it.sara.demo.service.auth.impl;

import it.sara.demo.dto.StatusDTO;
import it.sara.demo.exception.GenericException;
import it.sara.demo.security.CustomUserDetails;
import it.sara.demo.security.CustomUserDetailsService;
import it.sara.demo.security.JwtUtil;
import it.sara.demo.service.auth.AuthService;
import it.sara.demo.service.database.UserRepository;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.util.StringUtil;
import it.sara.demo.web.auth.response.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    public void setPassword(String guid, String token, String newPassword) throws GenericException {
        User user = userRepository.getByGuid(guid)
                .orElseThrow(() -> new GenericException(404, "User not found"));

        if (!validateResetToken(user, token)) {
            throw new GenericException(400, "Invalid or expired token");
        }

        user.setPassword(stringUtil.hashPassword(newPassword));
        user.setPasswordResetTokenUsed(true);
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        user.setPasswordNeedsReset(false);

        userRepository.update(user);
    }

    private boolean validateResetToken(User user, String rawToken) {
        if (user.isPasswordResetTokenUsed()) return false;
        if (user.getPasswordResetTokenExpiry() == null || Instant.now().isAfter(user.getPasswordResetTokenExpiry())) return false;
        return stringUtil.hashToken(rawToken).equals(user.getPasswordResetToken());
    }

    @Override
    public LoginResponse login(String email, String password) throws GenericException {

        try{
            User user = userRepository.getByEmail(email)
                    .orElseThrow(() -> new GenericException(404, "User not found"));

            if (user.isPasswordNeedsReset()) {
                throw new GenericException(403, "User must reset password before logging in");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            final String jwt = jwtUtil.generateToken(userDetails);
            LoginResponse response = new LoginResponse();
            response.setToken(jwt);
            response.setStatus(StatusDTO.success("Login successful"));

            return response;

        }catch (BadCredentialsException e) {
            throw new GenericException(401, "Invalid email or password");
        }
        catch (GenericException e){
            throw e;
        }
        catch (Exception e) {
            log.error("Error during login: {}", e.getMessage(), e);
            throw new GenericException(500, "Internal error");
        }



    }

}

