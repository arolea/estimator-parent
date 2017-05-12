package com.learning.estimator.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.learning.estimator.model.views.EstimatorViews;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Defines an aggregation of users.
 * Projects are defined at an user group level.
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "user_groups",
        indexes = {
                @Index(name = "index_on_group_name", columnList = "user_group_name", unique = true)
        })
public class UserGroup implements Serializable {

    private Long userGroupId;
    @NotNull
    private String userGroupName;
    private ZonedDateTime createdDate;
    private Long version;

    @SuppressWarnings("unused")
    @Deprecated
    private UserGroup() {
    }

    public UserGroup(String name) {
        this.userGroupName = name;
        this.createdDate = ZonedDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_group_id")
    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    @Column(name = "user_group_name")
    @JsonView(EstimatorViews.Summary.class)
    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String name) {
        this.userGroupName = name;
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
        result = prime * result + ((userGroupName == null) ? 0 : userGroupName.hashCode());
        result = prime * result + ((userGroupId == null) ? 0 : userGroupId.hashCode());
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
        UserGroup other = (UserGroup) obj;
        if (userGroupName == null) {
            if (other.userGroupName != null)
                return false;
        } else if (!userGroupName.equals(other.userGroupName))
            return false;
        if (userGroupId == null) {
            if (other.userGroupId != null)
                return false;
        } else if (!userGroupId.equals(other.userGroupId))
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
        return userGroupName;
    }

}
