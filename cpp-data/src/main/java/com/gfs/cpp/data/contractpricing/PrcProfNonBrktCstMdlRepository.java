package com.gfs.cpp.data.contractpricing;

import static com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRowMapper.FETCH_PRC_PROF_NON_BRKT_CST_MDL_FOR_CMG_CPP_SEQ;
import static com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRowMapper.FETCH_PRC_PROF_NON_BRKT_CST_MDL_FOR_CPP_SEQ;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.model.markup.PrcProfNonBrktCstMdlDO;

@Repository(value = "prcProfNonBrktCstMdlRepository")
public class PrcProfNonBrktCstMdlRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    @Autowired
    private PrcProfNonBrktCstMdlParamListBuilder prcProfNonBrktCstMdlParamListBuilder;

    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String LAST_UPDATE_USER_ID = "LAST_UPDATE_USER_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String COST_MODEL_ID = "COST_MODEL_ID";
    private static final String CREATE_USER_ID = "CREATE_USER_ID";
    private static final String NEW_EFFECTIVE_DATE = "NEW_EFFECTIVE_DATE";
    private static final String NEW_EXPIRATION_DATE = "NEW_EXPIRATION_DATE";

    // @formatter:off
    private static final String INSERT_PRC_PROF_NON_BRKT_CST_MDL = "Insert into PRC_PROF_NON_BRKT_CST_MDL "
            + "(PRC_PROF_NON_BRKT_CST_MDL_SEQ, " 
            + "GFS_CUSTOMER_ID," 
            + "GFS_CUSTOMER_TYPE_CODE,"
            + "ITEM_PRICE_LEVEL_CODE," 
            + "ITEM_PRICE_ID," 
            + "EFFECTIVE_DATE," 
            + "EXPIRATION_DATE," 
            + "CREATE_USER_ID,"
            + "LAST_UPDATE_USER_ID," 
            + "COST_MODEL_ID," 
            + "CONTRACT_PRICE_PROFILE_SEQ) "
            + "values (PRC_PROF_NON_BRKT_CST_MDL_SEQ.nextVal," 
            + ":GFS_CUSTOMER_ID,"
            + ":GFS_CUSTOMER_TYPE_CODE," 
            + ":ITEM_PRICE_LEVEL_CODE," 
            + ":ITEM_PRICE_ID," 
            + ":EFFECTIVE_DATE,"
            + ":EXPIRATION_DATE," 
            + ":CREATE_USER_ID," 
            + ":LAST_UPDATE_USER_ID," 
            + ":COST_MODEL_ID,"
            + ":CONTRACT_PRICE_PROFILE_SEQ)";
    
    private static final String DELETE_PRC_PROF_NON_BRKT_CST_MDL = "DELETE from PRC_PROF_NON_BRKT_CST_MDL "
            + "where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ ";
    
    private static final String FETCH_COST_MODEL_ID = "select distinct(COST_MODEL_ID) "
            + "from PRC_PROF_NON_BRKT_CST_MDL "
            + " where CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "AND EXPIRATION_DATE >= SYSDATE "
            + "AND ROWNUM=1 "
            + "ORDER BY COST_MODEL_ID DESC";
 
    private static final String EXPIRE_PRC_PROF_NON_BRKT_CST_MDL_FOR_NON_CMG_CUSTOMERS_BY_CPP_SEQ = "UPDATE PRC_PROF_NON_BRKT_CST_MDL " 
            + "SET EXPIRATION_DATE              = :EXPIRATION_DATE, " 
            + "  LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + "  LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + "WHERE CONTRACT_PRICE_PROFILE_SEQ = :CONTRACT_PRICE_PROFILE_SEQ " 
            + "AND EXPIRATION_DATE             > :EXPIRATION_DATE "
            + "AND EFFECTIVE_DATE <= EXPIRATION_DATE "
            + "AND GFS_CUSTOMER_TYPE_CODE != "+CPPConstants.CMG_CUSTOMER_TYPE_CODE+"";            
    
    private static final String EXPIRE_PRC_PROF_NON_BRKT_CST_MDL_FOR_REAL_CUST = "UPDATE PRC_PROF_NON_BRKT_CST_MDL " 
            + " SET EXPIRATION_DATE              = :EXPIRATION_DATE, " 
            + "  LAST_UPDATE_TMSTMP             = SYSTIMESTAMP, " 
            + "  LAST_UPDATE_USER_ID            = :LAST_UPDATE_USER_ID " 
            + "WHERE EXPIRATION_DATE             > :EXPIRATION_DATE "
            + "AND GFS_CUSTOMER_ID = :GFS_CUSTOMER_ID "
            + "AND GFS_CUSTOMER_TYPE_CODE = :GFS_CUSTOMER_TYPE_CODE"
            + " AND EXPIRATION_DATE             >= :NEW_EFFECTIVE_DATE"
            + " AND EFFECTIVE_DATE              <= :NEW_EXPIRATION_DATE"
            + " AND EFFECTIVE_DATE <= EXPIRATION_DATE";
    
    private static final String UPDATE_PRC_PROF_NON_BRKT_CST_MDL = "UPDATE PRC_PROF_NON_BRKT_CST_MDL"
            + " SET COST_MODEL_ID                =:COST_MODEL_ID,"
            + " LAST_UPDATE_TMSTMP               =SYSTIMESTAMP,"
            + " LAST_UPDATE_USER_ID              =:LAST_UPDATE_USER_ID "
            + " where CONTRACT_PRICE_PROFILE_SEQ =:CONTRACT_PRICE_PROFILE_SEQ"
            + " AND ITEM_PRICE_LEVEL_CODE        =:ITEM_PRICE_LEVEL_CODE"
            + " AND EXPIRATION_DATE              >= SYSDATE";
    
    private static final String UPDATE_COST_MODEL = UPDATE_PRC_PROF_NON_BRKT_CST_MDL 
            + " AND GFS_CUSTOMER_ID               =:GFS_CUSTOMER_ID "
            + " AND GFS_CUSTOMER_TYPE_CODE        =:GFS_CUSTOMER_TYPE_CODE"
            + " AND (ITEM_PRICE_ID  is null or ITEM_PRICE_ID =:ITEM_PRICE_ID)";
    
    // @formatter:on

    public List<PrcProfNonBrktCstMdlDTO> fetchPrcProfNonBrktCstMdlForCMGForCPPSeq(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_PRC_PROF_NON_BRKT_CST_MDL_FOR_CMG_CPP_SEQ, paramMap, new PrcProfNonBrktCstMdlRowMapper());
    }

    public void savePrcProfNonBrktCstMdlForCustomer(List<PrcProfNonBrktCstMdlDO> prcProfNonBrktCstMdlDOList) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (PrcProfNonBrktCstMdlDO prcProfNonBrktCstMdlDO : prcProfNonBrktCstMdlDOList) {
            MapSqlParameterSource paramMap = prcProfNonBrktCstMdlParamListBuilder.createParamMap(prcProfNonBrktCstMdlDO);
            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(INSERT_PRC_PROF_NON_BRKT_CST_MDL, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public List<PrcProfNonBrktCstMdlDTO> fetchPrcProfNonBrktCstMdlForCPPSeq(int contractPriceProfileSeq,
            CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(GFS_CUSTOMER_ID, cmgCustomerResponseDTO.getId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, cmgCustomerResponseDTO.getTypeCode(), Types.INTEGER);
        return cppJdbcTemplate.query(FETCH_PRC_PROF_NON_BRKT_CST_MDL_FOR_CPP_SEQ, paramMap, new PrcProfNonBrktCstMdlRowMapper());
    }

    public void saveCostModelData(String userName, ContractPricingDO contractPricingDO, int itemLevelCostModelId, int subGroupLevelCostModelId,
            int productLevelCostModelId, Set<Integer> productIDs) {

        List<SqlParameterSource> paramList = new ArrayList<>();

        MapSqlParameterSource itemLevelCodeModel = buildParamForNonBrktCostModel(ItemPriceLevel.ITEM.getCode(), null, contractPricingDO,
                itemLevelCostModelId, contractPricingDO.getContractPriceProfileSeq(), userName);
        paramList.add(itemLevelCodeModel);

        MapSqlParameterSource subGroupLevelCostModel = buildParamForNonBrktCostModel(ItemPriceLevel.SUBGROUP.getCode(), null, contractPricingDO,
                subGroupLevelCostModelId, contractPricingDO.getContractPriceProfileSeq(), userName);
        paramList.add(subGroupLevelCostModel);

        for(Integer prodId:productIDs) {
            MapSqlParameterSource productLevelCostModel = buildParamForNonBrktCostModel(ItemPriceLevel.PRODUCT_TYPE.getCode(), String.valueOf(prodId),
                    contractPricingDO, productLevelCostModelId, contractPricingDO.getContractPriceProfileSeq(), userName);
            paramList.add(productLevelCostModel);
        }
        
        cppJdbcTemplate.batchUpdate(INSERT_PRC_PROF_NON_BRKT_CST_MDL, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    public void updateCostModelData(String userName, int itemLevelCostModelId, int subGroupLevelCostModelId, int productLevelCostModelId,
            int contractPriceProfileSeq) {

        List<SqlParameterSource> paramList = new ArrayList<>();
        MapSqlParameterSource itemLevelCodeModel = buildUpdateParamForNonBrktCostModel(ItemPriceLevel.ITEM.getCode(), itemLevelCostModelId,
                contractPriceProfileSeq, userName);
        paramList.add(itemLevelCodeModel);

        MapSqlParameterSource subGroupLevelCostModel = buildUpdateParamForNonBrktCostModel(ItemPriceLevel.SUBGROUP.getCode(),
                subGroupLevelCostModelId, contractPriceProfileSeq, userName);
        paramList.add(subGroupLevelCostModel);

        MapSqlParameterSource productLevelCostModel = buildUpdateParamForNonBrktCostModel(ItemPriceLevel.PRODUCT_TYPE.getCode(),
                    productLevelCostModelId, contractPriceProfileSeq, userName);
        paramList.add(productLevelCostModel);
      
        cppJdbcTemplate.batchUpdate(UPDATE_PRC_PROF_NON_BRKT_CST_MDL, paramList.toArray(new SqlParameterSource[paramList.size()]));

    }

    public void updateCostModel(Collection<PrcProfNonBrktCstMdlDTO> costModelTypeDTOList, int contractPriceProfileSeq, String userId) {
        List<SqlParameterSource> paramList = new ArrayList<>();
        for (PrcProfNonBrktCstMdlDTO costModelTypeDTO : costModelTypeDTOList) {
            MapSqlParameterSource paramMap = buildUpdateParamForCostModelUpdate(costModelTypeDTO, contractPriceProfileSeq, userId);

            paramList.add(paramMap);
        }
        cppJdbcTemplate.batchUpdate(UPDATE_COST_MODEL, paramList.toArray(new SqlParameterSource[paramList.size()]));
    }

    private MapSqlParameterSource buildUpdateParamForNonBrktCostModel(int itemPriceLevelCode, int costModelId, int contractPriceProfileSeq,
            String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(COST_MODEL_ID, costModelId, Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, itemPriceLevelCode, Types.INTEGER);

        return paramMap;
    }

    private MapSqlParameterSource buildParamForNonBrktCostModel(int itemPriceLevelCode, String itemPriceId, ContractPricingDO contractPricingDO,
            int costModelId, int contractPriceProfileSeq, String userName) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        paramMap.addValue(ITEM_PRICE_ID, itemPriceId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, contractPricingDO.getGfsCustomerId(), Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, contractPricingDO.getCustomerTypeCode(), Types.INTEGER);
        paramMap.addValue(ITEM_PRICE_LEVEL_CODE, itemPriceLevelCode, Types.INTEGER);
        paramMap.addValue(LAST_UPDATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CREATE_USER_ID, userName, Types.VARCHAR);
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(COST_MODEL_ID, costModelId, Types.INTEGER);
        paramMap.addValue(EFFECTIVE_DATE, contractPricingDO.getEffectiveDateFuture(), Types.DATE);
        paramMap.addValue(EXPIRATION_DATE, contractPricingDO.getExpirationDateFuture(), Types.DATE);

        return paramMap;
    }

    public void deletePriceProfileCostModel(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        cppJdbcTemplate.update(DELETE_PRC_PROF_NON_BRKT_CST_MDL, paramMap);
    }

    public Integer fetchCostModelId(int contractPriceProfileSeq) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_COST_MODEL_ID, paramMap, Integer.class);
    }

    public void expireNonCmgPriceProfileCostModelForContract(int contractPriceProfileSeq, Date expirationDate, String updatedUserId) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CONTRACT_PRICE_PROFILE_SEQ, contractPriceProfileSeq, Types.INTEGER);
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);

        cppJdbcTemplate.update(EXPIRE_PRC_PROF_NON_BRKT_CST_MDL_FOR_NON_CMG_CUSTOMERS_BY_CPP_SEQ, paramMap);

    }

    public void expirePriceProfileCostModelForRealCust(Date expirationDate, String updatedUserId, String customerId, int customerTypeCode,
            Date newPricingEffectiveDate, Date newPricingExpiryDate) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(EXPIRATION_DATE, expirationDate, Types.DATE);
        paramMap.addValue(LAST_UPDATE_USER_ID, updatedUserId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_ID, customerId, Types.VARCHAR);
        paramMap.addValue(GFS_CUSTOMER_TYPE_CODE, customerTypeCode, Types.INTEGER);
        paramMap.addValue(NEW_EFFECTIVE_DATE, newPricingEffectiveDate, Types.DATE);
        paramMap.addValue(NEW_EXPIRATION_DATE, newPricingExpiryDate, Types.DATE);
        cppJdbcTemplate.update(EXPIRE_PRC_PROF_NON_BRKT_CST_MDL_FOR_REAL_CUST, paramMap);

    }

    private MapSqlParameterSource buildUpdateParamForCostModelUpdate(PrcProfNonBrktCstMdlDTO costModelTypeDTO, int contractPriceProfileSeq,
            String userId) {
        MapSqlParameterSource mapSqlParams = buildUpdateParamForNonBrktCostModel(costModelTypeDTO.getItemPriceLevelCode(),
                costModelTypeDTO.getCostModelId(), contractPriceProfileSeq, userId);
        mapSqlParams.addValue(ITEM_PRICE_ID, costModelTypeDTO.getItemPriceId(), Types.VARCHAR);
        mapSqlParams.addValue(GFS_CUSTOMER_ID, costModelTypeDTO.getGfsCustomerId(), Types.VARCHAR);
        mapSqlParams.addValue(GFS_CUSTOMER_TYPE_CODE, costModelTypeDTO.getGfsCustomerTypeCode(), Types.INTEGER);
        return mapSqlParams;
    }
}
