package com.toy.thecommerce.global.aop;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LogAspect {

  @Pointcut("execution(* com.toy.thecommerce..*(..))")
  public void all() {

  }
  @Pointcut("execution(* com.toy.thecommerce..*Controller.*(..))")
  public void controller() {

  }

  @Pointcut("execution(* com.toy.thecommerce..*Service.*(..))")
  public void service() {

  }

  @Around("all()")
  public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    try {
      return joinPoint.proceed();
    } finally {
      long finish = System.currentTimeMillis();
      long timeMs = finish -start;
      log.info("log = {}", joinPoint.getSignature());
      log.info("timeMs = {}", timeMs);
    }
  }

  @Before("controller() || service()")
  public void beforeLogic(JoinPoint joinPoint) throws Throwable {
    logMethodName(joinPoint);
    logParameterTypeAndValue(joinPoint);
  }

  @After("controller() || service()")
  public void afterLogic(JoinPoint joinPoint) throws Throwable {
    logMethodName(joinPoint);
    logParameterTypeAndValue(joinPoint);
  }

  private void logMethodName(JoinPoint joinPoint) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Method method = methodSignature.getMethod();
    log.info("method = {}", method.getName());
  }

  private void logParameterTypeAndValue(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    for (Object arg : args) {
      if (arg != null) {
        log.info("type = {}", arg.getClass().getSimpleName());
        log.info("value = {}", arg);
      }
    }
  }

}
