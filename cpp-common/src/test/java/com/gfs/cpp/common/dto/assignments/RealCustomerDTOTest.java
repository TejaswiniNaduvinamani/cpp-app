package com.gfs.cpp.common.dto.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;

@RunWith(MockitoJUnitRunner.class)
public class RealCustomerDTOTest {

    @InjectMocks
    private RealCustomerDTO dto;

    @Test
    public void testMethods() {
        dto.setIsCustomerSaved(false);
        dto.setRealCustomerGroupType("CMG");
        dto.setRealCustomerId("9999");
        dto.setRealCustomerName("TEST NAME");
        dto.setRealCustomerType(9999);

        final RealCustomerDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getIsCustomerSaved(), is(actual.getIsCustomerSaved()));
        assertThat(dto.getRealCustomerGroupType(), is(actual.getRealCustomerGroupType()));
        assertThat(dto.getRealCustomerId(), is(actual.getRealCustomerId()));
        assertThat(dto.getRealCustomerName(), is(actual.getRealCustomerName()));
        assertThat(dto.getRealCustomerType(), is(actual.getRealCustomerType()));
    }
}
