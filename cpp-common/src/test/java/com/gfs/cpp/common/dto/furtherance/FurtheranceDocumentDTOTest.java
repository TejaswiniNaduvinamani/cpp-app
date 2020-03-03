package com.gfs.cpp.common.dto.furtherance;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.furtherance.FurtheranceDocumentDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceDocumentDTOTest {

    @InjectMocks
    private FurtheranceDocumentDTO dto;

    @Test
    public void testMethods() {
        FurtheranceInformationDTO furtheranceInformationDTO = null;
        SplitCaseReviewDTO splitCaseDTO = null;
        MarkupReviewDTO markupDTO = null;

        dto.setFurtheranceInformationDTO(furtheranceInformationDTO);
        dto.setMarkupDTO(markupDTO);
        dto.setSplitCaseDTO(splitCaseDTO);

        final FurtheranceDocumentDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), equalTo(true));
        assertThat(dto.hashCode(), equalTo(actual.hashCode()));
        assertThat(dto.toString() != null, equalTo(true));

        assertThat(actual.getFurtheranceInformationDTO(), equalTo(dto.getFurtheranceInformationDTO()));
        assertThat(actual.getMarkupDTO(), equalTo(dto.getMarkupDTO()));
        assertThat(actual.getSplitCaseDTO(), equalTo(dto.getSplitCaseDTO()));

    }

}
