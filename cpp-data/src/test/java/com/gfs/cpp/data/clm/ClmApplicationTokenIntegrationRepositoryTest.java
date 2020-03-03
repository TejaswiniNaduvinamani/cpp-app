package com.gfs.cpp.data.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.dto.clm.ClmApplicationTokenDO;
import com.gfs.cpp.data.clm.ClmApplicationTokenRepository;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value = "ClmApplicationTokenIntegrationRepositoryTest.xml")
public class ClmApplicationTokenIntegrationRepositoryTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private ClmApplicationTokenRepository target;

    @Test
    public void shouldFetchAllClmApplicationTokens() throws Exception {

        List<ClmApplicationTokenDO> actualTokens = target.fetchAllClmApplicationTokens();

        assertThat(actualTokens.size(), equalTo(1));

        ClmApplicationTokenDO actualToken = actualTokens.get(0);
        assertThat(actualToken.getApplicationTokenValue(), equalTo("Token2"));
        assertThat(actualToken.getClmApplicationTokenId(), equalTo(-342));

    }

}
