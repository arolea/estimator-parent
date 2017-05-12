package com.learning.estimator.gui.components.editors;

import com.learning.estimator.gui.core.AbstractEditorForm;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.UserGroup;
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

import javax.annotation.PostConstruct;

/**
 * Project editor
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProjectEditor extends AbstractEditorForm<Project> {

    private final TextField projectName = new MTextField("Project Name : ");
    private final RichTextArea projectDescription = new RichTextArea("Project Description : ");
    @Autowired
    private PersistenceFacadeClientSide facade;
    private ComboBox userGroup = new ComboBox("User Group :");

    public ProjectEditor() {
        super("Project editor : ");
        projectName.setDescription("The name for the project");
        projectDescription.setDescription("The description for the project");
        userGroup.setFilteringMode(FilteringMode.STARTSWITH);
        userGroup.setDescription("The associated user group for the project");
    }

    @PostConstruct
    public void init() {
        userGroup.removeAllItems();
        userGroup.addItems(facade.findAllUserGroups());
    }

    @Override
    public MBeanFieldGroup<Project> setEntity(Project e) {
        userGroup.removeAllItems();
        userGroup.addItems(facade.findAllUserGroups());
        return super.setEntity(e);
    }

    @Override
    protected MFormLayout getForm() {
        return new MFormLayout(projectName, userGroup, projectDescription);
    }

    @Override
    public void clearFields() {
        userGroup.setValue(null);
        projectName.setValue(null);
        projectDescription.setValue(null);
    }

    public Project getDataAsObject() {
        return new Project((UserGroup) userGroup.getValue(), projectName.getValue(), projectDescription.getValue());
    }

}
