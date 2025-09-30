package it.sara.demo.service.database.model;

import it.sara.demo.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class User {
    private String guid;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private UserRole role;
    private boolean passwordNeedsReset;
    private String passwordResetToken;
    private Instant passwordResetTokenExpiry;
    private boolean passwordResetTokenUsed;
}
