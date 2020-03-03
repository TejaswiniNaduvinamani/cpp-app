package com.gfs.cpp.component.clm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;

@Component
public class ClmContractTypeChecker {

    @Autowired
    private ClmContractTypeRepository contractTypeRepository;

    public boolean isPricingContractType(String contractType) {
        return contractTypeRepository.getAllContractTypes().contains(contractType);
    }

}
