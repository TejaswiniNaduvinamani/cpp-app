package com.gfs.cpp.common.dto.assignments;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RealCustomerDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String realCustomerName;
    private String realCustomerId;
    private int realCustomerType;
    private String realCustomerGroupType;
    private boolean isCustomerSaved;

    public int getRealCustomerType() {
        return realCustomerType;
    }

    public void setRealCustomerType(int realCustomerType) {
        this.realCustomerType = realCustomerType;
    }

    public String getRealCustomerGroupType() {
        return realCustomerGroupType;
    }

    public void setRealCustomerGroupType(String realCustomerGroupType) {
        this.realCustomerGroupType = realCustomerGroupType;
    }

    public boolean getIsCustomerSaved() {
        return isCustomerSaved;
    }

    public void setIsCustomerSaved(boolean isCustomerSaved) {
        this.isCustomerSaved = isCustomerSaved;
    }

    public String getRealCustomerName() {
        return realCustomerName;
    }

    public void setRealCustomerName(String realCustomerName) {
        this.realCustomerName = realCustomerName;
    }

    public String getRealCustomerId() {
        return realCustomerId;
    }

    public void setRealCustomerId(String realCustomerId) {
        this.realCustomerId = realCustomerId;
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
