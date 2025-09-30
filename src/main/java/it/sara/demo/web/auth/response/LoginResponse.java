package it.sara.demo.web.auth.response;

import it.sara.demo.web.response.GenericResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends GenericResponse {
    private String token;
}
