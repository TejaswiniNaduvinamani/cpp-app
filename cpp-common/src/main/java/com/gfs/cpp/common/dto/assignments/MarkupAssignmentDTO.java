package com.gfs.cpp.common.dto.assignments;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;

public class MarkupAssignmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String gfsCustomerId;
    private String markupName;
    private String gfsCustomerType;
    private int gfsCustomerTypeId;
    private List<ProductTypeMarkupDTO> markupList;
    private List<ItemLevelMarkupDTO> itemList;
    private List<SubgroupMarkupDTO> subgroupList;
    private boolean isDefault;
    private boolean isAssignmentSaved;
    private List<RealCustomerDTO> realCustomerDTOList;
    private Integer expireLowerInd;

    public String getGfsCustomerId() {
        return gfsCustomerId;
    }

    public void setGfsCustomerId(String gfsCustomerId) {
        this.gfsCustomerId = gfsCustomerId;
    }

    public String getMarkupName() {
        return markupName;
    }

    public void setMarkupName(String markupName) {
        this.markupName = markupName;
    }

    public List<ProductTypeMarkupDTO> getMarkupList() {
        return markupList;
    }

    public void setMarkupList(List<ProductTypeMarkupDTO> markup) {
        this.markupList = markup;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsAssignmentSaved() {
        return isAssignmentSaved;
    }

    public void setIsAssignmentSaved(boolean isAssignmentSaved) {
        this.isAssignmentSaved = isAssignmentSaved;
    }

    public String getGfsCustomerType() {
        return gfsCustomerType;
    }

    public void setGfsCustomerType(String gfsCustomerType) {
        this.gfsCustomerType = gfsCustomerType;
    }

    public List<RealCustomerDTO> getRealCustomerDTOList() {
        return realCustomerDTOList;
    }

    public void setRealCustomerDTOList(List<RealCustomerDTO> savedAssignmentList) {
        this.realCustomerDTOList = savedAssignmentList;
    }

    public int getGfsCustomerTypeId() {
        return gfsCustomerTypeId;
    }

    public void setGfsCustomerTypeId(int gfsCustomerTypeId) {
        this.gfsCustomerTypeId = gfsCustomerTypeId;
    }

    public List<ItemLevelMarkupDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemLevelMarkupDTO> itemList) {
        this.itemList = itemList;
    }

    public Integer getExpireLowerInd() {
        return expireLowerInd;
    }

    public void setExpireLowerInd(Integer expireLowerInd) {
        this.expireLowerInd = expireLowerInd;
    }

	public List<SubgroupMarkupDTO> getSubgroupList() {
		return subgroupList;
	}

	public void setSubgroupList(List<SubgroupMarkupDTO> subgroupMarkups) {
		this.subgroupList = subgroupMarkups;
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
