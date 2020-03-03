package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;

@RunWith(MockitoJUnitRunner.class)
public class MarkupDTOTest {

    @InjectMocks
    MarkupReviewDTO dto;

    @Test
    public void testMethods() {
        dto.setMarkupBasedOnSell("Test");
        dto.setMarkupBasedOnSellContractLanguage1("Test");
        dto.setMarkupBasedOnSellContractLanguage2("Test");
        List<MarkupGridDTO> markupGridDTOs = new ArrayList<>();
        dto.setMarkupGridDTOs(markupGridDTOs);
        dto.setPricingOverrideInd(true);
        dto.setMarkupTypeDefinitionSellUnitLanguage("Test");
        dto.setMarkupTypeDefinitionPerCaseLanguage("Test");
        dto.setMarkupTypeDefinitionPerWeightLanguage("Test");

        final MarkupReviewDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getMarkupBasedOnSell(), is(dto.getMarkupBasedOnSell()));
        assertThat(actual.getMarkupBasedOnSellContractLanguage1(), is(dto.getMarkupBasedOnSellContractLanguage1()));
        assertThat(actual.getMarkupBasedOnSellContractLanguage2(), is(dto.getMarkupBasedOnSellContractLanguage2()));
        assertThat(actual.getMarkupGridDTOs(), is(dto.getMarkupGridDTOs()));
        assertThat(actual.isPricingOverrideInd(), is(dto.isPricingOverrideInd()));
        assertThat(actual.getMarkupTypeDefinitionSellUnitLanguage(), is(dto.getMarkupTypeDefinitionSellUnitLanguage()));
        assertThat(actual.getMarkupTypeDefinitionPerCaseLanguage(), is(dto.getMarkupTypeDefinitionPerCaseLanguage()));
        assertThat(actual.getMarkupTypeDefinitionPerWeightLanguage(), is(dto.getMarkupTypeDefinitionPerWeightLanguage()));

    }
}
