package it.sara.demo.service.database.page;

import it.sara.demo.service.database.model.User;
import it.sara.demo.service.result.GenericPagedResult;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PageUserResult extends GenericPagedResult {
    private List<User> users;
}