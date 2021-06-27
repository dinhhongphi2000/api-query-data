package org.apiquery.shared.data;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.text.SimpleDateFormat;

import java.lang.reflect.ParameterizedType;

import org.springframework.data.jpa.domain.Specification;
import java.text.ParseException;

public class QueryAdvanceFilter {
    private QueryItem filter;

    public QueryItem getFilter() {
        return this.filter;
    }

    public void setFilter(QueryItem filter) {
        this.filter = filter;
    }

    public QueryItem getQueryData() {
        return this.filter;
    }

    public void setQueryData(QueryItem queryData) {
        this.filter = queryData;
    }

    public Specification createSpecification(Class entity) {
        if (this.filter == null)
            return Specification.where(null);

        return (root, query, cb) -> {
            query.distinct(true);
            return specificationParse(filter.getName(), filter.getValue(), entity).toPredicate(root, query, cb, null);
        };
    }

    @SuppressWarnings("unchecked")
    private <T> SpecificationCustom<T> specificationParse(String field, Object value, Class entity) {
        if (field.startsWith("$")) {
            // handler operator
            switch (field) {
                case "$and":
                    List<QueryItem> queryItems = (ArrayList<QueryItem>) value;
                    return (root, query, cb, fieldName) -> {
                        Predicate result = null;
                        for (QueryItem item : queryItems) {
                            Predicate predict = ((SpecificationCustom<T>) specificationParse(item.getName(),
                                    item.getValue(), entity)).toPredicate(root, query, cb, fieldName);
                            if (result == null)
                                result = predict;
                            else
                                result = cb.and(result, predict);
                        }
                        return result;
                    };
                case "$or":
                    queryItems = (ArrayList<QueryItem>) value;
                    return (root, query, cb, fieldName) -> {
                        Predicate result = null;
                        for (QueryItem item : queryItems) {
                            Predicate predict = ((SpecificationCustom<T>) specificationParse(item.getName(),
                                    item.getValue(), entity)).toPredicate(root, query, cb, fieldName);

                            if (result == null)
                                result = predict;
                            else
                                result = cb.or(result, predict);
                        }
                        return result;
                    };
                case "$lt":
                    return (root, query, cb, fieldName) -> {
                        Object fieldValue = convertDataToFieldType(value, fieldName, entity);
                        return ((Specification<T>) lt(fieldName, fieldValue, entity)).toPredicate(root, query, cb);
                    };
                case "$gt":
                    return (root, query, cb, fieldName) -> {
                        Object fieldValue = convertDataToFieldType(value, fieldName, entity);
                        return ((Specification<T>) gt(fieldName, fieldValue, entity)).toPredicate(root, query, cb);
                    };
                case "$ge":
                    return (root, query, cb, fieldName) -> {
                        Object fieldValue = convertDataToFieldType(value, fieldName, entity);
                        return ((Specification<T>) ge(fieldName, fieldValue, entity)).toPredicate(root, query, cb);
                    };
                case "$le":
                    return (root, query, cb, fieldName) -> {
                        Object fieldValue = convertDataToFieldType(value, fieldName, entity);
                        return ((Specification<T>) le(fieldName, fieldValue, entity)).toPredicate(root, query, cb);
                    };
                case "$like":
                    return (root, query, cb, fieldName) -> {
                        return cb.like(getPath(root, fieldName), (String) value);
                    };
                case "$notLike":
                    return (root, query, cb, fieldName) -> {
                        return cb.notLike(getPath(root, fieldName), (String) value);
                    };
                case "$not":
                    if(value == null) {
                        return (root, query, cb, fieldName) -> {
                            return cb.isNotNull(getPath(root, fieldName));
                        };
                    } else {
                        return (root, query, cb, fieldName) -> {
                            return cb.notEqual(getPath(root, fieldName), value);
                        };
                    }
                default:
                    return (root, query, cb, fieldName) -> {
                        Expression<Boolean> t = cb.literal(true);
                        return cb.or(cb.equal(t, false));
                    };
            }
        } else {
            if (!(value instanceof QueryItem)) {
                Object fieldValue = convertDataToFieldType(value, field, entity);
                if (fieldValue == null)
                    return (root, query, cb, fieldName) -> cb.isNull(getPath(root, field));
                else
                    return (root, query, cb, fieldName) -> cb.equal(getPath(root, field), fieldValue);
            } else {
                // value is QueryItem
                QueryItem newObject = (QueryItem) value;
                return (root, query, cb, fieldName) -> {
                    return ((SpecificationCustom<T>) specificationParse(newObject.getName(), newObject.getValue(),
                            entity)).toPredicate(root, query, cb, field);
                };
            }
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

    public static Object convertDataToFieldType(Object data, String field, Class entity) {
        Field objectField;
        if (data == null || data.equals("null"))
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
                    return parseDate((String) data);
                case "UUID":
                    return UUID.fromString((String) data);
                case "Boolean" :
                    return Boolean.parseBoolean((String)data);
                case "Integer": 
                    return Integer.parseInt((String)data);
                default:
                    return data;
            }
        } catch (NoSuchFieldException | SecurityException | ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Date parseDate(String data) throws ParseException {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data);
        } catch (Exception ex) {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(data);
        }
        return date;
    }
}