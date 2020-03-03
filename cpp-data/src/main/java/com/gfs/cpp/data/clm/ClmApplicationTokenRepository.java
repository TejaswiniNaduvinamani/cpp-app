package com.gfs.cpp.data.clm;

import static com.gfs.cpp.data.clm.ClmApplicationTokenRowMapper.FETCH_ALL_APPLICATION_TOKENS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gfs.cpp.common.dto.clm.ClmApplicationTokenDO;

@Repository
public class ClmApplicationTokenRepository {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    public List<ClmApplicationTokenDO> fetchAllClmApplicationTokens() {

        return cppJdbcTemplate.query(FETCH_ALL_APPLICATION_TOKENS, new ClmApplicationTokenRowMapper());

    }

}
