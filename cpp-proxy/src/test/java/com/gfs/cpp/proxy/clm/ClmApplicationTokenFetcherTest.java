package com.gfs.cpp.proxy.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.gfs.cpp.common.dto.clm.ClmApplicationTokenDO;
import com.gfs.cpp.data.clm.ClmApplicationTokenRepository;

@RunWith(MockitoJUnitRunner.class)
public class ClmApplicationTokenFetcherTest {

    @InjectMocks
    private ClmApplicationTokenFetcher target;

    @Mock
    private ClmApplicationTokenRepository clmApplicationTokenRepository;

    @Test
    public void shouldLaadAllTokenWhenTokenNotLoadedAlready() throws Exception {

        String applicationTokenValue = "tokent";

        ClmApplicationTokenDO clmApplicationTokenDO = new ClmApplicationTokenDO();
        clmApplicationTokenDO.setApplicationTokenValue(applicationTokenValue);

        doReturn(Collections.singletonList(clmApplicationTokenDO)).when(clmApplicationTokenRepository).fetchAllClmApplicationTokens();

        assertThat(target.getApplicationToken(), equalTo(applicationTokenValue));

        verify(clmApplicationTokenRepository).fetchAllClmApplicationTokens();
    }

    @Test
    public void shoulReturnLoadedWhenTokenIsLoadedAlready() throws Exception {

        String applicationTokenValue = "tokent";

        ClmApplicationTokenDO clmApplicationTokenDO = new ClmApplicationTokenDO();
        clmApplicationTokenDO.setApplicationTokenValue(applicationTokenValue);

        ReflectionTestUtils.setField(target, "clmApplicationTokenDO", clmApplicationTokenDO);

        assertThat(target.getApplicationToken(), equalTo(applicationTokenValue));

        verify(clmApplicationTokenRepository, never()).fetchAllClmApplicationTokens();
    }

    @Test
    public void shouldReturnLoggedInUserIdFromClmApplicationToken() throws Exception {

        String applicationTokenValue = "tokent";
        int clmApplicationid = 209;

        ClmApplicationTokenDO clmApplicationTokenDO = new ClmApplicationTokenDO();
        clmApplicationTokenDO.setApplicationTokenValue(applicationTokenValue);
        clmApplicationTokenDO.setClmApplicationTokenId(clmApplicationid);

        doReturn(Collections.singletonList(clmApplicationTokenDO)).when(clmApplicationTokenRepository).fetchAllClmApplicationTokens();

        assertThat(target.getLoggedInUserId(), equalTo(clmApplicationid));

        verify(clmApplicationTokenRepository).fetchAllClmApplicationTokens();

    }

}
