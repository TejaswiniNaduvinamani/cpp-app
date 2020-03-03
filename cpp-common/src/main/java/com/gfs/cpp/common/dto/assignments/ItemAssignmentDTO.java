package com.gfs.cpp.common.dto.assignments;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gfs.cpp.common.dto.clm.JsonDateDeserializer;
import com.gfs.cpp.common.dto.clm.JsonDateSerializer;

public class ItemAssignmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String itemId;
    private String itemDescription;
    private boolean isItemSaved;
    private int itemPriceLevelCode;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date effectiveDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date expirationDate;
    private int customerItemDescSeq;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public boolean getIsItemSaved() {
        return isItemSaved;
    }

    public void setIsItemSaved(boolean isItemSaved) {
        this.isItemSaved = isItemSaved;
    }

    public int getItemPriceLevelCode() {
        return itemPriceLevelCode;
    }

    public void setItemPriceLevelCode(int itemPriceLevelCode) {
        this.itemPriceLevelCode = itemPriceLevelCode;
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

    public int getCustomerItemDescSeq() {
        return customerItemDescSeq;
    }

    public void setCustomerItemDescSeq(int customerItemDescSeq) {
        this.customerItemDescSeq = customerItemDescSeq;
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
