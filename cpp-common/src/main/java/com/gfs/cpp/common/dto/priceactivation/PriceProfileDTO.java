package com.gfs.cpp.common.dto.priceactivation;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PriceProfileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean prcProfAuditAuthInd;

    private boolean prcProfVerifyPrivInd;

    private int prcProfCostSchedulePkgSeq;
    
    private int costModelId;
    
    private int pricingOverrideId;
    
    private boolean pricingOverrideInd;

    public boolean isPrcProfAuditAuthInd() {
        return prcProfAuditAuthInd;
    }

    public void setPrcProfAuditAuthInd(boolean prcProfAuditAuthInd) {
        this.prcProfAuditAuthInd = prcProfAuditAuthInd;
    }

    public boolean isPrcProfVerifyPrivInd() {
        return prcProfVerifyPrivInd;
    }

    public void setPrcProfVerifyPrivInd(boolean prcProfVerifyPrivInd) {
        this.prcProfVerifyPrivInd = prcProfVerifyPrivInd;
    }

    public int getPrcProfCostSchedulePkgSeq() {
        return prcProfCostSchedulePkgSeq;
    }

    public void setPrcProfCostSchedulePkgSeq(int prcProfCostSchedulePkgSeq) {
        this.prcProfCostSchedulePkgSeq = prcProfCostSchedulePkgSeq;
    }

    public int getCostModelId() {
        return costModelId;
    }

    public void setCostModelId(int costModelId) {
        this.costModelId = costModelId;
    }

    public int getPricingOverrideId() {
        return pricingOverrideId;
    }

    public void setPricingOverrideId(int pricingOverrideId) {
        this.pricingOverrideId = pricingOverrideId;
    }

    public boolean isPricingOverrideInd() {
        return pricingOverrideInd;
    }

    public void setPricingOverrideInd(boolean pricingOverrideInd) {
        this.pricingOverrideInd = pricingOverrideInd;
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
