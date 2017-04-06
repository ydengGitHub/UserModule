package com.usermodule.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class RequestMonitor {
	private static final Logger logger = LoggerFactory.getLogger(RequestMonitor.class);
	
	/*Advice method, runs either before, after or surrounding business function*/
	/*Surrounding advice, add a PointCut as parameter*/
	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object wrap(ProceedingJoinPoint pjp) throws Throwable{
		logger.info("Before controller method "+pjp.getSignature().getName() + ". Thread "+Thread.currentThread().getName());
		Object retVal=pjp.proceed();
		logger.info("Controller method "+ pjp.getSignature().getName()+" execution successful");
		return retVal;
	}
	
}
