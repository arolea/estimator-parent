package com.learning.estimator.persistenceservice;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.datapublisher.config.DataPublisherConfig;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.model.statistics.UserDimension;
import com.learning.estimator.persistence.config.server.CorePersistenceConfigServerSide;
import com.learning.estimator.persistence.config.statistics.StatisticsPersistenceConfig;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.learning.estimator.persistence.facade.statistics.StatisticsFacade;
import com.learning.estimator.persistencesd.config.JpaPersistenceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Boot for persistence service
 *
 * @author rolea
 */
@SpringBootApplication
@Import(value = {CorePersistenceConfigServerSide.class, JpaPersistenceConfig.class, StatisticsPersistenceConfig.class, DataPublisherConfig.class})
public class PersistenceServiceBoot implements CommandLineRunner {

    private static final ILogger LOG = LogManager.getLogger(PersistenceServiceBoot.class);
    @Autowired
    private PersistenceFacadeClientSide infrastructurePersistence;
    @Autowired
    private StatisticsFacade statisticsFacade;
    private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        SpringApplication.run(PersistenceServiceBoot.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (infrastructurePersistence.countUsers() == 0) {
            LOG.info("Initializing users -> adding user 'admin' with password 'admin'");
            addUser("admin", "admin", UserRole.ROLE_ADMIN);
        } else {
            LOG.info("Users are already initialized");
        }
    }

    private void addUser(String username, String password, UserRole... roles) {
        User user = new User(username, bcryptEncoder.encode(password));
        for (UserRole role : roles) {
            user.addUserRole(role);
        }
        user = infrastructurePersistence.saveUser(user);
        statisticsFacade.saveUser(new UserDimension(user.getUserId(), user.getUsername()));
    }

}
