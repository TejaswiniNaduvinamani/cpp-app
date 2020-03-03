package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ItemInformationDTO;

@RunWith(MockitoJUnitRunner.class)
public class ItemInformationDTOTest {

    @InjectMocks
    private ItemInformationDTO dto;

    @Test
    public void testMethods() {
        dto.setIsActive(false);
        dto.setIsValid(false);
        dto.setItemDescription("Test");
        dto.setItemNo("10001");
        dto.setItemStatusCode("AC");
        dto.setStockingCode("3");
        dto.setIsItemAlreadyExist(false);

        final ItemInformationDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getIsActive(), is(dto.getIsActive()));
        assertThat(actual.getIsValid(), is(dto.getIsValid()));
        assertThat(actual.getItemDescription(), is(dto.getItemDescription()));
        assertThat(actual.getItemNo(), is(dto.getItemNo()));
        assertThat(actual.getItemStatusCode(), is(dto.getItemStatusCode()));
        assertThat(actual.getStockingCode(), is(dto.getStockingCode()));
        assertThat(actual.getIsItemAlreadyExist(), is(dto.getIsItemAlreadyExist()));
    }
}
