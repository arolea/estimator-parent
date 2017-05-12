package com.learning.estimator.datapublisher.service;

import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserGroup;

/**
 * @author rolea
 */
public interface IDataPublisher {

    /**
     * Method invoked after saving a new project<br>
     * Will create a project entry for the new project in the statistics database<br>
     * Projects are used as a dimension in the statistics database schema<br>
     */
    void publishProject(Project project);


    /**
     * Method invoked after saving a new user<br>
     * Will create a user entry for the new user in the statistics database<br>
     * Users are used as a dimension in the statistics database schema<br>
     */
    void publishUser(User user);

    /**
     * Method invoked after saving a new user group<br>
     * Will create a user group entry for the new user group in the statistics database<br>
     * User groups are used as a dimension in the statistics database schema<br>
     */
    void publishGroup(UserGroup group);

    /**
     * Method invoked upon completing a task<br>
     * Will extract task related data for statistics purposes<br>
     * Task related data is used as facts in the statistics database<br>
     */
    void publishTask(Task task);

}
