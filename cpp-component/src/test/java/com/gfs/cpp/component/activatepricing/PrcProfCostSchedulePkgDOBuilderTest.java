package com.gfs.cpp.component.activatepricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.PrcProfCostSchedulePkgDOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfCostSchedulePkgDOBuilderTest {

    @InjectMocks
    private PrcProfCostSchedulePkgDOBuilder target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldBuildPrcProfCostSchedulePkgDO() {
        int contractPriceProfileSeq = 1;
        String userId = "Test";
        int pkgSeqNumberForCustomer = 2;

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

        PrcProfCostSchedulePkgDO actual = target.buildPrcProfCostSchedulePkgDOList(userId, contractCustomerMappingDTO, contractDetails,
                pkgSeqNumberForCustomer);

        assertThat(actual.getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.getEffectiveDate(), is(new LocalDate(2018, 01, 05).toDate()));
        assertThat(actual.getExpirationDate(), is(new LocalDate(2019, 01, 05).toDate()));
        assertThat(actual.getGfsCustomerId(), is(contractCustomerMappingDTO.getGfsCustomerId()));
        assertThat(actual.getGfsCustomerTypeCode(), is(contractCustomerMappingDTO.getGfsCustomerTypeCode()));
        assertThat(actual.getCreateUserId(), is(userId));
        assertThat(actual.getPrcProfCostSchedulePkgSeq(), is(pkgSeqNumberForCustomer));
        assertThat(actual.getLastUpdateUserId(), is(userId));
    }

}
