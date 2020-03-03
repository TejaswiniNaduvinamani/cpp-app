package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfNonBrktCstMdlDTOTest {

    @InjectMocks
    private PrcProfNonBrktCstMdlDTO dto;

    @Test
    public void testPrcProfNonBrktCstMdlDTO() {
        dto.setContractPriceProfileSeq(1);
        dto.setCostModelId(2);
        dto.setItemPriceId("1");
        dto.setItemPriceLevelCode(2);
        dto.setGfsCustomerId("custId");
        dto.setGfsCustomerTypeCode(31);
        dto.setGroupType("test_group");
        dto.setCostModelTypeValue("test_costModelType");

        final PrcProfNonBrktCstMdlDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getContractPriceProfileSeq(), is(actual.getContractPriceProfileSeq()));
        assertThat(dto.getCostModelId(), is(actual.getCostModelId()));
        assertThat(dto.getItemPriceId(), is(actual.getItemPriceId()));
        assertThat(dto.getItemPriceLevelCode(), is(actual.getItemPriceLevelCode()));
        assertThat(dto.getGfsCustomerId(), is(actual.getGfsCustomerId()));
        assertThat(dto.getGfsCustomerTypeCode(), is(actual.getGfsCustomerTypeCode()));
        assertThat(dto.getGroupType(), is(actual.getGroupType()));
        assertThat(dto.getCostModelTypeValue(), is(actual.getCostModelTypeValue()));
    }

}
