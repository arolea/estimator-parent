package com.learning.estimator.statisticsgenerator.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.model.statistics.AvailableStatistics;
import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.model.statistics.StatisticsResult;
import com.learning.estimator.persistence.facade.statistics.StatisticsFacade;
import com.learning.estimator.statisticsgenerator.service.batch.listeners.StatisticComputationFinishListener;
import com.learning.estimator.statisticsgenerator.service.batch.readers.EstimateAccuracyReader;
import com.learning.estimator.statisticsgenerator.service.batch.readers.LoggedTimeReader;
import com.learning.estimator.statisticsgenerator.service.batch.readers.VelocityPointReader;
import com.learning.estimator.statisticsgenerator.service.batch.utils.IResultProvider;
import com.learning.estimator.statisticsgenerator.service.batch.utils.ListAccumulator;
import com.learning.estimator.statisticsgenerator.service.batch.utils.MapAccumulator;
import com.learning.estimator.statisticsgenerator.service.batch.writers.BoxWriter;
import com.learning.estimator.statisticsgenerator.service.batch.writers.HistogramWriter;
import com.learning.estimator.statisticsgenerator.service.batch.writers.TimeEvolutionWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Statistics generator implementation
 *
 * @author rolea
 */
@Component
public class StatisticsGenerator {

    private static final ILogger LOG = LogManager.getLogger(StatisticsGenerator.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private SimpleJobLauncher launcher;
    @Autowired
    private RedisTemplate<Integer, StatisticsResult> template;
    @Autowired
    private StatisticsFacade facade;
    private AtomicLong counter;
    private Executor executor;

    @PostConstruct
    public void init() {
        LOG.info("Starting statistics generator node...");
        counter = new AtomicLong();
        initExecutor();
        LOG.info("Statistics generator node started...");
    }

    private void initExecutor() {
        ThreadFactory factory = new ThreadFactoryBuilder().
                setNameFormat("Statistic computation thread factory").
                setDaemon(true).
                setUncaughtExceptionHandler((thread, throwable) -> {
                    LOG.info("Exception caught in thread " + thread.getName() + " ( id : " + thread.getId() + " ) ");
                    LOG.error(throwable);
                }).
                build();
        executor = Executors.newCachedThreadPool(factory);
    }

    @JmsListener(destination = "queue.statistics")
    public void processStatisticsRequest(StatisticsQuery request) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        executor.execute(() -> {
            try {
                launcher.run(getJob(request), new JobParametersBuilder().toJobParameters());
            } catch (Exception e) {
                LOG.error(e);
            }
        });
    }

    public ItemReader<?> getItemReader(AvailableStatistics statistic, StatisticsQuery query) {
        switch (statistic) {
            case ESTIMATE_ACCURACY_BOX:
            case ESTIMATE_ACCURACY_EVOLUTION:
            case ESTIMATE_ACCURACY_HISTOGRAM:
                return new EstimateAccuracyReader(facade, query);
            case LOGGED_TIME_BOX:
            case LOGGED_TIME_EVOLUTION:
            case LOGGED_TIME_HISTOGRAM:
                return new LoggedTimeReader(facade, query);
            case VELOCITY_POINTS_EVOLUTION:
            case VELOCITY_POINT_BOX:
            case VELOCITY_POINT_HISTOGRAM:
                return new VelocityPointReader(facade, query);
            default:
                LOG.info("Received unknown statistic " + statistic);
                return null;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ItemWriter<? super Object> getItemWriter(AvailableStatistics statistic, IResultProvider<?> accumulator, StatisticsQuery query) {
        switch (statistic) {
            case ESTIMATE_ACCURACY_BOX:
            case LOGGED_TIME_BOX:
            case VELOCITY_POINT_BOX:
                return new BoxWriter(accumulator);
            case ESTIMATE_ACCURACY_EVOLUTION:
            case LOGGED_TIME_EVOLUTION:
            case VELOCITY_POINTS_EVOLUTION:
                return new TimeEvolutionWriter(query, accumulator);
            case ESTIMATE_ACCURACY_HISTOGRAM:
            case LOGGED_TIME_HISTOGRAM:
            case VELOCITY_POINT_HISTOGRAM:
                return new HistogramWriter(query, accumulator);
            default:
                LOG.info("Received unknown statistic " + statistic);
                return null;
        }
    }

    public IResultProvider<?> getResultProvider(StatisticsQuery query) {
        switch (query.getStatistic()) {
            case ESTIMATE_ACCURACY_BOX:
            case LOGGED_TIME_BOX:
            case VELOCITY_POINT_BOX:
                return new ListAccumulator<Double>();
            case ESTIMATE_ACCURACY_EVOLUTION:
            case LOGGED_TIME_EVOLUTION:
            case VELOCITY_POINTS_EVOLUTION:
                return new MapAccumulator<LocalDate>();
            case ESTIMATE_ACCURACY_HISTOGRAM:
            case LOGGED_TIME_HISTOGRAM:
            case VELOCITY_POINT_HISTOGRAM:
                return new MapAccumulator<Integer>();
            default:
                LOG.info("Received unknown statistic " + query.getStatistic());
                return null;
        }
    }

    public Job getJob(StatisticsQuery query) {
        long requestId = counter.incrementAndGet();
        IResultProvider<?> provider = getResultProvider(query);
        return jobBuilderFactory.get("Request " + requestId).
                incrementer(new RunIdIncrementer()).
                start(stepBuilderFactory.get("computeStatistic" + requestId).
                        chunk(100).
                        reader(getItemReader(query.getStatistic(), query)).
                        processor(new PassThroughItemProcessor<>()).
                        writer(getItemWriter(query.getStatistic(), provider, query)).
                        listener(new StatisticComputationFinishListener(provider, query, template)).
                        build()).
                build();
    }

}
