package com.learning.estimator.datasplitter.service;

import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserGroup;

/**
 * Defines data splitter operations
 *
 * @author rolea
 */
public interface IDataSplitter {

    /**
     * Extracts a project dimension from a newly created project entity
     */
    void processProject(Project project);

    /**
     * Extracts an user dimension from a newly created user entity
     */
    void processUser(User user);

    /**
     * Extracts a group dimension from a newly created group entity
     */
    void processGroup(UserGroup group);

    /**
     * Extracts an estimate accuracy fact , logged time fact and velocity point fact from a newly completed task
     */
    void processTask(Task task);

}
