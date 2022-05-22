package ru.bogdanov.diplom.repository.specification;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JpaSpecification<T, X> implements Specification<T> {

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getKey().split("\\.").length > 1) {

            String[] joinsLabels = criteria.getKey().split("\\.");

            List<Join> joins = new ArrayList<>();
            joins.add(root.join(joinsLabels[0]));

            for (int i = 1; i < joinsLabels.length - 1; i++) {
                joins.add(joins.get(i - 1).join(joinsLabels[i]));
            }

            return buildFrom(joins.get(joins.size() - 1), builder, joinsLabels[joinsLabels.length - 1], criteria.getValue());
        }

        return buildFrom((From<T, X>) root, builder, criteria.getKey(), criteria.getValue());
    }

    private Predicate buildFrom(From<T, X> from, CriteriaBuilder builder, String key, Object value) {
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(from.get(key), value);
            case NEGATION:
                return builder.notEqual(from.get(key), value);
            case GREATER_THAN:
                return builder.greaterThan(from.get(key), value.toString());
            case LOCALDATETIME_GREATHER_THAN:
                return builder.greaterThan(from.get(key).as(LocalDateTime.class), (LocalDateTime) value);
            case LOCALDATETIME_LESS_THAN:
                return builder.lessThan(from.get(key).as(LocalDateTime.class), (LocalDateTime) value);
            case LESS_THAN:
                return builder.lessThan(from.get(key), value.toString());
            case LIKE:
                return builder.like(from.get(key), value.toString());
            case IS_NULL:
                return builder.isNull(from.get(key));
            case IN:                
                return builder.in(from.get(key)).value(value);
            case STARTS_WITH:
                return builder.like(from.get(key), value + "%");
            case ENDS_WITH:
                return builder.like(from.get(key), "%" + value);
            case CONTAINS:
                return builder.like(from.get(key), "%" + value + "%");
            default:
                return null;
        }
    }

}
