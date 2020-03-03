package com.gfs.cpp.component.furtherance;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceBaseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;

@Component
public class FurtheranceDTOBuilder {

    public FurtheranceInformationDTO buildDefaultFurtheranceDTO(String parentAgreementId, int furtheranceSeq) {

        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setCppFurtheranceSeq(furtheranceSeq);
        furtheranceInformationDTO.setParentCLMAgreementId(parentAgreementId);
        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_INITIATED.code);

        return furtheranceInformationDTO;
    }

    public FurtheranceBaseDTO buildFurtheranceBaseDTO(String parentCLMAgreementId, int furtheranceSeq, String contractType) {

        FurtheranceBaseDTO furtheranceBaseDTO = new FurtheranceBaseDTO();
        furtheranceBaseDTO.setAgreementId(parentCLMAgreementId);
        furtheranceBaseDTO.setCppFurtheranceSeq(furtheranceSeq);
        furtheranceBaseDTO.setContractType(contractType);

        return furtheranceBaseDTO;
    }

    public CPPFurtheranceTrackingDTO buildCPPFurtheranceTrackingDTO(int cppFurtheranceSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemPriceId, int itemPriceLevelCode, String changeTableName, int furtheranceActionCode) {
        
        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = new CPPFurtheranceTrackingDTO();
        furtheranceTrackingDTO.setGfsCustomerId(gfsCustomerId);
        furtheranceTrackingDTO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        furtheranceTrackingDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceTrackingDTO.setItemPriceId(itemPriceId);
        furtheranceTrackingDTO.setItemPriceLevelCode(itemPriceLevelCode);
        furtheranceTrackingDTO.setChangeTableName(changeTableName);
        furtheranceTrackingDTO.setFurtheranceActionCode(furtheranceActionCode);
        
        return furtheranceTrackingDTO;
    }

}
