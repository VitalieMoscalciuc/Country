package com.vmoscalciuc.country.facade;

import com.vmoscalciuc.country.dto.CityDtoRequest;

import java.io.IOException;
import java.util.List;

public interface CityFacade {

    List<CityDtoRequest> readDataFromExcel(String excelFilePath) throws IOException;

    void addFromExcel(String excelPath) throws IOException ;

    void insertDataIntoExcel(List<CityDtoRequest> dataToInsert, String excelFilePath) throws IOException;
}
