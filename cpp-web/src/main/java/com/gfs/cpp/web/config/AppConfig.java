package com.gfs.cpp.web.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@Import({ SecurityConfig.class })
@ComponentScan(basePackages = "com.gfs.cpp")
public class AppConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private DataSource cppDataSource;

    private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

    public AppConfig() {
        LOG.info("Starting Application for Customer Price Profile");
    }
    
    @Override 
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public NamedParameterJdbcTemplate cppJdbcTemplate() {
        return new NamedParameterJdbcTemplate(cppDataSource);
    }

}