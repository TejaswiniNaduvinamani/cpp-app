package com.gfs.cpp.common.dto.distributioncenter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dcdo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dcdo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="abbreviationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billToSiteAddress1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billToSiteAddress2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billToSiteAddress3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billToSiteCityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billToSiteCountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billToSiteCountyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billToSiteName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billToSitePostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billToSiteStateAlphaCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="companyNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="dcNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="freezerContactInfo" type="{http://my.gfs.com/200810/IDistributionCenterQuery}contactInfoDO" minOccurs="0"/&gt;
 *         &lt;element name="groceryContactInfo" type="{http://my.gfs.com/200810/IDistributionCenterQuery}contactInfoDO" minOccurs="0"/&gt;
 *         &lt;element name="inventoryControlContactInfo" type="{http://my.gfs.com/200810/IDistributionCenterQuery}contactInfoDO" minOccurs="0"/&gt;
 *         &lt;element name="languageTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="measurementSystemCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="offeringGroupId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="outsideStorage" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="palletTracking" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="primarySiteAddress1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="primarySiteAddress2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="primarySiteAddress3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="primarySiteCityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="primarySiteCountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="primarySiteCountyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="primarySiteId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="primarySitePostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="primarySiteStateAlphaCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="purchaseCrossdock" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="reservingDCNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="reservingShipDCNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="shortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="slowMoverRepack" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="specialOrderPick" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="specialOrderReceive" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="statusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="timeZoneId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="timeZoneName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="transferVendorId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="transferVendorLocationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="wmsDBInstanceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="wmsMessageId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dcdo", propOrder = {
    "abbreviationCode",
    "billToSiteAddress1",
    "billToSiteAddress2",
    "billToSiteAddress3",
    "billToSiteCityName",
    "billToSiteCountryCode",
    "billToSiteCountyCode",
    "billToSiteName",
    "billToSitePostalCode",
    "billToSiteStateAlphaCode",
    "companyNumber",
    "dcNumber",
    "freezerContactInfo",
    "groceryContactInfo",
    "inventoryControlContactInfo",
    "languageTypeCode",
    "measurementSystemCode",
    "name",
    "offeringGroupId",
    "outsideStorage",
    "palletTracking",
    "primarySiteAddress1",
    "primarySiteAddress2",
    "primarySiteAddress3",
    "primarySiteCityName",
    "primarySiteCountryCode",
    "primarySiteCountyCode",
    "primarySiteId",
    "primarySitePostalCode",
    "primarySiteStateAlphaCode",
    "purchaseCrossdock",
    "reservingDCNumber",
    "reservingShipDCNumber",
    "shortName",
    "slowMoverRepack",
    "specialOrderPick",
    "specialOrderReceive",
    "statusCode",
    "timeZoneId",
    "timeZoneName",
    "transferVendorId",
    "transferVendorLocationCode",
    "wmsDBInstanceId",
    "wmsMessageId"
})
public class DistributionCenterSOAPResponseDTO {

    protected String abbreviationCode;
    protected String billToSiteAddress1;
    protected String billToSiteAddress2;
    protected String billToSiteAddress3;
    protected String billToSiteCityName;
    protected String billToSiteCountryCode;
    protected String billToSiteCountyCode;
    protected String billToSiteName;
    protected String billToSitePostalCode;
    protected String billToSiteStateAlphaCode;
    protected Integer companyNumber;
    protected Integer dcNumber;
    protected ContactInfoDTO freezerContactInfo;
    protected ContactInfoDTO groceryContactInfo;
    protected ContactInfoDTO inventoryControlContactInfo;
    protected String languageTypeCode;
    protected String measurementSystemCode;
    protected String name;
    protected String offeringGroupId;
    protected boolean outsideStorage;
    protected boolean palletTracking;
    protected String primarySiteAddress1;
    protected String primarySiteAddress2;
    protected String primarySiteAddress3;
    protected String primarySiteCityName;
    protected String primarySiteCountryCode;
    protected String primarySiteCountyCode;
    protected Integer primarySiteId;
    protected String primarySitePostalCode;
    protected String primarySiteStateAlphaCode;
    protected boolean purchaseCrossdock;
    protected Integer reservingDCNumber;
    protected Integer reservingShipDCNumber;
    protected String shortName;
    protected boolean slowMoverRepack;
    protected boolean specialOrderPick;
    protected boolean specialOrderReceive;
    protected String statusCode;
    protected String timeZoneId;
    protected String timeZoneName;
    protected String transferVendorId;
    protected String transferVendorLocationCode;
    protected String wmsDBInstanceId;
    protected String wmsMessageId;

    /**
     * Gets the value of the abbreviationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbbreviationCode() {
        return abbreviationCode;
    }

    /**
     * Sets the value of the abbreviationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbbreviationCode(String value) {
        this.abbreviationCode = value;
    }

    /**
     * Gets the value of the billToSiteAddress1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillToSiteAddress1() {
        return billToSiteAddress1;
    }

    /**
     * Sets the value of the billToSiteAddress1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillToSiteAddress1(String value) {
        this.billToSiteAddress1 = value;
    }

    /**
     * Gets the value of the billToSiteAddress2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillToSiteAddress2() {
        return billToSiteAddress2;
    }

    /**
     * Sets the value of the billToSiteAddress2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillToSiteAddress2(String value) {
        this.billToSiteAddress2 = value;
    }

    /**
     * Gets the value of the billToSiteAddress3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillToSiteAddress3() {
        return billToSiteAddress3;
    }

    /**
     * Sets the value of the billToSiteAddress3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillToSiteAddress3(String value) {
        this.billToSiteAddress3 = value;
    }

    /**
     * Gets the value of the billToSiteCityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillToSiteCityName() {
        return billToSiteCityName;
    }

    /**
     * Sets the value of the billToSiteCityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillToSiteCityName(String value) {
        this.billToSiteCityName = value;
    }

    /**
     * Gets the value of the billToSiteCountryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillToSiteCountryCode() {
        return billToSiteCountryCode;
    }

    /**
     * Sets the value of the billToSiteCountryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillToSiteCountryCode(String value) {
        this.billToSiteCountryCode = value;
    }

    /**
     * Gets the value of the billToSiteCountyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillToSiteCountyCode() {
        return billToSiteCountyCode;
    }

    /**
     * Sets the value of the billToSiteCountyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillToSiteCountyCode(String value) {
        this.billToSiteCountyCode = value;
    }

    /**
     * Gets the value of the billToSiteName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillToSiteName() {
        return billToSiteName;
    }

    /**
     * Sets the value of the billToSiteName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillToSiteName(String value) {
        this.billToSiteName = value;
    }

    /**
     * Gets the value of the billToSitePostalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillToSitePostalCode() {
        return billToSitePostalCode;
    }

    /**
     * Sets the value of the billToSitePostalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillToSitePostalCode(String value) {
        this.billToSitePostalCode = value;
    }

    /**
     * Gets the value of the billToSiteStateAlphaCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillToSiteStateAlphaCode() {
        return billToSiteStateAlphaCode;
    }

    /**
     * Sets the value of the billToSiteStateAlphaCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillToSiteStateAlphaCode(String value) {
        this.billToSiteStateAlphaCode = value;
    }

    /**
     * Gets the value of the companyNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCompanyNumber() {
        return companyNumber;
    }

    /**
     * Sets the value of the companyNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCompanyNumber(Integer value) {
        this.companyNumber = value;
    }

    /**
     * Gets the value of the dcNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDcNumber() {
        return dcNumber;
    }

    /**
     * Sets the value of the dcNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDcNumber(Integer value) {
        this.dcNumber = value;
    }

    /**
     * Gets the value of the freezerContactInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ContactInfoDTO }
     *     
     */
    public ContactInfoDTO getFreezerContactInfo() {
        return freezerContactInfo;
    }

    /**
     * Sets the value of the freezerContactInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactInfoDTO }
     *     
     */
    public void setFreezerContactInfo(ContactInfoDTO value) {
        this.freezerContactInfo = value;
    }

    /**
     * Gets the value of the groceryContactInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ContactInfoDTO }
     *     
     */
    public ContactInfoDTO getGroceryContactInfo() {
        return groceryContactInfo;
    }

    /**
     * Sets the value of the groceryContactInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactInfoDTO }
     *     
     */
    public void setGroceryContactInfo(ContactInfoDTO value) {
        this.groceryContactInfo = value;
    }

    /**
     * Gets the value of the inventoryControlContactInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ContactInfoDTO }
     *     
     */
    public ContactInfoDTO getInventoryControlContactInfo() {
        return inventoryControlContactInfo;
    }

    /**
     * Sets the value of the inventoryControlContactInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactInfoDTO }
     *     
     */
    public void setInventoryControlContactInfo(ContactInfoDTO value) {
        this.inventoryControlContactInfo = value;
    }

    /**
     * Gets the value of the languageTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguageTypeCode() {
        return languageTypeCode;
    }

    /**
     * Sets the value of the languageTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguageTypeCode(String value) {
        this.languageTypeCode = value;
    }

    /**
     * Gets the value of the measurementSystemCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeasurementSystemCode() {
        return measurementSystemCode;
    }

    /**
     * Sets the value of the measurementSystemCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeasurementSystemCode(String value) {
        this.measurementSystemCode = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the offeringGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfferingGroupId() {
        return offeringGroupId;
    }

    /**
     * Sets the value of the offeringGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfferingGroupId(String value) {
        this.offeringGroupId = value;
    }

    /**
     * Gets the value of the outsideStorage property.
     * 
     */
    public boolean isOutsideStorage() {
        return outsideStorage;
    }

    /**
     * Sets the value of the outsideStorage property.
     * 
     */
    public void setOutsideStorage(boolean value) {
        this.outsideStorage = value;
    }

    /**
     * Gets the value of the palletTracking property.
     * 
     */
    public boolean isPalletTracking() {
        return palletTracking;
    }

    /**
     * Sets the value of the palletTracking property.
     * 
     */
    public void setPalletTracking(boolean value) {
        this.palletTracking = value;
    }

    /**
     * Gets the value of the primarySiteAddress1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimarySiteAddress1() {
        return primarySiteAddress1;
    }

    /**
     * Sets the value of the primarySiteAddress1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimarySiteAddress1(String value) {
        this.primarySiteAddress1 = value;
    }

    /**
     * Gets the value of the primarySiteAddress2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimarySiteAddress2() {
        return primarySiteAddress2;
    }

    /**
     * Sets the value of the primarySiteAddress2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimarySiteAddress2(String value) {
        this.primarySiteAddress2 = value;
    }

    /**
     * Gets the value of the primarySiteAddress3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimarySiteAddress3() {
        return primarySiteAddress3;
    }

    /**
     * Sets the value of the primarySiteAddress3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimarySiteAddress3(String value) {
        this.primarySiteAddress3 = value;
    }

    /**
     * Gets the value of the primarySiteCityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimarySiteCityName() {
        return primarySiteCityName;
    }

    /**
     * Sets the value of the primarySiteCityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimarySiteCityName(String value) {
        this.primarySiteCityName = value;
    }

    /**
     * Gets the value of the primarySiteCountryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimarySiteCountryCode() {
        return primarySiteCountryCode;
    }

    /**
     * Sets the value of the primarySiteCountryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimarySiteCountryCode(String value) {
        this.primarySiteCountryCode = value;
    }

    /**
     * Gets the value of the primarySiteCountyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimarySiteCountyCode() {
        return primarySiteCountyCode;
    }

    /**
     * Sets the value of the primarySiteCountyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimarySiteCountyCode(String value) {
        this.primarySiteCountyCode = value;
    }

    /**
     * Gets the value of the primarySiteId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPrimarySiteId() {
        return primarySiteId;
    }

    /**
     * Sets the value of the primarySiteId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPrimarySiteId(Integer value) {
        this.primarySiteId = value;
    }

    /**
     * Gets the value of the primarySitePostalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimarySitePostalCode() {
        return primarySitePostalCode;
    }

    /**
     * Sets the value of the primarySitePostalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimarySitePostalCode(String value) {
        this.primarySitePostalCode = value;
    }

    /**
     * Gets the value of the primarySiteStateAlphaCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimarySiteStateAlphaCode() {
        return primarySiteStateAlphaCode;
    }

    /**
     * Sets the value of the primarySiteStateAlphaCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimarySiteStateAlphaCode(String value) {
        this.primarySiteStateAlphaCode = value;
    }

    /**
     * Gets the value of the purchaseCrossdock property.
     * 
     */
    public boolean isPurchaseCrossdock() {
        return purchaseCrossdock;
    }

    /**
     * Sets the value of the purchaseCrossdock property.
     * 
     */
    public void setPurchaseCrossdock(boolean value) {
        this.purchaseCrossdock = value;
    }

    /**
     * Gets the value of the reservingDCNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReservingDCNumber() {
        return reservingDCNumber;
    }

    /**
     * Sets the value of the reservingDCNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReservingDCNumber(Integer value) {
        this.reservingDCNumber = value;
    }

    /**
     * Gets the value of the reservingShipDCNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReservingShipDCNumber() {
        return reservingShipDCNumber;
    }

    /**
     * Sets the value of the reservingShipDCNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReservingShipDCNumber(Integer value) {
        this.reservingShipDCNumber = value;
    }

    /**
     * Gets the value of the shortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets the value of the shortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortName(String value) {
        this.shortName = value;
    }

    /**
     * Gets the value of the slowMoverRepack property.
     * 
     */
    public boolean isSlowMoverRepack() {
        return slowMoverRepack;
    }

    /**
     * Sets the value of the slowMoverRepack property.
     * 
     */
    public void setSlowMoverRepack(boolean value) {
        this.slowMoverRepack = value;
    }

    /**
     * Gets the value of the specialOrderPick property.
     * 
     */
    public boolean isSpecialOrderPick() {
        return specialOrderPick;
    }

    /**
     * Sets the value of the specialOrderPick property.
     * 
     */
    public void setSpecialOrderPick(boolean value) {
        this.specialOrderPick = value;
    }

    /**
     * Gets the value of the specialOrderReceive property.
     * 
     */
    public boolean isSpecialOrderReceive() {
        return specialOrderReceive;
    }

    /**
     * Sets the value of the specialOrderReceive property.
     * 
     */
    public void setSpecialOrderReceive(boolean value) {
        this.specialOrderReceive = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusCode(String value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the timeZoneId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeZoneId() {
        return timeZoneId;
    }

    /**
     * Sets the value of the timeZoneId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeZoneId(String value) {
        this.timeZoneId = value;
    }

    /**
     * Gets the value of the timeZoneName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeZoneName() {
        return timeZoneName;
    }

    /**
     * Sets the value of the timeZoneName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeZoneName(String value) {
        this.timeZoneName = value;
    }

    /**
     * Gets the value of the transferVendorId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferVendorId() {
        return transferVendorId;
    }

    /**
     * Sets the value of the transferVendorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferVendorId(String value) {
        this.transferVendorId = value;
    }

    /**
     * Gets the value of the transferVendorLocationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferVendorLocationCode() {
        return transferVendorLocationCode;
    }

    /**
     * Sets the value of the transferVendorLocationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferVendorLocationCode(String value) {
        this.transferVendorLocationCode = value;
    }

    /**
     * Gets the value of the wmsDBInstanceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWmsDBInstanceId() {
        return wmsDBInstanceId;
    }

    /**
     * Sets the value of the wmsDBInstanceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWmsDBInstanceId(String value) {
        this.wmsDBInstanceId = value;
    }

    /**
     * Gets the value of the wmsMessageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWmsMessageId() {
        return wmsMessageId;
    }

    /**
     * Sets the value of the wmsMessageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWmsMessageId(String value) {
        this.wmsMessageId = value;
    }

}
