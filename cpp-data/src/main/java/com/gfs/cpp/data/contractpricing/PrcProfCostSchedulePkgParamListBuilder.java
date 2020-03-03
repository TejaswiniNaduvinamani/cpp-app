package com.gfs.cpp.data.contractpricing;

import java.sql.Types;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;

@Component(value = "prcProfCostSchedulePkgParamListBuilder")
public class PrcProfCostSchedulePkgParamListBuilder {

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String PRC_PROF_COST_SCHEDULE_PKG_SEQ = "PRC_PROF_COST_SCHEDULE_PKG_SEQ";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";

    public MapSqlParameterSource createParamMap(PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, prcProfCostSchedulePkgDO.getContractPriceProfileSeq(), Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, prcProfCostSchedulePkgDO.getLastUpdateUserId(), Types.VARCHAR);
        paramMap.addValue(CREATE_USER_ID, prcProfCostSchedulePkgDO.getCreateUserId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, prcProfCostSchedulePkgDO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(EFFECTIVE_DATE, prcProfCostSchedulePkgDO.getEffectiveDate(), Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, prcProfCostSchedulePkgDO.getExpirationDate(), Types.DATE);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, prcProfCostSchedulePkgDO.getGfsCustomerTypeCode(), Types.INTEGER);
        paramMap.addValue(PRC_PROF_COST_SCHEDULE_PKG_SEQ, prcProfCostSchedulePkgDO.getPrcProfCostSchedulePkgSeq(), Types.INTEGER);
        return paramMap;

    }

}
