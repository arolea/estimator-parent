package com.learning.estimator.persistencesd.exceptions;

import com.learning.estimator.common.exceptions.persistence.EntityInConflictingStateException;
import com.learning.estimator.common.exceptions.persistence.EntityNotFoundException;
import com.learning.estimator.common.exceptions.persistence.PersistenceException;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;

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
        } catch (EntityNotFoundException e) {
            LOG.error(e);
            throw e;
        } catch (EmptyResultDataAccessException e) {
            LOG.error(e);
            throw new EntityNotFoundException(e.getMessage());
        } catch (ObjectOptimisticLockingFailureException | DataIntegrityViolationException | ConstraintViolationException e) {
            LOG.error(e);
            throw new EntityInConflictingStateException(e.getMessage());
        } catch (Exception e) {
            LOG.error(e);
            throw new PersistenceException("A persistence exception has occured.");
        }
    }
}
