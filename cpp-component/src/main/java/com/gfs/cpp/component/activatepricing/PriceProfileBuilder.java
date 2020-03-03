package com.gfs.cpp.component.activatepricing;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;

@Component("priceProfileBuilder")
public class PriceProfileBuilder {

    public ContractPricingDO buildContractPricingDO(int contractPriceProfileSeq, ContractCustomerMappingDTO contractCustomerMappingDTO,
            ContractPricingResponseDTO contractDetails) {
        ContractPricingDO contractPricingDO = new ContractPricingDO();
        contractPricingDO.setContractPriceProfileId(contractDetails.getContractPriceProfileId());
        contractPricingDO.setGfsCustomerId(contractCustomerMappingDTO.getGfsCustomerId());
        contractPricingDO.setCustomerTypeCode(contractCustomerMappingDTO.getGfsCustomerTypeCode());
        contractPricingDO.setEffectiveDateFuture(contractDetails.getPricingEffectiveDate());
        contractPricingDO.setExpirationDateFuture(contractDetails.getPricingExpirationDate());
        contractPricingDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        return contractPricingDO;
    }

}
