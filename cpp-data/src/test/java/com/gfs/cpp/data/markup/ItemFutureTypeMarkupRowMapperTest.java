package com.gfs.cpp.data.markup;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.data.markup.ItemFutureTypeMarkupRowMapper;

@RunWith(MockitoJUnitRunner.class)
public class ItemFutureTypeMarkupRowMapperTest {

    @InjectMocks
    private ItemFutureTypeMarkupRowMapper sut;

    @Mock
    private ResultSet resultSet;

    @Test
    public void shouldMapItemLevelToItemLevelMarkupDTO() throws SQLException {

        final String itemDesc = "Test_item";
        final String markupAmount = "Test_amount";
        final String markupAmountTypeCode = "Test_code";
        final int markupAmountUnit = 1;

        when(resultSet.getString("ITEM_DESC")).thenReturn(new String(itemDesc));
        when(resultSet.getString("COST_MARKUP_AMT")).thenReturn(new String(markupAmount));
        when(resultSet.getInt("MARKUP_UNIT_TYPE_CODE")).thenReturn(new Integer(markupAmountUnit));
        when(resultSet.getString("MARKUP_AMOUNT_TYPE_CODE")).thenReturn(new String(markupAmountTypeCode));

        final ItemLevelMarkupDTO actual = sut.mapRow(resultSet, 0);

        assertEquals(actual.getItemDesc(), new String(itemDesc));
        assertEquals(actual.getMarkupType(), markupAmountUnit);
        assertEquals(actual.getMarkup(), new String(markupAmount));
        assertEquals(actual.getUnit(), new String(markupAmountTypeCode));
    }

}
