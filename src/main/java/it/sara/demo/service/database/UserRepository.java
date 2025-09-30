package it.sara.demo.service.database;

import it.sara.demo.exception.GenericException;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.database.page.PageUserResult;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.service.util.RepositoryUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepository {

    public boolean save(User user) {
        user.setGuid(java.util.UUID.randomUUID().toString());
        FakeDatabase.TABLE_USER.add(user);
        return true;
    }

    public Optional<User> getByGuid(String guid) {
        return FakeDatabase.TABLE_USER.stream().filter(u -> u.getGuid().equals(guid)).findFirst();
    }

    public List<User> getAll() {
        return FakeDatabase.TABLE_USER;
    }

    /**
     * Recupera utenti filtrati, ordinati e paginati secondo i criteri specificati.
     * @param criteriaGetUsers criteri di ricerca, ordinamento e paginazione
     * @return risultato paginato con utenti e totale
     */
    public PageUserResult getByCriteria(CriteriaGetUsers criteriaGetUsers) {
        return RepositoryUtil.filterSortPaginateUsers(getAll(), criteriaGetUsers);
    }

    public Optional<User> getByEmail(String email) {
        return FakeDatabase.TABLE_USER.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
        }

    public boolean update(User user) {
        for (int i = 0; i < FakeDatabase.TABLE_USER.size(); i++) {
            if (FakeDatabase.TABLE_USER.get(i).getGuid().equals(user.getGuid())) {
                FakeDatabase.TABLE_USER.set(i, user);
                return true;
            }
        }
        return false;
    }

}
