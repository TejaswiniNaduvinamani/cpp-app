package com.gfs.cpp.component.assignment;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGContractDTO;
import com.gfs.cpp.component.assignment.DuplicateCustomerFinder;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class DuplicateCustomerFinderTest {

    @InjectMocks
    private DuplicateCustomerFinder target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    @Test
    public void shouldValidateCustomerAcrossConceptInContract() throws Exception {
        int contractPriceProfileSeq = 1;
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        List<RealCustomerDTO> realCustomerList = new ArrayList<>();
        realCustomerDTO.setIsCustomerSaved(true);
        realCustomerDTO.setRealCustomerGroupType("CMG");
        realCustomerDTO.setRealCustomerId("31");
        realCustomerDTO.setRealCustomerName("Test");
        realCustomerDTO.setRealCustomerType(1);
        realCustomerList.add(realCustomerDTO);

        List<DuplicateCustomerDTO> realCustomerValidationDTOList = new ArrayList<>();
        DuplicateCustomerDTO realCustomerValidationDTO = new DuplicateCustomerDTO();
        realCustomerValidationDTO.setConceptName("test");
        realCustomerValidationDTO.setContractPriceProfileId(1);
        realCustomerValidationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        realCustomerValidationDTO.setCustomerId("31");
        realCustomerValidationDTO.setCustomerType(1);
        realCustomerValidationDTOList.add(realCustomerValidationDTO);

        CMGContractDTO cmgContractDTO = new CMGContractDTO();
        cmgContractDTO.setContractName("Test");
        cmgContractDTO.setContractPriceProfileId("1");

        when(cppConceptMappingRepository.fetchDuplicateCustomersAcrossConcepts(contractPriceProfileSeq, "31", 1)).thenReturn(realCustomerValidationDTOList);
        when(contractPriceProfileRepository.fetchContractPriceProfileId(contractPriceProfileSeq)).thenReturn(1);
        when(customerServiceProxy.fetchCustomerGroup("31", 1)).thenReturn(cmgContractDTO);

        target.findDuplicateCustomerAcrossConcepts(realCustomerList, contractPriceProfileSeq);

        verify(cppConceptMappingRepository).fetchDuplicateCustomersAcrossConcepts(contractPriceProfileSeq, "31", 1);
        verify(contractPriceProfileRepository).fetchContractPriceProfileId(contractPriceProfileSeq);
        verify(customerServiceProxy).fetchCustomerGroup("31", 1);

    }

    @Test
    public void shouldCheckIfDuplicateCustomerInOtherContract() throws Exception {
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "31";
        int gfsCustomerType = 1;
        String cmgCustomerId = "21";
        int cmgCustomerType = 1;
        int contractPriceProfileId = 1;
        Date newPrcExpirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999");
        Date newPrcEffectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999");
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        List<RealCustomerDTO> realCustomerList = new ArrayList<>();
        realCustomerDTO.setIsCustomerSaved(true);
        realCustomerDTO.setRealCustomerGroupType("CMG");
        realCustomerDTO.setRealCustomerId(gfsCustomerId);
        realCustomerDTO.setRealCustomerName("Test");
        realCustomerDTO.setRealCustomerType(gfsCustomerType);
        realCustomerList.add(realCustomerDTO);

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setCmgCustomerId(cmgCustomerId);
        customerAssignmentDTO.setCmgCustomerType(cmgCustomerType);
        customerAssignmentDTO.setContractPriceProfileId(contractPriceProfileId);
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        customerAssignmentDTO.setClmContractStartDate(new LocalDate(9999, 01, 01).toDate());
        customerAssignmentDTO.setClmContractEndDate(new LocalDate(9999, 01, 01).toDate());
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerList);

        
        List<DuplicateCustomerDTO> realCustomerValidationDTOList = new ArrayList<>();
        
        DuplicateCustomerDTO realCustomerValidationDTO = new DuplicateCustomerDTO();
        realCustomerValidationDTO.setConceptName("test");
        realCustomerValidationDTO.setContractPriceProfileId(1);
        realCustomerValidationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        realCustomerValidationDTO.setCustomerId(gfsCustomerId);
        realCustomerValidationDTO.setCustomerType(gfsCustomerType);
        
        realCustomerValidationDTOList.add(realCustomerValidationDTO);

        CMGContractDTO cmgContractDTO = new CMGContractDTO();
        cmgContractDTO.setContractName("Test");
        cmgContractDTO.setContractPriceProfileId("1");

        when(contractPriceProfileRepository.fetchContractPriceProfileId(contractPriceProfileSeq)).thenReturn(1);
        when(customerServiceProxy.fetchCustomerGroup("31", 1)).thenReturn(cmgContractDTO);
        when(cppConceptMappingRepository.fetchDuplicateCustomersAcrossOtherActiveContracts(1, gfsCustomerId, gfsCustomerType, newPrcEffectiveDate,
                newPrcExpirationDate)).thenReturn(realCustomerValidationDTOList);

        target.findDuplicateCustomerInOtherContract(customerAssignmentDTO);

        verify(contractPriceProfileRepository).fetchContractPriceProfileId(contractPriceProfileSeq);
        verify(customerServiceProxy).fetchCustomerGroup(gfsCustomerId, gfsCustomerType);
        verify(cppConceptMappingRepository).fetchDuplicateCustomersAcrossOtherActiveContracts(1, gfsCustomerId, gfsCustomerType, newPrcEffectiveDate,
                newPrcExpirationDate);

    }

}
