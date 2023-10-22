package com.vmoscalciuc.country.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

//helper
@Component
public class DataHelper<E, I, DtoRequest> {

    public void mapDtoToRow(DtoRequest dto, Row row, List<String> record) {
        Field[] fields = dto.getClass().getDeclaredFields();
        if (record == null) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(dto);
                    assert false;
                    if (fieldValue != null) {
                        record.add(fieldValue.toString());
                    } else {
                        record.add("");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int cellIndex = 0; cellIndex < fields.length; cellIndex++) {
                Cell cell = row.createCell(cellIndex);
                Field field = fields[cellIndex];
                field.setAccessible(true);

                try {
                    Object fieldValue = field.get(dto);
                    if (fieldValue != null) {
                        if (fieldValue instanceof String) {
                            cell.setCellValue((String) fieldValue);
                        } else if (fieldValue instanceof Integer) {
                            cell.setCellValue((Integer) fieldValue);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setFieldInDto(DtoRequest dto, String cellValue, int cellIndex) {
        try {
            Field[] fields = dto.getClass().getDeclaredFields();
            Field field = fields[cellIndex];
            field.setAccessible(true);

            Class<?> fieldType = field.getType();
            if (fieldType == String.class) {
                field.set(dto, cellValue);
            }
            if (fieldType == Long.class) {
                double doubleValue = Double.parseDouble(cellValue);
                long longValue = (long) doubleValue;
                field.set(dto, longValue);
            } else if (fieldType == Integer.class) {
                double doubleValue = Double.parseDouble(cellValue);
                int intValue = (int) doubleValue;
                field.set(dto, intValue);
            } else if (fieldType == Double.class) {
                Double doubleValue = Double.parseDouble(cellValue);
                field.set(dto, doubleValue);
            }
        } catch (IllegalAccessException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
