package com.gfs.cpp.component.assignment;

import java.util.List;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;
import com.gfs.cpp.common.util.CustomerTypeCodeEnum;

@Component
public class SaveCustomerAssignmentValidator extends CustomerAssignmentValidator {

    @Override
    String buildErrorMessage(List<DuplicateCustomerDTO> duplicateCustomerList) {
        StringBuilder sb = new StringBuilder();
        for (DuplicateCustomerDTO realCustomerValidationDTO : duplicateCustomerList) {

            sb.append(CustomerTypeCodeEnum.getTypeNameByCode(realCustomerValidationDTO.getCustomerType())).append(" ")
                    .append(realCustomerValidationDTO.getCustomerId()).append(",");
        }
        String errorMessage = String.format("Duplicate customers found. Please fix and try again (%s)", sb.substring(0, sb.length() - 1));
        logger.error(errorMessage);

        return String.format("(%s)", sb.substring(0, sb.length() - 1));
    }

}
