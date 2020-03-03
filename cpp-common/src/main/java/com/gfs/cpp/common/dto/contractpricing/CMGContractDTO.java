package com.gfs.cpp.common.dto.contractpricing;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CMGContractDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String contractPriceProfileId;
    private String contractName;

    public String getContractPriceProfileId() {
        return contractPriceProfileId;
    }

    public void setContractPriceProfileId(String contractPriceProfileId) {
        this.contractPriceProfileId = contractPriceProfileId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
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
