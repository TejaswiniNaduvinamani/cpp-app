package com.gfs.cpp.proxy.clm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmApplicationTokenDO;
import com.gfs.cpp.data.clm.ClmApplicationTokenRepository;

@Component
public class ClmApplicationTokenFetcher {

    private ClmApplicationTokenDO clmApplicationTokenDO;

    @Autowired
    private ClmApplicationTokenRepository clmApplicationTokenRepository;

    public String getApplicationToken() {

        return getClmApplicationToken().getApplicationTokenValue();
    }

    private ClmApplicationTokenDO getClmApplicationToken() {

        if (clmApplicationTokenDO == null) {
            clmApplicationTokenDO = clmApplicationTokenRepository.fetchAllClmApplicationTokens().get(0);
        }

        return clmApplicationTokenDO;
    }

    public int getLoggedInUserId() {
        return getClmApplicationToken().getClmApplicationTokenId();
    }

}
