package com.gfs.cpp.component.activatepricing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.markup.PrcProfCostSchedulePkgScheduledGroupDTO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgScheduledGroupDO;

@Component("prcProfCostSchedulePkgScheduledGroupDOBuilder")
public class PrcProfCostSchedulePkgScheduledGroupDOBuilder {

    public List<PrcProfCostSchedulePkgScheduledGroupDO> buildPrcProfCostSchedulePkgScheduledGroupDOList(
            List<PrcProfCostSchedulePkgScheduledGroupDTO> prcProfCostSchedulePkgScheduledGroupDTOListForCMG, int pkgSeqNumberForCustomer) {
        List<PrcProfCostSchedulePkgScheduledGroupDO> prcProfCostSchedulePkgScheduledGroupDOList= new ArrayList<>(prcProfCostSchedulePkgScheduledGroupDTOListForCMG.size());
        for (PrcProfCostSchedulePkgScheduledGroupDTO prcProfCostSchedulePkgScheduledGroupDTO : prcProfCostSchedulePkgScheduledGroupDTOListForCMG) {
            PrcProfCostSchedulePkgScheduledGroupDO prcProfCostSchedulePkgScheduledGroupDO = buildPrcProfCostSchedulePkgScheduledGroupDO(
                    pkgSeqNumberForCustomer, prcProfCostSchedulePkgScheduledGroupDTO);
            prcProfCostSchedulePkgScheduledGroupDOList.add(prcProfCostSchedulePkgScheduledGroupDO);
        }
            return prcProfCostSchedulePkgScheduledGroupDOList;
        
    }

    private PrcProfCostSchedulePkgScheduledGroupDO buildPrcProfCostSchedulePkgScheduledGroupDO(int pkgSeqNumberForCustomer,
            PrcProfCostSchedulePkgScheduledGroupDTO prcProfCostSchedulePkgScheduledGroupDTO) {
        PrcProfCostSchedulePkgScheduledGroupDO prcProfCostSchedulePkgScheduledGroupDO =new PrcProfCostSchedulePkgScheduledGroupDO();
        prcProfCostSchedulePkgScheduledGroupDO.setContractPriceSeq(prcProfCostSchedulePkgScheduledGroupDTO.getContractPriceSeq());
        prcProfCostSchedulePkgScheduledGroupDO.setCostRunFrequencyCode(prcProfCostSchedulePkgScheduledGroupDTO.getCostRunFrequencyCode());
        prcProfCostSchedulePkgScheduledGroupDO.setPrcProfCostSchedulePkgSeq(pkgSeqNumberForCustomer);
        prcProfCostSchedulePkgScheduledGroupDO.setScheduleGroup(prcProfCostSchedulePkgScheduledGroupDTO.getScheduleGroup());
        return prcProfCostSchedulePkgScheduledGroupDO;
    }
}
