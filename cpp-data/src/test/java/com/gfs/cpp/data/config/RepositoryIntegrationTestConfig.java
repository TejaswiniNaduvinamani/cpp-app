package com.gfs.cpp.data.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;

@Configuration
@PropertySource("classpath:cpp-repositorytest.properties")
@ComponentScan(basePackages = "com.gfs.cpp.data, com.gfs.cpp.common.util", lazyInit = true)
@EnableTransactionManagement
public class RepositoryIntegrationTestConfig {
    
    @Autowired
    private Environment environment;

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(cppDataSource());
    }

    @Bean
    public DataSource cppDataSource() {

        final BasicDataSource driverManagerDataSource = new BasicDataSource();
        driverManagerDataSource.setDriverClassName(environment.getProperty("cpp.datasource.driver_class"));
        driverManagerDataSource.setUrl(environment.getProperty("cpp.datasource.connection.url"));
        driverManagerDataSource.setUsername(environment.getProperty("cpp.datasource.username"));
        driverManagerDataSource.setPassword(environment.getProperty("cpp.ot1.datasource.password"));
        driverManagerDataSource.setDefaultAutoCommit(true);
        driverManagerDataSource.setValidationQuery("select * from dual");

        return new TransactionAwareDataSourceProxy(driverManagerDataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {

        return new JdbcTemplate(cppDataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate cppJdbcTemplate() {

        return new NamedParameterJdbcTemplate(cppDataSource());
    }

    @Bean
    public DatabaseDataSourceConnection dbUnitDatabaseConnection() throws Exception {

        final DatabaseDataSourceConnectionFactoryBean bean = new DatabaseDataSourceConnectionFactoryBean();
        bean.setSchema("PRICE_ADMIN");
        bean.setDataSource(cppDataSource());
        bean.setDatabaseConfig(databaseConfig);

        return bean.getObject();
    }

    @Autowired(required = false)
    private DatabaseConfigBean databaseConfig;

}
