package com.learning.estimator.persistence.service.statistics;

import com.learning.estimator.model.statistics.LoggedTimeFact;

import java.time.LocalDate;
import java.util.List;

/**
 * Defines persistence operations on logged time facts
 *
 * @author rolea
 */
public interface ILoggedTimeService {

    LoggedTimeFact saveLoggedTime(LoggedTimeFact fact);

    LoggedTimeFact findLoggedTime(Long id);

    List<LoggedTimeFact> findAllLoggedTimes();

    List<LoggedTimeFact> findAllLoggedTimesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime);

    List<LoggedTimeFact> findLoggedTimes(int pageIndex, int pageSize);

    List<LoggedTimeFact> findLoggedTimesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime, int pageIndex, int pageSize);

    void deleteLoggedTime(Long id);

    void deleteAllLoggedTimes();

    Long countLoggedTimes();

    Long countLoggedTimesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime);

}
