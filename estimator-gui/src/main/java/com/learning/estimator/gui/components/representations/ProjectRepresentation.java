package com.learning.estimator.gui.components.representations;

import com.learning.estimator.gui.core.AbstractDataRepresentation;
import com.learning.estimator.gui.utils.DateConverter;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.SortableLazyList;

import javax.annotation.PostConstruct;

/**
 * Project representation
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProjectRepresentation extends AbstractDataRepresentation<Project> {

    private static final int PAGE_SIZE = 20;
    private static final String[] TABLE_PROPERTIES = new String[]{"projectId", "userGroup.userGroupName", "projectName", "createdDate", "version"};
    private static final String[] TABLE_HEADERS = new String[]{"Project ID", "User Group Name", "Project Name", "Date Created", "Version"};
    @Autowired
    private PersistenceFacadeClientSide facade;
    public ProjectRepresentation() {
        super(TABLE_PROPERTIES);
        setColumnHeaders(TABLE_HEADERS);
        enableAddButton();
        buildContent();
        addConverter("createdDate", new DateConverter());
    }

    @PostConstruct
    public void init() {
        this.setBeans(new SortableLazyList<>(
                // entity fetching strategy
                (firstRow, asc, sortProperty) -> facade.findProjects(firstRow / PAGE_SIZE, PAGE_SIZE),
                // count fetching strategy
                () -> facade.countProjects().intValue(),
                PAGE_SIZE
        ));
    }

    @Override
    public int getPageSize() {
        return PAGE_SIZE;
    }

}
