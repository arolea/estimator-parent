package com.learning.estimator.persistencesd.repositories.persistence;

import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Project repository
 *
 * @author rolea
 */
public interface IProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(value = "graph.Project.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Project m where m.projectId = :id")
    Project findProjectByProjectId(@Param("id") Long id);

    @EntityGraph(value = "graph.Project.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Project m where m.projectName = :projectName")
    Project findProjectByProjectName(@Param("projectName") String username);

    @EntityGraph(value = "graph.Project.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Project m")
    List<Project> findAllProjects();

    @EntityGraph(value = "graph.Project.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Project m")
    Page<Project> findProjects(Pageable page);

    @EntityGraph(value = "graph.Project.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Project m where m.userGroup in :groups")
    List<Project> findAllProjectsForGroups(@Param("groups") Set<UserGroup> groups);

}
