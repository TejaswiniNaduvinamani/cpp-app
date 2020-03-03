package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;

@RunWith(MockitoJUnitRunner.class)
public class ProductTypeMarkupDTOTest {

    @InjectMocks
    private ProductTypeMarkupDTO dto;

    @Test
    public void testMethods() {
        dto.setGfsCustomerId("1");
        dto.setEffectiveDate(new LocalDate(2010, 01, 01).toDate());
        dto.setExpirationDate(new LocalDate(2011, 01, 01).toDate());
        dto.setItemPriceId(2);
        dto.setMarkup("Test markup");
        dto.setMarkupType(2);
        dto.setProductType("2");
        dto.setUnit("%");
        dto.setGfsCustomerTypeCode(12);
        dto.setCustomerItemPriceSeq(1);
        dto.setContractPriceProfileSeq(1);
        dto.setPriceLockedInTypeCode(0);
        dto.setHoldCostFirmInd(1);
        dto.setPriceLockinReasonCode(2);
        dto.setPriceMaintenanceSourceCode(3);

        final ProductTypeMarkupDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getGfsCustomerId(), is(dto.getGfsCustomerId()));
        assertThat(actual.getEffectiveDate(), is(dto.getEffectiveDate()));
        assertThat(actual.getExpirationDate(), is(dto.getExpirationDate()));
        assertThat(actual.getItemPriceId(), is(dto.getItemPriceId()));
        assertThat(actual.getMarkup(), is(dto.getMarkup()));
        assertThat(actual.getMarkupType(), is(dto.getMarkupType()));
        assertThat(actual.getProductType(), is(dto.getProductType()));
        assertThat(actual.getUnit(), is(dto.getUnit()));
        assertThat(actual.getGfsCustomerTypeCode(), is(dto.getGfsCustomerTypeCode()));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getCustomerItemPriceSeq(), is(dto.getCustomerItemPriceSeq()));
        assertThat(actual.getHoldCostFirmInd(), is(dto.getHoldCostFirmInd()));
        assertThat(actual.getPriceLockinReasonCode(), is(dto.getPriceLockinReasonCode()));
        assertThat(actual.getPriceMaintenanceSourceCode(), is(dto.getPriceMaintenanceSourceCode()));
        assertThat(actual.getPriceLockedInTypeCode(), is(dto.getPriceLockedInTypeCode()));
    }
}
