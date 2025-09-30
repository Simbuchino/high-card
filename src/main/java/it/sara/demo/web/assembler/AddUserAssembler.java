package it.sara.demo.web.assembler;

import it.sara.demo.dto.StatusDTO;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.result.AddUserResult;
import it.sara.demo.web.user.request.AddUserRequest;
import it.sara.demo.web.user.response.AddUserResponse;
import org.springframework.stereotype.Component;

@Component
public class AddUserAssembler {

    public CriteriaAddUser toCriteria(AddUserRequest addUserRequest) {
        CriteriaAddUser returnValue = new CriteriaAddUser();
        returnValue.setEmail(addUserRequest.getEmail());
        returnValue.setFirstName(addUserRequest.getFirstName());
        returnValue.setLastName(addUserRequest.getLastName());
        returnValue.setPhoneNumber(addUserRequest.getPhoneNumber());
        return returnValue;
    }

    public AddUserResponse toResponse(AddUserResult addUserResult, StatusDTO statusDTO){
        AddUserResponse returnValue = new AddUserResponse();
        returnValue.setGuid(addUserResult.getGuid());
        returnValue.setStatus(statusDTO);
        return returnValue;
    }
}
