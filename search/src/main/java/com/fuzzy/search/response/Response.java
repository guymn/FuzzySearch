package com.fuzzy.search.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6883795561685913676L;

    private int status;

    private String code;

    private String error;

    private String message;

    private transient Object payload;

    private transient List<Object> payloads;

    private List<Violation> violations;

    private Date timestamp;

    private String path;

    private long timeTaken;

    public Response() {
    }

    public Response(int status, String code, String error, String message, Object payload, List<Object> payloads,
            List<Violation> violations, Date timestamp, String path) {
        this.status = status;
        this.code = code;
        this.error = error;
        this.message = message;
        this.payload = payload;
        this.payloads = payloads;
        this.violations = violations;
        this.timestamp = timestamp;
        this.path = path;
    }

    public Response(int status, Object payload, Date timestamp, String path) {
        this.status = status;
        this.payload = payload;
        this.timestamp = timestamp;
        this.path = path;
    }

}
