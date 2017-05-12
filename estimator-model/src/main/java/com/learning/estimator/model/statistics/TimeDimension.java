package com.learning.estimator.model.statistics;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Models the time dimension
 *
 * @author rolea
 */
@Entity
@Table(name = "dimension_time", indexes = {
        @Index(name = "index_on_dimension_date", columnList = "date", unique = true)
})
@SuppressWarnings("serial")
public class TimeDimension implements Serializable {

    private Long timeId;
    private LocalDate date;

    @Deprecated
    public TimeDimension() {
    }

    public TimeDimension(Integer dayOfMonth, Integer month, Integer year) {
        this.date = LocalDate.of(year, month, dayOfMonth);
    }

    public TimeDimension(LocalDate date) {
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "time_id")
    public Long getTimeId() {
        return timeId;
    }

    public void setTimeId(Long timeId) {
        this.timeId = timeId;
    }

    @Column(name = "date")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((timeId == null) ? 0 : timeId.hashCode());
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
        TimeDimension other = (TimeDimension) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (timeId == null) {
            if (other.timeId != null)
                return false;
        } else if (!timeId.equals(other.timeId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TimeDimension [timeId=" + timeId + ", date=" + date + "]";
    }

}
