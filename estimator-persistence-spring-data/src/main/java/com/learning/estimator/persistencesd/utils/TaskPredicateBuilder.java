package com.learning.estimator.persistencesd.utils;

import com.learning.estimator.model.entities.QTask;
import com.learning.estimator.model.entities.TaskStatus;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * Predicate builder for tasks
 *
 * @author rolea
 */
public class TaskPredicateBuilder {

    public static final Predicate build(Long userId, Long projectId, TaskStatus status) {
        BooleanExpression predicate = null;

        if (userId != null) {
            predicate = predicate == null ? QTask.task.user.userId.eq(userId) : predicate.and(QTask.task.user.userId.eq(userId));
        }

        if (projectId != null) {
            predicate = predicate == null ? QTask.task.project.projectId.eq(projectId) : predicate.and(QTask.task.project.projectId.eq(projectId));
        }

        if (status != null) {
            predicate = predicate == null ? QTask.task.taskStatus.eq(status) : predicate.and(QTask.task.taskStatus.eq(status));
        }

        return predicate;
    }

}
