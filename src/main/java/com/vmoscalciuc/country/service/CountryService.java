package com.vmoscalciuc.country.service;

import com.vmoscalciuc.country.dto.CountryDtoRequest;
import com.vmoscalciuc.country.dto.CountryDtoResponse;
import com.vmoscalciuc.country.exception.CountryNotFoundException;
import com.vmoscalciuc.country.model.Country;
import com.vmoscalciuc.country.repository.CountryRepository;
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
public class CountryService {

    @Autowired
    private ModelMapper modelMapper;

    private final CountryRepository countryRepository;


    public void addCountry(CountryDtoRequest CountryDtoRequest){
        countryRepository.save(
                Country.builder()
                        .name(CountryDtoRequest.getName())
                        .countryCode(CountryDtoRequest.getCountryCode()).build());
    }

    public List<Country> getAllCountries(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Country> pagedResult = countryRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public List<CountryDtoResponse> getAllCountries(){
        List<Country> cities = countryRepository.findAll();
        return cities.stream()
                .map(entity -> new CountryDtoResponse(entity.getId(),entity.getName(), entity.getCountryCode()))
                .collect(Collectors.toList());
    }

    public CountryDtoResponse getCountryDtoById(Long id) {
        Country Country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("No Country with Id:" + id + " is found"));
        return modelMapper.map(Country, CountryDtoResponse.class);
    }

    public CountryDtoResponse udpateCountryById(Long id, CountryDtoRequest newCountry) {
        Country oldCountry = countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("No Country with Id:" + id + " is found"));
        oldCountry.setName(newCountry.getName());
        oldCountry.setCountryCode(newCountry.getCountryCode());
        countryRepository.save(oldCountry);
        return modelMapper.map(oldCountry, CountryDtoResponse.class);
    }

    public void deleteCountryById(Long id){
        if(getCountryDtoById(id)==null){
            throw new CountryNotFoundException("No Country with Id:{id} is found");
        }else{
            countryRepository.deleteById(id);
        }
    }
}

