package com.gfs.cpp.data.auditauthority;

import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.model.auditauthority.PrcProfAuditAuthorityDO;

@Component(value = "prcProfAuditAuthorityParamListBuilder")
public class PrcProfAuditAuthorityParamListBuilder {
    public static final Logger logger = LoggerFactory.getLogger(PrcProfAuditAuthorityParamListBuilder.class);

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String PRC_PROF_AUDIT_AUTHORITY_IND = "PRC_PROF_AUDIT_AUTHORITY_IND";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";

    public MapSqlParameterSource createParamMap(PrcProfAuditAuthorityDO prcProfAuditAuthorityDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, prcProfAuditAuthorityDO.getContractPriceProfileSeq(), Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, prcProfAuditAuthorityDO.getLastUpdateUserId(), Types.VARCHAR);
        paramMap.addValue(CREATE_USER_ID, prcProfAuditAuthorityDO.getCreateUserId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, prcProfAuditAuthorityDO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(EFFECTIVE_DATE, prcProfAuditAuthorityDO.getEffectiveDate(), Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, prcProfAuditAuthorityDO.getExpirationDate(), Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, prcProfAuditAuthorityDO.getGfsCustomerTypeCode(), Types.INTEGER);
        paramMap.addValue(PRC_PROF_AUDIT_AUTHORITY_IND, prcProfAuditAuthorityDO.getPrcProfAuditAuthorityInd(), Types.INTEGER);
        return paramMap;

    }

}
