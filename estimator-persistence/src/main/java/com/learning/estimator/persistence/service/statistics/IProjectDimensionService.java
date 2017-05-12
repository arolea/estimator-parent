package com.learning.estimator.persistence.service.statistics;

import com.learning.estimator.model.statistics.ProjectDimension;

import java.util.List;

/**
 * Defines persistence operations on project dimensions
 *
 * @author rolea
 */
public interface IProjectDimensionService {

    ProjectDimension saveProject(ProjectDimension projectDimension);

    ProjectDimension findProject(Long id);

    ProjectDimension findProjectByName(String projectName);

    List<ProjectDimension> findAllProjects();

    List<ProjectDimension> findProjects(int pageIndex, int pageSize);

    void deleteProject(Long id);

    void deleteAllprojects();

    Long countProjects();

}
