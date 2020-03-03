package com.gfs.cpp.common.dto.markup;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MarkupGridDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String markupName;
    private String gfsCustomerId;
    private List<ProductTypeMarkupDTO> productTypeMarkups;
    private List<ItemLevelMarkupDTO> itemMarkups;
    private List<SubgroupMarkupDTO> subgroupMarkups;

    /**
     * @return the markupName
     */
    public String getMarkupName() {
        return markupName;
    }

    /**
     * @param markupName the markupName to set
     */
    public void setMarkupName(String markupName) {
        this.markupName = markupName;
    }

    public List<ProductTypeMarkupDTO> getProductTypeMarkups() {
        return productTypeMarkups;
    }

    public void setProductTypeMarkups(List<ProductTypeMarkupDTO> productTypeMarkups) {
        this.productTypeMarkups = productTypeMarkups;
    }

    public List<ItemLevelMarkupDTO> getItemMarkups() {
        return itemMarkups;
    }

    public void setItemMarkups(List<ItemLevelMarkupDTO> itemMarkups) {
        this.itemMarkups = itemMarkups;
    }

    public List<SubgroupMarkupDTO> getSubgroupMarkups() {
        return subgroupMarkups;
    }

    public void setSubgroupMarkups(List<SubgroupMarkupDTO> subgroupMarkups) {
        this.subgroupMarkups = subgroupMarkups;
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

    public String getGfsCustomerId() {
        return gfsCustomerId;
    }

    public void setGfsCustomerId(String gfsCustomerId) {
        this.gfsCustomerId = gfsCustomerId;
    }

}
