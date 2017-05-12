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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for user group persistence operations via REST controller
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class UserGroupControllerTest {

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

    @Test
    public void testIdDoesNotExist() {
        ResponseEntity<?> response = restTemplate.exchange(url + "/{id}", HttpMethod.GET, new HttpEntity<UserGroup>(Utils.authenticate("admin", "admin")), UserGroup.class, 100l);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        response = restTemplate.exchange(url + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, 100l);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUserGroupController() {
        UserGroup group1 = new UserGroup("User Group 1");
        ResponseEntity<UserGroup> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<UserGroup>(group1, Utils.authenticate("admin", "admin")), UserGroup.class);
        group1 = response.getBody();
        assertNotNull(group1.getUserGroupId());
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<UserGroup>(new UserGroup("User Group 2"), Utils.authenticate("admin", "admin")), UserGroup.class);
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<UserGroup>(new UserGroup("User Group 3"), Utils.authenticate("admin", "admin")), UserGroup.class);

        //test count
        ResponseEntity<Long> counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(3), counter.getBody());

        //test find and findAll
        response = restTemplate.exchange(url + "/{id}", HttpMethod.GET, new HttpEntity<UserGroup>(Utils.authenticate("admin", "admin")), UserGroup.class, group1.getUserGroupId());
        assertEquals(group1, response.getBody());

        ResponseEntity<UserGroup[]> allGroups = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<UserGroup[]>(Utils.authenticate("admin", "admin")), UserGroup[].class);
        assertEquals(3, allGroups.getBody().length);

        //test update
        group1.setUserGroupName("New name");
        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<UserGroup>(group1, Utils.authenticate("admin", "admin")), UserGroup.class);
        assertEquals(new Long(1), response.getBody().getVersion());

        //test delete and deleteAll
        restTemplate.exchange(url + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, group1.getUserGroupId());
        counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(2), counter.getBody());

        restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class);
        counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(0), counter.getBody());

        //test pagination
        for (int i = 0; i < 10; i++)
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<UserGroup>(new UserGroup("User Group " + i), Utils.authenticate("admin", "admin")), UserGroup.class);
        for (int i = 0; i < 5; i++) {
            ResponseEntity<UserGroup[]> orders = restTemplate.exchange(url + "/paginate/?pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), UserGroup[].class, i, 2);
            assertEquals(2, orders.getBody().length);
        }
    }

    @Test
    public void testUserGroupControllerValidation() {
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<UserGroup>(new UserGroup(null), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUserGroupControllerConstraints() {
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<UserGroup>(new UserGroup("User Group"), Utils.authenticate("admin", "admin")), UserGroup.class);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<UserGroup>(new UserGroup("User Group"), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
