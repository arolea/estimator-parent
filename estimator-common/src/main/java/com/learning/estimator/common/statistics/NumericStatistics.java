package com.learning.estimator.common.statistics;

/**
 * Defines available numeric statistics
 *
 * @author rolea
 */
public enum NumericStatistics {

    MEAN("Mean"),
    VARIANCE("Variance"),
    STANDARD_DEVIATION("Standard Deviation"),
    MEDIAN("Median"),
    SKEWNESS_COEFFICIENT("Skewness Coefficient"),
    PEARSONS_COEFFICIENT("Pearson's Coefficient");

    private String name;

    private NumericStatistics(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
