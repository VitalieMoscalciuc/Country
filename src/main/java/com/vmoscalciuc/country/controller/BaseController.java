package com.vmoscalciuc.country.controller;

import com.vmoscalciuc.country.util.ReflectionUtils;
import com.vmoscalciuc.country.service.GenericService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.modelmapper.ModelMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public abstract class BaseController<E, I, DtoRequest, DtoResponse> {

    private final GenericService<E, I, DtoRequest, DtoResponse> service;
    private final ModelMapper modelMapper;
    private final ReflectionUtils<E, DtoRequest, DtoResponse> reflectionUtils = new ReflectionUtils<>();
    Class<?> clazz = this.getClass();

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
    public ResponseEntity<byte[]> filterToPDF(
            @RequestParam(value = "search") String search,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "ascending", required = false, defaultValue = "true") boolean ascending
    ) throws JRException, IOException {

        byte[] data = service.generatePdfReport(search, sortField, ascending);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename="
                        + reflectionUtils.getEntityClass(clazz).getSimpleName().toLowerCase() + "report.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.FOUND)
    public DtoResponse getEntity(@PathVariable I id) {
        return modelMapper.map(service.getById(id), reflectionUtils.getDtoResponseClass(clazz));
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addEntity(@RequestBody DtoRequest dto) {
        service.add(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public DtoResponse updateEntity(@PathVariable I id, @RequestBody DtoRequest dto) {
        return modelMapper.map(service.update(id, dto),reflectionUtils.getDtoResponseClass(clazz));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.FOUND)
    public void deleteEntity(@PathVariable I id) {
        service.delete(id);
    }

}

