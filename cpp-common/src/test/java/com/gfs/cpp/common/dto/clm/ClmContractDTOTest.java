package com.gfs.cpp.common.dto.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;

@RunWith(MockitoJUnitRunner.class)
public class ClmContractDTOTest {

    private static final String FUTURE_DATE = "01/01/9999";

    private static final String DATE_FORMAT = "MM/dd/YYYY";

    @InjectMocks
    private ClmContractDTO target;

    @Test
    public void validateClmContractDTO() throws Exception {
        String agreementId = "4e4321ca-3d4d-40d5-a3c4-20b56ba1a308";
        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileId(1);
        cppInformationDTO.setContractPriceProfileId(1);
        cppInformationDTO.setVersionNumber(1);
        target.setAgreementId(agreementId);
        target.setIsAmendment(true);
        target.setContractEndDate(new SimpleDateFormat(DATE_FORMAT).parse(FUTURE_DATE));
        target.setContractName("Test");
        target.setContractStartDate(new SimpleDateFormat(DATE_FORMAT).parse(FUTURE_DATE));
        target.setContractStatus("Draft");
        target.setContractType("ICMDistributionAgreementRegional");
        target.setParentAgreementId(agreementId);
        target.setAutoRenewalEnabled(true);
        target.setCppInformationDto(cppInformationDTO);
        target.setIsAmendment(true);

        ClmContractDTO actual = SerializationUtils.clone(target);

        assertThat(actual, equalTo(target));
        assertThat(actual.hashCode(), equalTo(target.hashCode()));
        assertThat(actual.toString(), notNullValue());

        assertThat(actual.getAgreementId(), is(target.getAgreementId()));
        assertThat(actual.getContractEndDate(), is(target.getContractEndDate()));
        assertThat(actual.getContractName(), is(target.getContractName()));
        assertThat(actual.getContractStartDate(), is(target.getContractStartDate()));
        assertThat(actual.getContractStatus(), is(target.getContractStatus()));
        assertThat(actual.getContractType(), is(target.getContractType()));
        assertThat(actual.getParentAgreementId(), is(target.getParentAgreementId()));
        assertThat(actual.getIsAutoRenewalEnabled(), is(target.getIsAutoRenewalEnabled()));
        assertThat(actual.getIsAmendment(), is(target.getIsAmendment()));
        assertThat(actual.getCppInformationDto(), is(target.getCppInformationDto()));

    }

}
