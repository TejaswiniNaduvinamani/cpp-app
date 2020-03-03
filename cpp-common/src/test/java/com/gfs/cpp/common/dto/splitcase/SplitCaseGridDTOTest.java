package com.gfs.cpp.common.dto.splitcase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;

@RunWith(MockitoJUnitRunner.class)
public class SplitCaseGridDTOTest {

    @InjectMocks
    private SplitCaseDTO dto;

    @Test
    public void testSplitCaseGridDTO() {
        dto.setEffectiveDate(new Date());
        dto.setExpirationDate(new Date());
        dto.setItemPriceId("1");
        dto.setProductType("test");
        dto.setSplitCaseFee("21");
        dto.setUnit("$");
        dto.setLessCaseRuleId(1);

        final SplitCaseDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getEffectiveDate(), is(dto.getEffectiveDate()));
        assertThat(actual.getExpirationDate(), is(dto.getExpirationDate()));
        assertThat(actual.getItemPriceId(), is(dto.getItemPriceId()));
        assertThat(actual.getProductType(), is(dto.getProductType()));
        assertThat(actual.getSplitCaseFee(), is(dto.getSplitCaseFee()));
        assertThat(actual.getUnit(), is(dto.getUnit()));
        assertThat(actual.getLessCaseRuleId(), is(dto.getLessCaseRuleId()));
    }
}
