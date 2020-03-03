package com.gfs.cpp.data.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.data.markup.FutureItemDescriptionRowMapper;

@RunWith(MockitoJUnitRunner.class)
public class FutureItemDescriptionRowMapperTest {
    
    private static final String COST_MARKUP_AMT = "COST_MARKUP_AMT";
    private static final String MARKUP_AMOUNT_TYPE_CODE = "MARKUP_AMOUNT_TYPE_CODE";
    private static final String MARKUP_UNIT_TYPE_CODE = "MARKUP_UNIT_TYPE_CODE";

    @InjectMocks
    private FutureItemDescriptionRowMapper sut;

    @Mock
    private ResultSet resultSet;

    @Test
    public void shouldMapFutureItemToFutureItemDescDTO() throws SQLException {

        final String itemDesc = "Test_item";
        final String gfsCustomerId = "1";
        final int gfsCustomerTypeCode = 31;
        final int costMarkupAmt=1;
        final String markupAmountTypeCode = "MarkupAmountTypeCode";
        final int markupUnitTypeCode=2;

        when(resultSet.getString("ITEM_DESC")).thenReturn(new String(itemDesc));
        when(resultSet.getString("GFS_CUSTOMER_ID")).thenReturn(new String(gfsCustomerId));
        when(resultSet.getInt("GFS_CUSTOMER_TYPE_CODE")).thenReturn(new Integer(gfsCustomerTypeCode));
        when(resultSet.getInt(COST_MARKUP_AMT)).thenReturn(new Integer(costMarkupAmt));
        when(resultSet.getString(MARKUP_AMOUNT_TYPE_CODE)).thenReturn(new String(markupAmountTypeCode));
        when(resultSet.getInt(MARKUP_UNIT_TYPE_CODE)).thenReturn(new Integer(markupUnitTypeCode));

        final FutureItemDescriptionDTO actual = sut.mapRow(resultSet, 0);

        assertThat(actual.getFutureItemDesc(), is(itemDesc));
        assertThat(actual.getGfsCustomerId(), is(gfsCustomerId));
        assertThat(actual.getGfsCustomerTypeCode(), is(gfsCustomerTypeCode));
        assertThat(actual.getCostMarkupAmt(), is(costMarkupAmt));
        assertThat(actual.getMarkupAmountTypeCode(), is(markupAmountTypeCode));
        assertThat(actual.getMarkupUnitTypeCode(), is(markupUnitTypeCode));
    }
}
