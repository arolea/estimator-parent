package com.learning.estimator.persistencesd.repositories.persistence;

import com.learning.estimator.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * User repository
 *
 * @author rolea
 */
public interface IUserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value = "graph.User.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM User m where m.userId = :id")
    User findUserByUserId(@Param("id") Long id);

    @EntityGraph(value = "graph.User.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM User m where m.username = :username")
    User findUserByUsername(@Param("username") String username);

    @EntityGraph(value = "graph.User.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM User m")
    List<User> findAllUsers();

    @EntityGraph(value = "graph.User.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM User m")
    Page<User> findUsers(Pageable page);

}
