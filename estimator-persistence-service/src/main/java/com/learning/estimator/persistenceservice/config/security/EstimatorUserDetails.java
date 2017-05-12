package com.learning.estimator.persistenceservice.config.security;

import com.learning.estimator.model.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Custom UserDetails implementation
 *
 * @author rolea
 */
public class EstimatorUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private Collection<? extends GrantedAuthority> authorities;
    private Long userId;
    private String username;
    private String password;

    public EstimatorUserDetails(User user) {
        String roles = StringUtils.collectionToCommaDelimitedString(user.getRoles());
        this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
