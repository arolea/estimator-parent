package com.learning.estimator.persistence.service.server;

import com.learning.estimator.model.entities.Project;
import com.learning.estimator.persistence.service.client.IProjectServiceClientSide;

import java.util.List;

/**
 * Defines additional operations available only on server side for projects
 *
 * @author rolea
 */
public interface IProjectServiceServerSide extends IProjectServiceClientSide {

    List<Project> findAllProjectsForUser(Long userId);

}
