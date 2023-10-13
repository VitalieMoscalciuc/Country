package com.vmoscalciuc.country.controller;

import com.querydsl.core.types.dsl.PathBuilder;
import com.vmoscalciuc.country.repository.predicate.PredicateBuilder;
import com.vmoscalciuc.country.service.CityService;
import com.vmoscalciuc.country.service.GenericService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public abstract class BaseController<E, I, DtoRequest, DtoResponse> {

    private final GenericService<E, I, DtoRequest, DtoResponse> service;
    private final ModelMapper modelMapper;

    @GetMapping()
    @ResponseStatus(code = HttpStatus.OK)
    public List<DtoResponse> getAllEntities() {
        return service.getAll();
    }

    @GetMapping("/pagination")
    public ResponseEntity<List<E>> getAllEntitiesWithPagination(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        List<E> list = service.getAllWithPagination(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/pdf/filter", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<byte[]> filterToPDF(@RequestParam(value = "search") String search) throws JRException, IOException {

        byte data[] = service.generatePdfReport(search);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename="+ entityClass().getSimpleName().toLowerCase() +"report.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }


    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.FOUND)
    public DtoResponse getEntity(@PathVariable I id) {
        return modelMapper.map(service.getById(id), dtoResponseClass());
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addEntity(@RequestBody DtoRequest dto) {
        service.add(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public DtoResponse updateEntity(@PathVariable I id, @RequestBody DtoRequest dto) {
        return modelMapper.map(service.update(id, dto), dtoResponseClass());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.FOUND)
    public void deleteEntity(@PathVariable I id) {
        service.delete(id);
    }

    protected Class<DtoRequest> dtoRequestClass() {
        return (Class<DtoRequest>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[2];
    }

    protected Class<DtoResponse> dtoResponseClass() {
        return (Class<DtoResponse>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[3];
    }

    protected Class<E> entityClass() {
        return (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}

