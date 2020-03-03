package com.gfs.cpp.common.model.assignments;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ItemAssignmentDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int customerItemDescSeq;
    private int itemPriceLevelCode;
    private String itemPriceId;
    private Date effectiveDate;
    private Date expirationDate;

    public int getCustomerItemDescSeq() {
        return customerItemDescSeq;
    }

    public void setCustomerItemDescSeq(int customerItemDescSeq) {
        this.customerItemDescSeq = customerItemDescSeq;
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
