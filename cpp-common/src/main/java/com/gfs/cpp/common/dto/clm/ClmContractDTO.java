package com.gfs.cpp.common.dto.clm;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;

public class ClmContractDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date contractEndDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date contractStartDate;

    private String contractStatus;
    private String agreementId;
    private String contractType;
    private String contractName;
    private String parentAgreementId;
    private boolean isAmendment;
    private CPPInformationDTO cppInformationDto;
    private boolean isAutoRenewalEnabled;

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getParentAgreementId() {
        return parentAgreementId;
    }

    public void setParentAgreementId(String parentAgreementId) {
        this.parentAgreementId = parentAgreementId;
    }

    public boolean getIsAmendment() {
        return isAmendment;
    }

    public void setIsAmendment(boolean isAmendment) {
        this.isAmendment = isAmendment;
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

    public CPPInformationDTO getCppInformationDto() {
        return cppInformationDto;
    }

    public void setCppInformationDto(CPPInformationDTO cppInformationDto) {
        this.cppInformationDto = cppInformationDto;
    }

    public boolean getIsAutoRenewalEnabled() {
        return isAutoRenewalEnabled;
    }

    public void setAutoRenewalEnabled(boolean isAutoRenewalEnabled) {
        this.isAutoRenewalEnabled = isAutoRenewalEnabled;
    }

}
