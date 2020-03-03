package com.gfs.cpp.component.customerinfo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.customerinfo.ContractDataCopyHelper;

@RunWith(MockitoJUnitRunner.class)
public class ContractDataCopyHelperTest {

    @InjectMocks
    private ContractDataCopyHelper target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldBuildContractPricingDO() {
        String contractAgreementId = "agreement-id";
        String parentAgreementId = "parent-agreement-id";
        Date effectiveDate = new LocalDate(2019, 1, 1).toDate();
        Date contractExpirationDate = new LocalDate(2019, 12, 31).toDate();
        Date farOutDate = new LocalDate(9999, 12, 31).toDate();
        Date amendmentEffectiveDate = new LocalDate(2019, 6, 30).toDate();
        int versionNbr = 2;
        int cppId = -344;
        int expireLowerLevelInd = 1;
        String contractName = "contract-name";
        
        doReturn(farOutDate).when(cppDateUtils).getFutureDate();

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractAgreementId(contractAgreementId);
        agreementData.setContractEffectiveDate(effectiveDate);
        agreementData.setContractExpirationDate(contractExpirationDate);
        agreementData.setParentAgreementId(parentAgreementId);
        agreementData.setAmendmentEffectiveDate(amendmentEffectiveDate);
        agreementData.setContractName(contractName);
        
        ContractPricingResponseDTO contractDetailsOfLatestVersion = new ContractPricingResponseDTO();
        contractDetailsOfLatestVersion.setContractPriceProfileId(cppId);
        contractDetailsOfLatestVersion.setExpireLowerLevelInd(expireLowerLevelInd);

        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setVersionNumber(versionNbr);

        ContractPricingDO contractPricingDO = target.buildContractPricingDO(agreementData, contractDetailsOfLatestVersion, cppInformationDTO);

        assertThat(contractPricingDO.getAgreementId(), equalTo(contractAgreementId));
        assertThat(contractPricingDO.getClmContractEndDate(), equalTo(contractExpirationDate));
        assertThat(contractPricingDO.getClmContractStartDate(), equalTo(amendmentEffectiveDate));
        assertThat(contractPricingDO.getContractPriceProfileId(), equalTo(cppId));
        assertThat(contractPricingDO.getExpireLowerLevelInd(), equalTo(expireLowerLevelInd));
        assertThat(contractPricingDO.getPricingEffectiveDate(), equalTo(amendmentEffectiveDate));
        assertThat(contractPricingDO.getPricingExpirationDate(), equalTo(farOutDate));
        assertThat(contractPricingDO.getContractName(), equalTo(contractName));

    }

}
