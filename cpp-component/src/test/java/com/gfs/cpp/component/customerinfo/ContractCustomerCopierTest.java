package com.gfs.cpp.component.customerinfo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.model.assignments.CustomerAssignmentDO;
import com.gfs.cpp.component.customerinfo.ContractCustomerCopier;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;

@RunWith(MockitoJUnitRunner.class)
public class ContractCustomerCopierTest {

    @InjectMocks
    private ContractCustomerCopier target;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Test
    public void shouldFetchGFSCustomerDetailsListForLatestContractVersion() {
        int cppSeqForLatestContractVersion = 2;
        String customerId = "123456";
        Integer customerTypeCode = 0;

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(customerId);
        cmgCustomerResponseDTO.setTypeCode(customerTypeCode);
        List<CMGCustomerResponseDTO> cmgCustomerResponseDTOList = new ArrayList<>();
        cmgCustomerResponseDTOList.add(cmgCustomerResponseDTO);

        when(contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(cppSeqForLatestContractVersion)).thenReturn(cmgCustomerResponseDTOList);

        List<CMGCustomerResponseDTO> result = target.fetchGFSCustomerDetailsListForLatestContractVersion(cppSeqForLatestContractVersion);
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getId(), is(customerId));
        assertThat(result.get(0).getTypeCode(), is(customerTypeCode));

        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailsList(eq(cppSeqForLatestContractVersion));

    }

    @Test
    public void shouldRetrieveDefaultCMGForLatestContractVer() {
        String customerId = "123456";
        Integer customerTypeCode = 0;

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(customerId);
        cmgCustomerResponseDTO.setTypeCode(customerTypeCode);
        cmgCustomerResponseDTO.setDefaultCustomerInd(1);

        CMGCustomerResponseDTO cmgCustomerResponseDTOForNonDefaultCust = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTOForNonDefaultCust.setId(customerId + "test");
        cmgCustomerResponseDTOForNonDefaultCust.setTypeCode(customerTypeCode + 1);
        cmgCustomerResponseDTOForNonDefaultCust.setDefaultCustomerInd(0);

        List<CMGCustomerResponseDTO> cmgCustomerResponseDTOList = new ArrayList<>();
        cmgCustomerResponseDTOList.add(cmgCustomerResponseDTO);
        cmgCustomerResponseDTOList.add(cmgCustomerResponseDTOForNonDefaultCust);

        CMGCustomerResponseDTO result = target.retrieveDefaultCMGForLatestContractVer(cmgCustomerResponseDTOList);
        assertThat(result.getId(), is(customerId));
        assertThat(result.getTypeCode(), is(customerTypeCode));
        assertThat(result.getDefaultCustomerInd(), is(1));

    }

    @Test
    public void shouldCopyCustomerAndCustomerMapping() {
        String customerId = "123456";
        Integer customerTypeCode = 0;
        int contractPriceProfileSequence = 3;
        String userId = "Test";
        int cppCustomerNextSeq = 23;
        int existingCustomerSeq = 22;

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(customerId);
        cmgCustomerResponseDTO.setTypeCode(customerTypeCode);
        cmgCustomerResponseDTO.setCppCustomerSeq(existingCustomerSeq);
        cmgCustomerResponseDTO.setDefaultCustomerInd(1);

        List<CMGCustomerResponseDTO> cmgCustomerResponseDTOList = new ArrayList<>();
        cmgCustomerResponseDTOList.add(cmgCustomerResponseDTO);

        List<CustomerAssignmentDO> saveAssignmentDOList = new ArrayList<>();
        CustomerAssignmentDO customerAssignmentDO = new CustomerAssignmentDO();
        customerAssignmentDO.setCppCustomerSeq(cppCustomerNextSeq);
        customerAssignmentDO.setGfsCustomerId(customerId);
        customerAssignmentDO.setGfsCustomerType(customerTypeCode);
        saveAssignmentDOList.add(customerAssignmentDO);

        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerId(customerId);
        realCustomerDTO.setRealCustomerType(customerTypeCode);
        realCustomerDTOList.add(realCustomerDTO);

        when(contractPriceProfCustomerRepository.fetchCPPCustomerNextSequence()).thenReturn(cppCustomerNextSeq);
        when(cppConceptMappingRepository.fetchRealCustomersMappedForCppSeq(existingCustomerSeq)).thenReturn(realCustomerDTOList);

        target.copyCustomerAndCustomerMapping(contractPriceProfileSequence, userId, cmgCustomerResponseDTOList);

        verify(contractPriceProfCustomerRepository).fetchCPPCustomerNextSequence();
        verify(cppConceptMappingRepository).fetchRealCustomersMappedForCppSeq(eq(existingCustomerSeq));
        verify(contractPriceProfCustomerRepository).saveContractPriceProfCustomer(eq(contractPriceProfileSequence), eq(customerId), eq(customerTypeCode),
                eq(userId), eq(1), eq(cppCustomerNextSeq));
        verify(cppConceptMappingRepository).saveAssignments(eq(saveAssignmentDOList), eq(userId));
    }

}
