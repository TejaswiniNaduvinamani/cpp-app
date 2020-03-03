package com.gfs.cpp.component.statusprocessor;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractChangeEventDTO;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@RunWith(MockitoJUnitRunner.class)
public class UpdateContractUrlProcessorTest {

    @InjectMocks
    private UpdateContractUrlProcessor target;

    @Mock
    private ClmApiProxy clmApiProxy;

    @Test
    public void shouldUpdateCppUrl() throws Exception {

        String agreementId = "agreementId";
        String contractType = "contractType";

        ClmContractChangeEventDTO contractChangeEvent = new ClmContractChangeEventDTO();
        contractChangeEvent.setAgreementId(agreementId);
        contractChangeEvent.setContractType(contractType);

        target.process(contractChangeEvent);

        verify(clmApiProxy).updateCppUrlForPricingContract(agreementId, contractType);
    }

}
