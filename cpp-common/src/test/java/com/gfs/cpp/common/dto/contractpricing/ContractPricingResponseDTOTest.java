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

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;

@RunWith(MockitoJUnitRunner.class)
public class ContractPricingResponseDTOTest {

    @InjectMocks
    private ContractPricingResponseDTO dto;

    @Test
    public void testMethods() {
        dto.setClmContractTypeSeq(1);
        dto.setPricingEffectiveDate(new LocalDate(2017, 12, 30).toDate());
        dto.setPricingExpirationDate(new LocalDate(2017, 12, 30).toDate());
        dto.setContractPriceProfStatusCode(10);
        dto.setClmAgreementId("agreementId");
        dto.setContractPriceProfileSeq(201);
        dto.setPricingExhibitSysId("sysId");
        dto.setContractPriceProfileId(-1);
        dto.setExpireLowerLevelInd(1);
        dto.setClmParentAgreementId("parentagreementid");
        Date clmContractStartDate = new LocalDate(2018, 05, 24).toDate();
        Date clmContractEndDate = new LocalDate(2019, 05, 24).toDate();
        dto.setClmContractStartDate(clmContractStartDate);
        dto.setClmContractEndDate(clmContractEndDate);
        dto.setVersionNumber(0);
        dto.setClmContractName("contractName");

        final ContractPricingResponseDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getClmContractTypeSeq(), is(1));
        assertThat(actual.getPricingEffectiveDate(), is(new LocalDate(2017, 12, 30).toDate()));
        assertThat(actual.getPricingExpirationDate(), is(new LocalDate(2017, 12, 30).toDate()));
        assertThat(actual.getContractPriceProfStatusCode(), is(10));
        assertThat(actual.getClmAgreementId(), is("agreementId"));
        assertThat(actual.getContractPriceProfileSeq(), is(201));
        assertThat(actual.getPricingExhibitSysId(), is("sysId"));
        assertThat(actual.getContractPriceProfileId(), is(-1));
        assertThat(actual.getExpireLowerLevelInd(), is(1));
        assertThat(actual.getClmParentAgreementId(), is("parentagreementid"));
        assertThat(actual.getClmContractStartDate(), is(clmContractStartDate));
        assertThat(actual.getClmContractEndDate(), is(clmContractEndDate));
        assertThat(actual.getVersionNumber(), is(dto.getVersionNumber()));
        assertThat(actual.getClmContractName(), is(dto.getClmContractName()));
    }

}
