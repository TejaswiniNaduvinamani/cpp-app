package com.gfs.cpp.data.contractpricing;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;


public class CPPInformationDTORowMapper implements RowMapper<CPPInformationDTO> {

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String CONTRACT_PRICE_PROFILE_ID = "CONTRACT_PRICE_PROFILE_ID";
    private static final String VERSION_NUMBER = "VERSION_NBR";

 // @formatter:off
    public static final String FETCH_CONTRACT_PRICE_PROFILE_INFO = "select CONTRACT_PRICE_PROFILE_ID, "
            + "CONTRACT_PRICE_PROFILE_SEQ, "
            + "VERSION_NBR from CONTRACT_PRICE_PROFILE "
            + "where CLM_AGREEMENT_ID = :CLM_AGREEMENT_ID "
            + "and rownum=1 ORDER BY VERSION_NBR desc";
    
 // @formatter:on

    @Override
    public CPPInformationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CPPInformationDTO clmIntegrationDTO = new CPPInformationDTO();
        clmIntegrationDTO.setContractPriceProfileId(rs.getInt(CONTRACT_PRICE_PROFILE_ID));
        clmIntegrationDTO.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        clmIntegrationDTO.setVersionNumber(rs.getInt(VERSION_NUMBER));
        return clmIntegrationDTO;
    }

}
