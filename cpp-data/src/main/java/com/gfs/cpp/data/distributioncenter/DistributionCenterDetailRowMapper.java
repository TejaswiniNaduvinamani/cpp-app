package com.gfs.cpp.data.distributioncenter;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;

public class DistributionCenterDetailRowMapper implements RowMapper<DistributionCenterDetailDO> {

    // @formatter:off
    
    public static final String FETCH_CONTRACT_PRICE_PROF_SHIP_DC = "SELECT SHIP_DC_NBR,"
            + "CONTRACT_PRICE_PROFILE_SEQ, "
            + "EFFECTIVE_DATE, "
            + "EXPIRATION_DATE "
            + "FROM CONTRACT_PRICE_PROF_SHIP_DC "
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ  "
            + "and EXPIRATION_DATE>=sysdate";
    
    public static final String FETCH_ALL_CONTRACT_PRICE_PROF_SHIP_DC = "SELECT SHIP_DC_NBR,"
            + "CONTRACT_PRICE_PROFILE_SEQ, "
            + "EFFECTIVE_DATE, "
            + "EXPIRATION_DATE "
            + "FROM CONTRACT_PRICE_PROF_SHIP_DC "
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ";
    
    // @formatter:on
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String SHIP_DC_NBR = "SHIP_DC_NBR";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";

    public DistributionCenterDetailDO mapRow(ResultSet rs, int rowNum) throws SQLException {
        DistributionCenterDetailDO distributionCenterDetailDO = new DistributionCenterDetailDO();
        distributionCenterDetailDO.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        distributionCenterDetailDO.setDcCode(rs.getString(SHIP_DC_NBR));
        distributionCenterDetailDO.setEffectiveDate(rs.getDate(EFFECTIVE_DATE));
        distributionCenterDetailDO.setExpirationDate(rs.getDate(EXPIRATION_DATE));
        return distributionCenterDetailDO;
    }
}
