package org.apiquery.shared.exceptions;

public class ServiceException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private String code;
    private Object[] data;
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String code, Object[] data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public Object[] getData() {

        return this.data;
    }
}
