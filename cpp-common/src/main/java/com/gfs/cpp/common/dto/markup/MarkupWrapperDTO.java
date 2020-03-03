package com.gfs.cpp.common.dto.markup;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MarkupWrapperDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String gfsCustomerId;
    private int gfsCustomerType;
    private int contractPriceProfileSeq;
    private String markupName;
    private boolean isMarkupSaved;
	private int cppFurtheranceSeq; 
    private List<ProductTypeMarkupDTO> productMarkupList;
    private List<ItemLevelMarkupDTO> itemLevelMarkupList;
    private List<SubgroupMarkupDTO> subgroupMarkupList;

    public String getMarkupName() {
        return markupName;
    }

    public void setMarkupName(String markupName) {
        this.markupName = markupName;
    }

    public List<ProductTypeMarkupDTO> getProductMarkupList() {
        return productMarkupList;
    }

    public void setProductMarkupList(List<ProductTypeMarkupDTO> productMarkupList) {
        this.productMarkupList = productMarkupList;
    }

    public List<ItemLevelMarkupDTO> getItemLevelMarkupList() {
        return itemLevelMarkupList;
    }

    public void setItemLevelMarkupList(List<ItemLevelMarkupDTO> itemLevelMarkupList) {
        this.itemLevelMarkupList = itemLevelMarkupList;
    }

    public String getGfsCustomerId() {
        return gfsCustomerId;
    }

    public void setGfsCustomerId(String gfsCustomerId) {
        this.gfsCustomerId = gfsCustomerId;
    }

    public boolean getIsMarkupSaved() {
        return isMarkupSaved;
    }

    public void setIsMarkupSaved(boolean isMarkupSaved) {
        this.isMarkupSaved = isMarkupSaved;
    }

    public int getGfsCustomerType() {
        return gfsCustomerType;
    }

    public void setGfsCustomerType(int gfsCustomerType) {
        this.gfsCustomerType = gfsCustomerType;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public List<SubgroupMarkupDTO> getSubgroupMarkupList() {
        return subgroupMarkupList;
    }

    public void setSubgroupMarkupList(List<SubgroupMarkupDTO> subgroupMarkupList) {
        this.subgroupMarkupList = subgroupMarkupList;
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
