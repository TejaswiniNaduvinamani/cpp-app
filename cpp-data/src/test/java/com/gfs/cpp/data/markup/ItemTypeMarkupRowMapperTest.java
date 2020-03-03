package com.gfs.cpp.data.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.data.markup.ItemTypeMarkupRowMapper;

@RunWith(MockitoJUnitRunner.class)
public class ItemTypeMarkupRowMapperTest {

    @InjectMocks
    private ItemTypeMarkupRowMapper sut;

    @Mock
    private ResultSet resultSet;

    @Test
    public void shouldMapItemLevelToItemLevelMarkupDTO() throws SQLException {
        final String itemId = "item_id";
        final java.util.Date effectiveDate = new LocalDate(2018, 1, 1).toDate();
        final java.util.Date expirationDate = new LocalDate(2019, 1, 1).toDate();
        final String markupAmount = "Test_amount";
        final String markupAmountTypeCode = "Test_code";
        final int markupAmountUnit = 1;

        when(resultSet.getString("ITEM_PRICE_ID")).thenReturn(new String(itemId));
        when(resultSet.getDate("EFFECTIVE_DATE")).thenReturn(new Date(effectiveDate.getTime()));
        when(resultSet.getDate("EXPIRATION_DATE")).thenReturn(new Date(expirationDate.getTime()));
        when(resultSet.getString("COST_MARKUP_AMT")).thenReturn(new String(markupAmount));
        when(resultSet.getInt("MARKUP_UNIT_TYPE_CODE")).thenReturn(new Integer(markupAmountUnit));
        when(resultSet.getString("MARKUP_AMOUNT_TYPE_CODE")).thenReturn(new String(markupAmountTypeCode));

        final ItemLevelMarkupDTO actual = sut.mapRow(resultSet, 0);

        assertThat(actual.getItemId(), is(new String(itemId)));
        assertThat(actual.getEffectiveDate(), is(effectiveDate));
        assertThat(actual.getExpirationDate(), is(expirationDate));
        assertThat(actual.getMarkupType(), is(markupAmountUnit));
        assertThat(actual.getMarkup(), is(new String(markupAmount)));
        assertThat(actual.getUnit(), is(new String(markupAmountTypeCode)));
    }
}
