package com.gfs.cpp.component.assignment;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.proxy.MemberHierarchyQueryProxy;

@Component
public class CustomerHierarchyValidator {

    @Autowired
    private MemberHierarchyQueryProxy memberHierarchyQueryProxy;

    @Autowired
    private CppConceptMappingRepository cppConceptMappingRepository;

    static final Logger logger = LoggerFactory.getLogger(CustomerHierarchyValidator.class);

    public void validateCustomerHierarchy(int contractPriceProfileSeq, List<RealCustomerDTO> realCustomersToValidate) {

        List<RealCustomerDTO> customersNotAMember = findCustomersNotAMember(contractPriceProfileSeq, realCustomersToValidate);

        if (CollectionUtils.isNotEmpty(customersNotAMember)) {

            String errorMessage = buildErrorMessage(customersNotAMember);
            logger.error(errorMessage);
            throw new CPPRuntimeException(CPPExceptionType.NOT_A_MEMBER_OF_DEFAULT_CUSTOMER, errorMessage);
        }
    }

    private String buildErrorMessage(List<RealCustomerDTO> customersNotAMember) {

        StringBuilder sb = new StringBuilder();
        for (RealCustomerDTO realCustomerDTO : customersNotAMember) {
            sb.append(realCustomerDTO.getRealCustomerType()).append(" ").append(realCustomerDTO.getRealCustomerId()).append(",");
        }
        sb.append(" etc.");

        return String.format("(%s)", sb.toString());
    }

    public List<RealCustomerDTO> findCustomersNotAMember(int contractPriceProfileSeq, List<RealCustomerDTO> realCustomersToValidate) {

        RealCustomerDTO realCustomerMappedToDefaultConcept = cppConceptMappingRepository.fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);

        return memberHierarchyQueryProxy.filterCustomersNotMemberOfDefaultCustomer(realCustomerMappedToDefaultConcept.getRealCustomerId(),
                realCustomerMappedToDefaultConcept.getRealCustomerType(), realCustomersToValidate);
    }

}
