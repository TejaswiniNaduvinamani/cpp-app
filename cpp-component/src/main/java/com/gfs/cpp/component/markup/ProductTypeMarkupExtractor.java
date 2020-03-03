package com.gfs.cpp.component.markup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.component.markup.builder.MarkupDTOBuilder;
import com.gfs.cpp.proxy.ItemServiceProxy;

@Component
public class ProductTypeMarkupExtractor {

    @Autowired
    private ItemServiceProxy itemServiceProxy;

    @Autowired
    private MarkupDTOBuilder markupDTOBuilder;

    public List<ProductTypeMarkupDTO> extractProductTypeMarkup(List<ProductTypeMarkupDTO> allMarkupsForCmg) {

        List<ProductTypeMarkupDTO> allProductTypeMarkups = new ArrayList<>();

        Map<Integer, String> offeringMap = itemServiceProxy.getAllProductTypesById();

        for (ProductTypeMarkupDTO markup : allMarkupsForCmg) {
            if (ItemPriceLevel.PRODUCT_TYPE.getCode() == Integer.parseInt(markup.getProductType())) {
                markup.setProductType(offeringMap.get(markup.getItemPriceId()));
                markup.setMarkup(markupDTOBuilder.formatAmount(markup.getMarkup()));
                allProductTypeMarkups.add(markup);
            }
        }

        sortProductTypeByItemId(allProductTypeMarkups);
        return allProductTypeMarkups;
    }

    private void sortProductTypeByItemId(List<ProductTypeMarkupDTO> productTypeMarkupDTOList) {
        Collections.sort(productTypeMarkupDTOList, new Comparator<ProductTypeMarkupDTO>() {
            public int compare(ProductTypeMarkupDTO o1, ProductTypeMarkupDTO o2) {
                Integer itemPriceId1 = o1.getItemPriceId();
                Integer itemPriceId2 = o2.getItemPriceId();
                return (itemPriceId1).compareTo(itemPriceId2);
            }
        });
    }

}
