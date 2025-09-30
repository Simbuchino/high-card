package it.sara.demo.service.auth;

import it.sara.demo.exception.GenericException;
import it.sara.demo.web.auth.response.LoginResponse;

public interface AuthService {
    void setPassword(String guid, String token, String newPassword) throws GenericException;
    LoginResponse login(String email, String password) throws GenericException;
}