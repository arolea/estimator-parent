package com.learning.estimator.persistence.service.statistics;

import com.learning.estimator.model.statistics.TimeDimension;

import java.time.LocalDate;
import java.util.List;

/**
 * Defines persistence operations on time dimensions
 *
 * @author rolea
 */
public interface ITimeDimensionService {

    TimeDimension saveTime(TimeDimension timeDimension);

    TimeDimension findTime(Long id);

    TimeDimension findTime(LocalDate date);

    List<TimeDimension> findAllTimes();

    List<TimeDimension> findTimes(int pageIndex, int pageSize);

    void deleteTime(Long id);

    void deleteAllTimes();

    Long countTimes();

}
