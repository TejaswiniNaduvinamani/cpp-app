package com.gfs.cpp.common.dto.furtherance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.furtherance.SplitCaseGridFurtheranceDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;

@RunWith(MockitoJUnitRunner.class)
public class SplitCaseGridFurtheranceDTOTest {
	
	@InjectMocks
	SplitCaseGridFurtheranceDTO target;
	
    @Test
    public void testMethod() {
    	target.setContractPriceProfileSeq(0);
        List<SplitCaseDTO> splitCaseFeeValues = new ArrayList<SplitCaseDTO>();
        target.setSplitCaseFeeValues(splitCaseFeeValues);
        target.setCppFurtheranceSeq(1);

        final SplitCaseGridFurtheranceDTO actual = SerializationUtils.clone(target);

        assertThat(target.equals(actual), is(true));
        assertThat(target.hashCode(), is(actual.hashCode()));
        assertThat(target.toString() != null, is(true));

        assertThat(actual.getContractPriceProfileSeq(), is(target.getContractPriceProfileSeq()));
        assertThat(actual.getSplitCaseFeeValues(), is(target.getSplitCaseFeeValues()));
        assertThat(actual.getCppFurtheranceSeq(), is(target.getCppFurtheranceSeq()));

    }

}
