package com.sheetmanager.mappers;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMapperService<T,E> {
    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public abstract Class getBaseEntityType();
    public abstract Class getDTOEntityType();

    public T toEntity(E entityDTO) {
        mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(getDTOEntityType(), getBaseEntityType()).byDefault().register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return (T)mapper.map(entityDTO, getBaseEntityType());
    }

    public E toDTO(T entity) {
        mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(getBaseEntityType(), getDTOEntityType()).byDefault().register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return (E)mapper.map(entity, getDTOEntityType());
    }
}
