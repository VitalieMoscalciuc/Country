package com.vmoscalciuc.country.controller;

import com.vmoscalciuc.country.dto.CityDtoRequest;
import com.vmoscalciuc.country.dto.CityDtoResponse;
import com.vmoscalciuc.country.model.City;
import com.vmoscalciuc.country.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/api/city")
public class CityController {
    private final CityService cityService;

    @GetMapping()
    @ResponseStatus(code = HttpStatus.OK)
    public List<CityDtoResponse> getCities(){
        return cityService.getAllCities();
    }

    @GetMapping("/pagination")
    public ResponseEntity<List<City>> getAllCities(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<City> list = cityService.getAllCities(pageNo, pageSize, sortBy);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.FOUND)
    public CityDtoResponse getCity(@PathVariable Long id){
        return cityService.getCityDtoById(id);
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addCity(@RequestBody CityDtoRequest newCity){
        cityService.addCity(newCity);
    }


    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public CityDtoResponse updateCity(@PathVariable Long id,@RequestBody CityDtoRequest newCity){
        return cityService.udpateCityById(id,newCity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.FOUND)
    public void deleteCity(@PathVariable Long id){
        cityService.deleteCityById(id);
    }
}
