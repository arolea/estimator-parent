package com.learning.estimator.statisticsservice.controller;

import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.model.statistics.StatisticsResult;
import com.learning.estimator.statisticsservice.service.IStatisticsService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Statistics controller implementation
 *
 * @author rolea
 */
@RestController
@RequestMapping("/${api.statistics.version}")
@Api(value = "Statistics API")
public class StatisticsController {

    @Autowired
    private IStatisticsService service;

    @RequestMapping(value = "/requestComputation", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation(value = "Request statistic computation", notes = "Returns a token that is used in order to poll for the result")
    public Integer requestStatisticComputation(@RequestBody StatisticsQuery query) {
        return service.requestStatisticComputation(query);
    }

    @RequestMapping(value = "/requestResult/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Request statistic computation result", notes = "If available, returns the result of the statistic computation")
    @HystrixCommand
    public StatisticsResult requestStatisticResult(@PathVariable(name = "id") Integer id) {
        return service.requestStatisticResult(id);
    }

}
