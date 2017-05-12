package com.learning.estimator.model.statistics;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Models the logged time fact
 *
 * @author rolea
 */
@Entity
@Table(name = "fact_logged_times", indexes = {
        @Index(name = "index_on_user_logged", columnList = "user_id", unique = false),
        @Index(name = "index_on_project_logged", columnList = "project_id", unique = false),
        @Index(name = "index_on_group_logged", columnList = "group_id", unique = false),
        @Index(name = "index_on_time_logged", columnList = "time_id", unique = false)
})
//eagerly fetch time
@NamedEntityGraph(name = "graph.LoggedTimeFact.fetchPolicy",
        attributeNodes = @NamedAttributeNode(value = "time", subgraph = "time")
)
@SuppressWarnings("serial")
public class LoggedTimeFact implements Serializable, IDateProvider, IDataProvider {

    private Long loggedTimeId;
    private Double loggedTime;
    private UserDimension user;
    private ProjectDimension project;
    private GroupDimension group;
    private TimeDimension time;

    @Deprecated
    public LoggedTimeFact() {
    }

    public LoggedTimeFact(Double loggedTime, UserDimension user, ProjectDimension project, GroupDimension group, TimeDimension time) {
        this.loggedTime = loggedTime;
        this.user = user;
        this.project = project;
        this.group = group;
        this.time = time;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "logged_time_id")
    public Long getLoggedTimeId() {
        return loggedTimeId;
    }

    public void setLoggedTimeId(Long loggedTimeId) {
        this.loggedTimeId = loggedTimeId;
    }

    @Column(name = "logged_time", columnDefinition = "Decimal(10,4)")
    public Double getLoggedTime() {
        return loggedTime;
    }

    public void setLoggedTime(Double loggedTime) {
        this.loggedTime = loggedTime;
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
        result = prime * result + ((loggedTime == null) ? 0 : loggedTime.hashCode());
        result = prime * result + ((loggedTimeId == null) ? 0 : loggedTimeId.hashCode());
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
        LoggedTimeFact other = (LoggedTimeFact) obj;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (loggedTime == null) {
            if (other.loggedTime != null)
                return false;
        } else if (!loggedTime.equals(other.loggedTime))
            return false;
        if (loggedTimeId == null) {
            if (other.loggedTimeId != null)
                return false;
        } else if (!loggedTimeId.equals(other.loggedTimeId))
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
        return "LoggedTimesFact [loggedTimeId=" + loggedTimeId + ", loggedTime=" + loggedTime + ", user=" + user
                + ", project=" + project + ", group=" + group + ", time=" + time + "]";
    }

    @Override
    @Transient
    public Double getData() {
        return loggedTime;
    }

    @Override
    @Transient
    public LocalDate getDate() {
        return time.getDate();
    }

}
