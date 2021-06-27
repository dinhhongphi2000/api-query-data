package org.apiquery.shared.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryInclude {
    private List<String> fields;

    public String[] getFields() {
        if(this.fields.size() <= 0)
            return null;
        else
            return this.fields.toArray(new String[0]);
    }

    public void setFields(String[] fields) {
        this.fields = new ArrayList<>();
        this.fields.addAll(Arrays.asList(fields));
    }

    public QueryInclude() {
        this.fields = new ArrayList<>();
    }

    public QueryInclude(String[] fields) {
        this.fields = new ArrayList<>();
        this.fields.addAll(Arrays.asList(fields));
    }

    public void insertField(String field) {
        if(this.fields == null) {
            this.fields = new ArrayList<>();
            this.fields.add(field);
        }else if(!this.fields.contains(field)) {
            this.fields.add(field);
        }
    }
}