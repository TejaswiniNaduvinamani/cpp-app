package com.gfs.cpp.component.activatepricing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;

@Component("prcProfLessCaseRuleDOBuilder")
public class PrcProfLessCaseRuleDOBuilder {

    public List<PrcProfLessCaseRuleDO> buildPrcProfLessCaseRuleDOList(String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            ContractPricingResponseDTO contractDetails, List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOListForCMG) {
        List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList = new ArrayList<>();
        if (prcProfLessCaseRuleDOListForCMG != null && !prcProfLessCaseRuleDOListForCMG.isEmpty()) {
            for (PrcProfLessCaseRuleDO prcProfLessCaseRuleDOForCMG : prcProfLessCaseRuleDOListForCMG) {
                Date effectiveDate = contractDetails.getPricingEffectiveDate();
                Date expirationDate = contractDetails.getPricingExpirationDate();
                PrcProfLessCaseRuleDO prcProfLessCaseRuleDO = buildPrcProfLessCaseRuleDO(userId, defaultContractCustMapping, effectiveDate,
                        expirationDate, prcProfLessCaseRuleDOForCMG, prcProfLessCaseRuleDOForCMG.getContractPriceProfileSeq());

                prcProfLessCaseRuleDOList.add(prcProfLessCaseRuleDO);
            }
        }
        return prcProfLessCaseRuleDOList;
    }

    public List<PrcProfLessCaseRuleDO> buildPrcProfLessCaseRuleDOListForAmendment(int contractPriceProfileSeq, String userId, Date farOutDate,
            List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOListForCMG) {
        List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList = new ArrayList<>();
        if (prcProfLessCaseRuleDOListForCMG != null && !prcProfLessCaseRuleDOListForCMG.isEmpty()) {
            for (PrcProfLessCaseRuleDO prcProfLessCaseRuleDOForCMG : prcProfLessCaseRuleDOListForCMG) {
                PrcProfLessCaseRuleDO prcProfLessCaseRuleDO = buildPrcProfLessCaseRuleDO(userId, null, farOutDate, farOutDate,
                        prcProfLessCaseRuleDOForCMG, contractPriceProfileSeq);

                prcProfLessCaseRuleDOList.add(prcProfLessCaseRuleDO);
            }
        }
        return prcProfLessCaseRuleDOList;
    }

    private PrcProfLessCaseRuleDO buildPrcProfLessCaseRuleDO(String userId, ContractCustomerMappingDTO defaultContractCustMapping, Date effectiveDate,
            Date expirationDate, PrcProfLessCaseRuleDO prcProfLessCaseRuleDOForCMG, int contractPriceProfileSeq) {
        PrcProfLessCaseRuleDO prcProfLessCaseRuleDO = new PrcProfLessCaseRuleDO();
        prcProfLessCaseRuleDO.setEffectiveDate(effectiveDate);
        prcProfLessCaseRuleDO.setExpirationDate(expirationDate);
        prcProfLessCaseRuleDO.setGfsCustomerId(
                defaultContractCustMapping != null ? defaultContractCustMapping.getGfsCustomerId() : prcProfLessCaseRuleDOForCMG.getGfsCustomerId());
        prcProfLessCaseRuleDO.setGfsCustomerTypeCode(defaultContractCustMapping != null ? defaultContractCustMapping.getGfsCustomerTypeCode()
                : prcProfLessCaseRuleDOForCMG.getGfsCustomerTypeCode());
        prcProfLessCaseRuleDO.setCreateUserId(userId);
        prcProfLessCaseRuleDO.setLastUpdateUserId(userId);
        prcProfLessCaseRuleDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfLessCaseRuleDO.setCwMarkupAmnt(prcProfLessCaseRuleDOForCMG.getCwMarkupAmnt());
        prcProfLessCaseRuleDO.setCwMarkupAmountTypeCode(prcProfLessCaseRuleDOForCMG.getCwMarkupAmountTypeCode());
        prcProfLessCaseRuleDO.setItemPriceId(prcProfLessCaseRuleDOForCMG.getItemPriceId());
        prcProfLessCaseRuleDO.setItemPriceLevelCode(prcProfLessCaseRuleDOForCMG.getItemPriceLevelCode());
        prcProfLessCaseRuleDO.setLesscaseRuleId(prcProfLessCaseRuleDOForCMG.getLesscaseRuleId());
        prcProfLessCaseRuleDO.setMarkupAppliedBeforeDivInd(prcProfLessCaseRuleDOForCMG.getMarkupAppliedBeforeDivInd());
        prcProfLessCaseRuleDO.setNonCwMarkupAmnt(prcProfLessCaseRuleDOForCMG.getNonCwMarkupAmnt());
        prcProfLessCaseRuleDO.setNonCwMarkupAmntTypeCode(prcProfLessCaseRuleDOForCMG.getNonCwMarkupAmntTypeCode());
        return prcProfLessCaseRuleDO;
    }
}
