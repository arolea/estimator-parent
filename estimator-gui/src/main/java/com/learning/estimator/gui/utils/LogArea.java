package com.learning.estimator.gui.utils;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MPanel;

import javax.annotation.PostConstruct;

/**
 * GUI log area component
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LogArea extends MPanel {

    private Label logArea;

    @PostConstruct
    public void init() {
        this.setImmediate(true);
        this.withCaption("System output : ").withSize(MSize.FULL_SIZE);
        logArea = new MLabel().withCaption("System output : ").withContentMode(ContentMode.HTML);
        logArea.setSizeUndefined();
        logArea.setImmediate(true);
        this.setContent(logArea);
    }

    /**
     * Appends the message to the bottom of the text area
     */
    public void logInfo(String message, LogColor color) {
        String currentLogContent = logArea.getValue();
        if (currentLogContent.length() > 20000) {
            currentLogContent = currentLogContent.substring(10000, currentLogContent.length());
        }
        logArea.setValue(currentLogContent + "<br>" + color.getWrappedMessage(message));
        int scrollPos = this.getScrollTop();
        if (scrollPos > 1000)
            scrollPos = 1000;
        this.setScrollTop(scrollPos + 5000);
    }

}
