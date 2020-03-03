package com.gfs.cpp.data.splitcase;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.data.splitcase.SplitCaseGridDORowMapper;

@RunWith(MockitoJUnitRunner.class)
public class SplitCaseDORowMapperTest {

    @InjectMocks
    private SplitCaseGridDORowMapper sut;

    @Mock
    private ResultSet resultSet;

    @Test
    public void shouldMapRowIntoSplitCase() throws SQLException {

        final String itemPriceId = "item_price_id";
        final Date effectiveDate = randomDate();
        final Date expirationDate = randomDate();
        final double splitcaseAmount = 0;
        final String splitcaseAmountTypeCode = "$";

        when(resultSet.getString("ITEM_PRICE_ID")).thenReturn(new String(itemPriceId));
        when(resultSet.getDate("EFFECTIVE_DATE")).thenReturn(effectiveDate);
        when(resultSet.getDate("EXPIRATION_DATE")).thenReturn(expirationDate);
        when(resultSet.getDouble("NON_CW_MARKUP_AMT")).thenReturn(splitcaseAmount);
        when(resultSet.getString("NON_CW_MARKUP_AMOUNT_TYPE_CODE")).thenReturn(new String(splitcaseAmountTypeCode));

        final SplitCaseDO actual = sut.mapRow(resultSet, 0);

        assertThat(actual.getItemPriceId(), is(itemPriceId));
        assertThat(actual.getSplitCaseFee(), is(splitcaseAmount));
        assertThat(actual.getUnit(), is(splitcaseAmountTypeCode));
        assertThat(actual.getEffectiveDate().toString(), is(effectiveDate.toString()));
        assertThat(actual.getExpirationDate().toString(), is(expirationDate.toString()));

    }

    private Date randomDate() {
        return new Date(randomLong());
    }

    private Long randomLong() {
        return nextLong(0L, 1000L);
    }
}
