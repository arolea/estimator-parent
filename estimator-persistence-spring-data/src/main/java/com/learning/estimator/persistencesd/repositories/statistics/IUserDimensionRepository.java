package com.learning.estimator.persistencesd.repositories.statistics;

import com.learning.estimator.model.statistics.UserDimension;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User dimension repository
 *
 * @author rolea
 */
public interface IUserDimensionRepository extends JpaRepository<UserDimension, Long> {

    UserDimension findUserDimensionByUsername(String username);

}
