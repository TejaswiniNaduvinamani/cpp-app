package com.gfs.cpp.common.dto.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.RealCustomerResponseDTO;

@RunWith(MockitoJUnitRunner.class)
public class RealCustomerResponseDTOTest {

    @InjectMocks
    private RealCustomerResponseDTO dto;

    @Test
    public void testMethods() {
        dto.setCustomerName("Test");
        dto.setValidationCode(112);
        dto.setValidationMessage("Test Error");

        final RealCustomerResponseDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getValidationCode(), is(actual.getValidationCode()));
        assertThat(dto.getCustomerName(), is(actual.getCustomerName()));
        assertThat(dto.getValidationMessage(), is(actual.getValidationMessage()));
    }
}
