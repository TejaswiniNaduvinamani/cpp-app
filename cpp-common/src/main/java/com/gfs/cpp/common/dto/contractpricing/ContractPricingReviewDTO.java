package com.gfs.cpp.common.dto.contractpricing;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ContractPricingReviewDTO implements Serializable{

    private static final long serialVersionUID = 1L;
    private String scheduleForCost;
    private String scheduleForCostContractLanguage;
    private String formalPriceAudit;
    private String formalPriceAuditContractLanguage;
    private String priceVerification;
    private String priceVerificationContractLanguage;
    private String gfsTransferFee;
    private String gfsLabelAssesmentFee;
    private String costOfProductsContractLanguage;
    /**
     * @return the scheduleForCost
     */
    public String getScheduleForCost() {
        return scheduleForCost;
    }
    /**
     * @param scheduleForCost the scheduleForCost to set
     */
    public void setScheduleForCost(String scheduleForCost) {
        this.scheduleForCost = scheduleForCost;
    }
    /**
     * @return the scheduleForCostContractLanguage
     */
    public String getScheduleForCostContractLanguage() {
        return scheduleForCostContractLanguage;
    }
    /**
     * @param scheduleForCostContractLanguage the scheduleForCostContractLanguage to set
     */
    public void setScheduleForCostContractLanguage(String scheduleForCostContractLanguage) {
        this.scheduleForCostContractLanguage = scheduleForCostContractLanguage;
    }
    /**
     * @return the formalPriceAudit
     */
    public String getFormalPriceAudit() {
        return formalPriceAudit;
    }
    /**
     * @param formalPriceAudit the formalPriceAudit to set
     */
    public void setFormalPriceAudit(String formalPriceAudit) {
        this.formalPriceAudit = formalPriceAudit;
    }
    /**
     * @return the formalPriceAuditContractLanguage
     */
    public String getFormalPriceAuditContractLanguage() {
        return formalPriceAuditContractLanguage;
    }
    /**
     * @param formalPriceAuditContractLanguage the formalPriceAuditContractLanguage to set
     */
    public void setFormalPriceAuditContractLanguage(String formalPriceAuditContractLanguage) {
        this.formalPriceAuditContractLanguage = formalPriceAuditContractLanguage;
    }
    /**
     * @return the priceVerification
     */
    public String getPriceVerification() {
        return priceVerification;
    }
    /**
     * @param priceVerification the priceVerification to set
     */
    public void setPriceVerification(String priceVerification) {
        this.priceVerification = priceVerification;
    }
    /**
     * @return the priceVerificationContractLanguage
     */
    public String getPriceVerificationContractLanguage() {
        return priceVerificationContractLanguage;
    }
    /**
     * @param priceVerificationContractLanguage the priceVerificationContractLanguage to set
     */
    public void setPriceVerificationContractLanguage(String priceVerificationContractLanguage) {
        this.priceVerificationContractLanguage = priceVerificationContractLanguage;
    }
    /**
     * @return the gfsTransferFee
     */
    public String getGfsTransferFee() {
        return gfsTransferFee;
    }
    /**
     * @param gfsTransferFee the gfsTransferFee to set
     */
    public void setGfsTransferFee(String gfsTransferFee) {
        this.gfsTransferFee = gfsTransferFee;
    }
    /**
     * @return the gfsLabelAssesmentFee
     */
    public String getGfsLabelAssesmentFee() {
        return gfsLabelAssesmentFee;
    }
    /**
     * @param gfsLabelAssesmentFee the gfsLabelAssesmentFee to set
     */
    public void setGfsLabelAssesmentFee(String gfsLabelAssesmentFee) {
        this.gfsLabelAssesmentFee = gfsLabelAssesmentFee;
    }
    /**
     * @return the costOfProductsContractLanguage
     */
    public String getCostOfProductsContractLanguage() {
        return costOfProductsContractLanguage;
    }
    /**
     * @param costOfProductsContractLanguage the costOfProductsContractLanguage to set
     */
    public void setCostOfProductsContractLanguage(String costOfProductsContractLanguage) {
        this.costOfProductsContractLanguage = costOfProductsContractLanguage;
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
