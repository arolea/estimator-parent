package com.learning.estimator.gui.utils;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.layouts.MPanel;

import javax.annotation.PostConstruct;

/**
 * Holder for charts
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChartPanel extends MPanel {

    private Component chart;

    @PostConstruct
    public void init() {
        this.setSizeFull();
    }

    public Component getChart() {
        return chart;
    }

    public void setChart(Component chart) {
        this.setContent(chart);
    }

}
