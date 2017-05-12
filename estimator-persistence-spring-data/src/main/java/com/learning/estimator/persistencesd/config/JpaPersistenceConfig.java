package com.learning.estimator.persistencesd.config;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * Defines JPA persistence configuration
 *
 * @author rolea
 */
@Configuration
@ComponentScan(basePackages = {"com.learning.estimator.persistencesd.service.impl", "com.learning.estimator.persistencesd.exceptions"})
@EnableJpaRepositories(basePackages = {"com.learning.estimator.persistencesd.repositories"})
@EntityScan(basePackages = {"com.learning.estimator.model"})
@PropertySource(value = "classpath:persistence.properties")
@EnableAutoConfiguration
@EnableAspectJAutoProxy
public class JpaPersistenceConfig {

    @Value("${dev.mysql.host}")
    private String devHost;
    @Value("${dev.mysql.port}")
    private Integer devPort;
    @Value("${dev.mysql.user}")
    private String devUser;
    @Value("${dev.mysql.password}")
    private String devPassword;
    @Value("${dev.mysql.database}")
    private String devDatabase;

    @Value("${prod.mysql.host}")
    private String prodHost;
    @Value("${prod.mysql.port}")
    private Integer prodPort;
    @Value("${prod.mysql.user}")
    private String prodUser;
    @Value("${prod.mysql.password}")
    private String prodPassword;
    @Value("${prod.mysql.database}")
    private String prodDatabase;

    @Bean
    @Profile(value = "test")
    public DataSource getDevDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    @Profile(value = "prod")
    public DataSource getProdDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(prodHost);
        dataSource.setPort(prodPort);
        dataSource.setUser(prodUser);
        dataSource.setPassword(prodPassword);
        dataSource.setDatabaseName(prodDatabase);
        return dataSource;
    }

    @Bean
    @Profile(value = "dev")
    public DataSource getTestDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(devHost);
        dataSource.setPort(devPort);
        dataSource.setUser(devUser);
        dataSource.setPassword(devPassword);
        dataSource.setDatabaseName(devDatabase);
        return dataSource;
    }

}
