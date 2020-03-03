package com.gfs.cpp.acceptanceTests.common.data;

import java.sql.Types;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CppCucumberRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    // @formatter:off

    private static final String INSERT_INTO_CLM_CONTRACT_TYPE = "Insert into CLM_CONTRACT_TYPE "
            + "(CLM_CONTRACT_TYPE_SEQ,"
            + "CLM_CONTRACT_TYPE_NAME,"
            + "CLM_CONTRACT_TYPE_DESC,"
            + "LAST_UPDATE_TMSTMP,"
            + "LAST_UPDATE_USER_ID) values "
            + "(CLM_CONTRACT_TYPE_SEQ.nextVal,"
            + ":contractTypeName,"
            + ":contractTypeDesc,"
            + "SYSTIMESTAMP,"
            + "'Cucumber Test')";
    
    private static final String INSERT_INTO_COST_MODEL = "Insert into COST_MODEL_MAP "
            + "(COST_MODEL_MAP_SEQ,"
            + "COST_MODEL_ID,TRANSFER_FEE_IND,"
            + "LABEL_ASSESMENT_IND,AUDIT_PRICING_IND,"
            + "PRICE_VERIFICATION_IND,CREATE_USER_ID,"
            + "CREATE_USER_TMSTMP,LAST_UPDATE_USER_ID"
            + ",LAST_UPDATE_TMSTMP) values (:costModelSeq,"
            + ":costModelId,"
            + ":labelAssesmentId,"
            + ":transferFeeInd,"
            + ":auditPricingInd,"
            + ":priceVerificationInd,"
            + "'Initial Load',"
            + "SYSTIMESTAMP,"
            + "'Initial Load',"
            + "SYSTIMESTAMP)";
    
    private static final String INSERT_APPLICATION =  "Insert into CLM_APPLICATION_TOKEN "
            + "(CLM_APPLICATION_TOKEN_ID,"
            + "APPLICATION_TOKEN_VALUE,"
            + "EXPIRATION_DATE,"
            + "LAST_UPDATE_USER_ID,"
            + "LAST_UPDATE_TMSTMP) values"
            + " (307,'token',:expirationDate,'Initial Load',SYSTIMESTAMP)";
    
    private static final String INSERT_CPP_LANGUAGE_ATTRIBUTE =  "Insert into CPP_LANGUAGE_ATTRIBUTE "
            + "(CPP_LANGUAGE_ATTRIBUTE_SEQ,"
            + "CPP_ATTRIBUTE_COLUMN_NAME,"
            + "CPP_ATTRIBUTE_COLUMN_DESC,"
            + "LAST_UPDATE_USER_ID,"
            + "LAST_UPDATE_TMSTMP) values "
            + "(:cppLanguageAttributeSeq,:cppAttributeColumnName,"
            + ":cppAttributeColumnDesc,'Initial Load',SYSTIMESTAMP)";
    
    private static final String INSERT_CPP_LANGUAGE_ATTRIBUTE_MAP = "Insert into CPP_LANGUAGE_ATTRIBUTE_MAP "
            + "(CPP_LANGUAGE_ATTRIBUTE_MAP_SEQ,"
            + "CPP_LANGUAGE_ATTRIBUTE_SEQ,"
            + "CPP_ATTRIBUTE_COLUMN_VALUE,"
            + "CLM_CONTRACT_TYPE_NAME,"
            + "CPP_LANGUAGE_STATEMENT_SEQ,"
            + "LAST_UPDATE_USER_ID,LAST_UPDATE_TMSTMP) "
            + "values (:cppLanguageAttributeMapSeq,"
            + ":cppLanguageAttributeSeq,"
            + ":cppAttributeColumnValue,'DEFAULT',:cppLanguageStatementSeq,'Intial Load',SYSTIMESTAMP)";
    
    private static final String INSERT_CPP_LANGUAGE_STATEMENT = "Insert into CPP_LANGUAGE_STATEMENT"
            + " (CPP_LANGUAGE_STATEMENT_SEQ,LANGUAGE_TYPE_CODE,PRICING_LANGUAGE_STATEMENT_TXT,LAST_UPDATE_USER_ID,LAST_UPDATE_TMSTMP) "
            + "values (:cppLanguageStatementSeq,'en',:statementTxt,"
            + "'Intial Load',SYSTIMESTAMP)";
    
    // @formatter:on

    public void insertContractType(String contractTypeName, String contractTypeDesc) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("contractTypeName", contractTypeName, Types.VARCHAR);
        paramMap.addValue("contractTypeDesc", contractTypeDesc, Types.VARCHAR);
        cppJdbcTemplate.update(INSERT_INTO_CLM_CONTRACT_TYPE, paramMap);

    }

    public void insertCostModel(int costModelSeq, int costModelId, int labelAssesmentId, int transferFeeInd, int auditPriceingInd,
            int priceVerificationInd) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        paramMap.addValue("costModelSeq", costModelSeq, Types.INTEGER);
        paramMap.addValue("costModelId", costModelId, Types.INTEGER);
        paramMap.addValue("labelAssesmentId", labelAssesmentId, Types.INTEGER);
        paramMap.addValue("transferFeeInd", transferFeeInd, Types.INTEGER);
        paramMap.addValue("auditPricingInd", auditPriceingInd, Types.INTEGER);
        paramMap.addValue("priceVerificationInd", priceVerificationInd, Types.INTEGER);

        cppJdbcTemplate.update(INSERT_INTO_COST_MODEL, paramMap);

    }

    public void insertApplicationToken() {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();

        paramMap.addValue("expirationDate", new LocalDate(2099, 1, 1).toDate(), Types.DATE);

        cppJdbcTemplate.update(INSERT_APPLICATION, paramMap);

    }

    public void insertCppLanguageAttribute(int cppLanguageAttributeSeq, String cppAttributeColumnName, String attributeColumnDesc) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("cppLanguageAttributeSeq", cppLanguageAttributeSeq, Types.INTEGER);
        paramMap.addValue("cppAttributeColumnDesc", attributeColumnDesc, Types.VARCHAR);
        paramMap.addValue("cppAttributeColumnName", cppAttributeColumnName, Types.VARCHAR);

        cppJdbcTemplate.update(INSERT_CPP_LANGUAGE_ATTRIBUTE, paramMap);
    }

    public void insertCppLanguageAttributeMap(int cppLangugageAttributeMapSeq, int cppLanguageAttributeSeq, String cppAttributeColumnValue,
            int cppLanguageStatementSeq) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("cppLanguageAttributeSeq", cppLanguageAttributeSeq, Types.INTEGER);
        paramMap.addValue("cppLanguageAttributeMapSeq", cppLangugageAttributeMapSeq, Types.INTEGER);
        paramMap.addValue("cppAttributeColumnValue", cppAttributeColumnValue, Types.VARCHAR);
        paramMap.addValue("cppLanguageStatementSeq", cppLanguageStatementSeq, Types.INTEGER);

        cppJdbcTemplate.update(INSERT_CPP_LANGUAGE_ATTRIBUTE_MAP, paramMap);
    }

    public void insertCppLanguageAttributeStatement(int cppLanguageStatementSeq, Object statementTxt) {

        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("cppLanguageStatementSeq", cppLanguageStatementSeq, Types.INTEGER);
        paramMap.addValue("statementTxt", statementTxt, Types.INTEGER);
        

        cppJdbcTemplate.update(INSERT_CPP_LANGUAGE_STATEMENT, paramMap);
    }

}
