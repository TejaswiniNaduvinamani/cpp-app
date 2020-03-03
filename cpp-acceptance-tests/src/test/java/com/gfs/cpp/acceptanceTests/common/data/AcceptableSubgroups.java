package com.gfs.cpp.acceptanceTests.common.data;

import static com.gfs.cpp.acceptanceTests.config.CukesConstants.EMPTY_SUBGROUP_DESC;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.SUBGROUP_DESCRIPTION;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.SUBGROUP_ID_INVALID;
import static com.gfs.cpp.acceptanceTests.config.CukesConstants.SUBGROUP_ID_VALID;

public enum AcceptableSubgroups {

    // @formatter:off
    VALID_SUBGROUP_ID(SUBGROUP_ID_VALID, SUBGROUP_DESCRIPTION), 
    INVALID_SUBGROUP_ID(SUBGROUP_ID_INVALID, EMPTY_SUBGROUP_DESC);

    String subgroupId;
    String subgroupDesc;

    private AcceptableSubgroups(String subgroupId, String subgroupDesc) {
        this.subgroupId = subgroupId;
        this.subgroupDesc = subgroupDesc;
    }

    public String getSubgroupId() {
        return subgroupId;
    }

    public String getSubgroupDesc() {
        return subgroupDesc;
    }

}
