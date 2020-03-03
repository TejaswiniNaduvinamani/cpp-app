package com.gfs.cpp.common.dto.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.CMGCustomerTypeDTO;

@RunWith(MockitoJUnitRunner.class)
public class CMGCustomerTypeDTOTest {

    @InjectMocks
    private CMGCustomerTypeDTO dto;

    @Test
    public void testMethods() {
        dto.setCustomerTypeId(100);
        dto.setCustomerTypeValue("Test Group");

        final CMGCustomerTypeDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getCustomerTypeId(), is(actual.getCustomerTypeId()));
        assertThat(dto.getCustomerTypeValue(), is(actual.getCustomerTypeValue()));
    }
}
