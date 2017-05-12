package com.learning.estimator.statisticsservice;

import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.model.statistics.StatisticsResult;
import com.learning.estimator.statisticsservice.utils.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Tests persistence constraints via controllers
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class StatisticsServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${api.statistics.username}")
    private String username;
    @Value("${api.statistics.password}")
    private String password;
    @Value("${api.statistics.version}")
    private String apiVersion;

    private String url;

    @Before
    public void init() {
        url = "/" + apiVersion + "/";
    }

    @Test
    public void test() {
        ResponseEntity<Integer> response = restTemplate.exchange(url + "requestComputation", HttpMethod.POST, new HttpEntity<StatisticsQuery>(new StatisticsQuery(), Utils.authenticate(username, password)), Integer.class);
        assertNotNull(response.getBody());

        restTemplate.exchange(url + "requestResult/{id}", HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate(username, password)), StatisticsResult.class, 1);
        restTemplate.exchange(url + "publishResult/{id}", HttpMethod.POST, new HttpEntity<StatisticsResult>(new StatisticsResult(null, response), Utils.authenticate(username, password)), Void.class, 1);
    }

}
