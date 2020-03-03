package com.gfs.cpp.data.review;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.constants.CPPConstants;

@Repository(value = "reviewRepository")
public class ReviewRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    private static final String CPP_LANGUAGE_ATTRIBUTE_SEQ = "CPP_LANGUAGE_ATTRIBUTE_SEQ";
    private static final String CPP_ATTRIBUTE_COLUMN_VALUE = "CPP_ATTRIBUTE_COLUMN_VALUE";
    private static final String CPP_ATTRIBUTE_COLUMN_DESC = "CPP_ATTRIBUTE_COLUMN_DESC";
    private static final String CPP_LANGUAGE_STATEMENT_SEQ = "CPP_LANGUAGE_STATEMENT_SEQ";

 // @formatter:off
    private static final String FETCH_CONTRACT_LANGUAGE_SEQUENCE = "select CPP_LANGUAGE_ATTRIBUTE_SEQ "
            + "from CPP_LANGUAGE_ATTRIBUTE "
            + "where CPP_ATTRIBUTE_COLUMN_DESC=:CPP_ATTRIBUTE_COLUMN_DESC";
    private static final String FETCH_CPP_LANGUAGE_STATEMENT = "select PRICING_LANGUAGE_STATEMENT_TXT "
            + "from CPP_LANGUAGE_STATEMENT "
            + "where CPP_LANGUAGE_STATEMENT_SEQ "
            + "in(select CPP_LANGUAGE_STATEMENT_SEQ "
            + "from CPP_LANGUAGE_ATTRIBUTE_MAP "
            + "where CPP_ATTRIBUTE_COLUMN_VALUE = :CPP_ATTRIBUTE_COLUMN_VALUE " 
            + " and CPP_LANGUAGE_ATTRIBUTE_SEQ = :CPP_LANGUAGE_ATTRIBUTE_SEQ)";
    private static final String FETCH_PRICING_LANGUAGE_STATEMENT="select PRICING_LANGUAGE_STATEMENT_TXT "
            + "from CPP_LANGUAGE_STATEMENT "
            + "where CPP_LANGUAGE_STATEMENT_SEQ=:CPP_LANGUAGE_STATEMENT_SEQ"; 
 // @formatter:on
    public String fetchContractLanguage(int cppAttributeSeq, String cppAttributeColumn) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_LANGUAGE_ATTRIBUTE_SEQ, cppAttributeSeq, Types.INTEGER);
        paramMap.addValue(CPP_ATTRIBUTE_COLUMN_VALUE, cppAttributeColumn, Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(FETCH_CPP_LANGUAGE_STATEMENT, paramMap, String.class);
    }

    public int fetchContractLanguageSeq(String cppAttribute) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_ATTRIBUTE_COLUMN_DESC, cppAttribute, Types.VARCHAR);
        return cppJdbcTemplate.queryForObject(FETCH_CONTRACT_LANGUAGE_SEQUENCE, paramMap, Integer.class);
    }

    public String fetchContractLanguageMarkup() {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue(CPP_LANGUAGE_STATEMENT_SEQ, CPPConstants.VALUE_THIRTEEN, Types.INTEGER);
        return cppJdbcTemplate.queryForObject(FETCH_PRICING_LANGUAGE_STATEMENT, paramMap, String.class);
    }

}
