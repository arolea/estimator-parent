package com.learning.estimator.persistencesd.utils;

import com.learning.estimator.model.statistics.QVelocityPointFact;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;

/**
 * Predicate builder for velocity points
 *
 * @author rolea
 */
public class VelocityPointPredicateBuilder {

    public static final Predicate build(Long userId, Long projectId, Long groupId, LocalDate beginDate, LocalDate endDate) {
        BooleanExpression predicate = null;

        if (userId != null) {
            predicate = predicate == null ? QVelocityPointFact.velocityPointFact.user.userId.eq(userId) :
                    predicate.and(QVelocityPointFact.velocityPointFact.user.userId.eq(userId));
        }

        if (projectId != null) {
            predicate = predicate == null ? QVelocityPointFact.velocityPointFact.project.projectId.eq(projectId) :
                    predicate.and(QVelocityPointFact.velocityPointFact.project.projectId.eq(projectId));
        }

        if (groupId != null) {
            predicate = predicate == null ? QVelocityPointFact.velocityPointFact.group.groupId.eq(groupId) :
                    predicate.and(QVelocityPointFact.velocityPointFact.group.groupId.eq(groupId));
        }

        predicate = predicate == null ? QVelocityPointFact.velocityPointFact.time.date.between(beginDate != null ? beginDate : LocalDate.MIN, endDate != null ? endDate : LocalDate.MAX) :
                predicate.and(QVelocityPointFact.velocityPointFact.time.date.between(beginDate != null ? beginDate : LocalDate.MIN, endDate != null ? endDate : LocalDate.MAX));

        return predicate;
    }

}
