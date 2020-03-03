package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.SubgroupResponseDTO;

@RunWith(MockitoJUnitRunner.class)
public class SubgroupResponseDTOTest {

    @InjectMocks
    private SubgroupResponseDTO dto;

    @Test
    public void testMethods() {

        dto.setSubgroupDesc("choco");
        dto.setSubgroupId("11");
        dto.setValidationCode(200);
        dto.setValidationMessage(StringUtils.EMPTY);

        final SubgroupResponseDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getSubgroupId(), is(dto.getSubgroupId()));
        assertThat(actual.getSubgroupDesc(), is(dto.getSubgroupDesc()));
        assertThat(actual.getValidationMessage(), is(dto.getValidationMessage()));
        assertThat(actual.getValidationCode(), is(dto.getValidationCode()));

    }

}
