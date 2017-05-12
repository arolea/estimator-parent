package com.learning.estimator.common.exceptions.statistics;

import java.io.Serializable;

/**
 * Statistics related exceptions are converted to this exception
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public class StatisticsException extends RuntimeException implements Serializable {

    public StatisticsException(String message) {
        super(message);
    }

}
