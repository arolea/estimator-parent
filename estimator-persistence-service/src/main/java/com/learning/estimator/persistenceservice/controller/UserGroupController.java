package com.learning.estimator.persistenceservice.controller;

import com.learning.estimator.datapublisher.service.IDataPublisher;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.persistence.facade.server.PersistenceFacadeServerSide;
import com.learning.estimator.persistence.operations.GroupOperations;
import com.learning.estimator.persistence.service.client.IUserGroupServiceClientSide;
import com.learning.estimator.persistenceservice.utils.BracketsLink;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

/**
 * Exposes persistence operations for user groups
 *
 * @author rolea
 */
@RestController
@RequestMapping("/v1/groups")
@Api(value = "Groups API")
public class UserGroupController implements IUserGroupServiceClientSide {

    public static final String DISCOVER_GROUP_API = "discover_group_api";
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
    @ApiOperation(value = "Discovery", notes = "Discovery the Groups API based on user role")
    public Resource<String> discoverApi(@RequestParam(value = "userRole") UserRole role) {
        List<Link> links = new LinkedList<>();
        switch (role) {
            case ROLE_ADMIN:
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/groups").withRel(GroupOperations.SAVE_USER_GROUP.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/groups/{groupId}").withRel(GroupOperations.FIND_USER_GROUP.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/groups/findByName/?name={name}").withRel(GroupOperations.FIND_USER_GROUP_BY_NAME.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/groups/").withRel(GroupOperations.FIND_ALL_USES_GROUPS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/groups/paginate/?pageIndex={pageIndex}&pageSize={pageSize}").withRel(GroupOperations.PAGINATE_USER_GROUPS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/groups/{groupId}").withRel(GroupOperations.DELETE_USER_GROUP.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/groups/").withRel(GroupOperations.DELETE_ALL_USER_GROUPS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/groups/count").withRel(GroupOperations.COUNT_USES_GROUPS.getId())));
                break;
            case ROLE_USER:
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/groups").withRel(GroupOperations.FIND_ALL_USES_GROUPS.getId())));
                break;
        }
        return new Resource<String>("", links);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create or Update a Group entity", notes = "Performs an insert or update ( POST request ) for a Group entity")
    @HystrixCommand
    @RolesAllowed(value = {"ROLE_ADMIN"})
    @Override
    public UserGroup saveUserGroup(@RequestBody @Valid UserGroup group) {
        group = facade.saveUserGroup(group);
        if (group.getVersion() == 0l) dataPublisher.publishGroup(group);
        return group;
    }

    @RequestMapping(value = "/{groupId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find Group by id", notes = "Performs a GET request for the Group with the given id")
    @HystrixCommand
    @Override
    public UserGroup findUserGroup(@PathVariable("groupId") Long id) {
        return facade.findUserGroup(id);
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find Group by name", notes = "Performs a GET request for the Group with the given name")
    @HystrixCommand
    @Override
    public UserGroup findUserGroupByName(@RequestParam(value = "name") String name) {
        return facade.findUserGroupByName(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all Groups", notes = "Performs a GET request for all the Group entities")
    @HystrixCommand
    @Override
    public List<UserGroup> findAllUserGroups() {
        return facade.findAllUserGroups();
    }

    @RequestMapping(value = "/paginate", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Paginate Groups", notes = "Performs a GET request for a specific page of Group entities")
    @HystrixCommand
    @Override
    public List<UserGroup> findUserGroups(@RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {
        return facade.findUserGroups(pageIndex, pageSize);
    }

    @RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete Group by id", notes = "Performs a DELETE request for the Group entity with the specified id")
    @HystrixCommand
    @RolesAllowed(value = {"ROLE_ADMIN"})
    @Override
    public void deleteUserGroup(@PathVariable("groupId") Long id) {
        facade.deleteUserGroup(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete all Groups", notes = "Performs a DELETE request for all the Group entities")
    @HystrixCommand
    @RolesAllowed(value = {"ROLE_ADMIN"})
    @Override
    public void deleteAllUserGroups() {
        facade.deleteAllUserGroups();
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Count Groups", notes = "Performs a GET request that counts all the Group entities")
    @HystrixCommand
    @Override
    public Long countUserGroups() {
        return facade.countUserGroups();
    }

}
