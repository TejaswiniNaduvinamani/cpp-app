package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;

@RunWith(MockitoJUnitRunner.class)
public class ItemLevelMarkupDTOTest {

    @InjectMocks
    private ItemLevelMarkupDTO dto;

    @Test
    public void testMethods() {
        dto.setEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        dto.setExpirationDate(new LocalDate(2018, 01, 05).toDate());
        dto.setItemDesc("Test");
        dto.setMarkup("Test");
        dto.setMarkupType(2);
        dto.setUnit("$");
        dto.setItemId("test");
        dto.setNoItemId(false);
        dto.setStockingCode(0);
        dto.setInactive(false);
        dto.setInvalid(false);
        dto.setIsItemSaved(true);
        dto.setCustomerItemDescSeq(1);

        final ItemLevelMarkupDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getEffectiveDate(), is(dto.getEffectiveDate()));
        assertThat(actual.getExpirationDate(), is(dto.getExpirationDate()));
        assertThat(actual.getItemId(), is(dto.getItemId()));
        assertThat(actual.getMarkup(), is(dto.getMarkup()));
        assertThat(actual.getMarkupType(), is(dto.getMarkupType()));
        assertThat(actual.getUnit(), is(dto.getUnit()));
        assertThat(actual.getStockingCode(), is(dto.getStockingCode()));
        assertThat(actual.isNoItemId(), is(dto.isNoItemId()));
        assertThat(actual.getItemDesc(), is(dto.getItemDesc()));
        assertThat(actual.isInactive(), is(dto.isInactive()));
        assertThat(actual.isInvalid(), is(dto.isInvalid()));
        assertThat(actual.getIsItemSaved(), is(dto.getIsItemSaved()));
        assertThat(actual.getCustomerItemDescSeq(), is(dto.getCustomerItemDescSeq()));
    }
}
