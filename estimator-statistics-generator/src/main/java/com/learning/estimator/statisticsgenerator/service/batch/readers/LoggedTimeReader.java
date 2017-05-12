package com.learning.estimator.statisticsgenerator.service.batch.readers;

import com.learning.estimator.model.statistics.LoggedTimeFact;
import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.persistence.facade.statistics.StatisticsFacade;

/**
 * LoggedTimeFact stream reader
 *
 * @author rolea
 */
public class LoggedTimeReader extends AbstractPersistenceFacadeReader<LoggedTimeFact> {

    private static final int PAGE_SIZE = 100;
    private static final String METHOD_NAME = "findLoggedTimesByPredicate";

    public LoggedTimeReader(StatisticsFacade facade, StatisticsQuery query) {
        super(facade, query, PAGE_SIZE, METHOD_NAME);
    }

}
