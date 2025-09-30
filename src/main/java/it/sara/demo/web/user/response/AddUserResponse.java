package it.sara.demo.web.user.response;

import it.sara.demo.enums.UserRole;
import it.sara.demo.web.response.GenericResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserResponse extends GenericResponse {
    private String guid;
}
