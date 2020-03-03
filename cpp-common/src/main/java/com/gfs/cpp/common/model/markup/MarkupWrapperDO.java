package com.gfs.cpp.common.model.markup;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MarkupWrapperDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ProductTypeMarkupDO> markupList;
    private int contractPriceProfileSeq;
    private String userName;
    private List<FutureItemDO> futureItemList;

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

    public List<ProductTypeMarkupDO> getMarkupList() {
        return markupList;
    }

    public void setMarkupList(List<ProductTypeMarkupDO> markupList) {
        this.markupList = markupList;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<FutureItemDO> getFutureItemList() {
        return futureItemList;
    }

    public void setFutureItemList(List<FutureItemDO> futureItemList) {
        this.futureItemList = futureItemList;
    }
}
