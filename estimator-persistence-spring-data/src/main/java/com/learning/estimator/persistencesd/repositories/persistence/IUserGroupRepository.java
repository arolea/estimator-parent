package com.learning.estimator.persistencesd.repositories.persistence;

import com.learning.estimator.model.entities.UserGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * User group repository
 *
 * @author rolea
 */
public interface IUserGroupRepository extends JpaRepository<UserGroup, Long> {

    UserGroup findUserGroupByUserGroupName(String name);

    @EntityGraph(value = "graph.UserGroup.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM UserGroup m where m.userGroupId in :groups")
    List<UserGroup> findAllGroupsForUser(@Param("groups") Set<UserGroup> groups);

}
