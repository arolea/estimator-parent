package com.learning.estimator.persistencesd;

import com.learning.estimator.common.exceptions.persistence.EntityInConflictingStateException;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.persistence.config.server.CorePersistenceConfigServerSide;
import com.learning.estimator.persistence.facade.server.PersistenceFacadeServerSide;
import com.learning.estimator.persistencesd.config.JpaPersistenceConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

//import static org.junit.Assert.fail;


/**
 * Project persistence tests
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JpaPersistenceConfig.class, CorePersistenceConfigServerSide.class})
//refresh H2 after each test
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class ProjectPersistenceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Autowired
    private PersistenceFacadeServerSide facade;

    @Test
    public void testProjectPersistence() {
        UserGroup adminGroup = facade.saveUserGroup(new UserGroup("Admin group"));
        Project project = facade.saveProject(new Project(adminGroup, "Main project", "Description"));

        assertThat(project.getProjectId()).as("Id gets generated on save").isNotNull();
        assertThat(project).as("Test fetch by id").isEqualTo(facade.findProject(project.getProjectId()))
                .as("Test fetch by name").isEqualTo(facade.findProjectByName(project.getProjectName()));

        for (int i = 0; i < 3; i++)
            facade.saveProject(new Project(adminGroup, "Project" + i, "Description" + i));

        List<Project> allProjects = facade.findAllProjects();
        assertThat(allProjects.size()).as("All projects get fetched by findAll").isEqualTo(4);

        //test eager fetch for find all
        try {
            //should not fail with lazy load exception
            allProjects.forEach(System.out::println);
        } catch (Exception e) {
            fail();
        }

        for (int i = 0; i < 2; i++) {
            assertThat(facade.findProjects(i, 2).size()).as("Page size should be 2").isEqualTo(2);
            try {
                //test eager fetch for paging
                facade.findProjects(i, 2).forEach(System.out::println);
            } catch (Exception e) {
                fail();
            }
        }

        facade.deleteProject(project.getProjectId());
        assertThat(facade.countProjects()).as("Project count after one delete should be 3").isEqualTo(3);
        facade.deleteAllProjects();
        assertThat(facade.countProjects()).as("Project count after delete all should be 0").isEqualTo(0);

        //should be able to delete user groups that have no associated projects
        try {
            facade.deleteAllUserGroups();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUserGroupConsistencyForProjects() {
        UserGroup adminGroup = facade.saveUserGroup(new UserGroup("Admin group"));
        facade.saveProject(new Project(adminGroup, "Main project", "Description"));
        //should not be able to delete an user group that still has associated projects
        exception.expect(EntityInConflictingStateException.class);
        facade.deleteUserGroup(adminGroup.getUserGroupId());
    }

}
