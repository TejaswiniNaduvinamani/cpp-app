package com.gfs.cpp.component.assignment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;

@Component
public class FindCustomerAssignmentValidator extends CustomerAssignmentValidator {

    private static final String CONCEPT_NAME = "conceptName";

    @Override
    String buildErrorMessage(List<DuplicateCustomerDTO> duplicateCustomerList) {

        DuplicateCustomerDTO realCustomerValidationDTO = duplicateCustomerList.get(0);

        String errorMessage = String.format("This customer has already been assigned to %s concept (CPP ID: %d)",
                realCustomerValidationDTO.getConceptName(), realCustomerValidationDTO.getContractPriceProfileId());

        logger.error(errorMessage);

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(CONCEPT_NAME, realCustomerValidationDTO.getConceptName());
        errorMap.put(CPPConstants.CONTRACT_PRICE_PROFILE_ID, String.valueOf(realCustomerValidationDTO.getContractPriceProfileId()));

        return String.format("%s", new JSONObject(errorMap));
    }

}
