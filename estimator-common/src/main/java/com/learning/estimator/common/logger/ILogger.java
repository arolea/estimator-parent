package com.learning.estimator.common.logger;

/**
 * Defines logging utilities
 *
 * @author rolea
 */
public interface ILogger {

    void trace(String message);

    void debug(String message);

    void info(String message);

    void warn(String message);

    void error(Throwable t);

    void error(String message, Throwable t);

}
