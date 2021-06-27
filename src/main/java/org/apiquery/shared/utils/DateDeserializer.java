package org.apiquery.shared.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateDeserializer extends StdDeserializer<Date> {
    private static final long serialVersionUID = 1L;

    public DateDeserializer() {
        this(null);
    }

    public DateDeserializer(Class<Date> t) {
        super(t);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = p.getText();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.parse(date);
        } catch (ParseException e) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                return formatter.parse(date);
            } catch (ParseException e2) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                    return formatter.parse(date);
                } catch (ParseException e3) {
                }
            }
        }
        return null;
    }

}