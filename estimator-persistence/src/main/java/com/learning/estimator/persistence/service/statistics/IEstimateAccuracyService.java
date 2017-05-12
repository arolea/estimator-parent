package com.learning.estimator.persistence.service.statistics;

import com.learning.estimator.model.statistics.EstimateAccuracyFact;

import java.time.LocalDate;
import java.util.List;

/**
 * Defines persistence operations on estimate accuracy facts
 *
 * @author rolea
 */
public interface IEstimateAccuracyService {

    EstimateAccuracyFact saveEstimateAccuracy(EstimateAccuracyFact fact);

    EstimateAccuracyFact findEstimateAccuracy(Long id);

    List<EstimateAccuracyFact> findAllEstimateAccuracies();

    List<EstimateAccuracyFact> findAllEstimateAccuraciesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime);

    List<EstimateAccuracyFact> findEstimateAccuracies(int pageIndex, int pageSize);

    List<EstimateAccuracyFact> findEstimateAccuraciesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime, int pageIndex, int pageSize);

    void deleteEstimateAccuracy(Long id);

    void deleteAllEstimateAccuracies();

    Long countEstimateAccuracies();

    Long countEstimateAccuraciesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime);

}
