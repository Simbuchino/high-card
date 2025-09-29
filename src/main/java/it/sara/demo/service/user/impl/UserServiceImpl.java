package it.sara.demo.service.user.impl;

import it.sara.demo.dto.UserDTO;
import it.sara.demo.exception.GenericException;
import it.sara.demo.service.assembler.UserAssembler;
import it.sara.demo.service.database.UserRepository;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.database.page.PageUserResult;
import it.sara.demo.service.user.UserService;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.user.result.AddUserResult;
import it.sara.demo.service.user.result.GetUserResult;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.service.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAssembler userAssembler;

    @Override
    public AddUserResult addUser(CriteriaAddUser criteria) throws GenericException {

        AddUserResult returnValue;
        User user;

        try {

            returnValue = new AddUserResult();

            user = new User();
            user.setFirstName(criteria.getFirstName());
            user.setLastName(criteria.getLastName());
            user.setEmail(criteria.getEmail());
            user.setPhoneNumber(criteria.getPhoneNumber());

            if (!userRepository.save(user)) {
                throw new GenericException(500, "Error saving user");
            }

            returnValue.setGuid(user.getGuid());

        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            throw new GenericException(GenericException.GENERIC_ERROR);
        }
        return returnValue;
    }

    @Override
    public GetUsersResult getUsers(CriteriaGetUsers criteriaGetUsers) throws GenericException {

        GetUsersResult returnValue = new GetUsersResult();
        try {
        PageUserResult userList = userRepository.getByCriteria(criteriaGetUsers);

        List<UserDTO> dtoList = userList.getUsers().stream()
                .map(user -> userAssembler.toDTO(user))
                .toList();

        returnValue.setUsers(dtoList);
        returnValue.setTotal(userList.getTotal());
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            throw new GenericException(GenericException.GENERIC_ERROR);
        }
        return returnValue;
    }

    @Override
    public GetUserResult getUser(String guid) throws GenericException {
        User user = userRepository.getByGuid(guid)
                .orElseThrow(() -> new GenericException(404,"User not found with guid: " + guid));

        GetUserResult returnValue = new GetUserResult();
        returnValue.setUser(userAssembler.toDTO(user));
        return returnValue;
    }

}
