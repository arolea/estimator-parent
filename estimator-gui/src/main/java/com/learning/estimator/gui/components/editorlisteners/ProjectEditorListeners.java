package com.learning.estimator.gui.components.editorlisteners;

import com.learning.estimator.gui.components.editors.ProjectEditor;
import com.learning.estimator.gui.components.representations.ProjectRepresentation;
import com.learning.estimator.gui.core.AbstractDataRepresentation;
import com.learning.estimator.gui.core.AbstractEditorForm;
import com.learning.estimator.gui.core.AbstractEditorListeners;
import com.learning.estimator.gui.utils.LogArea;
import com.learning.estimator.gui.utils.LogColor;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * Project editor listeners
 *
 * @author rolea
 */
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProjectEditorListeners extends AbstractEditorListeners<Project> {

    @Autowired
    private PersistenceFacadeClientSide facade;
    @Autowired
    private LogArea logArea;
    @Autowired
    private ProjectRepresentation representation;
    @Autowired
    private ProjectEditor editor;

    @Override
    public Project performSave(Project entity) {
        logArea.logInfo("Saving project", LogColor.GREEN);
        return facade.saveProject(entity);
    }

    @Override
    public void performDelete(Project entity) {
        logArea.logInfo("Deleting project with id " + entity.getProjectId(), LogColor.GREEN);
        facade.deleteProject(entity.getProjectId());
    }

    @Override
    public Project createNewEntity() {
        return new Project(null, "", "");
    }

    @Override
    public AbstractDataRepresentation<Project> getRepresentation() {
        return representation;
    }

    @Override
    public AbstractEditorForm<Project> getEditor() {
        return editor;
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
