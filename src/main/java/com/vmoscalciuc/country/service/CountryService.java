package com.vmoscalciuc.country.service;

import com.vmoscalciuc.country.dto.CountryDtoRequest;
import com.vmoscalciuc.country.dto.CountryDtoResponse;
import com.vmoscalciuc.country.model.Country;
import com.vmoscalciuc.country.repository.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService extends GenericServiceImpl<Country, Long, CountryDtoRequest, CountryDtoResponse, CountryRepository> {

    @Autowired
    public CountryService(ModelMapper modelMapper, CountryRepository repository) {
        super(modelMapper, repository);
    }
}
