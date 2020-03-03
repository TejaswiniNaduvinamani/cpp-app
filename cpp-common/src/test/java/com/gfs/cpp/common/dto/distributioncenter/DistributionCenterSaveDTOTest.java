package com.gfs.cpp.common.dto.distributioncenter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSaveDTO;

@RunWith(MockitoJUnitRunner.class)
public class DistributionCenterSaveDTOTest {

    @InjectMocks
    private DistributionCenterSaveDTO dto;

    @Test
    public void testMethods() {

        dto.setContractPriceProfileSeq(0);
        dto.setCreateUserID("");
        ArrayList<String> distributionCenters = new ArrayList<>();
        dto.setDistributionCenters(distributionCenters);
        dto.setEffectiveDate(new LocalDate(9999, 01, 01).toDate());
        dto.setExpirationDate(new LocalDate(9999, 01, 01).toDate());

        final DistributionCenterSaveDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getCreateUserID(), is(dto.getCreateUserID()));
        assertThat(actual.getDistributionCenters(), is(dto.getDistributionCenters()));
        assertThat(actual.getEffectiveDate(), is(dto.getEffectiveDate()));
        assertThat(actual.getExpirationDate(), is(dto.getExpirationDate()));
    }
}
