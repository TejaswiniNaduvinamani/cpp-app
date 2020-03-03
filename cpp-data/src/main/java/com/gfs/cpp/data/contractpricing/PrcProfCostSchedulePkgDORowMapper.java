package com.gfs.cpp.data.contractpricing;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;

public class PrcProfCostSchedulePkgDORowMapper implements RowMapper<PrcProfCostSchedulePkgDO> {

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String PRC_PROF_COST_SCHEDULE_PKG_SEQ="PRC_PROF_COST_SCHEDULE_PKG_SEQ";

    // @formatter:off
    public static final String FETCH_PRC_PROF_COST_SCHEDULE_BY_CPP_SEQ = "SELECT GFS_CUSTOMER_ID, " 
            + " GFS_CUSTOMER_TYPE_CODE, "
            + " CONTRACT_PRICE_PROFILE_SEQ, " 
            + " PRC_PROF_COST_SCHEDULE_PKG_SEQ"
            + " FROM PRC_PROF_COST_SCHEDULE_PKG"
            + " WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + " AND GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + " AND GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE" ;

    // @formatter:on
    
    @Override
    public PrcProfCostSchedulePkgDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO = new PrcProfCostSchedulePkgDO();
        prcProfCostSchedulePkgDO.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        prcProfCostSchedulePkgDO.setPrcProfCostSchedulePkgSeq(rs.getInt(PRC_PROF_COST_SCHEDULE_PKG_SEQ));
        prcProfCostSchedulePkgDO.setGfsCustomerId(rs.getString(GFS_CUSTOMER_ID));
        prcProfCostSchedulePkgDO.setGfsCustomerTypeCode(rs.getInt(GFS_CUSTOMER_TYPE_CODE));

        return prcProfCostSchedulePkgDO;
    }
}
