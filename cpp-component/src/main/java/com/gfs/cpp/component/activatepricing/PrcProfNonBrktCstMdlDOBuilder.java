package com.gfs.cpp.component.activatepricing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.markup.PrcProfNonBrktCstMdlDO;

@Component("prcProfNonBrktCstMdlDOBuilder")
public class PrcProfNonBrktCstMdlDOBuilder {

    public List<PrcProfNonBrktCstMdlDO> buildPrcProfNonBrktCstMdlDOList(String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            ContractPricingResponseDTO contractDetails, List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTOList) {
        List<PrcProfNonBrktCstMdlDO> prcProfNonBrktCstMdlDOList = new ArrayList<>();
        if (prcProfNonBrktCstMdlDTOList != null && !prcProfNonBrktCstMdlDTOList.isEmpty()) {
            for (PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO : prcProfNonBrktCstMdlDTOList) {
                Date effectiveDate = contractDetails.getPricingEffectiveDate();
                Date expirationDate = contractDetails.getPricingExpirationDate();
                int cppSeq = prcProfNonBrktCstMdlDTO.getContractPriceProfileSeq();
                PrcProfNonBrktCstMdlDO prcProfNonBrktCstMdlDO = buildPrcProfNonBrktCstMdlDO(userId, defaultContractCustMapping, effectiveDate,
                        expirationDate, prcProfNonBrktCstMdlDTO, cppSeq);
                prcProfNonBrktCstMdlDOList.add(prcProfNonBrktCstMdlDO);

            }
        }
        return prcProfNonBrktCstMdlDOList;
    }

    public List<PrcProfNonBrktCstMdlDO> buildPrcProfNonBrktCstMdlDOListForAmendment(int cppSeq, String userId, Date farOutDate,
            List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTOList) {
        List<PrcProfNonBrktCstMdlDO> prcProfNonBrktCstMdlDOList = new ArrayList<>();
        for (PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO : prcProfNonBrktCstMdlDTOList) {
            PrcProfNonBrktCstMdlDO prcProfNonBrktCstMdlDO = buildPrcProfNonBrktCstMdlDO(userId, null, farOutDate, farOutDate, prcProfNonBrktCstMdlDTO,
                    cppSeq);
            prcProfNonBrktCstMdlDOList.add(prcProfNonBrktCstMdlDO);

        }
        return prcProfNonBrktCstMdlDOList;
    }

    private PrcProfNonBrktCstMdlDO buildPrcProfNonBrktCstMdlDO(String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            Date effectiveDate, Date expirationDate, PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO, int cppSeq) {
        PrcProfNonBrktCstMdlDO prcProfNonBrktCstMdlDO = new PrcProfNonBrktCstMdlDO();
        prcProfNonBrktCstMdlDO.setEffectiveDate(effectiveDate);
        prcProfNonBrktCstMdlDO.setExpirationDate(expirationDate);
        prcProfNonBrktCstMdlDO.setGfsCustomerId(
                defaultContractCustMapping != null ? defaultContractCustMapping.getGfsCustomerId() : prcProfNonBrktCstMdlDTO.getGfsCustomerId());
        prcProfNonBrktCstMdlDO.setGfsCustomerTypeCode(defaultContractCustMapping != null ? defaultContractCustMapping.getGfsCustomerTypeCode()
                : prcProfNonBrktCstMdlDTO.getGfsCustomerTypeCode());
        prcProfNonBrktCstMdlDO.setCreateUserId(userId);
        prcProfNonBrktCstMdlDO.setLastUpdateUserId(userId);
        prcProfNonBrktCstMdlDO.setContractPriceProfileSeq(cppSeq);
        prcProfNonBrktCstMdlDO.setCostModelId(prcProfNonBrktCstMdlDTO.getCostModelId());
        prcProfNonBrktCstMdlDO.setItemPriceId(prcProfNonBrktCstMdlDTO.getItemPriceId());
        prcProfNonBrktCstMdlDO.setItemPriceLevelCode(prcProfNonBrktCstMdlDTO.getItemPriceLevelCode());
        return prcProfNonBrktCstMdlDO;
    }

}