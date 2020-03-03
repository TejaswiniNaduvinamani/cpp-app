package com.gfs.cpp.data.furtherance;

import static com.gfs.cpp.data.furtherance.CPPFurtheranceTrackingDTORowMapper.FETCH_ALL_CPP_FURTHERANCE_TRACKING_RECORDS_BY_CPP_FURTHERANCE_SEQUENCE;

import java.sql.Types;
import java.util.ArrayList;
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

import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;

@Repository
public class CppFurtheranceTrackingRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    public static final Logger logger = LoggerFactory.getLogger(CppFurtheranceRepository.class);

    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";
    private static final String FURTHERANCE_ACTION_CODE = "FURTHERANCE_ACTION_CODE";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String CHANGE_TABLE_NAME = "CHANGE_TABLE_NAME";

    // @formatter:off
    private static final String INSERT_CPP_FURTHERANCE_TRACKING = "Insert into CPP_FURTHERANCE_TRACKING"
            + "(CPP_FURTHERANCE_TRACKING_SEQ,"
            + "CPP_FURTHERANCE_SEQ,"
            + "FURTHERANCE_ACTION_CODE,"
            + "ITEM_PRICE_ID,"
            + "ITEM_PRICE_LEVEL_CODE,"
            + "GFS_CUSTOMER_ID,"
            + "GFS_CUSTOMER_TYPE_CODE, "
            + "CHANGE_TABLE_NAME,"
            + "CREATE_USER_ID,"
            + "CREATE_TMSTMP,"
            + "LAST_UPDATE_USER_ID)"
            + "values (CPP_FURTHERANCE_TRACKING_SEQ.nextVal,"
            + ":CPP_FURTHERANCE_SEQ, "
            + ":FURTHERANCE_ACTION_CODE, "
            + ":ITEM_PRICE_ID,"
            + ":ITEM_PRICE_LEVEL_CODE,"
            + ":GFS_CUSTOMER_ID,"
            + ":GFS_CUSTOMER_TYPE_CODE,"
            + ":CHANGE_TABLE_NAME ,"
            + ":CREATE_USER_ID ,"
            + "SYSTIMESTAMP ,"
            + ":LAST_UPDATE_USER_ID)";
    
    private static final String DELETE_CPP_FURTHERANCE_TRACKING = "Delete from CPP_FURTHERANCE_TRACKING where "
            + "CPP_FURTHERANCE_SEQ = :CPP_FURTHERANCE_SEQ and "
            + "FURTHERANCE_ACTION_CODE = :FURTHERANCE_ACTION_CODE and "
            + "ITEM_PRICE_ID = :ITEM_PRICE_ID and "
            + "ITEM_PRICE_LEVEL_CODE = :ITEM_PRICE_LEVEL_CODE and "
            + "GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID and "
            + "GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE";
    
    private static final String FETCH_FURTHERANCE_ACTION_CODE = "Select FURTHERANCE_ACTION_CODE from CPP_FURTHERANCE_TRACKING where "
            + "CPP_FURTHERANCE_SEQ = :CPP_FURTHERANCE_SEQ and "
            + "ITEM_PRICE_ID = :ITEM_PRICE_ID and "
            + "ITEM_PRICE_LEVEL_CODE = :ITEM_PRICE_LEVEL_CODE and "
            + "GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID and "
            + "GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE and "
            + "CHANGE_TABLE_NAME = :CHANGE_TABLE_NAME";
    
    // @formatter:on

    public void deleteTrackingEntry(CPPFurtheranceTrackingDTO furtheranceTrackingDTO, int furtheranceActionCode) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_FURTHERANCE_SEQ, furtheranceTrackingDTO.getCppFurtheranceSeq(), Types.INTEGER);
        paramMap.addValue(FURTHERANCE_ACTION_CODE, furtheranceActionCode, Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_ID, furtheranceTrackingDTO.getItemPriceId(), Types.VARCHAR);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, furtheranceTrackingDTO.getItemPriceLevelCode(), Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, furtheranceTrackingDTO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, furtheranceTrackingDTO.getGfsCustomerTypeCode(), Types.INTEGER);

        cppJdbcTemplate.update(DELETE_CPP_FURTHERANCE_TRACKING, paramMap);

    }

    public Integer fetchFurtheranceActionCode(CPPFurtheranceTrackingDTO furtheranceTrackingDTO) {
        try {
            MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(CPP_FURTHERANCE_SEQ, furtheranceTrackingDTO.getCppFurtheranceSeq(), Types.INTEGER);
            paramMap.addValue(ITEM_PRICE_ID, furtheranceTrackingDTO.getItemPriceId(), Types.VARCHAR);
            paramMap.addValue(ITEM_PRICE_LEVEL_CODE, furtheranceTrackingDTO.getItemPriceLevelCode(), Types.INTEGER);
            paramMap.addValue(GFS_CUSTOMER_ID, furtheranceTrackingDTO.getGfsCustomerId(), Types.VARCHAR);
            paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, furtheranceTrackingDTO.getGfsCustomerTypeCode(), Types.INTEGER);
            paramMap.addValue(CHANGE_TABLE_NAME, furtheranceTrackingDTO.getChangeTableName(), Types.VARCHAR);
            return cppJdbcTemplate.queryForObject(FETCH_FURTHERANCE_ACTION_CODE, paramMap, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            logger.info("No Existing for furtherance tracking found for {}", furtheranceTrackingDTO.toString());
            return null;
        }
    }

    public List<CPPFurtheranceTrackingDTO> fetchFurtheranceDetailsByCPPFurtheranceSeq(int cppFurtheranceSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_FURTHERANCE_SEQ, cppFurtheranceSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_ALL_CPP_FURTHERANCE_TRACKING_RECORDS_BY_CPP_FURTHERANCE_SEQUENCE, paramMap,
                new CPPFurtheranceTrackingDTORowMapper());
    }

    public void batchInsertRecordsInFurtheranceTracking(List<CPPFurtheranceTrackingDTO> furtheranceWrapperDTOList, String userName) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (CPPFurtheranceTrackingDTO furtheranceTrackingDTO : furtheranceWrapperDTOList) {
            MapSqlParameterSource paramMap = createInsertParamList(furtheranceTrackingDTO, userName);
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(INSERT_CPP_FURTHERANCE_TRACKING, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    private MapSqlParameterSource createInsertParamList(CPPFurtheranceTrackingDTO furtheranceTrackingDTO, String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_FURTHERANCE_SEQ, furtheranceTrackingDTO.getCppFurtheranceSeq(), Types.INTEGER);
        paramMap.addValue(FURTHERANCE_ACTION_CODE, furtheranceTrackingDTO.getFurtheranceActionCode(), Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_ID, furtheranceTrackingDTO.getItemPriceId(), Types.VARCHAR);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, furtheranceTrackingDTO.getItemPriceLevelCode(), Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, furtheranceTrackingDTO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, furtheranceTrackingDTO.getGfsCustomerTypeCode(), Types.INTEGER);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CHANGE_TABLE_NAME, furtheranceTrackingDTO.getChangeTableName(), Types.VARCHAR);
        return paramMap;
    }
}
