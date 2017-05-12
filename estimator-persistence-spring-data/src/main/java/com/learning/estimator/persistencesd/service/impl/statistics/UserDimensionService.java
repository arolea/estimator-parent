package com.learning.estimator.persistencesd.service.impl.statistics;

import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.statistics.UserDimension;
import com.learning.estimator.persistence.service.statistics.IUserDimensionService;
import com.learning.estimator.persistencesd.repositories.statistics.IUserDimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation for user dimension persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class UserDimensionService implements IUserDimensionService {

    @Autowired
    private IUserDimensionRepository repository;

    @Override
    @WithPersistenceTryCatch
    public UserDimension saveUser(UserDimension userDimension) {
        return repository.save(userDimension);
    }

    @Override
    @WithPersistenceTryCatch
    public UserDimension findUser(Long id) {
        return repository.findOne(id);
    }

    @Override
    @WithPersistenceTryCatch
    public UserDimension findUserByUsername(String username) {
        return repository.findUserDimensionByUsername(username);
    }

    @Override
    @WithPersistenceTryCatch
    public List<UserDimension> findAllUsers() {
        return repository.findAll();
    }

    @Override
    @WithPersistenceTryCatch
    public List<UserDimension> findUsers(int pageIndex, int pageSize) {
        return repository.findAll(new PageRequest(pageIndex, pageSize)).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteUser(Long id) {
        repository.delete(id);
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllUsers() {
        repository.deleteAll();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countUsers() {
        return repository.count();
    }

}
