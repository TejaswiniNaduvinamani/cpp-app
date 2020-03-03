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

import com.gfs.cpp.common.model.markup.FutureItemDO;

@RunWith(MockitoJUnitRunner.class)
public class FutureItemDOTest {

    @InjectMocks
    private FutureItemDO dto;

    @Test
    public void testMethods() {
        dto.setEffectiveDate(new Date());
        dto.setExpirationDate(new Date());
        dto.setContractPriceProfileSeq(3);
        dto.setItemDesc("test");
        dto.setMarkup(new BigDecimal(0));
        dto.setMarkupType(2);
        dto.setUnit("$");
        dto.setGfsCustomerId("1234");
        dto.setCustomerTypeCode(3);

        final FutureItemDO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getEffectiveDate(), is(dto.getEffectiveDate()));
        assertThat(actual.getExpirationDate(), is(dto.getExpirationDate()));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getMarkup(), is(dto.getMarkup()));
        assertThat(actual.getMarkupType(), is(dto.getMarkupType()));
        assertThat(actual.getItemDesc(), is(dto.getItemDesc()));
        assertThat(actual.getUnit(), is(dto.getUnit()));
        assertThat(actual.getGfsCustomerId(), is(dto.getGfsCustomerId()));
        assertThat(actual.getCustomerTypeCode(), is(dto.getCustomerTypeCode()));
    }
}
