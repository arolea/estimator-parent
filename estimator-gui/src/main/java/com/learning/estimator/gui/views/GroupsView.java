package com.learning.estimator.gui.views;

import com.learning.estimator.gui.components.editorlisteners.GroupEditorListeners;
import com.learning.estimator.gui.components.editors.GroupEditor;
import com.learning.estimator.gui.components.representations.GroupRepresentation;
import com.learning.estimator.gui.core.AbstractView;
import com.learning.estimator.gui.utils.EstimatorViewLayout;
import com.learning.estimator.gui.utils.GuiUtils;
import com.learning.estimator.gui.utils.Sections;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import javax.annotation.PostConstruct;

/**
 * View for user groups interaction
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringView(name = GroupsView.VIEW_NAME)
@SideBarItem(sectionId = Sections.MANAGEMENT_VIEWS, caption = GroupsView.VIEW_NAME, order = 0)
@UIScope
@FontAwesomeIcon(FontAwesome.DATABASE)
@Secured(value = {"ROLE_ADMIN"})
public class GroupsView extends AbstractView {

    public static final String VIEW_NAME = "User Groups";

    @Autowired
    private GroupEditor projectEditor;
    @Autowired
    private GroupRepresentation projectRepresentation;
    @Autowired
    private GroupEditorListeners projectEditorListeners;

    @PostConstruct
    public void init() {
        super.init();
        GuiUtils.bindPresentations(projectRepresentation, projectEditor, projectEditorListeners);

        layout = new EstimatorViewLayout(projectRepresentation);

        setSizeFull();
        addComponent(layout);
        setExpandRatio(layout, 1);

        projectRepresentation.ensureCollapsed();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        try {
            projectRepresentation.setTableIndex(0);
        } catch (Exception e) {
            //the view was not yet instantiated
        }
    }

}