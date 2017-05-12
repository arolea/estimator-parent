package com.learning.estimator.persistence.service.client;

import com.learning.estimator.model.entities.User;

import java.util.List;

/**
 * Defines client side persistence operations for user entities
 *
 * @author rolea
 */
public interface IUserServiceClientSide {

    User saveUser(User user);

    User findUser(Long id);

    User findUserByUsername(String username);

    List<User> findAllUsers();

    List<User> findUsers(int pageIndex, int pageSize);

    void deleteUser(Long id);

    void deleteAllUsers();

    Long countUsers();

}
