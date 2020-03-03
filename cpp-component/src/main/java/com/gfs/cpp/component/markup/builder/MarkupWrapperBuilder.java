package com.gfs.cpp.component.markup.builder;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;

@Component
public class MarkupWrapperBuilder {

    @Autowired
    private MarkupDTOBuilder markupDTOBuilder;

    public MarkupWrapperDTO buildDefaultMarkupWrapper(int contractPriceProfileSeq, String cmgCustomerId, String markupName, Date pricingEffectiveDate,
            Date pricingExpirationDate) {

        MarkupWrapperDTO markupWrapperDTO = new MarkupWrapperDTO();
        markupWrapperDTO.setGfsCustomerId(cmgCustomerId);
        markupWrapperDTO.setGfsCustomerType(CPPConstants.CMG_CUSTOMER_TYPE_CODE);
        markupWrapperDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapperDTO
                .setProductMarkupList(markupDTOBuilder.buildDefaultProductMarkupDTOList(cmgCustomerId, pricingEffectiveDate, pricingExpirationDate));
        markupWrapperDTO.setMarkupName(markupName);
        markupWrapperDTO.setItemLevelMarkupList(Collections.<ItemLevelMarkupDTO> emptyList());
        markupWrapperDTO.setSubgroupMarkupList(Collections.<SubgroupMarkupDTO> emptyList());
        markupWrapperDTO.setIsMarkupSaved(false);

        return markupWrapperDTO;

    }

    public MarkupWrapperDTO buildMarkupWrapperFromGrid(int contractPriceProfileSeq, MarkupGridDTO markupGridDTO) {

        MarkupWrapperDTO markupWrapperDTO = new MarkupWrapperDTO();
        markupWrapperDTO.setGfsCustomerId(markupGridDTO.getGfsCustomerId());
        markupWrapperDTO.setGfsCustomerType(CPPConstants.CMG_CUSTOMER_TYPE_CODE);
        markupWrapperDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapperDTO.setProductMarkupList(markupGridDTO.getProductTypeMarkups());
        markupWrapperDTO.setItemLevelMarkupList(markupGridDTO.getItemMarkups());
        markupWrapperDTO.setSubgroupMarkupList(markupGridDTO.getSubgroupMarkups());
        markupWrapperDTO.setMarkupName(markupGridDTO.getMarkupName());
        markupWrapperDTO.setIsMarkupSaved(true);

        return markupWrapperDTO;
    }

}
