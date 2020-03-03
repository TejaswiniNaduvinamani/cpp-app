package com.gfs.cpp.common.dto.assignments;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ItemAssignmentValidationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean isItemAlreadyExist;
    private List<String> duplicateItemIdList;

    public boolean getIsItemAlreadyExist() {
        return isItemAlreadyExist;
    }

    public void setIsItemAlreadyExist(boolean isItemAlreadyExist) {
        this.isItemAlreadyExist = isItemAlreadyExist;
    }

    public List<String> getDuplicateItemIdList() {
        return duplicateItemIdList;
    }

    public void setDuplicateItemIdList(List<String> duplicateItemIdList) {
        this.duplicateItemIdList = duplicateItemIdList;
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
