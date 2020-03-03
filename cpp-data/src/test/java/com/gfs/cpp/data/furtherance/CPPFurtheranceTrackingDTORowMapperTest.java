package com.gfs.cpp.data.furtherance;

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

import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;
import com.gfs.cpp.data.furtherance.CPPFurtheranceTrackingDTORowMapper;

@RunWith(MockitoJUnitRunner.class)
public class CPPFurtheranceTrackingDTORowMapperTest {

    @InjectMocks
    private CPPFurtheranceTrackingDTORowMapper target;

    @Mock
    private ResultSet rs;

    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";
    private static final String FURTHERANCE_ACTION_CODE = "FURTHERANCE_ACTION_CODE";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CHANGE_TABLE_NAME = "CHANGE_TABLE_NAME";

    @Test
    public void shouldMapRowIntoCPPFurtheranceTrackingDTO() throws SQLException {

        int cppFurtherenceSeq = 1;
        String changeTableName = "CIP";
        String gfsCustomerId = "customer";
        int gfsCustomerTypeCode = -31;
        int itemPriceLevelCode = -31;
        String itemPriceId = "itemId";
        int furtheranceActionCode = 1;

        when(rs.getInt(CPP_FURTHERANCE_SEQ)).thenReturn(cppFurtherenceSeq);
        when(rs.getString(CHANGE_TABLE_NAME)).thenReturn(changeTableName);
        when(rs.getString(GFS_CUSTOMER_ID)).thenReturn(gfsCustomerId);
        when(rs.getInt(GFS_CUSTOMER_TYPE_CODE)).thenReturn(gfsCustomerTypeCode);
        when(rs.getInt(FURTHERANCE_ACTION_CODE)).thenReturn(furtheranceActionCode);
        when(rs.getString(ITEM_PRICE_ID)).thenReturn(itemPriceId);
        when(rs.getInt(ITEM_PRICE_LEVEL_CODE)).thenReturn(itemPriceLevelCode);

        final CPPFurtheranceTrackingDTO actual = target.mapRow(rs, 0);

        assertThat(actual.getCppFurtheranceSeq(), is(cppFurtherenceSeq));
        assertThat(actual.getChangeTableName(), is(changeTableName));
        assertThat(actual.getGfsCustomerId(), is(gfsCustomerId));
        assertThat(actual.getGfsCustomerTypeCode(), is(gfsCustomerTypeCode));
        assertThat(actual.getFurtheranceActionCode(), is(furtheranceActionCode));
        assertThat(actual.getItemPriceId(), is(itemPriceId));
        assertThat(actual.getItemPriceLevelCode(), is(itemPriceLevelCode));

    }

}
