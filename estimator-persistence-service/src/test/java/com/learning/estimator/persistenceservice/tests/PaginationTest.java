package com.learning.estimator.persistenceservice.tests;

import com.learning.estimator.model.entities.*;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Pagination tests
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class PaginationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PersistenceFacadeClientSide facade;

    @Value("${api.persistence.task.entrypoint}")
    private String taskEntrypoint;
    @Value("${api.persistence.user.entrypoint}")
    private String userEntrypoint;
    @Value("${api.persistence.version}")
    private String apiVersion;

    private String taskUrl;
    private String userUrl;

    @Before
    public void init() {
        taskUrl = "/" + apiVersion + "/" + taskEntrypoint;
        userUrl = "/" + apiVersion + "/" + userEntrypoint;
    }

    @Test
    public void testPaging() {
        UserGroup group1 = facade.saveUserGroup(new UserGroup("Group 1"));
        UserGroup group2 = facade.saveUserGroup(new UserGroup("Group 2"));

        BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

        User user1 = facade.saveUser(new User("user1", bcryptEncoder.encode("user1")).withUserGroup(group1).withUserRole(UserRole.ROLE_ADMIN));
        User user2 = facade.saveUser(new User("user2", bcryptEncoder.encode("user2")).withUserGroup(group2).withUserRole(UserRole.ROLE_ADMIN));
        User user3 = facade.saveUser(new User("user3", bcryptEncoder.encode("user3")).withUserGroup(group2).withUserRole(UserRole.ROLE_USER));

        Project project1 = facade.saveProject(new Project(group1, "Project 1", "Description 1"));
        Project project2 = facade.saveProject(new Project(group1, "Project 2", "Description 2"));
        Project project3 = facade.saveProject(new Project(group2, "project 3", "Description 3"));

        for (int i = 0; i < 10; i++)
            facade.saveTask(new Task(user1, project1, "Test", "Description", 1.0, 2.0, 3.0, 5.0));
        for (int i = 0; i < 10; i++)
            facade.saveTask(new Task(user1, project2, "Test", "Description", 1.0, 2.0, 3.0, 5.0));
        for (int i = 0; i < 10; i++) {
            Task task = new Task(user1, project3, "Test", "Description", 1.0, 2.0, 3.0, 5.0);
            task.setTaskStatus(TaskStatus.IN_PROGRESS);
            facade.saveTask(task);
        }

        for (int i = 0; i < 10; i++) {
            Task task = new Task(user2, project2, "Test", "Description", 1.0, 2.0, 3.0, 5.0);
            task.setTaskStatus(TaskStatus.DONE);
            facade.saveTask(task);
        }
        for (int i = 0; i < 10; i++) {
            Task task = new Task(user2, project3, "Test", "Description", 1.0, 2.0, 3.0, 5.0);
            task.setTaskStatus(TaskStatus.DONE);
            facade.saveTask(task);
        }

        for (int i = 0; i < 10; i++) {
            Task task = new Task(user3, project3, "Test", "Description", 1.0, 2.0, 3.0, 5.0);
            facade.saveTask(task);
        }

		/*
         * Tasks for users :
		 * 		user1 : 30
		 * 		user2 : 20
		 * 		user3 : 10
		 * 
		 * Tasks for projects :
		 * 		project1 : 10
		 * 		project2 : 20
		 * 		project3 : 30
		 * 
		 * Task status :
		 * 		pending : 30
		 * 		done : 20
		 * 		in progress : 10
		 * 
		 */

        //test fetch for users
        assertEquals(30, restTemplate.exchange(taskUrl + "/findAllByUser/?userId={userId}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId()).getBody().length);
        assertEquals(20, restTemplate.exchange(taskUrl + "/findAllByUser/?userId={userId}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user2.getUserId()).getBody().length);
        assertEquals(10, restTemplate.exchange(taskUrl + "/findAllByUser/?userId={userId}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user3.getUserId()).getBody().length);

        //test eager fetch ( note that user groups are not eagerly fetched )
        Arrays.stream(restTemplate.exchange(taskUrl + "/findAllByUser/?userId={userId}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId()).getBody()).forEach(task -> System.out.println(task.getUser().getUsername() + " -> " + task.getProject().getProjectName() + " -> " + task.getTaskStatus()));

        //test fetch for projects
        assertEquals(10, restTemplate.exchange(taskUrl + "/findAllByProject/?projectId={projectId}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, project1.getProjectId()).getBody().length);
        assertEquals(20, restTemplate.exchange(taskUrl + "/findAllByProject/?projectId={projectId}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, project2.getProjectId()).getBody().length);
        assertEquals(30, restTemplate.exchange(taskUrl + "/findAllByProject/?projectId={projectId}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, project3.getProjectId()).getBody().length);

        //test eager fetch ( note that user groups are not eagerly fetched )
        Arrays.stream(restTemplate.exchange(taskUrl + "/findAllByProject/?projectId={projectId}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, project1.getProjectId()).getBody()).forEach(task -> System.out.println(task.getUser().getUsername() + " -> " + task.getProject().getProjectName() + " -> " + task.getTaskStatus()));

        //test fetch for status
        assertEquals(20, restTemplate.exchange(taskUrl + "/findAllByStatus/?status={status}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, TaskStatus.DONE).getBody().length);
        assertEquals(10, restTemplate.exchange(taskUrl + "/findAllByStatus/?status={status}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, TaskStatus.IN_PROGRESS).getBody().length);
        assertEquals(30, restTemplate.exchange(taskUrl + "/findAllByStatus/?status={status}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, TaskStatus.PENDING).getBody().length);

        //test eager fetch ( note that user groups are not eagerly fetched )
        Arrays.stream(restTemplate.exchange(taskUrl + "/findAllByStatus/?status={status}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, TaskStatus.IN_PROGRESS).getBody()).forEach(task -> System.out.println(task.getUser().getUsername() + " -> " + task.getProject().getProjectName() + " -> " + task.getTaskStatus()));

        //test paging and fetch for user based query
        for (int i = 0; i < 15; i++) {
            assertEquals(2, restTemplate.exchange(taskUrl + "/paginateByUser/?userId={userId}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), i, 2).getBody().length);
            Arrays.stream(restTemplate.exchange(taskUrl + "/paginateByUser/?userId={userId}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), i, 2).getBody()).forEach(task -> System.out.println(task.getUser().getUsername() + " -> " + task.getProject().getProjectName() + " -> " + task.getTaskStatus()));
        }

        //test paging and fetch for project based query
        for (int i = 0; i < 15; i++) {
            assertEquals(2, restTemplate.exchange(taskUrl + "/paginateByProject/?projectId={userId}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, project3.getProjectId(), i, 2).getBody().length);
            Arrays.stream(restTemplate.exchange(taskUrl + "/paginateByProject/?projectId={userId}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, project3.getProjectId(), i, 2).getBody()).forEach(task -> System.out.println(task.getUser().getUsername() + " -> " + task.getProject().getProjectName() + " -> " + task.getTaskStatus()));
        }

        //test paging and fetch for task status based query
        for (int i = 0; i < 15; i++) {
            assertEquals(2, restTemplate.exchange(taskUrl + "/paginateByStatus/?status={status}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, TaskStatus.PENDING, i, 2).getBody().length);
            Arrays.stream(restTemplate.exchange(taskUrl + "/paginateByStatus/?status={status}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, TaskStatus.PENDING, i, 2).getBody()).forEach(task -> System.out.println(task.getUser().getUsername() + " -> " + task.getProject().getProjectName() + " -> " + task.getTaskStatus()));
        }

        //test predicates
        assertEquals(30, restTemplate.exchange(taskUrl + "/findAllByCriteria/?userId={userId}&projectId={projectId}&status={status}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), null, null).getBody().length);
        //test eager fetch for user and project
        Arrays.stream(restTemplate.exchange(taskUrl + "/findAllByCriteria/?userId={userId}&projectId={projectId}&status={status}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), null, null).getBody()).forEach(task -> System.out.println(task.getUser().getUsername() + " -> " + task.getProject().getProjectName() + " -> " + task.getTaskStatus()));
        //test lazy fetch for user groups via user - in this scenario ensure they are not fetched
        Arrays.stream(restTemplate.exchange(taskUrl + "/findAllByCriteria/?userId={userId}&projectId={projectId}&status={status}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), null, null).getBody()).forEach(task -> assertNull(task.getUser().getGroups()));


        //test compound criteria
        assertEquals(10, restTemplate.exchange(taskUrl + "/findAllByCriteria/?userId={userId}&projectId={projectId}&status={status}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), project3.getProjectId(), null).getBody().length);

        //test paging and eager fetch
        for (int i = 0; i < 5; i++) {
            assertEquals(2, restTemplate.exchange(taskUrl + "/paginateByCriteria/?userId={userId}&projectId={projectId}&status={status}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), project3.getProjectId(), null, i, 2).getBody().length);
            Arrays.stream(restTemplate.exchange(taskUrl + "/paginateByCriteria/?userId={userId}&projectId={projectId}&status={status}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), project3.getProjectId(), null, i, 2).getBody()).forEach(task -> System.out.println(task.getUser().getUsername() + " -> " + task.getProject().getProjectName() + " -> " + task.getTaskStatus()));
        }

        //test lazy fetch for paging  - in this scenario ensure they are not fetched
        Arrays.stream(restTemplate.exchange(taskUrl + "/paginateByCriteria/?userId={userId}&projectId={projectId}&status={status}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), project3.getProjectId(), null, 0, 2).getBody()).forEach(task -> assertNull(task.getUser().getGroups()));

        assertEquals(10, restTemplate.exchange(taskUrl + "/findAllByCriteria/?userId={userId}&projectId={projectId}&status={status}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), project3.getProjectId(), TaskStatus.IN_PROGRESS).getBody().length);
        assertEquals(0, restTemplate.exchange(taskUrl + "/findAllByCriteria/?userId={userId}&projectId={projectId}&status={status}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("admin", "admin")), Task[].class, user1.getUserId(), project3.getProjectId(), TaskStatus.DONE).getBody().length);

        //tests for the same functionality exposed through the user controller

        //test find all request
        assertEquals(30, restTemplate.exchange(userUrl + "/tasks", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("user1", "user1")), Task[].class).getBody().length);

        for (int i = 0; i < 15; i++) {
            assertEquals(2, restTemplate.exchange(userUrl + "/tasks/paginate/?pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("user1", "user1")), Task[].class, i, 2).getBody().length);
            Arrays.stream(restTemplate.exchange(userUrl + "/tasks/paginate/?pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("user1", "user1")), Task[].class, i, 2).getBody()).forEach(task -> System.out.println(task.getUser().getUsername() + " -> " + task.getProject().getProjectName() + " -> " + task.getTaskStatus()));
        }

        for (int i = 0; i < 5; i++) {
            assertEquals(2, restTemplate.exchange(userUrl + "/tasks/paginateByCriteria/?projectId={projectId}&status={status}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("user1", "user1")), Task[].class, project3.getProjectId(), TaskStatus.IN_PROGRESS, i, 2).getBody().length);
            Arrays.stream(restTemplate.exchange(userUrl + "/tasks/paginateByCriteria/?projectId={projectId}&status={status}&pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Task[]>(Utils.authenticate("user1", "user1")), Task[].class, project3.getProjectId(), TaskStatus.IN_PROGRESS, i, 2).getBody()).forEach(task -> System.out.println(task.getUser().getUsername() + " -> " + task.getProject().getProjectName() + " -> " + task.getTaskStatus()));
        }

        assertEquals(10, restTemplate.exchange(userUrl + "/tasks/countByCriteria/?projectId={projectId}&status={status}", HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate("user1", "user1")), Long.class, project3.getProjectId(), TaskStatus.IN_PROGRESS).getBody().longValue());

    }

}
