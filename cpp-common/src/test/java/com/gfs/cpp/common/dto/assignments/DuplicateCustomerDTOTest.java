package com.gfs.cpp.common.dto.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;

@RunWith(MockitoJUnitRunner.class)
public class DuplicateCustomerDTOTest {

    @InjectMocks
    private DuplicateCustomerDTO dto;

    @Test
    public void testMethods() {
        dto.setConceptName("Test");
        dto.setContractPriceProfileId(1);
        dto.setContractPriceProfileSeq(1);
        dto.setCustomerId("1");
        dto.setCustomerType(31);

        final DuplicateCustomerDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getConceptName(), is(actual.getConceptName()));
        assertThat(dto.getContractPriceProfileId(), is(actual.getContractPriceProfileId()));
        assertThat(dto.getContractPriceProfileSeq(), is(actual.getContractPriceProfileSeq()));
        assertThat(dto.getCustomerId(), is(actual.getCustomerId()));
        assertThat(dto.getCustomerType(), is(actual.getCustomerType()));

    }

}
