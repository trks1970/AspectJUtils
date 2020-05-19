package com.ioz.aspectj.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import com.ioz.aspectj.Profiler;

@Aspect
public class ProfileExecutionAspect
{
	@SuppressAjWarnings( "adviceDidNotMatch" )
	@Around( "execution(* *(..)) && @annotation(com.ioz.aspectj.annotations.LogExecutionTime)" )
	public Object profile( ProceedingJoinPoint joinPoint/*, LogExecutionTime logExecutionTime*/ ) throws Throwable
	{
		final long start = System.currentTimeMillis();
		final Object proceed = joinPoint.proceed();
		final long executionTime = System.currentTimeMillis() - start;
		Profiler.getProfiler().addExecution( joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(), executionTime );
		return proceed;
	}
}
