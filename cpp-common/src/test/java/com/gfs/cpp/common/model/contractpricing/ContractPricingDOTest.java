package com.gfs.cpp.common.model.contractpricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;

@RunWith(MockitoJUnitRunner.class)
public class ContractPricingDOTest {

    @InjectMocks
    private ContractPricingDO contractPricingDO;

    @Test
    public void testContractPricingDO() {

        contractPricingDO.setContractName("New Test Contract");
        contractPricingDO.setContractPriceProfileId(1);
        contractPricingDO.setContractTypeCode("12");
        contractPricingDO.setCostModelGFSAssesFee(true);
        contractPricingDO.setCostModelTransferFee(true);
        contractPricingDO.setGfsCustomerId("15");
        contractPricingDO.setLabelAssesmentInd(1);
        contractPricingDO.setPriceAuditInd(1);
        contractPricingDO.setPriceAuditPrivileges(true);
        contractPricingDO.setPricingEffectiveDate(new LocalDate(2017, 01, 01).toDate());
        contractPricingDO.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingDO.setEffectiveDateFuture(new LocalDate(9999, 01, 01).toDate());
        contractPricingDO.setExpirationDateFuture(new LocalDate(9999, 01, 01).toDate());
        contractPricingDO.setScheduleForCostChange("Fiscal");
        contractPricingDO.setTransferFeeInd(1);
        contractPricingDO.setPriceVerifyPrivileges(true);
        contractPricingDO.setPriceVerifInd(1);
        contractPricingDO.setCustomerTypeCode(1);
        contractPricingDO.setContractPriceProfileSeq(100);
        contractPricingDO.setAgreementId("agreement id");
        contractPricingDO.setExpireLowerLevelInd(1);
        Date clmContractStartDate = new LocalDate(2018, 05, 24).toDate();
        Date clmContractEndDate = new LocalDate(2019, 05, 24).toDate();
        contractPricingDO.setClmContractStartDate(clmContractStartDate);
        contractPricingDO.setParentAgreementId("parent agreement Id");
        contractPricingDO.setClmContractEndDate(clmContractEndDate);
        contractPricingDO.setVersionNbr(0);

        final ContractPricingDO actual = SerializationUtils.clone(contractPricingDO);

        assertThat(contractPricingDO.equals(actual), is(true));
        assertThat(contractPricingDO.hashCode(), is(actual.hashCode()));
        assertThat(contractPricingDO.toString() != null, is(true));

        assertThat(actual.getContractName(), is(contractPricingDO.getContractName()));
        assertThat(actual.getContractPriceProfileId(), is(contractPricingDO.getContractPriceProfileId()));
        assertThat(actual.getContractTypeCode(), is(contractPricingDO.getContractTypeCode()));
        assertThat(actual.getCostModelGFSAssesFee(), is(contractPricingDO.getCostModelGFSAssesFee()));
        assertThat(actual.getCostModelTransferFee(), is(contractPricingDO.getCostModelTransferFee()));
        assertThat(actual.getGfsCustomerId(), is(contractPricingDO.getGfsCustomerId()));
        assertThat(actual.getLabelAssesmentInd(), is(contractPricingDO.getLabelAssesmentInd()));
        assertThat(actual.getPriceAuditInd(), is(contractPricingDO.getPriceAuditInd()));
        assertThat(actual.getPriceAuditPrivileges(), is(contractPricingDO.getPriceAuditPrivileges()));
        assertThat(actual.getPricingEffectiveDate(), is(contractPricingDO.getPricingEffectiveDate()));
        assertThat(actual.getPricingExpirationDate(), is(contractPricingDO.getPricingExpirationDate()));
        assertThat(actual.getScheduleForCostChange(), is(contractPricingDO.getScheduleForCostChange()));
        assertThat(actual.getTransferFeeInd(), is(contractPricingDO.getTransferFeeInd()));
        assertThat(actual.getPriceVerifyPrivileges(), is(contractPricingDO.getPriceVerifyPrivileges()));
        assertThat(actual.getPriceVerifInd(), is(contractPricingDO.getPriceVerifInd()));
        assertThat(actual.getCustomerTypeCode(), is(contractPricingDO.getCustomerTypeCode()));
        assertThat(actual.getEffectiveDateFuture(), is(contractPricingDO.getEffectiveDateFuture()));
        assertThat(actual.getExpirationDateFuture(), is(contractPricingDO.getExpirationDateFuture()));
        assertThat(actual.getContractPriceProfileSeq(), is(contractPricingDO.getContractPriceProfileSeq()));
        assertThat(actual.getAgreementId(), is(contractPricingDO.getAgreementId()));
        assertThat(actual.getExpireLowerLevelInd(), is(contractPricingDO.getExpireLowerLevelInd()));
        assertThat(actual.getClmContractStartDate(), is(contractPricingDO.getClmContractStartDate()));
        assertThat(actual.getClmContractEndDate(), is(contractPricingDO.getClmContractEndDate()));
        assertThat(actual.getParentAgreementId(), is(contractPricingDO.getParentAgreementId()));
        assertThat(actual.getVersionNbr(), is(contractPricingDO.getVersionNbr()));

    }
}
