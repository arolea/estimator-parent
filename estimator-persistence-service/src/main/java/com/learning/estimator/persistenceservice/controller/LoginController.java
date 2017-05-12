package com.learning.estimator.persistenceservice.controller;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.model.infrastructure.LoginOutcome;
import com.learning.estimator.model.infrastructure.Outcome;
import com.learning.estimator.persistence.facade.server.PersistenceFacadeServerSide;
import com.learning.estimator.persistenceservice.config.security.EstimatorUserDetails;
import com.learning.estimator.persistenceservice.utils.BracketsLink;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Exposes login / logout operations
 *
 * @author rolea
 */
@RestController
@RequestMapping("/${api.persistence.version}")
@Api(value = "Login API")
public class LoginController {

    private static final ILogger LOG = LogManager.getLogger(LoginController.class);
    @Autowired
    private PersistenceFacadeServerSide infrastructureFacade;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${api.persistence.version}")
    private String apiVersion;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Login", notes = "Login request with the specified credentials")
    public Resource<LoginOutcome> login(@AuthenticationPrincipal EstimatorUserDetails principal) {
        LOG.info("The user " + principal.getUsername() + " performed a succesfull login!");
        User user = infrastructureFacade.findUserByUsername(principal.getUsername());
        List<Link> links = new LinkedList<>();
        //login success , append links based on role
        if (user.getRoles().contains(UserRole.ROLE_ADMIN)) {
            links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/groups/discover/?userRole=" + UserRole.ROLE_ADMIN).withRel(UserGroupController.DISCOVER_GROUP_API)));
            links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/discover/?userRole=" + UserRole.ROLE_ADMIN).withRel(UserController.DISCOVER_USER_API)));
            links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/projects/discover/?userRole=" + UserRole.ROLE_ADMIN).withRel(ProjectController.DISCOVER_PROJECT_API)));
            links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/discover/?userRole=" + UserRole.ROLE_ADMIN).withRel(TaskController.DISCOVER_TASK_API)));
        } else {
            links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/groups/discover/?userRole=" + UserRole.ROLE_USER).withRel(UserGroupController.DISCOVER_GROUP_API)));
            links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/users/discover/?userRole=" + UserRole.ROLE_USER).withRel(UserController.DISCOVER_USER_API)));
            links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/projects/discover/?userRole=" + UserRole.ROLE_USER).withRel(ProjectController.DISCOVER_PROJECT_API)));
            links.add(new BracketsLink(new Link(applicationName + "/" + apiVersion + "/tasks/discover/?userRole=" + UserRole.ROLE_USER).withRel(TaskController.DISCOVER_TASK_API)));
        }
        return new Resource<LoginOutcome>(new LoginOutcome(Outcome.SUCCESS, user, "Login successfull!"), links);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Logout", notes = "Logout for the current user")
    public void logout(@RequestBody Long userId) {
        LOG.info("The user with id " + userId + " logged out!");
    }

}
