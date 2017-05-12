package com.learning.estimator.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.learning.estimator.model.views.EstimatorViews;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;

/**
 * Defines a task
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tasks", indexes = {
        @Index(name = "index_on_user", columnList = "user_id", unique = false),
        @Index(name = "index_on_project", columnList = "project_id", unique = false),
        @Index(name = "index_on_status", columnList = "task_status", unique = false)
})
//eagerly fetch user and project, and eagerly fetch user group via project
@NamedEntityGraph(name = "graph.Task.fetchPolicy",
        attributeNodes = {@NamedAttributeNode(value = "user", subgraph = "user"),
                @NamedAttributeNode(value = "project", subgraph = "project")},
        subgraphs = @NamedSubgraph(name = "project", attributeNodes = {@NamedAttributeNode("userGroup")})
)
public class Task implements Serializable {

    //since NumberFormat is not thread safe
    private static final ThreadLocal<NumberFormat> NUMBER_FORMAT = new ThreadLocal<NumberFormat>() {
        @Override
        public NumberFormat initialValue() {
            return new DecimalFormat("#.###");
        }
    };
    private Long taskId;
    @NotNull
    private User user;
    @NotNull
    private Project project;
    @NotNull
    private String taskName;
    @NotNull
    private String taskDescription;
    @NotNull
    private Double bestCaseEstimate;
    @NotNull
    private Double avgCaseEstimate;
    @NotNull
    private Double worstCaseEstimate;
    private Double actualTimeSpent;
    private TaskStatus taskStatus;
    @NotNull
    private Double points;
    private ZonedDateTime finishedDate;
    private ZonedDateTime createdDate;
    private Long version;

    @SuppressWarnings("unused")
    @Deprecated
    private Task() {
    }

    public Task(User user, Project project, String taskName, String taskDescription, Double bestCaseEstimate, Double avgCaseEstimate, Double worstCaseEstimate, Double points) {
        this.user = user;
        this.project = project;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.bestCaseEstimate = bestCaseEstimate;
        this.avgCaseEstimate = avgCaseEstimate;
        this.worstCaseEstimate = worstCaseEstimate;
        this.points = points;
        this.taskStatus = TaskStatus.PENDING;
        this.createdDate = ZonedDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "estimate_id")
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long estimateId) {
        this.taskId = estimateId;
    }

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    @JsonView(EstimatorViews.Summary.class)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "project_id")
    @JsonView(EstimatorViews.Summary.class)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Column(name = "task_name")
    @JsonView(EstimatorViews.Summary.class)
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Column(name = "task_description")
    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Column(name = "best_case_estimate", columnDefinition = "Decimal(10,4)")
    @JsonView(EstimatorViews.Summary.class)
    public Double getBestCaseEstimate() {
        return bestCaseEstimate;
    }

    public void setBestCaseEstimate(Double bestCaseEstimate) {
        this.bestCaseEstimate = bestCaseEstimate;
    }

    @Column(name = "avg_case_estimate", columnDefinition = "Decimal(10,4)")
    @JsonView(EstimatorViews.Summary.class)
    public Double getAvgCaseEstimate() {
        return avgCaseEstimate;
    }

    public void setAvgCaseEstimate(Double avgCaseEstimate) {
        this.avgCaseEstimate = avgCaseEstimate;
    }

    @Column(name = "worst_case_estimate", columnDefinition = "Decimal(10,4)")
    @JsonView(EstimatorViews.Summary.class)
    public Double getWorstCaseEstimate() {
        return worstCaseEstimate;
    }

    public void setWorstCaseEstimate(Double worstCaseEstimate) {
        this.worstCaseEstimate = worstCaseEstimate;
    }

    @Column(name = "actual_time_spent", columnDefinition = "Decimal(10,4)")
    @JsonView(EstimatorViews.Summary.class)
    public Double getActualTimeSpent() {
        return actualTimeSpent;
    }

    public void setActualTimeSpent(Double actualTimeSpent) {
        this.actualTimeSpent = actualTimeSpent;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    @JsonView(EstimatorViews.Summary.class)
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Column(name = "task_points", columnDefinition = "Decimal(10,4)")
    @JsonView(EstimatorViews.Summary.class)
    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    @Column(name = "finished_date")
    public ZonedDateTime getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(ZonedDateTime finishedDate) {
        if (this.finishedDate == null) this.finishedDate = finishedDate;
    }

    @Column(name = "created_date")
    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createDate) {
        if (this.createdDate == null) this.createdDate = createDate;
    }

    @Version
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Transient
    @JsonIgnore
    public String getComputedEstimation() {
        return NUMBER_FORMAT.get().format(((bestCaseEstimate + 4 * avgCaseEstimate + worstCaseEstimate) / 6));
    }

    @Transient
    @JsonIgnore
    public String getComputedStandardDeviation() {
        return NUMBER_FORMAT.get().format(((worstCaseEstimate - bestCaseEstimate) / 6));
    }

    @Transient
    @JsonIgnore
    public String getEstimateAccuracy() {
        if (TaskStatus.DONE.equals(taskStatus)) {
            if (actualTimeSpent == null) {
                return "Log time spent!";
            }
            return NUMBER_FORMAT.get().format((actualTimeSpent - ((bestCaseEstimate + 4 * avgCaseEstimate + worstCaseEstimate) / 6)));
        } else {
            return "Not finished yet";
        }
    }

    @Transient
    @JsonIgnore
    public Double getEstimateAccuracyAsDouble() {
        try {
            return actualTimeSpent - ((bestCaseEstimate + 4 * avgCaseEstimate + worstCaseEstimate) / 6);
        } catch (Exception e) {
            return Double.MIN_VALUE;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actualTimeSpent == null) ? 0 : actualTimeSpent.hashCode());
        result = prime * result + ((avgCaseEstimate == null) ? 0 : avgCaseEstimate.hashCode());
        result = prime * result + ((bestCaseEstimate == null) ? 0 : bestCaseEstimate.hashCode());
        result = prime * result + ((finishedDate == null) ? 0 : finishedDate.hashCode());
        result = prime * result + ((points == null) ? 0 : points.hashCode());
        result = prime * result + ((taskDescription == null) ? 0 : taskDescription.hashCode());
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
        result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
        result = prime * result + ((taskStatus == null) ? 0 : taskStatus.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((worstCaseEstimate == null) ? 0 : worstCaseEstimate.hashCode());
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
        Task other = (Task) obj;
        if (actualTimeSpent == null) {
            if (other.actualTimeSpent != null)
                return false;
        } else if (!actualTimeSpent.equals(other.actualTimeSpent))
            return false;
        if (avgCaseEstimate == null) {
            if (other.avgCaseEstimate != null)
                return false;
        } else if (!avgCaseEstimate.equals(other.avgCaseEstimate))
            return false;
        if (bestCaseEstimate == null) {
            if (other.bestCaseEstimate != null)
                return false;
        } else if (!bestCaseEstimate.equals(other.bestCaseEstimate))
            return false;
        if (finishedDate == null) {
            if (other.finishedDate != null)
                return false;
        } else if (!finishedDate.equals(other.finishedDate))
            return false;
        if (points == null) {
            if (other.points != null)
                return false;
        } else if (!points.equals(other.points))
            return false;
        if (taskDescription == null) {
            if (other.taskDescription != null)
                return false;
        } else if (!taskDescription.equals(other.taskDescription))
            return false;
        if (taskId == null) {
            if (other.taskId != null)
                return false;
        } else if (!taskId.equals(other.taskId))
            return false;
        if (taskName == null) {
            if (other.taskName != null)
                return false;
        } else if (!taskName.equals(other.taskName))
            return false;
        if (taskStatus != other.taskStatus)
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        if (worstCaseEstimate == null) {
            if (other.worstCaseEstimate != null)
                return false;
        } else if (!worstCaseEstimate.equals(other.worstCaseEstimate))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Task [taskId=" + taskId + ", taskName=" + taskName + ", taskDescription=" + taskDescription
                + ", bestCaseEstimate=" + bestCaseEstimate + ", avgCaseEstimate=" + avgCaseEstimate
                + ", worstCaseEstimate=" + worstCaseEstimate + ", actualTimeSpent=" + actualTimeSpent + ", taskStatus="
                + taskStatus + ", points=" + points + ", finishedDate=" + finishedDate + ", version=" + version + "]";
    }

}
