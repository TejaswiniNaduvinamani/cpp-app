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
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class PrcProfCostSchedulePkgRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private PrcProfCostSchedulePkgRepository target;

    private static final String USER_NAME = "test user";

    @ExpectedDatabase(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldDeleteCostScedulePackage-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldDeleteCostScedulePackage.xml")
    @Test
    public void shouldDeleteCostSchedulePackage() throws Exception {

        int contractPriceProfileSeq = -2001;

        target.deleteCostSchedulePackage(contractPriceProfileSeq);
    }

    @Test
    public void shouldFetchPrcProfileCostSchedulePkgSeq() throws Exception {

        int actual = target.fetchPrcProfileCostSchedulePkgNextSeq();

        assertThat(actual, equalTo(1000));
    }

    @ExpectedDatabase(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldInsertScheduleForCostChange-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertScheduleForCostChange() throws Exception {
        int costSchedulePkgSeq = -3001;
        ContractPricingDO contractPricingDO = new ContractPricingDO();
        int contractPriceProfileSeq = -3001;
        contractPricingDO.setGfsCustomerId("-3001");
        contractPricingDO.setContractPriceProfileId(-3001);
        contractPricingDO.setEffectiveDateFuture(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setContractTypeCode("DAN");
        contractPricingDO.setExpirationDateFuture(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setCustomerTypeCode(31);

        target.saveScheduleForCostChange(USER_NAME, contractPriceProfileSeq, contractPricingDO, costSchedulePkgSeq);
    }

    @ExpectedDatabase(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldExpireCostSchedulePackageForContract-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldExpireCostSchedulePackageForContract.xml")
    @Test
    public void shouldExpireCostSchedulePackageForContract() throws Exception {
        target.expireNonCmgCostSchedulePackageForContract(-3001, new LocalDate(2018, 04, 15).toDate(), "updated user");
    }

    @ExpectedDatabase(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldExpireCostSchedulePackageForRealCust-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldExpireCostSchedulePackageForRealCust.xml")
    @Test
    public void shouldExpireCostSchedulePackageForRealCust() throws Exception {
        Date newEffectiveDate = new LocalDate(2018, 04, 14).toDate();
        Date newExpirationDate = new LocalDate(2018, 04, 30).toDate();
        target.expireCostSchedulePackageForRealCust(new LocalDate(2018, 04, 13).toDate(), "updated user", "-1", 31, newEffectiveDate,
                newExpirationDate);
    }

    @Test
    @DatabaseSetup(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldFetchPrcProfCostScheduleForCPPSeq.xml")
    public void shouldFetchPrcProfCostScheduleForCPPSeq() throws Exception {
        int contractPriceProfileSeq = -2001;
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("-1");
        cmgCustomerResponseDTO.setTypeCode(31);

        List<PrcProfCostSchedulePkgDO> actual = target.fetchPrcProfCostScheduleForCPPSeq(contractPriceProfileSeq,cmgCustomerResponseDTO);
        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getPrcProfCostSchedulePkgSeq(), equalTo(-201));
        assertThat(actual.get(0).getGfsCustomerId(), equalTo("-1"));
        assertThat(actual.get(0).getGfsCustomerTypeCode(), equalTo(31));
        
    }

    @Test
    @ExpectedDatabase(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldSavePrcProfCostScheduleForCustomer.xml", assertionMode = NON_STRICT_UNORDERED)
    public void shouldSavePrcProfCostScheduleForCustomer() throws Exception {
        List<PrcProfCostSchedulePkgDO> prcProfCostSchedulePkgDOList = new ArrayList<PrcProfCostSchedulePkgDO>();
        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO = new PrcProfCostSchedulePkgDO();
        prcProfCostSchedulePkgDO.setContractPriceProfileSeq(-3001);
        prcProfCostSchedulePkgDO.setCreateUserId(CPPConstants.DUMMY_USER);
        prcProfCostSchedulePkgDO.setEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/2016"));
        prcProfCostSchedulePkgDO.setExpirationDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/2019"));
        prcProfCostSchedulePkgDO.setGfsCustomerId("-1001");
        prcProfCostSchedulePkgDO.setGfsCustomerTypeCode(24);
        prcProfCostSchedulePkgDO.setLastUpdateUserId(CPPConstants.DUMMY_USER);
        prcProfCostSchedulePkgDO.setPrcProfCostSchedulePkgSeq(-201);
        prcProfCostSchedulePkgDOList.add(prcProfCostSchedulePkgDO);

        target.savePrcProfCostScheduleForCustomer(prcProfCostSchedulePkgDOList);

    }

    @ExpectedDatabase(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldUpdateLastUpdateUserId-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfCostSchedulePkgRepositoryIntegrationTest.shouldUpdateLastUpdateUserId.xml")
    @Test
    public void shouldUpdateLastUpdateUserId() throws Exception {
        target.updateLastUpdateUserId("updated user", -3001);
    }

}
