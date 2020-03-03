package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;

@RunWith(MockitoJUnitRunner.class)
public class MarkupRequestDTOTest {

    @InjectMocks
    private MarkupRequestDTO dto;

    @Test
    public void testMethods() {
        dto.setContractPriceProfileSeq(2);
        dto.setEffectiveDate(new LocalDate(2010, 01, 01).toDate());
        dto.setExpirationDate(new LocalDate(2011, 01, 01).toDate());
        dto.setMarkupOnSell(true);
        dto.setExpireLower(true);

        final MarkupRequestDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getEffectiveDate(), is(dto.getEffectiveDate()));
        assertThat(actual.getExpirationDate(), is(dto.getExpirationDate()));
        assertThat(actual.isMarkupOnSell(), is(dto.isMarkupOnSell()));
        assertThat(actual.isExpireLower(), is(dto.isExpireLower()));
    }
}
