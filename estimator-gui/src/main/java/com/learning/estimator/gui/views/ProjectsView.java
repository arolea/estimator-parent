package com.learning.estimator.gui.views;

import com.learning.estimator.gui.components.editorlisteners.ProjectEditorListeners;
import com.learning.estimator.gui.components.editors.ProjectEditor;
import com.learning.estimator.gui.components.representations.ProjectRepresentation;
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
 * View for project interaction
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringView(name = ProjectsView.VIEW_NAME)
@SideBarItem(sectionId = Sections.MANAGEMENT_VIEWS, caption = ProjectsView.VIEW_NAME, order = 1)
@UIScope
@FontAwesomeIcon(FontAwesome.DATABASE)
@Secured(value = {"ROLE_ADMIN"})
public class ProjectsView extends AbstractView {

    public static final String VIEW_NAME = "Projects";

    @Autowired
    private ProjectEditor projectEditor;
    @Autowired
    private ProjectRepresentation projectRepresentation;
    @Autowired
    private ProjectEditorListeners projectEditorListeners;

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
