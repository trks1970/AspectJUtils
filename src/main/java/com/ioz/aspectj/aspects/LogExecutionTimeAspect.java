package com.ioz.aspectj.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogExecutionTimeAspect
{
	private static final Logger STAT = LoggerFactory.getLogger( "execution.STATS" );

	//@Around("execution(* *(..)) && @annotation(com.ioz.aspectj.annotations.LogExecutionTime) && args(logExecutionTime)")
	@SuppressAjWarnings("adviceDidNotMatch")
	@Around("execution(* *(..)) && @annotation(com.ioz.aspectj.annotations.LogExecutionTime)")
	public Object logExecutionTime( ProceedingJoinPoint joinPoint/*, LogExecutionTime logExecutionTime*/ ) throws Throwable
	{
		final long start = System.currentTimeMillis();

		final Object proceed = joinPoint.proceed();
		
		final long executionTime = System.currentTimeMillis() - start;
		
		/*Logger log = STAT;
		if( logExecutionTime.useClassLogger() )
		{
			log = LoggerFactory.getLogger( joinPoint.getTarget().getClass() );
		}*/
		STAT.info( joinPoint.getSignature() + " executed in " + executionTime + "ms" );
		return proceed;
	}

}
