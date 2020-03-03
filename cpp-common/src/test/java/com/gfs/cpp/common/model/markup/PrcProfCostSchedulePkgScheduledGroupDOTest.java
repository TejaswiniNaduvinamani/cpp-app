package com.gfs.cpp.common.model.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgScheduledGroupDO;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfCostSchedulePkgScheduledGroupDOTest {
	
	@InjectMocks
	private PrcProfCostSchedulePkgScheduledGroupDO prcProfCostSchedulePkgScheduledGroupDO;
	
	@Test
    public void testPrcProfCostSchedulePkgScheduledGroupDO() {
        prcProfCostSchedulePkgScheduledGroupDO.setPrcProfCostSchedulePkgSeq(2);
        prcProfCostSchedulePkgScheduledGroupDO.setContractPriceSeq(1);
        prcProfCostSchedulePkgScheduledGroupDO.setCostRunFrequencyCode("2");
        prcProfCostSchedulePkgScheduledGroupDO.setScheduleGroup(1);
        
        final PrcProfCostSchedulePkgScheduledGroupDO actual = SerializationUtils.clone(prcProfCostSchedulePkgScheduledGroupDO);

        assertThat(prcProfCostSchedulePkgScheduledGroupDO.equals(actual), is(true));
        assertThat(prcProfCostSchedulePkgScheduledGroupDO.hashCode(), is(actual.hashCode()));
        assertThat(prcProfCostSchedulePkgScheduledGroupDO.toString() != null, is(true));
        
        assertThat(prcProfCostSchedulePkgScheduledGroupDO.getScheduleGroup(), is(actual.getScheduleGroup()));
        assertThat(prcProfCostSchedulePkgScheduledGroupDO.getPrcProfCostSchedulePkgSeq(), is(actual.getPrcProfCostSchedulePkgSeq()));
        assertThat(prcProfCostSchedulePkgScheduledGroupDO.getContractPriceSeq(), is(actual.getContractPriceSeq()));
        assertThat(prcProfCostSchedulePkgScheduledGroupDO.getCostRunFrequencyCode(), is(actual.getCostRunFrequencyCode()));
    }


}
