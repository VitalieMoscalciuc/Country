package com.vmoscalciuc.country.facade;

import com.vmoscalciuc.country.dto.CountryDtoRequest;

import java.io.IOException;
import java.util.List;

public interface CountryFacade {
    List<CountryDtoRequest> readDataFromCSV(String csvFilePath) throws IOException;

    void insertDataIntoCSV(List<CountryDtoRequest> dataToInsert, String csvFilePath) throws IOException;

    void addFromCSV(String csvPath) throws IOException;
}
