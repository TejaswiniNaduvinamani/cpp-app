package com.gfs.cpp.common.dto.markup;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gfs.cpp.common.dto.clm.JsonDateDeserializer;
import com.gfs.cpp.common.dto.clm.JsonDateSerializer;

public class SubgroupMarkupDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String subgroupId;
    private String subgroupDesc;
    private String markup;
    private String unit;
    private int markupType;

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date effectiveDate;

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date expirationDate;
    private boolean isSubgroupSaved;
    private int customerItemDescSeq;

    public String getSubgroupId() {
        return subgroupId;
    }

    public void setSubgroupId(String subgroupId) {
        this.subgroupId = subgroupId;
    }

    public String getSubgroupDesc() {
        return subgroupDesc;
    }

    public void setSubgroupDesc(String subgroupDesc) {
        this.subgroupDesc = subgroupDesc;
    }

    public String getMarkup() {
        return markup;
    }

    public void setMarkup(String markup) {
        this.markup = markup;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getMarkupType() {
        return markupType;
    }

    public void setMarkupType(int markupType) {
        this.markupType = markupType;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean getIsSubgroupSaved() {
        return isSubgroupSaved;
    }

    public void setIsSubgroupSaved(boolean isSubgroupSaved) {
        this.isSubgroupSaved = isSubgroupSaved;
    }

    public int getCustomerItemDescSeq() {
        return customerItemDescSeq;
    }

    public void setCustomerItemDescSeq(int customerItemDescSeq) {
        this.customerItemDescSeq = customerItemDescSeq;
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
