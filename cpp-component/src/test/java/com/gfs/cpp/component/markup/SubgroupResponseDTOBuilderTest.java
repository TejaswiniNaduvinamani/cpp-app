package com.gfs.cpp.component.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.SubgroupResponseDTO;
import com.gfs.cpp.component.markup.SubgroupResponseDTOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class SubgroupResponseDTOBuilderTest {

    @InjectMocks
    private SubgroupResponseDTOBuilder target;

    @Test
    public void shouldBuildSubgroupResponseDTO() {

        String subgroupId = "11";
        String subgroupDescription = "TABLETOP MISC";
        int validationCode = 200;
        String validationMessage = "";

        SubgroupResponseDTO result = target.buildSubgroupResponseDTO(subgroupId, subgroupDescription, validationCode, validationMessage);

        assertThat(result.getSubgroupId(), is(subgroupId));
        assertThat(result.getSubgroupDesc(), is(subgroupDescription));
        assertThat(result.getValidationCode(), is(validationCode));
        assertThat(result.getValidationMessage(), is(validationMessage));

    }

}
