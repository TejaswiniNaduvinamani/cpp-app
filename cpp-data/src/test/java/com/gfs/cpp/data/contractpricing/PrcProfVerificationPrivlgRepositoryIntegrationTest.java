package com.gfs.cpp.data.contractpricing;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class PrcProfVerificationPrivlgRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    private static final String USER_NAME = "test user";

    @Autowired
    private PrcProfVerificationPrivlgRepository target;

    @ExpectedDatabase(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.shouldDeletePriceVerifyPrivileges-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.shouldDeletePriceVerifyPrivileges.xml")
    @Test
    public void shouldDeletePriceVerifyPrivileges() throws Exception {

        int contractPriceProfileSeq = -2001;

        target.deletePriceVerifyPrivileges(contractPriceProfileSeq);
    }

    @ExpectedDatabase(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.shouldInsertPriceVerifyPrivilages.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertPriceVerifyPrivileges() throws Exception {
        ContractPricingDO contractPricingDO = new ContractPricingDO();
        int contractPriceProfileSeq = -2001;
        contractPricingDO.setGfsCustomerId("-2001");
        contractPricingDO.setContractPriceProfileId(-2001);
        contractPricingDO.setPricingEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setContractTypeCode("DAN");
        contractPricingDO.setPricingExpirationDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setCustomerTypeCode(31);

        target.savePriceVerifyPrivileges(contractPriceProfileSeq, USER_NAME, contractPricingDO.getPricingEffectiveDate(),
                contractPricingDO.getPricingExpirationDate(), 1);
    }

    @ExpectedDatabase(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.shouldExpirePriceProfileVerificationPrivlg-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.shouldExpirePriceProfileVerificationPrivlg.xml")
    @Test
    public void shouldExpirePriceProfileVerificationPrivlg() throws Exception {

        target.expirePriceProfileVerficationPrivlg(-3001, new LocalDate(2018, 04, 15).toDate(), "updated user");

    }

    @ExpectedDatabase(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.shouldUpdateEffectiveDatePriceProfileVerificationPrivlg-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.shouldUpdateEffectiveDatePriceProfileVerificationPrivlg.xml")
    @Test
    public void shouldUpdateEffectiveDatePriceProfileVerificationPrivlg() throws Exception {

        target.updatePriceProfileVerficationPrivlgEffectiveDate(-3001, new LocalDate(2018, 04, 15).toDate(), "updated user");

    }

    @ExpectedDatabase(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.shouldUpdatePriceProfileVerficationPrivlgInd-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.shouldUpdatePriceProfileVerficationPrivlgInd.xml")
    @Test
    public void shouldUpdatePriceProfileVerficationPrivlgInd() throws Exception {
        ContractPricingDO contractPricingDO = new ContractPricingDO();
        contractPricingDO.setContractPriceProfileSeq(-3001);
        contractPricingDO.setPricingEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setPricingExpirationDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));

        target.updatePriceProfileVerficationPrivlgInd(contractPricingDO, CPPConstants.INDICATOR_ZERO, "updated user");
    }

    @Test
    @DatabaseSetup(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.shouldFetchPriceVerf.xml")
    public void shouldFetchPrcProfPricePriviligeIndicatorForCPPSeq() throws Exception {
        int contractPriceProfileSeq = -2001;

        int actual = target.fetchPricePriviligeIndicator(contractPriceProfileSeq);

        assertThat(actual, equalTo(1));
    }

    @ExpectedDatabase(value = "PrcProfVerificationPrivlgRepositoryIntegrationTest.savePriceVerifyPrivilegesForCPPSeq.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void savePriceVerifyPrivilegesForCPPSeq() throws Exception {
        int contractPriceProfileSeq = -2001;
        Date effectiveDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");
        Date expirationDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");
        int prcProfPricePrevInd = 1;
        target.savePriceVerifyPrivileges(contractPriceProfileSeq, USER_NAME, effectiveDate, expirationDate, prcProfPricePrevInd);
    }
}
