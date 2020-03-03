package com.gfs.cpp.component.assignment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGContractDTO;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@Component
public class DuplicateCustomerFinder {

    @Autowired
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    static final Logger logger = LoggerFactory.getLogger(DuplicateCustomerFinder.class);

    public List<DuplicateCustomerDTO> findDuplicateCustomerAcrossConcepts(List<RealCustomerDTO> realCustomerList, int contractPriceProfileSeq) {
        List<DuplicateCustomerDTO> realCustomerValidationList = new ArrayList<>();
        for (RealCustomerDTO realCustomerDTO : realCustomerList) {
            List<DuplicateCustomerDTO> realCustomerValidationDTOList = cppConceptMappingRepository.fetchDuplicateCustomersAcrossConcepts(
                    contractPriceProfileSeq, realCustomerDTO.getRealCustomerId(), realCustomerDTO.getRealCustomerType());
            if (CollectionUtils.isNotEmpty(realCustomerValidationDTOList)) {
                for (DuplicateCustomerDTO realCustomerValidationDTO : realCustomerValidationDTOList) {
                    buildRealCustomerValidationDTO(realCustomerValidationDTO);
                }
            }
            realCustomerValidationList.addAll(realCustomerValidationDTOList);
        }
        return realCustomerValidationList;
    }

    public List<DuplicateCustomerDTO> findDuplicateCustomerInOtherContract(CustomerAssignmentDTO customerAssignmentDTO) {
        List<DuplicateCustomerDTO> realCustomerValidationList = new ArrayList<>();
        for (RealCustomerDTO realCustomerDTO : customerAssignmentDTO.getRealCustomerDTOList()) {
            List<DuplicateCustomerDTO> realCustomerValidationDTOList = cppConceptMappingRepository.fetchDuplicateCustomersAcrossOtherActiveContracts(
                    customerAssignmentDTO.getContractPriceProfileId(), realCustomerDTO.getRealCustomerId(), realCustomerDTO.getRealCustomerType(),
                    customerAssignmentDTO.getClmContractStartDate(), customerAssignmentDTO.getClmContractEndDate());
            if (CollectionUtils.isNotEmpty(realCustomerValidationDTOList)) {
                for (DuplicateCustomerDTO realCustomerValidationDTO : realCustomerValidationDTOList) {
                    buildRealCustomerValidationDTO(realCustomerValidationDTO);
                }
            }
            realCustomerValidationList.addAll(realCustomerValidationDTOList);
        }
        return realCustomerValidationList;
    }

    private void buildRealCustomerValidationDTO(DuplicateCustomerDTO realCustomerValidationDTO) {
        int contractPriceProfileId = contractPriceProfileRepository.fetchContractPriceProfileId(realCustomerValidationDTO.getContractPriceProfileSeq());
        CMGContractDTO cmgContractDTO = customerServiceProxy.fetchCustomerGroup(realCustomerValidationDTO.getCustomerId(),
                realCustomerValidationDTO.getCustomerType());
        String customerName = StringUtils.EMPTY;
        if (cmgContractDTO != null && StringUtils.isNotBlank(cmgContractDTO.getContractName())) {
            customerName = cmgContractDTO.getContractName();
        }
        realCustomerValidationDTO.setConceptName(customerName);
        realCustomerValidationDTO.setContractPriceProfileId(contractPriceProfileId);
        realCustomerValidationDTO.setCustomerId(realCustomerValidationDTO.getCustomerId());
        realCustomerValidationDTO.setCustomerType(realCustomerValidationDTO.getCustomerType());
    }
}
