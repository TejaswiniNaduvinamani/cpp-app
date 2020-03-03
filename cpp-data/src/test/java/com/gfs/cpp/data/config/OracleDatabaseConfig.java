package com.gfs.cpp.data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.springtestdbunit.bean.DatabaseConfigBean;

@Configuration
@Profile("oracle")
public class OracleDatabaseConfig {

    @Bean
    public DatabaseConfigBean databaseConfig() {

        final DatabaseConfigBean bean = new DatabaseConfigBean();
        bean.setCaseSensitiveTableNames(true);
        bean.setDatatypeFactory(new org.dbunit.ext.oracle.Oracle10DataTypeFactory());
        return bean;
    }
}
