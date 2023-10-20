package com.vmoscalciuc.country.service;

import com.vmoscalciuc.country.repository.predicate.PredicateBuilder;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.List;

public interface GenericService<E, I, DtoRequest, DtoResponse> {
    List<E> getAllWithPagination(Integer pageNo, Integer pageSize, String sortBy);

    void add(DtoRequest dto);

    Iterable<E> getAllByPredicate(String search, PredicateBuilder<E> builder, String sortField, boolean ascending);

    List<DtoResponse> getAll();

    DtoResponse getById(I id);

    DtoResponse update(I id, DtoRequest dto);

    byte[] generatePdfReport(String search,String sortField, boolean ascending) throws JRException, IOException;

    void delete(I id);
}
