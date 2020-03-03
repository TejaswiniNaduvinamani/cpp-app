package com.gfs.cpp.common.dto.markup;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MarkupReviewDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String markupBasedOnSell;
    private String markupBasedOnSellContractLanguage1;
    private String markupTypeDefinitionSellUnitLanguage;
    private String markupTypeDefinitionPerCaseLanguage;
    private String markupTypeDefinitionPerWeightLanguage;
    private String markupBasedOnSellContractLanguage2;
    private List<MarkupGridDTO> markupGridDTOs;
    private boolean pricingOverrideInd;

    public String getMarkupBasedOnSell() {
        return markupBasedOnSell;
    }

    public void setMarkupBasedOnSell(String markupBasedOnSell) {
        this.markupBasedOnSell = markupBasedOnSell;
    }

    public String getMarkupBasedOnSellContractLanguage1() {
        return markupBasedOnSellContractLanguage1;
    }

    public void setMarkupBasedOnSellContractLanguage1(String markupBasedOnSellContractLanguage1) {
        this.markupBasedOnSellContractLanguage1 = markupBasedOnSellContractLanguage1;
    }

    public String getMarkupBasedOnSellContractLanguage2() {
        return markupBasedOnSellContractLanguage2;
    }

    public void setMarkupBasedOnSellContractLanguage2(String markupBasedOnSellContractLanguage2) {
        this.markupBasedOnSellContractLanguage2 = markupBasedOnSellContractLanguage2;
    }

    public List<MarkupGridDTO> getMarkupGridDTOs() {
        return markupGridDTOs;
    }

    public void setMarkupGridDTOs(List<MarkupGridDTO> markupGridDTOs) {
        this.markupGridDTOs = markupGridDTOs;
    }

    public boolean isPricingOverrideInd() {
        return pricingOverrideInd;
    }

    public void setPricingOverrideInd(boolean pricingOverrideInd) {
        this.pricingOverrideInd = pricingOverrideInd;
    }

    public String getMarkupTypeDefinitionSellUnitLanguage() {
        return markupTypeDefinitionSellUnitLanguage;
    }

    public void setMarkupTypeDefinitionSellUnitLanguage(String markupTypeDefinitionSellUnitLanguage) {
        this.markupTypeDefinitionSellUnitLanguage = markupTypeDefinitionSellUnitLanguage;
    }

    public String getMarkupTypeDefinitionPerCaseLanguage() {
        return markupTypeDefinitionPerCaseLanguage;
    }

    public void setMarkupTypeDefinitionPerCaseLanguage(String markupTypeDefinitionPerCaseLanguage) {
        this.markupTypeDefinitionPerCaseLanguage = markupTypeDefinitionPerCaseLanguage;
    }

    public String getMarkupTypeDefinitionPerWeightLanguage() {
        return markupTypeDefinitionPerWeightLanguage;
    }

    public void setMarkupTypeDefinitionPerWeightLanguage(String markupTypeDefinitionPerWeightLanguage) {
        this.markupTypeDefinitionPerWeightLanguage = markupTypeDefinitionPerWeightLanguage;
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
