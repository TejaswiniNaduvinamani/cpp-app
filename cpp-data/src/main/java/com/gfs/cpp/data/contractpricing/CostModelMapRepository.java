package com.gfs.cpp.data.contractpricing;

import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository(value = "costModelMapRepository")
public class CostModelMapRepository {

    public static final Logger logger = LoggerFactory.getLogger(CostModelMapRepository.class);

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    private static final String TRANSFER_FEE_IND = "TRANSFER_FEE_IND";
    private static final String LABEL_ASSESSMENT_IND = "LABEL_ASSESMENT_IND";
    private static final String AUDIT_PRICING_IND = "AUDIT_PRICING_IND";
    private static final String PRICE_VERIFICATION_IND = "PRICE_VERIFICATION_IND";

 // @formatter:off
    private static final String FETCH_COST_MODEL_MAP_ID = "Select COST_MODEL_ID from COST_MODEL_MAP "
            + "where TRANSFER_FEE_IND = :TRANSFER_FEE_IND " 
            + "and LABEL_ASSESMENT_IND = :LABEL_ASSESMENT_IND "
            + "and AUDIT_PRICING_IND= :AUDIT_PRICING_IND " 
            + "and PRICE_VERIFICATION_IND = :PRICE_VERIFICATION_IND";
    
 // @formatter:on
    public Integer fetchCostModelId(int transferFee, int labelAssessmentInd, int priceAuditInd, int prcProfVerificationInd) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(TRANSFER_FEE_IND, transferFee, Types.INTEGER);
            paramMap.addValue(LABEL_ASSESSMENT_IND, labelAssessmentInd, Types.INTEGER);
            paramMap.addValue(AUDIT_PRICING_IND, priceAuditInd, Types.INTEGER);
            paramMap.addValue(PRICE_VERIFICATION_IND, prcProfVerificationInd, Types.INTEGER);
            return cppJdbcTemplate.queryForObject(FETCH_COST_MODEL_MAP_ID, paramMap, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.info("No cost model found for the combination");
            return null;
        }
    }

}
