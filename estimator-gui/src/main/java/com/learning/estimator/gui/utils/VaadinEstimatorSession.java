package com.learning.estimator.gui.utils;

import com.learning.estimator.common.session.IEstimatorSession;
import com.vaadin.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Implementation for estimator session - avoids coupling REST client to vaadin
 *
 * @author rolea
 */
@Component
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class VaadinEstimatorSession implements IEstimatorSession {

    @Autowired
    private VaadinSession vaadinSession;

    @Override
    public Object getDataFromSession(String key) {
        return vaadinSession.getAttribute(key);
    }

    @Override
    public void storeDataIntoSession(String key, Object value) {
        vaadinSession.setAttribute(key, value);
    }

}
