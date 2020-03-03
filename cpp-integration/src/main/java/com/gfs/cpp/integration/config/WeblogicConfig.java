/**  © Gordon Food Service, Inc. All Rights Reserved.*/
package com.gfs.cpp.integration.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.WebLogicJtaTransactionManager;

@Configuration
@Profile("weblogic")
@EnableTransactionManagement
public class WeblogicConfig {

    @Bean(name = "cppDataSource", destroyMethod = "")
    public DataSource cppDataSource() throws NamingException {
        JndiObjectFactoryBean factoryBean = new JndiObjectFactoryBean();
        factoryBean.setJndiName("java:comp/env/jdbc/CustomerPriceProfileDS");
        factoryBean.afterPropertiesSet();
        return (DataSource) factoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new WebLogicJtaTransactionManager();
    }
}