package com.gfs.cpp.integration.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.gfs.cpp", lazyInit = true)
public class AppConfig {

    @Autowired
    private DataSource cppDataSource;

    private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

    public AppConfig() {
        LOG.info("Starting Application for Customer Price Profile");
    }

    @Bean
    public NamedParameterJdbcTemplate cppJdbcTemplate() {
        return new NamedParameterJdbcTemplate(cppDataSource);
    }

}