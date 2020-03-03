package com.gfs.cpp.component.furtherance;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.furtherance.FurtheranceDocumentDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.component.review.helper.MarkupReviewBuilder;
import com.gfs.cpp.component.splitcase.helper.SplitCaseReviewDTOBuilder;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceDocumentDataFetcherTest {

    @InjectMocks
    private FurtheranceDocumentDataFetcher target;

    @Mock
    private MarkupReviewBuilder markupReviewBuilder;

    @Mock
    private SplitCaseReviewDTOBuilder splitCaseReviewDTOBuilder;

    @Mock
    private CppFurtheranceRepository cppFurtheranceRepository;

    @Test
    public void shouldFetchDocumentDataForFurtherance() throws Exception {

        int cppFurtheranceSeq = 101;
        int contractPriceProfileSeq = -202;

        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);

        MarkupReviewDTO markupDTO = new MarkupReviewDTO();
        SplitCaseReviewDTO splitCaseDTO = new SplitCaseReviewDTO();

        doReturn(furtheranceInformationDTO).when(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
        doReturn(markupDTO).when(markupReviewBuilder).buildFetchMarkupDTO(contractPriceProfileSeq, null, null);
        doReturn(splitCaseDTO).when(splitCaseReviewDTOBuilder).buildSplitCaseReviewDTO(contractPriceProfileSeq);

        FurtheranceDocumentDTO actual = target.fetchFurtheranceDocumentData(cppFurtheranceSeq);

        assertThat(actual.getFurtheranceInformationDTO(), equalTo(furtheranceInformationDTO));
        assertThat(actual.getMarkupDTO(), equalTo(markupDTO));
        assertThat(actual.getSplitCaseDTO(), equalTo(splitCaseDTO));

        verify(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
        verify(markupReviewBuilder).buildFetchMarkupDTO(contractPriceProfileSeq, null, null);
        verify(splitCaseReviewDTOBuilder).buildSplitCaseReviewDTO(contractPriceProfileSeq);

    }

}
