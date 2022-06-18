package ru.bogdanov.diplom.repository.specification.entity;

import org.springframework.data.jpa.domain.Specification;
import ru.bogdanov.diplom.data.enums.SearchOperation;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.grpc.generated.auth.user.UserSearchRequest;
import ru.bogdanov.diplom.repository.specification.JpaSpecification;
import ru.bogdanov.diplom.repository.specification.SearchCriteria;
import ru.bogdanov.diplom.utils.JpaUtils;
import ru.bogdanov.diplom.utils.TimeUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author SBogdanov
 * Создание спецификаций для сущности пользователь
 * @see User
 */
public class UserSpecification {

    public static Specification<User> createSpecification(@NotNull final UserSearchRequest findRequest) {
        List<Specification<User>> specs = new ArrayList<>();
        if (findRequest.hasId()) {
            specs.add(new JpaSpecification(
                            new SearchCriteria("id",
                                    SearchOperation.EQUALITY,
                                    UUID.fromString(findRequest.getId()))
                    )
            );
        }
        if (findRequest.hasEmployeeId()) {
            specs.add(new JpaSpecification(
                            new SearchCriteria("employeeId",
                                    SearchOperation.EQUALITY,
                                    UUID.fromString(findRequest.getEmployeeId()))
                    )
            );
        }
        if (findRequest.hasEmployerId()) {
            specs.add(new JpaSpecification(
                            new SearchCriteria("employerId",
                                    SearchOperation.EQUALITY,
                                    UUID.fromString(findRequest.getEmployerId()))
                    )
            );
        }
        if (findRequest.hasLogin()) {
            specs.add(new JpaSpecification(
                            new SearchCriteria("login",
                                    SearchOperation.EQUALITY,
                                    findRequest.getLogin())
                    )
            );
        }
        if (findRequest.hasCreatedAt()) {
            specs.add(new JpaSpecification(
                    new SearchCriteria("createdAt",
                            SearchOperation.LOCALDATETIME_GREATHER_THAN,
                            LocalDateTime.of(TimeUtils.toLocalDateTime(findRequest.getCreatedAt()).toLocalDate(), LocalTime.MIN))));
            specs.add(new JpaSpecification(
                    new SearchCriteria("createdAt",
                            SearchOperation.LOCALDATETIME_LESS_THAN,
                            LocalDateTime.of(TimeUtils.toLocalDateTime(findRequest.getCreatedAt()).toLocalDate(), LocalTime.MAX))));
        }
        if (findRequest.hasUpdatedAt()) {
            specs.add(new JpaSpecification(
                    new SearchCriteria("updatedAt",
                            SearchOperation.LOCALDATETIME_GREATHER_THAN,
                            LocalDateTime.of(TimeUtils.toLocalDateTime(findRequest.getUpdatedAt()).toLocalDate(), LocalTime.MIN))));
            specs.add(new JpaSpecification(
                    new SearchCriteria("updatedAt",
                            SearchOperation.LOCALDATETIME_LESS_THAN,
                            LocalDateTime.of(TimeUtils.toLocalDateTime(findRequest.getUpdatedAt()).toLocalDate(), LocalTime.MAX))));
        }

        /*if (!CollectionUtils.isEmpty(findRequest.getRolesList())) {
            specs.add((root, criteriaQuery, criteriaBuilder) -> {
                Join roleJoin = root.join("roles", JoinType.INNER);

                Expression<UserRole> expression = roleJoin.get("roleName");
                Predicate predicate = expression.in(findRequest.getRolesList());
                criteriaQuery.where(predicate);

                return criteriaQuery.getRestriction();
            });
        }*/

        return JpaUtils.buildSingleAndSpecification(specs);
    }
}
