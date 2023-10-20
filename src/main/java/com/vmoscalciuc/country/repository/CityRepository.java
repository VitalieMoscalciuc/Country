package com.vmoscalciuc.country.repository;

import com.vmoscalciuc.country.model.City;
import com.vmoscalciuc.country.model.QCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long>, QuerydslPredicateExecutor<City>
        , CustomRepository<QCity> {
}
