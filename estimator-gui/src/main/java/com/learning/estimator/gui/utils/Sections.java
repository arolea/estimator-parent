package com.learning.estimator.gui.utils;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

/**
 * Right side bar sections
 *
 * @author rolea
 */
@Component
@SideBarSections({
        @SideBarSection(id = Sections.TASK_VIEWS, caption = "Task"),
        @SideBarSection(id = Sections.CHART_VIEWS, caption = "Charts"),
        @SideBarSection(id = Sections.MANAGEMENT_VIEWS, caption = "Management"),
        @SideBarSection(id = Sections.OPERATIONS, caption = "Operations")
})
public class Sections {
    public static final String TASK_VIEWS = "taskViews";
    public static final String CHART_VIEWS = "chartViews";
    public static final String MANAGEMENT_VIEWS = "managementViews";
    public static final String OPERATIONS = "operations";
}
