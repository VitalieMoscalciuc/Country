package com.vmoscalciuc.country.util;

import java.lang.reflect.ParameterizedType;

public class ReflectionUtils<E, DtoRequest, DtoResponse> {
    public Class<DtoRequest> getDtoRequestClass(Class<?> clazz) {
        return (Class<DtoRequest>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[2];
    }

    public Class<DtoResponse> getDtoResponseClass(Class<?> clazz) {
        return (Class<DtoResponse>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[3];
    }

    public Class<E> getEntityClass(Class<?> clazz) {
        return (Class<E>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
