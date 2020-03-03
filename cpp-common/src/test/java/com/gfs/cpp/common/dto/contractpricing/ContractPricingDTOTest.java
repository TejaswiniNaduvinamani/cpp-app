package com.gfs.cpp.common.dto.contractpricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;

@RunWith(MockitoJUnitRunner.class)
public class ContractPricingDTOTest {

    @InjectMocks
    private ContractPricingDTO dto;

    @Test
    public void testMethods() {
        dto.setContractType("TEST");
        dto.setAssessmentFeeFlag(true);
        dto.setContractName("TEST CONTRACT");
        dto.setContractPriceProfileId(1);
        dto.setContractPriceProfileSeq(1);
        dto.setPriceAuditFlag(true);
        dto.setPriceVerificationFlag(true);
        dto.setPricingEffectiveDate(new LocalDate(2017, 01, 01).toDate());
        dto.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        dto.setScheduleForCostChange("FISCAL");
        dto.setTransferFeeFlag(true);
        dto.setAgreementId("agreeement Id");
        dto.setExpireLowerLevelInd(1);
        dto.setPricingExhibitSysId("TEST-123");
        Date clmContractStartDate = new LocalDate(2018, 05, 24).toDate();
        Date clmContractEndDate = new LocalDate(2019, 05, 24).toDate();
        dto.setClmContractStartDate(clmContractStartDate);
        dto.setParentAgreementId("parent agreement Id");
        dto.setClmContractEndDate(clmContractEndDate);
        dto.setVersionNbr(0);

        final ContractPricingDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getContractType(), is(dto.getContractType()));
        assertThat(actual.getAssessmentFeeFlag(), is(dto.getAssessmentFeeFlag()));
        assertThat(actual.getContractName(), is(dto.getContractName()));
        assertThat(actual.getContractPriceProfileId(), is(dto.getContractPriceProfileId()));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getPriceAuditFlag(), is(dto.getPriceAuditFlag()));
        assertThat(actual.getPriceVerificationFlag(), is(dto.getPriceVerificationFlag()));
        assertThat(actual.getPricingEffectiveDate(), is(dto.getPricingEffectiveDate()));
        assertThat(actual.getPricingExpirationDate(), is(dto.getPricingExpirationDate()));
        assertThat(actual.getScheduleForCostChange(), is(dto.getScheduleForCostChange()));
        assertThat(actual.getTransferFeeFlag(), is(dto.getTransferFeeFlag()));
        assertThat(actual.getAgreementId(), is(dto.getAgreementId()));
        assertThat(actual.getExpireLowerLevelInd(), is(dto.getExpireLowerLevelInd()));
        assertThat(actual.getPricingExhibitSysId(), is(dto.getPricingExhibitSysId()));
        assertThat(actual.getClmContractStartDate(), is(dto.getClmContractStartDate()));
        assertThat(actual.getClmContractEndDate(), is(dto.getClmContractEndDate()));
        assertThat(actual.getParentAgreementId(), is(dto.getParentAgreementId()));
        assertThat(actual.getVersionNbr(), is(dto.getVersionNbr()));
    }
}
