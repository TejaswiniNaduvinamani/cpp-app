package com.gfs.cpp.data.assignment;

import static com.gfs.cpp.data.assignment.DuplicateCustomerDTORowMapper.FETCH_DUPLICATE_CUSTOMER_ACROSS_CONCEPT;
import static com.gfs.cpp.data.assignment.RealCustomerDTORowMapper.FETCH_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT;
import static com.gfs.cpp.data.assignment.RealCustomerDTORowMapper.FETCH_SAVED_ASSIGNMENT_LIST;
import static com.gfs.cpp.data.assignment.RealCustomerDTORowMapper.FETCH_SAVED_ASSIGNMENT_LIST_FOR_CPP_SEQ;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.model.assignments.CustomerAssignmentDO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;

@Repository(value = "cppConceptMappingRepository")
public class CppConceptMappingRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    public static final Logger logger = LoggerFactory.getLogger(CppConceptMappingRepository.class);

    private static final String CPP_CUSTOMER_SEQ = "CPP_CUSTOMER_SEQ";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String CONTRACT_PRICE_PROFILE_ID = "CONTRACT_PRICE_PROFILE_ID";
    private static final String CLM_CONTRACT_START_DATE = "CLM_CONTRACT_START_DATE";
    private static final String CLM_CONTRACT_END_DATE = "CLM_CONTRACT_END_DATE";

    // @formatter:off
    
    private static final String INSERT_CPP_CONCEPT_MAPPING = "Insert into CPP_CONCEPT_MAPPING "
            + "(CPP_CONCEPT_MAPPING_SEQ," 
            + "CPP_CUSTOMER_SEQ," 
            + "GFS_CUSTOMER_ID," 
            + "GFS_CUSTOMER_TYPE_CODE,"
            + "LAST_UPDATE_USER_ID,"
            + "LAST_UPDATE_TMSTMP,"
            + "CREATE_USER_ID ) "
            + "values (CPP_CONCEPT_MAPPING_SEQ.nextval , " 
            + ":CPP_CUSTOMER_SEQ , "
            + ":GFS_CUSTOMER_ID , " 
            + ":GFS_CUSTOMER_TYPE_CODE,"
            + ":LAST_UPDATE_USER_ID," 
            + "CURRENT_TIMESTAMP," 
            + ":CREATE_USER_ID)";

    private static final String DELETE_CPP_CONCEPT_MAPPING = "DELETE from CPP_CONCEPT_MAPPING "
            + "where GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID and "
            + "GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE ";
    
 
    
    private static final String FETCH_DUPLICATE_ACCROSS_ACTIVE_CONTRACTS = "select "
            + "c.GFS_CUSTOMER_ID, "
            + "c.GFS_CUSTOMER_TYPE_CODE, "
            + "cpp.CONTRACT_PRICE_PROFILE_SEQ "
            + "from CONTRACT_PRICE_PROFILE cpp "
            + "inner join CONTRACT_PRICE_PROF_CUSTOMER c "
            + "on cpp.contract_price_profile_seq = c.contract_price_profile_seq "
            + "inner join  CPP_CONCEPT_MAPPING  m "
            + "on c.cpp_customer_seq = m.cpp_customer_seq "
            + "where cpp.CONTRACT_PRC_PROF_STATUS_CODE "
            + "in ("
            + ContractPriceProfileStatus.DRAFT.code + ", " 
            + ContractPriceProfileStatus.HOLD.code +", "
            + ContractPriceProfileStatus.WAITING_FOR_APPROVAL.code +", "
            + ContractPriceProfileStatus.CONTRACT_APPROVED.code +","
            + ContractPriceProfileStatus.PRICING_ACTIVATED.code 
            + ") "
            + "and cpp.CLM_CONTRACT_END_DATE >= :CLM_CONTRACT_START_DATE "
            + "and cpp.CLM_CONTRACT_START_DATE <= :CLM_CONTRACT_END_DATE "
            + "and cpp.CLM_CONTRACT_START_DATE <= cpp.CLM_CONTRACT_END_DATE "
            + "and m.GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + "and m.GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE "
            + "And cpp.CONTRACT_PRICE_PROFILE_ID <> :CONTRACT_PRICE_PROFILE_ID";
    
    private static final String DELETE_ALL_CONCEPT_MAPPINGS ="DELETE FROM CPP_CONCEPT_MAPPING "
            + " WHERE CPP_CUSTOMER_SEQ IN ( SELECT "
            + " CPP_CUSTOMER_SEQ FROM CONTRACT_PRICE_PROF_CUSTOMER"
    		+ " WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ)";

    
    // @formatter:on

    public void saveAssignments(List<CustomerAssignmentDO> saveAssignmentDOList, String userName) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (CustomerAssignmentDO saveAssignmentDO : saveAssignmentDOList) {
            MapSqlParameterSource paramMap = buildParamMapForSaveAssignment(userName, saveAssignmentDO);
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(INSERT_CPP_CONCEPT_MAPPING, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public List<RealCustomerDTO> fetchRealCustomersMapped(int cppCustomerSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_CUSTOMER_SEQ, cppCustomerSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_SAVED_ASSIGNMENT_LIST, paramMap, new RealCustomerDTORowMapper());
    }

    public List<RealCustomerDTO> fetchRealCustomersMappedForCppSeq(int cppCustomerSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_CUSTOMER_SEQ, cppCustomerSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_SAVED_ASSIGNMENT_LIST_FOR_CPP_SEQ, paramMap, new RealCustomerDTORowMapper());
    }

    public RealCustomerDTO fetchRealCustomerMappedToDefaultConcept(int contractPriceProfileSeq) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
            return cppJdbcTemplate.queryForObject(FETCH_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT, paramMap, new RealCustomerDTORowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public List<DuplicateCustomerDTO> fetchDuplicateCustomersAcrossConcepts(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerType) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
            paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
            paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerType, Types.INTEGER);
            return cppJdbcTemplate.query(FETCH_DUPLICATE_CUSTOMER_ACROSS_CONCEPT, paramMap, new DuplicateCustomerDTORowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>();
        }
    }

    public List<DuplicateCustomerDTO> fetchDuplicateCustomersAcrossOtherActiveContracts(int contractPriceProfileId, String gfsCustomerId,
            int gfsCustomerType, Date clmContractStartDate, Date clmContractEndDate) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(CONTRACT_PRICE_PROFILE_ID, contractPriceProfileId, Types.INTEGER);
            paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
            paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerType, Types.INTEGER);
            paramMap.addValue(CLM_CONTRACT_START_DATE, clmContractStartDate, Types.DATE);
            paramMap.addValue(CLM_CONTRACT_END_DATE, clmContractEndDate, Types.DATE);
            return cppJdbcTemplate.query(FETCH_DUPLICATE_ACCROSS_ACTIVE_CONTRACTS, paramMap, new DuplicateCustomerDTORowMapper());
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>();
        }
    }

    public void deleteCustomerMapping(CustomerAssignmentDO customerAssignmentDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(GFS_CUSTOMER_ID, customerAssignmentDO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, customerAssignmentDO.getGfsCustomerType(), Types.INTEGER);
        cppJdbcTemplate.update(DELETE_CPP_CONCEPT_MAPPING, paramMap);
    }

    public void deleteAllCustomerMapping(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        cppJdbcTemplate.update(DELETE_ALL_CONCEPT_MAPPINGS, paramMap);
    }

    private MapSqlParameterSource buildParamMapForSaveAssignment(String userName, CustomerAssignmentDO customerAssignmentDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_CUSTOMER_SEQ, customerAssignmentDO.getCppCustomerSeq(), Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, customerAssignmentDO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, customerAssignmentDO.getGfsCustomerType(), Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        return paramMap;
    }
}
