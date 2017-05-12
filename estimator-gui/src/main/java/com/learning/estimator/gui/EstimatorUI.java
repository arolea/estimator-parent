package com.learning.estimator.gui;

import com.learning.estimator.gui.utils.EstimatorNavigator;
import com.learning.estimator.gui.utils.LoginScreen;
import com.learning.estimator.gui.views.TasksView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

/**
 * UI for estimator
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringUI
@Title("Estimator")
@Theme(ValoTheme.THEME_NAME)
@Push(transport = Transport.WEBSOCKET_XHR)
@PreserveOnRefresh
public class EstimatorUI extends UI {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private VaadinSecurity vaadinSecurity;
    @Autowired
    private EventBus.SessionEventBus eventBus;

    @Override
    protected void init(VaadinRequest request) {
        Responsive.makeResponsive(this);
        if (vaadinSecurity.isAuthenticated()) {
            showMainScreen();
        } else {
            showLoginScreen();
        }
    }

    @Override
    public void attach() {
        super.attach();
        eventBus.subscribe(this);
    }

    @Override
    public void detach() {
        eventBus.unsubscribe(this);
        try {
            super.detach();
        } catch (Exception e) {
            //session is closed
        }
    }

    public void showLoginScreen() {
        LoginScreen loginScreen = applicationContext.getBean(LoginScreen.class);
        setContent(loginScreen);
    }

    public void showMainScreen() {
        EstimatorNavigator navigator = applicationContext.getBean(EstimatorNavigator.class);
        navigator.navigateTo(TasksView.VIEW_NAME);
        setContent(navigator);
    }

    @EventBusListenerMethod
    void onLogin(SuccessfulLoginEvent loginEvent) {
        if (loginEvent.getSource().equals(this)) {
            access(() -> showMainScreen());
        } else {
            getPage().reload();
        }
    }

}
