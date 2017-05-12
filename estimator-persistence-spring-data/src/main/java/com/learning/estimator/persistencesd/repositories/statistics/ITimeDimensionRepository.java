package com.learning.estimator.persistencesd.repositories.statistics;

import com.learning.estimator.model.statistics.TimeDimension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

/**
 * Time dimension repository
 *
 * @author rolea
 */
public interface ITimeDimensionRepository extends JpaRepository<TimeDimension, Long> {

    TimeDimension findTimeByDate(LocalDate date);

}
