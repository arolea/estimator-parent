package com.learning.estimator.common.exceptions.persistence;

import java.io.Serializable;

/**
 * Exception thrown when an operation is attempted on an entity in inconsistent state
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public class EntityInConflictingStateException extends RuntimeException implements Serializable {

    public EntityInConflictingStateException(String message) {
        super(message);
    }

}
