package com.learning.estimator.persistencesd.utils;

import com.learning.estimator.model.statistics.QEstimateAccuracyFact;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;

/**
 * Predicate builder for estimate accuracies
 *
 * @author rolea
 */
public class EstimateAccuracyPredicateBuilder {

    public static final Predicate build(Long userId, Long projectId, Long groupId, LocalDate beginDate, LocalDate endDate) {
        BooleanExpression predicate = null;

        if (userId != null) {
            predicate = predicate == null ? QEstimateAccuracyFact.estimateAccuracyFact.user.userId.eq(userId) :
                    predicate.and(QEstimateAccuracyFact.estimateAccuracyFact.user.userId.eq(userId));
        }

        if (projectId != null) {
            predicate = predicate == null ? QEstimateAccuracyFact.estimateAccuracyFact.project.projectId.eq(projectId) :
                    predicate.and(QEstimateAccuracyFact.estimateAccuracyFact.project.projectId.eq(projectId));
        }

        if (groupId != null) {
            predicate = predicate == null ? QEstimateAccuracyFact.estimateAccuracyFact.group.groupId.eq(groupId) :
                    predicate.and(QEstimateAccuracyFact.estimateAccuracyFact.group.groupId.eq(groupId));
        }

        predicate = predicate == null ? QEstimateAccuracyFact.estimateAccuracyFact.time.date.between(beginDate != null ? beginDate : LocalDate.MIN, endDate != null ? endDate : LocalDate.MAX) :
                predicate.and(QEstimateAccuracyFact.estimateAccuracyFact.time.date.between(beginDate != null ? beginDate : LocalDate.MIN, endDate != null ? endDate : LocalDate.MAX));

        return predicate;
    }

}
