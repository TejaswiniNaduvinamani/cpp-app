package com.gfs.cpp.common.model.splitcase;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SplitCaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    private int contractPriceProfileSeq;
    private String productType;
    private double splitCaseFee;
    private String unit;
    private Date effectiveDate;
    private Date expirationDate;
    private String itemPriceId;
    private int gfsCustomerTypeCode;
    private int lessCaseRuleId;

    public int getLessCaseRuleId() {
        return lessCaseRuleId;
    }

    public void setLessCaseRuleId(int lessCaseRuleId) {
        this.lessCaseRuleId = lessCaseRuleId;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getSplitCaseFee() {
        return splitCaseFee;
    }

    public void setSplitCaseFee(double splitCaseFee) {
        this.splitCaseFee = splitCaseFee;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(String itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public int getGfsCustomerTypeCode() {
        return gfsCustomerTypeCode;
    }

    public void setGfsCustomerTypeCode(int gfsCustomerTypeCode) {
        this.gfsCustomerTypeCode = gfsCustomerTypeCode;
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
