package com.learning.estimator.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import com.learning.estimator.model.views.EstimatorViews;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines a system user
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "index_on_username", columnList = "username", unique = true)
        })
//eagerly fetch user groups and user roles
@NamedEntityGraph(name = "graph.User.fetchPolicy",
        attributeNodes = {
                @NamedAttributeNode(value = "groups", subgraph = "userGroups")
        }
)
public class User implements Serializable {

    private Long userId;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private Set<UserRole> roles;
    private Set<UserGroup> groups;
    private Long version;

    @SuppressWarnings("unused")
    @Deprecated
    private User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
        this.groups = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "username")
    @JsonView(EstimatorViews.Summary.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public void addUserRole(UserRole role) {
        this.roles.add(role);
    }

    public void removeUserRole(UserRole role) {
        this.roles.remove(role);
    }

    public User withRoles(Set<UserRole> roles) {
        this.setRoles(roles);
        return this;
    }

    public User withUserRole(UserRole role) {
        this.addUserRole(role);
        return this;
    }

    public User withoutUserRole(UserRole role) {
        this.removeUserRole(role);
        return this;
    }

    @ManyToMany(cascade = CascadeType.DETACH)
    public Set<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = groups;
    }

    public void addUserGroup(UserGroup group) {
        groups.add(group);
    }

    public void removeUserGroup(UserGroup group) {
        groups.remove(group);
    }

    public User withGroups(Set<UserGroup> groups) {
        this.setGroups(groups);
        return this;
    }

    public User withUserGroup(UserGroup group) {
        this.addUserGroup(group);
        return this;
    }

    public User withoutUserGroup(UserGroup group) {
        this.removeUserGroup(group);
        return this;
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
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((roles == null) ? 0 : roles.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        User other = (User) obj;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (roles == null) {
            if (other.roles != null)
                return false;
        } else if (!roles.equals(other.roles))
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
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return username;
    }

}
