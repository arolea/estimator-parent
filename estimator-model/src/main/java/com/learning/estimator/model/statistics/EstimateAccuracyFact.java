package com.learning.estimator.model.statistics;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Models the accuracy of an estimate
 *
 * @author rolea
 */
@Entity
@Table(name = "fact_estimate_accuracy", indexes = {
        @Index(name = "index_on_user_accuracy", columnList = "user_id", unique = false),
        @Index(name = "index_on_project_accuracy", columnList = "project_id", unique = false),
        @Index(name = "index_on_group_accuracy", columnList = "group_id", unique = false),
        @Index(name = "index_on_time_accuracy", columnList = "time_id", unique = false)
})
//eagerly fetch time
@NamedEntityGraph(name = "graph.EstimateAccuracyFact.fetchPolicy",
        attributeNodes = @NamedAttributeNode(value = "time", subgraph = "time")
)
@SuppressWarnings("serial")
public class EstimateAccuracyFact implements Serializable, IDataProvider, IDateProvider {

    private Long estimateAccuracyId;
    private Double estimateAccuracy;
    private UserDimension user;
    private ProjectDimension project;
    private GroupDimension group;
    private TimeDimension time;

    @Deprecated
    public EstimateAccuracyFact() {
    }

    public EstimateAccuracyFact(Double estimateAccuracy, UserDimension user, ProjectDimension project, GroupDimension group, TimeDimension time) {
        this.estimateAccuracy = estimateAccuracy;
        this.user = user;
        this.project = project;
        this.group = group;
        this.time = time;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "estimate_accuracy_id")
    public Long getEstimateAccuracyId() {
        return estimateAccuracyId;
    }

    public void setEstimateAccuracyId(Long estimateAccuracyId) {
        this.estimateAccuracyId = estimateAccuracyId;
    }

    @Column(name = "estimate_accuracy", columnDefinition = "Decimal(10,4)")
    public Double getEstimateAccuracy() {
        return estimateAccuracy;
    }

    public void setEstimateAccuracy(Double estimateAccuracy) {
        this.estimateAccuracy = estimateAccuracy;
    }

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public UserDimension getUser() {
        return user;
    }

    public void setUser(UserDimension user) {
        this.user = user;
    }

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    public ProjectDimension getProject() {
        return project;
    }

    public void setProject(ProjectDimension project) {
        this.project = project;
    }

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    public GroupDimension getGroup() {
        return group;
    }

    public void setGroup(GroupDimension group) {
        this.group = group;
    }

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "time_id")
    public TimeDimension getTime() {
        return time;
    }

    public void setTime(TimeDimension time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((estimateAccuracy == null) ? 0 : estimateAccuracy.hashCode());
        result = prime * result + ((estimateAccuracyId == null) ? 0 : estimateAccuracyId.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((project == null) ? 0 : project.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        EstimateAccuracyFact other = (EstimateAccuracyFact) obj;
        if (estimateAccuracy == null) {
            if (other.estimateAccuracy != null)
                return false;
        } else if (!estimateAccuracy.equals(other.estimateAccuracy))
            return false;
        if (estimateAccuracyId == null) {
            if (other.estimateAccuracyId != null)
                return false;
        } else if (!estimateAccuracyId.equals(other.estimateAccuracyId))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (project == null) {
            if (other.project != null)
                return false;
        } else if (!project.equals(other.project))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EstimateAccuracyFact [estimateAccuracyId=" + estimateAccuracyId + ", estimateAccuracy="
                + estimateAccuracy + ", user=" + user + ", project=" + project + ", group=" + group + ", time=" + time
                + "]";
    }

    @Override
    @Transient
    public LocalDate getDate() {
        return time.getDate();
    }

    @Override
    @Transient
    public Double getData() {
        return estimateAccuracy;
    }

}
