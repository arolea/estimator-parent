package com.learning.estimator.gui.components.editorlisteners;

import com.learning.estimator.gui.components.editors.UserEditor;
import com.learning.estimator.gui.components.representations.UserRepresentation;
import com.learning.estimator.gui.core.AbstractDataRepresentation;
import com.learning.estimator.gui.core.AbstractEditorForm;
import com.learning.estimator.gui.core.AbstractEditorListeners;
import com.learning.estimator.gui.utils.LogArea;
import com.learning.estimator.gui.utils.LogColor;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * User editor listeners
 *
 * @author rolea
 */
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserEditorListeners extends AbstractEditorListeners<User> {

    @Autowired
    private PersistenceFacadeClientSide infrastructureFacade;
    @Autowired
    private LogArea logArea;
    @Autowired
    private UserEditor userEditor;
    @Autowired
    private UserRepresentation userRepresentation;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User performSave(User entity) {
        if (shouldEncodePassword(entity))
            entity.setPassword(encoder.encode(entity.getPassword()));
        logArea.logInfo("Saving user " + entity.getUsername(), LogColor.GREEN);
        return infrastructureFacade.saveUser(entity);
    }

    @Override
    public void performDelete(User entity) {
        logArea.logInfo("Deleting user " + entity.getUsername(), LogColor.GREEN);
        infrastructureFacade.deleteUser(userRepresentation.getTableValue().getUserId());
    }

    @Override
    public User createNewEntity() {
        return new User("", "");
    }

    @Override
    public AbstractDataRepresentation<User> getRepresentation() {
        return userRepresentation;
    }

    @Override
    public AbstractEditorForm<User> getEditor() {
        return userEditor;
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

    private boolean shouldEncodePassword(User entity) {
        //new entity , should encode the password
        if (entity.getUserId() == null) {
            return true;
            //old entity , the password field was not modified , should not re encode
        } else if (passwordNotModified(entity)) {
            return false;
            //old entity , the password field was modified , should re encode
        } else {
            return true;
        }
    }

    private boolean passwordNotModified(User entity) {
        User savedUser = infrastructureFacade.findUser(entity.getUserId());
        if (entity.getPassword().equals(savedUser.getPassword()) || encoder.encode(entity.getPassword()).equals(savedUser.getPassword()))
            return true;
        return false;
    }

}
