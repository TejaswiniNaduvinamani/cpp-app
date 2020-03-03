package com.gfs.cpp.acceptanceTests.mocks;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSOAPResponseDTO;
import com.gfs.cpp.component.distributioncenter.DistributionSOAPClientService;
import com.gfs.cpp.component.distributioncenter.DistributionServiceException;

@Component
public class DistributionSOAPClientServiceMock implements Resettable {

    @Autowired
    private DistributionSOAPClientService distSOAPService;

    private DistributionCenterSOAPResponseDTO distributionCenterSOAPResponseDTO = new DistributionCenterSOAPResponseDTO();

    public void mockFindDcGroupsByDCAndCategory() {

        buildDistributionCenter();
        List<DistributionCenterSOAPResponseDTO> dcList = new ArrayList<>();
        dcList.add(distributionCenterSOAPResponseDTO);

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add(CPPConstants.STATUS_CODE);
        ;
        try {
            when(distSOAPService.getShipDCsForCompanyByLangTypeCode(CPPConstants.COMPANY_NUMBER, statusCodes, CPPConstants.LANGUAGE_CODE))
                    .thenReturn(dcList);
        } catch (DistributionServiceException e) {
        }
    }

    private void buildDistributionCenter() {
        distributionCenterSOAPResponseDTO.setDcNumber(CukesConstants.DC_NUMBER);
        distributionCenterSOAPResponseDTO.setShortName(CukesConstants.DC_NAME);
        distributionCenterSOAPResponseDTO.setName(CukesConstants.DC_NAME);
        distributionCenterSOAPResponseDTO.setWmsDBInstanceId(CukesConstants.DC_NAME);
    }
    
    public void moreThanOneDC() {
        
    }

    @Override
    public void reset() {
        mockFindDcGroupsByDCAndCategory();
    }
}
