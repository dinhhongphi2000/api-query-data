package org.apiquery.shared.enums;

public enum ResponseStatusCode {
    SUCCESS(1, "success"),
    ERROR(2, "error");

    private Integer code;
    private String name;

    private ResponseStatusCode(Integer code, String name) {
        this.code= code;
        this.name = name;
    }

    public static ResponseStatusCode getRoleByCode(Integer code)  {
        for (ResponseStatusCode result : ResponseStatusCode.values()) {
            if (result.code.equals(code)) {
                return result;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
