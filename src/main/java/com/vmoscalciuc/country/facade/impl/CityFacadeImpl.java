package com.vmoscalciuc.country.facade.impl;

import com.vmoscalciuc.country.dto.CityDtoRequest;
import com.vmoscalciuc.country.facade.CityFacade;
import com.vmoscalciuc.country.helper.DataHelper;
import com.vmoscalciuc.country.model.City;
import com.vmoscalciuc.country.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityFacadeImpl implements CityFacade {

    private final DataHelper<City,Long, CityDtoRequest> dataHelper;
    private final ModelMapper modelMapper;
    private final CityRepository repository;


    @Override
    public List<CityDtoRequest> readDataFromExcel(String excelFilePath) throws IOException {

        InputStream inputStream = new FileInputStream(excelFilePath);

        List<CityDtoRequest> dtoResponseList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                CityDtoRequest dto = new CityDtoRequest();
                for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    if (cell != null) {
                        String cellValue = cell.toString();
                        dataHelper.setFieldInDto(dto, cellValue, cellIndex);
                    }
                }
                dtoResponseList.add(dto);
            }
        }
        return dtoResponseList;
    }

    @Override
    public void addFromExcel(String excelPath) throws IOException {
        List<CityDtoRequest> dtos = readDataFromExcel(excelPath);
        dtos.stream()
                .map(dtoRequest -> modelMapper.map(dtoRequest, City.class))
                .forEach(repository::save);
    }

    @Override
    public void insertDataIntoExcel(List<CityDtoRequest> dataToInsert, String excelFilePath) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            int currentRowIndex = sheet.getLastRowNum() + 1;

            for (CityDtoRequest dto : dataToInsert) {
                Row row = sheet.createRow(currentRowIndex);
                DataHelper<City,Long,CityDtoRequest> dataHelper = new DataHelper<>();
                dataHelper.mapDtoToRow(dto, row,null);

                currentRowIndex++;
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOutputStream);
            }
        }
    }

}
