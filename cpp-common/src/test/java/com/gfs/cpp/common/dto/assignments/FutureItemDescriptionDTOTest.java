package com.gfs.cpp.common.dto.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;

@RunWith(MockitoJUnitRunner.class)
public class FutureItemDescriptionDTOTest {

    @InjectMocks
    private FutureItemDescriptionDTO dto;

    @Test
    public void testMethods() {
        dto.setGfsCustomerId("1234");
        dto.setGfsCustomerTypeCode(31);
        dto.setFutureItemDesc("Test");
        dto.setCustomerItemDescSeq(1);
        dto.setCostMarkupAmt(1);
        dto.setMarkupAmountTypeCode("TypeCode");
        dto.setMarkupUnitTypeCode(2);

        final FutureItemDescriptionDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getGfsCustomerId(), is(actual.getGfsCustomerId()));
        assertThat(dto.getGfsCustomerTypeCode(), is(actual.getGfsCustomerTypeCode()));
        assertThat(dto.getFutureItemDesc(), is(actual.getFutureItemDesc()));
        assertThat(dto.getCustomerItemDescSeq(), is(actual.getCustomerItemDescSeq()));
        
        
        assertThat(dto.getCostMarkupAmt(), is(actual.getCostMarkupAmt()));
        assertThat(dto.getMarkupAmountTypeCode(), is(actual.getMarkupAmountTypeCode()));
        assertThat(dto.getMarkupUnitTypeCode(), is(actual.getMarkupUnitTypeCode()));
    }

}
