package com.learning.estimator.persistencesd.service.impl.statistics;

import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.statistics.ProjectDimension;
import com.learning.estimator.persistence.service.statistics.IProjectDimensionService;
import com.learning.estimator.persistencesd.repositories.statistics.IProjectDimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation for project dimensions persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class ProjectDimensionService implements IProjectDimensionService {

    @Autowired
    private IProjectDimensionRepository repository;

    @Override
    @WithPersistenceTryCatch
    public ProjectDimension saveProject(ProjectDimension projectDimension) {
        return repository.save(projectDimension);
    }

    @Override
    @WithPersistenceTryCatch
    public ProjectDimension findProject(Long id) {
        return repository.findOne(id);
    }

    @Override
    @WithPersistenceTryCatch
    public ProjectDimension findProjectByName(String projectName) {
        return repository.findByProjectName(projectName);
    }

    @Override
    @WithPersistenceTryCatch
    public List<ProjectDimension> findAllProjects() {
        return repository.findAll();
    }

    @Override
    @WithPersistenceTryCatch
    public List<ProjectDimension> findProjects(int pageIndex, int pageSize) {
        return repository.findAll(new PageRequest(pageIndex, pageSize)).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteProject(Long id) {
        repository.delete(id);
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllprojects() {
        repository.deleteAll();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countProjects() {
        return repository.count();
    }

}
