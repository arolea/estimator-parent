package com.learning.estimator.statisticsservice.utils;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import org.springframework.http.HttpStatus;
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
public class StatisticsServiceControllerAdvice {

    private static final ILogger LOG = LogManager.getLogger(StatisticsServiceControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    //application exceptions are considered server errors
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleAppException(Exception exception) {
        LOG.error(exception);
        return "An application error has occured : " + exception.getMessage();
    }

}
