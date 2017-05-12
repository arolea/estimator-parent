package com.learning.estimator.persistenceservice.tests;

import com.learning.estimator.model.entities.Project;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for project persistence operations via REST controller
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class ProjectControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PersistenceFacadeClientSide facade;

    @Value("${api.persistence.project.entrypoint}")
    private String entrypoint;
    @Value("${api.persistence.version}")
    private String apiVersion;

    private String url;

    @Before
    public void init() {
        url = "/" + apiVersion + "/" + entrypoint;
    }

    @Test
    public void testProjectController() {
        UserGroup group = facade.saveUserGroup(new UserGroup("User Group 1"));

        Project project = new Project(group, "Project 1", "Description 1");
        ResponseEntity<Project> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Project>(project, Utils.authenticate("admin", "admin")), Project.class);
        project = response.getBody();
        assertNotNull(project.getProjectId());
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Project>(new Project(group, "Project 2", "Description 2"), Utils.authenticate("admin", "admin")), Project.class);
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Project>(new Project(group, "Project 3", "Description 3"), Utils.authenticate("admin", "admin")), Project.class);

        //test count
        ResponseEntity<Long> counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(3), counter.getBody());

        //test find and findAll
        response = restTemplate.exchange(url + "/{id}", HttpMethod.GET, new HttpEntity<Project>(Utils.authenticate("admin", "admin")), Project.class, project.getProjectId());
        assertEquals(project, response.getBody());

        ResponseEntity<Project[]> allGroups = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Project[]>(Utils.authenticate("admin", "admin")), Project[].class);
        assertEquals(3, allGroups.getBody().length);

        //test update
        project.setProjectName("New name");
        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Project>(project, Utils.authenticate("admin", "admin")), Project.class);
        assertEquals(new Long(1), response.getBody().getVersion());

        //test delete and deleteAll
        restTemplate.exchange(url + "/{id}", HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class, project.getProjectId());
        counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(2), counter.getBody());

        restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Void.class);
        counter = restTemplate.exchange(url + "/count", HttpMethod.GET, new HttpEntity<Long>(Utils.authenticate("admin", "admin")), Long.class);
        assertEquals(new Long(0), counter.getBody());

        //test pagination
        for (int i = 0; i < 10; i++)
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Project>(new Project(group, "Project" + i, "Description" + i), Utils.authenticate("admin", "admin")), Project.class);
        for (int i = 0; i < 5; i++) {
            ResponseEntity<Project[]> orders = restTemplate.exchange(url + "/paginate/?pageIndex={pageIndex}&pageSize={pageSize}", HttpMethod.GET, new HttpEntity<Void>(Utils.authenticate("admin", "admin")), Project[].class, i, 2);
            assertEquals(2, orders.getBody().length);
        }
    }

    @Test
    public void testProjectControllerValidation() {
        UserGroup group = facade.saveUserGroup(new UserGroup("User Group 1"));

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Project>(new Project(null, "Project 1", "Description 1"), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Project>(new Project(group, null, "Description 1"), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Project>(new Project(group, "Project 1", null), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testProjectControllerConstraints() {
        UserGroup group = facade.saveUserGroup(new UserGroup("User Group 1"));

        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Project>(new Project(group, "Project 1", "Description 1"), Utils.authenticate("admin", "admin")), Void.class);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Project>(new Project(group, "Project 1", "Description 1"), Utils.authenticate("admin", "admin")), Void.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
