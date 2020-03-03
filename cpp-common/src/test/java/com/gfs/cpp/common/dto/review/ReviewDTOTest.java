package com.gfs.cpp.common.dto.review;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingReviewDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.review.ReviewDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;

@RunWith(MockitoJUnitRunner.class)
public class ReviewDTOTest {

    @InjectMocks
    private ReviewDTO dto;

    @Test
    public void testMethods() {
        ContractPricingReviewDTO contractPricingReviewDTO = null;
        SplitCaseReviewDTO splitCaseReviewDTO = null;
        MarkupReviewDTO markupReviewDTO = null;
        List<String> distributionCenter = null;
        dto.setContractPricingReviewDTO(contractPricingReviewDTO);
        dto.setDistributionCenter(distributionCenter);
        dto.setMarkupReviewDTO(markupReviewDTO);
        dto.setSplitCaseReviewDTO(splitCaseReviewDTO);

        final ReviewDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getContractPricingReviewDTO(), is(dto.getContractPricingReviewDTO()));
        assertThat(actual.getDistributionCenter(), is(dto.getDistributionCenter()));
        assertThat(actual.getMarkupReviewDTO(), is(dto.getMarkupReviewDTO()));
        assertThat(actual.getSplitCaseReviewDTO(), is(dto.getSplitCaseReviewDTO()));
    }
}
