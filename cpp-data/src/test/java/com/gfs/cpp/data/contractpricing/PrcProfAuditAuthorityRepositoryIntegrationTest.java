package com.gfs.cpp.data.contractpricing;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfAuditAuthorityDTO;
import com.gfs.cpp.common.model.auditauthority.PrcProfAuditAuthorityDO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class PrcProfAuditAuthorityRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private PrcProfAuditAuthorityRepository target;

    private static final String USER_NAME = "test user";

    @Test
    @DatabaseSetup(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.xml")
    public void shouldFetchPriceAudit() throws Exception {
        int contractPriceProfileSeq = -2001;
        String gfsCustomerId = "-1";
        int customerTypeCode = 32;

        int actual = target.fetchPriceAuditIndicator(contractPriceProfileSeq, gfsCustomerId, customerTypeCode);

        assertThat(actual, equalTo(1));
    }

    @ExpectedDatabase(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.shouldDeletePriceAudit-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.shouldDeletePriceAudit.xml")
    @Test
    public void shouldDeletePriceProfAudit() throws Exception {

        int contractPriceProfileSeq = -2001;

        target.deletePriceProfAudit(contractPriceProfileSeq);
    }

    @ExpectedDatabase(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.shouldInsertPriceProfileAudit.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertPriceProfileAudit() throws Exception {
        ContractPricingDO contractPricingDO = new ContractPricingDO();
        int contractPriceProfileSeq = -101;
        contractPricingDO.setGfsCustomerId("-1001");
        contractPricingDO.setContractPriceProfileId(-1001);
        contractPricingDO.setEffectiveDateFuture(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setContractTypeCode("DAN");
        contractPricingDO.setExpirationDateFuture(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setCustomerTypeCode(31);

        target.savePriceProfileAuditIndicator(USER_NAME, contractPriceProfileSeq, contractPricingDO, CPPConstants.INDICATOR_ONE);
    }

    @ExpectedDatabase(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.shouldExpirePriceProfileAuditForContract-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.shouldExpirePriceProfileAuditForContract.xml")
    @Test
    public void shouldExpirePriceProfileAuditForContract() throws Exception {

        target.expireNonCmgPriceProfAuditForContract(-3001, new LocalDate(2018, 04, 16).toDate(), "updated user");
    }

    @Test
    @DatabaseSetup(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.xml")
    public void shouldExpirePriceProfAuditForRealCust() throws Exception {
        Date expirationDate = new LocalDate(2018, 04, 16).toDate();
        String updatedUserId = "test";
        String customerId = "-1";
        int customerTypeCode = 32;
        Date newEffectiveDate = new LocalDate(2018, 04, 17).toDate();
        Date newExpiryDate = new LocalDate(2018, 04, 30).toDate();

        CMGCustomerResponseDTO contractLevelConcept = new CMGCustomerResponseDTO();
        contractLevelConcept.setDefaultCustomerInd(1);
        contractLevelConcept.setId("-1");
        contractLevelConcept.setTypeCode(32);
        target.expirePriceProfAuditForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode, newEffectiveDate, newExpiryDate);

    }

    @Test
    @DatabaseSetup(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.shouldFetchPrcProfAuditAuthorityForCPPSeq.xml")
    public void shouldFetchPrcProfAuditAuthorityForCPPSeq() throws Exception {
        int contractPriceProfileSeq = -2001;
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("-1");
        cmgCustomerResponseDTO.setTypeCode(32);
        
        List<PrcProfAuditAuthorityDTO> actual = target.fetchPrcProfAuditAuthorityForCPPSeq(contractPriceProfileSeq,cmgCustomerResponseDTO);
        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getGfsCustomerId(), equalTo("-1"));
    }

    @Test
    @ExpectedDatabase(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.shouldSavePriceProfileAuditForCustomer.xml", assertionMode = NON_STRICT_UNORDERED)
    public void shouldSavePriceProfileAuditForCustomer() throws Exception {
        List<PrcProfAuditAuthorityDO> prcProfAuditAuthorityDOList = new ArrayList<PrcProfAuditAuthorityDO>();
        PrcProfAuditAuthorityDO prcProfAuditAuthorityDO = new PrcProfAuditAuthorityDO();
        prcProfAuditAuthorityDO.setContractPriceProfileSeq(-1001);
        prcProfAuditAuthorityDO.setCreateUserId(CPPConstants.DUMMY_USER);
        prcProfAuditAuthorityDO.setEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        prcProfAuditAuthorityDO.setExpirationDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        prcProfAuditAuthorityDO.setGfsCustomerId("-1001");
        prcProfAuditAuthorityDO.setGfsCustomerTypeCode(24);
        prcProfAuditAuthorityDO.setLastUpdateUserId(CPPConstants.DUMMY_USER);
        prcProfAuditAuthorityDO.setPrcProfAuditAuthorityInd(CPPConstants.INDICATOR_ONE);
        prcProfAuditAuthorityDOList.add(prcProfAuditAuthorityDO);

        target.savePriceProfileAuditForCustomer(prcProfAuditAuthorityDOList);

    }

    @Test
    @DatabaseSetup(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.xml")
    @ExpectedDatabase(value = "PrcProfAuditAuthorityRepositoryIntegrationTest.shouldUpdatePriceProfileAuditIndicator-results.xml", assertionMode = NON_STRICT_UNORDERED)
    public void shouldUpdatePriceProfileAuditIndicator() throws Exception {
        int contractPriceProfileSeq = -2001;
        String userName = "updated user";
        int auditAuthorityIndicator = 0;

        target.updatePriceProfileAuditIndicator(userName, contractPriceProfileSeq, auditAuthorityIndicator);

    }

}
