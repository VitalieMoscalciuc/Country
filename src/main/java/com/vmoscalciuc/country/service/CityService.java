package com.vmoscalciuc.country.service;

import com.vmoscalciuc.country.dto.CityDtoRequest;
import com.vmoscalciuc.country.dto.CityDtoResponse;
import com.vmoscalciuc.country.model.City;
import com.vmoscalciuc.country.repository.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService extends GenericServiceImpl<City, Long, CityDtoRequest, CityDtoResponse, CityRepository> {

    @Autowired
    public CityService(ModelMapper modelMapper, CityRepository repository) {
        super(modelMapper, repository);
    }
}
