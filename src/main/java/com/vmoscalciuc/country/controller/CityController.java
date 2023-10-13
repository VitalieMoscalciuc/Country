package com.vmoscalciuc.country.controller;

import com.vmoscalciuc.country.dto.CityDtoRequest;
import com.vmoscalciuc.country.dto.CityDtoResponse;
import com.vmoscalciuc.country.model.City;
import com.vmoscalciuc.country.service.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/city")
public class CityController extends BaseController<City, Long, CityDtoRequest, CityDtoResponse> {

    @Autowired
    public CityController(GenericService<City, Long, CityDtoRequest, CityDtoResponse> service, ModelMapper modelMapper) {
        super(service, modelMapper);
    }


}
