package com.learning.estimator.gui.operations;

import com.learning.estimator.gui.utils.Sections;
import com.learning.estimator.persistenceclient.service.impl.LoginService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.security.managed.VaadinManagedSecurity;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 * Logout operation
 *
 * @author rolea
 */
@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "Logout", order = 3)
@FontAwesomeIcon(FontAwesome.POWER_OFF)
@PrototypeScope
public class LogoutOperation implements Runnable {

    @Autowired
    private VaadinManagedSecurity vaadinSecurity;
    @Autowired
    private VaadinSession session;
    @Autowired
    private LoginService loginService;

    @Override
    public void run() {
        loginService.logout((Long) session.getAttribute("user_id"));
        vaadinSecurity.logout();
    }
}