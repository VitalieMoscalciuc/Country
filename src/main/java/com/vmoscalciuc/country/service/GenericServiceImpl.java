package com.vmoscalciuc.country.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.vmoscalciuc.country.exception.NotFoundException;
import com.vmoscalciuc.country.repository.predicate.PredicateBuilder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public abstract class GenericServiceImpl<E, I, DtoRequest, DtoResponse, R extends JpaRepository<E, I> & QuerydslPredicateExecutor<E>> implements GenericService<E, I, DtoRequest, DtoResponse> {

    @Autowired
    private final ModelMapper modelMapper;

    protected final R repository;

    @Override
    public List<E> getAllWithPagination(Integer pageNo, Integer pageSize, String sortBy) {
        return repository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy))).getContent();
    }

    @Override
    public void add(DtoRequest dto) {
        repository.save(modelMapper.map(dto, entityClass()));
    }

    @Override
    public Iterable<E> getAllByPredicate(String search, PredicateBuilder<E> builder, String sortField, boolean ascending) {
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        if(sortField==null){
            BooleanExpression exp = builder.build();
            return repository.findAll(exp);
        }else{
            BooleanExpression exp = builder.build();
            Sort sort = ascending ? Sort.by(Sort.Order.asc(sortField)) : Sort.by(Sort.Order.desc(sortField));
            return repository.findAll(exp, sort);
        }
    }

    @Override
    public List<DtoResponse> getAll() {
        return repository.findAll().stream().map(entity -> modelMapper.map(entity, dtoResponseClass())).collect(Collectors.toList());
    }

    @Override
    public DtoResponse getById(I id) {
        return modelMapper.map(repository.findById(id).orElseThrow(() -> new NotFoundException("No Entity with Id:" + id + " is found")), dtoResponseClass());
    }

    @Override
    public DtoResponse update(I id, DtoRequest dto) {
        E existingEntity = repository.findById(id).orElseThrow(() -> new NotFoundException("No Entity with Id:" + id + " is found"));
        modelMapper.map(dto, existingEntity);
        E updatedEntity = repository.save(existingEntity);
        return modelMapper.map(updatedEntity, dtoResponseClass());
    }

    @Override
    public byte[] generatePdfReport(String search,String sortField, boolean ascending) throws JRException, IOException {
        PathBuilder<E> entityPath = new PathBuilder<>(entityClass(), entityClass().getSimpleName().toLowerCase());
        PredicateBuilder<E> builder = new PredicateBuilder<>(entityPath);

        JRBeanCollectionDataSource beanCollectionDataSource =
                new JRBeanCollectionDataSource((Collection<?>) getAllByPredicate(search, builder,sortField,ascending),
                        false);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("total", "7000");

        JasperReport compileReport = JasperCompileManager
                .compileReport(new FileInputStream("src/main/resources/"+entityClass().getSimpleName().toLowerCase()+".jrxml"));

        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, parameters, beanCollectionDataSource);

        byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);

        return data;
    }

    @Override
    public void delete(I id) {
        if (getById(id) != null) {
            repository.deleteById(id);
        }
    }

    protected Class<DtoRequest> dtoRequestClass() {
        return (Class<DtoRequest>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
    }

    protected Class<DtoResponse> dtoResponseClass() {
        return (Class<DtoResponse>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
    }

    protected Class<E> entityClass() {
        return (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
