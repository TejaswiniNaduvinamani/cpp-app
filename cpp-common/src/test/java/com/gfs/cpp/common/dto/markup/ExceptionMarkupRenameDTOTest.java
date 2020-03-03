package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ExceptionMarkupRenameDTO;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionMarkupRenameDTOTest {

    @InjectMocks
    private ExceptionMarkupRenameDTO exceptionMarkupRenameDTO;

    @Test
    public void testMethods() {
        exceptionMarkupRenameDTO.setGfsCustomerId("1");
        exceptionMarkupRenameDTO.setExceptionName("Test");
        exceptionMarkupRenameDTO.setContractPriceProfileSeq(1);
        exceptionMarkupRenameDTO.setNewExceptionName("UpdatedName");

        final ExceptionMarkupRenameDTO actual = SerializationUtils.clone(exceptionMarkupRenameDTO);

        assertThat(exceptionMarkupRenameDTO.equals(actual), is(true));
        assertThat(exceptionMarkupRenameDTO.hashCode(), is(actual.hashCode()));
        assertThat(exceptionMarkupRenameDTO.toString() != null, is(true));
        assertThat(actual.getContractPriceProfileSeq(), is(exceptionMarkupRenameDTO.getContractPriceProfileSeq()));
        assertThat(actual.getNewExceptionName(), is(exceptionMarkupRenameDTO.getNewExceptionName()));
        assertThat(actual.getGfsCustomerId(), is(exceptionMarkupRenameDTO.getGfsCustomerId()));
        assertThat(actual.getExceptionName(), is(exceptionMarkupRenameDTO.getExceptionName()));
    }
}
