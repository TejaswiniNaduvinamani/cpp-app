package com.gfs.cpp.data.contractpricing;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;

public class ContractPricingResponseDTORowMapper implements RowMapper<ContractPricingResponseDTO> {

    private static final String CLM_CONTRACT_END_DATE = "CLM_CONTRACT_END_DATE";
    private static final String CLM_CONTRACT_START_DATE = "CLM_CONTRACT_START_DATE";
    private static final String PARENT_CLM_AGREEMENT_ID = "PARENT_CLM_AGREEMENT_ID";
    private static final String PRC_EFFECTIVE_DATE = "PRC_EFFECTIVE_DATE";
    private static final String PRC_EXPIRATION_DATE = "PRC_EXPIRATION_DATE";
    private static final String CLM_CONTRACT_TYPE_SEQ = "CLM_CONTRACT_TYPE_SEQ";
    private static final String CONTRACT_PRC_PROF_STATUS_CODE = "CONTRACT_PRC_PROF_STATUS_CODE";
    private static final String CLM_AGREEMENT_ID = "CLM_AGREEMENT_ID";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String PRICING_EXHIBIT_GUID = "PRICING_EXHIBIT_GUID";
    private static final String CONTRACT_PRICE_PROFILE_ID = "CONTRACT_PRICE_PROFILE_ID";
    private static final String EXPIRE_LOWER_LEVEL_IND = "EXPIRE_LOWER_LEVEL_IND";
    private static final String VERSION_NBR = "VERSION_NBR";
    private static final String CLM_CONTRACT_NAME = "CLM_CONTRACT_NAME";
    private static final String TRANSFER_FEE_IND = "TRANSFER_FEE_IND";
    private static final String LABEL_ASSESMENT_IND = "LABEL_ASSESMENT_IND";

    // @formatter:off
    
    private static final String SELECT_BASE_COLUMNS_FROM_CONTRACT_PRICE_PROFILE = "SELECT " 
            + " CLM_CONTRACT_TYPE_SEQ, "
            + " CONTRACT_PRICE_PROFILE_SEQ, " 
            + " PRC_EFFECTIVE_DATE, " 
            + " PRC_EXPIRATION_DATE, "
            + " VERSION_NBR, " 
            + " CONTRACT_PRC_PROF_STATUS_CODE, "
            + " PRICING_EXHIBIT_GUID, " 
            + " CLM_AGREEMENT_ID, " 
            + " CONTRACT_PRICE_PROFILE_ID," 
            + " EXPIRE_LOWER_LEVEL_IND, "
            + " PARENT_CLM_AGREEMENT_ID, "
            + " CLM_CONTRACT_START_DATE, "
            + " CLM_CONTRACT_END_DATE,"
            + " CLM_CONTRACT_NAME,"
            + " TRANSFER_FEE_IND,"
            + " LABEL_ASSESMENT_IND"
            + " FROM "
            + " CONTRACT_PRICE_PROFILE ";
    
    public static final String FETCH_CONTRACT_DETAILS_FOR_LATEST_CONTRACT_VERSION = SELECT_BASE_COLUMNS_FROM_CONTRACT_PRICE_PROFILE
            + " WHERE "
            + " PARENT_CLM_AGREEMENT_ID =:PARENT_CLM_AGREEMENT_ID "
            + " AND CONTRACT_PRC_PROF_STATUS_CODE="+ContractPriceProfileStatus.PRICING_ACTIVATED.getCode() 
            + " AND PRICE_ACTIVATED_TMSTMP = (Select max(PRICE_ACTIVATED_TMSTMP) "
            + " from CONTRACT_PRICE_PROFILE "
            + " where PARENT_CLM_AGREEMENT_ID =:PARENT_CLM_AGREEMENT_ID "
            + " AND CONTRACT_PRC_PROF_STATUS_CODE="+ContractPriceProfileStatus.PRICING_ACTIVATED.getCode() +")" ;
    
    public static final String FETCH_CONTRACT_DETAILS_BY_CPP_SEQ = SELECT_BASE_COLUMNS_FROM_CONTRACT_PRICE_PROFILE
            + " where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ";
    
    public static final String FETCH_CONTRACT_DETAILS_BY_AGREEMENT_ID = SELECT_BASE_COLUMNS_FROM_CONTRACT_PRICE_PROFILE
            + "WHERE CLM_AGREEMENT_ID = :CLM_AGREEMENT_ID";
   
    
    // @formatter:on

    @Override
    public ContractPricingResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setPricingEffectiveDate(rs.getDate(PRC_EFFECTIVE_DATE));
        contractPricingResponseDTO.setPricingExpirationDate(rs.getDate(PRC_EXPIRATION_DATE));
        contractPricingResponseDTO.setClmContractTypeSeq(rs.getInt(CLM_CONTRACT_TYPE_SEQ));
        contractPricingResponseDTO.setContractPriceProfStatusCode(rs.getInt(CONTRACT_PRC_PROF_STATUS_CODE));
        contractPricingResponseDTO.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        contractPricingResponseDTO.setClmAgreementId(rs.getString(CLM_AGREEMENT_ID));
        contractPricingResponseDTO.setPricingExhibitSysId(rs.getString(PRICING_EXHIBIT_GUID));
        contractPricingResponseDTO.setVersionNumber(rs.getInt(VERSION_NBR));
        contractPricingResponseDTO.setContractPriceProfileId(rs.getInt(CONTRACT_PRICE_PROFILE_ID));
        contractPricingResponseDTO.setExpireLowerLevelInd(rs.getInt(EXPIRE_LOWER_LEVEL_IND));
        contractPricingResponseDTO.setClmParentAgreementId(rs.getString(PARENT_CLM_AGREEMENT_ID));
        contractPricingResponseDTO.setClmContractStartDate(rs.getDate(CLM_CONTRACT_START_DATE));
        contractPricingResponseDTO.setClmContractEndDate(rs.getDate(CLM_CONTRACT_END_DATE));
        contractPricingResponseDTO.setClmContractName(rs.getString(CLM_CONTRACT_NAME));
        contractPricingResponseDTO.setAssessmentFeeFlag(rs.getInt(LABEL_ASSESMENT_IND));
        contractPricingResponseDTO.setTransferFeeFlag(rs.getInt(TRANSFER_FEE_IND));
        return contractPricingResponseDTO;
    }

}
