package org.apiquery.shared.data;

public class FieldFilter {
    private String field;
    private String value;
    private String condition;

    public FieldFilter() {
    }

    public FieldFilter(String field, String value, String condition) {
        this.field = field;
        this.value = value;
        this.condition = condition;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}