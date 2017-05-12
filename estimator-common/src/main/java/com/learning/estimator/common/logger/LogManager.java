package com.learning.estimator.common.logger;

/**
 * Class used to instantiate loggers
 *
 * @author rolea
 */
public final class LogManager {

    public static final ILogger getLogger(Class<?> clazz) {
        return new Slf4jLogger(clazz);
    }

}
