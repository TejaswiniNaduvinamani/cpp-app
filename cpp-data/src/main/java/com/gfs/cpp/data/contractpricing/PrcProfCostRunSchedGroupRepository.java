package com.gfs.cpp.data.contractpricing;

import static com.gfs.cpp.data.markup.PrcProfCostSchedulePkgScheduledGroupRowMapper.FETCH_PRC_PROF_COST_RUN_SCHED_GROUP;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.gfs.corp.component.price.common.types.CostChangeFrequency;
import com.gfs.cpp.common.dto.markup.PrcProfCostSchedulePkgScheduledGroupDTO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgScheduledGroupDO;
import com.gfs.cpp.data.markup.PrcProfCostSchedulePkgScheduledGroupRowMapper;

@Repository(value = "prcProfCostRunSchedGroupRepository")
public class PrcProfCostRunSchedGroupRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    @Autowired
    private PrcProfCostSchedulePkgScheduledGroupParamListBuilder prcProfCostSchedulePkgScheduledGroupParamListBuilder;

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String COST_RUN_FREQUENCY_CODE = "COST_RUN_FREQUENCY_CODE";
    private static final String SCHEDULE_GROUP_SEQ = "SCHEDULE_GROUP_SEQ";
    private static final String PRC_PROF_COST_SCHEDULE_PKG_SEQ = "PRC_PROF_COST_SCHEDULE_PKG_SEQ";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";

    // @formatter:off
    private static final String INSERT_PRC_PROF_COST_RUN_SCHED_GROUP = "Insert into PRC_PROF_COST_RUN_SCHED_GROUP "
            + "(PRC_PROF_COST_SCHEDULE_PKG_SEQ," 
            + "SCHEDULE_GROUP_SEQ," 
            + "COST_RUN_FREQUENCY_CODE,"
            + "CONTRACT_PRICE_PROFILE_SEQ ) " 
            + " values (:PRC_PROF_COST_SCHEDULE_PKG_SEQ,"
            + ":SCHEDULE_GROUP_SEQ," 
            + ":COST_RUN_FREQUENCY_CODE," 
            + ":CONTRACT_PRICE_PROFILE_SEQ )";
    
    
    private static final String FETCH_PRC_PROF_COST_SCHEDULE_PKG = "SELECT SCHEDULE_GROUP_SEQ "
            + "from PRC_PROF_COST_RUN_SCHED_GROUP "
            + "where CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ "
            + "AND COST_RUN_FREQUENCY_CODE=:COST_RUN_FREQUENCY_CODE "
            + "AND PRC_PROF_COST_SCHEDULE_PKG_SEQ IN "
            + "(SELECT PRC_PROF_COST_SCHEDULE_PKG_SEQ FROM PRC_PROF_COST_SCHEDULE_PKG "
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ =:CONTRACT_PRICE_PROFILE_SEQ "
            + "AND GFS_CUSTOMER_ID =:GFS_CUSTOMER_ID "
            + "AND GFS_CUSTOMER_TYPE_CODE =:GFS_CUSTOMER_TYPE_CODE) ";
    
    private static final String DELETE_PRC_PROF_COST_RUN_SCHED_GROUP = "DELETE from PRC_PROF_COST_RUN_SCHED_GROUP "
            + " where CONTRACT_PRICE_PROFILE_SEQ =:CONTRACT_PRICE_PROFILE_SEQ ";

    private static final String UPDATE_PRC_PROF_COST_RUN_SCHED_GROUP = "UPDATE PRC_PROF_COST_RUN_SCHED_GROUP "
            + "SET SCHEDULE_GROUP_SEQ=:SCHEDULE_GROUP_SEQ "
            + "WHERE COST_RUN_FREQUENCY_CODE=:COST_RUN_FREQUENCY_CODE "
            + "AND CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ";
    
    
    // @formatter:on
    public void savePrcProfCostRunSchedGroupForCustomer(List<PrcProfCostSchedulePkgScheduledGroupDO> prcProfCostSchedulePkgScheduledGroupDOList) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (PrcProfCostSchedulePkgScheduledGroupDO prcProfCostSchedulePkgScheduledGroupDO : prcProfCostSchedulePkgScheduledGroupDOList) {
            MapSqlParameterSource paramMap = prcProfCostSchedulePkgScheduledGroupParamListBuilder
                    .createParamMap(prcProfCostSchedulePkgScheduledGroupDO);
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(INSERT_PRC_PROF_COST_RUN_SCHED_GROUP, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public List<PrcProfCostSchedulePkgScheduledGroupDTO> fetchPrcProfCostRunSchedGroupForCPPSeq(int contractPriceProfileSeq,
            int prcProfCostScheduledPkgSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(PRC_PROF_COST_SCHEDULE_PKG_SEQ, prcProfCostScheduledPkgSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_PRC_PROF_COST_RUN_SCHED_GROUP, paramMap, new PrcProfCostSchedulePkgScheduledGroupRowMapper());
    }
    

    public void insertSchedule(int contractPriceProfileSeq, int costSchedulePkgSeq, int scheduleGroupSeqWeek, int scheduleGroupSeqMonthly) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        buildParamMapForSchedule(scheduleGroupSeqWeek, scheduleGroupSeqMonthly, contractPriceProfileSeq, costSchedulePkgSeq, paramList);
        cppJdbcTemplate.batchUpdate(INSERT_PRC_PROF_COST_RUN_SCHED_GROUP, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    private void buildParamMapForSchedule(int scheduleGroupSeqWeek, int scheduleGroupSeqMonthly, int contractPriceProfileSeq, int costSchedulePkgSeq,
            List<SqlParameterSource> paramList) {
        MapSqlParameterSource paramMapCodeW = new MapSqlParameterSource();
        MapSqlParameterSource paramMapCodeM = new MapSqlParameterSource();
        createScheduleParam(CostChangeFrequency.WEEKLY.getCode(), scheduleGroupSeqWeek, contractPriceProfileSeq, costSchedulePkgSeq, paramMapCodeW);
        createScheduleParam(CostChangeFrequency.MONTHLY.getCode(), scheduleGroupSeqMonthly, contractPriceProfileSeq, costSchedulePkgSeq,
                paramMapCodeM);
        paramList.add(paramMapCodeM);
        paramList.add(paramMapCodeW);
    }

    private void createScheduleParam(String costFrequencyCode, int scheduleGroupSeqMonthly, int contractPriceProfileSeq, int costSchedulePkgSeq,
            MapSqlParameterSource paramMapCodeM) {
        paramMapCodeM.addValue(COST_RUN_FREQUENCY_CODE, costFrequencyCode, Types.VARCHAR);
        paramMapCodeM.addValue(SCHEDULE_GROUP_SEQ, scheduleGroupSeqMonthly, Types.NUMERIC);
        paramMapCodeM.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMapCodeM.addValue(PRC_PROF_COST_SCHEDULE_PKG_SEQ, costSchedulePkgSeq, Types.INTEGER);
    }

    public void deleteScheduleForGregorian(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        cppJdbcTemplate.update(DELETE_PRC_PROF_COST_RUN_SCHED_GROUP, paramMap);
    }

    public Integer fetchGfsMonthlyCostScheduleCost(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(COST_RUN_FREQUENCY_CODE, CostChangeFrequency.MONTHLY.getCode(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_PRC_PROF_COST_SCHEDULE_PKG, paramMap, Integer.class);

    }

    public void updateScheduleGroupSeqMonthly(int scheduleGroupSeqMonthly, int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(SCHEDULE_GROUP_SEQ, scheduleGroupSeqMonthly, Types.INTEGER);
        paramMap.addValue(COST_RUN_FREQUENCY_CODE, CostChangeFrequency.MONTHLY.getCode(), Types.VARCHAR);
        cppJdbcTemplate.update(UPDATE_PRC_PROF_COST_RUN_SCHED_GROUP, paramMap);
    }

}
