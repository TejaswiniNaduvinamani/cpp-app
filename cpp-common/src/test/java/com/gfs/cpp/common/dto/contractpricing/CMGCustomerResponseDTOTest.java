package com.gfs.cpp.common.dto.contractpricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;

@RunWith(MockitoJUnitRunner.class)
public class CMGCustomerResponseDTOTest {

    @InjectMocks
    private CMGCustomerResponseDTO dto;

    @Test
    public void testMethods() {
        dto.setId("TEST");
        dto.setTypeCode(1);
        dto.setDefaultCustomerInd(1);
        dto.setCppCustomerSeq(1);

        final CMGCustomerResponseDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getId(), is(dto.getId()));
        assertThat(actual.getTypeCode(), is(dto.getTypeCode()));
        assertThat(actual.getDefaultCustomerInd(), is(dto.getDefaultCustomerInd()));
        assertThat(actual.getCppCustomerSeq(), is(dto.getCppCustomerSeq()));
    }
}
