package com.learning.estimator.gui.utils;

import com.learning.estimator.gui.core.AbstractDataRepresentation;
import com.learning.estimator.gui.core.AbstractFilteringComponent;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Layout for estimator views
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public class EstimatorViewLayout extends MPanel {

    public EstimatorViewLayout(AbstractDataRepresentation<?> dataViewer) {
        super(dataViewer);
        this.withSize(MSize.FULL_SIZE);
    }

    public EstimatorViewLayout(AbstractDataRepresentation<?> dataViewer, AbstractFilteringComponent filter) {
        super(new MVerticalLayout(filter, dataViewer).withMargin(false).withSpacing(false).withSize(MSize.FULL_SIZE).withExpand(dataViewer, 1));
        this.withSize(MSize.FULL_SIZE);
    }

    public EstimatorViewLayout(MPanel chartPanel, AbstractFilteringComponent filter) {
        super(new MVerticalLayout(filter, chartPanel).withMargin(false).withSpacing(false).withSize(MSize.FULL_SIZE).withExpand(chartPanel, 1));
    }

}
