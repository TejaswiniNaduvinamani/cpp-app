package com.gfs.cpp.data.contractpricing;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.dto.search.ContractSearchResultDTO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.common.util.ContractTypeForSearchEnum;

public class ContractSearchResultDTORowMapper implements RowMapper<ContractSearchResultDTO> {

    private static final String VERSION_NBR = "VERSION_NBR";
    private static final String CLM_CONTRACT_NAME = "CLM_CONTRACT_NAME";
    private static final String CLM_AGREEMENT_ID = "CLM_AGREEMENT_ID";
    private static final String CONTRACT_PRC_PROF_STATUS_CODE = "CONTRACT_PRC_PROF_STATUS_CODE";
    private static final String PRC_EFFECTIVE_DATE = "PRC_EFFECTIVE_DATE";
    private static final String CONTRACT_PRICE_PROFILE_ID = "CONTRACT_PRICE_PROFILE_ID";
    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";
    private static final String CLM_CONTRACT_TYPE_SEQ = "CLM_CONTRACT_TYPE_SEQ";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String PARENT_CLM_AGREEMENT_ID = "PARENT_CLM_AGREEMENT_ID";

 // @formatter:off
    
    public static final String SELECT_COLUMNS_FROM_CONTRACT_PRICE_PROFILE_FOR_CONTRACT_SEARCH = "SELECT "
            + "CPP.CLM_CONTRACT_NAME, "
            + "CPP.CLM_CONTRACT_TYPE_SEQ, "
            + "CPP.CONTRACT_PRICE_PROFILE_ID, "
            + "CPP.VERSION_NBR, "
            + "CPP.CLM_AGREEMENT_ID, "
            + "CPP.PARENT_CLM_AGREEMENT_ID, "
            + "CPP.PRC_EFFECTIVE_DATE, "
            + "CPP.CONTRACT_PRC_PROF_STATUS_CODE, "
            + "CPPF.CPP_FURTHERANCE_SEQ, "
            + "CCM.GFS_CUSTOMER_ID, "
            + "CCM.GFS_CUSTOMER_TYPE_CODE "
            + "FROM CONTRACT_PRICE_PROFILE CPP "
            + "LEFT JOIN CPP_FURTHERANCE CPPF "
            + "ON CPPF.CONTRACT_PRICE_PROFILE_SEQ = CPP.CONTRACT_PRICE_PROFILE_SEQ "
            + "AND CPPF.FURTHERANCE_STATUS_CODE IN "+ "(" + FurtheranceStatus.FURTHERANCE_INITIATED.getCode() + ", "
            + FurtheranceStatus.FURTHERANCE_SAVED.getCode() + ")" 
            + " LEFT JOIN CONTRACT_PRICE_PROF_CUSTOMER CPPC "
            + " ON CPPC.CONTRACT_PRICE_PROFILE_SEQ = CPP.CONTRACT_PRICE_PROFILE_SEQ"
            + " AND CPPC.DEFAULT_CUSTOMER_IND = 1"
            + " LEFT JOIN CPP_CONCEPT_MAPPING CCM ON"
            + " CCM.CPP_CUSTOMER_SEQ = CPPC.CPP_CUSTOMER_SEQ" 
            + " WHERE CPP.CONTRACT_PRC_PROF_STATUS_CODE != "+ ContractPriceProfileStatus.DELETED.getCode();
    
    public static final String FETCH_ALL_CONTRACT_DETAILS_BY_CONTRACT_NAME = SELECT_COLUMNS_FROM_CONTRACT_PRICE_PROFILE_FOR_CONTRACT_SEARCH
            + " AND UPPER(CPP.CLM_CONTRACT_NAME) LIKE :SEARCH_CONTRACT_NAME";

    public static final String FETCH_ALL_CONTRACT_DETAILS_BY_CPP_ID = SELECT_COLUMNS_FROM_CONTRACT_PRICE_PROFILE_FOR_CONTRACT_SEARCH
            + " AND CPP.CONTRACT_PRICE_PROFILE_ID = :CONTRACT_PRICE_PROFILE_ID";
    
    public static final String FETCH_ALL_CONTRACT_DETAILS_BY_CUSTOMER = SELECT_COLUMNS_FROM_CONTRACT_PRICE_PROFILE_FOR_CONTRACT_SEARCH
            + " AND CCM.GFS_CUSTOMER_ID= :GFS_CUSTOMER_ID AND CCM.GFS_CUSTOMER_TYPE_CODE=:GFS_CUSTOMER_TYPE_CODE";
            
 // @formatter:on

    @Override
    public ContractSearchResultDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ContractSearchResultDTO contractSearchResultDTO = new ContractSearchResultDTO();
        contractSearchResultDTO.setAgreementId(rs.getString(CLM_AGREEMENT_ID));
        contractSearchResultDTO.setContractName(rs.getString(CLM_CONTRACT_NAME));
        contractSearchResultDTO.setContractPriceProfileId(rs.getInt(CONTRACT_PRICE_PROFILE_ID));
        contractSearchResultDTO.setEffectiveDate(rs.getDate(PRC_EFFECTIVE_DATE));
        contractSearchResultDTO.setIsFurtheranceExist(rs.getString(CPP_FURTHERANCE_SEQ) == null ? CPPConstants.NO : CPPConstants.YES);
        contractSearchResultDTO.setContractType(ContractTypeForSearchEnum.getDescByCode((rs.getInt(CLM_CONTRACT_TYPE_SEQ))));
        contractSearchResultDTO.setVersion(rs.getInt(VERSION_NBR));
        contractSearchResultDTO.setStatus(ContractPriceProfileStatus.getStatusByCode((rs.getInt(CONTRACT_PRC_PROF_STATUS_CODE))).getDesc());
        contractSearchResultDTO.setGfsCustomerId(rs.getString(GFS_CUSTOMER_ID));
        contractSearchResultDTO.setGfsCustomerTypeCode(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        contractSearchResultDTO.setParentAgreementId(rs.getString(PARENT_CLM_AGREEMENT_ID));
        return contractSearchResultDTO;
    }

}
