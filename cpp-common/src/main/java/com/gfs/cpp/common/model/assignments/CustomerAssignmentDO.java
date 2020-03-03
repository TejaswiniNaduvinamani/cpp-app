package com.gfs.cpp.common.model.assignments;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CustomerAssignmentDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int cppCustomerSeq;
    private String gfsCustomerId;
    private int gfsCustomerType;

    public int getCppCustomerSeq() {
        return cppCustomerSeq;
    }

    public void setCppCustomerSeq(int cppCustomerSeq) {
        this.cppCustomerSeq = cppCustomerSeq;
    }

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
