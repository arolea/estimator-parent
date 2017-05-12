package com.learning.estimator.persistenceservice.tests;

import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserGroup;
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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for task persistence operations via REST controller
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class TaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PersistenceFacadeClientSide facade;

    @Value("${api.persistence.task.entrypoint}")
    private String entrypoint;
    @Value("${api.persistence.version}")
    private String apiVersion;

    private String url;

    @Before
    public void init() {
        url = "/" + apiVersion + "/" + entrypoint;
    }

    @Test
    public void testTaskController() {
        UserGroup group = facade.saveUserGroup(new UserGroup("User Group 1"));
        User user = facade.saveUser(new User("User", "User"));
        Project project = facade.saveProject(new Project(group, "Project 1", "Description 1"));

        Task task = new Task(user, project, "Task 1", "Description 1", 0d, 1d, 2d, 2d);
        ResponseEntity<Task> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Task>(task, Utils.authenticate("admin", "admin")), Task.class);
        task = response.getBody();
        assertNotNull(task.getTaskId());
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Task>(new Task(user, project, "Task 2", "Description 1", 0d, 1d, 2d, 2d), Utils.authenticate("admin", "admin")), Task.class);
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Task>(new Task(user, project, "Task 3", "Description 1", 0d, 1d, 2d, 2d), Utils.authenticate("admin", "admin")), Task.class);

        //test count
        ResponseEntity<Long> counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(3), counter.getBody());

        //test find and findAll
        response = restTemplate.exchange(url + "/{id}", HttpMethod.GET, new HttpEntity<Task>(Utils.authenticate("admin", "admin")), Task.class, task.getTaskId());
        assertEquals(task, response.getBody());

        ResponseEntity<Task[]> allTasks = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class);
        assertEquals(3, allTasks.getBody().length);

        //test update
        task.setTaskName("New name");
        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Task>(task, Utils.authenticate("admin", "admin")), Task.class);
        assertEquals(new Long(1), response.getBody().getVersion());

        //test delete and deleteAll
        restTemplate.exchange(url + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, task.getTaskId());
        counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(2), counter.getBody());

        restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class);
        counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(0), counter.getBody());

        //test pagination
        for (int i = 0; i < 10; i++)
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Task>(new Task(user, project, "Task " + i, "Description 1", 0d, 1d, 2d, 2d), Utils.authenticate("admin", "admin")), Task.class);
        for (int i = 0; i < 5; i++) {
            ResponseEntity<Task[]> orders = restTemplate.exchange(url + "/paginate/?pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Task[].class, i, 2);
            assertEquals(2, orders.getBody().length);
        }
    }

    /**
     * Tests that demonstrates only the fields annotated with the
     * {@link com.learning.estimator.model.views.EstimatorViews.Summary Summary}
     * view are returned from the /summary endpoint.
     */
    @Test
    public void testJacksonView() {
        UserGroup group = facade.saveUserGroup(new UserGroup("User Group 1"));
        User user = facade.saveUser(new User("User", "User"));
        Project project = facade.saveProject(new Project(group, "Project 1", "Description 1"));
        facade.saveTask(new Task(user, project, "Task 1", "Description 1", 0d, 1d, 2d, 2d));
        facade.saveTask(new Task(user, project, "Task 2", "Description 1", 0d, 1d, 2d, 2d));
        facade.saveTask(new Task(user, project, "Task 3", "Description 1", 0d, 1d, 2d, 2d));

        ResponseEntity<Task[]> allTasks = restTemplate.exchange(url + "/summary", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class);

        Arrays.stream(allTasks.getBody()).forEach(taskSummary -> {
            assertThat(taskSummary.getTaskId()).as("Task id does not get serialized in summary").isNull();
            assertThat(taskSummary.getTaskName()).as("Task name gets serialized in summary").isNotNull();

            assertThat(taskSummary.getUser().getUserId()).as("User id does not get serialized in summary").isNull();
            assertThat(taskSummary.getUser().getUsername()).as("User name does get serialized in summary").isNotNull();

            assertThat(taskSummary.getProject().getProjectId()).as("Project id does not get serialized in summary").isNull();
            assertThat(taskSummary.getProject().getProjectName()).as("Project name does get serialized in summary").isNotNull();

            assertThat(taskSummary.getProject().getUserGroup().getUserGroupId()).as("Group id does not get serialized in summary").isNull();
            assertThat(taskSummary.getProject().getUserGroup().getUserGroupName()).as("Group name does get serialized in summary").isNotNull();
        });
    }

    @Test
    public void testTaskControllerValidation() {
        UserGroup group = facade.saveUserGroup(new UserGroup("User Group 1"));
        User user = facade.saveUser(new User("User", "User"));
        Project project = facade.saveProject(new Project(group, "Project 1", "Description 1"));

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Task>(new Task(null, project, "Task 1", "Description 1", 0d, 1d, 2d, 2d), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Task>(new Task(user, null, "Task 1", "Description 1", 0d, 1d, 2d, 2d), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Task>(new Task(user, project, null, "Description 1", 0d, 1d, 2d, 2d), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
