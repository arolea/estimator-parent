package com.learning.estimator.persistencesd.service.impl.persistence;

import com.learning.estimator.common.exceptions.persistence.EntityNotFoundException;
import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.persistence.service.server.IUserServiceServerSide;
import com.learning.estimator.persistencesd.repositories.persistence.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation for user persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class UserService implements IUserServiceServerSide {

    @Autowired
    private IUserRepository repository;

    @Override
    @WithPersistenceTryCatch
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    @WithPersistenceTryCatch
    public User findUser(Long id) {
        User user = repository.findUserByUserId(id);
        if (user == null)
            throw new EntityNotFoundException("The user with id " + id + " does not exist");
        return user;
    }

    @Override
    @WithPersistenceTryCatch
    public User findUserByUsername(String username) {
        User user = repository.findUserByUsername(username);
        if (user == null)
            throw new EntityNotFoundException("The user with name " + username + " does not exist");
        return user;
    }

    @Override
    @WithPersistenceTryCatch
    public List<User> findAllUsers() {
        return repository.findAllUsers();
    }

    @Override
    @WithPersistenceTryCatch
    public List<User> findUsers(int pageIndex, int pageSize) {
        return repository.findUsers(new PageRequest(pageIndex, pageSize)).getContent();
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
