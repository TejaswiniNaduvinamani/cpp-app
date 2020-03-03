package com.gfs.cpp.component.activatepricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.PrcProfLessCaseRuleDOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfLessCaseRuleDOBuilderTest {

    @InjectMocks
    private PrcProfLessCaseRuleDOBuilder target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldBuildPrcProfPricingRuleOvrdDO() {

        String userId = "Test";

        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("Customer ID");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(22);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);

        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 05).toDate());

        List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOListForCMG = new ArrayList<PrcProfLessCaseRuleDO>();
        PrcProfLessCaseRuleDO prcProfLessCaseRuleDO = new PrcProfLessCaseRuleDO();
        prcProfLessCaseRuleDO.setContractPriceProfileSeq(1);
        prcProfLessCaseRuleDO.setGfsCustomerId("1");
        prcProfLessCaseRuleDO.setCreateUserId("test user");
        prcProfLessCaseRuleDO.setEffectiveDate(new LocalDate(2018, 05, 05).toDate());
        prcProfLessCaseRuleDO.setExpirationDate(new LocalDate(2019, 05, 05).toDate());
        prcProfLessCaseRuleDO.setLastUpdateUserId("test user");
        prcProfLessCaseRuleDO.setCwMarkupAmnt(1);
        prcProfLessCaseRuleDO.setCwMarkupAmountTypeCode("12");
        prcProfLessCaseRuleDO.setGfsCustomerTypeCode(1);
        prcProfLessCaseRuleDO.setItemPriceId("1");
        prcProfLessCaseRuleDO.setItemPriceLevelCode(1);
        prcProfLessCaseRuleDO.setLesscaseRuleId(1);
        prcProfLessCaseRuleDO.setMarkupAppliedBeforeDivInd(1);
        prcProfLessCaseRuleDO.setNonCwMarkupAmnt(3);
        prcProfLessCaseRuleDO.setNonCwMarkupAmntTypeCode("12");
        prcProfLessCaseRuleDOListForCMG.add(prcProfLessCaseRuleDO);

        List<PrcProfLessCaseRuleDO> actual = target.buildPrcProfLessCaseRuleDOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfLessCaseRuleDOListForCMG);

        assertThat(prcProfLessCaseRuleDO.getContractPriceProfileSeq(), is(actual.get(0).getContractPriceProfileSeq()));
        assertThat(contractCustomerMappingDTO.getGfsCustomerId(), is(actual.get(0).getGfsCustomerId()));
        assertThat(contractCustomerMappingDTO.getGfsCustomerTypeCode(), is(actual.get(0).getGfsCustomerTypeCode()));
        assertThat(userId, is(actual.get(0).getCreateUserId()));
        assertThat((new LocalDate(2018, 01, 05).toDate()), is(actual.get(0).getEffectiveDate()));
        assertThat((new LocalDate(2019, 01, 05).toDate()), is(actual.get(0).getExpirationDate()));
        assertThat(userId, is(actual.get(0).getLastUpdateUserId()));
        assertThat(prcProfLessCaseRuleDO.getCwMarkupAmnt(), is(actual.get(0).getCwMarkupAmnt()));
        assertThat(prcProfLessCaseRuleDO.getCwMarkupAmountTypeCode(), is(actual.get(0).getCwMarkupAmountTypeCode()));
        assertThat(prcProfLessCaseRuleDO.getItemPriceId(), is(actual.get(0).getItemPriceId()));
        assertThat(prcProfLessCaseRuleDO.getItemPriceLevelCode(), is(actual.get(0).getItemPriceLevelCode()));
        assertThat(prcProfLessCaseRuleDO.getLesscaseRuleId(), is(actual.get(0).getLesscaseRuleId()));
        assertThat(prcProfLessCaseRuleDO.getMarkupAppliedBeforeDivInd(), is(actual.get(0).getMarkupAppliedBeforeDivInd()));
        assertThat(prcProfLessCaseRuleDO.getNonCwMarkupAmnt(), is(actual.get(0).getNonCwMarkupAmnt()));
        assertThat(prcProfLessCaseRuleDO.getNonCwMarkupAmntTypeCode(), is(actual.get(0).getNonCwMarkupAmntTypeCode()));

    }

    @Test
    public void shouldBuildPrcProfLessCaseRuleDOListForAmendment() {

        int contractPriceProfileSeq = 1;
        String userId = "Test";
        String customerId = "1";
        int customerTypeCode = 31;
        Date farOutDate = new Date();

        List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOListForCMG = new ArrayList<PrcProfLessCaseRuleDO>();
        PrcProfLessCaseRuleDO prcProfLessCaseRuleDO = new PrcProfLessCaseRuleDO();
        prcProfLessCaseRuleDO.setContractPriceProfileSeq(1);
        prcProfLessCaseRuleDO.setGfsCustomerId(customerId);
        prcProfLessCaseRuleDO.setCreateUserId("test user");
        prcProfLessCaseRuleDO.setEffectiveDate(new LocalDate(2018, 05, 05).toDate());
        prcProfLessCaseRuleDO.setExpirationDate(new LocalDate(2019, 05, 05).toDate());
        prcProfLessCaseRuleDO.setLastUpdateUserId("test user");
        prcProfLessCaseRuleDO.setCwMarkupAmnt(1);
        prcProfLessCaseRuleDO.setCwMarkupAmountTypeCode("12");
        prcProfLessCaseRuleDO.setGfsCustomerTypeCode(customerTypeCode);
        prcProfLessCaseRuleDO.setItemPriceId("1");
        prcProfLessCaseRuleDO.setItemPriceLevelCode(1);
        prcProfLessCaseRuleDO.setLesscaseRuleId(1);
        prcProfLessCaseRuleDO.setMarkupAppliedBeforeDivInd(1);
        prcProfLessCaseRuleDO.setNonCwMarkupAmnt(3);
        prcProfLessCaseRuleDO.setNonCwMarkupAmntTypeCode("12");
        prcProfLessCaseRuleDOListForCMG.add(prcProfLessCaseRuleDO);

        List<PrcProfLessCaseRuleDO> actual = target.buildPrcProfLessCaseRuleDOListForAmendment(contractPriceProfileSeq, userId, farOutDate,
                prcProfLessCaseRuleDOListForCMG);

        assertThat(prcProfLessCaseRuleDO.getContractPriceProfileSeq(), is(actual.get(0).getContractPriceProfileSeq()));
        assertThat(customerId, is(actual.get(0).getGfsCustomerId()));
        assertThat(customerTypeCode, is(actual.get(0).getGfsCustomerTypeCode()));
        assertThat(userId, is(actual.get(0).getCreateUserId()));
        assertThat((farOutDate), is(actual.get(0).getEffectiveDate()));
        assertThat(farOutDate, is(actual.get(0).getExpirationDate()));
        assertThat(userId, is(actual.get(0).getLastUpdateUserId()));
        assertThat(prcProfLessCaseRuleDO.getCwMarkupAmnt(), is(actual.get(0).getCwMarkupAmnt()));
        assertThat(prcProfLessCaseRuleDO.getCwMarkupAmountTypeCode(), is(actual.get(0).getCwMarkupAmountTypeCode()));
        assertThat(prcProfLessCaseRuleDO.getItemPriceId(), is(actual.get(0).getItemPriceId()));
        assertThat(prcProfLessCaseRuleDO.getItemPriceLevelCode(), is(actual.get(0).getItemPriceLevelCode()));
        assertThat(prcProfLessCaseRuleDO.getLesscaseRuleId(), is(actual.get(0).getLesscaseRuleId()));
        assertThat(prcProfLessCaseRuleDO.getMarkupAppliedBeforeDivInd(), is(actual.get(0).getMarkupAppliedBeforeDivInd()));
        assertThat(prcProfLessCaseRuleDO.getNonCwMarkupAmnt(), is(actual.get(0).getNonCwMarkupAmnt()));
        assertThat(prcProfLessCaseRuleDO.getNonCwMarkupAmntTypeCode(), is(actual.get(0).getNonCwMarkupAmntTypeCode()));

    }

}
