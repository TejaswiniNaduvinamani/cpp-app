package com.gfs.cpp.component.assignment;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;
import com.gfs.cpp.common.util.CustomerTypeCodeEnum;
import com.gfs.cpp.component.assignment.SaveCustomerAssignmentValidator;

public class SaveCustomerAssignmentValidatorTest {

    private SaveCustomerAssignmentValidator target = new SaveCustomerAssignmentValidator();

    @Test
    public void shouldBuildMessageWithCustomerIdAndType() throws Exception {

        String customerId = "customer id";

        DuplicateCustomerDTO duplicateCustomerDTO = new DuplicateCustomerDTO();
        duplicateCustomerDTO.setCustomerType(CustomerTypeCodeEnum.CUSTOMER_FAMILY.getGfsCustomerTypeCode());
        duplicateCustomerDTO.setCustomerId(customerId);
        List<DuplicateCustomerDTO> duplicateCustomerList = Collections.singletonList(duplicateCustomerDTO);

        String actualMessage = target.buildErrorMessage(duplicateCustomerList);

        assertThat(actualMessage, containsString(CustomerTypeCodeEnum.CUSTOMER_FAMILY.getGfsCustomerTypeName() + " " + customerId));

    }

}
