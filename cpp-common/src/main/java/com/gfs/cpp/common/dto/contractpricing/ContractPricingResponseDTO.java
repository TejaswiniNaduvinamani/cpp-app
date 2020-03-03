package com.gfs.cpp.common.dto.contractpricing;

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

public class ContractPricingResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date pricingEffectiveDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date pricingExpirationDate;
    private int clmContractTypeSeq;
    private int contractPriceProfStatusCode;
    private int contractPriceProfileSeq;
    private String clmAgreementId;
    private String pricingExhibitSysId;
    private int contractPriceProfileId;
    private int versionNumber;
    private int expireLowerLevelInd;
    private int transferFeeFlag;
    private int assessmentFeeFlag;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date clmContractStartDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date clmContractEndDate;
    private String clmParentAgreementId;
    private String clmContractName;

    public int getTransferFeeFlag() {
        return transferFeeFlag;
    }

    public void setTransferFeeFlag(int transferFeeFlag) {
        this.transferFeeFlag = transferFeeFlag;
    }

    public int getAssessmentFeeFlag() {
        return assessmentFeeFlag;
    }

    public void setAssessmentFeeFlag(int assessmentFeeFlag) {
        this.assessmentFeeFlag = assessmentFeeFlag;
    }

    public int getClmContractTypeSeq() {
        return clmContractTypeSeq;
    }

    public void setClmContractTypeSeq(int clmContractTypeSeq) {
        this.clmContractTypeSeq = clmContractTypeSeq;
    }

    public Date getPricingEffectiveDate() {
        return pricingEffectiveDate;
    }

    public void setPricingEffectiveDate(Date pricingEffectiveDate) {
        this.pricingEffectiveDate = pricingEffectiveDate;
    }

    public Date getPricingExpirationDate() {
        return pricingExpirationDate;
    }

    public void setPricingExpirationDate(Date pricingExpirationDate) {
        this.pricingExpirationDate = pricingExpirationDate;
    }

    public int getContractPriceProfStatusCode() {
        return contractPriceProfStatusCode;
    }

    public void setContractPriceProfStatusCode(int contractPriceProfStatusCode) {
        this.contractPriceProfStatusCode = contractPriceProfStatusCode;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public String getClmAgreementId() {
        return clmAgreementId;
    }

    public void setClmAgreementId(String clmAgreementId) {
        this.clmAgreementId = clmAgreementId;
    }

    public String getPricingExhibitSysId() {
        return pricingExhibitSysId;
    }

    public void setPricingExhibitSysId(String pricingExhibitSysId) {
        this.pricingExhibitSysId = pricingExhibitSysId;
    }

    public int getContractPriceProfileId() {
        return contractPriceProfileId;
    }

    public void setContractPriceProfileId(int contractPriceProfileId) {
        this.contractPriceProfileId = contractPriceProfileId;
    }

    public int getExpireLowerLevelInd() {
        return expireLowerLevelInd;
    }

    public void setExpireLowerLevelInd(int expireLowerLevelInd) {
        this.expireLowerLevelInd = expireLowerLevelInd;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Date getClmContractStartDate() {
        return clmContractStartDate;
    }

    public void setClmContractStartDate(Date clmContractStartDate) {
        this.clmContractStartDate = clmContractStartDate;
    }

    public Date getClmContractEndDate() {
        return clmContractEndDate;
    }

    public void setClmContractEndDate(Date clmContractEndDate) {
        this.clmContractEndDate = clmContractEndDate;
    }

    public String getClmParentAgreementId() {
        return clmParentAgreementId;
    }

    public void setClmParentAgreementId(String parentAgreementId) {
        this.clmParentAgreementId = parentAgreementId;
    }

    public String getClmContractName() {
        return clmContractName;
    }

    public void setClmContractName(String clmContractName) {
        this.clmContractName = clmContractName;
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
