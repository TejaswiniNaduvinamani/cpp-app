package com.gfs.cpp.data.contractpricing;

import java.sql.Types;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;

@Repository(value = "prcProfVerificationPrivlgRepository")
public class PrcProfVerificationPrivlgRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    private static final String PRC_PROF_VERIF_PRIVILEGE_IND = "PRC_PROF_VERIF_PRIVILEGE_IND";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";

    // @formatter:off
    private static final String INSERT_PRC_PROF_VERIFICATION_PRIVLG = "Insert into PRC_PROF_VERIFICATION_PRIVLG "
            + "(PRC_PROF_VERIF_PRIVLG_SEQ," 
            + "CONTRACT_PRICE_PROFILE_SEQ," 
            + "EFFECTIVE_DATE," 
            + "EXPIRATION_DATE,"
            + "PRC_PROF_VERIF_PRIVILEGE_IND," 
            + "LAST_UPDATE_USER_ID," 
            + "CREATE_USER_ID) "
            + " values (PRC_PROF_VERIF_PRIVLG_SEQ.nextVal," 
            + ":CONTRACT_PRICE_PROFILE_SEQ,"
            + ":EFFECTIVE_DATE," 
            + ":EXPIRATION_DATE," 
            + ":PRC_PROF_VERIF_PRIVILEGE_IND,"
            + ":LAST_UPDATE_USER_ID," 
            + ":CREATE_USER_ID)";
    
    private static final String DELETE_PRC_PROF_VERIFICATION_PRIVLG = "DELETE from PRC_PROF_VERIFICATION_PRIVLG "
            + " where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ";

    private static final String EXPIRE_PRC_PROF_VERIFICATION_PRIVLG = "UPDATE PRC_PROF_VERIFICATION_PRIVLG " 
            + "SET EXPIRATION_DATE              = :EXPIRATION_DATE, " 
            + "  LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + "  LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "AND EXPIRATION_DATE             > :EXPIRATION_DATE ";
    
    private static final String UPDATE_PRC_PROF_VERIFICATION_PRIVLG_EFFECTIVE_DATE = "UPDATE PRC_PROF_VERIFICATION_PRIVLG " 
            + "SET EFFECTIVE_DATE              = :EFFECTIVE_DATE, " 
            + "  LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + "  LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "AND EXPIRATION_DATE             >= SYSDATE ";
    
    private static final String UPDATE_PRC_PROF_VERIFICATION_PRIVLG = "UPDATE PRC_PROF_VERIFICATION_PRIVLG " 
            + "SET PRC_PROF_VERIF_PRIVILEGE_IND = :PRC_PROF_VERIF_PRIVILEGE_IND, "
            + "EFFECTIVE_DATE                   = :EFFECTIVE_DATE, " 
            + " EXPIRATION_DATE                 = :EXPIRATION_DATE, " 
            + "  LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + "  LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "AND EXPIRATION_DATE             >= SYSDATE";
    
    private static final String FETCH_PRC_PROF_VERIF_PRIVILEGE_IND_FOR_CPP_SEQ = "select PRC_PROF_VERIF_PRIVILEGE_IND "
            + "from PRC_PROF_VERIFICATION_PRIVLG "
            + "where CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ ";
    // @formatter:on

    public void savePriceVerifyPrivileges(int contractPriceProfileSeq, String userName, Date effectiveDate, Date expirationDate,
            int prcProfPricePrevInd) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(PRC_PROF_VERIF_PRIVILEGE_IND, prcProfPricePrevInd, Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(EFFECTIVE_DATE, effectiveDate, Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        cppJdbcTemplate.update(INSERT_PRC_PROF_VERIFICATION_PRIVLG, paramMap);
    }

    public void deletePriceVerifyPrivileges(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        cppJdbcTemplate.update(DELETE_PRC_PROF_VERIFICATION_PRIVLG, paramMap);
    }

    public void expirePriceProfileVerficationPrivlg(int contractPriceProfileSeq, Date expirationDate, String updatedUserId) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);

        cppJdbcTemplate.update(EXPIRE_PRC_PROF_VERIFICATION_PRIVLG, paramMap);

    }

    public void updatePriceProfileVerficationPrivlgEffectiveDate(int contractPriceProfileSeq, Date effectiveDate, String updatedUserId) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(EFFECTIVE_DATE, effectiveDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);

        cppJdbcTemplate.update(UPDATE_PRC_PROF_VERIFICATION_PRIVLG_EFFECTIVE_DATE, paramMap);

    }

    public void updatePriceProfileVerficationPrivlgInd(ContractPricingDO contractPricingDO, int prcProfPricePriviligeInd, String updatedUserId) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPricingDO.getContractPriceProfileSeq(), Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);
        paramMap.addValue(PRC_PROF_VERIF_PRIVILEGE_IND, prcProfPricePriviligeInd, Types.INTEGER);
        paramMap.addValue(EFFECTIVE_DATE, contractPricingDO.getPricingEffectiveDate(), Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, contractPricingDO.getPricingExpirationDate(), Types.DATE);
        cppJdbcTemplate.update(UPDATE_PRC_PROF_VERIFICATION_PRIVLG, paramMap);

    }

    public int fetchPricePriviligeIndicator(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_PRC_PROF_VERIF_PRIVILEGE_IND_FOR_CPP_SEQ, paramMap, Integer.class);
    }

}
