package com.gfs.cpp.integration.config;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.gfs.corp.customer.common.controller.CustomerGroupMembershipQuery;
import com.gfs.corp.customer.common.controller.CustomerQuery;
import com.gfs.corp.customer.common.controller.MemberHierarchyQuery;
import com.gfs.corp.item.common.service.ItemQueryService;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.proxy.ModeledCostQueryServiceProxy;
import com.gfs.util.spring.security.remoting.BasicAuthenticationHttpInvokerRequestExecutor;

@Configuration
public class ExternalServiceConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private BasicAuthenticationHttpInvokerRequestExecutor basicAuthenticationHttpInvokerRequestExecutor;

    @Value("${customer.http.service.url}")
    private String customerHostUrl;

    @Value("${cost.service.url}")
    private String costServiceUrl;

    @Value("${cpp.external.service.username}")
    private String cppUsername;
    
    @Value("${cpp.external.service.password}")
    private String cppPassword;
    
    private static final String ITEM_QUERY_SERVICE_URL = "remoting/httpItemQueryService";
    private static final String MEMBER_HIERARCHY_QUERY_SERVICE_URL = "remoting/memberHierarchyQuery";
    private static final String CUSTOMER_QUERY_SERVICE_URL = "remoting/customerQuery";
    private static final String CUSTOMER_GROUP_MEMBERSHIP_QUERY_SERVICE_URL = "remoting/customerGroupMembershipQuery";
    private static final String COST_MODEL_QUERY_SERVICE_URL = "services/v3/ModeledCostQueryService";

    @Bean
    public ItemQueryService itemQueryService() {
        final HttpInvokerProxyFactoryBean proxyFactoryBean = new HttpInvokerProxyFactoryBean();
        proxyFactoryBean.setServiceInterface(ItemQueryService.class);
        proxyFactoryBean.setServiceUrl(environment.getProperty(CPPConstants.ITEM_QUERY_HOST_URL) + ITEM_QUERY_SERVICE_URL);
        proxyFactoryBean.setHttpInvokerRequestExecutor(basicAuthenticationHttpInvokerRequestExecutor);

        proxyFactoryBean.afterPropertiesSet();
        return (ItemQueryService) proxyFactoryBean.getObject();
    }

    @Bean
    public MemberHierarchyQuery memberHierarchyQueryService() {
        final HttpInvokerProxyFactoryBean proxyFactoryBean = new HttpInvokerProxyFactoryBean();
        proxyFactoryBean.setServiceInterface(MemberHierarchyQuery.class);
        proxyFactoryBean.setServiceUrl(customerHostUrl + MEMBER_HIERARCHY_QUERY_SERVICE_URL);
        proxyFactoryBean.afterPropertiesSet();
        return (MemberHierarchyQuery) proxyFactoryBean.getObject();
    }

    @Bean
    public CustomerQuery customerQuery() {
        final HttpInvokerProxyFactoryBean proxyFactoryBean = new HttpInvokerProxyFactoryBean();
        proxyFactoryBean.setServiceInterface(CustomerQuery.class);
        proxyFactoryBean.setServiceUrl(customerHostUrl + CUSTOMER_QUERY_SERVICE_URL);
        proxyFactoryBean.afterPropertiesSet();
        return (CustomerQuery) proxyFactoryBean.getObject();
    }

    @Bean
    public CustomerGroupMembershipQuery customerGroupMembershipQuery() {
        final HttpInvokerProxyFactoryBean proxyFactoryBean = new HttpInvokerProxyFactoryBean();
        proxyFactoryBean.setServiceInterface(CustomerGroupMembershipQuery.class);
        proxyFactoryBean.setServiceUrl(customerHostUrl + CUSTOMER_GROUP_MEMBERSHIP_QUERY_SERVICE_URL);
        proxyFactoryBean.afterPropertiesSet();
        return (CustomerGroupMembershipQuery) proxyFactoryBean.getObject();
    }

    @Bean
    public ModeledCostQueryServiceProxy modeledCostQueryServiceProxy() {
        final JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(ModeledCostQueryServiceProxy.class);
        jaxWsProxyFactoryBean.setAddress(costServiceUrl + COST_MODEL_QUERY_SERVICE_URL);
        jaxWsProxyFactoryBean.setUsername(environment.getProperty("cpp.external.service.username"));
        jaxWsProxyFactoryBean.setPassword(environment.getProperty("cpp.external.service.password"));
        return (ModeledCostQueryServiceProxy) jaxWsProxyFactoryBean.create();
    }

    @Bean
    public BasicAuthenticationHttpInvokerRequestExecutor basicAuthenticationHttpInvokerRequestExecutor() {
        final BasicAuthenticationHttpInvokerRequestExecutor userAuthenticationExecutor = new BasicAuthenticationHttpInvokerRequestExecutor();
        userAuthenticationExecutor.setUsername(environment.getProperty("cpp.external.service.username"));
        userAuthenticationExecutor.setPassword(environment.getProperty("cpp.external.service.password"));
        return userAuthenticationExecutor;
    }

}
