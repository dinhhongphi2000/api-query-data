package org.apiquery.shared.utils;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apiquery.shared.data.QueryItem;

public class QueryFilterDeserialize implements JsonDeserializer<QueryItem> {

    @Override
    public QueryItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jObject = json.getAsJsonObject();
        JsonObject filter = jObject.get("filter").getAsJsonObject();
        Set<String> keySet = filter.keySet();
        if (keySet.size() > 1)
            return null;

        String field = keySet.iterator().next();
        QueryItem queryItem = new QueryItem();
        queryItem.setName(field);
        queryItem.setValue(getQueryItem(filter.get(field)));
        return queryItem;
    }

    private Object getQueryItem(JsonElement object) {
        if (object.isJsonObject()) {
            JsonObject jsonObject = object.getAsJsonObject();
            Set<String> keySet = jsonObject.keySet();
            if (keySet.size() > 1)
                return null;
            // parse each field in jsonObject
            QueryItem item = new QueryItem();
            // set name
            String fieldName = keySet.iterator().next();
            item.setName(fieldName);
            // set value
            JsonElement valueObject = jsonObject.get(fieldName);
            if (valueObject.isJsonArray()) {
                item.setValue(getQueryItem(valueObject));
            } else if (valueObject.isJsonObject()) {
                item.setValue(getQueryItem(valueObject));
            } else if (valueObject.isJsonNull()) {
                item.setValue(null);
            } else if (valueObject.isJsonPrimitive()) {
                item.setValue(encodeUTF8(valueObject.getAsString()));
            }
            return item;
        } else if (object.isJsonArray()) {
            JsonArray array = object.getAsJsonArray();
            Iterator<JsonElement> elementArray = array.iterator();

            JsonElement element = elementArray.next();
            if (element.isJsonPrimitive()) {
                List<String> valueList = new ArrayList<>();
                valueList.add(element.getAsString());
                while (elementArray.hasNext()) {
                    element = elementArray.next();
                    valueList.add(encodeUTF8(element.getAsString()));
                }
                return valueList;
            } else if (element.isJsonObject()) {
                List<Object> valueList = new ArrayList<>();
                valueList.add(getQueryItem(element));
                while (elementArray.hasNext()) {
                    element = elementArray.next();
                    valueList.add(getQueryItem(element));
                }
                return valueList;
            } else {
                return null;
            }
        } else if (object.isJsonNull()) {
            return null;
        } else if (object.isJsonPrimitive()) {
            return encodeUTF8(object.getAsString());
        } else {
            return null;
        }
    }

    private String encodeUTF8(String param) {
        if(param == null || param.isEmpty())
            return null;
        byte[] bytes = param.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}