package it.sara.demo.service.user.criteria;

import it.sara.demo.enums.OrderType;
import it.sara.demo.service.criteria.GenericCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CriteriaGetUsers extends GenericCriteria {

    private String query;
    private int offset;
    private int limit;
    private OrderType order;
}
