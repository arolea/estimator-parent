package com.learning.estimator.persistencesd.repositories.persistence;

import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.TaskStatus;
import com.learning.estimator.persistencesd.repositories.persistence.utils.ITaskRepositoryHelper;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
/*
 * Fetch -> All attributes that are not specified in the entity graph will be treated as lazy
 * Load -> All attributes that are not specified in the entity graph will be treated with their associated values
 */

/**
 * Task repository
 *
 * @author rolea
 */
public interface ITaskRepository extends JpaRepository<Task, Long>, QueryDslPredicateExecutor<Task>, ITaskRepositoryHelper {

    @EntityGraph(value = "graph.Task.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Task m where m.taskId = :id")
    Task findTaskByTaskId(@Param("id") Long id);

    @EntityGraph(value = "graph.Task.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Task m")
    List<Task> findAllTasks();

    @EntityGraph(value = "graph.Task.fetchPolicy", type = EntityGraphType.LOAD)
    List<Task> findAll(Predicate predicate);

    @EntityGraph(value = "graph.Task.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Task m where m.user.userId = :userId")
    List<Task> findAllByUser(@Param("userId") Long usreId);

    @EntityGraph(value = "graph.Task.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Task m where m.project.projectId = :projectId")
    List<Task> findAllByProject(@Param("projectId") Long projectId);

    @EntityGraph(value = "graph.Task.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Task m where m.taskStatus = :status")
    List<Task> findAllByTaskStatus(@Param("status") TaskStatus status);

    @EntityGraph(value = "graph.Task.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Task m")
    Page<Task> findTasks(Pageable page);

    @EntityGraph(value = "graph.Task.fetchPolicy", type = EntityGraphType.LOAD)
    @Query(value = "SELECT m FROM Task m where m.user.userId = :userId",
            countQuery = "SELECT count(*) FROM Task m WHERE m.user.userId = :userId")
    Page<Task> findTasksByUser(Pageable page, @Param("userId") Long userId);

    @EntityGraph(value = "graph.Task.fetchPolicy", type = EntityGraphType.LOAD)
    @Query(value = "SELECT m FROM Task m where m.project.projectId = :projectId",
            countQuery = "SELECT count(*) FROM Task m WHERE m.project.projectId = :projectId")
    Page<Task> findTasksByProject(Pageable page, @Param("projectId") Long projectId);

    @EntityGraph(value = "graph.Task.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM Task m where m.taskStatus = :status")
    Page<Task> findTasksByStatus(Pageable page, @Param("status") TaskStatus status);

    Long countByUserUserId(Long userId);

    Long countByProjectProjectId(Long projectId);

    Long countByTaskStatus(TaskStatus status);

    void deleteAllByUserUserId(Long userId);

}
