package com.gfs.cpp.common.dto.priceactivation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.priceactivation.PriceProfileDTO;

@RunWith(MockitoJUnitRunner.class)
public class PriceProfileDTOTest {

    @InjectMocks
    private PriceProfileDTO dto;

    @Test
    public void testMethods() {
        dto.setCostModelId(1);
        dto.setPrcProfAuditAuthInd(true);
        dto.setPrcProfCostSchedulePkgSeq(2);
        dto.setPrcProfVerifyPrivInd(true);
        dto.setPricingOverrideId(1);
        dto.setPricingOverrideInd(false);

        final PriceProfileDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getCostModelId(), is(actual.getCostModelId()));
        assertThat(dto.getPrcProfCostSchedulePkgSeq(), is(actual.getPrcProfCostSchedulePkgSeq()));
        assertThat(dto.getPricingOverrideId(), is(actual.getPricingOverrideId()));
        assertThat(dto.isPrcProfAuditAuthInd(), is(actual.isPrcProfAuditAuthInd()));
        assertThat(dto.isPrcProfVerifyPrivInd(), is(actual.isPrcProfVerifyPrivInd()));
        assertThat(dto.isPricingOverrideInd(), is(actual.isPricingOverrideInd()));

    }

}
