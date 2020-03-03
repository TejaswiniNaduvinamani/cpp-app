package com.gfs.cpp.common.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Test;

import com.gfs.cpp.common.util.ContractPriceProfileStatus;

public class ContractPriceProfileStatusTest {

    @Test
    public void shouldGetStatusByCode() throws Exception {
        assertThat(ContractPriceProfileStatus.getStatusByCode(10), equalTo(ContractPriceProfileStatus.DRAFT));
    }

    @Test
    public void shouldReturnNullWhenCodeNotFound() throws Exception {
        assertThat(ContractPriceProfileStatus.getStatusByCode(1), nullValue());
    }

    @Test
    public void shouldReturnDescription() throws Exception {
        ContractPriceProfileStatus pricingActivated = ContractPriceProfileStatus.PRICING_ACTIVATED;
        assertThat(pricingActivated.getDesc(), equalTo("Pricing Activated"));
    }

}
