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

public class ProductTypeMarkupDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String gfsCustomerId;
    private String productType;
    private int itemPriceId;
    private String markup;
    private String unit;
    private int markupType;

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date effectiveDate;

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date expirationDate;
    private int gfsCustomerTypeCode;
    private int customerItemPriceSeq;
    private int contractPriceProfileSeq;
    private int priceLockedInTypeCode;
    private int holdCostFirmInd;
    private int priceLockinReasonCode;
    private int priceMaintenanceSourceCode;

    public String getGfsCustomerId() {
        return gfsCustomerId;
    }

    public void setGfsCustomerId(String gfsCustomerId) {
        this.gfsCustomerId = gfsCustomerId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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

    public int getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(int itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public int getCustomerItemPriceSeq() {
        return customerItemPriceSeq;
    }

    public void setCustomerItemPriceSeq(int customerItemPriceSeq) {
        this.customerItemPriceSeq = customerItemPriceSeq;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public int getPriceLockedInTypeCode() {
        return priceLockedInTypeCode;
    }

    public void setPriceLockedInTypeCode(int priceLockedInTypeCode) {
        this.priceLockedInTypeCode = priceLockedInTypeCode;
    }

    public int getHoldCostFirmInd() {
        return holdCostFirmInd;
    }

    public void setHoldCostFirmInd(int holdCostFirmInd) {
        this.holdCostFirmInd = holdCostFirmInd;
    }

    public int getPriceLockinReasonCode() {
        return priceLockinReasonCode;
    }

    public void setPriceLockinReasonCode(int priceLockinReasonCode) {
        this.priceLockinReasonCode = priceLockinReasonCode;
    }

    public int getPriceMaintenanceSourceCode() {
        return priceMaintenanceSourceCode;
    }

    public void setPriceMaintenanceSourceCode(int priceMaintenanceSourceCode) {
        this.priceMaintenanceSourceCode = priceMaintenanceSourceCode;
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
