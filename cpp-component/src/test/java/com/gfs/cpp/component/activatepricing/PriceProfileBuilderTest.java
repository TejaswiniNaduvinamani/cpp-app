package com.gfs.cpp.component.activatepricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.PriceProfileBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PriceProfileBuilderTest {

    @InjectMocks
    private PriceProfileBuilder target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void buildContractPricingDOTest() throws ParseException {

        int contractPriceProfileSeq = 1;
        ContractCustomerMappingDTO contractCustomerMappingDTO = buildContractCustomerMappingDTO();
        ContractPricingResponseDTO contractDetails = buildContractPricingResponseDTO();
        ContractPricingDO verifyAgainstDTO = buildContractPricingDO();
        ContractPricingDO contractPricingDO = target.buildContractPricingDO(contractPriceProfileSeq, contractCustomerMappingDTO, contractDetails);

        assertThat(contractPricingDO.getContractPriceProfileSeq(), equalTo(verifyAgainstDTO.getContractPriceProfileSeq()));
        assertThat(contractPricingDO.getContractPriceProfileId(), equalTo(verifyAgainstDTO.getContractPriceProfileId()));
        assertThat(contractPricingDO.getCustomerTypeCode(), equalTo(verifyAgainstDTO.getCustomerTypeCode()));
        assertThat(contractPricingDO.getGfsCustomerId(), equalTo(verifyAgainstDTO.getGfsCustomerId()));

    }

    private ContractPricingDO buildContractPricingDO() throws ParseException {
        ContractPricingDO contractPricingDO = new ContractPricingDO();
        contractPricingDO.setContractPriceProfileId(1);
        contractPricingDO.setGfsCustomerId("Customer ID");
        contractPricingDO.setCustomerTypeCode(31);
        contractPricingDO.setEffectiveDateFuture(new LocalDate(9999, 01, 01).toDate());
        contractPricingDO.setExpirationDateFuture(new LocalDate(9999, 01, 01).toDate());
        contractPricingDO.setContractPriceProfileSeq(1);
        contractPricingDO.setPriceAuditPrivileges(true);
        contractPricingDO.setPriceVerifyPrivileges(true);
        contractPricingDO.setScheduleForCostChange(CPPConstants.SCHEDULE_FOR_COST_GREGORIAN);
        return contractPricingDO;
    }

    private ContractPricingResponseDTO buildContractPricingResponseDTO() {
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmAgreementId("1");
        contractPricingResponseDTO.setClmContractTypeSeq(1);
        contractPricingResponseDTO.setContractPriceProfileId(1);
        contractPricingResponseDTO.setContractPriceProfileSeq(1);
        contractPricingResponseDTO.setContractPriceProfStatusCode(1);
        contractPricingResponseDTO.setPricingEffectiveDate(new LocalDate(9999, 01, 01).toDate());
        contractPricingResponseDTO.setPricingExhibitSysId("1");
        contractPricingResponseDTO.setPricingExpirationDate(new LocalDate(9999, 01, 01).toDate());
        return contractPricingResponseDTO;
    }

    private ContractCustomerMappingDTO buildContractCustomerMappingDTO() {
        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("Customer ID");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(31);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);
        return contractCustomerMappingDTO;
    }

}
