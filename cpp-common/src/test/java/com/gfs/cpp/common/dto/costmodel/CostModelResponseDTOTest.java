package com.gfs.cpp.common.dto.costmodel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.costmodel.CostModelResponseDTO;

@RunWith(MockitoJUnitRunner.class)
public class CostModelResponseDTOTest {

    @InjectMocks
    private CostModelResponseDTO dto;

    @Test
    public void testMethods() {
        dto.setCostModelId(1);
        dto.setCostModelTypeValue("Test");

        final CostModelResponseDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getCostModelId(), is(dto.getCostModelId()));
        assertThat(actual.getCostModelTypeValue(), is(dto.getCostModelTypeValue()));

    }

}
