package org.apiquery.dtos;

import java.io.Serializable;

public class ResponseStatusDTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String status;
    private Object results;
    private String message ; //exists when status = error
    private String statuscode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }
}
