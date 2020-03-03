package com.gfs.cpp.component.activatepricing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfAuditAuthorityDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.model.auditauthority.PrcProfAuditAuthorityDO;

@Component("prcProfAuditAuthorityDOBuilder")
public class PrcProfAuditAuthorityDOBuilder {

    public List<PrcProfAuditAuthorityDO> buildPrcProfAuditAuthorityDTOList(String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            ContractPricingResponseDTO contractDetails, List<PrcProfAuditAuthorityDTO> prcProfAuditAuthorityDTOList) {
        List<PrcProfAuditAuthorityDO> prcProfAuditAuthorityDOList = null;
        if (prcProfAuditAuthorityDTOList != null && !prcProfAuditAuthorityDTOList.isEmpty()) {
            prcProfAuditAuthorityDOList = new ArrayList<>();
            for (PrcProfAuditAuthorityDTO prcProfAuditAuthorityDTO : prcProfAuditAuthorityDTOList) {
                Date effectiveDate = contractDetails.getPricingEffectiveDate();
                Date expirationDate = contractDetails.getPricingExpirationDate();
                PrcProfAuditAuthorityDO prcProfAuditAuthorityDO = buildPrcProfAuditAuthorityDO(userId, defaultContractCustMapping, effectiveDate,
                        expirationDate, prcProfAuditAuthorityDTO, contractDetails.getContractPriceProfileSeq());
                prcProfAuditAuthorityDOList.add(prcProfAuditAuthorityDO);
            }
        }
        return prcProfAuditAuthorityDOList;
    }

    public List<PrcProfAuditAuthorityDO> buildPrcProfAuditAuthorityDTOForAmendment(int customerProfileSeq, String userId, Date farOutDate,
            List<PrcProfAuditAuthorityDTO> prcProfAuditAuthorityDTOList) {
        List<PrcProfAuditAuthorityDO> prcProfAuditAuthorityDOList = null;
        if (prcProfAuditAuthorityDTOList != null && !prcProfAuditAuthorityDTOList.isEmpty()) {
            prcProfAuditAuthorityDOList = new ArrayList<>();
            for (PrcProfAuditAuthorityDTO prcProfAuditAuthorityDTO : prcProfAuditAuthorityDTOList) {
                PrcProfAuditAuthorityDO prcProfAuditAuthorityDO = buildPrcProfAuditAuthorityDO(userId, null, farOutDate, farOutDate,
                        prcProfAuditAuthorityDTO, customerProfileSeq);
                prcProfAuditAuthorityDOList.add(prcProfAuditAuthorityDO);
            }
        }
        return prcProfAuditAuthorityDOList;
    }

    private PrcProfAuditAuthorityDO buildPrcProfAuditAuthorityDO(String userId, ContractCustomerMappingDTO defaultContractCustMapping,
            Date effectiveDate, Date expirationDate, PrcProfAuditAuthorityDTO prcProfAuditAuthorityDTO, int contractPriceProfileSeq) {
        PrcProfAuditAuthorityDO prcProfAuditAuthorityDO = new PrcProfAuditAuthorityDO();
        prcProfAuditAuthorityDO.setEffectiveDate(effectiveDate);
        prcProfAuditAuthorityDO.setExpirationDate(expirationDate);
        prcProfAuditAuthorityDO.setGfsCustomerId(
                defaultContractCustMapping != null ? defaultContractCustMapping.getGfsCustomerId() : prcProfAuditAuthorityDTO.getGfsCustomerId());
        prcProfAuditAuthorityDO.setGfsCustomerTypeCode(defaultContractCustMapping != null ? defaultContractCustMapping.getGfsCustomerTypeCode()
                : prcProfAuditAuthorityDTO.getGfsCustomerType());
        prcProfAuditAuthorityDO.setCreateUserId(userId);
        prcProfAuditAuthorityDO.setLastUpdateUserId(userId);
        prcProfAuditAuthorityDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfAuditAuthorityDO.setPrcProfAuditAuthorityInd(prcProfAuditAuthorityDTO.getPrcProfAuditAuthorityInd());
        return prcProfAuditAuthorityDO;
    }

}
