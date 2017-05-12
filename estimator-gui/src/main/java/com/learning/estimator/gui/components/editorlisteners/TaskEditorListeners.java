package com.learning.estimator.gui.components.editorlisteners;

import com.learning.estimator.gui.components.editors.TaskEditor;
import com.learning.estimator.gui.components.filtering.TaskFilter;
import com.learning.estimator.gui.components.representations.TaskRepresentation;
import com.learning.estimator.gui.core.AbstractDataRepresentation;
import com.learning.estimator.gui.core.AbstractEditorForm;
import com.learning.estimator.gui.core.AbstractEditorListeners;
import com.learning.estimator.gui.utils.LogArea;
import com.learning.estimator.gui.utils.LogColor;
import com.learning.estimator.model.entities.Task;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * Task editor listeners
 *
 * @author rolea
 */
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TaskEditorListeners extends AbstractEditorListeners<Task> {

    @Autowired
    private PersistenceFacadeClientSide facade;
    @Autowired
    private LogArea logArea;
    @Autowired
    private TaskRepresentation representation;
    @Autowired
    private TaskEditor editor;
    @Autowired
    private TaskFilter filter;

    @Override
    public Task performSave(Task entity) {
        logArea.logInfo("Saving task", LogColor.GREEN);
        return facade.saveTask(entity);
    }

    @Override
    public void performDelete(Task entity) {
        logArea.logInfo("Deleting task with id " + entity.getTaskId(), LogColor.GREEN);
        facade.deleteTask(entity.getTaskId());
    }

    @Override
    public Task createNewEntity() {
        return new Task(null, null, "", "", 0d, 0d, 0d, 0d);
    }

    @Override
    public AbstractDataRepresentation<Task> getRepresentation() {
        return representation;
    }

    @Override
    public AbstractEditorForm<Task> getEditor() {
        return editor;
    }

    @Override
    protected void listEntities() {
        filter.filterTasks();
        getRepresentation().setSelected(null);
        getEditor().clearFields();
    }

    @Override
    public boolean isAddEnabled() {
        return true;
    }

    @Override
    public boolean isSelectEnabled() {
        return true;
    }

    @Override
    public boolean isSaveEnabled() {
        return true;
    }

    @Override
    public boolean isCancelEnabled() {
        return true;
    }

    @Override
    public boolean isDeleteEnabled() {
        return true;
    }

}
