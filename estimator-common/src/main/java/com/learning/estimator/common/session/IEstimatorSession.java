package com.learning.estimator.common.session;

/**
 * Provides basic session operations
 *
 * @author rolea
 */
public interface IEstimatorSession {

    Object getDataFromSession(String key);

    void storeDataIntoSession(String key, Object value);

}
