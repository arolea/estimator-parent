package com.learning.estimator.model.statistics;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Models the user group dimension
 *
 * @author rolea
 */
@Entity
@Table(name = "dimension_group", indexes = {
        @Index(name = "index_on_dimension_group_name", columnList = "group_name", unique = false)
})
@SuppressWarnings("serial")
public class GroupDimension implements Serializable {

    //reuses the UID generated when the entity is saved
    private Long groupId;
    private String groupName;
    private LocalDateTime dateCreated;

    @Deprecated
    public GroupDimension() {
    }

    public GroupDimension(Long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.dateCreated = LocalDateTime.now();
    }

    @Id
    @Column(name = "group_id")
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
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
        GroupDimension other = (GroupDimension) obj;
        if (dateCreated == null) {
            if (other.dateCreated != null)
                return false;
        } else if (!dateCreated.equals(other.dateCreated))
            return false;
        if (groupId == null) {
            if (other.groupId != null)
                return false;
        } else if (!groupId.equals(other.groupId))
            return false;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GroupDimension [groupId=" + groupId + ", groupName=" + groupName + ", dateCreated="
                + dateCreated + "]";
    }

}
