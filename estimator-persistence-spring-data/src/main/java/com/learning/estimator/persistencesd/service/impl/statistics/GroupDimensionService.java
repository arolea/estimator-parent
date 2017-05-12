package com.learning.estimator.persistencesd.service.impl.statistics;

import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.statistics.GroupDimension;
import com.learning.estimator.persistence.service.statistics.IGroupDimensionService;
import com.learning.estimator.persistencesd.repositories.statistics.IGroupDimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation for group dimensions persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class GroupDimensionService implements IGroupDimensionService {

    @Autowired
    private IGroupDimensionRepository repository;

    @Override
    @WithPersistenceTryCatch
    public GroupDimension saveGroup(GroupDimension groupDimension) {
        return repository.save(groupDimension);
    }

    @Override
    @WithPersistenceTryCatch
    public GroupDimension findGroup(Long id) {
        return repository.findOne(id);
    }

    @Override
    @WithPersistenceTryCatch
    public GroupDimension findGroupByName(String groupName) {
        return repository.findByGroupName(groupName);
    }

    @Override
    @WithPersistenceTryCatch
    public List<GroupDimension> findAllGroups() {
        return repository.findAll();
    }

    @Override
    @WithPersistenceTryCatch
    public List<GroupDimension> findGroups(int pageIndex, int pageSize) {
        return repository.findAll(new PageRequest(pageIndex, pageSize)).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteGroup(Long id) {
        repository.delete(id);
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllGroups() {
        repository.deleteAll();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countGroups() {
        return repository.count();
    }

}
