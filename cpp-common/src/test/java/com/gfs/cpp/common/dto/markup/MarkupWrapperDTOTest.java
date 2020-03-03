package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;

@RunWith(MockitoJUnitRunner.class)
public class MarkupWrapperDTOTest {

    @InjectMocks
    private MarkupWrapperDTO dto;

    @Test
    public void testMethods() {
        dto.setGfsCustomerId("1");
        dto.setMarkupName("Test");
        dto.setIsMarkupSaved(Boolean.FALSE);
        dto.setProductMarkupList(new ArrayList<ProductTypeMarkupDTO>());
        dto.setItemLevelMarkupList(new ArrayList<ItemLevelMarkupDTO>());
        dto.setContractPriceProfileSeq(1);
        dto.setGfsCustomerType(31);
        dto.setCppFurtheranceSeq(1);
        dto.setSubgroupMarkupList(new ArrayList<SubgroupMarkupDTO>());

        final MarkupWrapperDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(Boolean.TRUE));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(Boolean.TRUE));
        assertThat(actual.getIsMarkupSaved(), is(dto.getIsMarkupSaved()));
        assertThat(actual.getProductMarkupList(), is(dto.getProductMarkupList()));
        assertThat(actual.getGfsCustomerId(), is(dto.getGfsCustomerId()));
        assertThat(actual.getCppFurtheranceSeq(), is(dto.getCppFurtheranceSeq()));
        assertThat(actual.getMarkupName(), is(dto.getMarkupName()));
        assertThat(actual.getItemLevelMarkupList(), is(dto.getItemLevelMarkupList()));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getGfsCustomerType(), is(dto.getGfsCustomerType()));
        assertThat(actual.getSubgroupMarkupList(), is(dto.getSubgroupMarkupList()));

    }
}
