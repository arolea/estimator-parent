package com.learning.estimator.persistencesd.service.impl.statistics;

import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.statistics.TimeDimension;
import com.learning.estimator.persistence.service.statistics.ITimeDimensionService;
import com.learning.estimator.persistencesd.repositories.statistics.ITimeDimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA implementation for time dimensions persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class TimeDimensionService implements ITimeDimensionService {

    @Autowired
    private ITimeDimensionRepository repository;

    @Override
    @WithPersistenceTryCatch
    public TimeDimension saveTime(TimeDimension timeDimension) {
        return repository.save(timeDimension);
    }

    @Override
    @WithPersistenceTryCatch
    public TimeDimension findTime(Long id) {
        return repository.findOne(id);
    }

    @Override
    @WithPersistenceTryCatch
    public TimeDimension findTime(LocalDate date) {
        return repository.findTimeByDate(date);
    }

    @Override
    @WithPersistenceTryCatch
    public List<TimeDimension> findAllTimes() {
        return repository.findAll();
    }

    @Override
    @WithPersistenceTryCatch
    public List<TimeDimension> findTimes(int pageIndex, int pageSize) {
        return repository.findAll(new PageRequest(pageIndex, pageSize)).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteTime(Long id) {
        repository.delete(id);
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllTimes() {
        repository.deleteAll();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countTimes() {
        return repository.count();
    }

}
