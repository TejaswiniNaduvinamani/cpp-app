package com.gfs.cpp.common.dto.contractpricing;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingReviewDTO;

@RunWith(MockitoJUnitRunner.class)
public class ContractPricingReviewDTOTest {
    
    @InjectMocks
    private ContractPricingReviewDTO dto;
    
    @Test
    public void testMethods() {
        dto.setCostOfProductsContractLanguage("TEST");
        dto.setFormalPriceAudit("Yes");
        dto.setFormalPriceAuditContractLanguage("Yes");
        dto.setGfsLabelAssesmentFee("Yes");
        dto.setGfsTransferFee("Yes");
        dto.setPriceVerification("Yes");
        dto.setPriceVerificationContractLanguage("TEST");
        dto.setScheduleForCost("Yes");
        dto.setScheduleForCostContractLanguage("TEST");
        
        final ContractPricingReviewDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString()!=null, is(true));

        assertThat(actual.getCostOfProductsContractLanguage(), is(dto.getCostOfProductsContractLanguage()));
        assertThat(actual.getFormalPriceAudit(), is(dto.getFormalPriceAudit()));
        assertThat(actual.getFormalPriceAuditContractLanguage(), is(dto.getFormalPriceAuditContractLanguage()));
        assertThat(actual.getGfsLabelAssesmentFee(), is(dto.getGfsLabelAssesmentFee()));
        assertThat(actual.getGfsTransferFee(), is(dto.getGfsTransferFee()));
        assertThat(actual.getPriceVerification(), is(dto.getPriceVerification()));
        assertThat(actual.getPriceVerificationContractLanguage(), is(dto.getPriceVerificationContractLanguage()));
        assertThat(actual.getScheduleForCost(), is(dto.getScheduleForCost()));
        assertThat(actual.getScheduleForCostContractLanguage(), is(dto.getScheduleForCostContractLanguage()));
    }
}
