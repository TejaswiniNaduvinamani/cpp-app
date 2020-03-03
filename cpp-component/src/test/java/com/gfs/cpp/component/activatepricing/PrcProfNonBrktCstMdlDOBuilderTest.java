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
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.markup.PrcProfNonBrktCstMdlDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.PrcProfNonBrktCstMdlDOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfNonBrktCstMdlDOBuilderTest {

    @InjectMocks
    private PrcProfNonBrktCstMdlDOBuilder target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldBuildPrcProfNonBrktCstMdlDO() {
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

        List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTOList = new ArrayList<PrcProfNonBrktCstMdlDTO>();
        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO.setContractPriceProfileSeq(1);
        prcProfNonBrktCstMdlDTO.setCostModelId(2);
        prcProfNonBrktCstMdlDTO.setItemPriceId("1");
        prcProfNonBrktCstMdlDTO.setItemPriceLevelCode(1);
        prcProfNonBrktCstMdlDTOList.add(prcProfNonBrktCstMdlDTO);

        List<PrcProfNonBrktCstMdlDO> actual = target.buildPrcProfNonBrktCstMdlDOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfNonBrktCstMdlDTOList);

        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.get(0).getEffectiveDate(), is(new LocalDate(2018, 01, 05).toDate()));
        assertThat(actual.get(0).getExpirationDate(), is(new LocalDate(2019, 01, 05).toDate()));
        assertThat(actual.get(0).getGfsCustomerId(), is(contractCustomerMappingDTO.getGfsCustomerId()));
        assertThat(actual.get(0).getGfsCustomerTypeCode(), is(contractCustomerMappingDTO.getGfsCustomerTypeCode()));
        assertThat(actual.get(0).getCreateUserId(), is(userId));
        assertThat(actual.get(0).getCostModelId(), is(prcProfNonBrktCstMdlDTO.getCostModelId()));
        assertThat(actual.get(0).getItemPriceId(), is(prcProfNonBrktCstMdlDTO.getItemPriceId()));
        assertThat(actual.get(0).getItemPriceLevelCode(), is(prcProfNonBrktCstMdlDTO.getItemPriceLevelCode()));
        assertThat(actual.get(0).getLastUpdateUserId(), is(userId));
    }

    @Test
    public void shouldBuildPrcProfNonBrktCstMdlDOListForAmendment() {
        int contractPriceProfileSeq = 1;
        String userId = "Test";
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        Date farOutDate = new Date();

        List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTOList = new ArrayList<PrcProfNonBrktCstMdlDTO>();
        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO.setContractPriceProfileSeq(1);
        prcProfNonBrktCstMdlDTO.setCostModelId(2);
        prcProfNonBrktCstMdlDTO.setItemPriceId("1");
        prcProfNonBrktCstMdlDTO.setGfsCustomerId(gfsCustomerId);
        prcProfNonBrktCstMdlDTO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        prcProfNonBrktCstMdlDTO.setItemPriceLevelCode(1);
        prcProfNonBrktCstMdlDTOList.add(prcProfNonBrktCstMdlDTO);

        List<PrcProfNonBrktCstMdlDO> actual = target.buildPrcProfNonBrktCstMdlDOListForAmendment(contractPriceProfileSeq, userId, farOutDate,
                prcProfNonBrktCstMdlDTOList);

        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.get(0).getEffectiveDate(), is(farOutDate));
        assertThat(actual.get(0).getExpirationDate(), is(farOutDate));
        assertThat(actual.get(0).getGfsCustomerId(), is(gfsCustomerId));
        assertThat(actual.get(0).getGfsCustomerTypeCode(), is(gfsCustomerTypeCode));
        assertThat(actual.get(0).getCreateUserId(), is(userId));
        assertThat(actual.get(0).getCostModelId(), is(prcProfNonBrktCstMdlDTO.getCostModelId()));
        assertThat(actual.get(0).getItemPriceId(), is(prcProfNonBrktCstMdlDTO.getItemPriceId()));
        assertThat(actual.get(0).getItemPriceLevelCode(), is(prcProfNonBrktCstMdlDTO.getItemPriceLevelCode()));
        assertThat(actual.get(0).getLastUpdateUserId(), is(userId));
    }
}
