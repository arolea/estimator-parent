package com.learning.estimator.gui.charts;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Component;
import org.kopi.plotly.PlotlyChart;
import org.kopi.plotly.PlotlyFactory;
import org.kopi.plotly.configuration.DataConfiguration;
import org.kopi.plotly.configuration.LayoutConfiguration;
import org.kopi.plotly.data.dataseries.CoupleOfData;
import org.kopi.plotly.data.features.Colors;
import org.kopi.plotly.data.types.BarData;
import org.kopi.plotly.data.types.ColumnData;
import org.kopi.plotly.layout.LayoutWithAxis;

import java.util.Arrays;

/**
 * Factory for bar charts
 *
 * @author rolea
 */
public class BarChartFactory {

    private static final ILogger LOG = LogManager.getLogger(BarChartFactory.class);

    public static final Component getBarChart(String chartName, String xAxisLabel, String yAxisLabel, PlottableMap... lines) {
        DataConfiguration barLines = new DataConfiguration();

        Arrays.stream(lines).forEach(line -> {
            //instantiate column data for histograms
            BarData barData = new ColumnData(line.getLineName(), new CoupleOfData());
            line.getData().forEach((category, value) -> {
                try {
                    barData.getData().addData(category, value.doubleValue());
                } catch (Exception e) {
                    LOG.error(e);
                }
            });
            barData.setOrientation(null);
            barData.setColor(Color.BLUE);
            barLines.addDataConfiguration(barData);
        });

        LayoutWithAxis layoutwithAxis = new LayoutWithAxis();
        layoutwithAxis.setLayoutTitle(chartName);
        layoutwithAxis.setxAxisTitle(xAxisLabel);
        layoutwithAxis.setyAxisTitle(yAxisLabel);
        layoutwithAxis.setBackgroundColor(Colors.WHITE);
        LayoutConfiguration barBox = new LayoutConfiguration(layoutwithAxis);

        PlotlyChart barChart = PlotlyFactory.renderChart(barLines, barBox);
        barChart.setSizeFull();

        return barChart;
    }

}
