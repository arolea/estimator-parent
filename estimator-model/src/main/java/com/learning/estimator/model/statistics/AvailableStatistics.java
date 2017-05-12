package com.learning.estimator.model.statistics;

/**
 * Defines available statistics
 *
 * @author rolea
 */
public enum AvailableStatistics {

    //represented as line/spline charts
    //granularity in time
    LOGGED_TIME_EVOLUTION("Log evolution", true),
    VELOCITY_POINTS_EVOLUTION("Velocity evolution", true),
    ESTIMATE_ACCURACY_EVOLUTION("Accuracy evolution", true),

    //represented as bar charts/histograms
    //granularity in bins
    LOGGED_TIME_HISTOGRAM("Log histogram", true),
    VELOCITY_POINT_HISTOGRAM("Velocity histogram", true),
    ESTIMATE_ACCURACY_HISTOGRAM("Estimate histogram", true),

    //represented as box plots
    //no granularity
    LOGGED_TIME_BOX("Log box", true),
    VELOCITY_POINT_BOX("Velocity box", true),
    ESTIMATE_ACCURACY_BOX("Estimate box", true),

    //represented as pie/donut charts
    //no granularity
    TASK_STATUS_DISTRBUTION("Status distribution", false),

    //used as marker for statistics that are not done yet
    NOT_YET_COMPUTED("NONE", false),
    BAD_ID("NONE", false);

    private String name;
    private boolean enabled;

    private AvailableStatistics(String description, boolean enabled) {
        this.name = description;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return name;
    }

}
