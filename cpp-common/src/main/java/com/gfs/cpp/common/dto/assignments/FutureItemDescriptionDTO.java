package com.gfs.cpp.common.dto.assignments;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FutureItemDescriptionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String gfsCustomerId;
    private int gfsCustomerTypeCode;
    private String futureItemDesc;
    private int customerItemDescSeq;
    private int costMarkupAmt;
    private String markupAmountTypeCode;
    private int markupUnitTypeCode;
    
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

    public String getFutureItemDesc() {
        return futureItemDesc;
    }

    public void setFutureItemDesc(String futureItemDesc) {
        this.futureItemDesc = futureItemDesc;
    }

    public int getCustomerItemDescSeq() {
        return customerItemDescSeq;
    }

    public void setCustomerItemDescSeq(int customerItemDescSeq) {
        this.customerItemDescSeq = customerItemDescSeq;
    }

    public int getCostMarkupAmt() {
        return costMarkupAmt;
    }

    public void setCostMarkupAmt(int costMarkupAmt) {
        this.costMarkupAmt = costMarkupAmt;
    }

    public String getMarkupAmountTypeCode() {
        return markupAmountTypeCode;
    }

    public void setMarkupAmountTypeCode(String markupAmountTypeCode) {
        this.markupAmountTypeCode = markupAmountTypeCode;
    }

    public int getMarkupUnitTypeCode() {
        return markupUnitTypeCode;
    }

    public void setMarkupUnitTypeCode(int markupUnitTypeCode) {
        this.markupUnitTypeCode = markupUnitTypeCode;
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
