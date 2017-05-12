package com.learning.estimator.common.exceptions.persistence;

import java.io.Serializable;

/**
 * Persistence related exceptions are converted to this exception
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public class PersistenceException extends RuntimeException implements Serializable {

    public PersistenceException(String message) {
        super(message);
    }

}
