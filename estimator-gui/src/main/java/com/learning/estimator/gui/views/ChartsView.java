package com.learning.estimator.gui.views;

import com.learning.estimator.gui.components.filtering.StatisticFilterListener;
import com.learning.estimator.gui.components.filtering.StatisticsChartOptions;
import com.learning.estimator.gui.components.filtering.StatisticsFilter;
import com.learning.estimator.gui.core.AbstractView;
import com.learning.estimator.gui.utils.ChartPanel;
import com.learning.estimator.gui.utils.Sections;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Charts view
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringView(name = ChartsView.VIEW_NAME)
@SideBarItem(sectionId = Sections.CHART_VIEWS, caption = ChartsView.VIEW_NAME, order = 0)
@UIScope
@FontAwesomeIcon(FontAwesome.BAR_CHART)
public class ChartsView extends AbstractView {

    public static final String VIEW_NAME = "Charts";

    @Autowired
    private ChartPanel chartPanel;
    @Autowired
    private StatisticsFilter filter;
    @Autowired
    private StatisticsChartOptions options;
    @Autowired
    private StatisticFilterListener listeners;

    @PostConstruct
    public void init() {
        super.init();

        filter.addButtonListener("compute", listeners.getComputeListener());
        filter.addButtonListener("cancel", listeners.getCancelListener());
        filter.addComboListener("user", listeners.getUserSelectListener());

        MVerticalLayout layout = new MVerticalLayout(filter, options, chartPanel).withMargin(false).withSpacing(false).withSize(MSize.FULL_SIZE).withExpand(chartPanel, 1);

        setSizeFull();
        addComponent(layout);
        setExpandRatio(layout, 1);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        try {
            filter.initComboValues();
        } catch (Exception e) {
            //the view was not yet instantiated
        }
    }

}

