package com.learning.estimator.persistenceservice.controller;

import com.learning.estimator.datapublisher.service.IDataPublisher;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.persistence.facade.server.PersistenceFacadeServerSide;
import com.learning.estimator.persistence.operations.ProjectOperations;
import com.learning.estimator.persistence.service.client.IProjectServiceClientSide;
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
 * Exposes persistence operations for projects
 *
 * @author rolea
 */
@RestController
@RequestMapping("/v1/projects")
@Api(value = "Projects API")
public class ProjectController implements IProjectServiceClientSide {

    public static final String DISCOVER_PROJECT_API = "discover_project_api";
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
    @ApiOperation(value = "Discovery", notes = "Discovery the Projects API based on user role")
    public Resource<String> discoverApi(@RequestParam(value = "userRole") UserRole role) {
        List<Link> links = new LinkedList<>();
        switch (role) {
            case ROLE_ADMIN:
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/projects").withRel(ProjectOperations.SAVE_PROJECT.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/projects/{projectId}").withRel(ProjectOperations.FIND_PROJECT.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/projects/findByName/?name={name}").withRel(ProjectOperations.FIND_PROJECT_BY_NAME.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/projects").withRel(ProjectOperations.FIND_ALL_PROJECTS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/projects/paginate/?pageIndex={pageIndex}&pageSize={pageSize}").withRel(ProjectOperations.PAGINATE_PROJECTS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/projects/{projectId}").withRel(ProjectOperations.DELETE_PROJECT.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/projects").withRel(ProjectOperations.DELETE_ALL_PROJECTS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/projects/count").withRel(ProjectOperations.COUNT_PROJECTS.getId())));
                break;
            case ROLE_USER:
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/projects").withRel(ProjectOperations.FIND_ALL_PROJECTS.getId())));
                break;
        }
        return new Resource<String>("", links);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create or Update a Project entity", notes = "Performs an insert ( POST request ) or update for a Project entity")
    @HystrixCommand
    @RolesAllowed(value = {"ROLE_ADMIN"})
    @Override
    public Project saveProject(@RequestBody @Valid Project project) {
        project = facade.saveProject(project);
        if (project.getVersion() == 0l) dataPublisher.publishProject(project);
        return project;
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find Project by id", notes = "Performs a GET request for the Project with the given id")
    @HystrixCommand
    @Override
    public Project findProject(@PathVariable("projectId") Long id) {
        return facade.findProject(id);
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find Project by name", notes = "Performs a GET request for the Project with the given name")
    @HystrixCommand
    @Override
    public Project findProjectByName(@RequestParam(value = "name") String name) {
        return facade.findProjectByName(name);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all Projects", notes = "Performs a GET request for all the Project entities")
    @HystrixCommand
    @Override
    public List<Project> findAllProjects() {
        return facade.findAllProjects();
    }

    @RequestMapping(value = "/paginate", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Paginate Projects", notes = "Performs a GET request for a specific page of Project entities")
    @HystrixCommand
    @Override
    public List<Project> findProjects(@RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {
        return facade.findProjects(pageIndex, pageSize);
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete Projects by id", notes = "Performs a DELETE request for the Project entity with the specified id")
    @HystrixCommand
    @RolesAllowed(value = {"ROLE_ADMIN"})
    @Override
    public void deleteProject(@PathVariable("projectId") Long id) {
        facade.deleteProject(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete all Projects", notes = "Performs a DELETE request for all the Project entities")
    @HystrixCommand
    @RolesAllowed(value = {"ROLE_ADMIN"})
    @Override
    public void deleteAllProjects() {
        facade.deleteAllProjects();
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Count Projects", notes = "Performs a GET request that counts all the Project entities")
    @HystrixCommand
    @Override
    public Long countProjects() {
        return facade.countProjects();
    }

}
