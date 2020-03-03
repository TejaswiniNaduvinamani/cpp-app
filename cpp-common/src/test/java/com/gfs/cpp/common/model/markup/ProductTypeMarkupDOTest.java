package com.gfs.cpp.common.model.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;

@RunWith(MockitoJUnitRunner.class)
public class ProductTypeMarkupDOTest {

    @InjectMocks
    private ProductTypeMarkupDO dto;

    @Test
    public void testMethods() {
        dto.setEffectiveDate(new Date());
        dto.setExpirationDate(new Date());
        dto.setItemPriceId(2);
        dto.setMarkup(new BigDecimal(0));
        dto.setMarkupType(2);
        dto.setProductType("1");
        dto.setUnit("");
        dto.setGfsCustomerId("1234");
        dto.setCustomerTypeCode(3);
        dto.setContractPriceProfileSeq(1);
        dto.setPriceLockedInTypeCode(2);
        dto.setHoldCostFirmInd(1);
        dto.setPriceLockinReasonCode(2);
        dto.setPriceMaintenanceSourceCode(1);
        dto.setCreateUserId("test user");
        dto.setLastUpdateUserId("test user");

        final ProductTypeMarkupDO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getEffectiveDate(), is(dto.getEffectiveDate()));
        assertThat(actual.getExpirationDate(), is(dto.getExpirationDate()));
        assertThat(actual.getItemPriceId(), is(dto.getItemPriceId()));
        assertThat(actual.getMarkup(), is(dto.getMarkup()));
        assertThat(actual.getMarkupType(), is(dto.getMarkupType()));
        assertThat(actual.getProductType(), is(dto.getProductType()));
        assertThat(actual.getUnit(), is(dto.getUnit()));
        assertThat(actual.getGfsCustomerId(), is(dto.getGfsCustomerId()));
        assertThat(actual.getCustomerTypeCode(), is(dto.getCustomerTypeCode()));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getPriceLockedInTypeCode(), is(dto.getPriceLockedInTypeCode()));
        assertThat(actual.getHoldCostFirmInd(), is(dto.getHoldCostFirmInd()));
        assertThat(actual.getPriceLockinReasonCode(), is(dto.getPriceLockinReasonCode()));
        assertThat(actual.getPriceMaintenanceSourceCode(), is(dto.getPriceMaintenanceSourceCode()));
        assertThat(actual.getCreateUserId(), is(dto.getCreateUserId()));
        assertThat(actual.getLastUpdateUserId(), is(dto.getLastUpdateUserId()));
    }
}
