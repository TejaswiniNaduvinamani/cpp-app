package com.gfs.cpp.component.assignment;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CustomerTypeCodeEnum;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;

public abstract class CustomerAssignmentValidator {

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Autowired
    private DuplicateCustomerFinder duplicateCustomerFinder;

    @Autowired
    private CustomerHierarchyValidator customerHierarchyValidator;

    static final Logger logger = LoggerFactory.getLogger(CustomerAssignmentValidator.class);

    public void validateCustomer(CustomerAssignmentDTO customerAssignmentDTO) {

        boolean isDefaultConcept = isDefaultConceptCustomer(customerAssignmentDTO);

        validateExistingMappingForDefaultConcept(customerAssignmentDTO, isDefaultConcept);
        validateIfDuplicateAcrossConcept(customerAssignmentDTO);
        validateIfDuplicateAcrossContracts(customerAssignmentDTO);

        if (!isDefaultConcept) {
            customerHierarchyValidator.validateCustomerHierarchy(customerAssignmentDTO.getContractPriceProfileSeq(),
                    customerAssignmentDTO.getRealCustomerDTOList());
        }
    }

    void validateIfDuplicateAcrossContracts(CustomerAssignmentDTO customerAssignmentDTO) {
        List<DuplicateCustomerDTO> duplicateCustomerAcrossContracts = duplicateCustomerFinder
                .findDuplicateCustomerInOtherContract(customerAssignmentDTO);

        if (CollectionUtils.isNotEmpty(duplicateCustomerAcrossContracts) && duplicateCustomerAcrossContracts.get(0) != null) {
            throw new CPPRuntimeException(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT,
                    buildErrorMessage(duplicateCustomerAcrossContracts));
        }
    }

    void validateIfDuplicateAcrossConcept(CustomerAssignmentDTO customerAssignmentDTO) {
        List<DuplicateCustomerDTO> duplicateCustomerAcrossConcept = duplicateCustomerFinder.findDuplicateCustomerAcrossConcepts(
                customerAssignmentDTO.getRealCustomerDTOList(), customerAssignmentDTO.getContractPriceProfileSeq());

        if (CollectionUtils.isNotEmpty(duplicateCustomerAcrossConcept) && duplicateCustomerAcrossConcept.get(0) != null) {
            throw new CPPRuntimeException(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_CONCEPTS, buildErrorMessage(duplicateCustomerAcrossConcept));
        }
    }

    abstract String buildErrorMessage(List<DuplicateCustomerDTO> duplicateCustomerAcrossConcept);

    boolean isDefaultConceptCustomer(CustomerAssignmentDTO customerAssignmentDTO) {
        CMGCustomerResponseDTO cmgCustomerResponseDTO = contractPriceProfCustomerRepository.fetchGFSCustomerDetailForCustomer(
                customerAssignmentDTO.getContractPriceProfileSeq(), customerAssignmentDTO.getCmgCustomerId(),
                customerAssignmentDTO.getCmgCustomerType());
        return cmgCustomerResponseDTO.getDefaultCustomerInd() == 1 ? true : false;
    }

    void validateExistingMappingForDefaultConcept(CustomerAssignmentDTO customerAssignmentDTO, boolean isDefaultConcept) {

        RealCustomerDTO realCustomerMappedToDefault = cppConceptMappingRepository
                .fetchRealCustomerMappedToDefaultConcept(customerAssignmentDTO.getContractPriceProfileSeq());

        if (isDefaultConcept) {
            if (realCustomerMappedToDefault != null) {
                logger.error("Multiple customer assignments are not allowed on the default/contract level concept");
                throw new CPPRuntimeException(CPPExceptionType.HAS_MULTIPLE_CUSTOMER_MAPPING,
                        "Multiple customer assignments are not allowed on the default/contract level concept.");
            }
        } else {
            validateNewCustomerMappingForExceptionConcept(realCustomerMappedToDefault);
        }

    }

    void validateNewCustomerMappingForExceptionConcept(RealCustomerDTO realCustomerMappedToDefault) {
        if (realCustomerMappedToDefault == null) {
            logger.error("Customer is not assigned to contract level concept");
            throw new CPPRuntimeException(CPPExceptionType.NO_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT,
                    "Please assign a customer to the contract level concept.");
        } else if (realCustomerMappedToDefault.getRealCustomerType() == CustomerTypeCodeEnum.CUSTOMER_UNIT.getGfsCustomerTypeCode()) {
            logger.error("Customer assignments at contract level concept is a unit. Hence, Exception level customer assignments are not allowed.");
            throw new CPPRuntimeException(CPPExceptionType.CUSTOMER_MAPPED_TO_DEFAULT_IS_UNIT_TYPE,
                    "Customer assignments at contract level concept is a unit. Hence, Exception level customer assignments are not allowed.");
        }
    }

}