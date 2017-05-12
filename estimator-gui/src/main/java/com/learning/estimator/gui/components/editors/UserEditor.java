package com.learning.estimator.gui.components.editors;

import com.learning.estimator.gui.core.AbstractEditorForm;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Set;

/**
 * User editor
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserEditor extends AbstractEditorForm<User> {

    private final TextField username = new MTextField("Username : ");
    private final TextField password = new MTextField("Password : ");
    private final OptionGroup roles = new OptionGroup("Roles : ");
    private final OptionGroup groups = new OptionGroup("Groups : ");
    @Autowired
    private PersistenceFacadeClientSide facade;

    public UserEditor() {
        super("User editor : ");
        username.setDescription("Username");
        password.setDescription("Password");
        roles.setDescription("Roles for the current user");
        groups.setDescription("Groups for the current user");
    }

    @Override
    public MBeanFieldGroup<User> setEntity(User e) {
        groups.setMultiSelect(true);
        groups.removeAllItems();
        groups.addItems(facade.findAllUserGroups());
        return super.setEntity(e);
    }

    @PostConstruct
    public void init() {
        roles.setMultiSelect(true);
        Arrays.stream(UserRole.values()).forEach(mail -> roles.addItem(mail));
        groups.setMultiSelect(true);
        groups.clear();
        groups.addItems(facade.findAllUserGroups());
    }

    @Override
    protected MFormLayout getForm() {
        return new MFormLayout(username, password, roles, groups);
    }

    @Override
    public void clearFields() {
        username.setValue(null);
        password.setValue(null);
        roles.setValue(null);
    }

    @SuppressWarnings("unchecked")
    public User getDataAsObject() {
        User user = new User(username.getValue(), password.getValue());
        user.setRoles((Set<UserRole>) roles.getValue());
        return user;
    }
}
