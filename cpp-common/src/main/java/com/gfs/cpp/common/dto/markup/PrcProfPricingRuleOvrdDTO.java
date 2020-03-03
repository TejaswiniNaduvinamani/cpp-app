package com.gfs.cpp.common.dto.markup;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PrcProfPricingRuleOvrdDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int pricingOverrideInd;
    private int pricingOverrideId;
    private int contractPriceProfileSeq;
    private String gfsCustomerId;
    private int gfsCustomerTypeCode;
    
    public String getGfsCustomerId() {
        return gfsCustomerId;
    }

    public void setGfsCustomerId(String gfsCustomerId) {
        this.gfsCustomerId = gfsCustomerId;
    }

    public int getGfsCustomerTypeCode() {
        return gfsCustomerTypeCode;
    }

    public void setGfsCustomerTypeCode(int gfsCustomerTypeCode) {
        this.gfsCustomerTypeCode = gfsCustomerTypeCode;
    }

    public int getPricingOverrideInd() {
        return pricingOverrideInd;
    }

    public void setPricingOverrideInd(int pricingOverrideInd) {
        this.pricingOverrideInd = pricingOverrideInd;
    }

    public int getPricingOverrideId() {
        return pricingOverrideId;
    }

    public void setPricingOverrideId(int pricingOverrideId) {
        this.pricingOverrideId = pricingOverrideId;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
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
