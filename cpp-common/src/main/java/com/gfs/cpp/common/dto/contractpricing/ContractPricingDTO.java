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

public class ContractPricingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String contractType;
    private int contractPriceProfileId;
    private int contractPriceProfileSeq;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date pricingEffectiveDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date pricingExpirationDate;
    private String scheduleForCostChange;
    private boolean priceVerificationFlag;
    private boolean priceAuditFlag;
    private boolean transferFeeFlag;
    private boolean assessmentFeeFlag;
    private String contractName;
    private String agreementId;
    private Integer expireLowerLevelInd;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date clmContractStartDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date clmContractEndDate;
    private String parentAgreementId;
    private int versionNbr;
    private String pricingExhibitSysId;

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public int getContractPriceProfileId() {
        return contractPriceProfileId;
    }

    public void setContractPriceProfileId(int contractPriceProfileId) {
        this.contractPriceProfileId = contractPriceProfileId;
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

    public String getScheduleForCostChange() {
        return scheduleForCostChange;
    }

    public void setScheduleForCostChange(String scheduleForCostChange) {
        this.scheduleForCostChange = scheduleForCostChange;
    }

    public boolean getPriceVerificationFlag() {
        return priceVerificationFlag;
    }

    public void setPriceVerificationFlag(boolean priceVerificationFlag) {
        this.priceVerificationFlag = priceVerificationFlag;
    }

    public boolean getPriceAuditFlag() {
        return priceAuditFlag;
    }

    public void setPriceAuditFlag(boolean priceAuditFlag) {
        this.priceAuditFlag = priceAuditFlag;
    }

    public boolean getTransferFeeFlag() {
        return transferFeeFlag;
    }

    public void setTransferFeeFlag(boolean transferFeeFlag) {
        this.transferFeeFlag = transferFeeFlag;
    }

    public boolean getAssessmentFeeFlag() {
        return assessmentFeeFlag;
    }

    public void setAssessmentFeeFlag(boolean assessmentFeeFlag) {
        this.assessmentFeeFlag = assessmentFeeFlag;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public Integer getExpireLowerLevelInd() {
        return expireLowerLevelInd;
    }

    public void setExpireLowerLevelInd(Integer expireLowerLevelInd) {
        this.expireLowerLevelInd = expireLowerLevelInd;
    }

    public Date getClmContractStartDate() {
        return clmContractStartDate;
    }

    public void setClmContractStartDate(Date clmStartDate) {
        this.clmContractStartDate = clmStartDate;
    }

    public Date getClmContractEndDate() {
        return clmContractEndDate;
    }

    public void setClmContractEndDate(Date clmEndDate) {
        this.clmContractEndDate = clmEndDate;
    }

    public String getParentAgreementId() {
        return parentAgreementId;
    }

    public void setParentAgreementId(String parentAgreementId) {
        this.parentAgreementId = parentAgreementId;
    }

    public int getVersionNbr() {
        return versionNbr;
    }

    public void setVersionNbr(int versionNbr) {
        this.versionNbr = versionNbr;
    }

    public String getPricingExhibitSysId() {
        return pricingExhibitSysId;
    }

    public void setPricingExhibitSysId(String pricingExhibitSysId) {
        this.pricingExhibitSysId = pricingExhibitSysId;
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
