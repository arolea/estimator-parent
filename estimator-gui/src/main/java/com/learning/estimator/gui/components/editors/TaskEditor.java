package com.learning.estimator.gui.components.editors;

import com.learning.estimator.gui.core.AbstractEditorForm;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.model.entities.TaskStatus;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;

import java.time.ZonedDateTime;
import java.util.Arrays;

/**
 * Task editor
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TaskEditor extends AbstractEditorForm<Task> {

    private final TextField taskName = new MTextField("Task Name : ");
    private final RichTextArea taskDescription = new RichTextArea("Task Description : ");
    private final TextField bestCaseEstimate = new MTextField("Best case : ");
    private final TextField avgCaseEstimate = new MTextField("Avg case : ");
    private final TextField worstCaseEstimate = new MTextField("Worst case : ");
    private final TextField actualTimeSpent = new MTextField("Actual : ");
    private final TextField points = new MTextField("Points : ");
    @Autowired
    private PersistenceFacadeClientSide facade;
    private ComboBox project = new ComboBox("Project :");
    private ComboBox user = new ComboBox("User :");
    private ComboBox taskStatus = new ComboBox("Status :");

    public TaskEditor() {
        super("Task editor : ");

        taskName.setDescription("Task name");
        taskDescription.setDescription("Description for the current task");

        bestCaseEstimate.setDescription("Estimate ( in hours ) for the best case secnario");
        avgCaseEstimate.setDescription("Estimate ( in hours ) for the avg case secnario");
        worstCaseEstimate.setDescription("Estimate ( in hours ) for the worst case secnario");
        actualTimeSpent.setDescription("Actual time spent on the task ( should edit this upon completing a task )");

        points.setDescription("The number of velocity units associated with the task");

        project.setFilteringMode(FilteringMode.STARTSWITH);
        project.setDescription("The associated project for the task");

        user.setFilteringMode(FilteringMode.STARTSWITH);
        user.setDescription("The associated user for the task");

        taskStatus.setFilteringMode(FilteringMode.STARTSWITH);
        taskStatus.setDescription("The task status");
        taskStatus.addValueChangeListener((event) -> {
            if (TaskStatus.DONE.equals(taskStatus.getValue()))
                getEntity().setFinishedDate(ZonedDateTime.now());
        });
        taskStatus.addItems(Arrays.asList(TaskStatus.values()));
    }

    @Override
    public MBeanFieldGroup<Task> setEntity(Task e) {
        project.removeAllItems();
        project.addItems(facade.findAllProjects());
        user.removeAllItems();
        user.addItems(facade.findAllUsers());
        user.setValue(e.getUser());
        return super.setEntity(e);
    }

    @Override
    protected MFormLayout getForm() {
        return new MFormLayout(taskName, user, project, taskDescription, bestCaseEstimate, avgCaseEstimate, worstCaseEstimate, actualTimeSpent, taskStatus, points);
    }

    @Override
    public void clearFields() {
        project.setValue(null);
        taskName.setValue(null);
        taskDescription.setValue(null);
        bestCaseEstimate.setValue(null);
        avgCaseEstimate.setValue(null);
        worstCaseEstimate.setValue(null);
        taskStatus.setValue(null);
        points.setValue(null);
        actualTimeSpent.setValue(null);
    }

    public Task getDataAsObject() {
        Task task = new Task((User) user.getValue(), (Project) project.getValue(), taskName.getValue(), taskDescription.getValue(), Double.parseDouble(bestCaseEstimate.getValue()), Double.parseDouble(avgCaseEstimate.getValue()), Double.parseDouble(worstCaseEstimate.getValue()), Double.parseDouble(points.getValue()));
        task.setTaskStatus(TaskStatus.PENDING);
        task.setActualTimeSpent(Double.parseDouble(actualTimeSpent.getValue()));
        return task;
    }

}
