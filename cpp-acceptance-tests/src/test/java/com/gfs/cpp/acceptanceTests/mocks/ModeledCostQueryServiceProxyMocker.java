package com.gfs.cpp.acceptanceTests.mocks;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.dto.costmodel.CostModelDTO;
import com.gfs.cpp.proxy.ModeledCostQueryServiceProxy;

@Component
public class ModeledCostQueryServiceProxyMocker implements Resettable {

    @Autowired
    private ModeledCostQueryServiceProxy modeledCostQueryServiceProxy;

    private CostModelDTO costModelDTO = new CostModelDTO();

    @Override
    public void reset() {
        mockFindAllActiveCostModels();
    }

    public void mockFindAllActiveCostModels() {

        buildCostModelDTO();
        List<CostModelDTO> costModelList = new ArrayList<>();
        costModelList.add(costModelDTO);

        when(modeledCostQueryServiceProxy.lookupAllActiveCostModels()).thenReturn(costModelList);

    }

    private void buildCostModelDTO() {
        costModelDTO.setModelId(CukesConstants.COST_MODEL_ID);
        costModelDTO.setName(CukesConstants.COST_MODEL_NAME);
        costModelDTO.setProfileSelectionCriteria(CukesConstants.COST_MODEL_PROFILE_SELECTION);
    }

}
