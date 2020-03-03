package com.gfs.cpp.common.dto.customerinfo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CPPInformationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int contractPriceProfileId;
    private int contractPriceProfileSeq;
    private int versionNumber;
    
    public int getContractPriceProfileId() {
        return contractPriceProfileId;
    }

    public void setContractPriceProfileId(int contractPriceProfileId) {
        this.contractPriceProfileId = contractPriceProfileId;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
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
