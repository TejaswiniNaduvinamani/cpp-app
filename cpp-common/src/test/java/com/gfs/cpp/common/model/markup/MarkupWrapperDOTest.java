package com.gfs.cpp.common.model.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.markup.FutureItemDO;
import com.gfs.cpp.common.model.markup.MarkupWrapperDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;

@RunWith(MockitoJUnitRunner.class)
public class MarkupWrapperDOTest {

    @InjectMocks
    private MarkupWrapperDO dto;

    @Test
    public void testMethods() {
        dto.setContractPriceProfileSeq(3);
        dto.setFutureItemList(new ArrayList<FutureItemDO>());
        dto.setMarkupList(new ArrayList<ProductTypeMarkupDO>());
        dto.setUserName("v8cq5");

        final MarkupWrapperDO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getFutureItemList(), is(dto.getFutureItemList()));
        assertThat(actual.getMarkupList(), is(dto.getMarkupList()));
        assertThat(actual.getUserName(), is(dto.getUserName()));
    }
}
