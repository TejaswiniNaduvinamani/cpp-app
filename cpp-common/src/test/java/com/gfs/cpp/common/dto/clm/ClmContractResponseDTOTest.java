package com.gfs.cpp.common.dto.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;

public class ClmContractResponseDTOTest {

    private ClmContractResponseDTO target = new ClmContractResponseDTO();

    @Test
    public void validateClmContractResponseDTO() throws Exception {

        Date startDate = new LocalDate(2019, 1, 1).toDate();
        Date endDate = new LocalDate(2019, 12, 31).toDate();
        Date amendmentEffectiveDate = new LocalDate(2019, 6, 30).toDate();
        String contractAgreementId = "4e4321ca-3d4d-40d5-a3c4-20b56ba1a308";
        target.setContractAgreementId(contractAgreementId);
        target.setContractEffectiveDate(startDate);
        target.setContractExpirationDate(endDate);
        target.setAmendmentEffectiveDate(amendmentEffectiveDate);
        String contractType = "ICMDistributionAgreementRegional";
        target.setContractTypeName(contractType);
        String contractStatus = "Draft";
        target.setContractStatus(contractStatus);
        String contractName = "Test Contract";
        target.setContractName(contractName);
        String parentAgreementId = "4e4321a-3d4d-40d5-a3c4-20b56ba1a308";
        target.setParentAgreementId(parentAgreementId);
        target.setParentContractTypeName("Test-Contract");
        target.setAmendment(true);

        ClmContractResponseDTO actual = SerializationUtils.clone(target);

        assertThat(actual, equalTo(target));
        assertThat(actual.hashCode(), equalTo(target.hashCode()));
        assertThat(actual.toString(), notNullValue());

        assertThat(actual.getContractAgreementId(), equalTo(contractAgreementId));
        assertThat(actual.getContractName(), equalTo(contractName));
        assertThat(actual.getContractTypeName(), equalTo(contractType));
        assertThat(actual.getContractStatus(), equalTo(contractStatus));
        assertThat(actual.getContractEffectiveDate(), equalTo(startDate));
        assertThat(actual.getContractExpirationDate(), equalTo(endDate));
        assertThat(actual.isAmendment(), equalTo(true));
        assertThat(actual.getParentAgreementId(), equalTo(parentAgreementId));
        assertThat(actual.getAmendmentEffectiveDate(), equalTo(amendmentEffectiveDate));

    }

}
