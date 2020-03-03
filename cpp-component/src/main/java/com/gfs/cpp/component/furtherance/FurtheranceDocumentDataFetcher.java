package com.gfs.cpp.component.furtherance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.furtherance.FurtheranceDocumentDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.component.review.helper.MarkupReviewBuilder;
import com.gfs.cpp.component.splitcase.helper.SplitCaseReviewDTOBuilder;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;

@Component
public class FurtheranceDocumentDataFetcher {

    @Autowired
    private MarkupReviewBuilder markupReviewBuilder;

    @Autowired
    private SplitCaseReviewDTOBuilder splitCaseReviewDTOBuilder;

    @Autowired
    private CppFurtheranceRepository cppFurtheranceRepository;

    public FurtheranceDocumentDTO fetchFurtheranceDocumentData(int cppFurtheranceSeq) {
        FurtheranceDocumentDTO furtheranceExhibitDTO = new FurtheranceDocumentDTO();
        FurtheranceInformationDTO furtheranceInformationDTO = getFurtheranceInformationDTO(cppFurtheranceSeq);
        MarkupReviewDTO markupDTO = markupReviewBuilder.buildFetchMarkupDTO(furtheranceInformationDTO.getContractPriceProfileSeq(), null, null);
        SplitCaseReviewDTO splitCaseDTO = splitCaseReviewDTOBuilder.buildSplitCaseReviewDTO(furtheranceInformationDTO.getContractPriceProfileSeq());

        furtheranceExhibitDTO.setFurtheranceInformationDTO(furtheranceInformationDTO);
        furtheranceExhibitDTO.setMarkupDTO(markupDTO);
        furtheranceExhibitDTO.setSplitCaseDTO(splitCaseDTO);
        return furtheranceExhibitDTO;
    }

    private FurtheranceInformationDTO getFurtheranceInformationDTO(int cppFurtheranceSeq) {
        return cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
    }

}
