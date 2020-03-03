package com.gfs.cpp.common.dto.distributioncenter;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.distributioncenter.ContactInfoDTO;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSOAPResponseDTO;

@RunWith(MockitoJUnitRunner.class)
public class DistributionCenterSOAPDTOTest {

    @InjectMocks
    private DistributionCenterSOAPResponseDTO dto;

    @Test
    public void testMethods() {
        dto.setAbbreviationCode("TEST");
        dto.setBillToSiteAddress1("TEST");
        dto.setBillToSiteAddress2("TEST");
        dto.setBillToSiteAddress3("TEST");
        dto.setBillToSiteCityName("TEST");
        dto.setBillToSiteCountryCode("TEST");
        dto.setBillToSiteCountyCode("TEST");
        dto.setBillToSiteName("TEST");
        dto.setBillToSitePostalCode("TEST");
        dto.setBillToSiteStateAlphaCode("TEST");
        dto.setCompanyNumber(3);
        dto.setDcNumber(2);
        dto.setFreezerContactInfo(new ContactInfoDTO());
        dto.setGroceryContactInfo(new ContactInfoDTO());
        dto.setInventoryControlContactInfo(new ContactInfoDTO());
        dto.setLanguageTypeCode("TEST");
        dto.setMeasurementSystemCode("TEST");
        dto.setName("TEST");
        dto.setOfferingGroupId("TEST");
        dto.setOutsideStorage(false);
        dto.setPalletTracking(false);
        dto.setPrimarySiteAddress1("TEST");
        dto.setPrimarySiteAddress2("TEST");
        dto.setPrimarySiteAddress3("TEST");
        dto.setPrimarySiteCityName("TEST");
        dto.setPrimarySiteCountryCode("TEST");
        dto.setPrimarySiteCountyCode("TEST");
        dto.setPrimarySiteId(1);
        dto.setPrimarySitePostalCode("TEST");
        dto.setPrimarySiteStateAlphaCode("TEST");
        dto.setPurchaseCrossdock(false);
        dto.setReservingDCNumber(1);
        dto.setReservingShipDCNumber(1);
        dto.setShortName("TEST");
        dto.setSlowMoverRepack(false);
        dto.setSpecialOrderPick(false);
        dto.setSpecialOrderReceive(false);
        dto.setStatusCode("TEST");
        dto.setTimeZoneId("TEST");
        dto.setTimeZoneName("TEST");
        dto.setTransferVendorId("TEST");
        dto.setTransferVendorId("TEST");
        dto.setTransferVendorLocationCode("TEST");
        dto.setWmsDBInstanceId("TEST");
        dto.setWmsMessageId("TEST");
        assertTrue(null != dto.getAbbreviationCode());
        assertTrue(null != dto.getBillToSiteAddress1());
        assertTrue(null != dto.getBillToSiteAddress2());
        assertTrue(null != dto.getBillToSiteAddress3());
        assertTrue(null != dto.getBillToSiteCityName());
        assertTrue(null != dto.getBillToSiteCountryCode());
        assertTrue(null != dto.getBillToSiteCountyCode());
        assertTrue(null != dto.getBillToSiteName());
        assertTrue(null != dto.getBillToSitePostalCode());
        assertTrue(null != dto.getBillToSiteStateAlphaCode());
        assertTrue(null != dto.getCompanyNumber());
        assertTrue(null != dto.getDcNumber());
        assertTrue(null != dto.getFreezerContactInfo());
        assertTrue(null != dto.getGroceryContactInfo());
        assertTrue(null != dto.getInventoryControlContactInfo());
        assertTrue(null != dto.getLanguageTypeCode());
        assertTrue(null != dto.getMeasurementSystemCode());
        assertTrue(null != dto.getName());
        assertTrue(null != dto.getOfferingGroupId());
        assertTrue(!dto.isOutsideStorage());
        assertTrue(!dto.isPalletTracking());
        assertTrue(null != dto.getPrimarySiteAddress1());
        assertTrue(null != dto.getPrimarySiteAddress2());
        assertTrue(null != dto.getPrimarySiteAddress3());
        assertTrue(null != dto.getPrimarySiteCityName());
        assertTrue(null != dto.getPrimarySiteCountryCode());
        assertTrue(null != dto.getPrimarySiteCountyCode());
        assertTrue(null != dto.getPrimarySiteId());
        assertTrue(null != dto.getPrimarySitePostalCode());
        assertTrue(null != dto.getPrimarySiteStateAlphaCode());
        assertTrue(!dto.isPurchaseCrossdock());
        assertTrue(null != dto.getReservingDCNumber());
        assertTrue(null != dto.getReservingShipDCNumber());
        assertTrue(null != dto.getShortName());
        assertTrue(!dto.isSlowMoverRepack());
        assertTrue(!dto.isSpecialOrderPick());
        assertTrue(!dto.isSpecialOrderReceive());
        assertTrue(null != dto.getStatusCode());
        assertTrue(null != dto.getTimeZoneId());
        assertTrue(null != dto.getTimeZoneName());
        assertTrue(null != dto.getTransferVendorId());
        assertTrue(null != dto.getTransferVendorLocationCode());
        assertTrue(null != dto.getWmsDBInstanceId());
        assertTrue(null != dto.getWmsMessageId());
    }
}
