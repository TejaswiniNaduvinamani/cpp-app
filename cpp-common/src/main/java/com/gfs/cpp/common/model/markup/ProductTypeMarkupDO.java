package com.gfs.cpp.common.model.markup;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProductTypeMarkupDO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String productType;
    private int itemPriceId;
    private BigDecimal markup;
    private String unit;
    private int markupType;
    private Date effectiveDate;
    private Date expirationDate;
    private String gfsCustomerId;
    private int customerTypeCode;
    
    private int contractPriceProfileSeq;
    private int priceLockedInTypeCode;
    private int holdCostFirmInd;
    private int priceLockinReasonCode;
    private int priceMaintenanceSourceCode;
    private String createUserId;
    private String lastUpdateUserId;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(int itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public BigDecimal getMarkup() {
        return markup;
    }

    public void setMarkup(BigDecimal markup) {
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

    public String getGfsCustomerId() {
        return gfsCustomerId;
    }

    public void setGfsCustomerId(String gfsCustomerId) {
        this.gfsCustomerId = gfsCustomerId;
    }

    public int getCustomerTypeCode() {
        return customerTypeCode;
    }

    public void setCustomerTypeCode(int customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
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
