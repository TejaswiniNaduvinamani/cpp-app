package com.gfs.cpp.common.dto.splitcase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;

@RunWith(MockitoJUnitRunner.class)
public class SplitCaseReviewDTOTest {

    @InjectMocks
    private SplitCaseReviewDTO dto;

    @Test
    public void testSplitCaseDTO() {
        dto.setFeeType(2);
        List<SplitCaseDTO> splitCaseFeeValues = new ArrayList<SplitCaseDTO>();
        dto.setSplitCaseFeeValues(splitCaseFeeValues);
        dto.setFeeTypeContractLanguage("Contract Language Fee Type");

        final SplitCaseReviewDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getFeeType(), is(dto.getFeeType()));
        assertThat(actual.getSplitCaseFeeValues(), is(dto.getSplitCaseFeeValues()));
        assertThat(actual.getFeeTypeContractLanguage(), is(dto.getFeeTypeContractLanguage()));
    }
}
