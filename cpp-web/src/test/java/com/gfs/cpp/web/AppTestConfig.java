package com.gfs.cpp.web;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.gfs.cpp.web.config.AppConfig;

@Import({ AppConfig.class })
@PropertySource("classpath:cppComponentTest.properties")
public class AppTestConfig {

}
