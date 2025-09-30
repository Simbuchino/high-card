package it.sara.demo.service.util;

import it.sara.demo.service.database.model.User;
import it.sara.demo.service.database.page.PageUserResult;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Utility per filtrare, ordinare e paginare liste di utenti.
 * Usata principalmente da UserRepository per getUsers.
 * Mantiene separata la logica di repository dal service, come avverrebbe con un DB reale.
 */
@Component
public class RepositoryUtil {

    public static PageUserResult filterSortPaginateUsers(List<User> users, CriteriaGetUsers criteria){

        PageUserResult pageUserResult = new PageUserResult();
        pageUserResult.setUsers(List.of());
        pageUserResult.setTotal(0);

        String query = criteria.getQuery();
        if(query != null && !query.isBlank()){
            String queryLower = criteria.getQuery().toLowerCase();
            users = users.stream()
                    .filter(user ->
                            (user.getFirstName() != null && user.getFirstName().toLowerCase().contains(queryLower)) ||
                                    (user.getLastName() != null && user.getLastName().toLowerCase().contains(queryLower)) ||
                                    (user.getEmail() != null && user.getEmail().toLowerCase().contains(queryLower)))
                    .toList();

        }
        int total = users.size();
        pageUserResult.setTotal(total);

        Comparator<User> comparator = getUserComparator(criteria);
        int offset = Math.max(0, criteria.getOffset());
        int limit = Math.max(1, criteria.getLimit());

        users = users.stream()
                    .sorted(comparator)
                    .skip(offset)
                    .limit(limit)
                    .toList();

        pageUserResult.setUsers(users);

        return pageUserResult;
    }

    private static Comparator<User> getUserComparator(CriteriaGetUsers criteria) {
        Comparator<User> comparator = Comparator.comparing(User::getFirstName, Comparator.nullsLast(String::compareToIgnoreCase));
        if(criteria.getOrder() != null) {
            switch (criteria.getOrder()) {
                case BY_FIRSTNAME_DESC ->
                        comparator = Comparator.comparing(User::getFirstName, Comparator.nullsLast(String::compareToIgnoreCase).reversed());
                case BY_LASTNAME ->
                        comparator = Comparator.comparing(User::getLastName, Comparator.nullsLast(String::compareToIgnoreCase));
                case BY_LASTNAME_DESC ->
                        comparator = Comparator.comparing(User::getLastName, Comparator.nullsLast(String::compareToIgnoreCase).reversed());
            }
        }
        return comparator;
    }
}
