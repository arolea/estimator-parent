package com.learning.estimator.persistence.service.client;

import com.learning.estimator.model.entities.UserGroup;

import java.util.List;

/**
 * Defines client side persistence operations for user group entities
 *
 * @author rolea
 */
public interface IUserGroupServiceClientSide {

    UserGroup saveUserGroup(UserGroup group);

    UserGroup findUserGroup(Long id);

    UserGroup findUserGroupByName(String name);

    List<UserGroup> findAllUserGroups();

    List<UserGroup> findUserGroups(int pageIndex, int pageSize);

    void deleteUserGroup(Long id);

    void deleteAllUserGroups();

    Long countUserGroups();

}
