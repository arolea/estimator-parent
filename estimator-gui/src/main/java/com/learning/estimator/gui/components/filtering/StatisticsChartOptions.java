package com.learning.estimator.gui.components.filtering;

import com.learning.estimator.common.statistics.EvolutionRepresentation;
import com.learning.estimator.common.statistics.FrequencyRepresentation;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.OptionGroup;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Options for chart generation
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StatisticsChartOptions extends MHorizontalLayout {

    private OptionGroup frequencyOptions;
    private OptionGroup lineOptions;

    @PostConstruct
    public void init() {
        this.withMargin(false).withSpacing(true);

        frequencyOptions = new OptionGroup("Frequency charts representations : ");
        frequencyOptions.addItems(Arrays.stream(FrequencyRepresentation.values()).collect(Collectors.toList()));
        frequencyOptions.setMultiSelect(false);
        frequencyOptions.setValue(FrequencyRepresentation.BAR);
        frequencyOptions.setNullSelectionAllowed(false);
        frequencyOptions.addStyleName("horizontal");

        lineOptions = new OptionGroup("Line charts representations : ");
        lineOptions.addItems(Arrays.stream(EvolutionRepresentation.values()).collect(Collectors.toList()));
        lineOptions.setMultiSelect(false);
        lineOptions.setValue(EvolutionRepresentation.SPLINE);
        lineOptions.setNullSelectionAllowed(false);
        lineOptions.addStyleName("horizontal");

        this.addComponent(frequencyOptions);
        this.addComponent(lineOptions);

        this.setHeightUndefined();
    }

    public FrequencyRepresentation getFrequencyRepresentation() {
        return (FrequencyRepresentation) frequencyOptions.getValue();
    }

    public EvolutionRepresentation getEvolutionRepresentation() {
        return (EvolutionRepresentation) lineOptions.getValue();
    }

}
