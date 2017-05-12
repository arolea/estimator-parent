package com.learning.estimator.persistenceservice.tests;

import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.persistenceservice.utils.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Demonstrates ETags usage
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class ETagTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${api.persistence.group.entrypoint}")
    private String entrypoint;
    @Value("${api.persistence.version}")
    private String apiVersion;

    private String url;

    @Before
    public void init() {
        url = "/" + apiVersion + "/" + entrypoint;
    }

    //NOTE : the ShallowEtagHeaderFilter does not support if-match behavior. See https://jira.spring.io/browse/SPR-10164
    @Test
    public void conditionalGet() {
        UserGroup group1 = new UserGroup("User Group 1");
        ResponseEntity<UserGroup> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<UserGroup>(group1, Utils.authenticate("admin", "admin")), UserGroup.class);
        group1 = response.getBody();

        response = restTemplate.exchange(url + "/{id}", HttpMethod.GET, new HttpEntity<UserGroup>(Utils.authenticate("admin", "admin")), UserGroup.class, group1.getUserGroupId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        HttpHeaders headers = Utils.authenticate("admin", "admin");
        headers.add("If-None-Match", response.getHeaders().getETag());
        response = restTemplate.exchange(url + "/{id}", HttpMethod.GET, new HttpEntity<UserGroup>(headers), UserGroup.class, group1.getUserGroupId());
        assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
        assertNull(response.getBody());
    }

}
