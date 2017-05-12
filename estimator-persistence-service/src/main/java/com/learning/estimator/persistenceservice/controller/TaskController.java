package com.learning.estimator.persistenceservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.learning.estimator.datapublisher.service.IDataPublisher;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.TaskStatus;
import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.model.views.EstimatorViews;
import com.learning.estimator.persistence.facade.server.PersistenceFacadeServerSide;
import com.learning.estimator.persistence.operations.TaskOperations;
import com.learning.estimator.persistence.service.client.ITaskServiceClientSide;
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
 * Exposes persistence operations for tasks
 *
 * @author rolea
 */
@RestController
@RequestMapping("/v1/tasks")
@Api(value = "Tasks API")
public class TaskController implements ITaskServiceClientSide {

    public static final String DISCOVER_TASK_API = "discover_task_api";
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
    @ApiOperation(value = "Discovery", notes = "Discovery the Tasks API based on user role")
    public Resource<String> discoverApi(@RequestParam(value = "userRole") UserRole role) {
        List<Link> links = new LinkedList<>();
        links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks").withRel(TaskOperations.SAVE_TASK.getId())));
        links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/{taskId}").withRel(TaskOperations.FIND_TASK.getId())));
        links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/{taskId}").withRel(TaskOperations.DELETE_TASK.getId())));
        switch (role) {
            case ROLE_ADMIN:
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks").withRel(TaskOperations.DELETE_ALL_TASKS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/count").withRel(TaskOperations.COUNT_TASKS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/countByUser/?userId={userId}").withRel(TaskOperations.COUNT_TASKS_BY_USER.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/countByProject/?projectId={projectId}").withRel(TaskOperations.COUNT_TASKS_BY_PROJECT.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/countByStatus/?status={status}").withRel(TaskOperations.COUNT_TASKS_BY_STATUS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/countByCriteria/?userId={userId}&projectId={projectId}&status={status}").withRel(TaskOperations.COUNT_TASKS_BY_CRITERIA.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks").withRel(TaskOperations.FIND_ALL_TASKS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/findAllByUser/?userId={userId}").withRel(TaskOperations.FIND_ALL_BY_USER.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/findAllByProject/?projectId={projectId}").withRel(TaskOperations.FIND_ALL_BY_PROJECT.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/findAllByStatus/?status={status}").withRel(TaskOperations.FIND_ALL_BY_STATUS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/findAllByCriteria/?userId={userId}&projectId={projectId}&status={status}").withRel(TaskOperations.FIND_ALL_BY_CRITERIA.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/paginate/?pageIndex={pageIndex}&pageSize={pageSize}").withRel(TaskOperations.PAGINATE_TASKS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/paginateByUser/?userId={userId}&pageIndex={pageIndex}&pageSize={pageSize}").withRel(TaskOperations.PAGINATE_BY_USER.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/paginateByProject/?projectId={projectId}&pageIndex={pageIndex}&pageSize={pageSize}").withRel(TaskOperations.PAGINATE_BY_PROJECT.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/paginateByStatus/?status={status}&pageIndex={pageIndex}&pageSize={pageSize}").withRel(TaskOperations.PAGINATE_BY_STATUS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/paginateByCriteria/?userId={userId}&projectId={projectId}&status={status}&pageIndex={pageIndex}&pageSize={pageSize}").withRel(TaskOperations.PAGINATE_BY_CRITERIA.getId())));
                break;
            case ROLE_USER:
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/tasks").withRel(TaskOperations.DELETE_ALL_TASKS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/tasks/count").withRel(TaskOperations.COUNT_TASKS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/tasks").withRel(TaskOperations.FIND_ALL_TASKS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/tasks/paginate/?pageIndex={pageIndex}&pageSize={pageSize}").withRel(TaskOperations.PAGINATE_TASKS.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/tasks/paginateByCriteria/?projectId={projectId}&status={status}&pageIndex={pageIndex}&pageSize={pageSize}").withRel(TaskOperations.PAGINATE_BY_CRITERIA.getId())));
                links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/tasks/countByCriteria/?projectId={projectId}&status={status}").withRel(TaskOperations.COUNT_TASKS_BY_CRITERIA.getId())));
                break;
        }
        return new Resource<String>("", links);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create or Update a Task entity", notes = "Performs an insert or update ( POST request ) for a Task entity")
    @HystrixCommand
    @Override
    public Task saveTask(@RequestBody @Valid Task task) {
        task = facade.saveTask(task);
        if (TaskStatus.DONE.equals(task.getTaskStatus())) dataPublisher.publishTask(task);
        return task;
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find Task by id", notes = "Performs a GET request for the Task with the given id")
    @HystrixCommand
    @Override
    public Task findTask(@PathVariable("taskId") Long id) {
        return facade.findTask(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all Tasks", notes = "Performs a GET request for all the Tasks entities")
    @HystrixCommand
    @Override
    public List<Task> findAllTasks() {
        return facade.findAllTasks();
    }

    @RequestMapping(value = "/findAllByCriteria", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all Tasks matching criteria", notes = "Performs a GET request for all the Tasks entities that match the specified criteria")
    @HystrixCommand
    @Override
    public List<Task> findAllTasksByPredicate(@RequestParam(value = "userId") Long userId, @RequestParam(value = "projectId") Long projectId, @RequestParam(value = "status") TaskStatus status) {
        return facade.findAllTasksByPredicate(userId, projectId, status);
    }

    @RequestMapping(value = "/findAllByUser", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all Tasks for user", notes = "Performs a GET request for all the Tasks entities for the specified user")
    @HystrixCommand
    @Override
    public List<Task> findAllTasksByUser(@RequestParam(value = "userId") Long userId) {
        return facade.findAllTasksByUser(userId);
    }

    @RequestMapping(value = "/findAllByProject", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all Tasks for project", notes = "Performs a GET request for all the Tasks entities for the specified project")
    @HystrixCommand
    @Override
    public List<Task> findAllTasksByProject(@RequestParam(value = "projectId") Long projectId) {
        return facade.findAllTasksByProject(projectId);
    }

    @RequestMapping(value = "/findAllByStatus", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all Tasks for status", notes = "Performs a GET request for all the Tasks entities matching the specified status")
    @HystrixCommand
    @Override
    public List<Task> findAllTasksByStatus(@RequestParam(value = "status") TaskStatus status) {
        return facade.findAllTasksByStatus(status);
    }

    @RequestMapping(value = "/paginate", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Paginate Tasks", notes = "Performs a GET request for a specific page of Task entities")
    @HystrixCommand
    @Override
    public List<Task> findTasks(@RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {
        return facade.findTasks(pageIndex, pageSize);
    }

    @RequestMapping(value = "/paginateByUser", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Paginate Tasks by user", notes = "Performs a GET request for a specific page of Task entities for the specified user")
    @HystrixCommand
    @Override
    public List<Task> findTasksByUser(@RequestParam(value = "userId") Long userId, @RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {
        return facade.findTasksByUser(userId, pageIndex, pageSize);
    }

    @RequestMapping(value = "/paginateByProject", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Paginate Tasks by project", notes = "Performs a GET request for a specific page of Task entities for the specified project")
    @HystrixCommand
    @Override
    public List<Task> findTasksByProject(@RequestParam(value = "projectId") Long projectId, @RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {
        return facade.findTasksByProject(projectId, pageIndex, pageSize);
    }

    @RequestMapping(value = "/paginateByStatus", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Paginate Tasks by status", notes = "Performs a GET request for a specific page of Task entities for the specified status")
    @HystrixCommand
    @Override
    public List<Task> findTasksByStatus(@RequestParam(value = "status") TaskStatus status, @RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {
        return facade.findTasksByStatus(status, pageIndex, pageSize);
    }

    @RequestMapping(value = "/paginateByCriteria", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Paginate Tasks by criteria", notes = "Performs a GET request for a specific page of Task entities for the specified criteria")
    @HystrixCommand
    @Override
    public List<Task> findTasksByPredicate(@RequestParam(value = "userId") Long userId, @RequestParam(value = "projectId") Long projectId, @RequestParam(value = "status") TaskStatus status, @RequestParam(value = "pageIndex") int pageIndex, @RequestParam(value = "pageSize") int pageSize) {
        return facade.findTasksByPredicate(userId, projectId, status, pageIndex, pageSize);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete Task by id", notes = "Performs a DELETE request for the Task entity with the specified id")
    @HystrixCommand
    @Override
    public void deleteTask(@PathVariable("taskId") Long id) {
        facade.deleteTask(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete all Tasks", notes = "Performs a DELETE request for all the Task entities")
    @HystrixCommand
    @RolesAllowed(value = {"ROLE_ADMIN"})
    @Override
    public void deleteAllTasks() {
        facade.deleteAllTasks();
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Count Tasks", notes = "Performs a GET request that counts all the Tasks entities")
    @HystrixCommand
    @Override
    public Long countTasks() {
        return facade.countTasks();
    }

    @RequestMapping(value = "/countByUser", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Count Projects by user", notes = "Performs a GET request that counts all the Project entities for the specified user")
    @HystrixCommand
    @Override
    public Long countTasksByUser(@RequestParam(value = "userId") Long userId) {
        return facade.countTasksByUser(userId);
    }

    @RequestMapping(value = "/countByProject", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Count Projects by project", notes = "Performs a GET request that counts all the Project entities for the specified project")
    @HystrixCommand
    @Override
    public Long countTasksByProject(@RequestParam(value = "projectId") Long projectId) {
        return facade.countTasksByProject(projectId);
    }

    @RequestMapping(value = "/countByStatus", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Count Projects by status", notes = "Performs a GET request that counts all the Project entities for the specified status")
    @HystrixCommand
    @Override
    public Long countTasksByStatus(@RequestParam(value = "status") TaskStatus status) {
        return facade.countTasksByStatus(status);
    }

    @RequestMapping(value = "/countByCriteria", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Count Projects by criteria", notes = "Performs a GET request that counts all the Project entities for the specified criteria")
    @HystrixCommand
    @Override
    public Long countTasksByPredicate(@RequestParam(value = "userId") Long userId, @RequestParam(value = "projectId") Long projectId, @RequestParam(value = "status") TaskStatus status) {
        return facade.countTasksByPredicate(userId, projectId, status);
    }

    /**
     * Exposes Jackson View integration ( only fields annotated with the Summary view get serialized )
     * Check TaskControllerTest for sample usage
     *
     * @return
     */
    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all Tasks summary", notes = "Performs a GET request for all the Tasks entities. A summary of thise entities is returned")
    @HystrixCommand
    @JsonView(EstimatorViews.Summary.class)
    public List<Task> findAllTasksSummary() {
        return facade.findAllTasks();
    }
}
