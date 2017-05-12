package com.learning.estimator.statisticsgenerator.service.batch.listeners;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.model.statistics.StatisticsResult;
import com.learning.estimator.statisticsgenerator.service.batch.utils.IResultProvider;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Gets the accumulated result from a writer and posts in back to the statistics service
 *
 * @author rolea
 */
public class StatisticComputationFinishListener implements StepExecutionListener {

    private static final ILogger LOG = LogManager.getLogger(StatisticComputationFinishListener.class);
    private RedisTemplate<Integer, StatisticsResult> template;
    private IResultProvider<?> resultProvider;
    private StatisticsQuery query;

    public StatisticComputationFinishListener(IResultProvider<?> resultProvider, StatisticsQuery query, RedisTemplate<Integer, StatisticsResult> template) {
        this.resultProvider = resultProvider;
        this.query = query;
        this.template = template;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOG.info("Starting statistic computation for query " + query);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOG.info("Publishing result for statistic request with id " + query.getUid());
        LOG.info("Result : " + resultProvider.getResult());
        StatisticsResult result = new StatisticsResult(query.getStatistic(), resultProvider.getResult());
        template.opsForValue().set(query.getUid(), result, 10, TimeUnit.MINUTES);
        return stepExecution.getExitStatus();
    }


}
