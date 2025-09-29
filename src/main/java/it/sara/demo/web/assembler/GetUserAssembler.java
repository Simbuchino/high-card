package it.sara.demo.web.assembler;

import it.sara.demo.dto.StatusDTO;
import it.sara.demo.service.user.result.GetUserResult;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.web.user.response.GetUserResponse;
import it.sara.demo.web.user.response.GetUsersResponse;
import org.springframework.stereotype.Component;

@Component
public class GetUserAssembler {

    public GetUsersResponse toResponse(GetUsersResult getUsersResult, StatusDTO statusDTO){
        GetUsersResponse returnValue = new GetUsersResponse();
        returnValue.setUsers(getUsersResult.getUsers());
        returnValue.setTotal(getUsersResult.getTotal());
        returnValue.setStatus(statusDTO);
        return returnValue;
    }

    public GetUserResponse toResponse(GetUserResult getUsersResult, StatusDTO statusDTO){
        GetUserResponse returnValue = new GetUserResponse();
        returnValue.setUser(getUsersResult.getUser());
        returnValue.setStatus(statusDTO);
        return returnValue;
    }
}

