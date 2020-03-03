package com.gfs.cpp.common.model.splitcase;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PrcProfLessCaseRuleDO implements Serializable {

    private static final long serialVersionUID = 1L;
    private int contractPriceProfileSeq;
    private String gfsCustomerId;
    private int gfsCustomerTypeCode;
    private Date effectiveDate;
    private Date expirationDate;
    private String createUserId;
    private String lastUpdateUserId;
    private int lessCaseRuleId;
    private int cwMarkupAmnt;
    private String cwMarkupAmountTypeCode;
    private int nonCwMarkupAmnt;
    private String nonCwMarkupAmntTypeCode;
    private int markupAppliedBeforeDivInd;
    private int itemPriceLevelCode;
    private String itemPriceId;

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public int getLesscaseRuleId() {
        return lessCaseRuleId;
    }

    public void setLesscaseRuleId(int lesscaseRuleId) {
        this.lessCaseRuleId = lesscaseRuleId;
    }

    public int getCwMarkupAmnt() {
        return cwMarkupAmnt;
    }

    public void setCwMarkupAmnt(int cwMarkupAmnt) {
        this.cwMarkupAmnt = cwMarkupAmnt;
    }

    public String getCwMarkupAmountTypeCode() {
        return cwMarkupAmountTypeCode;
    }

    public void setCwMarkupAmountTypeCode(String cwMarkupAmountTypeCode) {
        this.cwMarkupAmountTypeCode = cwMarkupAmountTypeCode;
    }

    public int getNonCwMarkupAmnt() {
        return nonCwMarkupAmnt;
    }

    public void setNonCwMarkupAmnt(int nonCwMarkupAmnt) {
        this.nonCwMarkupAmnt = nonCwMarkupAmnt;
    }

    public String getNonCwMarkupAmntTypeCode() {
        return nonCwMarkupAmntTypeCode;
    }

    public void setNonCwMarkupAmntTypeCode(String nonCwMarkupAmntTypeCode) {
        this.nonCwMarkupAmntTypeCode = nonCwMarkupAmntTypeCode;
    }

    public int getMarkupAppliedBeforeDivInd() {
        return markupAppliedBeforeDivInd;
    }

    public void setMarkupAppliedBeforeDivInd(int markupAppliedBeforeDivInd) {
        this.markupAppliedBeforeDivInd = markupAppliedBeforeDivInd;
    }

    public int getItemPriceLevelCode() {
        return itemPriceLevelCode;
    }

    public void setItemPriceLevelCode(int itemPriceLevelCode) {
        this.itemPriceLevelCode = itemPriceLevelCode;
    }

    public String getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(String itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

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

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
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
