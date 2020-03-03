package com.gfs.cpp.component.document.documentgenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.util.MarkupTypeEnum;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.data.review.ReviewRepository;

@Component
public class MarkupContractLanguageHelper {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    public void setMarkupTypeLanguage(MarkupReviewDTO markupDTO, int contractPriceProfileSeq) {

        String cppAttributeColumn = null;
        Integer pricingOverrideInd = getPricingOverrideInd(contractPriceProfileSeq);
        if (pricingOverrideInd == null) {
            markupDTO.setMarkupBasedOnSell(CPPConstants.NO);
            markupDTO.setPricingOverrideInd(false);
            cppAttributeColumn = CPPConstants.ZERO_INDICATOR;
        } else {
            markupDTO.setMarkupBasedOnSell(CPPConstants.YES);
            markupDTO.setPricingOverrideInd(true);
            cppAttributeColumn = CPPConstants.ONE_INDICATOR;
        }

        int contractLanguageSeqForMarkupBasedOnSell = reviewRepository.fetchContractLanguageSeq(CPPConstants.MARKUP_BASED_ON_SELL);
        markupDTO.setMarkupBasedOnSellContractLanguage1(reviewRepository.fetchContractLanguage(contractLanguageSeqForMarkupBasedOnSell, cppAttributeColumn));
        markupDTO.setMarkupBasedOnSellContractLanguage2(reviewRepository.fetchContractLanguageMarkup());

        int contractLanguageSeqForMarkupType = reviewRepository.fetchContractLanguageSeq(CPPConstants.MARKUP_TYPE);

        for (MarkupGridDTO markupGridDTO : markupDTO.getMarkupGridDTOs()) {
            setMarkupTypeContractLanguageForProduct(markupDTO, contractLanguageSeqForMarkupType, markupGridDTO);
            setMarkupTypeContractLanguageForItem(markupDTO, contractLanguageSeqForMarkupType, markupGridDTO);
            setMarkupTypeContractLanguageForSubgroup(markupDTO, contractLanguageSeqForMarkupType, markupGridDTO);
        }
    }

    void setMarkupTypeContractLanguageForProduct(MarkupReviewDTO markupDTO, int contractLanguageSeqForMarkupType, MarkupGridDTO markupGridDTO) {

        boolean hasMarkupTypeSellUnit = false;
        boolean hasMarkupTypePerCase = false;
        boolean hasMarkupTypePerWeight = false;

        for (ProductTypeMarkupDTO productTypeMarkup : markupGridDTO.getProductTypeMarkups()) {
            if (productTypeMarkup.getMarkupType() == MarkupTypeEnum.SELL_UNIT.getMarkupType()) {
                hasMarkupTypeSellUnit = true;
            } else if (productTypeMarkup.getMarkupType() == MarkupTypeEnum.PER_CASE.getMarkupType()) {
                hasMarkupTypePerCase = true;
            } else if (productTypeMarkup.getMarkupType() == MarkupTypeEnum.PER_WEIGHT.getMarkupType()) {
                hasMarkupTypePerWeight = true;
            }
        }

        setMarkupTypeContractLanguage(markupDTO, contractLanguageSeqForMarkupType, hasMarkupTypeSellUnit, hasMarkupTypePerCase,
                hasMarkupTypePerWeight);
    }

    void setMarkupTypeContractLanguageForItem(MarkupReviewDTO markupDTO, int contractLanguageSeqForMarkupType, MarkupGridDTO markupGridDTO) {

        boolean hasMarkupTypeSellUnit = false;
        boolean hasMarkupTypePerCase = false;
        boolean hasMarkupTypePerWeight = false;

        for (ItemLevelMarkupDTO itemLevelMarkup : markupGridDTO.getItemMarkups()) {
            if (itemLevelMarkup.getMarkupType() == MarkupTypeEnum.SELL_UNIT.getMarkupType()) {
                hasMarkupTypeSellUnit = true;
            } else if (itemLevelMarkup.getMarkupType() == MarkupTypeEnum.PER_CASE.getMarkupType()) {
                hasMarkupTypePerCase = true;
            } else if (itemLevelMarkup.getMarkupType() == MarkupTypeEnum.PER_WEIGHT.getMarkupType()) {
                hasMarkupTypePerWeight = true;
            }
        }

        setMarkupTypeContractLanguage(markupDTO, contractLanguageSeqForMarkupType, hasMarkupTypeSellUnit, hasMarkupTypePerCase,
                hasMarkupTypePerWeight);
    }

    void setMarkupTypeContractLanguageForSubgroup(MarkupReviewDTO markupDTO, int contractLanguageSeqForMarkupType, MarkupGridDTO markupGridDTO) {

        boolean hasMarkupTypeSellUnit = false;
        boolean hasMarkupTypePerCase = false;
        boolean hasMarkupTypePerWeight = false;

        for (SubgroupMarkupDTO subgroupLevelMarkup : markupGridDTO.getSubgroupMarkups()) {

            if (subgroupLevelMarkup.getMarkupType() == MarkupTypeEnum.SELL_UNIT.getMarkupType()) {
                hasMarkupTypeSellUnit = true;
            } else if (subgroupLevelMarkup.getMarkupType() == MarkupTypeEnum.PER_CASE.getMarkupType()) {
                hasMarkupTypePerCase = true;
            } else if (subgroupLevelMarkup.getMarkupType() == MarkupTypeEnum.PER_WEIGHT.getMarkupType()) {
                hasMarkupTypePerWeight = true;
            }
            if (hasMarkupTypeSellUnit && hasMarkupTypePerCase && hasMarkupTypePerWeight) {
                break;
            }
        }

        setMarkupTypeContractLanguage(markupDTO, contractLanguageSeqForMarkupType, hasMarkupTypeSellUnit, hasMarkupTypePerCase,
                hasMarkupTypePerWeight);

    }

    void setMarkupTypeContractLanguage(MarkupReviewDTO markupDTO, int contractLanguageSeqForMarkupType, boolean hasMarkupTypeSellUnit,
            boolean hasMarkupTypePerCase, boolean hasMarkupTypePerWeight) {
        if (hasMarkupTypeSellUnit) {
            markupDTO.setMarkupTypeDefinitionSellUnitLanguage(
                    reviewRepository.fetchContractLanguage(contractLanguageSeqForMarkupType, CPPConstants.ONE_INDICATOR));
        }
        if (hasMarkupTypePerCase) {
            markupDTO.setMarkupTypeDefinitionPerCaseLanguage(
                    reviewRepository.fetchContractLanguage(contractLanguageSeqForMarkupType, CPPConstants.TWO_INDICATOR));
        }
        if (hasMarkupTypePerWeight) {
            markupDTO.setMarkupTypeDefinitionPerWeightLanguage(
                    reviewRepository.fetchContractLanguage(contractLanguageSeqForMarkupType, CPPConstants.THREE_INDICATOR));
        }
    }

    private Integer getPricingOverrideInd(int contractPriceProfileSeq) {
        return prcProfPricingRuleOvrdRepository.fetchPrcProfOvrdInd(contractPriceProfileSeq);
    }
}
