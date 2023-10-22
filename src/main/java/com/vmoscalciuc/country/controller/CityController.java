package com.vmoscalciuc.country.controller;

import com.vmoscalciuc.country.dto.CityDtoRequest;
import com.vmoscalciuc.country.dto.CityDtoResponse;
import com.vmoscalciuc.country.model.City;
import com.vmoscalciuc.country.service.CityService;
import com.vmoscalciuc.country.facade.CityFacade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/city")
public class CityController extends BaseController<City, Long, CityDtoRequest, CityDtoResponse> {
    private final CityService cityService;
    private final CityFacade cityFacade;

    @Autowired
    public CityController(CityService cityService, ModelMapper modelMapper,CityFacade cityFacade) {
        super(cityService, modelMapper);
        this.cityService = cityService;
        this.cityFacade = cityFacade;
    }

    @GetMapping("/readExcel")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CityDtoRequest> getAllEntitiesFromExcel() throws IOException {
        return cityFacade.readDataFromExcel("C:/Users/VitalicPC/Desktop/cities.xlsx");
    }
    @PostMapping("/saveExcel")
    @ResponseStatus(code = HttpStatus.OK)
    public void saveEntitiesFromExcel() throws IOException {
        cityFacade.addFromExcel("C:/Users/VitalicPC/Desktop/cities.xlsx");
    }
    @PostMapping("/saveToExcel")
    @ResponseStatus(code = HttpStatus.OK)
    public void saveEntitiesToExcel(@RequestBody List<CityDtoRequest> dataToInsert) throws IOException {
        cityFacade.insertDataIntoExcel(dataToInsert,"C:/Users/VitalicPC/Desktop/cities.xlsx");
    }

    //request url example
    //http://localhost:8080/api/city/pdf/filter?search=peopleCount<35000&sortField=peopleCount&ascending=true

}
