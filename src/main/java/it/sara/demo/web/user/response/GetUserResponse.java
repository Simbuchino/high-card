package it.sara.demo.web.user.response;

import it.sara.demo.dto.UserDTO;
import it.sara.demo.web.response.GenericPagedResponse;
import it.sara.demo.web.response.GenericResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetUserResponse extends GenericResponse {
    private UserDTO user;
}

