package com.gfs.cpp.common.dto.furtherance;

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

public class FurtheranceInformationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int cppFurtheranceSeq;
    private int contractPriceProfileSeq;
    private int furtheranceStatusCode;
    private String parentCLMAgreementId;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date furtheranceEffectiveDate;
    private String changeReasonTxt;
    private String contractReferenceTxt;
    private String furtheranceDocumentGUID;

    public int getCppFurtheranceSeq() {
        return cppFurtheranceSeq;
    }

    public void setCppFurtheranceSeq(int cppFurtheranceSeq) {
        this.cppFurtheranceSeq = cppFurtheranceSeq;
    }

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public int getFurtheranceStatusCode() {
        return furtheranceStatusCode;
    }

    public void setFurtheranceStatusCode(int furtheranceStatusCode) {
        this.furtheranceStatusCode = furtheranceStatusCode;
    }

    public String getParentCLMAgreementId() {
        return parentCLMAgreementId;
    }

    public void setParentCLMAgreementId(String parentCLMAgreementId) {
        this.parentCLMAgreementId = parentCLMAgreementId;
    }

    public Date getFurtheranceEffectiveDate() {
        return furtheranceEffectiveDate;
    }

    public void setFurtheranceEffectiveDate(Date furtheranceEffectiveDate) {
        this.furtheranceEffectiveDate = furtheranceEffectiveDate;
    }

    public String getChangeReasonTxt() {
        return changeReasonTxt;
    }

    public void setChangeReasonTxt(String changeReasonTxt) {
        this.changeReasonTxt = changeReasonTxt;
    }

    public String getContractReferenceTxt() {
        return contractReferenceTxt;
    }

    public void setContractReferenceTxt(String contractReferenceTxt) {
        this.contractReferenceTxt = contractReferenceTxt;
    }

    public String getFurtheranceDocumentGUID() {
        return furtheranceDocumentGUID;
    }

    public void setFurtheranceDocumentGUID(String furtheranceDocumentGUID) {
        this.furtheranceDocumentGUID = furtheranceDocumentGUID;
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
