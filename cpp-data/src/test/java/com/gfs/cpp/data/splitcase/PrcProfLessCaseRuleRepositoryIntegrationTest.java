package com.gfs.cpp.data.splitcase;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hamcrest.CoreMatchers;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class PrcProfLessCaseRuleRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private PrcProfLessCaseRuleRepository target;

    @ExpectedDatabase(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldSaveSplitCase.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldSaveSplitCase() throws Exception {
        List<SplitCaseDO> splitCaseList = new ArrayList<>();
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        splitCaseDO.setContractPriceProfileSeq(-1001);
        splitCaseDO.setProductType("test");
        splitCaseDO.setSplitCaseFee(20);
        splitCaseDO.setUnit("%");
        splitCaseDO.setLessCaseRuleId(2);
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setItemPriceId("1");
        splitCaseList.add(splitCaseDO);
        String gfsCustomerId = "-10001234";
        String userName = "test user";

        target.saveSplitCase(splitCaseList, gfsCustomerId, userName);
    }

    @DatabaseSetup(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchSplitCaseGrid() throws Exception {
        int contractPriceProfileSeq = -1001;

        List<SplitCaseDO> actual = target.fetchSplitCaseGridForCMG(contractPriceProfileSeq);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getEffectiveDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE)));
        assertThat(actual.get(0).getExpirationDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE)));
        assertThat(actual.get(0).getItemPriceId(), equalTo("-1"));
        assertThat(actual.get(0).getSplitCaseFee(), equalTo(20.0));
        assertThat(actual.get(0).getUnit(), equalTo("%"));
    }

    @DatabaseSetup(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldExpireLessCaseRuleForContract.xml")
    @ExpectedDatabase(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldExpireLessCaseRuleForContract-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldExpireLessCaseRuleForContract() throws Exception {

        target.expireNonCmgLessCaseRuleForContract(-3001, new LocalDate(2018, 04, 15).toDate(), "updated user");

    }

    @DatabaseSetup(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldExpireLessCaseRuleForRealCust.xml")
    @ExpectedDatabase(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldExpireLessCaseRuleForRealCust-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldExpireLessCaseRuleForRealCust() throws Exception {
        String customerId = "-10001234";
        int customerTypeCode = 31;
        Date oldExpirydate = new LocalDate(2018, 04, 9).toDate();
        Date newEffectiveDate = new LocalDate(2018, 04, 10).toDate();
        Date newExpirationDate = new LocalDate(2018, 04, 20).toDate();

        target.expireLessCaseRuleForRealCust(oldExpirydate, "updated user", customerId, customerTypeCode, newEffectiveDate, newExpirationDate);
    }

    @Test
    @DatabaseSetup(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldFetchPrcProfLessCaseRuleForCPPSeq.xml")
    public void shouldFetchPrcProfLessCaseRuleForCPPSeq() throws Exception {
        int contractPriceProfileSeq = -3001;
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("-1");
        cmgCustomerResponseDTO.setTypeCode(31);

        List<PrcProfLessCaseRuleDO> actual = target.fetchPrcProfLessCaseRuleForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getCwMarkupAmnt(), equalTo(21));
        assertThat(actual.get(0).getCwMarkupAmountTypeCode(), equalTo("$"));
        assertThat(actual.get(0).getItemPriceId(), equalTo("1"));
        assertThat(actual.get(0).getItemPriceLevelCode(), equalTo(2));
        assertThat(actual.get(0).getLesscaseRuleId(), equalTo(2));
        assertThat(actual.get(0).getMarkupAppliedBeforeDivInd(), equalTo(0));
        assertThat(actual.get(0).getNonCwMarkupAmnt(), equalTo(20));
        assertThat(actual.get(0).getNonCwMarkupAmntTypeCode(), equalTo("%"));
    }

    @ExpectedDatabase(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldSavePrcProfLessCaseRuleForCustomer-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldSavePrcProfLessCaseRuleForCustomer() throws Exception {

        List<PrcProfLessCaseRuleDO> splitCaseList = new ArrayList<>();
        PrcProfLessCaseRuleDO splitCaseDO = new PrcProfLessCaseRuleDO();
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        splitCaseDO.setContractPriceProfileSeq(-1001);
        splitCaseDO.setCreateUserId("test user");
        splitCaseDO.setLastUpdateUserId("test user");
        splitCaseDO.setCwMarkupAmnt(21);
        splitCaseDO.setCwMarkupAmountTypeCode("$");
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setGfsCustomerId("-10001234");
        splitCaseDO.setGfsCustomerTypeCode(24);
        splitCaseDO.setItemPriceId("1");
        splitCaseDO.setItemPriceLevelCode(2);
        splitCaseDO.setLesscaseRuleId(2);
        splitCaseDO.setMarkupAppliedBeforeDivInd(0);
        splitCaseDO.setNonCwMarkupAmnt(20);
        splitCaseDO.setNonCwMarkupAmntTypeCode("%");
        splitCaseList.add(splitCaseDO);

        target.savePrcProfLessCaseRuleForCustomer(splitCaseList);
    }

    @DatabaseSetup(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.xml")
    @ExpectedDatabase(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldUpdateSplitCase-results.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldUpdateSplitCase() throws Exception {
        int contractPriceProfileSeq = -1001;
        String userName = "updated user";
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        List<SplitCaseDO> oldList = new ArrayList<>();
        SplitCaseDO oldSplitCaseDO = new SplitCaseDO();
        oldSplitCaseDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        oldSplitCaseDO.setEffectiveDate(effectiveDate);
        oldSplitCaseDO.setExpirationDate(expirationDate);
        oldSplitCaseDO.setGfsCustomerTypeCode(-31);
        oldSplitCaseDO.setItemPriceId("-1");
        oldSplitCaseDO.setProductType("Veg");
        oldSplitCaseDO.setSplitCaseFee(1);
        oldSplitCaseDO.setLessCaseRuleId(-1);
        oldSplitCaseDO.setUnit("%");
        oldList.add(oldSplitCaseDO);
        List<SplitCaseDO> splitCaseDOList = new ArrayList<>();
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        splitCaseDO.setProductType("Non Veg");
        splitCaseDO.setSplitCaseFee(30);
        splitCaseDO.setLessCaseRuleId(-2);
        splitCaseDO.setUnit("$");
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setItemPriceId("-1");
        splitCaseDO.setGfsCustomerTypeCode(-31);
        splitCaseDOList.add(splitCaseDO);

        Collection<SplitCaseDO> updatedSplitcaseList = CollectionUtils.subtract(splitCaseDOList, oldList);

        target.updateSplitCase(updatedSplitcaseList, userName, "-10001234", -31);

    }

    @Test
    @DatabaseSetup(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldFetchPrcProfLessCaseForRealCustomersForFurtherance.xml")
    public void shouldFetchPrcProfLessCaseForRealCustomersForFurtherance() throws Exception {
        int cppFurtheranceSeq = -1;
        Date furtheranceEffectiveDate = new LocalDate(2018, 01, 01).toDate();

        List<PrcProfLessCaseRuleDO> actual = target.fetchPrcProfLessCaseForRealCustomersForFurtherance(cppFurtheranceSeq, furtheranceEffectiveDate);

        assertThat(actual.size(), equalTo(2));

        for (PrcProfLessCaseRuleDO prcProfLessCaseRuleDO : actual) {
            assertThat(prcProfLessCaseRuleDO.getGfsCustomerId(), CoreMatchers.anyOf(CoreMatchers.is("-1212"), CoreMatchers.is("-1213")));
            assertThat(prcProfLessCaseRuleDO.getGfsCustomerTypeCode(), equalTo(36));
            assertThat(prcProfLessCaseRuleDO.getItemPriceId(), equalTo("1"));
            assertThat(prcProfLessCaseRuleDO.getItemPriceLevelCode(), equalTo(2));
        }
    }

    @ExpectedDatabase(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldSavePrcProfLessCaseRuleForCustomer-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldSavePrcProfLessCaseRuleForFurtheranceUpdates() throws Exception {

        List<PrcProfLessCaseRuleDO> splitCaseList = new ArrayList<>();
        String userName = "test user";
        PrcProfLessCaseRuleDO splitCaseDO = new PrcProfLessCaseRuleDO();
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        splitCaseDO.setContractPriceProfileSeq(-1001);
        splitCaseDO.setCreateUserId("check user");
        splitCaseDO.setLastUpdateUserId("check user");
        splitCaseDO.setCwMarkupAmnt(21);
        splitCaseDO.setCwMarkupAmountTypeCode("$");
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setGfsCustomerId("-10001234");
        splitCaseDO.setGfsCustomerTypeCode(24);
        splitCaseDO.setItemPriceId("1");
        splitCaseDO.setItemPriceLevelCode(2);
        splitCaseDO.setLesscaseRuleId(2);
        splitCaseDO.setMarkupAppliedBeforeDivInd(0);
        splitCaseDO.setNonCwMarkupAmnt(20);
        splitCaseDO.setNonCwMarkupAmntTypeCode("%");
        splitCaseList.add(splitCaseDO);

        target.savePrcProfLessCaseRuleForFurtheranceUpdates(splitCaseList, userName);
    }

    @DatabaseSetup(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldExpirePrcProfLessCaseRuleForFurtherance.xml")
    @ExpectedDatabase(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldExpirePrcProfLessCaseRuleForFurtherance-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldExpirePrcProfLessCaseRuleForFurtherance() throws Exception {

        List<PrcProfLessCaseRuleDO> splitCaseList = new ArrayList<>();
        String userName = "test user";
        PrcProfLessCaseRuleDO splitCaseDO = new PrcProfLessCaseRuleDO();
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        splitCaseDO.setContractPriceProfileSeq(-3001);
        splitCaseDO.setCreateUserId("check user");
        splitCaseDO.setLastUpdateUserId("check user");
        splitCaseDO.setCwMarkupAmnt(21);
        splitCaseDO.setCwMarkupAmountTypeCode("$");
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setGfsCustomerId("-10001234");
        splitCaseDO.setGfsCustomerTypeCode(24);
        splitCaseDO.setItemPriceId("4");
        splitCaseDO.setItemPriceLevelCode(2);
        splitCaseDO.setLesscaseRuleId(2);
        splitCaseDO.setMarkupAppliedBeforeDivInd(0);
        splitCaseDO.setNonCwMarkupAmnt(20);
        splitCaseDO.setNonCwMarkupAmntTypeCode("%");
        splitCaseList.add(splitCaseDO);
        target.expirePrcProfLessCaseRuleForFurtherance(splitCaseList, new LocalDate(2018, 04, 15).toDate(), userName);

    }

    @DatabaseSetup(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.xml")
    @ExpectedDatabase(value = "PrcProfLessCaseRuleRepositoryIntegrationTest.shouldExpireSplitCaseForFurtherance-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldExpireSplitcaseForFurtherance() throws Exception {

        int contractPriceProfileSeq = -1001;
        String userName = "updated user";
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        List<SplitCaseDO> splitCaseDOList = new ArrayList<>();
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        splitCaseDO.setProductType("Veg");
        splitCaseDO.setLessCaseRuleId(-2);
        splitCaseDO.setUnit("$");
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setItemPriceId("-1");
        splitCaseDO.setGfsCustomerTypeCode(-31);
        splitCaseDOList.add(splitCaseDO);

        target.expirePrcProfLessCaseRuleForFurtherance(splitCaseDOList, userName, "-10001234", -31, new LocalDate(2018, 04, 15).toDate());

    }

}
