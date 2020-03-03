package com.gfs.cpp.common.product.aquisition;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL) 
@JsonPropertyOrder({ "offeringTypeId", "offeringCategoryId", "offeringCategoryDescription",
		"offeringCategoryAbbreviation", "offeringCategoryDisplaySequence", "languageTypeCode" })
public class ProductTypeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonProperty("offeringTypeId")
	private Integer offeringTypeId;
	@JsonProperty("offeringCategoryId")
	private Integer offeringCategoryId;
	@JsonProperty("offeringCategoryDescription")
	private String offeringCategoryDescription;
	@JsonProperty("offeringCategoryAbbreviation")
	private String offeringCategoryAbbreviation;
	@JsonProperty("offeringCategoryDisplaySequence")
	private Integer offeringCategoryDisplaySequence;
	@JsonProperty("languageTypeCode")
	private String languageTypeCode;

	@JsonProperty("offeringTypeId")
	public Integer getOfferingTypeId() {
		return offeringTypeId;
	}

	@JsonProperty("offeringTypeId")
	public void setOfferingTypeId(Integer offeringTypeId) {
		this.offeringTypeId = offeringTypeId;
	}

	@JsonProperty("offeringCategoryId")
	public Integer getOfferingCategoryId() {
		return offeringCategoryId;
	}

	@JsonProperty("offeringCategoryId")
	public void setOfferingCategoryId(Integer offeringCategoryId) {
		this.offeringCategoryId = offeringCategoryId;
	}

	@JsonProperty("offeringCategoryDescription")
	public String getOfferingCategoryDescription() {
		return offeringCategoryDescription;
	}

	@JsonProperty("offeringCategoryDescription")
	public void setOfferingCategoryDescription(String offeringCategoryDescription) {
		this.offeringCategoryDescription = offeringCategoryDescription;
	}

	@JsonProperty("offeringCategoryAbbreviation")
	public String getOfferingCategoryAbbreviation() {
		return offeringCategoryAbbreviation;
	}

	@JsonProperty("offeringCategoryAbbreviation")
	public void setOfferingCategoryAbbreviation(String offeringCategoryAbbreviation) {
		this.offeringCategoryAbbreviation = offeringCategoryAbbreviation;
	}

	@JsonProperty("offeringCategoryDisplaySequence")
	public Integer getOfferingCategoryDisplaySequence() {
		return offeringCategoryDisplaySequence;
	}

	@JsonProperty("offeringCategoryDisplaySequence")
	public void setOfferingCategoryDisplaySequence(Integer offeringCategoryDisplaySequence) {
		this.offeringCategoryDisplaySequence = offeringCategoryDisplaySequence;
	}

	@JsonProperty("languageTypeCode")
	public String getLanguageTypeCode() {
		return languageTypeCode;
	}

	@JsonProperty("languageTypeCode")
	public void setLanguageTypeCode(String languageTypeCode) {
		this.languageTypeCode = languageTypeCode;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
	}

	@Override
	public boolean equals(Object other) { 
		return EqualsBuilder.reflectionEquals(this, other);}

}
