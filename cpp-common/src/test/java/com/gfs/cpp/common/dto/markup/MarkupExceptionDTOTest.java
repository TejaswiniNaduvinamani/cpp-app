package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.MarkupExceptionDTO;

@RunWith(MockitoJUnitRunner.class)
public class MarkupExceptionDTOTest {

    @InjectMocks
    private MarkupExceptionDTO dto;

    @Test
    public void testMethods() {
        dto.setContractPriceProfileSeq(2);
        dto.setExceptionName("Test");

        final MarkupExceptionDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(Boolean.TRUE));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(Boolean.TRUE));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getExceptionName(), is(dto.getExceptionName()));
    }
}
