package com.gfs.cpp.component.customerinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.util.CPPDateUtils;

@Component
public class ContractDataCopyHelper {

    @Autowired
    private CPPDateUtils cppDateUtils;

    public ContractPricingDO buildContractPricingDO(ClmContractResponseDTO agreementData, ContractPricingResponseDTO contractDetailsOfLatestVersion,
            CPPInformationDTO cppInformationDTO) {

        ContractPricingDO contractPricingDO = new ContractPricingDO();
        contractPricingDO.setVersionNbr(cppInformationDTO.getVersionNumber());
        contractPricingDO.setAgreementId(agreementData.getContractAgreementId());
        contractPricingDO.setPricingEffectiveDate(agreementData.getAmendmentEffectiveDate());
        contractPricingDO.setPricingExpirationDate(cppDateUtils.getFutureDate());
        contractPricingDO.setClmContractStartDate(agreementData.getAmendmentEffectiveDate());
        contractPricingDO.setClmContractEndDate(agreementData.getContractExpirationDate());
        contractPricingDO.setParentAgreementId(agreementData.getParentAgreementId());
        contractPricingDO.setContractPriceProfileId(contractDetailsOfLatestVersion.getContractPriceProfileId());
        contractPricingDO.setExpireLowerLevelInd(contractDetailsOfLatestVersion.getExpireLowerLevelInd());
        contractPricingDO.setContractName(agreementData.getContractName());
        contractPricingDO.setTransferFeeInd(contractDetailsOfLatestVersion.getTransferFeeFlag());
        contractPricingDO.setLabelAssesmentInd(contractDetailsOfLatestVersion.getAssessmentFeeFlag());

        return contractPricingDO;
    }

}
