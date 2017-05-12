package com.learning.estimator.statisticsservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration for Swagger
 * See /swagger-ui.html for details
 *
 * @author rolea
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${api.statistics.version}")
    private String apiVersion;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).
                select().
                apis(RequestHandlerSelectors.any()).
                paths(PathSelectors.ant("/" + apiVersion + "/**")).build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Estimator Statistics API",
                "Provides statistics functionality",
                "V1",
                "Terms of service",
                new Contact("Rolea", "/", "alexrolea93@gmail.com"),
                "License of API",
                "API license URL");
        return apiInfo;
    }

}