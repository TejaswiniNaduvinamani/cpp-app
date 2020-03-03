package com.gfs.cpp.component.statusprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@Component
public class ContractPriceProfileNameSyncher {

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    public void synchCLMContractNameWithCPPContractName(String clmContractName, int contractPriceProfileSeq, String lastUpdateUserId) {

        ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        String cppContractName = contractPricingResponseDTO.getClmContractName();

        String updatedClmContractName = clmContractName.substring(0, Math.min(clmContractName.length(), CPPConstants.MAX_CONTRACT_NAME_LENGTH));

        if (!updatedClmContractName.equals(cppContractName)) {
            contractPriceProfileRepository.updateContractPriceProfileContractName(contractPriceProfileSeq, updatedClmContractName, lastUpdateUserId);
        }
    }

}
