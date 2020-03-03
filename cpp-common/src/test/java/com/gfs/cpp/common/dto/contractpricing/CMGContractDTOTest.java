package com.gfs.cpp.common.dto.contractpricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGContractDTO;

@RunWith(MockitoJUnitRunner.class)
public class CMGContractDTOTest {

    @InjectMocks
    private CMGContractDTO dto;

    @Test
    public void testMethods() {
        dto.setContractName("TEST");
        dto.setContractPriceProfileId("1234");
        final CMGContractDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getContractName(), is(dto.getContractName()));
        assertThat(actual.getContractPriceProfileId(), is(dto.getContractPriceProfileId()));
    }
}
