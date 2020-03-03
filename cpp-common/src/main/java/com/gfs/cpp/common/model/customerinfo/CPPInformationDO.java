package com.gfs.cpp.common.model.customerinfo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CPPInformationDO implements Serializable{

    private static final long serialVersionUID = 1L;
    private Integer contractPriceProfileSeq;
    private Integer contractPriceProfileId;
    private Integer clmContractTypeCode;
    private Date priceEffectiveDate;
    private Date priceExpirationDate;
    private String lastUpdateUserId;
    public Integer getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }
    public void setContractPriceProfileSeq(Integer contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }
    public Integer getContractPriceProfileId() {
        return contractPriceProfileId;
    }
    public void setContractPriceProfileId(Integer contractPriceProfileId) {
        this.contractPriceProfileId = contractPriceProfileId;
    }
    public Integer getClmContractTypeCode() {
        return clmContractTypeCode;
    }
    public void setClmContractTypeCode(Integer clmContractTypeCode) {
        this.clmContractTypeCode = clmContractTypeCode;
    }
    public Date getPriceEffectiveDate() {
        return priceEffectiveDate;
    }
    public void setPriceEffectiveDate(Date priceEffectiveDate) {
        this.priceEffectiveDate = priceEffectiveDate;
    }
    public Date getPriceExpirationDate() {
        return priceExpirationDate;
    }
    public void setPriceExpirationDate(Date priceExpirationDate) {
        this.priceExpirationDate = priceExpirationDate;
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
