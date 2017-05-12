package com.learning.estimator.persistenceservice.config.security;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.persistence.facade.server.PersistenceFacadeServerSide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Custom UserDetailsService implementation
 *
 * @author rolea
 */
@Component
public class EstimatorUserDetailsService implements UserDetailsService {

    private static final ILogger LOG = LogManager.getLogger(EstimatorUserDetailsService.class);
    @Autowired
    private PersistenceFacadeServerSide facade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        try {
            user = facade.findUserByUsername(username);
        } catch (Exception e) {
            LOG.error(e);
            throw new UsernameNotFoundException("The user with the name " + username + " was not found");
        }
        return new EstimatorUserDetails(user);
    }

}
