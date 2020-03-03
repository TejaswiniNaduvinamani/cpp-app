package com.gfs.cpp.common.dto.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.gfs.cpp.common.dto.clm.ClmApplicationTokenDO;

public class ClmApplicationTokenDOTest {

    private ClmApplicationTokenDO target = new ClmApplicationTokenDO();

    @Test
    public void shouldValidateClmApplicationToken() throws Exception {

        String applicationTokenValue = "token value";
        int clmApplicationTokenId = 123;

        target.setApplicationTokenValue(applicationTokenValue);
        target.setClmApplicationTokenId(clmApplicationTokenId);

        ClmApplicationTokenDO actual = SerializationUtils.clone(target);

        assertThat(actual, equalTo(target));
        assertThat(actual.hashCode(), equalTo(target.hashCode()));
        assertThat(actual.toString(), notNullValue());

        assertThat(actual.getApplicationTokenValue(), equalTo(applicationTokenValue));
        assertThat(actual.getClmApplicationTokenId(), equalTo(clmApplicationTokenId));

    }

}
