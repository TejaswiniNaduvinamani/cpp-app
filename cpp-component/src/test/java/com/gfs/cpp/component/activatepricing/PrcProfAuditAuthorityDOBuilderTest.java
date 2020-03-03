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
import com.gfs.cpp.common.dto.markup.PrcProfAuditAuthorityDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.auditauthority.PrcProfAuditAuthorityDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.PrcProfAuditAuthorityDOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfAuditAuthorityDOBuilderTest {

    @InjectMocks
    private PrcProfAuditAuthorityDOBuilder target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldBuildPrcProfAuditAuthorityDO() {
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
        contractDetails.setContractPriceProfileId(1);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 05).toDate());

        List<PrcProfAuditAuthorityDTO> prcProfAuditAuthorityDTOList = new ArrayList<PrcProfAuditAuthorityDTO>();
        PrcProfAuditAuthorityDTO prcProfAuditAuthorityDTO = new PrcProfAuditAuthorityDTO();
        prcProfAuditAuthorityDTO.setContractPriceProfileSeq(1);
        prcProfAuditAuthorityDTO.setGfsCustomerId("1");
        prcProfAuditAuthorityDTO.setGfsCustomerType(22);
        prcProfAuditAuthorityDTO.setCreateUserId("test user");
        prcProfAuditAuthorityDTO.setEffectiveDate(new LocalDate(2018, 05, 05).toDate());
        prcProfAuditAuthorityDTO.setExpirationDate(new LocalDate(2019, 05, 05).toDate());
        prcProfAuditAuthorityDTO.setLastUpdateUserId("test user");
        prcProfAuditAuthorityDTO.setPrcProfAuditAuthorityInd(1);
        prcProfAuditAuthorityDTOList.add(prcProfAuditAuthorityDTO);

        List<PrcProfAuditAuthorityDO> actual = target.buildPrcProfAuditAuthorityDTOList(userId, contractCustomerMappingDTO, contractDetails,
                prcProfAuditAuthorityDTOList);

        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractDetails.getContractPriceProfileSeq()));
        assertThat(actual.get(0).getGfsCustomerId(), is(contractCustomerMappingDTO.getGfsCustomerId()));
        assertThat(actual.get(0).getGfsCustomerTypeCode(), is(contractCustomerMappingDTO.getGfsCustomerTypeCode()));
        assertThat(actual.get(0).getCreateUserId(), is(userId));
        assertThat(actual.get(0).getEffectiveDate(), is(new LocalDate(2018, 01, 05).toDate()));
        assertThat(actual.get(0).getExpirationDate(), is(new LocalDate(2019, 01, 05).toDate()));
        assertThat(actual.get(0).getLastUpdateUserId(), is(userId));
        assertThat(actual.get(0).getPrcProfAuditAuthorityInd(), is(prcProfAuditAuthorityDTO.getPrcProfAuditAuthorityInd()));

    }

    @Test
    public void shouldBuildPrcProfAuditAuthorityDTOForAmendment() {
        int contractPriceProfileSeq = 1;
        String userId = "Test";
        String customerId = "1";
        int customerTypeCode = 31;
        Date farOutDate = new Date();

        List<PrcProfAuditAuthorityDTO> prcProfAuditAuthorityDTOList = new ArrayList<PrcProfAuditAuthorityDTO>();
        PrcProfAuditAuthorityDTO prcProfAuditAuthorityDTO = new PrcProfAuditAuthorityDTO();
        prcProfAuditAuthorityDTO.setContractPriceProfileSeq(1);
        prcProfAuditAuthorityDTO.setGfsCustomerId(customerId);
        prcProfAuditAuthorityDTO.setGfsCustomerType(customerTypeCode);
        prcProfAuditAuthorityDTO.setCreateUserId("test user");
        prcProfAuditAuthorityDTO.setEffectiveDate(new LocalDate(2018, 05, 05).toDate());
        prcProfAuditAuthorityDTO.setExpirationDate(new LocalDate(2019, 05, 05).toDate());
        prcProfAuditAuthorityDTO.setLastUpdateUserId("test user");
        prcProfAuditAuthorityDTO.setPrcProfAuditAuthorityInd(1);
        prcProfAuditAuthorityDTOList.add(prcProfAuditAuthorityDTO);

        List<PrcProfAuditAuthorityDO> actual = target.buildPrcProfAuditAuthorityDTOForAmendment(contractPriceProfileSeq, userId, farOutDate,
                prcProfAuditAuthorityDTOList);

        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.get(0).getGfsCustomerId(), is(customerId));
        assertThat(actual.get(0).getGfsCustomerTypeCode(), is(customerTypeCode));
        assertThat(actual.get(0).getCreateUserId(), is(userId));
        assertThat(actual.get(0).getEffectiveDate(), is(farOutDate));
        assertThat(actual.get(0).getExpirationDate(), is(farOutDate));
        assertThat(actual.get(0).getLastUpdateUserId(), is(userId));
        assertThat(actual.get(0).getPrcProfAuditAuthorityInd(), is(prcProfAuditAuthorityDTO.getPrcProfAuditAuthorityInd()));

    }

}
