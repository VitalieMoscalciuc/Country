package com.vmoscalciuc.country.repository;

import com.vmoscalciuc.country.model.Country;
import com.vmoscalciuc.country.model.QCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country,Long>, QuerydslPredicateExecutor<Country>
        , CustomRepository<QCountry> {
}
