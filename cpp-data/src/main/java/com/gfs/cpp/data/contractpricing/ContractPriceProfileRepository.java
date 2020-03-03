package com.gfs.cpp.data.contractpricing;

import static com.gfs.cpp.data.contractpricing.CPPInformationDTORowMapper.FETCH_CONTRACT_PRICE_PROFILE_INFO;
import static com.gfs.cpp.data.contractpricing.ContractPricingResponseDTORowMapper.FETCH_CONTRACT_DETAILS_BY_AGREEMENT_ID;
import static com.gfs.cpp.data.contractpricing.ContractPricingResponseDTORowMapper.FETCH_CONTRACT_DETAILS_BY_CPP_SEQ;
import static com.gfs.cpp.data.contractpricing.ContractPricingResponseDTORowMapper.FETCH_CONTRACT_DETAILS_FOR_LATEST_CONTRACT_VERSION;
import static com.gfs.cpp.data.contractpricing.ContractSearchResultDTORowMapper.FETCH_ALL_CONTRACT_DETAILS_BY_CONTRACT_NAME;
import static com.gfs.cpp.data.contractpricing.ContractSearchResultDTORowMapper.FETCH_ALL_CONTRACT_DETAILS_BY_CPP_ID;
import static com.gfs.cpp.data.contractpricing.ContractSearchResultDTORowMapper.FETCH_ALL_CONTRACT_DETAILS_BY_CUSTOMER;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.common.dto.search.ContractSearchResultDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;

@Repository(value = "contractPriceProfileRepository")
public class ContractPriceProfileRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String CONTRACT_PRICE_PROFILE_ID = "CONTRACT_PRICE_PROFILE_ID";
    private static final String CLM_CONTRACT_TYPE_SEQ = "CLM_CONTRACT_TYPE_SEQ";
    private static final String PRC_EFFECTIVE_DATE = "PRC_EFFECTIVE_DATE";
    private static final String PRC_EXPIRATION_DATE = "PRC_EXPIRATION_DATE";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String CONTRACT_PRC_PROF_STATUS_CODE = "CONTRACT_PRC_PROF_STATUS_CODE";
    private static final String CLM_AGREEMENT_ID = "CLM_AGREEMENT_ID";
    private static final String PRICING_EXHIBIT_GUID = "PRICING_EXHIBIT_GUID";
    private static final String EXPIRE_LOWER_LEVEL_IND = "EXPIRE_LOWER_LEVEL_IND";
    private static final String PARENT_CLM_AGREEMENT_ID = "PARENT_CLM_AGREEMENT_ID";
    private static final String CLM_CONTRACT_START_DATE = "CLM_CONTRACT_START_DATE";
    private static final String CLM_CONTRACT_END_DATE = "CLM_CONTRACT_END_DATE";
    private static final String VERSION_NBR = "VERSION_NBR";
    private static final String CLM_CONTRACT_NAME = "CLM_CONTRACT_NAME";
    private static final String TRANSFER_FEE_IND = "TRANSFER_FEE_IND";
    private static final String LABEL_ASSESMENT_IND = "LABEL_ASSESMENT_IND";
    private static final String SEARCH_CONTRACT_NAME = "SEARCH_CONTRACT_NAME";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";

    public static final Logger logger = LoggerFactory.getLogger(ContractPriceProfileRepository.class);

    // @formatter:off
    private static final String INSERT_CONTRACT_PRICE_PROFILE_DETAILS = "Insert into CONTRACT_PRICE_PROFILE "
            + "(CONTRACT_PRICE_PROFILE_SEQ," 
            + "CONTRACT_PRICE_PROFILE_ID, "
            + "VERSION_NBR,"
            + "EXPIRE_LOWER_LEVEL_IND, "
            + "CLM_CONTRACT_START_DATE, "
            + "CLM_CONTRACT_END_DATE, "
            + "PARENT_CLM_AGREEMENT_ID," 
            + "CLM_CONTRACT_TYPE_SEQ,"
            + "PRC_EFFECTIVE_DATE," 
            + "PRC_EXPIRATION_DATE,"
            + "CLM_AGREEMENT_ID, " 
            + "CREATE_USER_ID, " 
            + "LAST_UPDATE_USER_ID, "
            + "TRANSFER_FEE_IND, "
            + "LABEL_ASSESMENT_IND, "
            + "CLM_CONTRACT_NAME)"
            + " values (:CONTRACT_PRICE_PROFILE_SEQ," 
            + ":CONTRACT_PRICE_PROFILE_ID, "
            + ":VERSION_NBR, "
            + ":EXPIRE_LOWER_LEVEL_IND, "
            + ":CLM_CONTRACT_START_DATE, "
            + ":CLM_CONTRACT_END_DATE, "
            + ":PARENT_CLM_AGREEMENT_ID,"
            + ":CLM_CONTRACT_TYPE_SEQ,"
            + ":PRC_EFFECTIVE_DATE," 
            + ":PRC_EXPIRATION_DATE,"
            + ":CLM_AGREEMENT_ID," 
            + ":CREATE_USER_ID, " 
            + ":LAST_UPDATE_USER_ID,"
            + ":TRANSFER_FEE_IND,"
            + ":LABEL_ASSESMENT_IND,"
            + ":CLM_CONTRACT_NAME)";    

    private static final String FETCH_CPP_SEQUENCE = "Select CONTRACT_PRICE_PROFILE_SEQ "
            + "from CONTRACT_PRICE_PROFILE "
            + " where CONTRACT_PRICE_PROFILE_ID = :CONTRACT_PRICE_PROFILE_ID "
            + "and VERSION_NBR = (select max(VERSION_NBR) from "
            + "CONTRACT_PRICE_PROFILE where CONTRACT_PRICE_PROFILE_ID = :CONTRACT_PRICE_PROFILE_ID )";

    private static final String UPDATE_CONTRACT_PRICE_PROFILE = "UPDATE CONTRACT_PRICE_PROFILE"
            + " SET PRC_EFFECTIVE_DATE=:PRC_EFFECTIVE_DATE,"
            + " TRANSFER_FEE_IND=:TRANSFER_FEE_IND,"
            + " LABEL_ASSESMENT_IND=:LABEL_ASSESMENT_IND,"
            + " LAST_UPDATE_TMSTMP=SYSTIMESTAMP,"
            + " LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID"
            + " where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ ";
    
    private static final String UPDATE_CONTRACT_PRICE_PROFILE_CONTRACT_NAME = " UPDATE "
            + " CONTRACT_PRICE_PROFILE "
            + " SET CLM_CONTRACT_NAME=:CLM_CONTRACT_NAME, "
            + " LAST_UPDATE_TMSTMP = SYSTIMESTAMP, "
            + " LAST_UPDATE_USER_ID =:LAST_UPDATE_USER_ID"
            + " WHERE"
            + " CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ ";
    
    
    private static final String UPDATE_CONTRACT_PRICE_PROFILE_DATES = "UPDATE CONTRACT_PRICE_PROFILE "
            + "SET PRC_EFFECTIVE_DATE=:PRC_EFFECTIVE_DATE, "
            + "CLM_CONTRACT_START_DATE=:CLM_CONTRACT_START_DATE, "
            + "CLM_CONTRACT_END_DATE=:CLM_CONTRACT_END_DATE, "
            + " LAST_UPDATE_TMSTMP=SYSTIMESTAMP,"
            + " LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID"
            + " where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ ";
    
    
    private static final String UPDATE_CONTRACT_STATUS_BY_CPP_SEQ = "UPDATE CONTRACT_PRICE_PROFILE SET "
            + "CONTRACT_PRC_PROF_STATUS_CODE=:CONTRACT_PRC_PROF_STATUS_CODE "
            + ", LAST_UPDATE_TMSTMP=SYSTIMESTAMP "
            + ", LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID "
            + " WHERE "
            + "CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ ";
    
    private static final String UPDATE_TO_PRICING_ACVITATED_STATUS = "UPDATE "
            + " CONTRACT_PRICE_PROFILE SET "
            + "CONTRACT_PRC_PROF_STATUS_CODE= "+ ContractPriceProfileStatus.PRICING_ACTIVATED.getCode()
            + ", LAST_UPDATE_TMSTMP=SYSTIMESTAMP "
            + ", LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID ,"
            + " PRICE_ACTIVATED_TMSTMP=SYSTIMESTAMP "
            + " WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ ";
    
    private static final String FETCH_CPP_NEXT_SEQ = "select CONTRACT_PRICE_PROFILE_SEQ1.nextVal from dual";
    
    private static final String FETCH_CONTRACT_PRICE_PROFILE_ID = "select CONTRACT_PRICE_PROFILE_ID "
                + "from CONTRACT_PRICE_PROFILE "
                + "where CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ";
    
    private static final String FETCH_CONTRACT_PRICE_PROFILE_ID_SEQ = "select CONTRACT_PRICE_PROFILE_SEQ2.nextVal from dual";
    
    private static final String UPDATE_PRICING_EXHIBIT_GUID = "UPDATE CONTRACT_PRICE_PROFILE "
            + "SET PRICING_EXHIBIT_GUID =:PRICING_EXHIBIT_GUID,"
            + "LAST_UPDATE_TMSTMP=SYSTIMESTAMP, "
            + "LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID "
            + "where CONTRACT_PRICE_PROFILE_SEQ =:CONTRACT_PRICE_PROFILE_SEQ "
            + "AND PRC_EXPIRATION_DATE>=SYSDATE";
    
    private static final String UPDATE_EXPIRE_LOWER_IND_BY_SEQ = "Update CONTRACT_PRICE_PROFILE SET " 
            + "EXPIRE_LOWER_LEVEL_IND=:EXPIRE_LOWER_LEVEL_IND, "
            + "LAST_UPDATE_TMSTMP=SYSTIMESTAMP, "
            + "LAST_UPDATE_USER_ID=:LAST_UPDATE_USER_ID "
            + "where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + "and PRC_EXPIRATION_DATE>= SYSDATE";
    
    private static final String FETCH_EXPIRE_LOWER_IND_BY_SEQ = "Select EXPIRE_LOWER_LEVEL_IND "
            + " from CONTRACT_PRICE_PROFILE "
            + "where CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ "
            + "and PRC_EXPIRATION_DATE>= SYSDATE"; 
  
    private static final String FETCH_LATEST_CONTRACT_VERSION_NUMBER = "Select max(VERSION_NBR) from "
            + "CONTRACT_PRICE_PROFILE where PARENT_CLM_AGREEMENT_ID = :PARENT_CLM_AGREEMENT_ID";
    
    private static final String FETCH_CONTRACT_IN_PROGRESS_VERSION_COUNT = "Select "
            + " COUNT(CONTRACT_PRICE_PROFILE_ID) "
            + " FROM CONTRACT_PRICE_PROFILE "
            + " WHERE PARENT_CLM_AGREEMENT_ID=:PARENT_CLM_AGREEMENT_ID "
            + " AND CONTRACT_PRC_PROF_STATUS_CODE IN ("
            +  ContractPriceProfileStatus.DRAFT.getCode() + ","  + ContractPriceProfileStatus.HOLD.getCode() + ","  
            + ContractPriceProfileStatus.WAITING_FOR_APPROVAL.getCode() + ","  + ContractPriceProfileStatus.CONTRACT_APPROVED.getCode()
            + ")";
    
    // @formatter:on

    public Integer fetchInProgressContractVersionCount(String parentAgreementId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(PARENT_CLM_AGREEMENT_ID, parentAgreementId, Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(FETCH_CONTRACT_IN_PROGRESS_VERSION_COUNT, paramMap, Integer.class);
    }

    public Integer fetchLatestContractVersionNumber(String parentAgreementId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(PARENT_CLM_AGREEMENT_ID, parentAgreementId, Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(FETCH_LATEST_CONTRACT_VERSION_NUMBER, paramMap, Integer.class);
    }

    public void saveContractPricingDetails(ContractPricingDO contractPricingDO, String userName, int contractTypeSeq, int contractPriceProfileSeq) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(VERSION_NBR, contractPricingDO.getVersionNbr(), Types.INTEGER);
        paramMap.addValue(PRC_EFFECTIVE_DATE, contractPricingDO.getPricingEffectiveDate(), Types.DATE);
        paramMap.addValue(PRC_EXPIRATION_DATE, contractPricingDO.getPricingExpirationDate(), Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_ID, contractPricingDO.getContractPriceProfileId(), Types.INTEGER);
        paramMap.addValue(CLM_AGREEMENT_ID, contractPricingDO.getAgreementId(), Types.VARCHAR);
        paramMap.addValue(CLM_CONTRACT_TYPE_SEQ, contractTypeSeq, Types.INTEGER);
        paramMap.addValue(CLM_CONTRACT_START_DATE, contractPricingDO.getClmContractStartDate(), Types.DATE);
        paramMap.addValue(CLM_CONTRACT_END_DATE, contractPricingDO.getClmContractEndDate(), Types.DATE);
        paramMap.addValue(PARENT_CLM_AGREEMENT_ID, contractPricingDO.getParentAgreementId(), Types.VARCHAR);
        paramMap.addValue(EXPIRE_LOWER_LEVEL_IND, contractPricingDO.getExpireLowerLevelInd(), Types.INTEGER);
        paramMap.addValue(LABEL_ASSESMENT_IND, contractPricingDO.getLabelAssesmentInd(), Types.INTEGER);
        paramMap.addValue(TRANSFER_FEE_IND, contractPricingDO.getTransferFeeInd(), Types.INTEGER);
        paramMap.addValue(CLM_CONTRACT_NAME, contractPricingDO.getContractName(), Types.VARCHAR);

        cppJdbcTemplate.update(INSERT_CONTRACT_PRICE_PROFILE_DETAILS, paramMap);
    }

    public int fetchCPPNextSequence() {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        return cppJdbcTemplate.queryForObject(FETCH_CPP_NEXT_SEQ, paramMap, Integer.class);
    }

    public int fetchCPPSequence(int contractPriceProfileId) {
        int contractPriceProfileSeq = 0;
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(CONTRACT_PRICE_PROFILE_ID, contractPriceProfileId, Types.INTEGER);
            contractPriceProfileSeq = cppJdbcTemplate.queryForObject(FETCH_CPP_SEQUENCE, paramMap, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.info("No records found for contract price profile Id {}", contractPriceProfileId);
            return 0;
        }
        return contractPriceProfileSeq;
    }

    public ContractPricingResponseDTO fetchContractDetailsByCppSeq(int contractPriceProfileSeq) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
            return cppJdbcTemplate.queryForObject(FETCH_CONTRACT_DETAILS_BY_CPP_SEQ, paramMap, new ContractPricingResponseDTORowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("No contract pricing records found for contract price profile seq {}", contractPriceProfileSeq);
            return null;
        }
    }

    public ContractPricingResponseDTO fetchContractDetailsByAgreementId(String agreementId) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(CLM_AGREEMENT_ID, agreementId, Types.VARCHAR);
            return cppJdbcTemplate.queryForObject(FETCH_CONTRACT_DETAILS_BY_AGREEMENT_ID, paramMap, new ContractPricingResponseDTORowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("No contract pricing records found for agreement id {}", agreementId);
            return null;
        }
    }

    public void updateContractPriceProfileContractName(int contractPriceProfileSeq, String clmContractName, String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CLM_CONTRACT_NAME, clmContractName, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        cppJdbcTemplate.update(UPDATE_CONTRACT_PRICE_PROFILE_CONTRACT_NAME, paramMap);
    }

    public void updateContractPriceProfile(int contractPriceProfileSeq, Date prcEffDate, String userName, int transferFeeInd, int labelAssesmentInd) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(PRC_EFFECTIVE_DATE, prcEffDate, Types.DATE);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(TRANSFER_FEE_IND, transferFeeInd, Types.INTEGER);
        paramMap.addValue(LABEL_ASSESMENT_IND, labelAssesmentInd, Types.INTEGER);
        cppJdbcTemplate.update(UPDATE_CONTRACT_PRICE_PROFILE, paramMap);
    }

    public void updateContractPriceProfileDates(int contractPriceProfileSeq, Date pricingEffectiveDate, Date contractStartDate, Date contractEndDate,
            String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(PRC_EFFECTIVE_DATE, pricingEffectiveDate, Types.DATE);
        paramMap.addValue(CLM_CONTRACT_START_DATE, contractStartDate, Types.DATE);
        paramMap.addValue(CLM_CONTRACT_END_DATE, contractEndDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        cppJdbcTemplate.update(UPDATE_CONTRACT_PRICE_PROFILE_DATES, paramMap);
    }

    public int fetchContractPriceProfileId(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_CONTRACT_PRICE_PROFILE_ID, paramMap, Integer.class);
    }

    public void updateContractStatusWithLastUpdateUserIdByCppSeq(int contractPriceProfileSeq, int statusCode, String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRC_PROF_STATUS_CODE, statusCode, Types.INTEGER);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        cppJdbcTemplate.update(UPDATE_CONTRACT_STATUS_BY_CPP_SEQ, paramMap);
    }

    public void updateToPriceActivateStatus(int contractPriceProfileSeq, String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        cppJdbcTemplate.update(UPDATE_TO_PRICING_ACVITATED_STATUS, paramMap);
    }

    public int fetchContractPriceProfileIdSeq() {
        return cppJdbcTemplate.queryForObject(FETCH_CONTRACT_PRICE_PROFILE_ID_SEQ, new MapSqlParameterSource(), Integer.class);
    }

    public CPPInformationDTO fetchCPPInformation(String agreementId) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(CLM_AGREEMENT_ID, agreementId, Types.VARCHAR);
            return cppJdbcTemplate.queryForObject(FETCH_CONTRACT_PRICE_PROFILE_INFO, paramMap, new CPPInformationDTORowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("No data found for CLM Agreement Id {}", agreementId);
            return null;
        }
    }

    public void updatePricingExhibitGuid(int contractPriceProfileSeq, String exhibitSysId, String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(PRICING_EXHIBIT_GUID, exhibitSysId, Types.VARCHAR);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        cppJdbcTemplate.update(UPDATE_PRICING_EXHIBIT_GUID, paramMap);
    }

    public void updateExpireLowerIndicator(int contractPriceProfileSeq, boolean isExpireLower, String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(EXPIRE_LOWER_LEVEL_IND, isExpireLower ? 1 : 0, Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        cppJdbcTemplate.update(UPDATE_EXPIRE_LOWER_IND_BY_SEQ, paramMap);

    }

    public Integer fetchExpireLowerIndicator(Integer contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_EXPIRE_LOWER_IND_BY_SEQ, paramMap, Integer.class);
    }

    public ContractPricingResponseDTO fetchContractDetailsForLatestActivatedContractVersion(String parentAgreementId) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(PARENT_CLM_AGREEMENT_ID, parentAgreementId, Types.VARCHAR);
            return cppJdbcTemplate.queryForObject(FETCH_CONTRACT_DETAILS_FOR_LATEST_CONTRACT_VERSION, paramMap,
                    new ContractPricingResponseDTORowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("No contract pricing records found for contract price profile clm agreement id {}", parentAgreementId);
            return null;
        }
    }

    public List<ContractSearchResultDTO> fetchContractDetailsByContractName(String contractName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(SEARCH_CONTRACT_NAME, "%" + contractName.toUpperCase() + "%", Types.VARCHAR);

        return cppJdbcTemplate.query(FETCH_ALL_CONTRACT_DETAILS_BY_CONTRACT_NAME, paramMap, new ContractSearchResultDTORowMapper());
    }

    public List<ContractSearchResultDTO> fetchContractDetailsbyCPPId(long contractPriceProfileId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_ID, contractPriceProfileId, Types.VARCHAR);

        return cppJdbcTemplate.query(FETCH_ALL_CONTRACT_DETAILS_BY_CPP_ID, paramMap, new ContractSearchResultDTORowMapper());
    }

    public List<ContractSearchResultDTO> fetchContractDetailsByCustomer(String gfsCustomerId, int gfsCustomerTypeCode) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.VARCHAR);

        return cppJdbcTemplate.query(FETCH_ALL_CONTRACT_DETAILS_BY_CUSTOMER, paramMap, new ContractSearchResultDTORowMapper());
    }
}