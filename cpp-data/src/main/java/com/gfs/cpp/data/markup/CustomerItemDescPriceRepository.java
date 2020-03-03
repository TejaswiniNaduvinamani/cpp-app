package com.gfs.cpp.data.markup;

import static com.gfs.cpp.data.markup.FutureItemDescriptionRowMapper.FETCH_ALL_CUSTOMER_ITEM_DESC_PRICE;
import static com.gfs.cpp.data.markup.FutureItemDescriptionRowMapper.FETCH_CUSTOMER_ITEM_DESC_PRICE_FOR_FURTHERANCE;
import static com.gfs.cpp.data.markup.ItemFutureTypeMarkupRowMapper.FETCH_CUSTOMER_ITEM_DESC_PRICE;
import static com.gfs.cpp.data.markup.ItemFutureTypeMarkupRowMapper.FETCH_CUSTOMER_ITEM_DESC_PRICE_FOR_ASSIGNMENT;

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

import com.gfs.corp.component.price.common.types.AmountType;
import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.model.markup.FutureItemDO;

@Repository(value = "customerItemDescPriceRepository")
public class CustomerItemDescPriceRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String ITEM_DESC = "ITEM_DESC";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String COST_MARKUP_AMT = "COST_MARKUP_AMT";
    private static final String MARKUP_AMOUNT_TYPE_CODE = "MARKUP_AMOUNT_TYPE_CODE";
    private static final String MARKUP_UNIT_TYPE_CODE = "MARKUP_UNIT_TYPE_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String CUSTOMER_ITEM_DESC_PRICE_SEQ = "CUSTOMER_ITEM_DESC_PRICE_SEQ";

    public static final Logger logger = LoggerFactory.getLogger(CustomerItemDescPriceRepository.class);
    //@formatter:off
    private static final String INSERT_INTO_CUSTOMER_ITEM_DESC_PRICE = "Insert into CUSTOMER_ITEM_DESC_PRICE "
            + "(CUSTOMER_ITEM_DESC_PRICE_SEQ, GFS_CUSTOMER_ID,"
            + " GFS_CUSTOMER_TYPE_CODE , "
            + "ITEM_DESC,"
            + "CREATE_USER_ID,"
            + "LAST_UPDATE_USER_ID,"
            + "COST_MARKUP_AMT ,"
            + "MARKUP_AMOUNT_TYPE_CODE ,"
            + "MARKUP_UNIT_TYPE_CODE,"
            + "CONTRACT_PRICE_PROFILE_SEQ) "
            + "values (:CUSTOMER_ITEM_DESC_PRICE_SEQ,"
            + ":GFS_CUSTOMER_ID,"
            + ":GFS_CUSTOMER_TYPE_CODE,"
            + ":ITEM_DESC,"
            + ":CREATE_USER_ID,"
            + ":LAST_UPDATE_USER_ID,"
            + ":COST_MARKUP_AMT,"
            + ":MARKUP_AMOUNT_TYPE_CODE,"
            + ":MARKUP_UNIT_TYPE_CODE,"
            + ":CONTRACT_PRICE_PROFILE_SEQ)";
    
    
    private static final String INSERT_CUSTOMER_ITEM_DESC_PRICE = "Insert into CUSTOMER_ITEM_DESC_PRICE "
            + "(CUSTOMER_ITEM_DESC_PRICE_SEQ, GFS_CUSTOMER_ID,"
            + " GFS_CUSTOMER_TYPE_CODE , "
            + "ITEM_DESC,"
            + "CREATE_USER_ID,"
            + "LAST_UPDATE_USER_ID,"
            + "COST_MARKUP_AMT ,"
            + "MARKUP_AMOUNT_TYPE_CODE ,"
            + "MARKUP_UNIT_TYPE_CODE,"
            + "CONTRACT_PRICE_PROFILE_SEQ) "
            + "values (CUSTOMER_ITEM_DESC_PRICE_SEQ.nextVal,"
            + ":GFS_CUSTOMER_ID,"
            + ":GFS_CUSTOMER_TYPE_CODE,"
            + ":ITEM_DESC,"
            + ":CREATE_USER_ID,"
            + ":LAST_UPDATE_USER_ID,"
            + ":COST_MARKUP_AMT,"
            + ":MARKUP_AMOUNT_TYPE_CODE,"
            + ":MARKUP_UNIT_TYPE_CODE,"
            + ":CONTRACT_PRICE_PROFILE_SEQ)";
    
    private static final String UPDATE_CUSTOMER_ITEM_DESC_PRICE = "Update CUSTOMER_ITEM_DESC_PRICE SET "
            + "GFS_CUSTOMER_TYPE_CODE =:GFS_CUSTOMER_TYPE_CODE, "
            + "LAST_UPDATE_USER_ID =:LAST_UPDATE_USER_ID, "
            + "LAST_UPDATE_TMSTMP = SYSTIMESTAMP, "
            + "COST_MARKUP_AMT =:COST_MARKUP_AMT, "
            + "MARKUP_AMOUNT_TYPE_CODE=:MARKUP_AMOUNT_TYPE_CODE, "
            + "MARKUP_UNIT_TYPE_CODE=:MARKUP_UNIT_TYPE_CODE "
            + "where CONTRACT_PRICE_PROFILE_SEQ =  :CONTRACT_PRICE_PROFILE_SEQ "
            + "and GFS_CUSTOMER_ID= :GFS_CUSTOMER_ID "
            + "and ITEM_DESC=:ITEM_DESC";
    
    private static final String DELETE_CUSTOMER_ITEM_DESC_PRICE = "Delete from CUSTOMER_ITEM_DESC_PRICE where "
            + "CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID " 
            + "and ITEM_DESC=:ITEM_DESC";
    
    private static final String DELETE_ALL_CUSTOMER_ITEM_DESC_PRICE = "Delete from CUSTOMER_ITEM_DESC_PRICE where "
            + "CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "and GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID "; 
    
    private static final String FETCH_SELL_PRICE_IND_FROM_CUSTOMER_ITEM_DESC_PRICE = "Select count(*) from CUSTOMER_ITEM_DESC_PRICE "
            + "where CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ "
            + "AND MARKUP_AMOUNT_TYPE_CODE= '" + AmountType.PERCENT.getCode() + "'"; 
    
    private static final String FETCH_CUSTOMER_ITEM_DESC_PRICE_NEXT_SEQ = "select CUSTOMER_ITEM_DESC_PRICE_SEQ.nextVal from dual";
  
    //@formatter:on

    public void saveFutureItems(List<FutureItemDO> futureItemList, String userName, int contractPriceProfileSeq) {
        List<SqlParameterSource> paramList = buildFutureItemDO(futureItemList, userName, contractPriceProfileSeq);
        cppJdbcTemplate.batchUpdate(INSERT_CUSTOMER_ITEM_DESC_PRICE, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public int fetchCPPCustomerItemDescPriceNextSequence() {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        return cppJdbcTemplate.queryForObject(FETCH_CUSTOMER_ITEM_DESC_PRICE_NEXT_SEQ, paramMap, Integer.class);
    }

    public void saveFutureItemForCPPSeq(FutureItemDescriptionDTO futureItemDTO, String userName, int contractPriceProfileSeq,
            int cppCustomerItemDescPriceSeq) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, futureItemDTO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, futureItemDTO.getGfsCustomerTypeCode(), Types.INTEGER);
        paramMap.addValue(ITEM_DESC, futureItemDTO.getFutureItemDesc(), Types.VARCHAR);
        paramMap.addValue(COST_MARKUP_AMT, futureItemDTO.getCostMarkupAmt(), Types.NUMERIC);
        paramMap.addValue(MARKUP_AMOUNT_TYPE_CODE, futureItemDTO.getMarkupAmountTypeCode(), Types.VARCHAR);
        paramMap.addValue(MARKUP_UNIT_TYPE_CODE, futureItemDTO.getMarkupUnitTypeCode(), Types.INTEGER);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CUSTOMER_ITEM_DESC_PRICE_SEQ, cppCustomerItemDescPriceSeq, Types.INTEGER);
        cppJdbcTemplate.update(INSERT_INTO_CUSTOMER_ITEM_DESC_PRICE, paramMap);

    }

    public int fetchMarkupAmountTypeCodeCount(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_SELL_PRICE_IND_FROM_CUSTOMER_ITEM_DESC_PRICE, paramMap, Integer.class);
    }

    public void updateFutureItems(List<FutureItemDO> futureItemList, String userName, int contractPriceProfileSeq) {
        List<SqlParameterSource> paramList = buildFutureItemDO(futureItemList, userName, contractPriceProfileSeq);
        cppJdbcTemplate.batchUpdate(UPDATE_CUSTOMER_ITEM_DESC_PRICE, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public void deleteFutureItem(int contractPriceProfileSeq, String gfsCustomerId, String itemDesc) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(ITEM_DESC, itemDesc, Types.VARCHAR);
        cppJdbcTemplate.update(DELETE_CUSTOMER_ITEM_DESC_PRICE, paramMap);
    }

    public void deleteAllFutureItem(int contractPriceProfileSeq, String gfsCustomerId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        cppJdbcTemplate.update(DELETE_ALL_CUSTOMER_ITEM_DESC_PRICE, paramMap);
    }

    public List<ItemLevelMarkupDTO> fetchFutureItemTypeMarkups(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_CUSTOMER_ITEM_DESC_PRICE, paramMap, new ItemFutureTypeMarkupRowMapper());
    }

    public List<FutureItemDescriptionDTO> fetchAllFutureItems(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_ALL_CUSTOMER_ITEM_DESC_PRICE, paramMap, new FutureItemDescriptionRowMapper());
    }

    public FutureItemDescriptionDTO fetchFutureItemForFurtherance(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemDesc) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.INTEGER);
        paramMap.addValue(ITEM_DESC, itemDesc, Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(FETCH_CUSTOMER_ITEM_DESC_PRICE_FOR_FURTHERANCE, paramMap, new FutureItemDescriptionRowMapper());
    }

    public ItemLevelMarkupDTO fetchFutureItemForAssignment(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemDesc) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, gfsCustomerId, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, gfsCustomerTypeCode, Types.INTEGER);
        paramMap.addValue(ITEM_DESC, itemDesc, Types.VARCHAR);
        try {
            return cppJdbcTemplate.queryForObject(FETCH_CUSTOMER_ITEM_DESC_PRICE_FOR_ASSIGNMENT, paramMap, new ItemFutureTypeMarkupRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("No records found for contract price profile seq {}", contractPriceProfileSeq);
            return null;
        }
    }

    private List<SqlParameterSource> buildFutureItemDO(List<FutureItemDO> futureItemList, String userName, int contractPriceProfileSeq) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (FutureItemDO futureItem : futureItemList) {
            MapSqlParameterSource paramMapMarkupData = new MapSqlParameterSource();
            paramMapMarkupData.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
            paramMapMarkupData.addValue(GFS_CUSTOMER_ID, futureItem.getGfsCustomerId(), Types.VARCHAR);
            paramMapMarkupData.addValue(GFS_CUSTOMER_TYPE_CODE, futureItem.getCustomerTypeCode(), Types.INTEGER);
            paramMapMarkupData.addValue(ITEM_DESC, futureItem.getItemDesc(), Types.VARCHAR);
            paramMapMarkupData.addValue(COST_MARKUP_AMT, futureItem.getMarkup(), Types.NUMERIC);
            paramMapMarkupData.addValue(MARKUP_AMOUNT_TYPE_CODE, futureItem.getUnit(), Types.VARCHAR);
            paramMapMarkupData.addValue(MARKUP_UNIT_TYPE_CODE, futureItem.getMarkupType(), Types.INTEGER);
            paramMapMarkupData.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
            paramMapMarkupData.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
            paramList.add(paramMapMarkupData);
        }
        return paramList;
    }
}
