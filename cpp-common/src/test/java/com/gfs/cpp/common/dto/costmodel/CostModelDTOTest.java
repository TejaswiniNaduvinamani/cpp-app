package com.gfs.cpp.common.dto.costmodel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.costmodel.CostModelDTO;

@RunWith(MockitoJUnitRunner.class)
public class CostModelDTOTest {

    @InjectMocks
    private CostModelDTO dto;

    @Test
    public void testMethods() {
        dto.setModelId(1);
        dto.setName("Test");
        dto.setProfileSelectionCriteria("TEST");

        final CostModelDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getModelId(), is(dto.getModelId()));
        assertThat(actual.getName(), is(dto.getName()));
        assertThat(actual.getProfileSelectionCriteria(), is(dto.getProfileSelectionCriteria()));
    }

}
