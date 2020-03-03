package com.gfs.cpp.component.markup.builder;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.component.markup.ItemLevelMarkupExtractor;
import com.gfs.cpp.component.markup.ProductTypeMarkupExtractor;
import com.gfs.cpp.component.markup.SubgroupMarkupExtractor;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@Component
public class MarkupGridBuilder {

    @Autowired
    private ProductTypeMarkupExtractor productTypeMarkupExtractor;
    @Autowired
    private ItemLevelMarkupExtractor itemLevelMarkupExtractor;
    @Autowired
    private CustomerServiceProxy customerServiceProxy;
    @Autowired
    private SubgroupMarkupExtractor subgroupMarkupExtractor;

    public MarkupGridDTO buildMarkupGrid(String cmgCustomerId, List<ProductTypeMarkupDTO> allMarkupsForCmg,
            Map<String, ItemInformationDTO> allItemsByItemId, int contractPriceProfileSeq, Date pricingEffectiveDate, Date pricingExpirationDate) {

        MarkupGridDTO markupGridForCmg = new MarkupGridDTO();
        markupGridForCmg.setGfsCustomerId(cmgCustomerId);
        markupGridForCmg.setMarkupName(customerServiceProxy.fetchGroupName(cmgCustomerId, CPPConstants.CMG_CUSTOMER_TYPE_CODE));
        markupGridForCmg.setItemMarkups(itemLevelMarkupExtractor.extractItemTypeMarkups(allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq,
                cmgCustomerId, pricingEffectiveDate, pricingExpirationDate));
        markupGridForCmg
                .setSubgroupMarkups(subgroupMarkupExtractor.extractSubgroupMarkups(allMarkupsForCmg, pricingEffectiveDate, pricingExpirationDate));
        markupGridForCmg.setProductTypeMarkups(productTypeMarkupExtractor.extractProductTypeMarkup(allMarkupsForCmg));

        return markupGridForCmg;
    }

}
