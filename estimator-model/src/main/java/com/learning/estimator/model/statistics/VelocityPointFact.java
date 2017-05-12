package com.learning.estimator.model.statistics;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Models the velocity point fact
 *
 * @author rolea
 */
@Entity
@Table(name = "fact_velocity", indexes = {
        @Index(name = "index_on_user_velocity", columnList = "user_id", unique = false),
        @Index(name = "index_on_project_velocity", columnList = "project_id", unique = false),
        @Index(name = "index_on_group_velocity", columnList = "group_id", unique = false),
        @Index(name = "index_on_time_velocity", columnList = "time_id", unique = false)
})
//eagerly fetch time
@NamedEntityGraph(name = "graph.VelocityPointFact.fetchPolicy",
        attributeNodes = @NamedAttributeNode(value = "time", subgraph = "time")
)
@SuppressWarnings("serial")
public class VelocityPointFact implements Serializable, IDataProvider, IDateProvider {

    private Long velocityPointsId;
    private Double velocityPoints;
    private UserDimension user;
    private ProjectDimension project;
    private GroupDimension group;
    private TimeDimension time;

    @Deprecated
    public VelocityPointFact() {
    }

    public VelocityPointFact(Double velocityPoints, UserDimension user, ProjectDimension project, GroupDimension group, TimeDimension time) {
        this.velocityPoints = velocityPoints;
        this.user = user;
        this.project = project;
        this.group = group;
        this.time = time;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "velocity_points_id")
    public Long getVelocityPointsId() {
        return velocityPointsId;
    }

    public void setVelocityPointsId(Long velocityPointsId) {
        this.velocityPointsId = velocityPointsId;
    }

    @Column(name = "velocity_points", columnDefinition = "Decimal(10,4)")
    public Double getVelocityPoints() {
        return velocityPoints;
    }

    public void setVelocityPoints(Double velocityPoints) {
        this.velocityPoints = velocityPoints;
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
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((project == null) ? 0 : project.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((velocityPoints == null) ? 0 : velocityPoints.hashCode());
        result = prime * result + ((velocityPointsId == null) ? 0 : velocityPointsId.hashCode());
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
        VelocityPointFact other = (VelocityPointFact) obj;
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
        if (velocityPoints == null) {
            if (other.velocityPoints != null)
                return false;
        } else if (!velocityPoints.equals(other.velocityPoints))
            return false;
        if (velocityPointsId == null) {
            if (other.velocityPointsId != null)
                return false;
        } else if (!velocityPointsId.equals(other.velocityPointsId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "VelocityPointsFact [velocityPointsId=" + velocityPointsId + ", velocityPoints=" + velocityPoints
                + ", user=" + user + ", project=" + project + ", group=" + group + ", time=" + time + "]";
    }

    @Override
    @Transient
    public LocalDate getDate() {
        return time.getDate();
    }

    @Override
    @Transient
    public Double getData() {
        return velocityPoints;
    }

}
