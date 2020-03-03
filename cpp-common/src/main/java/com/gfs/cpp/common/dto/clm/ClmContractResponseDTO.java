package com.gfs.cpp.common.dto.clm;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClmContractResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("ICMEffectiveDate")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date contractEffectiveDate;
    @JsonProperty("ICMExpiryDate")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date contractExpirationDate;
    @JsonProperty("ICMAmendmentEffectiveDate")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date amendmentEffectiveDate;
    @JsonProperty("Status")
    private String contractStatus;
    @JsonProperty("EntityInstanceId")
    private String contractAgreementId;
    @JsonProperty("EntityName")
    private String contractTypeName;
    @JsonProperty("ICMContractTypeName")
    private String parentContractTypeName;
    @JsonProperty("Name")
    private String contractName;
    @JsonProperty("ParentEntityInstanceID")
    private String parentAgreementId;
    @JsonProperty("ICMIsAmendment")
    private boolean amendment;

    public void setAmendment(boolean amendment) {
        this.amendment = amendment;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getContractAgreementId() {
        return contractAgreementId;
    }

    public void setContractAgreementId(String contractAgreementId) {
        this.contractAgreementId = contractAgreementId;
    }

    public String getContractTypeName() {
        return contractTypeName;
    }

    public void setContractTypeName(String contractTypeName) {
        this.contractTypeName = contractTypeName;
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

    public boolean isAmendment() {
        return amendment;
    }

    public String getParentContractTypeName() {
        return parentContractTypeName;
    }

    public void setParentContractTypeName(String parentContractTypeName) {
        this.parentContractTypeName = parentContractTypeName;
    }

    public Date getContractEffectiveDate() {
        return contractEffectiveDate;
    }

    public void setContractEffectiveDate(Date contractEffectiveDate) {
        this.contractEffectiveDate = contractEffectiveDate;
    }

    public Date getContractExpirationDate() {
        return contractExpirationDate;
    }

    public void setContractExpirationDate(Date contractExpirationDate) {
        this.contractExpirationDate = contractExpirationDate;
    }

    public Date getAmendmentEffectiveDate() {
        return amendmentEffectiveDate;
    }

    public void setAmendmentEffectiveDate(Date amendmentEffectiveDate) {
        this.amendmentEffectiveDate = amendmentEffectiveDate;
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
