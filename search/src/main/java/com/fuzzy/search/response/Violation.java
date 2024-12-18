package com.fuzzy.search.response;

import java.io.Serializable;

public class Violation implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6034779650623534019L;

    private String field;

    private String message;

    public Violation() {
    }

    public Violation(String field, String message) {
        super();
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
