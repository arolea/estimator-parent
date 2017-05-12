package com.learning.estimator.model.statistics;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Abstracts a statistics query
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public class StatisticsQuery implements Serializable {

    private Long userId;
    private Long projectId;
    private Long groupId;
    private LocalDate begindDate;
    private LocalDate endDate;

    private int binSize = 1;
    private AvailableStatistics statistic;

    private String responseUrl;
    private Integer uid;

    public StatisticsQuery() {
    }

    public StatisticsQuery withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public StatisticsQuery withProjectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public StatisticsQuery withGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    public StatisticsQuery withStartingDate(LocalDate startingDate) {
        this.begindDate = startingDate;
        return this;
    }

    public StatisticsQuery withEndingDate(LocalDate endingDate) {
        this.endDate = endingDate;
        return this;
    }

    public StatisticsQuery withStatisticType(AvailableStatistics statistic) {
        this.statistic = statistic;
        return this;
    }

    public StatisticsQuery withBinSize(int binSize) {
        this.binSize = binSize;
        return this;
    }

    public StatisticsQuery withResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
        return this;
    }

    public StatisticsQuery withUid(Integer uid) {
        this.uid = uid;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public LocalDate getBegindDate() {
        return begindDate;
    }

    public void setBegindDate(LocalDate begindDate) {
        this.begindDate = begindDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public AvailableStatistics getStatistic() {
        return statistic;
    }

    public void setStatistic(AvailableStatistics statistic) {
        this.statistic = statistic;
    }

    public int getBinSize() {
        return binSize;
    }

    public void setBinSize(int binSize) {
        this.binSize = binSize;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((begindDate == null) ? 0 : begindDate.hashCode());
        result = prime * result + binSize;
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
        result = prime * result + ((responseUrl == null) ? 0 : responseUrl.hashCode());
        result = prime * result + ((statistic == null) ? 0 : statistic.hashCode());
        result = prime * result + ((uid == null) ? 0 : uid.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        StatisticsQuery other = (StatisticsQuery) obj;
        if (begindDate == null) {
            if (other.begindDate != null)
                return false;
        } else if (!begindDate.equals(other.begindDate))
            return false;
        if (binSize != other.binSize)
            return false;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (groupId == null) {
            if (other.groupId != null)
                return false;
        } else if (!groupId.equals(other.groupId))
            return false;
        if (projectId == null) {
            if (other.projectId != null)
                return false;
        } else if (!projectId.equals(other.projectId))
            return false;
        if (responseUrl == null) {
            if (other.responseUrl != null)
                return false;
        } else if (!responseUrl.equals(other.responseUrl))
            return false;
        if (statistic != other.statistic)
            return false;
        if (uid == null) {
            if (other.uid != null)
                return false;
        } else if (!uid.equals(other.uid))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Query : user=" + userId + ", project=" + projectId + ", group=" + groupId + " ( " + begindDate + "," + endDate + " ) bin=" + binSize + ", stat=" + statistic + ", responseUrl=" + responseUrl + ", uid=" + uid + "]";
    }

}
