package com.learning.estimator.gui.views;

import com.learning.estimator.gui.components.editorlisteners.TaskEditorListeners;
import com.learning.estimator.gui.components.editors.TaskEditor;
import com.learning.estimator.gui.components.filtering.TaskFilter;
import com.learning.estimator.gui.components.representations.TaskRepresentation;
import com.learning.estimator.gui.core.AbstractView;
import com.learning.estimator.gui.utils.EstimatorViewLayout;
import com.learning.estimator.gui.utils.GuiUtils;
import com.learning.estimator.gui.utils.Sections;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import javax.annotation.PostConstruct;

/**
 * View for task interaction
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringView(name = TasksView.VIEW_NAME)
@SideBarItem(sectionId = Sections.TASK_VIEWS, caption = TasksView.VIEW_NAME, order = 0)
@UIScope
@FontAwesomeIcon(FontAwesome.DATABASE)
public class TasksView extends AbstractView {

    public static final String VIEW_NAME = "Tasks";

    @Autowired
    private TaskEditor taskEditor;
    @Autowired
    private TaskRepresentation taskRepresentation;
    @Autowired
    private TaskEditorListeners taskEditorListeners;

    @Autowired
    private TaskFilter taskFiler;

    @PostConstruct
    public void init() {
        super.init();
        GuiUtils.bindPresentations(taskRepresentation, taskEditor, taskEditorListeners);

        layout = new EstimatorViewLayout(taskRepresentation, taskFiler);

        setSizeFull();
        addComponent(layout);
        setExpandRatio(layout, 1);

        taskRepresentation.ensureCollapsed();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        try {
            taskRepresentation.setTableIndex(0);
            taskFiler.initComboValues();
        } catch (Exception e) {
            //the view was not yet instantiated
        }
    }

}
