package com.gfs.cpp.data.assignment;

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

import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.data.assignment.ItemAssignmentDTORowMapper;

@RunWith(MockitoJUnitRunner.class)
public class ItemAssignmentDTORowMapperTest {

    @InjectMocks
    private ItemAssignmentDTORowMapper sut;

    @Mock
    private ResultSet resultSet;

    @Mock
    private CPPDateUtils cppDateUtils;

    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";

    @Test
    public void shouldMapItemAssignmentDTO() throws SQLException {
        final String itemId = "9999999";
        final int itemPriceLevelCode = 1;
        final java.util.Date effectiveDate = new LocalDate(2018, 1, 1).toDate();
        final java.util.Date expirationDate = new LocalDate(2019, 1, 1).toDate();

        when(resultSet.getString(ITEM_PRICE_ID)).thenReturn(new String(itemId));
        when(resultSet.getInt(ITEM_PRICE_LEVEL_CODE)).thenReturn(itemPriceLevelCode);
        when(resultSet.getDate(EFFECTIVE_DATE)).thenReturn(new Date(effectiveDate.getTime()));
        when(resultSet.getDate(EXPIRATION_DATE)).thenReturn(new Date(expirationDate.getTime()));

        final ItemAssignmentDTO actual = sut.mapRow(resultSet, 0);

        assertThat(actual.getItemId(), is(new String(itemId)));
        assertThat(actual.getItemPriceLevelCode(), is(itemPriceLevelCode));
        assertThat(actual.getEffectiveDate(), is(effectiveDate));
        assertThat(actual.getExpirationDate(), is(expirationDate));
    }
}
