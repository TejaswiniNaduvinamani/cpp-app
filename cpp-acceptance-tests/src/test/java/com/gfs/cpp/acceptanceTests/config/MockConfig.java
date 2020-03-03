package com.gfs.cpp.acceptanceTests.config;

import static org.mockito.Mockito.mock;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.corp.customer.common.controller.CustomerGroupMembershipQuery;
import com.gfs.corp.customer.common.controller.CustomerQuery;
import com.gfs.corp.customer.common.controller.MemberHierarchyQuery;
import com.gfs.corp.item.common.service.ItemConfigurationService;
import com.gfs.corp.item.common.service.ItemQueryService;
import com.gfs.cpp.component.distributioncenter.DistributionSOAPClientService;
import com.gfs.cpp.proxy.ModeledCostQueryServiceProxy;
import com.gfs.cpp.proxy.common.CppRestTemplate;

@Configuration
public class MockConfig {

    @Bean
    @Qualifier("mockito")
    public DistributionSOAPClientService distributionSOAPClientService() {
        return mock(DistributionSOAPClientService.class);
    }

    @Bean
    @Qualifier("mockito")
    public ModeledCostQueryServiceProxy modeledCostQueryServiceProxy() {
        return mock(ModeledCostQueryServiceProxy.class);
    }

    @Bean
    @Qualifier("mockito")
    public CppRestTemplate cppRestTemplate() {
        return mock(CppRestTemplate.class);
    }

    @Bean
    @Qualifier("mockito")
    public ItemQueryService getItemQueryService() {
        return mock(ItemQueryService.class);
    }

    @Bean
    @Qualifier("mockito")
    public RestTemplate clmRestTemplate() {
        return mock(RestTemplate.class);
    }

    @Bean
    @Qualifier("mockito")
    public GrantedAuthority grantedAuthority() {
        return mock(GrantedAuthority.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Qualifier("mockito")
    public MemberHierarchyQuery getMemberHierarchyQueryService() {
        return mock(MemberHierarchyQuery.class);
    }

    @Bean
    @Qualifier("mockito")
    public CustomerQuery getCustomerQuery() {
        return mock(CustomerQuery.class);
    }

    @Bean
    @Qualifier("mockito")
    public ItemConfigurationService getItemConfigurationService() {
        return mock(ItemConfigurationService.class);
    }

    @Bean
    @Qualifier("mockito")
    public CustomerGroupMembershipQuery getCustomerGroupMembershipQuery() {
        return mock(CustomerGroupMembershipQuery.class);
    }

}
