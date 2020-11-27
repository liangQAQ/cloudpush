package com.huangliang.api.exception;

import lombok.Data;

@Data
public class NetException extends Exception{

    private Integer code;

    private String message;

    private Exception exception;

    public NetException(String message) {
        this.message = message;
    }

    public NetException(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
    public NetException(Integer code, Exception exception) {
        super();
        this.code = code;
        this.exception = exception;
    }
    public NetException(String message, Exception exception) {
        super();
        this.message = message;
        this.exception = exception;
    }
}
