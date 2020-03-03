package com.gfs.cpp.data.furtherance;

import static com.gfs.cpp.data.furtherance.FurtheranceInformationDTORowMapper.FETCH_FURTHERANCE_DETAILS_BY_FURTHERANCE_SEQ;
import static com.gfs.cpp.data.furtherance.FurtheranceInformationDTORowMapper.FETCH_INPROGRESS_FURTHERANCE_DETAILS_BY_PARENT_AGREEMENT_ID;

import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;

@Repository
public class CppFurtheranceRepository {

    public static final Logger logger = LoggerFactory.getLogger(CppFurtheranceRepository.class);

    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String FURTHERANCE_STATUS_CODE = "FURTHERANCE_STATUS_CODE";
    private static final String PARENT_CLM_AGREEMENT_ID = "PARENT_CLM_AGREEMENT_ID";
    private static final String FURTHERANCE_EFFECTIVE_DATE = "FURTHERANCE_EFFECTIVE_DATE";
    private static final String CHANGE_REASON_TXT = "CHANGE_REASON_TXT";
    private static final String CONTRACT_REFERENCE_TXT = "CONTRACT_REFERENCE_TXT";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String FURTHERANCE_DOCUMENT_GUID = "FURTHERANCE_DOCUMENT_GUID";

    // @formatter:off
    
    private static final String FETCH_CPP_FURTHERANCE_NEXT_SEQ = "select CPP_FURTHERANCE_SEQ.nextVal from dual";

    private static final String INSERT_CPP_FURTHERANCE = "Insert into CPP_FURTHERANCE "
            + "(CPP_FURTHERANCE_SEQ, "
            + "CONTRACT_PRICE_PROFILE_SEQ,"
            + "FURTHERANCE_STATUS_CODE, "
            + "PARENT_CLM_AGREEMENT_ID, "
            + "FURTHERANCE_EFFECTIVE_DATE, "
            + "CHANGE_REASON_TXT, "
            + "CONTRACT_REFERENCE_TXT, "
            + "CREATE_USER_ID, " 
            + "LAST_UPDATE_USER_ID )" 
            + " values "
            + "(:CPP_FURTHERANCE_SEQ,"
            + ":CONTRACT_PRICE_PROFILE_SEQ,"
            + ":FURTHERANCE_STATUS_CODE,"
            + ":PARENT_CLM_AGREEMENT_ID," 
            + ":FURTHERANCE_EFFECTIVE_DATE,"
            + ":CHANGE_REASON_TXT,"
            + ":CONTRACT_REFERENCE_TXT,"
            + ":CREATE_USER_ID," 
            + ":LAST_UPDATE_USER_ID)";
    
    private static final String UPDATE_CPP_FURTHERANCE = "UPDATE CPP_FURTHERANCE "
            + "SET FURTHERANCE_STATUS_CODE=:FURTHERANCE_STATUS_CODE,"
            + "FURTHERANCE_EFFECTIVE_DATE=:FURTHERANCE_EFFECTIVE_DATE, "
            + "CHANGE_REASON_TXT=:CHANGE_REASON_TXT,"
            + "CONTRACT_REFERENCE_TXT=:CONTRACT_REFERENCE_TXT,"
            + "LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID, "
            + "LAST_UPDATE_TMSTMP =SYSTIMESTAMP "
            + "WHERE CPP_FURTHERANCE_SEQ=:CPP_FURTHERANCE_SEQ";

    private static final String UPDATE_CPP_FURTHERANCE_TO_PRICING_ACVITATED_STATUS = "UPDATE "
            + " CPP_FURTHERANCE SET "
            + " FURTHERANCE_STATUS_CODE= "+ FurtheranceStatus.FURTHERANCE_ACTIVATED.getCode()
            + ", LAST_UPDATE_TMSTMP=SYSTIMESTAMP "
            + ", LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID ,"
            + " PRICE_ACTIVATED_TMSTMP=SYSTIMESTAMP "
            + " WHERE CPP_FURTHERANCE_SEQ = :CPP_FURTHERANCE_SEQ ";    
    
    private static final String UPDATE_FURTHERANCE_DOCUMENT_GUID_AND_STATUS = "UPDATE CPP_FURTHERANCE "
            + "SET FURTHERANCE_DOCUMENT_GUID =:FURTHERANCE_DOCUMENT_GUID,"
            + "FURTHERANCE_STATUS_CODE=" + FurtheranceStatus.FURTHERANCE_SAVED.getCode()
            + ", LAST_UPDATE_TMSTMP=SYSTIMESTAMP "
            + ", LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID "
            + "where CPP_FURTHERANCE_SEQ =:CPP_FURTHERANCE_SEQ ";     
    
    private static final String FETCH_FURTHERANCE_STATUS = "SELECT FURTHERANCE_STATUS_CODE FROM CPP_FURTHERANCE "
            + "WHERE CPP_FURTHERANCE_SEQ = :CPP_FURTHERANCE_SEQ "
            + "AND CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ";
    
    // @formatter:on

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    public FurtheranceInformationDTO fetchInProgressFurtheranceDetailsByParentAgreementId(String parentAgreementId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(PARENT_CLM_AGREEMENT_ID, parentAgreementId, Types.VARCHAR);
        try {
            return cppJdbcTemplate.queryForObject(FETCH_INPROGRESS_FURTHERANCE_DETAILS_BY_PARENT_AGREEMENT_ID, paramMap,
                    new FurtheranceInformationDTORowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("No inprogress furtherance found for parent agreement id {}", parentAgreementId);
            return null;
        }
    }

    public int fetchCPPFurtheranceNextSequence() {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        return cppJdbcTemplate.queryForObject(FETCH_CPP_FURTHERANCE_NEXT_SEQ, paramMap, Integer.class);
    }

    public void updateCPPFurtherance(FurtheranceInformationDTO furtheranceInformationDTO, String userName) {

        MapSqlParameterSource paramMap = buildParamMap(furtheranceInformationDTO, userName);
        cppJdbcTemplate.update(UPDATE_CPP_FURTHERANCE, paramMap);
    }

    public void saveCPPFurtherance(FurtheranceInformationDTO furtheranceInformationDTO, String userName) {

        MapSqlParameterSource paramMap = buildParamMap(furtheranceInformationDTO, userName);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        cppJdbcTemplate.update(INSERT_CPP_FURTHERANCE, paramMap);
    }

    public FurtheranceInformationDTO fetchFurtheranceDetailsByFurtheranceSeq(int furtheranceSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_FURTHERANCE_SEQ, furtheranceSeq, Types.INTEGER);
        try {
            return cppJdbcTemplate.queryForObject(FETCH_FURTHERANCE_DETAILS_BY_FURTHERANCE_SEQ, paramMap, new FurtheranceInformationDTORowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("No furtherance information found for furtherance sequence {}", furtheranceSeq);
            return null;
        }
    }

    public void updateCPPFurtheranceStatusToPricingActivated(FurtheranceInformationDTO furtheranceInformationDTO, String userName) {

        MapSqlParameterSource paramMap = buildParamMap(furtheranceInformationDTO, userName);
        cppJdbcTemplate.update(UPDATE_CPP_FURTHERANCE_TO_PRICING_ACVITATED_STATUS, paramMap);
    }

    public void updateFurtheranceStatusToSavedWithGUID(int cppfurtheranceSeq, String furtheranceDocumentGuid, String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_FURTHERANCE_SEQ, cppfurtheranceSeq, Types.INTEGER);
        paramMap.addValue(FURTHERANCE_DOCUMENT_GUID, furtheranceDocumentGuid, Types.VARCHAR);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        cppJdbcTemplate.update(UPDATE_FURTHERANCE_DOCUMENT_GUID_AND_STATUS, paramMap);
    }

    public Integer fetchCPPFurtheranceStatus(int contractPriceProfileSeq, int cppFurtheranceSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq);
        paramMap.addValue(CPP_FURTHERANCE_SEQ, cppFurtheranceSeq);
        try {
            return cppJdbcTemplate.queryForObject(FETCH_FURTHERANCE_STATUS, paramMap, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.info("No furtherance information found for furtherance sequence {}", cppFurtheranceSeq);
            return null;
        }
    }

    private MapSqlParameterSource buildParamMap(FurtheranceInformationDTO furtheranceInformationDTO, String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, furtheranceInformationDTO.getContractPriceProfileSeq(), Types.INTEGER);
        paramMap.addValue(CPP_FURTHERANCE_SEQ, furtheranceInformationDTO.getCppFurtheranceSeq(), Types.INTEGER);
        paramMap.addValue(FURTHERANCE_STATUS_CODE, furtheranceInformationDTO.getFurtheranceStatusCode(), Types.INTEGER);
        paramMap.addValue(PARENT_CLM_AGREEMENT_ID, furtheranceInformationDTO.getParentCLMAgreementId(), Types.VARCHAR);
        paramMap.addValue(FURTHERANCE_EFFECTIVE_DATE, furtheranceInformationDTO.getFurtheranceEffectiveDate(), Types.DATE);
        paramMap.addValue(CHANGE_REASON_TXT, furtheranceInformationDTO.getChangeReasonTxt(), Types.VARCHAR);
        paramMap.addValue(CONTRACT_REFERENCE_TXT, furtheranceInformationDTO.getContractReferenceTxt(), Types.VARCHAR);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        return paramMap;
    }

}
