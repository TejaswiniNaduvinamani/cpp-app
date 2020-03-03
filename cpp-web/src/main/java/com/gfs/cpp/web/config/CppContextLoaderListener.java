package com.gfs.cpp.web.config;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.gfs.util.spring.config.EncryptedResourcePropertySource;

public class CppContextLoaderListener extends ContextLoaderListener {
    
    @Override
    protected void configureAndRefreshWebApplicationContext(final ConfigurableWebApplicationContext appContext, final ServletContext servletContext) {
        configureEncryptedPropertySource(appContext);
        super.configureAndRefreshWebApplicationContext(appContext, servletContext);
    }
    
    private void configureEncryptedPropertySource(final ConfigurableApplicationContext appContext) {
        final String name = "classpath:cpp." + System.getProperty("runtime.env") + ".properties";    	
        try {
            final EncryptedResourcePropertySource props = new EncryptedResourcePropertySource(name, "x52ebh8lgyo=");
            appContext.getEnvironment().getPropertySources().addFirst(props);           
        } catch (final IOException e) {
            throw new IllegalStateException("Cannot load " + name, e);
        }
    }
}
