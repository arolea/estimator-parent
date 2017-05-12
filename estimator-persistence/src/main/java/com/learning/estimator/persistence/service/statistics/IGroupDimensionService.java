package com.learning.estimator.persistence.service.statistics;

import com.learning.estimator.model.statistics.GroupDimension;

import java.util.List;

/**
 * Defines persistence operations on group dimensions
 *
 * @author rolea
 */
public interface IGroupDimensionService {

    GroupDimension saveGroup(GroupDimension groupDimension);

    GroupDimension findGroup(Long id);

    GroupDimension findGroupByName(String groupName);

    List<GroupDimension> findAllGroups();

    List<GroupDimension> findGroups(int pageIndex, int pageSize);

    void deleteGroup(Long id);

    void deleteAllGroups();

    Long countGroups();

}
