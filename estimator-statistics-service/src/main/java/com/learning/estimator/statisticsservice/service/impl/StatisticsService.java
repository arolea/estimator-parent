package com.learning.estimator.statisticsservice.service.impl;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.model.statistics.AvailableStatistics;
import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.model.statistics.StatisticsResult;
import com.learning.estimator.statisticsservice.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.Queue;

/**
 * Statistics service implementation
 *
 * @author rolea
 */
@Component
@Profile(value = {"dev", "prod"})
public class StatisticsService implements IStatisticsService {

    private static final String COUNTER_KEY = "statisticsId";
    private static final ILogger LOG = LogManager.getLogger(StatisticsService.class);
    @Autowired
    private RedisTemplate<Integer, StatisticsResult> template;
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;
    private RedisAtomicInteger idGenerator;
    @Autowired
    private Queue computationRequestQueue;
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @PostConstruct
    public void init() {
        LOG.info("Starting statistics service...");
        this.idGenerator = new RedisAtomicInteger(COUNTER_KEY, jedisConnectionFactory);
        LOG.info("Statistics service started...");
    }

    @Override
    public Integer requestStatisticComputation(StatisticsQuery query) {
        Integer uid = idGenerator.incrementAndGet();
        query = query.withUid(uid);
        LOG.info("Received computation request -> publishing " + query);
        send(computationRequestQueue, query);
        return uid;
    }

    @Override
    //throwing an exception instead of returning not yet computed can be used in order to
    //trigger hystrix in case of excessive polling
    public StatisticsResult requestStatisticResult(Integer id) {
        try {
            StatisticsResult result = (StatisticsResult) template.opsForValue().get(id);
            if (result != null)
                return result;
            return new StatisticsResult(AvailableStatistics.NOT_YET_COMPUTED, null);
        } catch (Exception e) {
            LOG.info("No result for id " + id);
            return new StatisticsResult(AvailableStatistics.NOT_YET_COMPUTED, null);
        }
    }

    private void send(Queue queue, Object payload) {
        try {
            this.jmsMessagingTemplate.convertAndSend(queue, payload);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

}
