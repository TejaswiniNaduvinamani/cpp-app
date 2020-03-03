package com.gfs.cpp.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gfs.corp.item.common.service.ItemQueryService;
import com.gfs.cpp.component.distributioncenter.DistributionSOAPClientService;

@Configuration
public class ProxyTestConfig {

    @Bean
    public DistributionSOAPClientService distSOAPService() {
        return null;
    }

    @Bean
    public ItemQueryService itemQueryService() {
        return null;
    }
}