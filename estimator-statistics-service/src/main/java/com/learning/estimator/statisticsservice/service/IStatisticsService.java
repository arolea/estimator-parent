package com.learning.estimator.statisticsservice.service;

import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.model.statistics.StatisticsResult;

/**
 * Defines the operations exposed by the statistics service
 *
 * @author rolea
 */
public interface IStatisticsService {

    /**
     * Invoked by a client requesting the computation of a specific statistic<br>
     * Will not block and will return a token to the client<br>
     * The token can be used in order to retrieve the statistic result<br>
     */
    Integer requestStatisticComputation(StatisticsQuery query);

    /**
     * Invoked by a client requesting the result of a specific computation<br>
     * Blocks for a predefined amount of time, and if the result is not available at time out, returns a default response to the client<br>
     * Avoids blocking the client for an unbounded interval<br>
     */
    StatisticsResult requestStatisticResult(Integer id);

}
