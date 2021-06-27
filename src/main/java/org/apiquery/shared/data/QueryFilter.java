package org.apiquery.shared.data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class QueryFilter {
    public final static String AND = "and";
    public final static String OR = "or";

    private List<FieldFilter> filters;
    private String type;
    private QueryFilterOption filterOptions;

    public String getType() {
        return this.type;
    }

    public List<FieldFilter> getFilters() {
        return this.filters;
    }

    public void setFilters(List<FieldFilter> filters) {
        if (filters == null)
            this.filters = new ArrayList<>();
        this.filters = filters;
    }

    private QueryFilterOption getFilterOption() {
        return this.filterOptions;
    }

    public QueryFilter() {
        filters = new ArrayList<>();
    }

    public QueryFilter(List<FieldFilter> fields, String type, QueryFilterOption filterOptions) {
        this.filters = fields;
        this.type = type;
        this.filterOptions = filterOptions;
    }

    public Specification createSpecification(Class entity) {
        if (filters.size() <= 0)
            return Specification.where(null);

        Specification conditions = Specification.where(specificationParse(filters.get(0), entity));
        for (int i = 1; i < filters.size(); i++) {
            if (type.equals("and"))
                conditions = conditions.and(specificationParse(filters.get(i), entity));
            else
                conditions = conditions.or(specificationParse(filters.get(i), entity));
        }
        return conditions;
    }

    public String getValue(String field) {
        for (FieldFilter item : this.filters) {
            if (item.getField().equals(field))
                return item.getValue();
        }
        return null;
    }

    public void addFilter(String field, String value, String condition) {
        FieldFilter filter = new FieldFilter();
        filter.setField(field);
        filter.setValue(value);
        filter.setCondition(condition);
        this.filters.add(filter);
    }

    public void removeField(String field) {
        Iterator<FieldFilter> iter = this.filters.iterator();
        while (iter.hasNext()) {
            if (iter.next().getField().equals(field))
                iter.remove();
        }
    }

    private <T> Specification<T> specificationParse(FieldFilter field, Class entity) {
        Object fieldValue = convertDataToFieldType(field.getValue(), field.getField(), entity);
        switch (field.getCondition()) {
            case "eq":
                if (fieldValue == null)
                    return (root, query, cb) -> cb.isNull(getPath(root, field.getField()));
                else
                    return (root, query, cb) -> cb.equal(getPath(root, field.getField()), fieldValue);
            case "gt":
                return gt(field.getField(), fieldValue, entity);
            case "lt":
                return lt(field.getField(), fieldValue, entity);
            case "le":
                return le(field.getField(), fieldValue, entity);
            case "ge":
                return ge(field.getField(), fieldValue, entity);
            case "not":
                return (root, query, cb) -> cb.notEqual(getPath(root, field.getField()), fieldValue);
            case "like":
                return (root, query, cb) -> {
                    query.distinct(true);
                    return cb.like(getPath(root, field.getField()), (String) fieldValue);
                };
            case "nlike":
                return (root, query, cb) -> cb.notLike(getPath(root, field.getField()), (String) fieldValue);
            default:
                return null;
        }
    }

    private <Y, T> Path<Y> getPath(Root<T> root, String attributeName) {
        String[] parts = attributeName.split("\\.");
        if (parts.length == 1) {
            return root.get(attributeName);
        } else if (parts.length == 2) {
            return root.join(parts[0], JoinType.LEFT).get(parts[1]);
        } else {
            return root.get(attributeName);
        }
    }

    private <T> Specification<T> gt(String field, Object value, Class entity) {

        try {
            String typeName = null;
            Field objectField = entity.getDeclaredField(field);
            typeName = objectField.getType().getSimpleName();

            switch (typeName) {
                case "Date":
                    return (root, query, cb) -> cb.greaterThan(getPath(root, field), (Date) value);
                case "Byte":
                    return (root, query, cb) -> cb.gt(getPath(root, field), (Byte) value);
                case "Integer":
                    return (root, query, cb) -> cb.gt(getPath(root, field), (Integer) value);
                case "Long":
                    return (root, query, cb) -> cb.gt(getPath(root, field), (Long) value);
                case "Float":
                    return (root, query, cb) -> cb.gt(getPath(root, field), (Float) value);
                case "String":
                    return (root, query, cb) -> cb.greaterThan(getPath(root, field), (String) value);
            }
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> Specification<T> lt(String field, Object value, Class entity) {
        try {
            String typeName = null;
            Field objectField = entity.getDeclaredField(field);
            typeName = objectField.getType().getSimpleName();

            switch (typeName) {
                case "Date":
                    return (root, query, cb) -> cb.lessThan(getPath(root, field), (Date) value);
                case "Byte":
                    return (root, query, cb) -> cb.lt(getPath(root, field), (Byte) value);
                case "Integer":
                    return (root, query, cb) -> cb.lt(getPath(root, field), (Integer) value);
                case "Long":
                    return (root, query, cb) -> cb.lt(getPath(root, field), (Long) value);
                case "Float":
                    return (root, query, cb) -> cb.lt(getPath(root, field), (Float) value);
                case "String":
                    return (root, query, cb) -> cb.lessThan(getPath(root, field), (String) value);
            }
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> Specification<T> le(String field, Object value, Class entity) {

        try {
            String typeName = null;
            Field objectField = entity.getDeclaredField(field);
            typeName = objectField.getType().getSimpleName();

            switch (typeName) {
                case "Date":
                    return (root, query, cb) -> cb.lessThanOrEqualTo(getPath(root, field), (Date) value);
                case "Byte":
                    return (root, query, cb) -> cb.le(getPath(root, field), (Byte) value);
                case "Integer":
                    return (root, query, cb) -> cb.le(getPath(root, field), (Integer) value);
                case "Long":
                    return (root, query, cb) -> cb.le(getPath(root, field), (Long) value);
                case "Float":
                    return (root, query, cb) -> cb.le(getPath(root, field), (Float) value);
                case "String":
                    return (root, query, cb) -> cb.lessThanOrEqualTo(getPath(root, field), (String) value);
            }
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> Specification<T> ge(String field, Object value, Class entity) {
        try {
            String typeName = null;
            Field objectField = entity.getDeclaredField(field);
            typeName = objectField.getType().getSimpleName();

            switch (typeName) {
                case "Date":
                    return (root, query, cb) -> cb.greaterThanOrEqualTo(getPath(root, field), (Date) value);
                case "Byte":
                    return (root, query, cb) -> cb.ge(getPath(root, field), (Byte) value);
                case "Integer":
                    return (root, query, cb) -> cb.ge(getPath(root, field), (Integer) value);
                case "Long":
                    return (root, query, cb) -> cb.ge(getPath(root, field), (Long) value);
                case "Float":
                    return (root, query, cb) -> cb.ge(getPath(root, field), (Float) value);
                case "String":
                    return (root, query, cb) -> cb.greaterThanOrEqualTo(getPath(root, field), (String) value);
            }
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object convertDataToFieldType(String data, String field, Class entity) {
        Field objectField;
        if (data.equals("null"))
            return null;

        try {
            String[] parts = field.split("\\.");
            if (parts.length == 2) {
                objectField = entity.getDeclaredField(parts[0]);
                if (objectField.getType().getCanonicalName().equals("java.util.Set")
                        || objectField.getType().getCanonicalName().equals("java.util.List")) {
                    ParameterizedType parasType = (ParameterizedType) objectField.getGenericType();
                    return convertDataToFieldType(data, parts[1], (Class<?>) parasType.getActualTypeArguments()[0]);
                } else
                    return convertDataToFieldType(data, parts[1], objectField.getType());
            }

            String typeName = null;
            if (field == "isDeleted")
                typeName = "Boolean";
            else if (field.equals("id"))
                typeName = "Integer";
            else {
                objectField = entity.getDeclaredField(field);
                typeName = objectField.getType().getSimpleName();
            }
            switch (typeName) {
                case "Date":
                    return parseDate(data);
                case "Boolean":
                    return Boolean.parseBoolean(data);
                case "Byte":
                    return Byte.parseByte(data);
                case "Integer":
                    return Integer.parseInt(data);
                case "Long":
                    return Long.parseLong(data);
                case "Float":
                    return Float.parseFloat(data);
                case "UUID":
                    return UUID.fromString(data);
                default:
                    return data;
            }
        } catch (NoSuchFieldException | SecurityException | ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    private Date parseDate(String data) throws ParseException {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data);
        } catch (Exception ex) {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(data);
        }
        return date;
    }
}