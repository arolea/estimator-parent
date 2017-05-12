package com.learning.estimator.gui.config;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.model.infrastructure.LoginOutcome;
import com.learning.estimator.model.infrastructure.Outcome;
import com.learning.estimator.persistenceclient.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implements REST authentication procedure
 *
 * @author rolea
 */
@Component
public class RestAuthenticationProvider implements AuthenticationProvider {

    private static final ILogger LOG = LogManager.getLogger(RestAuthenticationProvider.class);
    @Autowired
    private LoginService loginService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        LoginOutcome outcome = null;
        try {
            outcome = loginService.login(name, password);
        } catch (Exception e) {

        }

        if (outcome == null) {
            throw new AuthenticationServiceException("Login failed.");
        }

        if (outcome.getOutcome().equals(Outcome.FAILURE)) {
            LOG.info("Login failure :\n" + outcome);
            throw new AuthenticationServiceException(outcome.getMessage());
        }

        LOG.info("Login successfull");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(name, password, getAsGrantedAuthority(outcome.getUser().getRoles().stream().map(UserRole::toString).collect(Collectors.toSet())));
        //need to save token and userId into session
        token.setDetails(outcome);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Set<GrantedAuthority> getAsGrantedAuthority(Set<String> roles) {
        final Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> authorities.add(() -> {
            return role;
        }));
        return authorities;
    }

}
