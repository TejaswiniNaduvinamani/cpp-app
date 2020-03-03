package com.gfs.cpp.common.dto.markup;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PrcProfAuditAuthorityDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String gfsCustomerId;
    private int gfsCustomerType;
    private Date effectiveDate;
    private Date expirationDate;
    private String createUserId;
    private String lastUpdateUserId;
    private int prcProfAuditAuthorityInd;
    private int contractPriceProfileSeq;
    
    public String getGfsCustomerId() {
        return gfsCustomerId;
    }

    public void setGfsCustomerId(String gfsCustomerId) {
        this.gfsCustomerId = gfsCustomerId;
    }

    public int getGfsCustomerType() {
        return gfsCustomerType;
    }

    public void setGfsCustomerType(int gfsCustomerType) {
        this.gfsCustomerType = gfsCustomerType;
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

    public int getPrcProfAuditAuthorityInd() {
        return prcProfAuditAuthorityInd;
    }

    public void setPrcProfAuditAuthorityInd(int prcProfAuditAuthorityInd) {
        this.prcProfAuditAuthorityInd = prcProfAuditAuthorityInd;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
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
