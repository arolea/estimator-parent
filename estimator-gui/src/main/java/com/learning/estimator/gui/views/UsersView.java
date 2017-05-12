package com.learning.estimator.gui.views;

import com.learning.estimator.gui.components.editorlisteners.UserEditorListeners;
import com.learning.estimator.gui.components.editors.UserEditor;
import com.learning.estimator.gui.components.representations.UserRepresentation;
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
 * View for user interaction
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringView(name = UsersView.VIEW_NAME)
@SideBarItem(sectionId = Sections.OPERATIONS, caption = UsersView.VIEW_NAME, order = 2)
@UIScope
@FontAwesomeIcon(FontAwesome.USER)
@Secured(value = {"ROLE_ADMIN"})
public class UsersView extends AbstractView {

    public static final String VIEW_NAME = "Users";

    @Autowired
    private UserEditor userEditor;
    @Autowired
    private UserRepresentation userRepresentation;
    @Autowired
    private UserEditorListeners userEditorListeners;

    @PostConstruct
    public void init() {
        super.init();
        GuiUtils.bindPresentations(userRepresentation, userEditor, userEditorListeners);

        layout = new EstimatorViewLayout(userRepresentation);

        setSizeFull();
        addComponent(layout);
        setExpandRatio(layout, 1);

        userRepresentation.ensureCollapsed();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        try {
            userRepresentation.setTableIndex(0);
        } catch (Exception e) {
            //the view was not yet instantiated
        }
    }

}
