package com.gfs.cpp.common.model.distributioncenter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDO;

@RunWith(MockitoJUnitRunner.class)
public class DistributionCenterDOTest {
    
    @InjectMocks
    private DistributionCenterDO distributionCenterDO;
    
    @Test
    public void testMethods() {
        distributionCenterDO.setContractPriceProfileSeq(0);
        distributionCenterDO.setCreateUserID("");
        ArrayList<String> distributionCenters = new ArrayList<>();
        distributionCenterDO.setDcCodes(distributionCenters);
        distributionCenterDO.setEffectiveDate(new Date());
        distributionCenterDO.setExpirationDate(new Date());
        final DistributionCenterDO actual = SerializationUtils.clone(distributionCenterDO);
        assertThat(distributionCenterDO.equals(actual), is(true));
        assertThat(distributionCenterDO.hashCode(), is(actual.hashCode()));
        assertThat(distributionCenterDO.toString()!=null, is(true));
        assertThat(actual.getDcCodes(), is(distributionCenterDO.getDcCodes()));
        assertThat(actual.getContractPriceProfileSeq(), is(distributionCenterDO.getContractPriceProfileSeq()));
        assertThat(actual.getCreateUserID(), is(distributionCenterDO.getCreateUserID()));
        assertThat(actual.getExpirationDate(), is(distributionCenterDO.getExpirationDate()));
        assertThat(actual.getEffectiveDate(), is(distributionCenterDO.getEffectiveDate()));
    }

}
