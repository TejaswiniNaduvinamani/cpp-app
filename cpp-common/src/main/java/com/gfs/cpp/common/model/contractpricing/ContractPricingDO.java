package com.gfs.cpp.common.model.contractpricing;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ContractPricingDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int contractPriceProfileId;
    private int contractPriceProfileSeq;
    private Date pricingEffectiveDate;
    private Date pricingExpirationDate;
    private boolean priceVerifyPrivileges;
    private boolean priceAuditPrivileges;
    private boolean costModelTransferFee;
    private boolean costModelGFSAssesFee;
    private String contractTypeCode;
    private String scheduleForCostChange;
    private int priceVerifInd;
    private int priceAuditInd;
    private int transferFeeInd;
    private int labelAssesmentInd;
    private int customerTypeCode;
    private String gfsCustomerId;
    private String contractName;
    private Date effectiveDateFuture;
    private Date expirationDateFuture;
    private String agreementId;
    private Integer expireLowerLevelInd;
    private Date clmContractStartDate;
    private Date clmContractEndDate;
    private String parentAgreementId;
    private int versionNbr;
  
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

    public boolean getPriceVerifyPrivileges() {
        return priceVerifyPrivileges;
    }

    public void setPriceVerifyPrivileges(boolean priceVerifyPrivileges) {
        this.priceVerifyPrivileges = priceVerifyPrivileges;
    }

    public boolean getPriceAuditPrivileges() {
        return priceAuditPrivileges;
    }

    public void setPriceAuditPrivileges(boolean priceAuditPrivileges) {
        this.priceAuditPrivileges = priceAuditPrivileges;
    }

    public boolean getCostModelTransferFee() {
        return costModelTransferFee;
    }

    public void setCostModelTransferFee(boolean costModelTransferFee) {
        this.costModelTransferFee = costModelTransferFee;
    }

    public boolean getCostModelGFSAssesFee() {
        return costModelGFSAssesFee;
    }

    public void setCostModelGFSAssesFee(boolean costModelGFSAssesFee) {
        this.costModelGFSAssesFee = costModelGFSAssesFee;
    }

    public String getContractTypeCode() {
        return contractTypeCode;
    }

    public void setContractTypeCode(String contractTypeCode) {
        this.contractTypeCode = contractTypeCode;
    }

    public int getContractPriceProfileId() {
        return contractPriceProfileId;
    }

    public void setContractPriceProfileId(int contractPriceProfileId) {
        this.contractPriceProfileId = contractPriceProfileId;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public String getScheduleForCostChange() {
        return scheduleForCostChange;
    }

    public void setScheduleForCostChange(String scheduleForCostChange) {
        this.scheduleForCostChange = scheduleForCostChange;
    }

    public int getPriceVerifInd() {
        return priceVerifInd;
    }

    public void setPriceVerifInd(int priceVerifInd) {
        this.priceVerifInd = priceVerifInd;
    }

    public int getPriceAuditInd() {
        return priceAuditInd;
    }

    public void setPriceAuditInd(int priceAuditInd) {
        this.priceAuditInd = priceAuditInd;
    }

    public int getTransferFeeInd() {
        return transferFeeInd;
    }

    public void setTransferFeeInd(int transferFeeInd) {
        this.transferFeeInd = transferFeeInd;
    }

    public int getLabelAssesmentInd() {
        return labelAssesmentInd;
    }

    public void setLabelAssesmentInd(int labelAssesmentInd) {
        this.labelAssesmentInd = labelAssesmentInd;
    }

    public String getGfsCustomerId() {
        return gfsCustomerId;
    }

    public void setGfsCustomerId(String gfsCustomerId) {
        this.gfsCustomerId = gfsCustomerId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public int getCustomerTypeCode() {
        return customerTypeCode;
    }

    public void setCustomerTypeCode(int customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    public Date getEffectiveDateFuture() {
        return effectiveDateFuture;
    }

    public void setEffectiveDateFuture(Date effectiveDateFuture) {
        this.effectiveDateFuture = effectiveDateFuture;
    }

    public Date getExpirationDateFuture() {
        return expirationDateFuture;
    }

    public void setExpirationDateFuture(Date expirationDateFuture) {
        this.expirationDateFuture = expirationDateFuture;
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
