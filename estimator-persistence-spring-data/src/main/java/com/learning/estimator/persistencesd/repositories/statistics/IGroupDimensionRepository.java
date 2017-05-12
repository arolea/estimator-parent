package com.learning.estimator.persistencesd.repositories.statistics;

import com.learning.estimator.model.statistics.GroupDimension;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Group dimension repository
 *
 * @author rolea
 */
public interface IGroupDimensionRepository extends JpaRepository<GroupDimension, Long> {

    GroupDimension findByGroupName(String groupName);

}
