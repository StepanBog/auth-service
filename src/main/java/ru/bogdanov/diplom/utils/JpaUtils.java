package ru.bogdanov.diplom.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@UtilityClass
public class JpaUtils {

    public static <T> Specification<T> buildSingleAndSpecification(List<Specification<T>> specs) {
        if (specs.isEmpty()) {
            return Specification.where(null);
        }
        Specification<T> result = Specification.where(specs.get(0));
        if (specs.size() > 1) {
            for (int i = 1; i < specs.size(); i++) {
                result = result.and(specs.get(i));
            }
        }
        return result;
    }
}
