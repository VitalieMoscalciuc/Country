package com.vmoscalciuc.country.repository.predicate;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.core.types.dsl.StringPath;
import com.vmoscalciuc.country.model.City;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNumeric;
@NoArgsConstructor
public class Predicate<T> {
    private SearchCriteria criteria;
    private PathBuilder<T> entityPath;

    public Predicate(SearchCriteria criteria, PathBuilder<T> entityPath) {
        this.criteria = criteria;
        this.entityPath = entityPath;
    }

    public BooleanExpression getPredicate() {
        if (isNumeric(criteria.getValue().toString())) {
            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
            int value = Integer.parseInt(criteria.getValue().toString());
            switch (criteria.getOperation()) {
                case ":":
                    return path.eq(value);
                case ">":
                    return path.goe(value);
                case "<":
                    return path.loe(value);
            }
        } else {
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.containsIgnoreCase(criteria.getValue().toString());
            }
        }
        return null;
    }
}

