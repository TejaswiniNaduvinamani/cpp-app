package com.gfs.cpp.data.markup;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.markup.PrcProfCostSchedulePkgScheduledGroupDTO;

public class PrcProfCostSchedulePkgScheduledGroupRowMapper implements RowMapper<PrcProfCostSchedulePkgScheduledGroupDTO> {

    private static final String SCHEDULE_GROUP_SEQ = "SCHEDULE_GROUP_SEQ";
    private static final String COST_RUN_FREQUENCY_CODE = "COST_RUN_FREQUENCY_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String PRC_PROF_COST_SCHEDULE_PKG_SEQ = "PRC_PROF_COST_SCHEDULE_PKG_SEQ";

    // @formatter:off
         
     public static final String FETCH_PRC_PROF_COST_RUN_SCHED_GROUP = " SELECT PRC_PROF_COST_SCHEDULE_PKG_SEQ,"
             + " SCHEDULE_GROUP_SEQ, "
             + " COST_RUN_FREQUENCY_CODE,"
             + " CONTRACT_PRICE_PROFILE_SEQ"
             + " FROM PRC_PROF_COST_RUN_SCHED_GROUP "
             + " WHERE CONTRACT_PRICE_PROFILE_SEQ =:CONTRACT_PRICE_PROFILE_SEQ "
             + " AND PRC_PROF_COST_SCHEDULE_PKG_SEQ=:PRC_PROF_COST_SCHEDULE_PKG_SEQ";
     
      // @formatter:on

    @Override
    public PrcProfCostSchedulePkgScheduledGroupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PrcProfCostSchedulePkgScheduledGroupDTO prcProfCostSchedulePkgScheduledGroupDTO = new PrcProfCostSchedulePkgScheduledGroupDTO();
        prcProfCostSchedulePkgScheduledGroupDTO.setScheduleGroup(rs.getInt(PRC_PROF_COST_SCHEDULE_PKG_SEQ));
        prcProfCostSchedulePkgScheduledGroupDTO.setScheduleGroup(rs.getInt(SCHEDULE_GROUP_SEQ));
        prcProfCostSchedulePkgScheduledGroupDTO.setCostRunFrequencyCode(rs.getString(COST_RUN_FREQUENCY_CODE));
        prcProfCostSchedulePkgScheduledGroupDTO.setContractPriceSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        return prcProfCostSchedulePkgScheduledGroupDTO;
    }

}
