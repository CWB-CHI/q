package com.chi.question.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Before("execution(* com.chi.question.controller..*.*(..))")
	public void before(JoinPoint joinPoint) {
		Class<?> aClass = joinPoint.getTarget().getClass();
		logger.info("get in aspect before " + aClass.getName());
	}

	@After("execution(* com.chi.question.controller..*.*(..))")
	public void after(JoinPoint joinPoint) {
		logger.info("get in aspect after");
	}


//	@Before("execution(* com.chi.question.controller..*(..))")
//	public void before(JoinPoint joinPoint) {
//		System.out.println("[Aspect1] before advise");
//	}
//
//	@Around("execution(* com.chi.question.controller..*(..))")
//	public void around(ProceedingJoinPoint pjp) throws  Throwable{
//		System.out.println("[Aspect1] around advise 1");
//		pjp.proceed();
//		System.out.println("[Aspect1] around advise2");
//	}
//
//	@AfterReturning("execution(* com.chi.question.controller..*(..))")
//	public void afterReturning(JoinPoint joinPoint) {
//		System.out.println("[Aspect1] afterReturning advise");
//	}
//
//	@AfterThrowing("execution(* com.chi.question.controller..*(..))")
//	public void afterThrowing(JoinPoint joinPoint) {
//		System.out.println("[Aspect1] afterThrowing advise");
//	}
//
//	@After("execution(* com.chi.question.controller..*(..))")
//	public void after(JoinPoint joinPoint) {
//		System.out.println("[Aspect1] after advise");
//	}
}
