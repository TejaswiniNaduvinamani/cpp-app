package com.gfs.cpp.component.activatepricing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfPricingRuleOvrdDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.markup.PrcProfPricingRuleOvrdDO;

@Component("prcProfPricingRuleOvrdDOBuilder")
public class PrcProfPricingRuleOvrdDOBuilder {

    public List<PrcProfPricingRuleOvrdDO> buildPrcProfPricingRuleOvrdDOList(String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            ContractPricingResponseDTO contractDetails, List<PrcProfPricingRuleOvrdDTO> prcProfPricingRuleOvrdDTOList) {
        List<PrcProfPricingRuleOvrdDO> prcProfPricingRuleOvrdDOList = new ArrayList<>();
        if (prcProfPricingRuleOvrdDTOList != null && !prcProfPricingRuleOvrdDTOList.isEmpty()) {
            for (PrcProfPricingRuleOvrdDTO prcProfPricingRuleOvrdDTO : prcProfPricingRuleOvrdDTOList) {
                String gfsCustomerId = defaultContractCustMapping.getGfsCustomerId();
                int gfsCustomerTypeCode = defaultContractCustMapping.getGfsCustomerTypeCode();
                Date effectiveDate = contractDetails.getPricingEffectiveDate();
                Date expirationDate = contractDetails.getPricingExpirationDate();
                PrcProfPricingRuleOvrdDO prcProfPricingRuleOvrdDO = buildPrcProfPricingRuleOvrdDO(userId, gfsCustomerId, gfsCustomerTypeCode,
                        effectiveDate, expirationDate, prcProfPricingRuleOvrdDTO, prcProfPricingRuleOvrdDTO.getContractPriceProfileSeq());

                prcProfPricingRuleOvrdDOList.add(prcProfPricingRuleOvrdDO);
            }
        }
        return prcProfPricingRuleOvrdDOList;
    }

    public List<PrcProfPricingRuleOvrdDO> buildPrcProfPricingRuleOvrdDOListForAmendment(int cppSeq, String userId, Date farOutDate,
            List<PrcProfPricingRuleOvrdDTO> prcProfPricingRuleOvrdDTOList) {
        List<PrcProfPricingRuleOvrdDO> prcProfPricingRuleOvrdDOList = new ArrayList<>();
        for (PrcProfPricingRuleOvrdDTO prcProfPricingRuleOvrdDTO : prcProfPricingRuleOvrdDTOList) {
            String gfsCustomerId = prcProfPricingRuleOvrdDTO.getGfsCustomerId();
            int gfsCustomerTypeCode = prcProfPricingRuleOvrdDTO.getGfsCustomerTypeCode();
            PrcProfPricingRuleOvrdDO prcProfPricingRuleOvrdDO = buildPrcProfPricingRuleOvrdDO(userId, gfsCustomerId, gfsCustomerTypeCode, farOutDate,
                    farOutDate, prcProfPricingRuleOvrdDTO, cppSeq);

            prcProfPricingRuleOvrdDOList.add(prcProfPricingRuleOvrdDO);
        }
        return prcProfPricingRuleOvrdDOList;
    }

    private PrcProfPricingRuleOvrdDO buildPrcProfPricingRuleOvrdDO(String userId, String gfsCustomerId, int gfsCustomerTypeCode, Date effectiveDate,
            Date expirationDate, PrcProfPricingRuleOvrdDTO prcProfPricingRuleOvrdDTO, int contractPriceProfileSeq) {
        PrcProfPricingRuleOvrdDO prcProfPricingRuleOvrdDO = new PrcProfPricingRuleOvrdDO();
        prcProfPricingRuleOvrdDO.setEffectiveDate(effectiveDate);
        prcProfPricingRuleOvrdDO.setExpirationDate(expirationDate);
        prcProfPricingRuleOvrdDO.setGfsCustomerId(gfsCustomerId);
        prcProfPricingRuleOvrdDO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        prcProfPricingRuleOvrdDO.setCreateUserId(userId);
        prcProfPricingRuleOvrdDO.setLastUpdateUserId(userId);
        prcProfPricingRuleOvrdDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfPricingRuleOvrdDO.setPricingOverrideId(prcProfPricingRuleOvrdDTO.getPricingOverrideId());
        prcProfPricingRuleOvrdDO.setPricingOverrideInd(prcProfPricingRuleOvrdDTO.getPricingOverrideInd());
        return prcProfPricingRuleOvrdDO;
    }
}
