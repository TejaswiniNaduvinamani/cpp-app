package com.gfs.cpp.proxy.integration.config;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gfs.cpp.common.dto.clm.CustomDateDeserializer;
import com.gfs.util.spring.config.EncryptedResourcePropertySource;

@Configuration
@PropertySource("classpath:cppComponentTest.properties")
@ComponentScan(basePackages = { "com.gfs.cpp.proxy.clm", "com.gfs.cpp.data.clm" }, lazyInit = true)
@EnableTransactionManagement
public class ClmIntegrationTestConfig {

    @Autowired
    private Environment environment;

    @Bean("clmRestTemplate")
    public RestTemplate clmRestTemplate() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, null);

        CloseableHttpClient client = HttpClientBuilder.create().setSSLContext(sslContext).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);

        return new RestTemplate(requestFactory);
    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Date.class, new CustomDateDeserializer());
        objectMapper.registerModule(simpleModule);

        return objectMapper;
    }

    @Bean("cppDataSource")
    public DataSource cppDataSource() {
        final BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        ds.setUrl(environment.getProperty("jdbc.url"));
        ds.setUsername(environment.getProperty("jdbc.username"));
        ds.setPassword(getPassword());
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

    private String getPassword() {
        try {
            EncryptedResourcePropertySource encryptedResourcePropertySource = new EncryptedResourcePropertySource(
                    "classpath:cppComponentTest.properties", "x52ebh8lgyo=");
            return (String) encryptedResourcePropertySource.getProperty("encrypted:jdbc.password");
        } catch (IOException e) {
            return null;
        }
    }

    @Bean
    public NamedParameterJdbcTemplate cppJdbcTemplate() {

        return new NamedParameterJdbcTemplate(cppDataSource());
    }

}
