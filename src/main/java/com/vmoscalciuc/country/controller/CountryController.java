package com.vmoscalciuc.country.controller;

import com.vmoscalciuc.country.dto.CountryDtoRequest;
import com.vmoscalciuc.country.dto.CountryDtoResponse;
import com.vmoscalciuc.country.model.Country;
import com.vmoscalciuc.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/country")
public class CountryController {
    private final CountryService countryService;

    @GetMapping()
    @ResponseStatus(code = HttpStatus.OK)
    public List<CountryDtoResponse> getCities(){
        return countryService.getAllCountries();
    }

    @GetMapping("/pagination")
    public ResponseEntity<List<Country>> getAllCities(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<Country> list = countryService.getAllCountries(pageNo, pageSize, sortBy);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.FOUND)
    public CountryDtoResponse getCountry(@PathVariable Long id){
        return countryService.getCountryDtoById(id);
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addCountry(@RequestBody CountryDtoRequest newCountry){
        countryService.addCountry(newCountry);
    }


    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public CountryDtoResponse updateCountry(@PathVariable Long id,@RequestBody CountryDtoRequest newCountry){
        return countryService.udpateCountryById(id,newCountry);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.FOUND)
    public void deleteCountry(@PathVariable Long id){
        countryService.deleteCountryById(id);
    }
}

