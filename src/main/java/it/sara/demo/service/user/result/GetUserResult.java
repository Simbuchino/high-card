package it.sara.demo.service.user.result;

import it.sara.demo.dto.UserDTO;
import it.sara.demo.service.result.GenericResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserResult extends GenericResult {
    private UserDTO user;
}
