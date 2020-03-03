package com.gfs.cpp.data.assignment;

import static com.gfs.cpp.data.assignment.ItemAssignmentDTORowMapper.FETCH_ASSIGNED_ITEMS_IN_A_CONCEPT;

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

import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.model.assignments.ItemAssignmentDO;

@Repository(value = "cppItemMappingRepository")
public class CppItemMappingRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    private static final String CUSTOMER_ITEM_DESC_PRICE_SEQ = "CUSTOMER_ITEM_DESC_PRICE_SEQ";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String LIST_OF_ITEM_DESC_SEQ = "LIST_OF_ITEM_DESC_SEQ";

    // @formatter:off
    
    private static final String INSERT_CPP_ITEM_MAPPING_WITH_EFCTV_AND_EXP_DATE = "Insert into CPP_ITEM_MAPPING "
            + "(CPP_ITEM_MAPPING_SEQ," 
            + "CUSTOMER_ITEM_DESC_PRICE_SEQ," 
            + "ITEM_PRICE_LEVEL_CODE," 
            + "ITEM_PRICE_ID,"
            + "LAST_UPDATE_USER_ID,"
            + "LAST_UPDATE_TMSTMP,"
            + "CREATE_USER_ID,"
            + "EFFECTIVE_DATE,"
            + "EXPIRATION_DATE ) "
            + "values (CPP_ITEM_MAPPING_SEQ.nextval , " 
            + ":CUSTOMER_ITEM_DESC_PRICE_SEQ , "
            + ":ITEM_PRICE_LEVEL_CODE , " 
            + ":ITEM_PRICE_ID,"
            + ":LAST_UPDATE_USER_ID," 
            + "CURRENT_TIMESTAMP," 
            + ":CREATE_USER_ID,"
            + ":EFFECTIVE_DATE, "
            + ":EXPIRATION_DATE)";
    
    private static final String INSERT_CPP_ITEM_MAPPING = "Insert into CPP_ITEM_MAPPING "
            + "(CPP_ITEM_MAPPING_SEQ," 
            + "CUSTOMER_ITEM_DESC_PRICE_SEQ," 
            + "ITEM_PRICE_LEVEL_CODE," 
            + "ITEM_PRICE_ID,"
            + "LAST_UPDATE_USER_ID,"
            + "LAST_UPDATE_TMSTMP,"
            + "CREATE_USER_ID ) "
            + "values (CPP_ITEM_MAPPING_SEQ.nextval , " 
            + ":CUSTOMER_ITEM_DESC_PRICE_SEQ , "
            + ":ITEM_PRICE_LEVEL_CODE , " 
            + ":ITEM_PRICE_ID,"
            + ":LAST_UPDATE_USER_ID," 
            + "CURRENT_TIMESTAMP," 
            + ":CREATE_USER_ID)";
    
    private static final String UPDATE_CPP_ITEM_MAPPING = "Update CPP_ITEM_MAPPING SET "
            + "LAST_UPDATE_USER_ID =:LAST_UPDATE_USER_ID, "
            + "LAST_UPDATE_TMSTMP =SYSTIMESTAMP , "
            + "EXPIRATION_DATE = :EXPIRATION_DATE "
            + "where CUSTOMER_ITEM_DESC_PRICE_SEQ =:CUSTOMER_ITEM_DESC_PRICE_SEQ "
            + "and ITEM_PRICE_ID=:ITEM_PRICE_ID";
    
    // @formatter:on

    public void saveItems(List<ItemAssignmentDO> newItemAssignmentDOList, String userName) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (ItemAssignmentDO itemAssignmentDO : newItemAssignmentDOList) {
            MapSqlParameterSource paramMap = buildParamMapForAssignItem(userName, itemAssignmentDO);
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(INSERT_CPP_ITEM_MAPPING, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public void saveCPPItemMapping(List<ItemAssignmentDO> newItemAssignmentDOList, String userName, Date expirationDate) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (ItemAssignmentDO itemAssignmentDO : newItemAssignmentDOList) {
            MapSqlParameterSource paramMap = buildParamMapForAssignItemWithExpirationDates(userName, itemAssignmentDO,
                    itemAssignmentDO.getEffectiveDate(), expirationDate);
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(INSERT_CPP_ITEM_MAPPING_WITH_EFCTV_AND_EXP_DATE, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public void expireOrUpdateItems(List<ItemAssignmentDO> updateItemAssignmentDOList, Date expirationDate, String userName) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (ItemAssignmentDO itemAssignmentDO : updateItemAssignmentDOList) {
            MapSqlParameterSource paramMap = buildParamMapForAssignItem(userName, itemAssignmentDO);
            paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(UPDATE_CPP_ITEM_MAPPING, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public List<ItemAssignmentDTO> fetchAssignedItemsForAConcept(List<Integer> customerItemDescSeqList) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(LIST_OF_ITEM_DESC_SEQ, customerItemDescSeqList, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_ASSIGNED_ITEMS_IN_A_CONCEPT, paramMap, new ItemAssignmentDTORowMapper());
    }

    private MapSqlParameterSource buildParamMapForAssignItemWithExpirationDates(String userName, ItemAssignmentDO itemAssignmentDO,
            Date effectiveDate, Date expirationDate) {
        MapSqlParameterSource paramMap = buildParamMapForAssignItem(userName, itemAssignmentDO);
        paramMap.addValue(EFFECTIVE_DATE, effectiveDate, Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        return paramMap;
    }

    private MapSqlParameterSource buildParamMapForAssignItem(String userName, ItemAssignmentDO itemAssignmentDO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CUSTOMER_ITEM_DESC_PRICE_SEQ, itemAssignmentDO.getCustomerItemDescSeq(), Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, itemAssignmentDO.getItemPriceLevelCode(), Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_ID, itemAssignmentDO.getItemPriceId(), Types.VARCHAR);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        return paramMap;
    }
}
