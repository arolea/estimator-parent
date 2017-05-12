package com.learning.estimator.persistenceclient.exceptions;

import com.learning.estimator.common.exceptions.persistence.PersistenceException;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Implements common persistence error handling logic
 *
 * @author rolea
 */
@Component
@Aspect
public class ExceptionHandlingAdvice {

    private static final ILogger LOG = LogManager.getLogger(ExceptionHandlingAdvice.class);

    @Around("@annotation(com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch) && execution(* *(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnObject = null;
        try {
            returnObject = joinPoint.proceed();
            return returnObject;
        } catch (Exception e) {
            LOG.error(e);
            throw new PersistenceException(e.getMessage());
        }
    }
}
