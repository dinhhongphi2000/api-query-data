package org.apiquery.shared.data;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apiquery.shared.utils.QueryFilterDeserialize;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class QueryAdvanceFilterArgumentResolver implements HandlerMethodArgumentResolver {
    Logger Logger = LogManager.getLogger(QueryFilterArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(QueryAdvanceFilter.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        GsonBuilder gsonBldr = new GsonBuilder();
        gsonBldr.registerTypeAdapter(QueryItem.class, new QueryFilterDeserialize());
        QueryItem filter = gsonBldr.create().fromJson(httpRequest.getReader(), QueryItem.class);
        QueryAdvanceFilter queryAdvance = new QueryAdvanceFilter();
        queryAdvance.setFilter(filter);
        return queryAdvance;
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
}