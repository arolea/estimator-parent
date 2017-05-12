package com.learning.estimator.persistenceservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;

/**
 * Avoids fetch of lazy loaded fields during controller serialization
 * Workaround -> https://github.com/spring-projects/spring-hateoas/issues/333
 *
 * @author rolea
 */
@Configuration
@EnableWebMvc
@EnableEntityLinks
@EnableDiscoveryClient
@EnableCircuitBreaker
public class ServiceConfig extends WebMvcConfigurerAdapter {

    private static final String SPRING_HATEOAS_OBJECT_MAPPER = "_halObjectMapper";

    //HATEOAS defines own mapper
    @Autowired
    @Qualifier(SPRING_HATEOAS_OBJECT_MAPPER)
    private ObjectMapper springHateoasObjectMapper;

    @Autowired
    private Jackson2ObjectMapperBuilder springBootObjectMapperBuilder;

    @Bean(name = "objectMapper")
    @Primary
    public ObjectMapper objectMapper() {
        springHateoasObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        springHateoasObjectMapper.registerModule(new Hibernate4Module());
        springHateoasObjectMapper.registerModule(new JavaTimeModule());
        //register hateoas mapper in spring boot default mapper
        this.springBootObjectMapperBuilder.configure(this.springHateoasObjectMapper);
        return springHateoasObjectMapper;
    }

    //otherwise only the rest controller is affected ( and RestTemplate will fail to deserialize )
    @Bean
    public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON, MediaType.APPLICATION_JSON));
        messageConverter.setObjectMapper(objectMapper());
        return messageConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jacksonMessageConverter());
        converters.add(new StringHttpMessageConverter());
        super.configureMessageConverters(converters);
    }

    /**
     * Support for ETags
     * Note that the filter is shallow ( saves network bandwidth but does not save database I/O interaction )
     */
    @Bean
    public Filter shallowETagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
