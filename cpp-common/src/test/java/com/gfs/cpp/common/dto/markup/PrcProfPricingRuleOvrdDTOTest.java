package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.PrcProfPricingRuleOvrdDTO;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfPricingRuleOvrdDTOTest {

    @InjectMocks
    private PrcProfPricingRuleOvrdDTO dto;

    @Test
    public void testMethods() {

        dto.setContractPriceProfileSeq(1);
        dto.setPricingOverrideId(1);
        dto.setPricingOverrideInd(1);
        dto.setGfsCustomerId("custId");
        dto.setGfsCustomerTypeCode(31);

        final PrcProfPricingRuleOvrdDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getPricingOverrideId(), is(dto.getPricingOverrideId()));
        assertThat(actual.getPricingOverrideInd(), is(dto.getPricingOverrideInd()));
        assertThat(actual.getGfsCustomerId(), is(dto.getGfsCustomerId()));
        assertThat(actual.getGfsCustomerTypeCode(), is(dto.getGfsCustomerTypeCode()));

    }
}
