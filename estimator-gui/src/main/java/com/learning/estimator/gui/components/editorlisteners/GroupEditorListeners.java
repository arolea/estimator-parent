package com.learning.estimator.gui.components.editorlisteners;

import com.learning.estimator.gui.components.editors.GroupEditor;
import com.learning.estimator.gui.components.representations.GroupRepresentation;
import com.learning.estimator.gui.core.AbstractDataRepresentation;
import com.learning.estimator.gui.core.AbstractEditorForm;
import com.learning.estimator.gui.core.AbstractEditorListeners;
import com.learning.estimator.gui.utils.LogArea;
import com.learning.estimator.gui.utils.LogColor;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * User group editor listeners
 *
 * @author rolea
 */
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GroupEditorListeners extends AbstractEditorListeners<UserGroup> {

    @Autowired
    private PersistenceFacadeClientSide facade;
    @Autowired
    private LogArea logArea;
    @Autowired
    private GroupRepresentation representation;
    @Autowired
    private GroupEditor editor;

    @Override
    public UserGroup performSave(UserGroup entity) {
        logArea.logInfo("Saving user group", LogColor.GREEN);
        return facade.saveUserGroup(entity);
    }

    @Override
    public void performDelete(UserGroup entity) {
        logArea.logInfo("Deleting user group with id " + entity.getUserGroupId(), LogColor.GREEN);
        facade.deleteUserGroup(entity.getUserGroupId());
    }

    @Override
    public UserGroup createNewEntity() {
        return new UserGroup("");
    }

    @Override
    public AbstractDataRepresentation<UserGroup> getRepresentation() {
        return representation;
    }

    @Override
    public AbstractEditorForm<UserGroup> getEditor() {
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
