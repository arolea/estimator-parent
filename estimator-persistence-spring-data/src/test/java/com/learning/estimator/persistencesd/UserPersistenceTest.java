package com.learning.estimator.persistencesd;

import com.learning.estimator.common.exceptions.persistence.EntityInConflictingStateException;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.persistence.config.client.CorePersistenceConfigClientSide;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
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

/**
 * User persistence tests
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JpaPersistenceConfig.class, CorePersistenceConfigClientSide.class})
//refresh H2 after each test
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class UserPersistenceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Autowired
    private PersistenceFacadeClientSide facade;

    @Test
    public void testUserPersistence() {
        UserGroup adminGroup = facade.saveUserGroup(new UserGroup("Admin group"));
        User admin = facade.saveUser(new User("admin", "admin").withUserGroup(adminGroup).withUserRole(UserRole.ROLE_ADMIN));

        assertThat(admin.getUserId()).as("Id gets generated upon save").isNotNull();

        assertThat(admin).as("Test fetch by id").isEqualTo(facade.findUser(admin.getUserId()))
                .as("Test fetch by username").isEqualTo(facade.findUserByUsername(admin.getUsername()));

        for (int i = 0; i < 3; i++)
            facade.saveUser(new User("User" + i, "Password" + i));

        List<User> users = facade.findAllUsers();
        assertThat(users.size()).as("All users get fetched by findAll").isEqualTo(4);

        //test eager fetch for find all
        try {
            users.forEach(System.out::println);
        } catch (Exception e) {
            fail();
        }

        for (int i = 0; i < 2; i++) {
            assertThat(facade.findUsers(i, 2).size()).as("Each page has a size of 2").isEqualTo(2);
            //test eager fetch for paging
            try {
                facade.findUsers(i, 2).forEach(System.out::println);
            } catch (Exception e) {
                fail();
            }
        }

        facade.deleteUser(admin.getUserId());
        assertThat(facade.countUsers()).as("User count after one delete should be 3").isEqualTo(3);
        facade.deleteAllUsers();
        assertThat(facade.countUsers()).as("User count after one delete should be 3").isEqualTo(0);

        try {
            facade.deleteAllUserGroups();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testUserGroupConsistencyForUsers() {
        UserGroup adminGroup = facade.saveUserGroup(new UserGroup("Admin group"));
        facade.saveUser(new User("admin", "admin").withUserGroup(adminGroup).withUserRole(UserRole.ROLE_ADMIN));
        //should not be able to delete an user group that still has associated users
        exception.expect(EntityInConflictingStateException.class);
        facade.deleteUserGroup(adminGroup.getUserGroupId());
    }

}
