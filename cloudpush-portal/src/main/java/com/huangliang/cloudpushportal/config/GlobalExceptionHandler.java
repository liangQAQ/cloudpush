package com.huangliang.cloudpushportal.config;

import com.huangliang.api.entity.response.Message;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Message defaultErrorHandler(HttpServletRequest request, Exception e)  {
        //打印异常信息
        e.printStackTrace();
        System.out.println("GlobalDefaultExceptionHandler.defaultErrorHandler()");
        return new Message("false",e.getMessage());
    }
}
