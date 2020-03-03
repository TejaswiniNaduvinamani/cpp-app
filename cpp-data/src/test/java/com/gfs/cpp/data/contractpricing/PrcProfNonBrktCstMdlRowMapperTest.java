package com.gfs.cpp.data.contractpricing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRowMapper;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfNonBrktCstMdlRowMapperTest {

    
    @InjectMocks
    private PrcProfNonBrktCstMdlRowMapper target;
    
    @Mock
    private ResultSet rs;
    
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String COST_MODEL_ID = "COST_MODEL_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    
    
    @Test
    public void shouldMapRowIntoPrcProfNonBrktCstMdlDTO() throws SQLException {
        final String gfsCustomerId = "100";
        final int gfsCustomerTypeCode = 31;
        final int contractPriceProfileSeq = 1;
        final int itemPriceLevelCode = 3;
        final String itemPriceId = "itmId";
        final int costModelId = 2;

        when(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ)).thenReturn(contractPriceProfileSeq);
        when(rs.getString(GFS_CUSTOMER_ID)).thenReturn(gfsCustomerId);
        when(rs.getInt(GFS_CUSTOMER_TYPE_CODE)).thenReturn(gfsCustomerTypeCode);
        when(rs.getInt(ITEM_PRICE_LEVEL_CODE)).thenReturn(itemPriceLevelCode);
        when(rs.getString(ITEM_PRICE_ID)).thenReturn(itemPriceId);
        when(rs.getInt(COST_MODEL_ID)).thenReturn(costModelId);
       

        final PrcProfNonBrktCstMdlDTO actual = target.mapRow(rs, 0);

        assertEquals(actual.getContractPriceProfileSeq(), contractPriceProfileSeq);
        assertEquals(actual.getGfsCustomerId(), gfsCustomerId);
        assertEquals(actual.getGfsCustomerTypeCode(), gfsCustomerTypeCode);
        assertEquals(actual.getItemPriceLevelCode(), itemPriceLevelCode);
        assertEquals(actual.getItemPriceId(), itemPriceId);
        assertEquals(actual.getCostModelId(), costModelId);
        
    }
    
}
