package com.learning.estimator.persistencesd.service.impl.statistics;

import com.google.common.collect.Lists;
import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.statistics.EstimateAccuracyFact;
import com.learning.estimator.persistence.service.statistics.IEstimateAccuracyService;
import com.learning.estimator.persistencesd.repositories.statistics.IEstimateAccuracyRepository;
import com.learning.estimator.persistencesd.utils.EstimateAccuracyPredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA implementation for estimate accuracies persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class EstimateAccuracyService implements IEstimateAccuracyService {

    @Autowired
    private IEstimateAccuracyRepository repository;

    @Override
    @WithPersistenceTryCatch
    public EstimateAccuracyFact saveEstimateAccuracy(EstimateAccuracyFact fact) {
        return repository.save(fact);
    }

    @Override
    @WithPersistenceTryCatch
    public EstimateAccuracyFact findEstimateAccuracy(Long id) {
        return repository.findEstimateByEstimateAccuracyId(id);
    }

    @Override
    @WithPersistenceTryCatch
    public List<EstimateAccuracyFact> findAllEstimateAccuracies() {
        return repository.findAllEstimatesAccuracies();
    }

    @Override
    @WithPersistenceTryCatch
    public List<EstimateAccuracyFact> findAllEstimateAccuraciesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return Lists.newLinkedList(repository.findAll(EstimateAccuracyPredicateBuilder.build(userId, projectId, groupId, beginTime, endTime)));
    }

    @Override
    @WithPersistenceTryCatch
    public List<EstimateAccuracyFact> findEstimateAccuracies(int pageIndex, int pageSize) {
        return repository.findEstimateAccuracies(new PageRequest(pageIndex, pageSize)).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public List<EstimateAccuracyFact> findEstimateAccuraciesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime, int pageIndex, int pageSize) {
        /**
         * Fetch policy is defined at mappings level (since @ManyToOne is eager by default - which si what we need)
         * and not via entity graphs ( only for criteria based paging )
         * Avoid  https://jira.spring.io/browse/DATAJPA-684
         */
        return Lists.newLinkedList(repository.findAll(EstimateAccuracyPredicateBuilder.build(userId, projectId, groupId, beginTime, endTime), new PageRequest(pageIndex, pageSize)));
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteEstimateAccuracy(Long id) {
        repository.delete(id);
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllEstimateAccuracies() {
        repository.deleteAll();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countEstimateAccuracies() {
        return repository.count();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countEstimateAccuraciesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return repository.count(EstimateAccuracyPredicateBuilder.build(userId, projectId, groupId, beginTime, endTime));
    }

}
