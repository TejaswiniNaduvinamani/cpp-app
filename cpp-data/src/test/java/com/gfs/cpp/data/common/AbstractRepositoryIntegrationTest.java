package com.gfs.cpp.data.common;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.cpp.data.config.RepositoryIntegrationTestConfig;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;

@ContextConfiguration(classes = { RepositoryIntegrationTestConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class })
@Transactional
@ActiveProfiles("hsqldb")
public abstract class AbstractRepositoryIntegrationTest {

}
