package com.gfs.cpp.common.dto.furtherance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;

@RunWith(MockitoJUnitRunner.class)
public class CPPFurtheranceTrackingDTOTest {

    @InjectMocks
    private CPPFurtheranceTrackingDTO dto;

    @Test
    public void testMethods() {

        dto.setChangeTableName("CIP");
        dto.setGfsCustomerId("customer");
        dto.setGfsCustomerTypeCode(31);
        dto.setCppFurtheranceSeq(1);
        dto.setCreateTimestamp(new Date());
        dto.setCreateUserId("CreateUser");
        dto.setFurtheranceActionCode(1);
        dto.setItemPriceId("item");
        dto.setItemPriceLevelCode(2);
        dto.setLastUpdateTimestamp(new Date());
        dto.setLastUpdateUserId("UpdateUser");
        dto.setCppFurtheranceTrackingSeq(100);

        final CPPFurtheranceTrackingDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getChangeTableName(), is(dto.getChangeTableName()));
        assertThat(actual.getGfsCustomerId(), is(dto.getGfsCustomerId()));
        assertThat(actual.getGfsCustomerTypeCode(), is(dto.getGfsCustomerTypeCode()));
        assertThat(actual.getCppFurtheranceSeq(), is(dto.getCppFurtheranceSeq()));
        assertThat(actual.getCreateTimestamp(), is(dto.getCreateTimestamp()));
        assertThat(actual.getCreateUserId(), is(dto.getCreateUserId()));
        assertThat(actual.getFurtheranceActionCode(), is(dto.getFurtheranceActionCode()));
        assertThat(actual.getItemPriceId(), is(dto.getItemPriceId()));
        assertThat(actual.getItemPriceLevelCode(), is(dto.getItemPriceLevelCode()));
        assertThat(actual.getLastUpdateTimestamp(), is(dto.getLastUpdateTimestamp()));
        assertThat(actual.getLastUpdateUserId(), is(dto.getLastUpdateUserId()));
        assertThat(actual.getCppFurtheranceTrackingSeq(), is(dto.getCppFurtheranceTrackingSeq()));

    }
}
