package com.learning.estimator.gui.utils;

import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.model.infrastructure.LoginOutcome;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

import javax.annotation.PostConstruct;

/**
 * Login screen for estimator
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@PrototypeScope
public class LoginScreen extends CustomComponent {

    @Autowired
    private VaadinSecurity vaadinSecurity;
    @Autowired
    private EventBus.SessionEventBus eventBus;

    private TextField userName = new TextField("Username");
    private PasswordField passwordField = new PasswordField("Password");
    private Button login = new Button("Login");
    private Label loginFailedLabel = new Label();

    @PostConstruct
    public void init() {
        FormLayout loginForm = new FormLayout();
        loginForm.setSizeUndefined();

        loginForm.addComponents(userName, passwordField, login);

        login.addStyleName(ValoTheme.BUTTON_PRIMARY);
        login.setDisableOnClick(true);
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                login();
            }
        });

        VerticalLayout loginLayout = new VerticalLayout();
        loginLayout.setSizeUndefined();
        loginLayout.addComponent(loginFailedLabel);
        loginLayout.setComponentAlignment(loginFailedLabel, Alignment.BOTTOM_CENTER);
        loginFailedLabel.setSizeUndefined();
        loginFailedLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        loginFailedLabel.setVisible(false);

        loginLayout.addComponent(loginForm);
        loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

        VerticalLayout rootLayout = new VerticalLayout(loginLayout);
        rootLayout.setSizeFull();
        rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
        setCompositionRoot(rootLayout);
        setSizeFull();
    }

    private void login() {
        try {
            String password = passwordField.getValue();
            passwordField.setValue("");

            final Authentication authentication = vaadinSecurity.login(userName.getValue(), password);

            getSession().setAttribute("user", authentication.getPrincipal().toString());
            getSession().setAttribute("username", authentication.getPrincipal().toString());
            getSession().setAttribute("password", password);
            getSession().setAttribute("user_id", ((LoginOutcome) authentication.getDetails()).getUser().getUserId());

            eventBus.publish(this, new SuccessfulLoginEvent(getUI(), authentication));
        } catch (AuthenticationException ex) {
            userName.focus();
            userName.selectAll();
            loginFailedLabel.setValue(String.format("Login failed: %s", ex.getMessage()));
            loginFailedLabel.setVisible(true);
        } catch (Exception ex) {
            Notification.show("An unexpected error occurred", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            LogManager.getLogger(getClass()).error("Unexpected error while logging in", ex);
        } finally {
            login.setEnabled(true);
        }
    }

    public void setLoginLabel(String string) {
        loginFailedLabel.setVisible(true);
        loginFailedLabel.setValue(string);
    }

    public void setLoginLabelVisible(boolean visible) {
        loginFailedLabel.setVisible(visible);
    }


}
