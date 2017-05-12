package com.learning.estimator.persistencesd.service.impl.persistence;

import com.learning.estimator.common.exceptions.persistence.EntityNotFoundException;
import com.learning.estimator.common.exceptions.persistence.WithPersistenceTryCatch;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.persistence.service.server.IUserGroupServiceServerSide;
import com.learning.estimator.persistencesd.repositories.persistence.IUserGroupRepository;
import com.learning.estimator.persistencesd.repositories.persistence.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * JPA implementation for user group persistence operations
 *
 * @author rolea
 */
@Component
@Primary
public class UserGroupService implements IUserGroupServiceServerSide {

    @Autowired
    private IUserGroupRepository repository;
    @Autowired
    private IUserRepository userRepository;

    @Override
    @WithPersistenceTryCatch
    public UserGroup saveUserGroup(UserGroup group) {
        return repository.save(group);
    }

    @Override
    @WithPersistenceTryCatch
    public UserGroup findUserGroup(Long id) {
        UserGroup group = repository.findOne(id);
        if (group == null)
            throw new EntityNotFoundException("The user group with id " + id + " does not exist");
        return group;
    }

    @Override
    @WithPersistenceTryCatch
    public UserGroup findUserGroupByName(String name) {
        UserGroup group = repository.findUserGroupByUserGroupName(name);
        if (group == null)
            throw new EntityNotFoundException("The user group with name " + name + " does not exist");
        return group;
    }

    @Override
    @WithPersistenceTryCatch
    public List<UserGroup> findAllUserGroups() {
        return repository.findAll();
    }

    @Override
    @WithPersistenceTryCatch
    public List<UserGroup> findUserGroups(int pageIndex, int pageSize) {
        return repository.findAll(new PageRequest(pageIndex, pageSize)).getContent();
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteUserGroup(Long id) {
        repository.delete(id);
    }

    @Override
    @WithPersistenceTryCatch
    public void deleteAllUserGroups() {
        repository.deleteAll();
    }

    @Override
    @WithPersistenceTryCatch
    public Long countUserGroups() {
        return repository.count();
    }

    @Override
    @WithPersistenceTryCatch
    public Set<UserGroup> findAllGroupsForUser(Long userId) {
        return userRepository.findUserByUserId(userId).getGroups();
    }


}
