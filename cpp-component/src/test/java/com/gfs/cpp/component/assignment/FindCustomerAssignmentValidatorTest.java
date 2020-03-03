package com.gfs.cpp.component.assignment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;
import com.gfs.cpp.component.assignment.FindCustomerAssignmentValidator;

public class FindCustomerAssignmentValidatorTest {

    private FindCustomerAssignmentValidator target = new FindCustomerAssignmentValidator();

    private static final String CONCEPT_NAME = "conceptName";

    @Test
    public void shouldBuildErrorMessageAsJsonObject() throws Exception {

        String conceptName = "concept name";
        int contractPriceProfileId = 1012;

        DuplicateCustomerDTO duplicateCustomerDTO = new DuplicateCustomerDTO();
        duplicateCustomerDTO.setConceptName(conceptName);
        duplicateCustomerDTO.setContractPriceProfileId(contractPriceProfileId);
        List<DuplicateCustomerDTO> duplicateCustomerList = Collections.singletonList(duplicateCustomerDTO);

        String actualMessage = target.buildErrorMessage(duplicateCustomerList);

        JSONObject actualObject = new JSONObject(actualMessage);
        assertThat(actualObject.get(CONCEPT_NAME).toString(), equalTo(conceptName));
        assertThat(actualObject.get(CPPConstants.CONTRACT_PRICE_PROFILE_ID).toString(), equalTo(String.valueOf(contractPriceProfileId)));

    }
}
