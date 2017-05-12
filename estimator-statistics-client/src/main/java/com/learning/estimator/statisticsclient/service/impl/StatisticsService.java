package com.learning.estimator.statisticsclient.service.impl;

import com.learning.estimator.common.exceptions.statistics.StatisticsException;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.model.statistics.StatisticsResult;
import com.learning.estimator.statisticsclient.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Exposes statistics operations
 *
 * @author rolea
 */
@Component
public class StatisticsService {

    private static final ILogger LOG = LogManager.getLogger(StatisticsService.class);
    @javax.annotation.Resource(name = "statisticstemplate")
    protected RestTemplate restTemplate;
    @Value("${api.statistics.username}")
    private String username;
    @Value("${api.statistics.password}")
    private String password;

    @Value("${api.statistics.version}")
    private String apiVersion;
    @Value("${api.statistics.name}")
    private String apiName;

    private String url;

    @PostConstruct
    public void init() {
        url = apiName + "/" + apiVersion;
        LOG.info("Statistics service url : " + url);
    }

    public Integer requestStatisticComputation(StatisticsQuery query) {
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(url + "/requestComputation", HttpMethod.POST, new HttpEntity<StatisticsQuery>(query, Utils.authenticate(username, password)), Integer.class);
            return response.getBody();
        } catch (Exception e) {
            LOG.error(e);
            throw new StatisticsException("A statistics exception has occured");
        }
    }

    public StatisticsResult requestStatisticResult(Long id) {
        try {
            ResponseEntity<StatisticsResult> response = restTemplate.exchange(url + "/requestResult/{id}", HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate(username, password)), StatisticsResult.class, id);
            return response.getBody();
        } catch (Exception e) {
            LOG.error(e);
            throw new StatisticsException("A statistics exception has occured");
        }
    }

}
