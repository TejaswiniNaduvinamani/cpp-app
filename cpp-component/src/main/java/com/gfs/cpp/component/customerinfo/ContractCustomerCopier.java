package com.gfs.cpp.component.customerinfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.model.assignments.CustomerAssignmentDO;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;

@Component
public class ContractCustomerCopier {

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private CppConceptMappingRepository cppConceptMappingRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public List<CMGCustomerResponseDTO> fetchGFSCustomerDetailsListForLatestContractVersion(int cppSeqForLatestContractVersion) {
        return contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(cppSeqForLatestContractVersion);
    }

    public CMGCustomerResponseDTO retrieveDefaultCMGForLatestContractVer(
            List<CMGCustomerResponseDTO> cmgCustomerResponseDTOListForLatestContractVer) {
        for (CMGCustomerResponseDTO defaultCMG : ListUtils.emptyIfNull(cmgCustomerResponseDTOListForLatestContractVer)) {
            if (defaultCMG.getDefaultCustomerInd() == 1) {
                return defaultCMG;
            }
        }
        return null;
    }

    public void copyCustomerAndCustomerMapping(int contractPriceProfileSequence, String userId, List<CMGCustomerResponseDTO> cmgCustomerResponseDTOList) {
        logger.info("Copying customer entry in prc_prof_customer & the mapping table for contract {} ", contractPriceProfileSequence);

        for (CMGCustomerResponseDTO cmgCustomerResponseDTO : cmgCustomerResponseDTOList) {
            
            int cppCustomerSeq = contractPriceProfCustomerRepository.fetchCPPCustomerNextSequence();
            
            contractPriceProfCustomerRepository.saveContractPriceProfCustomer(contractPriceProfileSequence, cmgCustomerResponseDTO.getId(),
                    cmgCustomerResponseDTO.getTypeCode(), userId, cmgCustomerResponseDTO.getDefaultCustomerInd(), cppCustomerSeq);
            
            copyCPPConceptMapping(userId, cmgCustomerResponseDTO.getCppCustomerSeq(), cppCustomerSeq);
        }
    }

    private void copyCPPConceptMapping(String userId, int existingCppCustomerSeq, int newCppCustomerSeq) {
        List<CustomerAssignmentDO> saveAssignmentDOList = new ArrayList<>();

        List<RealCustomerDTO> realCustomerDTOList = cppConceptMappingRepository.fetchRealCustomersMappedForCppSeq(existingCppCustomerSeq);
        for (RealCustomerDTO realCustomerDTO : ListUtils.emptyIfNull(realCustomerDTOList)) {
            CustomerAssignmentDO customer = new CustomerAssignmentDO();
            customer.setGfsCustomerId(realCustomerDTO.getRealCustomerId());
            customer.setGfsCustomerType(realCustomerDTO.getRealCustomerType());
            customer.setCppCustomerSeq(newCppCustomerSeq);
            saveAssignmentDOList.add(customer);
        }

        if (CollectionUtils.isNotEmpty(saveAssignmentDOList)) {
            cppConceptMappingRepository.saveAssignments(saveAssignmentDOList, userId);
        }
    }

  


}
