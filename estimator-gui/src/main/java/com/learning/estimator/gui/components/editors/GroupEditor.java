package com.learning.estimator.gui.components.editors;

import com.learning.estimator.gui.core.AbstractEditorForm;
import com.learning.estimator.model.entities.UserGroup;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.TextField;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;

/**
 * User group editor
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GroupEditor extends AbstractEditorForm<UserGroup> {

    private final TextField userGroupName = new MTextField("User Group Name : ");

    public GroupEditor() {
        super("User Group editor : ");
        userGroupName.setDescription("The name of the user group");
    }

    @Override
    protected MFormLayout getForm() {
        return new MFormLayout(userGroupName);
    }

    @Override
    public void clearFields() {
        userGroupName.setValue(null);
    }

    public UserGroup getDataAsObject() {
        return new UserGroup(userGroupName.getValue());
    }

}
