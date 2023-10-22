package com.vmoscalciuc.country.facade.impl;

import com.vmoscalciuc.country.dto.CountryDtoRequest;
import com.vmoscalciuc.country.facade.CountryFacade;
import com.vmoscalciuc.country.helper.DataHelper;
import com.vmoscalciuc.country.model.Country;
import com.vmoscalciuc.country.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryFacadeImpl implements CountryFacade {

    private final DataHelper<Country,Long, CountryDtoRequest> dataHelper;
    private final ModelMapper modelMapper;
    private final CountryRepository repository;
    @Override
    public List<CountryDtoRequest> readDataFromCSV(String csvFilePath) throws IOException {
        List<CountryDtoRequest> dtoRequestList = new ArrayList<>();
        try (CSVParser csvParser = CSVParser.parse(new File(csvFilePath), StandardCharsets.UTF_8, CSVFormat.DEFAULT)) {
            for (CSVRecord record : csvParser) {
                CountryDtoRequest dto = new CountryDtoRequest();
                int cellIndex = 0;
                for (String cellValue : record) {
                    dataHelper.setFieldInDto(dto, cellValue, cellIndex);
                    cellIndex++;
                }
                dtoRequestList.add(dto);
            }
        }
        return dtoRequestList;
    }

    @Override
    public void insertDataIntoCSV(List<CountryDtoRequest> dataToInsert, String csvFilePath) throws IOException {
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(csvFilePath, true), CSVFormat.DEFAULT)) {
            for (CountryDtoRequest dto : dataToInsert) {
                List<String> record = new ArrayList<>();
                dataHelper.mapDtoToRow(dto,null, record);
                csvPrinter.printRecord(record);
            }
        }
    }

    @Override
    public void addFromCSV(String csvPath) throws IOException {
        List<CountryDtoRequest> dtos = readDataFromCSV(csvPath);
        dtos.stream()
                .map(dtoRequest -> modelMapper.map(dtoRequest, Country.class))
                .forEach(repository::save);
    }
}
