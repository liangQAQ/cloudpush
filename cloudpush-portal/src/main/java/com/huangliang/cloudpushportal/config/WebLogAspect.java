package com.huangliang.cloudpushportal.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
//@Component
@Slf4j
public class WebLogAspect {

    @Pointcut("execution(public * com.huangliang.cloudpushportal..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        System.out.println("我是前置通知");
        Object[] obj = joinPoint.getArgs();//获取目标方法的参数信息
        joinPoint.getThis(); // AOP代理类信息
        joinPoint.getTarget(); // 代理的目标对象
        Signature signature = joinPoint.getSignature(); //  用的最多，通知的签名
        System.out.println("代理的方法是 ： " + signature.getName()); //  打印 代理的是哪一个方法
        // AOP 代理的名字
        System.out.println("AOP 代理的名字 ： " + signature.getDeclaringTypeName());
        signature.getDeclaringType();

    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        log.info("RESPONSE : " + ret);
    }

}
