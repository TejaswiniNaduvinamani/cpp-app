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
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;

@RunWith(MockitoJUnitRunner.class)
public class SplitCaseDTOTest {

    @InjectMocks
    private SplitCaseGridDTO splitCaseDTO;

    @Test
    public void testSplitCaseDTO() {
        splitCaseDTO.setContractPriceProfileSeq(0);
        List<SplitCaseDTO> splitCaseFeeValues = new ArrayList<SplitCaseDTO>();
        splitCaseDTO.setSplitCaseFeeValues(splitCaseFeeValues);
        splitCaseDTO.setIsSplitCaseSaved(true);

        final SplitCaseGridDTO actual = SerializationUtils.clone(splitCaseDTO);

        assertThat(splitCaseDTO.equals(actual), is(true));
        assertThat(splitCaseDTO.hashCode(), is(actual.hashCode()));
        assertThat(splitCaseDTO.toString() != null, is(true));

        assertThat(actual.getContractPriceProfileSeq(), is(splitCaseDTO.getContractPriceProfileSeq()));
        assertThat(actual.getSplitCaseFeeValues(), is(splitCaseDTO.getSplitCaseFeeValues()));
        assertThat(actual.getIsSplitCaseSaved(), is(splitCaseDTO.getIsSplitCaseSaved()));

    }
}
