package com.gfs.cpp.common.dto.markup;

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

public class ItemLevelMarkupDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean noItemId;
    private String itemId;
    private String itemDesc;
    private int stockingCode;
    private String markup;
    private String unit;
    private int markupType;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date effectiveDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date expirationDate;
    private boolean invalid;
    private boolean inactive;
    private boolean isItemSaved;
    private int customerItemDescSeq;

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public boolean getIsItemSaved() {
        return isItemSaved;
    }

    public void setIsItemSaved(boolean isItemSaved) {
        this.isItemSaved = isItemSaved;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getStockingCode() {
        return stockingCode;
    }

    public void setStockingCode(int stockingCode) {
        this.stockingCode = stockingCode;
    }

    public String getMarkup() {
        return markup;
    }

    public void setMarkup(String markup) {
        this.markup = markup;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getMarkupType() {
        return markupType;
    }

    public void setMarkupType(int markupType) {
        this.markupType = markupType;
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

    public boolean isNoItemId() {
        return noItemId;
    }

    public void setNoItemId(boolean noItemId) {
        this.noItemId = noItemId;
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
