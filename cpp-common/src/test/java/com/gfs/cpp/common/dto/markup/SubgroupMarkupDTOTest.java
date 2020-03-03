package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;

@RunWith(MockitoJUnitRunner.class)
public class SubgroupMarkupDTOTest {

    @InjectMocks
    private SubgroupMarkupDTO dto;

    @Test
    public void testMethods() {
        dto.setEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        dto.setExpirationDate(new LocalDate(2018, 01, 05).toDate());
        dto.setCustomerItemDescSeq(1);
        dto.setIsSubgroupSaved(false);
        dto.setMarkup("1");
        dto.setMarkupType(2);
        dto.setSubgroupDesc("choco");
        dto.setSubgroupId("11");
        dto.setUnit("1");

        final SubgroupMarkupDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getEffectiveDate(), is(dto.getEffectiveDate()));
        assertThat(actual.getExpirationDate(), is(dto.getExpirationDate()));
        assertThat(actual.getSubgroupId(), is(dto.getSubgroupId()));
        assertThat(actual.getMarkup(), is(dto.getMarkup()));
        assertThat(actual.getMarkupType(), is(dto.getMarkupType()));
        assertThat(actual.getUnit(), is(dto.getUnit()));
        assertThat(actual.getCustomerItemDescSeq(), is(dto.getCustomerItemDescSeq()));
        assertThat(actual.getIsSubgroupSaved(), is(dto.getIsSubgroupSaved()));
        assertThat(actual.getCustomerItemDescSeq(), is(dto.getCustomerItemDescSeq()));
        assertThat(actual.getSubgroupDesc(), is(dto.getSubgroupDesc()));
    }

}
