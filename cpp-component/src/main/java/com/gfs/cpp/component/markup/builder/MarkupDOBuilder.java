package com.gfs.cpp.component.markup.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.corp.component.price.common.types.PriceLockinReason;
import com.gfs.corp.component.price.common.types.PriceLockinType;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.model.markup.FutureItemDO;
import com.gfs.cpp.common.model.markup.MarkupWrapperDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;

@Component
public class MarkupDOBuilder {

    @Autowired
    private CPPDateUtils cppDateUtils;

    public MarkupWrapperDO buildMarkupWrapperDO(MarkupWrapperDTO markupWrapper, int contractPriceProfileSeq, String userName) {
        List<ProductTypeMarkupDO> markupList = new ArrayList<>();
        List<FutureItemDO> futureItemList = new ArrayList<>();
        for (ProductTypeMarkupDTO productTypeMarkupToSave : markupWrapper.getProductMarkupList()) {
            ProductTypeMarkupDO productTypeMarkupDO = buildProductTypeMarkupDO(markupWrapper, productTypeMarkupToSave);
            markupList.add(productTypeMarkupDO);
        }
        for (ItemLevelMarkupDTO itemLeveMarkupToSave : markupWrapper.getItemLevelMarkupList()) {
            if (!itemLeveMarkupToSave.isNoItemId()) {
                ProductTypeMarkupDO markupDO = buildItemLevelMarkupDO(markupWrapper, itemLeveMarkupToSave);
                markupList.add(markupDO);
            } else {
                FutureItemDO futureItem = buildFutureItemDO(markupWrapper, contractPriceProfileSeq, itemLeveMarkupToSave);
                futureItemList.add(futureItem);
            }
        }
        for (SubgroupMarkupDTO subgroupMarkuMarkupToSave : markupWrapper.getSubgroupMarkupList()) {
            ProductTypeMarkupDO subgroupMarkupDO = buildSubgroupMarkupDO(markupWrapper.getGfsCustomerId(), markupWrapper.getGfsCustomerType(),
                    subgroupMarkuMarkupToSave);
            markupList.add(subgroupMarkupDO);
        }
        return buildMarkupWrapperDO(contractPriceProfileSeq, userName, markupList, futureItemList);
    }

    public List<ProductTypeMarkupDO> buildProductMarkupDOListForAmendment(String userId, int cppSeq, Date farOutDate,
            List<ProductTypeMarkupDTO> productTypeMarkupList) {
        List<ProductTypeMarkupDO> productTypeMarkupDOList = null;
        if (productTypeMarkupList != null && !productTypeMarkupList.isEmpty()) {
            productTypeMarkupDOList = new ArrayList<>(productTypeMarkupList.size());
            for (ProductTypeMarkupDTO productTypeMarkupDTO : productTypeMarkupList) {
                ProductTypeMarkupDO productTypeMarkupDO = buildProductMarkupDO(userId, productTypeMarkupDTO);
                productTypeMarkupDO.setContractPriceProfileSeq(cppSeq);
                productTypeMarkupDO.setEffectiveDate(farOutDate);
                productTypeMarkupDO.setExpirationDate(farOutDate);
                productTypeMarkupDOList.add(productTypeMarkupDO);
            }
        }
        return productTypeMarkupDOList;
    }

    public List<ProductTypeMarkupDO> buildProductMarkupDOList(String userId, Date pricingEffectiveDate, Date pricingExpirationDate,
            List<ProductTypeMarkupDTO> productTypeMarkupList) {
        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();
        if (productTypeMarkupList != null && !productTypeMarkupList.isEmpty()) {
            for (ProductTypeMarkupDTO productTypeMarkupDTO : productTypeMarkupList) { // saveMarkup
                ProductTypeMarkupDO productTypeMarkupDO = buildProductMarkupDO(userId, productTypeMarkupDTO);
                productTypeMarkupDO.setContractPriceProfileSeq(productTypeMarkupDTO.getContractPriceProfileSeq());
                productTypeMarkupDO.setEffectiveDate(pricingEffectiveDate);
                productTypeMarkupDO.setExpirationDate(pricingExpirationDate);
                productTypeMarkupDOList.add(productTypeMarkupDO);
            }
        }
        return productTypeMarkupDOList;
    }

    public List<ProductTypeMarkupDO> buildMarkupDOListForAssignment(ItemAssignmentWrapperDTO itemAssignmentWrapperDTO,
            ItemLevelMarkupDTO futureItemDTO) {
        List<ProductTypeMarkupDO> markupDOList = new ArrayList<>();
        for (ItemAssignmentDTO itemAssignmentDTO : itemAssignmentWrapperDTO.getItemAssignmentList()) {
            ProductTypeMarkupDO markupDO = buildMarkupDO(itemAssignmentWrapperDTO.getGfsCustomerId(),
                    itemAssignmentWrapperDTO.getGfsCustomerTypeCode(), futureItemDTO, itemAssignmentDTO.getItemId());
            markupDOList.add(markupDO);
        }
        return markupDOList;
    }

    public ProductTypeMarkupDO buildMarkupDO(String gfsCustomerId, int gfsCustomerTypeCode, ItemLevelMarkupDTO itemLevelMarkup, String itemId) {
        ProductTypeMarkupDO markupDO = new ProductTypeMarkupDO();
        markupDO.setCustomerTypeCode(gfsCustomerTypeCode);
        markupDO.setGfsCustomerId(gfsCustomerId);
        Date futureDate = cppDateUtils.getFutureDate();
        markupDO.setEffectiveDate(futureDate);
        markupDO.setExpirationDate(futureDate);
        markupDO.setItemPriceId(Integer.parseInt(itemId));
        markupDO.setPriceLockedInTypeCode(PriceLockinType.COST_PLUS.getCode());
        markupDO.setPriceLockinReasonCode(PriceLockinReason.COMPETITIVE.code);
        markupDO.setMarkup(new BigDecimal(itemLevelMarkup.getMarkup()).setScale(CPPConstants.INDICATOR_TWO, BigDecimal.ROUND_HALF_UP));
        markupDO.setMarkupType(itemLevelMarkup.getMarkupType());
        markupDO.setProductType(String.valueOf(ItemPriceLevel.ITEM.getCode()));
        markupDO.setUnit(itemLevelMarkup.getUnit());
        return markupDO;
    }

    private MarkupWrapperDO buildMarkupWrapperDO(int contractPriceProfileSeq, String userName, List<ProductTypeMarkupDO> markupList,
            List<FutureItemDO> futureItemList) {
        MarkupWrapperDO markupWrapperDO = new MarkupWrapperDO();
        markupWrapperDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapperDO.setMarkupList(markupList);
        markupWrapperDO.setUserName(userName);
        markupWrapperDO.setFutureItemList(futureItemList);
        return markupWrapperDO;
    }

    private FutureItemDO buildFutureItemDO(MarkupWrapperDTO markupWrapper, int contractPriceProfileSeq, ItemLevelMarkupDTO itemLeveMarkup) {
        FutureItemDO futureItemDO = new FutureItemDO();
        futureItemDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        futureItemDO.setCustomerTypeCode(markupWrapper.getGfsCustomerType());
        futureItemDO.setEffectiveDate(cppDateUtils.getFutureDate());
        futureItemDO.setExpirationDate(cppDateUtils.getFutureDate());
        futureItemDO.setGfsCustomerId(markupWrapper.getGfsCustomerId());
        futureItemDO.setItemDesc(itemLeveMarkup.getItemDesc());
        futureItemDO.setMarkup(new BigDecimal(itemLeveMarkup.getMarkup()).setScale(CPPConstants.INDICATOR_TWO, BigDecimal.ROUND_HALF_UP));
        futureItemDO.setMarkupType(itemLeveMarkup.getMarkupType());
        futureItemDO.setUnit(itemLeveMarkup.getUnit());
        return futureItemDO;
    }

    public ProductTypeMarkupDO buildItemLevelMarkupDO(MarkupWrapperDTO markupWrapper, ItemLevelMarkupDTO itemLeveMarkup) {
        ProductTypeMarkupDO itemTypeMarkupDO = new ProductTypeMarkupDO();
        itemTypeMarkupDO.setCustomerTypeCode(markupWrapper.getGfsCustomerType());
        itemTypeMarkupDO.setGfsCustomerId(markupWrapper.getGfsCustomerId());
        itemTypeMarkupDO.setEffectiveDate(cppDateUtils.getFutureDate());
        itemTypeMarkupDO.setExpirationDate(cppDateUtils.getFutureDate());
        itemTypeMarkupDO.setItemPriceId(Integer.parseInt(itemLeveMarkup.getItemId()));
        itemTypeMarkupDO.setMarkup(new BigDecimal(itemLeveMarkup.getMarkup()).setScale(CPPConstants.INDICATOR_TWO, BigDecimal.ROUND_HALF_UP));
        itemTypeMarkupDO.setMarkupType(itemLeveMarkup.getMarkupType());
        itemTypeMarkupDO.setProductType(String.valueOf(ItemPriceLevel.ITEM.getCode()));
        itemTypeMarkupDO.setUnit(itemLeveMarkup.getUnit());
        itemTypeMarkupDO.setPriceLockedInTypeCode(PriceLockinType.COST_PLUS.getCode());
        itemTypeMarkupDO.setPriceLockinReasonCode(PriceLockinReason.COMPETITIVE.code);
        return itemTypeMarkupDO;
    }

    public ProductTypeMarkupDO buildProductTypeMarkupDO(MarkupWrapperDTO markupWrapper, ProductTypeMarkupDTO productTypeMarkupToSave) {
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setCustomerTypeCode(markupWrapper.getGfsCustomerType());
        productTypeMarkupDO.setGfsCustomerId(markupWrapper.getGfsCustomerId());
        productTypeMarkupDO.setEffectiveDate(cppDateUtils.getFutureDate());
        productTypeMarkupDO.setExpirationDate(cppDateUtils.getFutureDate());
        productTypeMarkupDO.setItemPriceId(productTypeMarkupToSave.getItemPriceId());
        productTypeMarkupDO
                .setMarkup(new BigDecimal(productTypeMarkupToSave.getMarkup()).setScale(CPPConstants.INDICATOR_TWO, BigDecimal.ROUND_HALF_UP));
        productTypeMarkupDO.setMarkupType(productTypeMarkupToSave.getMarkupType());
        productTypeMarkupDO.setUnit(productTypeMarkupToSave.getUnit());
        productTypeMarkupDO.setProductType(String.valueOf(ItemPriceLevel.PRODUCT_TYPE.getCode()));
        productTypeMarkupDO.setPriceLockedInTypeCode(PriceLockinType.COST_PLUS.getCode());
        productTypeMarkupDO.setPriceLockinReasonCode(PriceLockinReason.COMPETITIVE.code);
        return productTypeMarkupDO;
    }

    public ProductTypeMarkupDO buildSubgroupMarkupDO(String gfsCustomerId, int gfsCustomerTypeCode, SubgroupMarkupDTO subgroupMarkup) {
        ProductTypeMarkupDO subgroupMarkupDO = new ProductTypeMarkupDO();
        subgroupMarkupDO.setCustomerTypeCode(gfsCustomerTypeCode);
        subgroupMarkupDO.setGfsCustomerId(gfsCustomerId);
        subgroupMarkupDO.setEffectiveDate(cppDateUtils.getFutureDate());
        subgroupMarkupDO.setExpirationDate(cppDateUtils.getFutureDate());
        subgroupMarkupDO.setItemPriceId(Integer.parseInt(subgroupMarkup.getSubgroupId()));
        subgroupMarkupDO.setMarkup(new BigDecimal(subgroupMarkup.getMarkup()).setScale(CPPConstants.INDICATOR_TWO, BigDecimal.ROUND_HALF_UP));
        subgroupMarkupDO.setMarkupType(subgroupMarkup.getMarkupType());
        subgroupMarkupDO.setProductType(String.valueOf(ItemPriceLevel.SUBGROUP.getCode()));
        subgroupMarkupDO.setUnit(subgroupMarkup.getUnit());
        subgroupMarkupDO.setPriceLockedInTypeCode(PriceLockinType.COST_PLUS.getCode());
        subgroupMarkupDO.setPriceLockinReasonCode(PriceLockinReason.COMPETITIVE.code);
        return subgroupMarkupDO;
    }

    private ProductTypeMarkupDO buildProductMarkupDO(String userId, ProductTypeMarkupDTO productTypeMarkupDTO) {
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setCustomerTypeCode(productTypeMarkupDTO.getGfsCustomerTypeCode());
        productTypeMarkupDO.setGfsCustomerId(productTypeMarkupDTO.getGfsCustomerId());
        productTypeMarkupDO.setItemPriceId(productTypeMarkupDTO.getItemPriceId());
        productTypeMarkupDO.setMarkup(new BigDecimal(productTypeMarkupDTO.getMarkup()));
        productTypeMarkupDO.setMarkupType(productTypeMarkupDTO.getMarkupType());
        productTypeMarkupDO.setProductType(productTypeMarkupDTO.getProductType());
        productTypeMarkupDO.setUnit(productTypeMarkupDTO.getUnit());
        productTypeMarkupDO.setPriceLockedInTypeCode(productTypeMarkupDTO.getPriceLockedInTypeCode());
        productTypeMarkupDO.setHoldCostFirmInd(productTypeMarkupDTO.getHoldCostFirmInd());
        productTypeMarkupDO.setPriceLockinReasonCode(productTypeMarkupDTO.getPriceLockinReasonCode());
        productTypeMarkupDO.setPriceMaintenanceSourceCode(productTypeMarkupDTO.getPriceMaintenanceSourceCode());
        productTypeMarkupDO.setCreateUserId(userId);
        productTypeMarkupDO.setLastUpdateUserId(userId);
        return productTypeMarkupDO;
    }

    public ProductTypeMarkupDO buildProductTypeMarkupDO(String gfsCustomerId, int gfsCustomerTypeCode, ItemAssignmentDTO itemAssignmentDTO,
            FutureItemDO futureItemDO) {
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setCustomerTypeCode(gfsCustomerTypeCode);
        productTypeMarkupDO.setGfsCustomerId(gfsCustomerId);
        productTypeMarkupDO.setEffectiveDate(cppDateUtils.getFutureDate());
        productTypeMarkupDO.setExpirationDate(cppDateUtils.getFutureDate());
        productTypeMarkupDO.setItemPriceId(Integer.parseInt(itemAssignmentDTO.getItemId()));
        productTypeMarkupDO.setMarkup(futureItemDO.getMarkup());
        productTypeMarkupDO.setMarkupType(futureItemDO.getMarkupType());
        productTypeMarkupDO.setUnit(futureItemDO.getUnit());
        productTypeMarkupDO.setProductType(String.valueOf(ItemPriceLevel.PRODUCT_TYPE.getCode()));
        productTypeMarkupDO.setPriceLockedInTypeCode(PriceLockinType.COST_PLUS.getCode());
        productTypeMarkupDO.setPriceLockinReasonCode(PriceLockinReason.COMPETITIVE.code);
        return productTypeMarkupDO;
    }

}
