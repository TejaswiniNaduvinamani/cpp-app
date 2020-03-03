package com.gfs.cpp.integration.config;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import com.gfs.util.spring.config.EncryptedResourcePropertySource;

public class CppIntegrationContextLoadListener extends ContextLoaderListener {

    @Override
    protected void configureAndRefreshWebApplicationContext(final ConfigurableWebApplicationContext appContext, final ServletContext servletContext) {
        configureEncryptedPropertySource(appContext);
        super.configureAndRefreshWebApplicationContext(appContext, servletContext);
    }

    private void configureEncryptedPropertySource(final ConfigurableApplicationContext appContext) {
        final String name = "classpath:cpp-integration." + System.getProperty("runtime.env") + ".properties";
        try {
            final EncryptedResourcePropertySource props = new EncryptedResourcePropertySource(name, "x52ebh8lgyo=");
            appContext.getEnvironment().getPropertySources().addFirst(props);
        } catch (final IOException e) {
            throw new IllegalStateException("Cannot load " + name, e);
        }
    }

}
