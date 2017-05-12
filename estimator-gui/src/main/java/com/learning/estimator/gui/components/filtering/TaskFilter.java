package com.learning.estimator.gui.components.filtering;

import com.learning.estimator.gui.components.representations.TaskRepresentation;
import com.learning.estimator.gui.core.AbstractFilteringComponent;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.TaskStatus;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.button.MButton.MClickListener;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Abstracts task filtering
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TaskFilter extends AbstractFilteringComponent {

    @Autowired
    private PersistenceFacadeClientSide facade;
    @Autowired
    private TaskRepresentation representation;
    @Autowired
    private VaadinSecurity security;
    @Autowired
    private VaadinSession session;

    @PostConstruct
    public void init() {
        Arrays.stream(TaskFilteringField.values()).forEach(field -> this.addComboBox(field.getId(), field.getCaption(), field.getDescription()));
        buildContent("Filter", "Filter tasks by the given criteria", "Filter tasks!");
    }

    public void initComboValues() {
        this.setComboValues(TaskFilteringField.USER.getId(), facade.findAllUsers());
        this.setComboValues(TaskFilteringField.PROJECT.getId(), facade.findAllProjects());
        this.setComboValues(TaskFilteringField.STATUS.getId(), Arrays.stream(TaskStatus.values()).collect(Collectors.toList()));
    }

    @Override
    protected MClickListener getClickListener() {
        return () -> {
            filterTasks();
        };
    }

    public void filterTasks() {
        User user = (User) this.getComboBoxValue(TaskFilteringField.USER.getId());
        Project project = (Project) this.getComboBoxValue(TaskFilteringField.PROJECT.getId());
        TaskStatus status = (TaskStatus) this.getComboBoxValue(TaskFilteringField.STATUS.getId());

        if (user == null && project == null && status == null) {
            representation.init();
        } else {
            representation.setBeans(new SortableLazyList<>(
                    // entity fetching strategy
                    (firstRow, asc, sortProperty) -> facade.findTasksByPredicate(getUserId(user),
                            getProjectId(project),
                            getTaskStatus(status),
                            firstRow / representation.getPageSize(), representation.getPageSize()),
                    // count fetching strategy
                    () -> facade.countTasksByPredicate(getUserId(user),
                            getProjectId(project),
                            getTaskStatus(status)).intValue(),
                    representation.getPageSize()
            ));
        }
    }

    private Long getUserId(User user) {
        Long userId = null;
        userId = (user != null ? user.getUserId() : null);
        if (userId == null && !security.hasAuthority("ROLE_ADMIN")) {
            //user id is mandatory for non-admins
            //must be added here since admins can query with null user ( and therefore appending the user id by default to the request is not an option )
            userId = (Long) session.getAttribute("user_id");
        }
        return userId;
    }

    private Long getProjectId(Project project) {
        return project != null ? project.getProjectId() : null;
    }

    private TaskStatus getTaskStatus(TaskStatus status) {
        return status != null ? status : null;
    }

    enum TaskFilteringField {

        USER("user", "Select user", "Select user name"),
        PROJECT("project", "Select project", "Select project name"),
        STATUS("status", "Select status", "Select task status");

        private String id;
        private String caption;
        private String description;

        private TaskFilteringField(String id, String caption, String description) {
            this.id = id;
            this.caption = caption;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getCaption() {
            return caption;
        }

        public String getDescription() {
            return description;
        }

    }

}
