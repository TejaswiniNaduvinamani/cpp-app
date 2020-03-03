package com.gfs.cpp.common.model.distributioncenter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;

@RunWith(MockitoJUnitRunner.class)
public class DistributionCenterDetailDOTest {

    @InjectMocks
    private DistributionCenterDetailDO dto;

    @Test
    public void testMethods() {

        dto.setContractPriceProfileSeq(0);
        dto.setEffectiveDate(new Date());
        dto.setExpirationDate(new Date());
        dto.setDcCode("");

        final DistributionCenterDetailDO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getDcCode(), is(dto.getDcCode()));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getExpirationDate(), is(dto.getExpirationDate()));
        assertThat(actual.getEffectiveDate(), is(dto.getEffectiveDate()));
    }

}
