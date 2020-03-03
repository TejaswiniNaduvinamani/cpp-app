package com.gfs.cpp.common.dto.furtherance;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FurtheranceBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String agreementId;
    private String contractType;
    private int cppFurtheranceSeq;

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public int getCppFurtheranceSeq() {
        return cppFurtheranceSeq;
    }

    public void setCppFurtheranceSeq(int cppFurtheranceSeq) {
        this.cppFurtheranceSeq = cppFurtheranceSeq;
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
