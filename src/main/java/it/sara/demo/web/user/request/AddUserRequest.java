package it.sara.demo.web.user.request;

import it.sara.demo.enums.UserRole;
import it.sara.demo.web.request.GenericRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest extends GenericRequest {
    /*  TASK 2
        Per prevenire correttamente SQL injection nella repository "UserRepository" occorre:
        - utilizzare query parametrizzate, inserendo quindi i dati client negli appositi parametri indicati con ?
        - effettuare la validazione input (in questo progetto con database finto, ho applicato una validazione semplice
            con una regex ad esclusione per alcuni caratteri non convenzionali in base al contesto del parametro (ex. First name)
     */
    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[^;\"=\\\\-]{1,50}$", message = "First name not valid")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[^;\"=\\\\-]{1,50}$", message = "Last name not valid")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$", message = "Email not valid")
    private String email;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^(?:\\+39|0039)3\\d{9}$", message = "Phone not valid")
    private String phoneNumber;

    private UserRole role = UserRole.USER;
}
