package com.learning.estimator.persistencesd.repositories.statistics;

import com.learning.estimator.model.statistics.ProjectDimension;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Project dimension repository
 *
 * @author rolea
 */
public interface IProjectDimensionRepository extends JpaRepository<ProjectDimension, Long> {

    ProjectDimension findByProjectName(String projectName);

}
