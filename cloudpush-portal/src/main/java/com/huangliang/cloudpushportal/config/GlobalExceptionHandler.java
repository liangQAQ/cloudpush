package com.huangliang.cloudpushportal.config;

import com.huangliang.api.constants.CommonConsts;
import com.huangliang.api.entity.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response defaultErrorHandler(Exception e)  {
        log.error(e.getMessage(),e);
        return new Response(CommonConsts.ERROR,e.getMessage());
    }
}
