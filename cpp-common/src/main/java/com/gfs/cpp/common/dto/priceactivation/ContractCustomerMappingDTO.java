package com.gfs.cpp.common.dto.priceactivation;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ContractCustomerMappingDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int defaultCustomerInd;

    private int cppCustomerSeq;

    private int cppConceptMappingSeq;

    private String gfsCustomerId;

    private int gfsCustomerTypeCode;

    public int getCppCustomerSeq() {
        return cppCustomerSeq;
    }

    public void setCppCustomerSeq(int cppCustomerSeq) {
        this.cppCustomerSeq = cppCustomerSeq;
    }

    public int getCppConceptMappingSeq() {
        return cppConceptMappingSeq;
    }

    public void setCppConceptMappingSeq(int cppConceptMappingSeq) {
        this.cppConceptMappingSeq = cppConceptMappingSeq;
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

    public int getDefaultCustomerInd() {
        return defaultCustomerInd;
    }

    public void setDefaultCustomerInd(int defaultCustomerInd) {
        this.defaultCustomerInd = defaultCustomerInd;
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
