package com.learning.estimator.persistence.service.server;

import com.learning.estimator.persistence.service.client.ITaskServiceClientSide;

/**
 * Defines additional operations available only on server side for tasks
 *
 * @author rolea
 */
public interface ITaskServiceServerSide extends ITaskServiceClientSide {

    void deleteAllTasksByUserId(Long userId);

}
