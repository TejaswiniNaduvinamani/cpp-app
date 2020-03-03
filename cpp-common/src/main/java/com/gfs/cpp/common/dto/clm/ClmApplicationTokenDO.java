package com.gfs.cpp.common.dto.clm;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ClmApplicationTokenDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int clmApplicationTokenId;
    private String applicationTokenValue;

    public int getClmApplicationTokenId() {
        return clmApplicationTokenId;
    }

    public void setClmApplicationTokenId(int clmApplicationTokenId) {
        this.clmApplicationTokenId = clmApplicationTokenId;
    }

    public String getApplicationTokenValue() {
        return applicationTokenValue;
    }

    public void setApplicationTokenValue(String applicationTokenValue) {
        this.applicationTokenValue = applicationTokenValue;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
