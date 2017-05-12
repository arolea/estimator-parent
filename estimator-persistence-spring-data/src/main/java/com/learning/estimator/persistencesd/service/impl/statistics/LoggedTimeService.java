package com.learning.estimator.persistencesd.service.impl.statistics;

import com.google.common.collect.Lists;
import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.statistics.LoggedTimeFact;
import com.learning.estimator.persistence.service.statistics.ILoggedTimeService;
import com.learning.estimator.persistencesd.repositories.statistics.ILoggedTimeRepository;
import com.learning.estimator.persistencesd.utils.LoggedTimePredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA implementation for logged times persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class LoggedTimeService implements ILoggedTimeService {

    @Autowired
    private ILoggedTimeRepository repository;

    @Override
    @WithPersistenceTryCatch
    public LoggedTimeFact saveLoggedTime(LoggedTimeFact fact) {
        return repository.save(fact);
    }

    @Override
    @WithPersistenceTryCatch
    public LoggedTimeFact findLoggedTime(Long id) {
        return repository.findLoggedTimeById(id);
    }

    @Override
    @WithPersistenceTryCatch
    public List<LoggedTimeFact> findAllLoggedTimes() {
        return repository.findAllLoggedTimes();
    }

    @Override
    @WithPersistenceTryCatch
    public List<LoggedTimeFact> findAllLoggedTimesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return Lists.newLinkedList(repository.findAll(LoggedTimePredicateBuilder.build(userId, projectId, groupId, beginTime, endTime)));
    }

    @Override
    @WithPersistenceTryCatch
    public List<LoggedTimeFact> findLoggedTimes(int pageIndex, int pageSize) {
        return repository.findLoggedTimes(new PageRequest(pageIndex, pageSize)).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public List<LoggedTimeFact> findLoggedTimesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime, int pageIndex, int pageSize) {
        /**
         * Fetch policy is defined at mappings level (since @ManyToOne is eager by default - which si what we need)
         * and not via entity graphs ( only for criteria based paging )
         * Avoid  https://jira.spring.io/browse/DATAJPA-684
         */
        return Lists.newLinkedList(repository.findAll(LoggedTimePredicateBuilder.build(userId, projectId, groupId, beginTime, endTime), new PageRequest(pageIndex, pageSize)));
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteLoggedTime(Long id) {
        repository.delete(id);
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllLoggedTimes() {
        repository.deleteAll();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countLoggedTimes() {
        return repository.count();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countLoggedTimesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return repository.count(LoggedTimePredicateBuilder.build(userId, projectId, groupId, beginTime, endTime));
    }
}
