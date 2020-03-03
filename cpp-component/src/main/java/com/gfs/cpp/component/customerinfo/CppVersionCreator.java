package com.gfs.cpp.component.customerinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@Component
public class CppVersionCreator {

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    public CPPInformationDTO createInitialVersion() {
        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileId(contractPriceProfileRepository.fetchContractPriceProfileIdSeq());
        cppInformationDTO.setContractPriceProfileSeq(contractPriceProfileRepository.fetchCPPNextSequence());
        cppInformationDTO.setVersionNumber(CPPConstants.INDICATOR_ONE);
        return cppInformationDTO;
    }

    public CPPInformationDTO createNextCppVersion(int latestVersionNumber, int contractPriceProfileId) {
        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileId(contractPriceProfileId);
        cppInformationDTO.setContractPriceProfileSeq(contractPriceProfileRepository.fetchCPPNextSequence());
        cppInformationDTO.setVersionNumber(latestVersionNumber + 1);
        return cppInformationDTO;
    }

}
