package com.gfs.cpp.component.contractpricing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.util.CPPDateUtils;

@Component
public class ContractPricingDOBuilder {

    @Autowired
    private CPPDateUtils cppDateUtils;

    public ContractPricingDO buildContractPricingDO(ContractPricingDTO contractPricingDTO, CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        ContractPricingDO contractPricingDO = new ContractPricingDO();

        contractPricingDO.setContractPriceProfileSeq(contractPricingDTO.getContractPriceProfileSeq());
        contractPricingDO.setGfsCustomerId(cmgCustomerResponseDTO.getId());
        contractPricingDO.setCustomerTypeCode(cmgCustomerResponseDTO.getTypeCode());
        contractPricingDO.setPricingEffectiveDate(contractPricingDTO.getPricingEffectiveDate());
        contractPricingDO.setPricingExpirationDate(contractPricingDTO.getPricingExpirationDate());
        contractPricingDO.setContractPriceProfileId(contractPricingDTO.getContractPriceProfileId());
        contractPricingDO.setPriceAuditPrivileges(contractPricingDTO.getPriceAuditFlag());
        contractPricingDO.setPriceAuditInd(contractPricingDTO.getPriceAuditFlag() ? 1 : 0);
        contractPricingDO.setPriceVerifyPrivileges(contractPricingDTO.getPriceVerificationFlag());
        contractPricingDO.setPriceVerifInd(contractPricingDTO.getPriceVerificationFlag() ? 1 : 0);
        contractPricingDO.setScheduleForCostChange(contractPricingDTO.getScheduleForCostChange());
        contractPricingDO.setCostModelGFSAssesFee(contractPricingDTO.getAssessmentFeeFlag());
        contractPricingDO.setLabelAssesmentInd(contractPricingDTO.getAssessmentFeeFlag() ? 1 : 0);
        contractPricingDO.setCostModelTransferFee(contractPricingDTO.getTransferFeeFlag());
        contractPricingDO.setTransferFeeInd(contractPricingDTO.getTransferFeeFlag() ? 1 : 0);
        contractPricingDO.setContractTypeCode(contractPricingDTO.getContractType());
        contractPricingDO.setContractName(contractPricingDTO.getContractName());
        contractPricingDO.setAgreementId(contractPricingDTO.getAgreementId());
        contractPricingDO.setExpireLowerLevelInd(contractPricingDTO.getExpireLowerLevelInd());
        contractPricingDO.setEffectiveDateFuture(cppDateUtils.getFutureDate());
        contractPricingDO.setExpirationDateFuture(cppDateUtils.getFutureDate());
        contractPricingDO.setClmContractStartDate(contractPricingDTO.getClmContractStartDate());
        contractPricingDO.setClmContractEndDate(contractPricingDTO.getClmContractEndDate());
        contractPricingDO.setParentAgreementId(contractPricingDTO.getParentAgreementId());
        contractPricingDO.setVersionNbr(contractPricingDTO.getVersionNbr());
        return contractPricingDO;
    }

}
