package com.learning.estimator.persistenceservice.controller;

import com.learning.estimator.datapublisher.service.IDataPublisher;
import com.learning.estimator.model.entities.*;
import com.learning.estimator.persistence.facade.server.PersistenceFacadeServerSide;
import com.learning.estimator.persistence.operations.UserOperations;
import com.learning.estimator.persistence.service.client.IUserServiceClientSide;
import com.learning.estimator.persistenceservice.config.security.EstimatorUserDetails;
import com.learning.estimator.persistenceservice.utils.BracketsLink;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Exposes persistence operations for users
 *
 * @author rolea
 */
@RestController
@RequestMapping("/v1/users")
@Api(value = "Users API")
public class UserController implements IUserServiceClientSide {

    public static final String DISCOVER_USER_API = "discover_user_api";
    @Autowired
    private PersistenceFacadeServerSide facade;
    @Autowired
    private IDataPublisher dataPublisher;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${api.persistence.version}")
    private String apiVersion;

    @RequestMapping(value = "/discover", method = RequestMethod.OPTIONS)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Discovery", notes = "Discovery the Users API based on user role")
    public Resource<String> discoverApi(@RequestParam(value = "userRole") UserRole role) {
        List<Link> links = new LinkedList<>();
        switch (role) {
            case ROLE_ADMIN:
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users").withRel(UserOperations.SAVE_USER.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/{userId}").withRel(UserOperations.FIND_USER.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/findByName/?name={name}").withRel(UserOperations.FIND_USER_BY_NAME.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users").withRel(UserOperations.FIND_ALL_USERS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/paginate/?pageIndex={pageIndex}&pageSize={pageSize}").withRel(UserOperations.PAGINATE_USERS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/{userId}").withRel(UserOperations.DELETE_USER.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users").withRel(UserOperations.DELETE_ALL_USERS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/count").withRel(UserOperations.COUNT_USERS.getId())));
                break;
            case ROLE_USER:
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/findPrincipal").withRel(UserOperations.FIND_ALL_USERS.getId())));
                break;
        }
        return new Resource<String>("", links);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create or Update an User entity", notes = "Performs an insert or update ( POST request ) for an User entity")
    @HystrixCommand
    @RolesAllowed(value = {"ROLE_ADMIN"})
    @Override
    public User saveUser(@RequestBody @Valid User user) {
        user = facade.saveUser(user);
        if (user.getVersion() == 0l) dataPublisher.publishUser(user);
        return user;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find User by id", notes = "Performs a GET request for the User with the given id")
    @HystrixCommand
    @Override
    public User findUser(@PathVariable("userId") Long id) {
        return facade.findUser(id);
    }

    @RequestMapping(value = "/findPrincipal", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find Principal", notes = "Performs a GET request for the Principal. The response is packaged as a List")
    @HystrixCommand
    public List<User> findUserById(@AuthenticationPrincipal EstimatorUserDetails principal) {
        User user = facade.findUser(principal.getUserId());
        LinkedList<User> userList = new LinkedList<>();
        userList.add(user);
        return userList;
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find User by name", notes = "Performs a GET request for the User with the given name")
    @HystrixCommand
    @Override
    public User findUserByUsername(@RequestParam(value = "name") String username) {
        return facade.findUserByUsername(username);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all Users", notes = "Performs a GET request for all the User entities")
    @HystrixCommand
    @Override
    public List<User> findAllUsers() {
        return facade.findAllUsers();
    }

    @RequestMapping(value = "/paginate", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Paginate Users", notes = "Performs a GET request for a specific page of User entities")
    @HystrixCommand
    @Override
    public List<User> findUsers(@RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {
        return facade.findUsers(pageIndex, pageSize);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete User by id", notes = "Performs a DELETE request for the User entity with the specified id")
    @HystrixCommand
    @RolesAllowed(value = {"ROLE_ADMIN"})
    @Override
    public void deleteUser(@PathVariable("userId") Long id) {
        facade.deleteUser(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete all Users", notes = "Performs a DELETE request for all the User entities")
    @HystrixCommand
    @RolesAllowed(value = {"ROLE_ADMIN"})
    @Override
    public void deleteAllUsers() {
        facade.deleteAllUsers();
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Count Users", notes = "Performs a GET request that counts all the User entities")
    @HystrixCommand
    @Override
    public Long countUsers() {
        return facade.countUsers();
    }

    //methods used by users ( with 'user' role ) for paging tasks

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all tasks for user", notes = "Performs a GET request that fetches all the tasks for the specified user")
    @HystrixCommand
    public List<Task> getAllTasksForCurrentUser(@AuthenticationPrincipal EstimatorUserDetails principal) {
        return facade.findAllTasksByUser(principal.getUserId());
    }

    @RequestMapping(value = "/tasks/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Count tasks for user", notes = "Performs a GET request that counts all the tasks for the specified user")
    @HystrixCommand
    public Long countTasksForUser(@AuthenticationPrincipal EstimatorUserDetails principal) {
        return facade.countTasksByUser(principal.getUserId());
    }

    @RequestMapping(value = "/tasks/paginate", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find tasks for user", notes = "Performs a GET request that fetches a page of tasks for the specified user")
    @HystrixCommand
    public List<Task> getTasksForCurrentUser(@AuthenticationPrincipal EstimatorUserDetails principal, @RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {
        return facade.findTasksByUser(principal.getUserId(), pageIndex, pageSize);
    }

    @RequestMapping(value = "/tasks/paginateByCriteria", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find tasks for user by criteria", notes = "Performs a GET request that fetches a page of tasks matching the specified criteria for the specified user")
    @HystrixCommand
    public List<Task> getTasksForCurrentUserWithCriteria(@AuthenticationPrincipal EstimatorUserDetails principal, @RequestParam(value = "projectId") Long projectId, @RequestParam(value = "status") TaskStatus status, @RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {
        return facade.findTasksByPredicate(principal.getUserId(), projectId, status, pageIndex, pageSize);
    }

    @RequestMapping(value = "/tasks/countByCriteria", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Count tasks for user by criteria", notes = "Performs a GET request that counts all the tasks matching the specified criteria for the specified user")
    @HystrixCommand
    public Long countTasksForCurrentUserWithCriteria(@AuthenticationPrincipal EstimatorUserDetails principal, @RequestParam(value = "projectId") Long projectId, @RequestParam(value = "status") TaskStatus status) {
        return facade.countTasksByPredicate(principal.getUserId(), projectId, status);
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete all tasks for user", notes = "Performs a DELETE request for all the tasks associated with the specified user")
    @HystrixCommand
    public void deleteAllTasksForCurrentUser(@AuthenticationPrincipal EstimatorUserDetails principal) {
        facade.deleteAllTasksByUserId(principal.getUserId());
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all projects for user", notes = "Performs a GET request that returns all the projects that have an associated user group in which the specified user is a member")
    @HystrixCommand
    public List<Project> getAllProjectsForCurrentUser(@AuthenticationPrincipal EstimatorUserDetails principal) {
        return facade.findAllProjectsForUser(principal.getUserId());
    }

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all groups for user", notes = "Performs a GET request that returns all the groups in which the specified user is a member")
    @HystrixCommand
    public Set<UserGroup> getAllGroupsForCurrentUser(@AuthenticationPrincipal EstimatorUserDetails principal) {
        return facade.findAllGroupsForUser(principal.getUserId());
    }

}
