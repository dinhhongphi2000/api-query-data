package org.apiquery.shared.utils;

import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityToDTO {
    public static <Entity, EntityDTO> EntityDTO convertTo(Class<EntityDTO> destinationType, Entity entity, String[] includes)
            throws Exception {
        if(entity == null)
                return null;

        entity = (Entity)Hibernate.unproxy(entity);
        Class<Entity> entityMeta = (Class<Entity>) (entity.getClass());
        EntityDTO dtoResult = destinationType.newInstance();
        BeanUtils.copyProperties(entity, dtoResult);
        if (includes == null)
            return dtoResult;
        //convert included entity field to DTO
        for (String field : includes) {
            //get included field information of entity
            Field entityField = entityMeta.getDeclaredField(field);
            //get included field information of DTO
            Field dtoField = destinationType.getDeclaredField(field + "Dto");
            if(dtoField == null)
                dtoField = destinationType.getDeclaredField(field);
            //get dto field type will be convert to
            Class dtoFieldType = dtoField.getType();
            //check field type of DTO is list or Object
            if (dtoFieldType.equals(Set.class) || dtoFieldType.equals(List.class)) {
                //get parameterized type of Set or List .Ex : Set<T> => T
                Class dtoFieldParametered = (Class) ((ParameterizedType) dtoField.getGenericType()).getActualTypeArguments()[0];
                entityField.setAccessible(true);
                Set<Object> entityFieldValue = (Set<Object>) entityField.get(entity);
                Set<Object> dtoFieldList = new HashSet<>();
                for (Object value : entityFieldValue) {
                    Object dtoItem = dtoFieldParametered.newInstance();
                    BeanUtils.copyProperties(value, dtoItem);
                    dtoFieldList.add(dtoItem);
                }
                dtoField.setAccessible(true);
                if(dtoFieldType.equals(List.class)) {
                    dtoField.set(dtoResult, new ArrayList<>(dtoFieldList));
                }else {
                    dtoField.set(dtoResult, dtoFieldList);
                }
            } else { //field DTO type is object
                //source must not null
                entityField.setAccessible(true);
                if(entityField.get(entity) == null)
                    continue;

                Object dtoActualField = dtoFieldType.newInstance();
                BeanUtils.copyProperties(entityField.get(entity), dtoActualField);
                dtoField.setAccessible(true);
                dtoField.set(dtoResult, dtoActualField);
            }
        }
        return dtoResult;
    }
}
