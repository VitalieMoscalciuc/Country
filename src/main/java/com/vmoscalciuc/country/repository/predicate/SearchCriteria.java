package com.vmoscalciuc.country.repository.predicate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}