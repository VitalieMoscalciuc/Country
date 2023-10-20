package com.vmoscalciuc.country.repository.predicate;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.vmoscalciuc.country.model.City;
import com.vmoscalciuc.country.model.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PredicateBuilder<T> {
    private final List<SearchCriteria> params;
    private final PathBuilder<T> entityPath;

    public PredicateBuilder(PathBuilder<T> entityPath) {
        this.entityPath = entityPath;
        params = new ArrayList<>();
    }

    public PredicateBuilder<T> with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build() {
        if (params.isEmpty()) {
            return null;
        }

        List<BooleanExpression> predicates = params.stream().map(param -> {
            Predicate<T> predicate = new Predicate<>(param, entityPath);
            return predicate.getPredicate();
        }).filter(Objects::nonNull).toList();

        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }
        return result;
    }
}

