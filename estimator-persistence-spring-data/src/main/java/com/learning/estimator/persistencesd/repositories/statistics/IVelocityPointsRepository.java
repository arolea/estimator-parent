package com.learning.estimator.persistencesd.repositories.statistics;

import com.learning.estimator.model.statistics.VelocityPointFact;
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
 * Velocity points repository
 *
 * @author rolea
 */
public interface IVelocityPointsRepository extends JpaRepository<VelocityPointFact, Long>, QueryDslPredicateExecutor<VelocityPointFact> {

    @EntityGraph(value = "graph.VelocityPointFact.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM VelocityPointFact m where m.velocityPointsId = :id")
    VelocityPointFact findVelocityPointById(@Param("id") Long id);

    @EntityGraph(value = "graph.VelocityPointFact.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM VelocityPointFact m")
    List<VelocityPointFact> findAllVelocityPoints();

    @EntityGraph(value = "graph.VelocityPointFact.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM VelocityPointFact m")
    Page<VelocityPointFact> findVelocityPoints(Pageable page);

}
