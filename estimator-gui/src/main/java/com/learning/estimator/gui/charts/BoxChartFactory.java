package com.learning.estimator.gui.charts;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Component;
import org.kopi.plotly.PlotlyChart;
import org.kopi.plotly.PlotlyFactory;
import org.kopi.plotly.configuration.DataConfiguration;
import org.kopi.plotly.configuration.LayoutConfiguration;
import org.kopi.plotly.data.features.Colors;
import org.kopi.plotly.data.types.BoxPlotData;
import org.kopi.plotly.layout.LayoutWithoutAxis;

import java.util.Arrays;

/**
 * Factory for box charts
 *
 * @author rolea
 */
public class BoxChartFactory {

    private static final ILogger LOG = LogManager.getLogger(BarChartFactory.class);

    public static final Component getBoxChart(String chartName, PlottableList... data) {
        DataConfiguration dataBox = new DataConfiguration();
        Arrays.stream(data).forEach(dataSet -> {
            BoxPlotData box = new BoxPlotData(dataSet.getName());
            dataSet.getValues().forEach(value -> {
                try {
                    box.getData().addData(value);
                } catch (Exception e) {
                    LOG.error(e);
                }
            });
            box.setColor(Color.BLUE);
            dataBox.addDataConfiguration(box);
        });

        LayoutWithoutAxis layoutwithoutAxis = new LayoutWithoutAxis();
        layoutwithoutAxis.setLayoutTitle(chartName);
        layoutwithoutAxis.setBackgroundColor(Colors.WHITE);
        LayoutConfiguration boxBox = new LayoutConfiguration(layoutwithoutAxis);

        PlotlyChart boxChart = PlotlyFactory.renderChart(dataBox, boxBox);
        boxChart.setSizeFull();

        return boxChart;
    }

}
