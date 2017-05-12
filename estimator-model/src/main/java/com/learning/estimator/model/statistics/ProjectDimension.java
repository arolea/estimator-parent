package com.learning.estimator.model.statistics;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Models the project dimension
 *
 * @author rolea
 */
@Entity
@Table(name = "dimension_project", indexes = {
        @Index(name = "index_on_dimension_project_name", columnList = "project_name", unique = false)
})
@SuppressWarnings("serial")
public class ProjectDimension implements Serializable {

    //reuses the UID generated when the entity is saved
    private Long projectId;
    private String projectName;
    private LocalDateTime dateCreated;

    @Deprecated
    public ProjectDimension() {
    }

    public ProjectDimension(Long projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.dateCreated = LocalDateTime.now();
    }

    @Id
    @Column(name = "project_id")
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Column(name = "project_name")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column(name = "date_created")
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
        result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
        result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
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
        ProjectDimension other = (ProjectDimension) obj;
        if (dateCreated == null) {
            if (other.dateCreated != null)
                return false;
        } else if (!dateCreated.equals(other.dateCreated))
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
        return true;
    }

    @Override
    public String toString() {
        return "ProjectDimension [projectId=" + projectId + ", projectName=" + projectName + ", dateCreated="
                + dateCreated + "]";
    }

}
