package com.learning.estimator.persistence.service.server;

import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.persistence.service.client.IUserGroupServiceClientSide;

import java.util.Set;

/**
 * Defines additional operations available only on server side for user groups
 *
 * @author rolea
 */
public interface IUserGroupServiceServerSide extends IUserGroupServiceClientSide {

    Set<UserGroup> findAllGroupsForUser(Long userId);

}
