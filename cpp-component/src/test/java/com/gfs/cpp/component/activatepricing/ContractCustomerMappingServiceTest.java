package com.gfs.cpp.component.activatepricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.component.activatepricing.ContractCustomerMappingService;
import com.gfs.cpp.data.activatepricing.ContractCustomerMappingRepository;

@RunWith(MockitoJUnitRunner.class)
public class ContractCustomerMappingServiceTest {

    @InjectMocks
    private ContractCustomerMappingService target;

    @Mock
    private ContractCustomerMappingRepository contractCustomerMappingRepository;

    @Captor
    private ArgumentCaptor<ContractCustomerMappingDTO> contractCustomerMappingCaptor;

    @Test
    public void shouldFetchCmgCustomerMapping() {

        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = new ArrayList<ContractCustomerMappingDTO>();
        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("1");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(22);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);
        contractCustomerMappingDTOList.add(contractCustomerMappingDTO);

        when(contractCustomerMappingRepository.fetchAllConceptCustomerMapping(1)).thenReturn(contractCustomerMappingDTOList);

        target.fetchAllConceptCustomerMapping(1);

        verify(contractCustomerMappingRepository).fetchAllConceptCustomerMapping(1);

        assertThat(contractCustomerMappingDTO.getCppConceptMappingSeq(), is(1));
    }

    @Test
    public void shouldFetchUnmappedContractConcepts() {

        when(contractCustomerMappingRepository.fetchUnmappedConceptCount(1)).thenReturn(1);

        target.fetchUnmappedConceptCount(1);

        verify(contractCustomerMappingRepository).fetchUnmappedConceptCount(1);

    }

}
