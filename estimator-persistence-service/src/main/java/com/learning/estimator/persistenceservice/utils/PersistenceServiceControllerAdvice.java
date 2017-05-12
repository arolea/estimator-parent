package com.learning.estimator.persistenceservice.utils;

import com.learning.estimator.common.exceptions.persistence.EntityInConflictingStateException;
import com.learning.estimator.common.exceptions.persistence.EntityNotFoundException;
import com.learning.estimator.common.exceptions.persistence.PersistenceException;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Defines common error handling logic
 *
 * @author rolea
 */
@ControllerAdvice
public class PersistenceServiceControllerAdvice {

    private static final ILogger LOG = LogManager.getLogger(PersistenceServiceControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    //application exceptions are considered server errors
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleAppException(Exception exception) {
        LOG.error(exception);
        return "An application error has occured : " + exception.getMessage();
    }

    @ExceptionHandler(PersistenceException.class)
    //general persistence exceptions are considered client errors
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handlePersistenceException(Exception exception) {
        LOG.error(exception);
        return "A persistence error has occured : " + exception.getMessage();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFound(Exception exception) {
        LOG.error(exception);
        return "The entity was not found : " + exception.getMessage();
    }

    @ExceptionHandler(EntityInConflictingStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public String handleConflictingState(Exception exception) {
        LOG.error(exception);
        return "The entity is in onconsistent state : " + exception.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    //validation exceptions are considered client errors
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleValidationException(Exception exception) {
        LOG.error(exception);
        return "A validation error has occured : " + exception.getMessage();
    }

}
