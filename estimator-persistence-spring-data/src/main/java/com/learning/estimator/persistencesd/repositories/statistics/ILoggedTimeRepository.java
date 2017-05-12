package com.learning.estimator.persistencesd.repositories.statistics;

import com.learning.estimator.model.statistics.LoggedTimeFact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Logged time repository
 *
 * @author rolea
 */
public interface ILoggedTimeRepository extends JpaRepository<LoggedTimeFact, Long>, QueryDslPredicateExecutor<LoggedTimeFact> {

    @EntityGraph(value = "graph.LoggedTimeFact.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM LoggedTimeFact m where m.loggedTimeId = :id")
    LoggedTimeFact findLoggedTimeById(@Param("id") Long id);

    @EntityGraph(value = "graph.LoggedTimeFact.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM LoggedTimeFact m")
    List<LoggedTimeFact> findAllLoggedTimes();

    @EntityGraph(value = "graph.LoggedTimeFact.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM LoggedTimeFact m")
    Page<LoggedTimeFact> findLoggedTimes(Pageable page);

}
