package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.PrcProfCostSchedulePkgScheduledGroupDTO;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfCostSchedulePkgScheduledGroupDTOTest {

    @InjectMocks
    private PrcProfCostSchedulePkgScheduledGroupDTO dto;

    @Test
    public void testPrcProfCostSchedulePkgScheduledGroupDTO() {
        dto.setPrcProfCostSchedulePkgSeq(2);
        dto.setContractPriceSeq(1);
        dto.setCostRunFrequencyCode("2");
        dto.setScheduleGroup(1);

        final PrcProfCostSchedulePkgScheduledGroupDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getScheduleGroup(), is(actual.getScheduleGroup()));
        assertThat(dto.getPrcProfCostSchedulePkgSeq(), is(actual.getPrcProfCostSchedulePkgSeq()));
        assertThat(dto.getContractPriceSeq(), is(actual.getContractPriceSeq()));
        assertThat(dto.getCostRunFrequencyCode(), is(actual.getCostRunFrequencyCode()));
    }

}
