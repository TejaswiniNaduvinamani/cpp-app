package com.gfs.cpp.component.activatepricing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.data.activatepricing.ContractCustomerMappingRepository;



@Component("contractCustomerMappingService")
public class ContractCustomerMappingService {
    
    @Autowired
    private ContractCustomerMappingRepository contractCustomerMappingRepository;
    
    public List<ContractCustomerMappingDTO> fetchAllConceptCustomerMapping(int contractPriceProfileSeq){
        return contractCustomerMappingRepository.fetchAllConceptCustomerMapping(contractPriceProfileSeq);
    }
    
    public Integer fetchUnmappedConceptCount(int contractPriceProfileSeq){
        return contractCustomerMappingRepository.fetchUnmappedConceptCount(contractPriceProfileSeq);
    }

}
