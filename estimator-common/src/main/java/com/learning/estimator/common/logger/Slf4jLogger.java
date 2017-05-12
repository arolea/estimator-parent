package com.learning.estimator.common.logger;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SLF4J implementation for logger
 *
 * @author rolea
 */
public class Slf4jLogger implements ILogger {

    private Logger logger;

    public Slf4jLogger(Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void info(String message) {
        if (logger.isInfoEnabled())
            logger.info(message);
    }

    @Override
    public void warn(String message) {
        if (logger.isWarnEnabled())
            logger.info(message);
    }

    @Override
    public void error(Throwable t) {
        if (logger.isErrorEnabled())
            logger.error("Error detected", t);
    }

    @Override
    public void error(String message, Throwable t) {
        if (logger.isErrorEnabled())
            logger.error(message, t);
    }

    @Override
    public void trace(String message) {
        if (logger.isTraceEnabled())
            logger.trace(message);
    }

    @Override
    public void debug(String message) {
        if (logger.isDebugEnabled())
            logger.debug(message);
    }

}
