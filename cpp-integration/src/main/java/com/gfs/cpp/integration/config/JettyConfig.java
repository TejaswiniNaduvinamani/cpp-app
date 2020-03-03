/**  © Gordon Food Service, Inc. All Rights Reserved.*/
package com.gfs.cpp.integration.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile("jetty")
@EnableTransactionManagement
public class JettyConfig {
    @Autowired
    private Environment environment;


    @Bean("cppDataSource")
    public DataSource cppDataSource() {
        final BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        ds.setUrl(environment.getProperty("jdbc.url"));
        ds.setUsername(environment.getProperty("jdbc.username"));
        ds.setPassword(environment.getProperty("jdbc.password"));
        ds.setInitialSize(0);
        ds.setMinIdle(1);
        ds.setMaxActive(5);
        ds.setTestOnBorrow(false);
        ds.setTestOnReturn(true);
        ds.setTimeBetweenEvictionRunsMillis(60000);
        ds.setDefaultAutoCommit(false);
        ds.setValidationQuery("select 1 from dual");
        return ds;
    }


    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(cppDataSource());
    }

}