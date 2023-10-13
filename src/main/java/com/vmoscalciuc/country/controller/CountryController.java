package com.vmoscalciuc.country.controller;

import com.vmoscalciuc.country.dto.CountryDtoRequest;
import com.vmoscalciuc.country.dto.CountryDtoResponse;
import com.vmoscalciuc.country.model.Country;
import com.vmoscalciuc.country.service.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/country")
public class CountryController extends BaseController<Country, Long, CountryDtoRequest, CountryDtoResponse> {

    @Autowired
    public CountryController(GenericService<Country, Long, CountryDtoRequest, CountryDtoResponse> service, ModelMapper modelMapper) {
        super(service, modelMapper);
    }
}

