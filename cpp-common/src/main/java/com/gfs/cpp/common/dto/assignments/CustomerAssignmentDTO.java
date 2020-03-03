package com.gfs.cpp.common.dto.assignments;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gfs.cpp.common.dto.clm.JsonDateDeserializer;
import com.gfs.cpp.common.dto.clm.JsonDateSerializer;

public class CustomerAssignmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int contractPriceProfileSeq;
    private int contractPriceProfileId;
    private String cmgCustomerId;
    private int cmgCustomerType;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date clmContractStartDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date clmContractEndDate;
    private List<RealCustomerDTO> realCustomerDTOList;

    public int getContractPriceProfileId() {
        return contractPriceProfileId;
    }

    public void setContractPriceProfileId(int contractPriceProfileId) {
        this.contractPriceProfileId = contractPriceProfileId;
    }

    public Date getClmContractStartDate() {
        return clmContractStartDate;
    }

    public void setClmContractStartDate(Date pricingEffectiveDate) {
        this.clmContractStartDate = pricingEffectiveDate;
    }

    public Date getClmContractEndDate() {
        return clmContractEndDate;
    }

    public void setClmContractEndDate(Date pricingExpirationDate) {
        this.clmContractEndDate = pricingExpirationDate;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public String getCmgCustomerId() {
        return cmgCustomerId;
    }

    public void setCmgCustomerId(String cmgCustomerId) {
        this.cmgCustomerId = cmgCustomerId;
    }

    public int getCmgCustomerType() {
        return cmgCustomerType;
    }

    public void setCmgCustomerType(int cmgCustomerType) {
        this.cmgCustomerType = cmgCustomerType;
    }

    public List<RealCustomerDTO> getRealCustomerDTOList() {
        return realCustomerDTOList;
    }

    public void setRealCustomerDTOList(List<RealCustomerDTO> realCustomerDTOList) {
        this.realCustomerDTOList = realCustomerDTOList;
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
