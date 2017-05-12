package com.learning.estimator.persistencesd.service.impl.statistics;

import com.google.common.collect.Lists;
import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.statistics.VelocityPointFact;
import com.learning.estimator.persistence.service.statistics.IVelocityPointService;
import com.learning.estimator.persistencesd.repositories.statistics.IVelocityPointsRepository;
import com.learning.estimator.persistencesd.utils.VelocityPointPredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA implementation for velocity points persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class VelocityPointService implements IVelocityPointService {

    @Autowired
    private IVelocityPointsRepository repository;

    @Override
    @WithPersistenceTryCatch
    public VelocityPointFact saveVelocityPoint(VelocityPointFact fact) {
        return repository.save(fact);
    }

    @Override
    @WithPersistenceTryCatch
    public VelocityPointFact findVelocityPoint(Long id) {
        return repository.findVelocityPointById(id);
    }

    @Override
    @WithPersistenceTryCatch
    public List<VelocityPointFact> findAllVelocityPoints() {
        return repository.findAllVelocityPoints();
    }

    @Override
    @WithPersistenceTryCatch
    public List<VelocityPointFact> findAllVelocityPoinysByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return Lists.newLinkedList(repository.findAll(VelocityPointPredicateBuilder.build(userId, projectId, groupId, beginTime, endTime)));
    }

    @Override
    @WithPersistenceTryCatch
    public List<VelocityPointFact> findVelocityPoints(int pageIndex, int pageSize) {
        return repository.findVelocityPoints(new PageRequest(pageIndex, pageSize)).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public List<VelocityPointFact> findVelocityPointsByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime, int pageIndex, int pageSize) {
        /**
         * Fetch policy is defined at mappings level (since @ManyToOne is eager by default - which si what we need)
         * and not via entity graphs ( only for criteria based paging )
         * Avoid  https://jira.spring.io/browse/DATAJPA-684
         */
        return Lists.newLinkedList(repository.findAll(VelocityPointPredicateBuilder.build(userId, projectId, groupId, beginTime, endTime), new PageRequest(pageIndex, pageSize)));
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteVelocityPoint(Long id) {
        repository.delete(id);
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllVelocityPoints() {
        repository.deleteAll();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countVelocityPoints() {
        return repository.count();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countVelocityPointsByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return repository.count(VelocityPointPredicateBuilder.build(userId, projectId, groupId, beginTime, endTime));
    }

}
