package com.learning.estimator.model.statistics;

import java.io.Serializable;
import java.time.Instant;

/**
 * Abstracts the result of a statistics computation
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public class StatisticsResult implements Serializable {

    private AvailableStatistics statistic;
    private Object data;
    private Instant dateCreated;

    @SuppressWarnings("unused")
    @Deprecated
    private StatisticsResult() {
    }

    public StatisticsResult(AvailableStatistics statistic, Object data) {
        this.statistic = statistic;
        this.data = data;
        this.dateCreated = Instant.now();
    }

    public AvailableStatistics getStatistic() {
        return statistic;
    }

    public void setStatistic(AvailableStatistics statistic) {
        this.statistic = statistic;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
        result = prime * result + ((statistic == null) ? 0 : statistic.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StatisticsResult other = (StatisticsResult) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (dateCreated == null) {
            if (other.dateCreated != null)
                return false;
        } else if (!dateCreated.equals(other.dateCreated))
            return false;
        if (statistic != other.statistic)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Result : " + data;
    }

}
