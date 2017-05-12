package com.learning.estimator.gui.components.representations;

import com.learning.estimator.gui.core.AbstractDataRepresentation;
import com.learning.estimator.gui.utils.DateConverter;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.SortableLazyList;

import javax.annotation.PostConstruct;

/**
 * Task representation
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TaskRepresentation extends AbstractDataRepresentation<Task> {

    private static final int PAGE_SIZE = 20;
    private static final String[] TABLE_PROPERTIES = new String[]{"user.username", "project.projectName", "taskName", "bestCaseEstimate", "avgCaseEstimate", "worstCaseEstimate", "computedEstimation", "computedStandardDeviation", "actualTimeSpent", "estimateAccuracy", "points", "taskStatus", "createdDate", "finishedDate"};
    private static final String[] TABLE_HEADERS = new String[]{"User", "Project", "Task Name", "Best Case", "Avg Case", "Worst Case", "Estimation", "Standard Deviation", "Actual", "Accuracy", "Points", "Status", "Date Created", "Date Finished"};
    @Autowired
    private PersistenceFacadeClientSide facade;
    public TaskRepresentation() {
        super(TABLE_PROPERTIES);
        setColumnHeaders(TABLE_HEADERS);
        enableAddButton();
        buildContent();
        addConverter("createdDate", new DateConverter());
        addConverter("finishedDate", new DateConverter());
    }

    @PostConstruct
    public void init() {
        this.setBeans(new SortableLazyList<>(
                // entity fetching strategy
                (firstRow, asc, sortProperty) -> facade.findTasks(firstRow / PAGE_SIZE, PAGE_SIZE),
                // count fetching strategy
                () -> facade.countTasks().intValue(),
                PAGE_SIZE
        ));

    }

    @Override
    public int getPageSize() {
        return PAGE_SIZE;
    }

}
