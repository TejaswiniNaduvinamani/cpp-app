package com.gfs.cpp.common.dto.review;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.review.SavePricingExhibitDTO;

@RunWith(MockitoJUnitRunner.class)
public class SavePricingExhibitDTOTest {

    @InjectMocks
    private SavePricingExhibitDTO dto;

    @Test
    public void testMethods() {
        dto.setContractAgeementId("Test");
        dto.setContractPriceProfileSeq(1);
        dto.setContractTypeName("Test");

        final SavePricingExhibitDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getContractAgeementId(), is(dto.getContractAgeementId()));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getContractTypeName(), is(dto.getContractTypeName()));
    }

}
