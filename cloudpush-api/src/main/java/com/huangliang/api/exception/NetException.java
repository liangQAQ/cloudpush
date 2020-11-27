package com.huangliang.api.exception;

import lombok.Data;

@Data
public class NetException extends Exception{

    private Integer code;

    private String message;

    public NetException(String message) {
        this.message = message;
    }

    public NetException(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
