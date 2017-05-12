package com.learning.estimator.persistencesd.service.impl.persistence;

import com.learning.estimator.common.exceptions.persistence.EntityNotFoundException;
import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.persistence.service.server.IProjectServiceServerSide;
import com.learning.estimator.persistencesd.repositories.persistence.IProjectRepository;
import com.learning.estimator.persistencesd.repositories.persistence.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation for project persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class ProjectService implements IProjectServiceServerSide {

    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IUserRepository userRepository;

    @Override
    @WithPersistenceTryCatch
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    @WithPersistenceTryCatch
    public Project findProject(Long id) {
        Project project = projectRepository.findProjectByProjectId(id);
        if (project == null)
            throw new EntityNotFoundException("The project with id " + id + " was not found");
        return project;
    }

    @Override
    @WithPersistenceTryCatch
    public Project findProjectByName(String name) {
        Project project = projectRepository.findProjectByProjectName(name);
        if (project == null)
            throw new EntityNotFoundException("The project with name " + name + " was not found");
        return project;
    }

    @Override
    @WithPersistenceTryCatch
    public List<Project> findAllProjects() {
        return projectRepository.findAllProjects();
    }

    @Override
    @WithPersistenceTryCatch
    public List<Project> findProjects(int pageIndex, int pageSize) {
        return projectRepository.findProjects(new PageRequest(pageIndex, pageSize)).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteProject(Long id) {
        projectRepository.delete(id);
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllProjects() {
        projectRepository.deleteAll();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countProjects() {
        return projectRepository.count();
    }

    /**
     * Users have an associated set of user groups
     * Projects have an associated user group
     * Returns the available projects for a user given by its id ( returns the projects for which the associated user group contains the user given by id )
     */
    @Override
    @WithPersistenceTryCatch
    public List<Project> findAllProjectsForUser(Long userId) {
        return projectRepository.findAllProjectsForGroups(userRepository.findUserByUserId(userId).getGroups());
    }

}
