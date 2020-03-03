package com.gfs.cpp.data.contractpricing;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;

public class PrcProfNonBrktCstMdlRowMapper implements RowMapper<PrcProfNonBrktCstMdlDTO> {
    
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String COST_MODEL_ID = "COST_MODEL_ID";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
   
    // @formatter:off
    public static final String FETCH_PRC_PROF_NON_BRKT_CST_MDL_FOR_CPP_SEQ = "SELECT CONTRACT_PRICE_PROFILE_SEQ, "
            + " ITEM_PRICE_LEVEL_CODE, "
            + " ITEM_PRICE_ID, "
            + " COST_MODEL_ID,"
            + " GFS_CUSTOMER_ID,"
            + " GFS_CUSTOMER_TYPE_CODE "
            + " FROM PRC_PROF_NON_BRKT_CST_MDL"
            + " WHERE CONTRACT_PRICE_PROFILE_SEQ=:CONTRACT_PRICE_PROFILE_SEQ "
            + "AND GFS_CUSTOMER_ID=:GFS_CUSTOMER_ID "
            + "AND GFS_CUSTOMER_TYPE_CODE=:GFS_CUSTOMER_TYPE_CODE "
            + "AND EXPIRATION_DATE >= SYSDATE"; 
    
    public static final String FETCH_PRC_PROF_NON_BRKT_CST_MDL_FOR_CMG_CPP_SEQ = "SELECT "
            + "     ppnbcm.contract_price_profile_seq, "  
            + "     ppnbcm.item_price_level_code, " 
            + "     ppnbcm.item_price_id, " 
            + "     ppnbcm.cost_model_id, " 
            + "     ppnbcm.gfs_customer_id, " 
            + "     ppnbcm.gfs_customer_type_code " 
            + "  FROM PRC_PROF_NON_BRKT_CST_MDL ppnbcm " 
            + "     inner join CONTRACT_PRICE_PROF_CUSTOMER cppc " 
            + "         ON ppnbcm.contract_price_profile_seq = cppc.contract_price_profile_seq "
            + "         AND cppc.GFS_CUSTOMER_TYPE_CODE = ppnbcm.GFS_CUSTOMER_TYPE_CODE" 
            + "  WHERE  cppc.default_customer_ind = 1 " 
            + "  AND cppc.contract_price_profile_seq = :CONTRACT_PRICE_PROFILE_SEQ "
            + " ORDER BY ITEM_PRICE_LEVEL_CODE ASC ,  TO_NUMBER(ITEM_PRICE_ID) ASC ";
    
     // @formatter:on

    @Override
    public PrcProfNonBrktCstMdlDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO.setContractPriceProfileSeq(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ));
        prcProfNonBrktCstMdlDTO.setItemPriceId(rs.getString(ITEM_PRICE_ID));
        prcProfNonBrktCstMdlDTO.setItemPriceLevelCode(rs.getInt(ITEM_PRICE_LEVEL_CODE));
        prcProfNonBrktCstMdlDTO.setCostModelId(rs.getInt(COST_MODEL_ID));
        prcProfNonBrktCstMdlDTO.setGfsCustomerId(rs.getString(GFS_CUSTOMER_ID));
        prcProfNonBrktCstMdlDTO.setGfsCustomerTypeCode(rs.getInt(GFS_CUSTOMER_TYPE_CODE));
        return prcProfNonBrktCstMdlDTO;
    }

}
