package com.learning.estimator.persistence.service.statistics;

import com.learning.estimator.model.statistics.UserDimension;

import java.util.List;

/**
 * Defines persistence operations on user dimensions
 *
 * @author rolea
 */
public interface IUserDimensionService {

    UserDimension saveUser(UserDimension userDimension);

    UserDimension findUser(Long id);

    UserDimension findUserByUsername(String username);

    List<UserDimension> findAllUsers();

    List<UserDimension> findUsers(int pageIndex, int pageSize);

    void deleteUser(Long id);

    void deleteAllUsers();

    Long countUsers();

}
