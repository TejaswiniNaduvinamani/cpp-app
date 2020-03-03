package com.gfs.cpp.data.auditauthority;

import static com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRowMapper.FETCH_PRC_PROF_AUDIT_AUTHORITY_FOR_CPP_SEQ;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfAuditAuthorityDTO;
import com.gfs.cpp.common.model.auditauthority.PrcProfAuditAuthorityDO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.data.contractpricing.ContractPricingParamListBuilder;

@Repository(value = "prcProfAuditAuthorityRepository")
public class PrcProfAuditAuthorityRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    @Autowired
    private ContractPricingParamListBuilder contractPricingParamListBuilder;

    @Autowired
    private PrcProfAuditAuthorityParamListBuilder prcProfAuditAuthorityParamListBuilder;

    private static final String PRC_PROF_AUDIT_AUTHORITY_IND = "PRC_PROF_AUDIT_AUTHORITY_IND";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String NEW_EFFECTIVE_DATE = "NEW_EFFECTIVE_DATE";
    private static final String NEW_EXPIRATION_DATE = "NEW_EXPIRATION_DATE";

 // @formatter:off
    private static final String INSERT_PRC_PROF_AUDIT_AUTHORITY = "Insert into PRC_PROF_AUDIT_AUTHORITY "
            + "(PRC_PROF_AUDIT_AUTHORITY_SEQ," 
            + " GFS_CUSTOMER_ID," 
            + " GFS_CUSTOMER_TYPE_CODE," 
            + " EFFECTIVE_DATE,"
            + " EXPIRATION_DATE," 
            + " CREATE_USER_ID," 
            + " LAST_UPDATE_USER_ID," 
            + " PRC_PROF_AUDIT_AUTHORITY_IND,"
            + " CONTRACT_PRICE_PROFILE_SEQ) " 
            + " values (PRC_PROF_AUDIT_AUTHORITY_SEQ.nextVal," 
            + " :GFS_CUSTOMER_ID,"
            + " :GFS_CUSTOMER_TYPE_CODE," 
            + " :EFFECTIVE_DATE," 
            + " :EXPIRATION_DATE,"
            + " :CREATE_USER_ID," 
            + " :LAST_UPDATE_USER_ID," 
            + " :PRC_PROF_AUDIT_AUTHORITY_IND,"
            + " :CONTRACT_PRICE_PROFILE_SEQ)";
    
    private static final String DELETE_PRC_PROF_AUDIT_AUTHORITY = "DELETE from PRC_PROF_AUDIT_AUTHORITY "
            + " where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ ";
    
    private static final String FETCH_PRC_PROF_AUDIT_AUTHORITY = "select PRC_PROF_AUDIT_AUTHORITY_IND "
            + "from PRC_PROF_AUDIT_AUTHORITY "
            + " WHERE  CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + " AND GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + " AND GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE ";
    
     private static final String EXPIRE_PRC_PROF_AUDIT_AUTHORITY_FOR_NON_CMG_CUSTOMERS_BY_CPP_SEQ = "UPDATE PRC_PROF_AUDIT_AUTHORITY "
            + "SET EXPIRATION_DATE              = :EXPIRATION_DATE, "
            + "LAST_UPDATE_TMSTMP               = SYSTIMESTAMP, "
            + "LAST_UPDATE_USER_ID              = :LAST_UPDATE_USER_ID "
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "AND EXPIRATION_DATE             > :EXPIRATION_DATE "
            + "AND EFFECTIVE_DATE <= EXPIRATION_DATE "
            + "AND GFS_CUSTOMER_TYPE_CODE != "+CPPConstants.CMG_CUSTOMER_TYPE_CODE+"";
    
    private static final String EXPIRE_PRC_PROF_AUDIT_AUTH_FOR_REAL_CUST = "UPDATE PRC_PROF_AUDIT_AUTHORITY "
            + " SET EXPIRATION_DATE              = :EXPIRATION_DATE, "
            + " LAST_UPDATE_TMSTMP               = SYSTIMESTAMP, "
            + " LAST_UPDATE_USER_ID              = :LAST_UPDATE_USER_ID "
            + " WHERE GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + " AND GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE"
            + " AND EXPIRATION_DATE             >= :NEW_EFFECTIVE_DATE"
            + " AND EFFECTIVE_DATE              <= :NEW_EXPIRATION_DATE"
            + " AND EFFECTIVE_DATE <= EXPIRATION_DATE";
    
    private static final String UPDATE_PRC_PROF_AUDIT_AUTHORITY = "UPDATE PRC_PROF_AUDIT_AUTHORITY"
            + " SET PRC_PROF_AUDIT_AUTHORITY_IND = :PRC_PROF_AUDIT_AUTHORITY_IND, "
            + " LAST_UPDATE_TMSTMP               = SYSTIMESTAMP, "
            + " LAST_UPDATE_USER_ID              = :LAST_UPDATE_USER_ID "
            + " WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ "
            + " AND EXPIRATION_DATE >= SYSDATE";

   // @formatter:on

    public void savePriceProfileAuditForCustomer(List<PrcProfAuditAuthorityDO> prcProfAuditAuthorityDOList) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (PrcProfAuditAuthorityDO prcProfAuditAuthorityDO : prcProfAuditAuthorityDOList) {
            MapSqlParameterSource paramMap = prcProfAuditAuthorityParamListBuilder.createParamMap(prcProfAuditAuthorityDO);
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(INSERT_PRC_PROF_AUDIT_AUTHORITY, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public List<PrcProfAuditAuthorityDTO> fetchPrcProfAuditAuthorityForCPPSeq(int contractPriceProfileSeq, CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, cmgCustomerResponseDTO.getId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, cmgCustomerResponseDTO.getTypeCode(), Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_PRC_PROF_AUDIT_AUTHORITY_FOR_CPP_SEQ, paramMap, new PrcProfAuditAuthorityRowMapper());
    }

    public int fetchPriceAuditIndicator(int contractPriceProfileSeq, String customerId, int customerTypeCode) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, customerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, customerTypeCode, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_PRC_PROF_AUDIT_AUTHORITY, paramMap, Integer.class);
    }

    public void savePriceProfileAuditIndicator(String userName, int contractPriceProfileSeq, ContractPricingDO contractPricingDO,
            int auditAuthorityIndicator) {
        MapSqlParameterSource paramMap = contractPricingParamListBuilder.createParamMap(userName, contractPriceProfileSeq,
                contractPricingDO.getContractPriceProfileId(), contractPricingDO);
        paramMap.addValue(PRC_PROF_AUDIT_AUTHORITY_IND, auditAuthorityIndicator, Types.INTEGER);
        cppJdbcTemplate.update(INSERT_PRC_PROF_AUDIT_AUTHORITY, paramMap);
    }

    public void deletePriceProfAudit(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        cppJdbcTemplate.update(DELETE_PRC_PROF_AUDIT_AUTHORITY, paramMap);
    }

    public void expireNonCmgPriceProfAuditForContract(int contractPriceProfileSeq, Date expirationDate, String updatedUserId) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);

        cppJdbcTemplate.update(EXPIRE_PRC_PROF_AUDIT_AUTHORITY_FOR_NON_CMG_CUSTOMERS_BY_CPP_SEQ, paramMap);
    }

    public void expirePriceProfAuditForRealCust(Date expirationDate, String updatedUserId, String customerId, int customerTypeCode,
            Date newPricingEffectiveDate, Date newPricingExpiryDate) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, customerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, customerTypeCode, Types.INTEGER);
        paramMap.addValue(NEW_EFFECTIVE_DATE, newPricingEffectiveDate, Types.DATE);
        paramMap.addValue(NEW_EXPIRATION_DATE, newPricingExpiryDate, Types.DATE);
        cppJdbcTemplate.update(EXPIRE_PRC_PROF_AUDIT_AUTH_FOR_REAL_CUST, paramMap);
    }

    public void updatePriceProfileAuditIndicator(String userName, int contractPriceProfileSeq, int auditAuthorityIndicator) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(PRC_PROF_AUDIT_AUTHORITY_IND, auditAuthorityIndicator, Types.INTEGER);
        cppJdbcTemplate.update(UPDATE_PRC_PROF_AUDIT_AUTHORITY, paramMap);
    }
}
