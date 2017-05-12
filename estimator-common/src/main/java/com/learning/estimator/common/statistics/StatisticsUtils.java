package com.learning.estimator.common.statistics;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Statistics utils
 *
 * @author rolea
 */
public final class StatisticsUtils {

    private static final ILogger LOG = LogManager.getLogger(StatisticsUtils.class);

    public static final Map<NumericStatistics, Double> getNumericStatistics(Collection<? extends Number> values) {
        Map<NumericStatistics, Double> result = new LinkedHashMap<>();

        try {
            Double mean = values.stream().collect(Collectors.summarizingDouble(Number::doubleValue)).getAverage();
            Double variance = values.stream().collect(Collectors.summarizingDouble(value -> Math.pow(mean.doubleValue() - value.doubleValue(), 2))).getAverage();
            Double standardDeviation = Math.sqrt(variance);

            Double median = values.stream().sorted(Comparator.comparing(Number::doubleValue)).collect(Collectors.toList()).get(values.size() / 2).doubleValue();

            Double m3 = values.stream().collect(Collectors.summarizingDouble(value -> Math.pow(mean.doubleValue() - value.doubleValue(), 3))).getAverage();

            Double skewness = m3 / Math.pow(variance, 3 / 2);
            Double peasrons = 3 * (mean - median) / Math.sqrt(variance);

            result.put(NumericStatistics.MEAN, mean);
            result.put(NumericStatistics.MEDIAN, median);
            result.put(NumericStatistics.VARIANCE, variance);
            result.put(NumericStatistics.STANDARD_DEVIATION, standardDeviation);
            result.put(NumericStatistics.SKEWNESS_COEFFICIENT, skewness);
            result.put(NumericStatistics.PEARSONS_COEFFICIENT, peasrons);
        } catch (Exception e) {
            LOG.error(e);
        }

        return result;
    }

}