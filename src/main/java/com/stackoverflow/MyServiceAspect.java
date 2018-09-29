package com.stackoverflow;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyServiceAspect {

    private static final Logger logger = LoggerFactory.getLogger(MyServiceAspect.class);

    @Around(value = "execution(* com.stackoverflow.MyService.doCall(..))")
    public Object rules(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object obj = joinPoint.proceed();

        logger.info("Took {} milliseconds", System.currentTimeMillis() - startTime);
        return obj;
    }

}
