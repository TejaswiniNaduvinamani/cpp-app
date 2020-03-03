package com.gfs.cpp.common.dto.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;

@RunWith(MockitoJUnitRunner.class)
public class ItemAssignmentWrapperDTOTest {

    @InjectMocks
    private ItemAssignmentWrapperDTO dto;

    @Test
    public void testMethods() {
        dto.setExceptionName("Test");
        dto.setFutureItemDesc("Test");
        dto.setIsFutureItemSaved(false);
        dto.setGfsCustomerId("1");
        dto.setGfsCustomerTypeCode(22);
        dto.setContractPriceProfileSeq(1);
        dto.setCppFurtheranceSeq(100);
        dto.setItemAssignmentList(new ArrayList<ItemAssignmentDTO>());

        final ItemAssignmentWrapperDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getExceptionName(), is(actual.getExceptionName()));
        assertThat(dto.getFutureItemDesc(), is(actual.getFutureItemDesc()));
        assertThat(dto.getItemAssignmentList(), is(actual.getItemAssignmentList()));
        assertThat(dto.getIsFutureItemSaved(), is(actual.getIsFutureItemSaved()));
        assertThat(dto.getGfsCustomerId(), is(actual.getGfsCustomerId()));
        assertThat(dto.getGfsCustomerTypeCode(), is(actual.getGfsCustomerTypeCode()));
        assertThat(dto.getContractPriceProfileSeq(), is(actual.getContractPriceProfileSeq()));
        assertThat(dto.getCppFurtheranceSeq(), is(actual.getCppFurtheranceSeq()));
    }
}
