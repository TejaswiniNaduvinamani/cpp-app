package com.gfs.cpp.component.splitcase.helper;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.component.splitcase.SplitCaseService;
import com.gfs.cpp.data.review.ReviewRepository;

@Component
public class SplitCaseReviewDTOBuilder {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private SplitCaseService splitCaseService;

    public SplitCaseReviewDTO buildSplitCaseReviewDTO(int contractPriceProfileSeq) {
        SplitCaseReviewDTO splitCaseReviewDTO = new SplitCaseReviewDTO();
        SplitCaseGridDTO splitCaseGridDTO = splitCaseService.fetchSplitCaseFee(contractPriceProfileSeq, null, null);
        if (!splitCaseGridDTO.getIsSplitCaseSaved()) {
            splitCaseReviewDTO.setSplitCaseFeeValues(Collections.<SplitCaseDTO> emptyList());
        } else {
            List<SplitCaseDTO> splitCaseDTOs = splitCaseGridDTO.getSplitCaseFeeValues();
            splitCaseReviewDTO.setSplitCaseFeeValues(splitCaseDTOs);
            splitCaseReviewDTO.setFeeType(splitCaseDTOs.get(0).getLessCaseRuleId());
            buildContractLanguageForFeeType(splitCaseReviewDTO);
        }
        return splitCaseReviewDTO;
    }

    private void buildContractLanguageForFeeType(SplitCaseReviewDTO splitCaseReviewDTO) {
        int contractLanguageSeqFeeType = reviewRepository.fetchContractLanguageSeq(CPPConstants.SPLIT_CASE_CONTRACT_LANGUAGE_COLUMN_DESC);
        splitCaseReviewDTO.setFeeTypeContractLanguage(
                reviewRepository.fetchContractLanguage(contractLanguageSeqFeeType, String.valueOf(splitCaseReviewDTO.getFeeType())));
    }

}
