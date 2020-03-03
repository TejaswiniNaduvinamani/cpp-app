package com.gfs.cpp.common.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.util.StatusDTO;

@RunWith(MockitoJUnitRunner.class)
public class StatusDTOTest {

    @InjectMocks
    private StatusDTO statusDTO;

    @Test
    public void testMethod() {
        statusDTO.setErrorCode(null);
        statusDTO.setErrorMessage("");
        statusDTO.setErrorType("");

        final StatusDTO actual = SerializationUtils.clone(statusDTO);

        assertThat(statusDTO.equals(actual), is(true));
        assertThat(statusDTO.hashCode(), is(actual.hashCode()));
        assertThat(statusDTO.toString() != null, is(true));
        assertThat(actual.getErrorCode(), is(statusDTO.getErrorCode()));
        assertThat(actual.getErrorMessage(), is(statusDTO.getErrorMessage()));
        assertThat(actual.getErrorType(), is(statusDTO.getErrorType()));
    }
}
