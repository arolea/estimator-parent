package com.learning.estimator.common.exceptions.persistence;

import java.io.Serializable;

/**
 * Exception that is thrown when an entity is assumed present but is not found
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public class EntityNotFoundException extends RuntimeException implements Serializable {

    public EntityNotFoundException(String message) {
        super(message);
    }

}
