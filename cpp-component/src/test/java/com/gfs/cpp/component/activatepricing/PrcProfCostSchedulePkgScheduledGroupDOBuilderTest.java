package com.gfs.cpp.component.activatepricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.PrcProfCostSchedulePkgScheduledGroupDTO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgScheduledGroupDO;
import com.gfs.cpp.component.activatepricing.PrcProfCostSchedulePkgScheduledGroupDOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfCostSchedulePkgScheduledGroupDOBuilderTest {

    @InjectMocks
    private PrcProfCostSchedulePkgScheduledGroupDOBuilder target;

    @Test
    public void shouldBuildPrcProfCostSchedulePkgScheduledGroupDO() {
        int pkgSeqNumberForCustomer = 1;
        List<PrcProfCostSchedulePkgScheduledGroupDTO> prcProfCostSchedulePkgScheduledGroupDTOListForCMG = new ArrayList<PrcProfCostSchedulePkgScheduledGroupDTO>();
        PrcProfCostSchedulePkgScheduledGroupDTO prcProfCostSchedulePkgScheduledGroupDTO = new PrcProfCostSchedulePkgScheduledGroupDTO();
        prcProfCostSchedulePkgScheduledGroupDTO.setPrcProfCostSchedulePkgSeq(pkgSeqNumberForCustomer);
        prcProfCostSchedulePkgScheduledGroupDTO.setContractPriceSeq(1);
        prcProfCostSchedulePkgScheduledGroupDTO.setCostRunFrequencyCode("2");
        prcProfCostSchedulePkgScheduledGroupDTO.setScheduleGroup(1);
        prcProfCostSchedulePkgScheduledGroupDTOListForCMG.add(prcProfCostSchedulePkgScheduledGroupDTO);

        List<PrcProfCostSchedulePkgScheduledGroupDO> actual = target
                .buildPrcProfCostSchedulePkgScheduledGroupDOList(prcProfCostSchedulePkgScheduledGroupDTOListForCMG, pkgSeqNumberForCustomer);

        assertThat(actual.get(0).getScheduleGroup(), is(prcProfCostSchedulePkgScheduledGroupDTO.getScheduleGroup()));
        assertThat(actual.get(0).getPrcProfCostSchedulePkgSeq(), is(prcProfCostSchedulePkgScheduledGroupDTO.getPrcProfCostSchedulePkgSeq()));
        assertThat(actual.get(0).getContractPriceSeq(), is(prcProfCostSchedulePkgScheduledGroupDTO.getContractPriceSeq()));
        assertThat(actual.get(0).getCostRunFrequencyCode(), is(prcProfCostSchedulePkgScheduledGroupDTO.getCostRunFrequencyCode()));

    }

}
