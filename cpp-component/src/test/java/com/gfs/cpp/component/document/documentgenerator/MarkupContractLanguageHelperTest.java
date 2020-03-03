package com.gfs.cpp.component.document.documentgenerator;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.component.document.documentgenerator.MarkupContractLanguageHelper;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.data.review.ReviewRepository;

@RunWith(MockitoJUnitRunner.class)
public class MarkupContractLanguageHelperTest {

    @InjectMocks
    private MarkupContractLanguageHelper target;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Test
    public void shouldSetMarkupTypeLanguage() {
        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();

        when(reviewRepository.fetchContractLanguageSeq(CPPConstants.MARKUP_BASED_ON_SELL)).thenReturn(1);
        when(reviewRepository.fetchContractLanguageSeq(CPPConstants.MARKUP_TYPE)).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(1, "1")).thenReturn("");
        when(prcProfPricingRuleOvrdRepository.fetchPrcProfOvrdInd(1)).thenReturn(null);

        target.setMarkupTypeLanguage(markupDTO, 1);

        verify(reviewRepository).fetchContractLanguageSeq(CPPConstants.MARKUP_BASED_ON_SELL);
        verify(reviewRepository).fetchContractLanguageSeq(CPPConstants.MARKUP_TYPE);
        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "1");
        verify(reviewRepository).fetchContractLanguageMarkup();
        verify(prcProfPricingRuleOvrdRepository).fetchPrcProfOvrdInd(1);

    }

    @Test
    public void shouldSetMarkupTypeLanguageTrueIndicator() {
        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        markupDTO.setPricingOverrideInd(true);
        when(reviewRepository.fetchContractLanguageSeq(CPPConstants.MARKUP_BASED_ON_SELL)).thenReturn(1);
        when(reviewRepository.fetchContractLanguageSeq(CPPConstants.MARKUP_TYPE)).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(1, "1")).thenReturn("");
        when(prcProfPricingRuleOvrdRepository.fetchPrcProfOvrdInd(1)).thenReturn(1);

        target.setMarkupTypeLanguage(markupDTO, 1);

        verify(reviewRepository).fetchContractLanguageSeq(CPPConstants.MARKUP_BASED_ON_SELL);
        verify(reviewRepository).fetchContractLanguageSeq(CPPConstants.MARKUP_TYPE);
        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "1");
        verify(reviewRepository).fetchContractLanguageMarkup();
        verify(prcProfPricingRuleOvrdRepository).fetchPrcProfOvrdInd(1);

    }

    @Test
    public void shouldSetMarkupTypeContractLanguageForProduct() {
        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        when(reviewRepository.fetchContractLanguageSeq("Test")).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(1, "Test")).thenReturn("");

        target.setMarkupTypeContractLanguageForProduct(markupDTO, 1, markupDTO.getMarkupGridDTOs().get(0));

        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "1");
        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "2");
        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "3");

    }

    @Test
    public void shouldSetMarkupTypeContractLanguageForItem() {
        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        markupDTO.setPricingOverrideInd(true);
        when(reviewRepository.fetchContractLanguageSeq("Test")).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(1, "Test")).thenReturn("");

        target.setMarkupTypeContractLanguageForItem(markupDTO, 1, markupDTO.getMarkupGridDTOs().get(0));

        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "1");
        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "2");
        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "3");

    }

    @Test
    public void shouldSetMarkupTypeContractLanguageForSubgroup() {
        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        markupDTO.setPricingOverrideInd(true);
        when(reviewRepository.fetchContractLanguageSeq("Test")).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(1, "Test")).thenReturn("");

        target.setMarkupTypeContractLanguageForSubgroup(markupDTO, 1, markupDTO.getMarkupGridDTOs().get(0));

        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "1");
        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "2");
        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(1, "3");

    }

    private MarkupReviewDTO shouldBuildMarkupDTO() {
        MarkupGridDTO markupGridDTO = new MarkupGridDTO();
        ProductTypeMarkupDTO productTypeMarkupDTOOne = new ProductTypeMarkupDTO();
        productTypeMarkupDTOOne.setItemPriceId(1);
        productTypeMarkupDTOOne.setGfsCustomerId("1");
        productTypeMarkupDTOOne.setMarkup("5");
        productTypeMarkupDTOOne.setMarkupType(1);
        productTypeMarkupDTOOne.setUnit("%");
        ProductTypeMarkupDTO productTypeMarkupDTOTwo = new ProductTypeMarkupDTO();
        productTypeMarkupDTOTwo.setItemPriceId(1);
        productTypeMarkupDTOTwo.setGfsCustomerId("1");
        productTypeMarkupDTOTwo.setMarkup("5");
        productTypeMarkupDTOTwo.setMarkupType(2);
        productTypeMarkupDTOTwo.setUnit("%");
        ProductTypeMarkupDTO productTypeMarkupDTOThree = new ProductTypeMarkupDTO();
        productTypeMarkupDTOThree.setItemPriceId(1);
        productTypeMarkupDTOThree.setGfsCustomerId("1");
        productTypeMarkupDTOThree.setMarkup("5");
        productTypeMarkupDTOThree.setMarkupType(3);
        productTypeMarkupDTOThree.setUnit("%");
        List<ProductTypeMarkupDTO> markup = new ArrayList<>();
        List<ItemLevelMarkupDTO> item = new ArrayList<>();
        List<SubgroupMarkupDTO> subgroupMarkupList = new ArrayList<>();
        ItemLevelMarkupDTO itemLevelMarkupDTOOne = new ItemLevelMarkupDTO();
        itemLevelMarkupDTOOne.setMarkupType(1);
        ItemLevelMarkupDTO itemLevelMarkupDTOTwo = new ItemLevelMarkupDTO();
        itemLevelMarkupDTOTwo.setMarkupType(2);
        ItemLevelMarkupDTO itemLevelMarkupDTOThree = new ItemLevelMarkupDTO();
        itemLevelMarkupDTOThree.setMarkupType(3);
        item.add(itemLevelMarkupDTOOne);
        item.add(itemLevelMarkupDTOTwo);
        item.add(itemLevelMarkupDTOThree);
        markup.add(productTypeMarkupDTOOne);
        markup.add(productTypeMarkupDTOTwo);
        markup.add(productTypeMarkupDTOThree);
        markupGridDTO.setProductTypeMarkups(markup);
        markupGridDTO.setItemMarkups(item);
        SubgroupMarkupDTO subgroupDTOOne = new SubgroupMarkupDTO();
        subgroupDTOOne.setMarkupType(1);
        SubgroupMarkupDTO subgroupDTOTwo = new SubgroupMarkupDTO();
        subgroupDTOTwo.setMarkupType(2);
        SubgroupMarkupDTO subgroupDTOThree = new SubgroupMarkupDTO();
        subgroupDTOThree.setMarkupType(3);
        subgroupMarkupList.add(subgroupDTOOne);
        subgroupMarkupList.add(subgroupDTOTwo);
        subgroupMarkupList.add(subgroupDTOThree);
        markupGridDTO.setSubgroupMarkups(subgroupMarkupList);
        List<MarkupGridDTO> markupGridDTOs = new ArrayList<>();
        markupGridDTOs.add(markupGridDTO);
        MarkupReviewDTO markupDTO = new MarkupReviewDTO();
        markupDTO.setPricingOverrideInd(false);
        markupDTO.setMarkupBasedOnSell(CPPConstants.NO);
        markupDTO.setMarkupGridDTOs(markupGridDTOs);
        return markupDTO;
    }
}
