package com.learning.estimator.persistenceservice.tests;

import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
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

import static org.junit.Assert.*;

/**
 * Tests for user persistence operations via REST controller
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PersistenceFacadeClientSide facade;

    @Value("${api.persistence.user.entrypoint}")
    private String entrypoint;
    @Value("${api.persistence.version}")
    private String apiVersion;

    private String url;

    @Before
    public void init() {
        url = "/" + apiVersion + "/" + entrypoint;
    }

    @Test
    public void testUserController() {
        UserGroup group = facade.saveUserGroup(new UserGroup("User Group 1"));

        User user = new User("User 1", "User 1").withoutUserGroup(group).withUserRole(UserRole.ROLE_ADMIN);
        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<User>(user, Utils.authenticate("admin", "admin")), User.class);
        user = response.getBody();
        assertNotNull(user.getUserId());
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<User>(new User("User 2", "User 2").withoutUserGroup(group).withUserRole(UserRole.ROLE_ADMIN), Utils.authenticate("admin", "admin")), User.class);
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<User>(new User("User 3", "User 3").withoutUserGroup(group).withUserRole(UserRole.ROLE_ADMIN), Utils.authenticate("admin", "admin")), User.class);

        //test count
        ResponseEntity<Long> counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(4), counter.getBody());

        //test find and findAll
        response = restTemplate.exchange(url + "/{id}", HttpMethod.GET, new HttpEntity<User>(Utils.authenticate("admin", "admin")), User.class, user.getUserId());
        assertEquals(user, response.getBody());

        ResponseEntity<User[]> allGroups = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<User[]>(Utils.authenticate("admin", "admin")), User[].class);
        assertEquals(4, allGroups.getBody().length);

        //test update
        user.setUsername("New name");
        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<User>(user, Utils.authenticate("admin", "admin")), User.class);
        assertEquals(new Long(1), response.getBody().getVersion());

        //test delete and deleteAll
        restTemplate.exchange(url + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, user.getUserId());
        counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(3), counter.getBody());

        restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class);
        try {
            counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
            fail();
        } catch (Exception e) {
            // the user with which the authentication is done no longer exists
        }
    }

    @Test
    public void testPaging() {
        UserGroup group = facade.saveUserGroup(new UserGroup("User Group 1"));
        //test pagination
        for (int i = 0; i < 10; i++)
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<User>(new User("User" + i, "User " + i).withoutUserGroup(group).withUserRole(UserRole.ROLE_ADMIN), Utils.authenticate("admin", "admin")), User.class);
        for (int i = 0; i < 5; i++) {
            ResponseEntity<User[]> orders = restTemplate.exchange(url + "/paginate/?pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), User[].class, i, 2);
            assertEquals(2, orders.getBody().length);
        }
    }

    @Test
    public void testUserControllerValidation() {
        UserGroup group = facade.saveUserGroup(new UserGroup("User Group 1"));

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<User>(new User(null, "User 1").withoutUserGroup(group).withUserRole(UserRole.ROLE_ADMIN), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<User>(new User("User 1", null).withoutUserGroup(group).withUserRole(UserRole.ROLE_ADMIN), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUserControllerConstraints() {
        UserGroup group = facade.saveUserGroup(new UserGroup("User Group 1"));

        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<User>(new User("User 1", "User 1").withoutUserGroup(group).withUserRole(UserRole.ROLE_ADMIN), Utils.authenticate("admin", "admin")), Void.class);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<User>(new User("User 1", "User 1").withoutUserGroup(group).withUserRole(UserRole.ROLE_ADMIN), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
