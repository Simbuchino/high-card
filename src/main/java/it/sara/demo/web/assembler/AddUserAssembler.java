package it.sara.demo.web.assembler;

import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.web.user.request.AddUserRequest;
import org.springframework.stereotype.Component;

@Component
public class AddUserAssembler {

    public CriteriaAddUser toCriteria(AddUserRequest addUserRequest) {
        CriteriaAddUser returnValue = new CriteriaAddUser();
        returnValue.setEmail(addUserRequest.getEmail());
        returnValue.setFirstName(addUserRequest.getFirstName());
        returnValue.setLastName(addUserRequest.getFirstName());
        returnValue.setPhoneNumber(addUserRequest.getPhoneNumber());
        return returnValue;
    }
}
