package com.learning.estimator.gui.core;

import com.learning.estimator.gui.utils.EstimatorViewLayout;
import com.vaadin.navigator.View;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.spring.security.util.SecurityExceptionUtils;

/**
 * Common error handling for views
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public abstract class AbstractView extends VerticalLayout implements View {

    protected EstimatorViewLayout layout;

    public void init() {
        this.setMargin(false);
        this.setSpacing(false);
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
    }

}
