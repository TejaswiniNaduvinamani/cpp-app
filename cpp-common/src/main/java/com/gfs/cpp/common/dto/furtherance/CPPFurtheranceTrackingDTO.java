package com.gfs.cpp.common.dto.furtherance;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CPPFurtheranceTrackingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int cppFurtheranceTrackingSeq;
    private int cppFurtheranceSeq;
    private int furtheranceActionCode;
    private String itemPriceId;
    private int itemPriceLevelCode;
    private String gfsCustomerId;
    private int gfsCustomerTypeCode;
    private String changeTableName;
    private String createUserId;
    private Date createTimestamp;
    private String lastUpdateUserId;
    private Date lastUpdateTimestamp;

    public int getCppFurtheranceTrackingSeq() {
        return cppFurtheranceTrackingSeq;
    }

    public void setCppFurtheranceTrackingSeq(int cppFurtheranceTrackingSeq) {
        this.cppFurtheranceTrackingSeq = cppFurtheranceTrackingSeq;
    }

    public int getCppFurtheranceSeq() {
        return cppFurtheranceSeq;
    }

    public void setCppFurtheranceSeq(int cppFurtheranceSeq) {
        this.cppFurtheranceSeq = cppFurtheranceSeq;
    }

    public int getFurtheranceActionCode() {
        return furtheranceActionCode;
    }

    public void setFurtheranceActionCode(int furtheranceActionCode) {
        this.furtheranceActionCode = furtheranceActionCode;
    }

    public String getItemPriceId() {
        return itemPriceId;
    }

    public void setItemPriceId(String itemPriceId) {
        this.itemPriceId = itemPriceId;
    }

    public int getItemPriceLevelCode() {
        return itemPriceLevelCode;
    }

    public void setItemPriceLevelCode(int itemPriceLevelCode) {
        this.itemPriceLevelCode = itemPriceLevelCode;
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

    public String getChangeTableName() {
        return changeTableName;
    }

    public void setChangeTableName(String changeTableName) {
        this.changeTableName = changeTableName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public Date getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
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
