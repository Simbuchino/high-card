package it.sara.demo.service.user.criteria;

import it.sara.demo.enums.UserRole;
import it.sara.demo.service.criteria.GenericCriteria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriteriaAddUser extends GenericCriteria {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserRole role;
}
