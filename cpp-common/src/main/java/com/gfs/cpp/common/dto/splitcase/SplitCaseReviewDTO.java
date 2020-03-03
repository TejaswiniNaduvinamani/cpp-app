package com.gfs.cpp.common.dto.splitcase;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SplitCaseReviewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String feeTypeContractLanguage;
    private int feeType;
    private List<SplitCaseDTO> splitCaseFeeValues;

    public List<SplitCaseDTO> getSplitCaseFeeValues() {
        return splitCaseFeeValues;
    }

    public void setSplitCaseFeeValues(List<SplitCaseDTO> splitCaseFeeValues) {
        this.splitCaseFeeValues = splitCaseFeeValues;
    }

    public String getFeeTypeContractLanguage() {
        return feeTypeContractLanguage;
    }

    public void setFeeTypeContractLanguage(String feeTypeContractLanguage) {
        this.feeTypeContractLanguage = feeTypeContractLanguage;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
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
