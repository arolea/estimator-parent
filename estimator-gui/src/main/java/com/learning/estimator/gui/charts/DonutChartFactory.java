package com.learning.estimator.gui.charts;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.vaadin.ui.Component;
import org.kopi.plotly.PlotlyChart;
import org.kopi.plotly.PlotlyFactory;
import org.kopi.plotly.configuration.DataConfiguration;
import org.kopi.plotly.configuration.LayoutConfiguration;
import org.kopi.plotly.data.dataseries.CoupleOfData;
import org.kopi.plotly.data.features.Colors;
import org.kopi.plotly.data.types.DonutData;
import org.kopi.plotly.data.types.PieData;
import org.kopi.plotly.layout.LayoutWithoutAxis;

import java.util.Map;

/**
 * Factory for pie charts
 *
 * @author rolea
 */
public final class DonutChartFactory {

    private static final ILogger LOG = LogManager.getLogger(DonutChartFactory.class);

    /**
     * Builds a pie chart with the given title and data
     */
    public static final Component getDonutChart(String chartName, Map<Object, Number> data) {
        PieData pie = new DonutData(chartName, new CoupleOfData());
        data.forEach((category, value) -> {
            try {
                pie.getData().addData(category, value.intValue());
            } catch (Exception e) {
                LOG.error(e);
            }
        });

        DataConfiguration dataPie = new DataConfiguration();
        dataPie.addDataConfiguration(pie);

        LayoutWithoutAxis layoutwithoutAxis = new LayoutWithoutAxis();
        layoutwithoutAxis.setLayoutTitle(chartName);
        layoutwithoutAxis.setBackgroundColor(Colors.WHITE);
        LayoutConfiguration boxPie = new LayoutConfiguration(layoutwithoutAxis);

        PlotlyChart pieChart = PlotlyFactory.renderChart(dataPie, boxPie);
        pieChart.setSizeFull();

        return pieChart;
    }

}

