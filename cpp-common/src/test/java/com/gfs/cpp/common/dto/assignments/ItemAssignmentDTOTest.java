package com.gfs.cpp.common.dto.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;

@RunWith(MockitoJUnitRunner.class)
public class ItemAssignmentDTOTest {

    @InjectMocks
    private ItemAssignmentDTO dto;

    @Test
    public void testMethods() {
        dto.setItemId("Test");
        dto.setItemDescription("Test");
        dto.setIsItemSaved(true);
        dto.setItemPriceLevelCode(1);
        dto.setEffectiveDate(new Date());
        dto.setExpirationDate(new Date());
        dto.setCustomerItemDescSeq(10);

        final ItemAssignmentDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getItemId(), is(actual.getItemId()));
        assertThat(dto.getItemDescription(), is(actual.getItemDescription()));
        assertThat(dto.getIsItemSaved(), is(actual.getIsItemSaved()));
        assertThat(dto.getItemPriceLevelCode(), is(actual.getItemPriceLevelCode()));
        assertThat(dto.getEffectiveDate(), is(actual.getEffectiveDate()));
        assertThat(dto.getExpirationDate(), is(actual.getExpirationDate()));
        assertThat(dto.getCustomerItemDescSeq(), is(actual.getCustomerItemDescSeq()));
    }

}
