package com.gfs.cpp.data.markup;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
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
import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;
import com.gfs.cpp.common.dto.markup.PrcProfPricingRuleOvrdDTO;
import com.gfs.cpp.common.model.markup.PrcProfPricingRuleOvrdDO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class PrcProfPricingRuleOvrdRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    private static final String USER_NAME = "test user";
    private static final Date pricingEffectiveDate = new LocalDate(2018, 01, 02).toDate();
    private static final Date pricingExpirationDate = new LocalDate(2019, 01, 02).toDate();

    @Autowired
    private PrcProfPricingRuleOvrdRepository target;

    @ExpectedDatabase(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldInsertPricingProfileRuleOvrdWithIndOne-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertPricingProfileRuleOvrdWithIndOne() throws Exception {
        MarkupRequestDTO markupRequest = new MarkupRequestDTO();
        markupRequest.setContractPriceProfileSeq(-101);
        markupRequest.setEffectiveDate(pricingEffectiveDate);
        markupRequest.setExpirationDate(pricingExpirationDate);
        markupRequest.setMarkupOnSell(true);
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("-10000012");
        cmgCustomerResponseDTO.setTypeCode(36);

        target.saveMarkupOnSellIndicator(markupRequest, cmgCustomerResponseDTO.getId(), cmgCustomerResponseDTO.getTypeCode(), USER_NAME,
                effectiveDate, expirationDate);
    }

    @ExpectedDatabase(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldInsertPricingProfileRuleOvrdWithIndZero-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertPricingProfileRuleOvrdWithIndZero() throws Exception {
        MarkupRequestDTO markupRequest = new MarkupRequestDTO();
        markupRequest.setContractPriceProfileSeq(-201);
        markupRequest.setEffectiveDate(pricingEffectiveDate);
        markupRequest.setExpirationDate(pricingExpirationDate);
        markupRequest.setMarkupOnSell(false);
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("-10000012");
        cmgCustomerResponseDTO.setTypeCode(36);

        target.saveMarkupOnSellIndicator(markupRequest, cmgCustomerResponseDTO.getId(), cmgCustomerResponseDTO.getTypeCode(), USER_NAME,
                effectiveDate, expirationDate);
    }

    @ExpectedDatabase(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldUpdatePricingProfileRuleOvrdWithIndOne-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldUpdatePricingProfileRuleOvrdWithIndOne.xml")
    @Test
    public void shouldUpdatePricingProfileRuleOvrdWithIndOne() throws Exception {
        MarkupRequestDTO markupRequest = new MarkupRequestDTO();
        markupRequest.setContractPriceProfileSeq(-101);
        markupRequest.setEffectiveDate(pricingEffectiveDate);
        markupRequest.setExpirationDate(pricingExpirationDate);
        markupRequest.setMarkupOnSell(true);
        Date effectiveDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");
        Date expirationDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("-10000012");
        cmgCustomerResponseDTO.setTypeCode(36);

        target.updateMarkupOnSellIndicator(markupRequest, cmgCustomerResponseDTO.getId(), cmgCustomerResponseDTO.getTypeCode(), USER_NAME,
                effectiveDate, expirationDate);
    }

    @ExpectedDatabase(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldUpdatePricingProfileRuleOvrdWithIndZero-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldUpdatePricingProfileRuleOvrdWithIndZero.xml")
    @Test
    public void shouldUpdatePricingProfileRuleOvrdWithIndZero() throws Exception {
        MarkupRequestDTO markupRequest = new MarkupRequestDTO();
        markupRequest.setContractPriceProfileSeq(-201);
        markupRequest.setEffectiveDate(pricingEffectiveDate);
        markupRequest.setExpirationDate(pricingExpirationDate);
        markupRequest.setMarkupOnSell(false);
        Date effectiveDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");
        Date expirationDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("-10000012");
        cmgCustomerResponseDTO.setTypeCode(36);

        target.updateMarkupOnSellIndicator(markupRequest, cmgCustomerResponseDTO.getId(), cmgCustomerResponseDTO.getTypeCode(), USER_NAME,
                effectiveDate, expirationDate);
    }

    @DatabaseSetup(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.xml")
    @Test
    public void shouldGetMarkupOnSellCount() throws Exception {
        int contractPriceProfileSeq = -101;
        int rowCount = target.fetchSellPriceCount(contractPriceProfileSeq);

        assertThat(rowCount, equalTo(1));
    }

    @DatabaseSetup(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchMarkupOnSell() throws Exception {
        int contractPriceProfileSeq = -101;

        int markupOnSellInd = target.fetchPrcProfOvrdInd(contractPriceProfileSeq);

        assertThat(markupOnSellInd, equalTo(1));
    }

    @DatabaseSetup(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchNullMarkupOnSell() throws Exception {
        int contractPriceProfileSeq = -102;

        Integer markupOnSellInd = target.fetchPrcProfOvrdInd(contractPriceProfileSeq);

        assertThat(markupOnSellInd, nullValue());
    }

    @ExpectedDatabase(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldExpirePriceProfRuleOverdForRealCust-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldExpirePriceProfRuleOverdForRealCust.xml")
    @Test
    public void shouldExpirePriceProfRuleOverdForRealCust() throws Exception {
        Date expirationDateForExistingPricing = new LocalDate(2018, 04, 04).toDate();
        Date newEeffectiveDate = new LocalDate(2018, 04, 05).toDate();
        Date newExpirationDate = new LocalDate(2018, 04, 15).toDate();
        String customerId = "-10000012";
        int customerTypeCode = 36;
        target.expirePriceProfRuleOverdForRealCust(expirationDateForExistingPricing, "updated user", customerId, customerTypeCode, newEeffectiveDate,
                newExpirationDate);
    }

    @ExpectedDatabase(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldExpirePricingOvrdForContract-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldExpirePricingOvrdForContract.xml")
    @Test
    public void shouldExpirePriceProfRuleOverdForContract() throws Exception {
        int cppSeq = -3001;
        Date expirationDate = new LocalDate(2018, 04, 15).toDate();
        String updatedUserId = "updated user";

        target.expireNonCmgPriceProfRuleOverdForContract(cppSeq, expirationDate, updatedUserId);

    }

    @Test
    @DatabaseSetup(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldFetchPrcProfPricingRuleOvrdForCPPSeq.xml")
    public void shouldFetchPrcProfPricingRuleOvrdForCPPSeq() throws Exception {
        int contractPriceProfileSeq = -3001;
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("-1");
        cmgCustomerResponseDTO.setTypeCode(31);

        List<PrcProfPricingRuleOvrdDTO> actual = target.fetchPrcProfPricingRuleOvrdForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getPricingOverrideId(), equalTo(7));
        assertThat(actual.get(0).getPricingOverrideInd(), equalTo(0));
    }

    @Test
    @ExpectedDatabase(value = "PrcProfPricingRuleOvrdRepositoryIntegrationTest.shouldSavePrcProfPricingRuleOvrdForCustomer-result.xml", assertionMode = NON_STRICT_UNORDERED)
    public void shouldSavePrcProfPricingRuleOvrdForCustomer() throws Exception {
        List<PrcProfPricingRuleOvrdDO> prcProfPricingRuleOvrdDOList = new ArrayList<PrcProfPricingRuleOvrdDO>();
        PrcProfPricingRuleOvrdDO prcProfPricingRuleOvrdDO = new PrcProfPricingRuleOvrdDO();
        prcProfPricingRuleOvrdDO.setContractPriceProfileSeq(-1001);
        prcProfPricingRuleOvrdDO.setEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        prcProfPricingRuleOvrdDO.setExpirationDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        prcProfPricingRuleOvrdDO.setGfsCustomerId("-10000012");
        prcProfPricingRuleOvrdDO.setGfsCustomerTypeCode(36);
        prcProfPricingRuleOvrdDO.setPricingOverrideId(7);
        prcProfPricingRuleOvrdDO.setPricingOverrideInd(1);
        prcProfPricingRuleOvrdDO.setLastUpdateUserId(CPPConstants.DUMMY_USER);
        prcProfPricingRuleOvrdDO.setCreateUserId(CPPConstants.DUMMY_USER);
        prcProfPricingRuleOvrdDOList.add(prcProfPricingRuleOvrdDO);

        target.savePrcProfPricingRuleOvrdForCustomer(prcProfPricingRuleOvrdDOList);

    }

}
