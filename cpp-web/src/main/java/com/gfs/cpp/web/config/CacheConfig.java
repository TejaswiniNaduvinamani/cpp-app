package com.gfs.cpp.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import net.sf.ehcache.management.ManagementService;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${ehcache.config.file.name}")
    private String ehCacheConfigFileName;

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        final EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource(ehCacheConfigFileName));
        cmfb.setShared(true);
        return cmfb;
    }
    
    @Bean
    public ManagementService ehCacheManagementService() {
        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        final ManagementService managementService = new ManagementService(ehCacheCacheManager().getObject(), mBeanServer, false, false, false, true);
        managementService.init();
        return managementService;
    }

}