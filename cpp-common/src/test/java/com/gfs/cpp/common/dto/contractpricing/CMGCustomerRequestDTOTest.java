package com.gfs.cpp.common.dto.contractpricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerRequestDTO;


@RunWith(MockitoJUnitRunner.class)
public class CMGCustomerRequestDTOTest {
    
    @InjectMocks
    private CMGCustomerRequestDTO dto;
    
    @Test
    public void testMethods() {
        dto.setGroupDesc("TEST");
        dto.setGroupName("TEST");
        final CMGCustomerRequestDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString()!=null, is(true));
        assertThat(actual.getGroupDesc(), is(dto.getGroupDesc()));
        assertThat(actual.getGroupName(), is(dto.getGroupName()));
    }
}
