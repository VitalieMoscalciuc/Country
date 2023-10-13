package com.vmoscalciuc.country.service;

import com.vmoscalciuc.country.dto.CityDtoRequest;
import com.vmoscalciuc.country.dto.CityDtoResponse;
import com.vmoscalciuc.country.exception.CityNotFoundException;
import com.vmoscalciuc.country.model.City;
import com.vmoscalciuc.country.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityService {

    @Autowired
    private ModelMapper modelMapper;

    private final CityRepository cityRepository;


    public void addCity(CityDtoRequest cityDtoRequest){
        cityRepository.save(
                City.builder()
                .name(cityDtoRequest.getName())
                .peopleCount(cityDtoRequest.getPeopleCount()).build());
    }

    public List<City> getAllCities(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<City> pagedResult = cityRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<CityDtoResponse> getAllCities(){
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .map(entity -> new CityDtoResponse(entity.getId(),entity.getName(), entity.getPeopleCount()))
                .collect(Collectors.toList());
    }

    public CityDtoResponse getCityDtoById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("No City with Id:" + id + " is found"));
        return modelMapper.map(city, CityDtoResponse.class);
    }

    public CityDtoResponse udpateCityById(Long id, CityDtoRequest newCity) {
        City oldCity = cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("No City with Id:" + id + " is found"));
        oldCity.setName(newCity.getName());
        oldCity.setPeopleCount(newCity.getPeopleCount());
        cityRepository.save(oldCity);
        return modelMapper.map(oldCity, CityDtoResponse.class);
    }

    public void deleteCityById(Long id){
        if(getCityDtoById(id)==null){
            throw new CityNotFoundException("No City with Id:{id} is found");
        }else{
            cityRepository.deleteById(id);
        }
    }
}
