package com.gfs.cpp.component.splitcase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.component.splitcase.SplitCaseService;
import com.gfs.cpp.component.splitcase.helper.SplitCaseReviewDTOBuilder;
import com.gfs.cpp.data.review.ReviewRepository;

@Configuration
@RunWith(MockitoJUnitRunner.class)
public class SplitCaseReviewDTOBuilderTest {

    @InjectMocks
    private SplitCaseReviewDTOBuilder target;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private SplitCaseService splitCaseService;

    @Test
    public void shouldBuildSplitCaseReviewDTO() {
        int contractPriceProfileSeq = 1;
        Date effectiveDate = new LocalDate(9999, 01, 01).toDate();
        Date expirationDate = new LocalDate(9999, 01, 01).toDate();
        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        splitCaseDTO.setEffectiveDate(effectiveDate);
        splitCaseDTO.setExpirationDate(expirationDate);
        splitCaseDTO.setItemPriceId("1");
        splitCaseDTO.setLessCaseRuleId(2);
        splitCaseDTO.setProductType("Meal");
        splitCaseDTO.setUnit("%");
        splitCaseDTO.setSplitCaseFee(CPPConstants.SPLIT_CASE_FEE);

        List<SplitCaseDTO> splitCaseFee = new ArrayList<>();
        splitCaseFee.add(splitCaseDTO);
        SplitCaseGridDTO splitCaseGridDTO = new SplitCaseGridDTO();
        splitCaseGridDTO.setSplitCaseFeeValues(splitCaseFee);
        splitCaseGridDTO.setIsSplitCaseSaved(true);

        when(reviewRepository.fetchContractLanguageSeq(CPPConstants.SPLIT_CASE_CONTRACT_LANGUAGE_COLUMN_DESC)).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(contractPriceProfileSeq, "2")).thenReturn("Contract");
        when(splitCaseService.fetchSplitCaseFee(contractPriceProfileSeq, null, null)).thenReturn(splitCaseGridDTO);

        SplitCaseReviewDTO result = target.buildSplitCaseReviewDTO(contractPriceProfileSeq);

        assertThat(result.getFeeType(), equalTo(2));
        assertThat(result.getFeeTypeContractLanguage(), equalTo("Contract"));

        verify(reviewRepository).fetchContractLanguageSeq(CPPConstants.SPLIT_CASE_CONTRACT_LANGUAGE_COLUMN_DESC);
        verify(reviewRepository).fetchContractLanguage(contractPriceProfileSeq, "2");
        verify(splitCaseService).fetchSplitCaseFee(contractPriceProfileSeq, null, null);
    }

    @Test
    public void shouldBuildEmptySplitCaseReviewDTOWhenNoResultFoundFromDatabase() {

        int contractPriceProfileSeq = 1;
        SplitCaseGridDTO splitCaseGridDTO = new SplitCaseGridDTO();
        splitCaseGridDTO.setIsSplitCaseSaved(false);

        when(splitCaseService.fetchSplitCaseFee(contractPriceProfileSeq, null, null)).thenReturn(splitCaseGridDTO);

        SplitCaseReviewDTO result = target.buildSplitCaseReviewDTO(contractPriceProfileSeq);

        assertThat(result.getSplitCaseFeeValues().size(), equalTo(0));

        verify(splitCaseService).fetchSplitCaseFee(contractPriceProfileSeq, null, null);
    }
}
