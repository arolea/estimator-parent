package com.learning.estimator.persistence.service.client;

import com.learning.estimator.model.entities.Project;

import java.util.List;

/**
 * Defines client side persistence operations for project entities
 *
 * @author rolea
 */
public interface IProjectServiceClientSide {

    Project saveProject(Project project);

    Project findProject(Long id);

    Project findProjectByName(String name);

    List<Project> findAllProjects();

    List<Project> findProjects(int pageIndex, int pageSize);

    void deleteProject(Long id);

    void deleteAllProjects();

    Long countProjects();

}
