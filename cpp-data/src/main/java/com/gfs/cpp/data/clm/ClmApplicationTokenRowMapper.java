package com.gfs.cpp.data.clm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.clm.ClmApplicationTokenDO;

public class ClmApplicationTokenRowMapper implements RowMapper<ClmApplicationTokenDO> {

    private static final String CLM_APPLICATION_TOKEN_ID = "CLM_APPLICATION_TOKEN_ID";
    private static final String APPLICATION_TOKEN_VALUE = "APPLICATION_TOKEN_VALUE";

    // @formatter:off
        
    public static final String FETCH_ALL_APPLICATION_TOKENS = "select "
            + "CLM_APPLICATION_TOKEN_ID, "
            + "APPLICATION_TOKEN_VALUE "
            + " from CLM_APPLICATION_TOKEN "
            + "where EXPIRATION_DATE >= sysdate";
    
    // @formatter:on

    @Override
    public ClmApplicationTokenDO mapRow(ResultSet rs, int rowNum) throws SQLException {

        ClmApplicationTokenDO clmApplicationTokenDO = new ClmApplicationTokenDO();
        clmApplicationTokenDO.setApplicationTokenValue(rs.getString(APPLICATION_TOKEN_VALUE));
        clmApplicationTokenDO.setClmApplicationTokenId(rs.getInt(CLM_APPLICATION_TOKEN_ID));

        return clmApplicationTokenDO;
    }

}
