package it.sara.demo.web.auth.request;

import it.sara.demo.web.request.GenericRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest extends GenericRequest {
    private String email;
    private String password;
}
