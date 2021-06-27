package org.apiquery.shared.data;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class QueryFilterArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(QueryFilter.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String filters = encodeUTF8(httpRequest.getParameter("filtersand"));
        String type = "and";
        if(filters == null || filters.isEmpty()) {
            filters = encodeUTF8(httpRequest.getParameter("filtersor"));
            type = "or";
        }
        
        if(filters == null)
                return null;
        
        String filterOptions = encodeUTF8(httpRequest.getParameter("filterOptions"));
        try {
            String[] fieldsString = getFieldsFromFilterString(filters);
            Object filterObject = saveFieldStringToObject(fieldsString, type, filterOptions);
            return filterObject;
        } catch (Exception ex) {
            return null;
        }
    }

    private String[] getFieldsFromFilterString(String filters) {
        return filters.split(",");
    }

    private QueryFilter saveFieldStringToObject(String[] fields, String type, String filterOption)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        List<FieldFilter> fieldFilters = new ArrayList<>();
        for(String item : fields) {
            String[] parts = item.split("\\$");
            if(parts.length != 3)
                continue;
            
            FieldFilter field = new FieldFilter();
            field.setField(parts[0]);
            field.setCondition(parts[1]);
            field.setValue(parts[2]);
            fieldFilters.add(field);
        }
        QueryFilterOption filterOptionObject = QueryFilterOption.createQueryFilterOption(filterOption);
        return new QueryFilter(fieldFilters, type, filterOptionObject);
    }

    private String encodeUTF8(String param) {
        if(param == null || param.isEmpty())
            return null;
        byte[] bytes = param.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}