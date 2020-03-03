package com.gfs.cpp.component.markup.builder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.AmountType;
import com.gfs.corp.component.price.common.types.PricingUnitType;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.proxy.ItemServiceProxy;

@Component
public class MarkupDTOBuilder {

    @Autowired
    private ItemServiceProxy itemServiceProxy;

    public List<ProductTypeMarkupDTO> buildDefaultProductMarkupDTOList(String cmgCustomerId, Date pricingEffectiveDate, Date pricingExpirationDate) {

        List<ProductTypeMarkupDTO> allProductTypeMarkupDTOs = new ArrayList<>();

        Map<Integer, String> allProductTypesById = itemServiceProxy.getAllProductTypesById();

        for (Entry<Integer, String> allProductTypeEntry : allProductTypesById.entrySet()) {
            allProductTypeMarkupDTOs.add(buildDefaultProductMarkupDTO(cmgCustomerId, allProductTypeEntry.getKey(), allProductTypeEntry.getValue(),
                    pricingEffectiveDate, pricingExpirationDate));
        }
        return allProductTypeMarkupDTOs;

    }

    public String formatAmount(String markupAmount) {
        DecimalFormat formatter = new DecimalFormat(CPPConstants.AMOUNT_FORMAT);
        return formatter.format(Double.parseDouble(markupAmount));
    }

    public ItemLevelMarkupDTO buildItemLevelMarkupDTO(ItemLevelMarkupDTO itemLevelMarkupDTO, String itemId, boolean noItemId,
            Date pricingEffectiveDate, Date pricingExpirationDate) {
        itemLevelMarkupDTO.setItemId(itemId);
        itemLevelMarkupDTO.setStockingCode(itemLevelMarkupDTO.getStockingCode());
        itemLevelMarkupDTO.setIsItemSaved(true);
        itemLevelMarkupDTO.setMarkup(formatAmount(itemLevelMarkupDTO.getMarkup()));
        itemLevelMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        itemLevelMarkupDTO.setExpirationDate(pricingExpirationDate);
        itemLevelMarkupDTO.setNoItemId(noItemId);
        itemLevelMarkupDTO.setInactive(false);
        itemLevelMarkupDTO.setInvalid(false);
        return itemLevelMarkupDTO;
    }

    public ItemLevelMarkupDTO buildItemLevelMarkupDTO(ItemInformationDTO itemInformationDTO, boolean noItemId,
            ProductTypeMarkupDTO productTypeMarkupDTO) {
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setItemDesc(itemInformationDTO.getItemDescription());
        itemLevelMarkupDTO.setItemId(String.valueOf(productTypeMarkupDTO.getItemPriceId()));
        itemLevelMarkupDTO.setStockingCode(Integer.parseInt(itemInformationDTO.getStockingCode()));
        itemLevelMarkupDTO.setIsItemSaved(true);
        itemLevelMarkupDTO.setMarkup(formatAmount(productTypeMarkupDTO.getMarkup()));
        itemLevelMarkupDTO.setEffectiveDate(productTypeMarkupDTO.getEffectiveDate());
        itemLevelMarkupDTO.setExpirationDate(productTypeMarkupDTO.getExpirationDate());
        itemLevelMarkupDTO.setNoItemId(noItemId);
        itemLevelMarkupDTO.setUnit(productTypeMarkupDTO.getUnit());
        itemLevelMarkupDTO.setMarkupType(productTypeMarkupDTO.getMarkupType());
        itemLevelMarkupDTO.setInactive(false);
        itemLevelMarkupDTO.setInvalid(false);
        return itemLevelMarkupDTO;
    }

    public SubgroupMarkupDTO buildSubgroupMarkupDTO(ProductTypeMarkupDTO productTypeMarkupDTO, String subgroupDesc, Date pricingEffectiveDate,
            Date pricingExpirationDate) {

        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setSubgroupDesc(subgroupDesc);
        subgroupMarkupDTO.setSubgroupId(String.valueOf(productTypeMarkupDTO.getItemPriceId()));
        subgroupMarkupDTO.setIsSubgroupSaved(true);
        subgroupMarkupDTO.setMarkup(formatAmount(productTypeMarkupDTO.getMarkup()));
        subgroupMarkupDTO.setUnit(productTypeMarkupDTO.getUnit());
        subgroupMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        subgroupMarkupDTO.setExpirationDate(pricingExpirationDate);
        subgroupMarkupDTO.setMarkupType(productTypeMarkupDTO.getMarkupType());

        return subgroupMarkupDTO;
    }

    public ItemLevelMarkupDTO buildDefaultItemLevelMarkupDTO(Date pricingExpirationDate, Date pricingEffectiveDate) {
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        itemLevelMarkupDTO.setExpirationDate(pricingExpirationDate);
        itemLevelMarkupDTO.setItemDesc(StringUtils.EMPTY);
        itemLevelMarkupDTO.setItemId(StringUtils.EMPTY);
        itemLevelMarkupDTO.setMarkup(StringUtils.EMPTY);
        itemLevelMarkupDTO.setMarkupType(PricingUnitType.CASE.getCode());
        itemLevelMarkupDTO.setUnit(AmountType.DOLLAR.getCode());
        itemLevelMarkupDTO.setNoItemId(false);
        itemLevelMarkupDTO.setStockingCode(CPPConstants.INDICATOR_ZERO);
        itemLevelMarkupDTO.setInactive(false);
        itemLevelMarkupDTO.setInvalid(false);
        itemLevelMarkupDTO.setIsItemSaved(false);
        return itemLevelMarkupDTO;
    }

    public SubgroupMarkupDTO buildDefaultSubgroupMarkupDTO(Date pricingExpirationDate, Date pricingEffectiveDate) {
        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        subgroupMarkupDTO.setExpirationDate(pricingExpirationDate);
        subgroupMarkupDTO.setSubgroupDesc(StringUtils.EMPTY);
        subgroupMarkupDTO.setSubgroupId(StringUtils.EMPTY);
        subgroupMarkupDTO.setMarkup(StringUtils.EMPTY);
        subgroupMarkupDTO.setMarkupType(PricingUnitType.CASE.getCode());
        subgroupMarkupDTO.setUnit(AmountType.DOLLAR.getCode());
        subgroupMarkupDTO.setIsSubgroupSaved(false);
        return subgroupMarkupDTO;
    }

    private ProductTypeMarkupDTO buildDefaultProductMarkupDTO(String cmgCustomerId, Integer productId, String productType, Date pricingEffectiveDate,
            Date pricingExpirationDate) {
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setMarkup(StringUtils.EMPTY);
        productTypeMarkupDTO.setGfsCustomerId(cmgCustomerId);
        productTypeMarkupDTO.setUnit(AmountType.DOLLAR.getCode());
        productTypeMarkupDTO.setItemPriceId(productId);
        productTypeMarkupDTO.setProductType(productType);
        productTypeMarkupDTO.setMarkupType(PricingUnitType.CASE.getCode());
        productTypeMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        productTypeMarkupDTO.setExpirationDate(pricingExpirationDate);
        return productTypeMarkupDTO;
    }

}