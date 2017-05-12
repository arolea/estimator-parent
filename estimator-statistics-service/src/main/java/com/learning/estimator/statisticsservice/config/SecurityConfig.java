package com.learning.estimator.statisticsservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Security configuration
 *
 * @author rolea
 */
@Configuration
@EnableWebSecurity
@PropertySource(value = "classpath:api-config.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${api.statistics.username}")
    private String username;
    @Value("${api.statistics.password}")
    private String password;

    /**
     * Defines authentication policy
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //authenticate anything
                .anyRequest().authenticated()
                //HTTP Basic authentication over HTTPS
                .and().requiresChannel()
                .and().httpBasic()
                //no session is created by Spring
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //HTTPBasic on each request
                .and().csrf().disable();
    }

    /**
     * Defines available users
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(username).password(password).roles("USER", "ADMIN");
    }

}
