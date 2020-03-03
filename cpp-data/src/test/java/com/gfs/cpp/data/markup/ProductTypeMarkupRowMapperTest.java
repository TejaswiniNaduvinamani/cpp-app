package com.gfs.cpp.data.markup;

import static org.junit.Assert.assertEquals;
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

import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.data.markup.ProductTypeMarkupRowMapper;

@RunWith(MockitoJUnitRunner.class)
public class ProductTypeMarkupRowMapperTest {

    @InjectMocks
    private ProductTypeMarkupRowMapper sut;

    @Mock
    private ResultSet resultSet;

    @Test
    public void shouldMapItemLevelToItemLevelMarkupDTO() throws SQLException {

        final java.util.Date effectiveDate = new LocalDate(2018, 1, 1).toDate();
        final java.util.Date expirationDate = new LocalDate(2018, 1, 1).toDate();
        final int itemPriceId = 1;
        final String gfsCustomerId = "9999999";
        final String markupAmount = "Test_amount";
        final String markupAmountTypeCode = "Test_code";
        final int customerTypeCode = 31;
        final int markupAmountUnit = 1;

        when(resultSet.getString("GFS_CUSTOMER_ID")).thenReturn(new String(gfsCustomerId));
        when(resultSet.getDate("EFFECTIVE_DATE")).thenReturn(new Date(effectiveDate.getTime()));
        when(resultSet.getDate("EXPIRATION_DATE")).thenReturn(new Date(expirationDate.getTime()));
        when(resultSet.getInt("ITEM_PRICE_ID")).thenReturn(new Integer(itemPriceId));
        when(resultSet.getString("COST_MARKUP_AMT")).thenReturn(new String(markupAmount));
        when(resultSet.getInt("MARKUP_UNIT_TYPE_CODE")).thenReturn(new Integer(markupAmountUnit));
        when(resultSet.getString("MARKUP_AMOUNT_TYPE_CODE")).thenReturn(new String(markupAmountTypeCode));
        when(resultSet.getInt("GFS_CUSTOMER_TYPE_CODE")).thenReturn(new Integer(customerTypeCode));
        final ProductTypeMarkupDTO actual = sut.mapRow(resultSet, 0);

        assertEquals(actual.getItemPriceId(), itemPriceId);
        assertEquals(actual.getGfsCustomerId(), new String(gfsCustomerId));
        assertEquals(actual.getEffectiveDate(), effectiveDate);
        assertEquals(actual.getExpirationDate(), expirationDate);
        assertEquals(actual.getMarkupType(), markupAmountUnit);
        assertEquals(actual.getMarkup(), new String(markupAmount));
        assertEquals(actual.getUnit(), new String(markupAmountTypeCode));
        assertEquals(actual.getGfsCustomerTypeCode(), customerTypeCode);
    }
}
