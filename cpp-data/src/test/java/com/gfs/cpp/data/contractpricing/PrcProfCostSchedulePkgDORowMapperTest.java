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

import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgDORowMapper;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfCostSchedulePkgDORowMapperTest {

    @InjectMocks
    private PrcProfCostSchedulePkgDORowMapper target;

    @Mock
    private ResultSet rs;

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String PRC_PROF_COST_SCHEDULE_PKG_SEQ = "PRC_PROF_COST_SCHEDULE_PKG_SEQ";

    @Test
    public void shouldMapRowIntoPrcProfCostSchedulePkgDO() throws SQLException {
        final String gfsCustomerId = "100";
        final int gfsCustomerTypeCode = 31;
        final int contractPriceProfileSeq = 1;
        final int prcProfCostSchedulePkgSeq = 3;

        when(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ)).thenReturn(contractPriceProfileSeq);
        when(rs.getInt(PRC_PROF_COST_SCHEDULE_PKG_SEQ)).thenReturn(prcProfCostSchedulePkgSeq);
        when(rs.getString(GFS_CUSTOMER_ID)).thenReturn(gfsCustomerId);
        when(rs.getInt(GFS_CUSTOMER_TYPE_CODE)).thenReturn(gfsCustomerTypeCode);
       

        final PrcProfCostSchedulePkgDO actual = target.mapRow(rs, 0);

        assertEquals(actual.getContractPriceProfileSeq(), contractPriceProfileSeq);
        assertEquals(actual.getGfsCustomerId(), gfsCustomerId);
        assertEquals(actual.getPrcProfCostSchedulePkgSeq(), prcProfCostSchedulePkgSeq);
        assertEquals(actual.getGfsCustomerTypeCode(), gfsCustomerTypeCode);
    }
}

