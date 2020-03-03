package com.gfs.cpp.component.assignment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@Component
public class CustomerAssignmentBuilder {

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    CustomerAssignmentDTO buildCustomerAssignmentDTO(String gfsCustomerId, int gfsCustomerType, int contractPriceProfileSeq, String cmgCustomerId,
            int cmgCustomerType) {

        ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setCmgCustomerId(cmgCustomerId);
        customerAssignmentDTO.setCmgCustomerType(cmgCustomerType);
        customerAssignmentDTO.setContractPriceProfileId(contractPricingResponseDTO.getContractPriceProfileId());
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        customerAssignmentDTO.setClmContractStartDate(contractPricingResponseDTO.getClmContractStartDate());
        customerAssignmentDTO.setClmContractEndDate(contractPricingResponseDTO.getClmContractEndDate());

        List<RealCustomerDTO> realCustomerList = buildRealCustomerList(gfsCustomerId, gfsCustomerType);
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerList);
        return customerAssignmentDTO;
    }

    private List<RealCustomerDTO> buildRealCustomerList(String gfsCustomerId, int gfsCustomerType) {
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        List<RealCustomerDTO> realCustomerList = new ArrayList<>();
        realCustomerDTO.setRealCustomerId(gfsCustomerId);
        realCustomerDTO.setRealCustomerType(gfsCustomerType);
        realCustomerList.add(realCustomerDTO);
        return realCustomerList;
    }

}
