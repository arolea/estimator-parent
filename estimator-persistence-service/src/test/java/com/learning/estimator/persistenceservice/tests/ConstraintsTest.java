package com.learning.estimator.persistenceservice.tests;

import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.User;
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

/**
 * Tests persistence constraints via controllers
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class ConstraintsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${api.persistence.task.entrypoint}")
    private String taskEntrypoint;
    @Value("${api.persistence.project.entrypoint}")
    private String projectEntrypoint;
    @Value("${api.persistence.user.entrypoint}")
    private String userEntrypoint;
    @Value("${api.persistence.group.entrypoint}")
    private String groupEntrypoint;
    @Value("${api.persistence.version}")
    private String apiVersion;

    private String taskUrl;
    private String projectUrl;
    private String userUrl;
    private String groupUrl;

    @Before
    public void init() {
        taskUrl = "/" + apiVersion + "/" + taskEntrypoint;
        projectUrl = "/" + apiVersion + "/" + projectEntrypoint;
        userUrl = "/" + apiVersion + "/" + userEntrypoint;
        groupUrl = "/" + apiVersion + "/" + groupEntrypoint;
    }

    @Test
    public void testConstraints() {
        ResponseEntity<UserGroup> userGroupResponse = restTemplate.exchange(groupUrl, HttpMethod.POST, new HttpEntity<UserGroup>(new UserGroup("User Group 1"), Utils.authenticate("admin", "admin")), UserGroup.class);
        ResponseEntity<User> userResponse = restTemplate.exchange(userUrl, HttpMethod.POST, new HttpEntity<User>(new User("User 1", "User 1").withoutUserGroup(userGroupResponse.getBody()), Utils.authenticate("admin", "admin")), User.class);
        ResponseEntity<Project> projectResponse = restTemplate.exchange(projectUrl, HttpMethod.POST, new HttpEntity<Project>(new Project(userGroupResponse.getBody(), "Project 2", "Description 2"), Utils.authenticate("admin", "admin")), Project.class);
        ResponseEntity<Task> taskResponse = restTemplate.exchange(taskUrl, HttpMethod.POST, new HttpEntity<Task>(new Task(userResponse.getBody(), projectResponse.getBody(), "Task 2", "Description 1", 0d, 1d, 2d, 2d), Utils.authenticate("admin", "admin")), Task.class);

        assertEquals(new Long(1), restTemplate.exchange(groupUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(2), restTemplate.exchange(userUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(1), restTemplate.exchange(projectUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(1), restTemplate.exchange(taskUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());

        //should not be able to delete user groups with associated projects / users
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(groupUrl + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, userGroupResponse.getBody().getUserGroupId());
        assertEquals(HttpStatus.CONFLICT, deleteResponse.getStatusCode());

        //should not be able to delete projects with associated tasks
        deleteResponse = restTemplate.exchange(projectUrl + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, projectResponse.getBody().getProjectId());
        assertEquals(HttpStatus.CONFLICT, deleteResponse.getStatusCode());

        //should not be able to delete users with associated tasks
        deleteResponse = restTemplate.exchange(userUrl + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, userResponse.getBody().getUserId());
        assertEquals(HttpStatus.CONFLICT, deleteResponse.getStatusCode());

        assertEquals(new Long(1), restTemplate.exchange(groupUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(2), restTemplate.exchange(userUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(1), restTemplate.exchange(projectUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(1), restTemplate.exchange(taskUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());

        //should be able to delete tasks
        deleteResponse = restTemplate.exchange(taskUrl + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, taskResponse.getBody().getTaskId());
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        assertEquals(new Long(1), restTemplate.exchange(groupUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(2), restTemplate.exchange(userUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(1), restTemplate.exchange(projectUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(0), restTemplate.exchange(taskUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());

        //should be able to delete projects if there are no associated tasks
        deleteResponse = restTemplate.exchange(projectUrl + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, projectResponse.getBody().getProjectId());
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        assertEquals(new Long(1), restTemplate.exchange(groupUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(2), restTemplate.exchange(userUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(0), restTemplate.exchange(projectUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(0), restTemplate.exchange(taskUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());

        //should be able to delete users if there are no associated tasks
        deleteResponse = restTemplate.exchange(userUrl + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, userResponse.getBody().getUserId());
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        assertEquals(new Long(1), restTemplate.exchange(groupUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(1), restTemplate.exchange(userUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(0), restTemplate.exchange(projectUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(0), restTemplate.exchange(taskUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());

        //should be able to delete user groups if there are non associated users
        deleteResponse = restTemplate.exchange(groupUrl + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, userGroupResponse.getBody().getUserGroupId());
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        assertEquals(new Long(0), restTemplate.exchange(groupUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(1), restTemplate.exchange(userUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(0), restTemplate.exchange(projectUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
        assertEquals(new Long(0), restTemplate.exchange(taskUrl + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class).getBody());
    }

}
