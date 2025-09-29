package it.sara.demo.web.user;

import it.sara.demo.dto.StatusDTO;
import it.sara.demo.enums.OrderType;
import it.sara.demo.exception.GenericException;
import it.sara.demo.service.user.UserService;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.user.result.AddUserResult;
import it.sara.demo.service.user.result.GetUserResult;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.web.assembler.AddUserAssembler;
import it.sara.demo.web.assembler.GetUserAssembler;
import it.sara.demo.web.user.request.AddUserRequest;
import it.sara.demo.web.user.response.AddUserResponse;
import it.sara.demo.web.user.response.GetUserResponse;
import it.sara.demo.web.user.response.GetUsersResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddUserAssembler addUserAssembler;
    @Autowired
    private GetUserAssembler getUserAssembler;

    @PostMapping
    public ResponseEntity<AddUserResponse> addUser(@RequestBody @Valid AddUserRequest request, ServletUriComponentsBuilder servletUriComponentsBuilder) throws GenericException {
        CriteriaAddUser criteria = addUserAssembler.toCriteria(request);
        AddUserResult addUserResult = userService.addUser(criteria);
        URI uri = servletUriComponentsBuilder.path("/{guid}").buildAndExpand(addUserResult.getGuid()).toUri();
        AddUserResponse addUserResponse = addUserAssembler.toResponse(addUserResult, StatusDTO.success("User added."));
        return ResponseEntity.created(uri).body(addUserResponse);
    }

    @GetMapping("/{guid}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable @NotBlank String guid) throws GenericException {
        GetUserResult userResult = userService.getUser(guid);
        GetUserResponse response = getUserAssembler.toResponse(userResult, StatusDTO.success());
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<GetUsersResponse> getUsers(@RequestParam @Min(value = 0, message = "Offset must be 0 or greater") int offset,
                                                     @RequestParam @Min(value = 1, message = "Limit must be 1 or greater")
                                                     @Max(value = 100, message = "Limit cannot be greater than 100") int limit,
                                                     @RequestParam(required = false) String query,
                                                     @RequestParam(required = false) OrderType order) throws GenericException {

        CriteriaGetUsers criteria = new CriteriaGetUsers(query, offset, limit, order);
        GetUsersResult getUsersResult = userService.getUsers(criteria);
        GetUsersResponse response = getUserAssembler.toResponse(getUsersResult, StatusDTO.success());

        return ResponseEntity.ok(response);
    }
}
