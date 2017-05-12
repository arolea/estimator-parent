package com.learning.estimator.persistence.facade.server;

import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.learning.estimator.persistence.service.server.IProjectServiceServerSide;
import com.learning.estimator.persistence.service.server.ITaskServiceServerSide;
import com.learning.estimator.persistence.service.server.IUserGroupServiceServerSide;
import com.learning.estimator.persistence.service.server.IUserServiceServerSide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Facade for server side persistence operations
 *
 * @author rolea
 */
@Component
public class PersistenceFacadeServerSide extends PersistenceFacadeClientSide implements ITaskServiceServerSide, IUserServiceServerSide, IUserGroupServiceServerSide, IProjectServiceServerSide {

    @Autowired
    private ITaskServiceServerSide taskService;
    @Autowired
    private IProjectServiceServerSide projectService;
    @Autowired
    private IUserGroupServiceServerSide groupService;

    @Override
    public void deleteAllTasksByUserId(Long userId) {
        taskService.deleteAllTasksByUserId(userId);
    }

    @Override
    public List<Project> findAllProjectsForUser(Long userId) {
        return projectService.findAllProjectsForUser(userId);
    }

    @Override
    public Set<UserGroup> findAllGroupsForUser(Long userId) {
        return groupService.findAllGroupsForUser(userId);
    }

}
