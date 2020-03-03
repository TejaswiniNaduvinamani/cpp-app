package com.gfs.cpp.data.furtherance;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;

public class FurtheranceInformationDTORowMapper implements RowMapper<FurtheranceInformationDTO> {

    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String FURTHERANCE_STATUS_CODE = "FURTHERANCE_STATUS_CODE";
    private static final String PARENT_CLM_AGREEMENT_ID = "PARENT_CLM_AGREEMENT_ID";
    private static final String FURTHERANCE_EFFECTIVE_DATE = "FURTHERANCE_EFFECTIVE_DATE";
    private static final String CHANGE_REASON_TXT = "CHANGE_REASON_TXT";
    private static final String CONTRACT_REFERENCE_TXT = "CONTRACT_REFERENCE_TXT";
    private static final String FURTHERANCE_DOCUMENT_GUID = "FURTHERANCE_DOCUMENT_GUID";

    // @formatter:off
    
    private static final String SELECT_FURTHERANCE_DETAILS = "select CPP_FURTHERANCE_SEQ, "
            + "CONTRACT_PRICE_PROFILE_SEQ, "
            + "FURTHERANCE_STATUS_CODE, "
            + "PARENT_CLM_AGREEMENT_ID, "
            + "FURTHERANCE_EFFECTIVE_DATE, "
            + "CHANGE_REASON_TXT, "
            + "CONTRACT_REFERENCE_TXT, "
            + "FURTHERANCE_DOCUMENT_GUID " 
            + "from CPP_FURTHERANCE ";
    
    public static final String FETCH_INPROGRESS_FURTHERANCE_DETAILS_BY_PARENT_AGREEMENT_ID = SELECT_FURTHERANCE_DETAILS
            + "where PARENT_CLM_AGREEMENT_ID = :PARENT_CLM_AGREEMENT_ID "
            + "and FURTHERANCE_STATUS_CODE IN ("
            + FurtheranceStatus.FURTHERANCE_INITIATED.getCode() + ","  
            + FurtheranceStatus.FURTHERANCE_SAVED.getCode()
            + ")";
    
    public static final String FETCH_FURTHERANCE_DETAILS_BY_FURTHERANCE_SEQ = SELECT_FURTHERANCE_DETAILS
            + "where CPP_FURTHERANCE_SEQ = :CPP_FURTHERANCE_SEQ ";
      
    // @formatter:on
    @Override
    public FurtheranceInformationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setChangeReasonTxt(rs.getString(CHANGE_REASON_TXT));
        furtheranceInformationDTO.setContractReferenceTxt(rs.getString(CONTRACT_REFERENCE_TXT));
        furtheranceInformationDTO.setCppFurtheranceSeq(rs.getInt(CPP_FURTHERANCE_SEQ));
        furtheranceInformationDTO.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        furtheranceInformationDTO.setFurtheranceDocumentGUID(rs.getString(FURTHERANCE_DOCUMENT_GUID));
        furtheranceInformationDTO.setFurtheranceStatusCode(rs.getInt(FURTHERANCE_STATUS_CODE));
        furtheranceInformationDTO.setFurtheranceEffectiveDate(rs.getDate(FURTHERANCE_EFFECTIVE_DATE));
        furtheranceInformationDTO.setParentCLMAgreementId(rs.getString(PARENT_CLM_AGREEMENT_ID));
        return furtheranceInformationDTO;
    }

}
