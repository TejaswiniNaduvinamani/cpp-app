package com.gfs.cpp.common.dto.markup;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ItemInformationDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String itemNo;
    private String itemDescription;
    private String itemStatusCode;
    private String stockingCode;
    private boolean isActive;
    private boolean isValid;
    private boolean isItemAlreadyExist;

    public boolean getIsItemAlreadyExist() {
        return isItemAlreadyExist;
    }

    public void setIsItemAlreadyExist(boolean isItemAlreadyExist) {
        this.isItemAlreadyExist = isItemAlreadyExist;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemStatusCode() {
        return itemStatusCode;
    }

    public void setItemStatusCode(String itemStatusCode) {
        this.itemStatusCode = itemStatusCode;
    }

    public String getStockingCode() {
        return stockingCode;
    }

    public void setStockingCode(String stockingCode) {
        this.stockingCode = stockingCode;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
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
