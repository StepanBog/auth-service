package ru.bogdanov.diplom.repository.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.bogdanov.diplom.data.enums.SearchOperation;

@Data
@AllArgsConstructor
public class SearchCriteria {

    private String key;
    private SearchOperation operation;
    private Object value;
}
