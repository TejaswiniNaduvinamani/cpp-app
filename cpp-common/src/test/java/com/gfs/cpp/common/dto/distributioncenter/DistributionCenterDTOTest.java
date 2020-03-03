package com.gfs.cpp.common.dto.distributioncenter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterDTO;

@RunWith(MockitoJUnitRunner.class)
public class DistributionCenterDTOTest {
    
    @InjectMocks
    private DistributionCenterDTO dto;
    
    @Test
    public void testMethods() {
        dto.setDcNumber("TEST");
        dto.setName("TEST");
        dto.setShortName("TEST");
        dto.setWmsDBInstanceId("TEST");
        final DistributionCenterDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString()!=null, is(true));
        assertThat(actual.getDcNumber(), is(dto.getDcNumber()));
        assertThat(actual.getShortName(), is(dto.getShortName()));
        assertThat(actual.getName(), is(dto.getName()));
        assertThat(actual.getWmsDBInstanceId(), is(dto.getWmsDBInstanceId()));
    }
}
