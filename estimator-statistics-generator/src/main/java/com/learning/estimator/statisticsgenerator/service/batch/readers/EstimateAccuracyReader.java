package com.learning.estimator.statisticsgenerator.service.batch.readers;

import com.learning.estimator.model.statistics.EstimateAccuracyFact;
import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.persistence.facade.statistics.StatisticsFacade;

/**
 * EstimateAccuracyFact stream reader
 *
 * @author rolea
 */
public class EstimateAccuracyReader extends AbstractPersistenceFacadeReader<EstimateAccuracyFact> {

    private static final int PAGE_SIZE = 100;
    private static final String METHOD_NAME = "findEstimateAccuraciesByPredicate";

    public EstimateAccuracyReader(StatisticsFacade facade, StatisticsQuery query) {
        super(facade, query, PAGE_SIZE, METHOD_NAME);
    }

}

