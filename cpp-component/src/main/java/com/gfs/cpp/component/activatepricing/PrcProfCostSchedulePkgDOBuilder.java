package com.gfs.cpp.component.activatepricing;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;

@Component("prcProfCostSchedulePkgDOBuilder")
public class PrcProfCostSchedulePkgDOBuilder {

    public PrcProfCostSchedulePkgDO buildPrcProfCostSchedulePkgDOList(String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            ContractPricingResponseDTO contractDetails, int pkgSeqNumberForCustomer) {
        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO = new PrcProfCostSchedulePkgDO();
        prcProfCostSchedulePkgDO.setEffectiveDate(contractDetails.getPricingEffectiveDate());
        prcProfCostSchedulePkgDO.setExpirationDate(contractDetails.getPricingExpirationDate());
        prcProfCostSchedulePkgDO.setGfsCustomerId(defaultContractCustMapping.getGfsCustomerId());
        prcProfCostSchedulePkgDO.setGfsCustomerTypeCode(defaultContractCustMapping.getGfsCustomerTypeCode());
        prcProfCostSchedulePkgDO.setCreateUserId(userId);
        prcProfCostSchedulePkgDO.setLastUpdateUserId(userId);
        prcProfCostSchedulePkgDO.setContractPriceProfileSeq(contractDetails.getContractPriceProfileSeq());
        prcProfCostSchedulePkgDO.setPrcProfCostSchedulePkgSeq(pkgSeqNumberForCustomer);
        return prcProfCostSchedulePkgDO;
    }
}
