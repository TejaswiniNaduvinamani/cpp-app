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
import com.gfs.cpp.common.dto.markup.PrcProfPricingRuleOvrdDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.markup.PrcProfPricingRuleOvrdDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.PrcProfPricingRuleOvrdDOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfPricingRuleOvrdDOBuilderTest {

    @InjectMocks
    private PrcProfPricingRuleOvrdDOBuilder target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldBuildPrcProfPricingRuleOvrdDO() {
        int contractPriceProfileSeq = 1;
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

        List<PrcProfPricingRuleOvrdDTO> prcProfPricingRuleOvrdDTOList = new ArrayList<PrcProfPricingRuleOvrdDTO>();
        PrcProfPricingRuleOvrdDTO prcProfPricingRuleOvrdDTO = new PrcProfPricingRuleOvrdDTO();
        prcProfPricingRuleOvrdDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfPricingRuleOvrdDTO.setPricingOverrideId(1);
        prcProfPricingRuleOvrdDTO.setPricingOverrideInd(2);
        prcProfPricingRuleOvrdDTOList.add(prcProfPricingRuleOvrdDTO);

        List<PrcProfPricingRuleOvrdDO> actual = target.buildPrcProfPricingRuleOvrdDOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfPricingRuleOvrdDTOList);

        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.get(0).getEffectiveDate(), is(new LocalDate(2018, 01, 05).toDate()));
        assertThat(actual.get(0).getExpirationDate(), is(new LocalDate(2019, 01, 05).toDate()));
        assertThat(actual.get(0).getGfsCustomerId(), is(contractCustomerMappingDTO.getGfsCustomerId()));
        assertThat(actual.get(0).getGfsCustomerTypeCode(), is(contractCustomerMappingDTO.getGfsCustomerTypeCode()));
        assertThat(actual.get(0).getCreateUserId(), is(userId));
        assertThat(actual.get(0).getPricingOverrideId(), is(prcProfPricingRuleOvrdDTO.getPricingOverrideId()));
        assertThat(actual.get(0).getPricingOverrideInd(), is(prcProfPricingRuleOvrdDTO.getPricingOverrideInd()));
        assertThat(actual.get(0).getLastUpdateUserId(), is(userId));

    }

    @Test
    public void shouldBuildPrcProfPricingRuleOvrdDOListForAmendment() {
        int contractPriceProfileSeq = 1;
        String userId = "Test";
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        Date farOutDate = new Date();

        List<PrcProfPricingRuleOvrdDTO> prcProfPricingRuleOvrdDTOList = new ArrayList<PrcProfPricingRuleOvrdDTO>();
        PrcProfPricingRuleOvrdDTO prcProfPricingRuleOvrdDTO = new PrcProfPricingRuleOvrdDTO();
        prcProfPricingRuleOvrdDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfPricingRuleOvrdDTO.setPricingOverrideId(1);
        prcProfPricingRuleOvrdDTO.setPricingOverrideInd(2);
        prcProfPricingRuleOvrdDTO.setGfsCustomerId(gfsCustomerId);
        prcProfPricingRuleOvrdDTO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        prcProfPricingRuleOvrdDTOList.add(prcProfPricingRuleOvrdDTO);

        List<PrcProfPricingRuleOvrdDO> actual = target.buildPrcProfPricingRuleOvrdDOListForAmendment(contractPriceProfileSeq, userId, farOutDate,
                prcProfPricingRuleOvrdDTOList);

        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.get(0).getEffectiveDate(), is(farOutDate));
        assertThat(actual.get(0).getExpirationDate(), is(farOutDate));
        assertThat(actual.get(0).getGfsCustomerId(), is(gfsCustomerId));
        assertThat(actual.get(0).getGfsCustomerTypeCode(), is(gfsCustomerTypeCode));
        assertThat(actual.get(0).getCreateUserId(), is(userId));
        assertThat(actual.get(0).getPricingOverrideId(), is(prcProfPricingRuleOvrdDTO.getPricingOverrideId()));
        assertThat(actual.get(0).getPricingOverrideInd(), is(prcProfPricingRuleOvrdDTO.getPricingOverrideInd()));
        assertThat(actual.get(0).getLastUpdateUserId(), is(userId));

    }

}
