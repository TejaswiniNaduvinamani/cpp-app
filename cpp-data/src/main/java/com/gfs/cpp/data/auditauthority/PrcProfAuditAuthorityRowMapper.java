package com.gfs.cpp.data.auditauthority;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.markup.PrcProfAuditAuthorityDTO;

public class PrcProfAuditAuthorityRowMapper implements RowMapper<PrcProfAuditAuthorityDTO> {

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String PRC_PROF_AUDIT_AUTHORITY_IND = "PRC_PROF_AUDIT_AUTHORITY_IND";
    
    // @formatter:off
        
    public static final String FETCH_PRC_PROF_AUDIT_AUTHORITY_FOR_CPP_SEQ = "SELECT "
            + "GFS_CUSTOMER_ID, "
            + "GFS_CUSTOMER_TYPE_CODE, "
            + "EFFECTIVE_DATE, "
            + "EXPIRATION_DATE, "
            + "PRC_PROF_AUDIT_AUTHORITY_IND, "
            + "CONTRACT_PRICE_PROFILE_SEQ "
            + "FROM PRC_PROF_AUDIT_AUTHORITY "
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ "
            + "AND GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID "
            + "AND GFS_CUSTOMER_TYPE_CODE=:GFS_CUSTOMER_TYPE_CODE "
            + "AND EXPIRATION_DATE >= SYSDATE"; 
     
     // @formatter:on

    @Override
    public PrcProfAuditAuthorityDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PrcProfAuditAuthorityDTO prcProfAuditAuthorityDTO = new PrcProfAuditAuthorityDTO();
        prcProfAuditAuthorityDTO.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        prcProfAuditAuthorityDTO.setEffectiveDate(rs.getDate(EFFECTIVE_DATE));
        prcProfAuditAuthorityDTO.setExpirationDate(rs.getDate(EXPIRATION_DATE));
        prcProfAuditAuthorityDTO.setGfsCustomerId(rs.getString(GFS_CUSTOMER_ID));
        prcProfAuditAuthorityDTO.setGfsCustomerType(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        prcProfAuditAuthorityDTO.setPrcProfAuditAuthorityInd(rs.getInt(PRC_PROF_AUDIT_AUTHORITY_IND));
        return prcProfAuditAuthorityDTO;
    }

}
