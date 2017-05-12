package com.learning.estimator.persistencesd.repositories.statistics;

import com.learning.estimator.model.statistics.EstimateAccuracyFact;
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
 * Estimate accuracy repository
 *
 * @author rolea
 */
public interface IEstimateAccuracyRepository extends JpaRepository<EstimateAccuracyFact, Long>, QueryDslPredicateExecutor<EstimateAccuracyFact> {

    @EntityGraph(value = "graph.EstimateAccuracyFact.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM EstimateAccuracyFact m where m.estimateAccuracyId = :id")
    EstimateAccuracyFact findEstimateByEstimateAccuracyId(@Param("id") Long id);

    @EntityGraph(value = "graph.EstimateAccuracyFact.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM EstimateAccuracyFact m")
    List<EstimateAccuracyFact> findAllEstimatesAccuracies();

    @EntityGraph(value = "graph.EstimateAccuracyFact.fetchPolicy", type = EntityGraphType.LOAD)
    @Query("SELECT m FROM EstimateAccuracyFact m")
    Page<EstimateAccuracyFact> findEstimateAccuracies(Pageable page);

}
