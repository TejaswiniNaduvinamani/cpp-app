package com.gfs.cpp.common.dto.assignments;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ItemAssignmentWrapperDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int contractPriceProfileSeq;
    private int cppFurtheranceSeq;
    private String futureItemDesc;
    private String exceptionName;
    private boolean isFutureItemSaved;
    private String gfsCustomerId;
    private int gfsCustomerTypeCode;
    private List<ItemAssignmentDTO> itemAssignmentList;

    public String getFutureItemDesc() {
        return futureItemDesc;
    }

    public void setFutureItemDesc(String futureItemDesc) {
        this.futureItemDesc = futureItemDesc;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public boolean getIsFutureItemSaved() {
        return isFutureItemSaved;
    }

    public void setIsFutureItemSaved(boolean isFutureItemSaved) {
        this.isFutureItemSaved = isFutureItemSaved;
    }

    public List<ItemAssignmentDTO> getItemAssignmentList() {
        return itemAssignmentList;
    }

    public void setItemAssignmentList(List<ItemAssignmentDTO> itemAssignmentList) {
        this.itemAssignmentList = itemAssignmentList;
    }

    public String getGfsCustomerId() {
        return gfsCustomerId;
    }

    public void setGfsCustomerId(String gfsCustomerId) {
        this.gfsCustomerId = gfsCustomerId;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public int getGfsCustomerTypeCode() {
        return gfsCustomerTypeCode;
    }

    public void setGfsCustomerTypeCode(int gfsCustomerTypeCode) {
        this.gfsCustomerTypeCode = gfsCustomerTypeCode;
    }

    public int getCppFurtheranceSeq() {
        return cppFurtheranceSeq;
    }

    public void setCppFurtheranceSeq(int cppFurtheranceSeq) {
        this.cppFurtheranceSeq = cppFurtheranceSeq;
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
