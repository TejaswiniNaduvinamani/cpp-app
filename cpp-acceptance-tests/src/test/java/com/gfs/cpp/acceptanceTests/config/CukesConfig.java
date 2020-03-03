package com.gfs.cpp.acceptanceTests.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:cpp-acceptance-tests.${runtime.env}.properties")
@EnableTransactionManagement
public class CukesConfig {

    @Autowired
    private Environment environment;

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(cppDataSource());
    }

    @Bean
    public DataSource cppDataSource() {
        final SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName(environment.getProperty("cpp.datasource.driver"));
        dataSource.setUrl(environment.getProperty("cpp.datasource.url"));
        dataSource.setUsername(environment.getProperty("cpp.datasource.username"));
        dataSource.setPassword(environment.getProperty("cpp.datasource.password"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {

        return new JdbcTemplate(cppDataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate cppJdbcTemplate() {

        return new NamedParameterJdbcTemplate(cppDataSource());
    }
}
