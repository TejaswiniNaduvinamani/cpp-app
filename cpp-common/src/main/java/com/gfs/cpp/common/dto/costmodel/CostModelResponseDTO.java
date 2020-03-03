package com.gfs.cpp.common.dto.costmodel;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CostModelResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int costModelId;

    private String costModelTypeValue;

    public int getCostModelId() {
        return costModelId;
    }

    public void setCostModelId(int costModelId) {
        this.costModelId = costModelId;
    }

    public String getCostModelTypeValue() {
        return costModelTypeValue;
    }

    public void setCostModelTypeValue(String costModelTypeValue) {
        this.costModelTypeValue = costModelTypeValue;
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
