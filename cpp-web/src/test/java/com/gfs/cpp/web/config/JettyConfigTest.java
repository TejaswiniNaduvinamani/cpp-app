package com.gfs.cpp.web.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class JettyConfigTest extends BaseTestClass {

    @InjectMocks
    private JettyConfig JettyConfig;

    @Mock
    private Environment environment;

    @Test
    public void transactionManagerTest() {
        PlatformTransactionManager platformTransactionManager = JettyConfig.transactionManager();
    }
}
