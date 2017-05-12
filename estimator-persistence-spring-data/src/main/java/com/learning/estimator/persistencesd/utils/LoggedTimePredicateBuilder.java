package com.learning.estimator.persistencesd.utils;

import com.learning.estimator.model.statistics.QLoggedTimeFact;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;

/**
 * Predicate builder for logged times
 *
 * @author rolea
 */
public class LoggedTimePredicateBuilder {

    public static final Predicate build(Long userId, Long projectId, Long groupId, LocalDate beginDate, LocalDate endDate) {
        BooleanExpression predicate = null;

        if (userId != null) {
            predicate = predicate == null ? QLoggedTimeFact.loggedTimeFact.user.userId.eq(userId) :
                    predicate.and(QLoggedTimeFact.loggedTimeFact.user.userId.eq(userId));
        }

        if (projectId != null) {
            predicate = predicate == null ? QLoggedTimeFact.loggedTimeFact.project.projectId.eq(projectId) :
                    predicate.and(QLoggedTimeFact.loggedTimeFact.project.projectId.eq(projectId));
        }

        if (groupId != null) {
            predicate = predicate == null ? QLoggedTimeFact.loggedTimeFact.group.groupId.eq(groupId) :
                    predicate.and(QLoggedTimeFact.loggedTimeFact.group.groupId.eq(groupId));
        }

        predicate = predicate == null ? QLoggedTimeFact.loggedTimeFact.time.date.between(beginDate != null ? beginDate : LocalDate.MIN, endDate != null ? endDate : LocalDate.MAX) :
                predicate.and(QLoggedTimeFact.loggedTimeFact.time.date.between(beginDate != null ? beginDate : LocalDate.MIN, endDate != null ? endDate : LocalDate.MAX));

        return predicate;
    }

}
