package com.learning.estimator.model.statistics;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Models the user dimension
 *
 * @author rolea
 */
@Entity
@Table(name = "dimension_user", indexes = {
        @Index(name = "index_on_dimension_username", columnList = "username", unique = false)
})
@SuppressWarnings("serial")
public class UserDimension implements Serializable {

    //reuses the UID generated when the entity is saved
    private Long userId;
    private String username;
    private LocalDateTime dateCreated;

    @Deprecated
    public UserDimension() {
    }

    public UserDimension(Long userId, String username) {
        this.userId = userId;
        this.username = username;
        this.dateCreated = LocalDateTime.now();
    }

    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        UserDimension other = (UserDimension) obj;
        if (dateCreated == null) {
            if (other.dateCreated != null)
                return false;
        } else if (!dateCreated.equals(other.dateCreated))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserDimension [userId=" + userId + ", username=" + username + ", dateCreated=" + dateCreated + "]";
    }

}
