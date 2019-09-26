package com.huangliang.cloudpushportal.aspect;

import com.huangliang.api.constants.CommonConsts;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
@Component
@Aspect
public class LogAdvice {

    @Pointcut("@annotation(com.huangliang.api.annotation.LogOperate)")   //切点
    public void log(){

    }

    @Around(value = "log()")       //环绕增强，切点为log这个切点
    public Object around(ProceedingJoinPoint point) throws Throwable {     //这里使用参数为ProceedingJoinPoint 类型，只有环绕增强可以使用，并且在方法中必须执行proceed方法，否则被增强的方法不会执行
        Long start = System.currentTimeMillis();
        Object result = point.proceed();
        Long end = System.currentTimeMillis();
        Method method = ((MethodSignature)point.getSignature()).getMethod();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        log.info("调用{},耗时[{}ms],参数[{}]",requestMapping.value(),end-start,generateParam(method,point.getArgs()));
        return result;
    }

    private String generateParam(Method method, Object[] args) {
        if(method.getParameterCount()<=0){
            return "";
        }
        StringBuffer result = new StringBuffer();
        Parameter[] arg = method.getParameters();
        for(int i =0 ;i < arg.length ; i++){
            //键值对
            result.append(args[i].toString()).append(CommonConsts.COMMA_FLAG);
        }
        return result.substring(0,result.length()-1);
    }

    @Before(value = "log()")            //除了环绕增强，其他使用的是joinPoint 类型
    public void before(JoinPoint point) throws Throwable {
        //System.out.println("before exec");
    }

    @After(value = "log()")
    public void after(JoinPoint point) throws Throwable {
        //System.out.println("after exec");
    }

    @AfterThrowing("log()")
    public void afterThrowing(JoinPoint point){
        //System.out.println("after throw");
    }
}
