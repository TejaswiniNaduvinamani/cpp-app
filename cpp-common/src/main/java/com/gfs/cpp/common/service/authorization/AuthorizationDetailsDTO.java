package com.gfs.cpp.common.service.authorization;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthorizationDetailsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean priceProfileEditable;
    private boolean customerAssignmentEditable;
    private boolean itemAssignmentEditable;
    private boolean costModelEditable;
    private boolean isPowerUser;
    private String cppStatus;

    public boolean isPriceProfileEditable() {
        return priceProfileEditable;
    }

    public void setPriceProfileEditable(boolean priceProfileEditable) {
        this.priceProfileEditable = priceProfileEditable;
    }

    public boolean isCustomerAssignmentEditable() {
        return customerAssignmentEditable;
    }

    public void setCustomerAssignmentEditable(boolean assignmentEditable) {
        this.customerAssignmentEditable = assignmentEditable;
    }

    public boolean getIsPowerUser() {
        return isPowerUser;
    }

    public void setPowerUser(boolean isPowerUser) {
        this.isPowerUser = isPowerUser;
    }

    public String getCppStatus() {
        return cppStatus;
    }

    public void setCppStatus(String cppStatus) {
        this.cppStatus = cppStatus;
    }

    public boolean isItemAssignmentEditable() {
        return itemAssignmentEditable;
    }

    public void setItemAssignmentEditable(boolean itemAssignmentEditable) {
        this.itemAssignmentEditable = itemAssignmentEditable;
    }

    public boolean isCostModelEditable() {
        return costModelEditable;
    }

    public void setCostModelEditable(boolean costModelEditable) {
        this.costModelEditable = costModelEditable;
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
