package com.learning.estimator.persistence.service.statistics;

import com.learning.estimator.model.statistics.VelocityPointFact;

import java.time.LocalDate;
import java.util.List;

/**
 * Defines persistence operations on velocity point facts
 *
 * @author rolea
 */
public interface IVelocityPointService {

    VelocityPointFact saveVelocityPoint(VelocityPointFact fact);

    VelocityPointFact findVelocityPoint(Long id);

    List<VelocityPointFact> findAllVelocityPoints();

    List<VelocityPointFact> findAllVelocityPoinysByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime);

    List<VelocityPointFact> findVelocityPoints(int pageIndex, int pageSize);

    List<VelocityPointFact> findVelocityPointsByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime, int pageIndex, int pageSize);

    void deleteVelocityPoint(Long id);

    void deleteAllVelocityPoints();

    Long countVelocityPoints();

    Long countVelocityPointsByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime);

}
