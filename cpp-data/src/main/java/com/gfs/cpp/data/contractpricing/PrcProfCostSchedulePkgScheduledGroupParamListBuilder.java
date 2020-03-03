package com.gfs.cpp.data.contractpricing;

import java.sql.Types;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgScheduledGroupDO;

@Component(value="prcProfCostSchedulePkgScheduledGroupParamListBuilder")
public class PrcProfCostSchedulePkgScheduledGroupParamListBuilder {
  
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String COST_RUN_FREQUENCY_CODE = "COST_RUN_FREQUENCY_CODE";
    private static final String SCHEDULE_GROUP_SEQ = "SCHEDULE_GROUP_SEQ";
    private static final String PRC_PROF_COST_SCHEDULE_PKG_SEQ = "PRC_PROF_COST_SCHEDULE_PKG_SEQ";

    public MapSqlParameterSource createParamMap(PrcProfCostSchedulePkgScheduledGroupDO prcProfCostSchedulePkgScheduledGroupDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource(); 
        
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, prcProfCostSchedulePkgScheduledGroupDO.getContractPriceSeq(), Types.INTEGER);
        paramMap.addValue(COST_RUN_FREQUENCY_CODE, prcProfCostSchedulePkgScheduledGroupDO.getCostRunFrequencyCode(), Types.VARCHAR);
        paramMap.addValue(PRC_PROF_COST_SCHEDULE_PKG_SEQ, prcProfCostSchedulePkgScheduledGroupDO.getPrcProfCostSchedulePkgSeq(), Types.INTEGER);
        paramMap.addValue(SCHEDULE_GROUP_SEQ, prcProfCostSchedulePkgScheduledGroupDO.getScheduleGroup(), Types.INTEGER);
      
        return paramMap;

    }

}
