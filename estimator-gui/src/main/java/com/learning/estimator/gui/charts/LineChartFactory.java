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
import org.kopi.plotly.data.types.LineData;
import org.kopi.plotly.layout.LayoutWithAxis;

import java.util.Arrays;

/**
 * Factory for line charts
 *
 * @author rolea
 */
public class LineChartFactory {

    private static final ILogger LOG = LogManager.getLogger(LineChartFactory.class);

    /**
     * Builds a pie chart with the given title and data
     */
    public static final Component getLineChart(String chartName, String xAxisLabel, String yAxisLabel, PlottableMap... lines) {
        DataConfiguration dataLine = new DataConfiguration();

        Arrays.stream(lines).forEach(line -> {
            LineData lineData = new LineData(line.getLineName());
            CoupleOfData points = new CoupleOfData();
            line.getData().forEach((category, value) -> {
                try {
                    points.addData(category, value.doubleValue());
                } catch (Exception e) {
                    LOG.error(e);
                }
            });
            try {
                lineData.setDataSeries(points);
            } catch (Exception e) {
                e.printStackTrace();
            }
            lineData.setColor(Color.BLUE);
            dataLine.addDataConfiguration(lineData);
        });

        LayoutWithAxis layoutwithAxis = new LayoutWithAxis();
        layoutwithAxis.setLayoutTitle(chartName);
        layoutwithAxis.setBackgroundColor(Colors.WHITE);
        layoutwithAxis.setxAxisTitle(xAxisLabel);
        layoutwithAxis.setyAxisTitle(yAxisLabel);
        layoutwithAxis.disableXGrid();
        LayoutConfiguration boxLine = new LayoutConfiguration(layoutwithAxis);

        PlotlyChart lineChart = PlotlyFactory.renderChart(dataLine, boxLine);
        lineChart.setSizeFull();

        return lineChart;
    }

}
