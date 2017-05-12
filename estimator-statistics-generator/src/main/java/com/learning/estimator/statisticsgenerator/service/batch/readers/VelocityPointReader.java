package com.learning.estimator.statisticsgenerator.service.batch.readers;

import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.model.statistics.VelocityPointFact;
import com.learning.estimator.persistence.facade.statistics.StatisticsFacade;

/**
 * VelocityPointFact stream reader
 *
 * @author rolea
 */
public class VelocityPointReader extends AbstractPersistenceFacadeReader<VelocityPointFact> {

    private static final int PAGE_SIZE = 100;
    private static final String METHOD_NAME = "findVelocityPointsByPredicate";

    public VelocityPointReader(StatisticsFacade facade, StatisticsQuery query) {
        super(facade, query, PAGE_SIZE, METHOD_NAME);
    }

}
