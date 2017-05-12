package com.learning.estimator.persistenceservice.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration
 *
 * @author rolea
 */
@Configuration
@EnableWebSecurity
//JSR250 should be enough - prePostEnabled for @PreAuthorize and @PostAuthorize for finer grained constraints
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@PropertySource(value = "classpath:api-config.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${api.persistence.version}")
    private String apiVersion;
    @Autowired
    private EstimatorUserDetailsService userDetailsService;

    /**
     * Defines security policy
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //allow requests to login and logout for anyone
                .mvcMatchers("/" + apiVersion + "/login/**", "/" + apiVersion + "/logout/**").permitAll()
                .mvcMatchers("/" + apiVersion + "/groups/**").authenticated()
                .mvcMatchers("/" + apiVersion + "/projects/**").authenticated()
                .mvcMatchers("/" + apiVersion + "/users/**").authenticated()
                .mvcMatchers("/" + apiVersion + "/tasks/**").authenticated()
                //allow unauthenticated access to resources and ui swagger endpoints
                .mvcMatchers("/swagger-resources/**", "/configuration/ui/**", "/swagger-ui.html", "/v2/api-docs/**").permitAll()
                //authenticate swagger security endpoint
                .mvcMatchers("/configuration/security/**").authenticated()
                //allow access to error page
                .mvcMatchers("/error/**").permitAll()
                //authenticate anything else - including actuator endpoints
                .anyRequest().authenticated()
                //HTTP Basic authentication over HTTPS
                .and().requiresChannel()
                .and().httpBasic()
                //no session is created by Spring 
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //HTTPBasic authentication on a per request basis
                .and().csrf().disable();
    }

    /**
     * Authentication against a database
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
