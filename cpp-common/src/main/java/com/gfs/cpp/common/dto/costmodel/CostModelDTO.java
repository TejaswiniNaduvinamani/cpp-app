
package com.gfs.cpp.common.dto.costmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Java class for costModelDTO complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="costModelDTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="modelId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileSelectionCriteria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "costModelDTO", propOrder = { "modelId", "name", "profileSelectionCriteria" })
public class CostModelDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("costModelId")
    protected Integer modelId;

    @JsonProperty("costModelTypeValue")
    protected String name;

    protected String profileSelectionCriteria;

    /**
     * Gets the value of the modelId property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getModelId() {
        return modelId;
    }

    /**
     * Sets the value of the modelId property.
     * 
     * @param value allowed object is {@link Integer }
     * 
     */
    public void setModelId(Integer value) {
        this.modelId = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the profileSelectionCriteria property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProfileSelectionCriteria() {
        return profileSelectionCriteria;
    }

    /**
     * Sets the value of the profileSelectionCriteria property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setProfileSelectionCriteria(String value) {
        this.profileSelectionCriteria = value;
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
