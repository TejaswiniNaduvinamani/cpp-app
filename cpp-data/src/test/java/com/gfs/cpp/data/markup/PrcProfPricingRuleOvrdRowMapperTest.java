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

import com.gfs.cpp.common.dto.markup.PrcProfPricingRuleOvrdDTO;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRowMapper;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfPricingRuleOvrdRowMapperTest {
    
    @InjectMocks
    private PrcProfPricingRuleOvrdRowMapper mapperObject;
    
    @Mock
    private ResultSet resultSet;
    
    @Test
    public void shouldMapSellPriceDetailsToPrcProfPricingRuleOvrdDTO() throws SQLException {
        
        final int contractPriceProfileSeq = 1;
        final int pricingOverId = 2;
        final int pricingOverInd = 0;
        final int gfsCustomerTypeCode = 10;
        final String gfsCustomerTypeId = "customerId";
     

        when(resultSet.getInt("CONTRACT_PRICE_PROFILE_SEQ")).thenReturn(new Integer(contractPriceProfileSeq));
        when(resultSet.getInt("PRICING_OVERRIDE_ID")).thenReturn(new Integer(pricingOverId));
        when(resultSet.getInt("PRICING_OVERRIDE_IND")).thenReturn(new Integer(pricingOverInd));
        when(resultSet.getInt("GFS_CUSTOMER_TYPE_CODE")).thenReturn(new Integer(gfsCustomerTypeCode));
        when(resultSet.getString("GFS_CUSTOMER_ID")).thenReturn(new String(gfsCustomerTypeId));
        
       

        final PrcProfPricingRuleOvrdDTO actual = mapperObject.mapRow(resultSet, 0);

        assertEquals(actual.getContractPriceProfileSeq(), contractPriceProfileSeq);
        assertEquals(actual.getPricingOverrideId(), pricingOverId);
        assertEquals(actual.getPricingOverrideInd(), pricingOverInd);
        assertEquals(actual.getGfsCustomerId(), gfsCustomerTypeId);
        assertEquals(actual.getGfsCustomerTypeCode(), gfsCustomerTypeCode);
 
    }

}
