package com.gfs.cpp.component.markup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.component.markup.builder.MarkupDTOBuilder;
import com.gfs.cpp.proxy.ItemConfigurationServiceProxy;

@Component
public class SubgroupMarkupExtractor {

    @Autowired
    private MarkupDTOBuilder markupDTOBuilder;

    @Autowired
    private ItemConfigurationServiceProxy itemConfigurationServiceProxy;

    public List<SubgroupMarkupDTO> extractSubgroupMarkups(List<ProductTypeMarkupDTO> allMarkupsForCmg, Date pricingEffectiveDate,
            Date pricingExpirationDate) {

        List<SubgroupMarkupDTO> allSubgroupMarkupList = new ArrayList<>();

        Map<String, String> subgroupDescMap = itemConfigurationServiceProxy.getAllSubCategories(CPPConstants.LANGUAGE_CODE);

        for (ProductTypeMarkupDTO markupForCustomer : allMarkupsForCmg) {
            if (ItemPriceLevel.SUBGROUP.getCode() == Integer.parseInt(markupForCustomer.getProductType())) {
                SubgroupMarkupDTO subgroupMarkupDTO = markupDTOBuilder.buildSubgroupMarkupDTO(markupForCustomer,
                        subgroupDescMap.get(String.valueOf(markupForCustomer.getItemPriceId())), pricingEffectiveDate, pricingExpirationDate);
                allSubgroupMarkupList.add(subgroupMarkupDTO);
            }
        }

        return allSubgroupMarkupList;

    }

}
