package com.gfs.cpp.common.dto.markup;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PrcProfCostSchedulePkgScheduledGroupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int prcProfCostSchedulePkgSeq;
    private int scheduleGroup;
    private String costRunFrequencyCode;
    private int contractPriceSeq;

    public int getPrcProfCostSchedulePkgSeq() {
        return prcProfCostSchedulePkgSeq;
    }

    public void setPrcProfCostSchedulePkgSeq(int prcProfCostSchedulePkgSeq) {
        this.prcProfCostSchedulePkgSeq = prcProfCostSchedulePkgSeq;
    }

    public int getScheduleGroup() {
        return scheduleGroup;
    }

    public void setScheduleGroup(int scheduleGroup) {
        this.scheduleGroup = scheduleGroup;
    }

    public String getCostRunFrequencyCode() {
        return costRunFrequencyCode;
    }

    public void setCostRunFrequencyCode(String costRunFrequencyCode) {
        this.costRunFrequencyCode = costRunFrequencyCode;
    }

    public int getContractPriceSeq() {
        return contractPriceSeq;
    }

    public void setContractPriceSeq(int contractPriceSeq) {
        this.contractPriceSeq = contractPriceSeq;
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
