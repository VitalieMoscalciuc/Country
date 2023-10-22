package com.vmoscalciuc.country.controller;

import com.vmoscalciuc.country.dto.CountryDtoRequest;
import com.vmoscalciuc.country.dto.CountryDtoResponse;
import com.vmoscalciuc.country.model.Country;
import com.vmoscalciuc.country.service.CountryService;
import com.vmoscalciuc.country.facade.CountryFacade;
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
@RequestMapping("/api/country")
public class CountryController extends BaseController<Country, Long, CountryDtoRequest, CountryDtoResponse> {

    private final CountryService countryService;
    private final CountryFacade countryFacade;


    @Autowired
    public CountryController(CountryService countryService, ModelMapper modelMapper,CountryFacade countryFacade) {
        super(countryService, modelMapper);
        this.countryService = countryService;
        this.countryFacade = countryFacade;
    }

    @GetMapping("/readCSV")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CountryDtoRequest> getAllEntitiesFromCSV() throws IOException {

        return countryFacade.readDataFromCSV("C:/Users/VitalicPC/Desktop/country.csv");
    }
    @PostMapping("/saveCSV")
    @ResponseStatus(code = HttpStatus.OK)
    public void saveEntitiesFromExcel() throws IOException {
        countryFacade.addFromCSV("C:/Users/VitalicPC/Desktop/country.csv");
    }

    @PostMapping("/saveToCSV")
    @ResponseStatus(code = HttpStatus.OK)
    public void saveEntitiesToCSV(@RequestBody List<CountryDtoRequest> dataToInsert) throws IOException {
        countryFacade.insertDataIntoCSV(dataToInsert,"C:/Users/VitalicPC/Desktop/country.csv");
    }
}

