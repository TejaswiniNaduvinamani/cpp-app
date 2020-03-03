package com.gfs.cpp.common.dto.contractpricing;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CMGCustomerResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private int typeCode;
    private int defaultCustomerInd;
    private int cppCustomerSeq;

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

    public int getDefaultCustomerInd() {
        return defaultCustomerInd;
    }

    public void setDefaultCustomerInd(int defaultCustomerInd) {
        this.defaultCustomerInd = defaultCustomerInd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getCppCustomerSeq() {
        return cppCustomerSeq;
    }

    public void setCppCustomerSeq(int cppCustomerSeq) {
        this.cppCustomerSeq = cppCustomerSeq;
    }
}
