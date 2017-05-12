package com.learning.estimator.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.learning.estimator.model.views.EstimatorViews;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Defines a project
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "projects",
        indexes = {
                @Index(name = "index_on_project_name", columnList = "project_name", unique = true)
        })
//eagerly fetch user group
@NamedEntityGraph(name = "graph.Project.fetchPolicy",
        attributeNodes = @NamedAttributeNode(value = "userGroup", subgraph = "userGroup")
)
public class Project implements Serializable {

    private Long projectId;
    @NotNull
    private UserGroup userGroup;
    @NotNull
    private String projectName;
    @NotNull
    private String projectDescription;
    private ZonedDateTime createdDate;
    private Long version;

    @SuppressWarnings("unused")
    @Deprecated
    private Project() {
    }

    public Project(UserGroup userGroup, String projectName, String projectDescription) {
        this.userGroup = userGroup;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.createdDate = ZonedDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_group_id")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @JsonView(EstimatorViews.Summary.class)
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Column(name = "project_name")
    @JsonView(EstimatorViews.Summary.class)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column(name = "project_description")
    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    @Column(name = "created_date")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((projectDescription == null) ? 0 : projectDescription.hashCode());
        result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
        result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
        result = prime * result + ((userGroup == null) ? 0 : userGroup.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        Project other = (Project) obj;
        if (projectDescription == null) {
            if (other.projectDescription != null)
                return false;
        } else if (!projectDescription.equals(other.projectDescription))
            return false;
        if (projectId == null) {
            if (other.projectId != null)
                return false;
        } else if (!projectId.equals(other.projectId))
            return false;
        if (projectName == null) {
            if (other.projectName != null)
                return false;
        } else if (!projectName.equals(other.projectName))
            return false;
        if (userGroup == null) {
            if (other.userGroup != null)
                return false;
        } else if (!userGroup.equals(other.userGroup))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return projectName;
    }

}
