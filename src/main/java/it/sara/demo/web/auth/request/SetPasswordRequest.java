package it.sara.demo.web.auth.request;

import it.sara.demo.web.request.GenericRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPasswordRequest extends GenericRequest {
    @NotBlank
    private String guid;
    @NotBlank
    private String token;
    @NotBlank
    private String newPassword;
}

