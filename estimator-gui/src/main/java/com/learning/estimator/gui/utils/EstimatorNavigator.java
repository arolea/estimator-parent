package com.learning.estimator.gui.utils;

import com.learning.estimator.gui.views.utils.AccessDeniedView;
import com.learning.estimator.gui.views.utils.ErrorView;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SecurityExceptionUtils;
import org.vaadin.spring.sidebar.components.ValoSideBar;
import org.vaadin.spring.sidebar.security.VaadinSecurityItemFilter;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Navigator for estimator
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@UIScope
public class EstimatorNavigator extends CustomComponent {

    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private ValoSideBar sideBar;
    @Autowired
    private LogArea logArea;
    @Autowired
    private VaadinSecurity vaadinSecurity;
    private Navigator navigator;

    @PostConstruct
    public void init() {
        this.setSizeFull();
        this.setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                if (SecurityExceptionUtils.isAccessDeniedException(event.getThrowable())) {
                    Notification.show("Sorry, you don't have access to do that.");
                } else {
                    super.error(event);
                }
            }
        });
        sideBar.setItemFilter(new VaadinSecurityItemFilter(vaadinSecurity));

        VerticalSplitPanel contentContainer = new VerticalSplitPanel();
        contentContainer.setSizeFull();
        contentContainer.setSplitPosition(83, Unit.PERCENTAGE);
        VerticalLayout dataContainer = new MVerticalLayout().withMargin(false).withSpacing(false).withSize(MSize.FULL_SIZE);
        contentContainer.setFirstComponent(dataContainer);
        contentContainer.setSecondComponent(new MVerticalLayout(logArea).withMargin(false).withSpacing(false).withSize(MSize.FULL_SIZE).withExpand(logArea, 1));

        HorizontalLayout navigatorAndContentLayout = new MHorizontalLayout().withMargin(false).withSpacing(false).withSize(MSize.FULL_SIZE);
        this.setCompositionRoot(navigatorAndContentLayout);
        navigatorAndContentLayout.addComponents(sideBar, contentContainer);
        navigatorAndContentLayout.setExpandRatio(contentContainer, 1f);

        navigator = new Navigator(UI.getCurrent(), dataContainer);
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(ErrorView.class);
        navigator.navigateTo(navigator.getState());
    }

    public void navigateTo(String viewName) {
        navigator.navigateTo(viewName);
    }
}
