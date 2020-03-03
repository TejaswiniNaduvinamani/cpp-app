package com.gfs.cpp.acceptanceTests.stepdefs.assignments;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAmendmentContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.common.data.AcceptableCmgCustomer;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableRealCustomer;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.web.controller.assignments.AssignmentController;

import cucumber.api.java.en.Given;

public class AssignmentsStepDefs {

    private static final String SAVE_CUSTOMER_RESPONSE_ATTRIBUTE = "saveCustomerErrorType";

    @Autowired
    private AssignmentController assignmentsController;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Given("^a request is made to save assignment for \"([^\"]*)\" with real customer \"([^\"]*)\"$")
    public void a_request_is_made_to_save_assignment_for_with_real_customer(AcceptableCmgCustomer cmgCustomer, AcceptableRealCustomer realCustomer)
            throws Exception {
        saveAssignmentForCustomer(cmgCustomer.getCustomerId(), realCustomer.getCustomerId(), realCustomer.getCustomerTypeCode(),
                getContractPriceProfileSeq());
    }

    @Given("^a request is made to save assignment for amendment for \"([^\"]*)\" with real customer \"([^\"]*)\"$")
    public void a_request_is_made_to_save_assignment_for_amendment_with_real_customer(AcceptableCmgCustomer cmgCustomer,
            AcceptableRealCustomer realCustomer) throws Exception {
        saveAssignmentForCustomer(cmgCustomer.getCustomerId(), realCustomer.getCustomerId(), realCustomer.getCustomerTypeCode(),
                getAmendmentContractPriceProfileSeq());
    }

    @Given("^a request is made to delete assignment for amendment for \"([^\"]*)\" with real customer \"([^\"]*)\"$")
    public void request_to_delete_customer_assignment(AcceptableCmgCustomer cmgCustomer, AcceptableRealCustomer realCustomer) {
        String realCustomerId = realCustomer.getCustomerId();
        int realCustomerType = realCustomer.getCustomerTypeCode();
        int contractPriceProfileSeq = getAmendmentContractPriceProfileSeq();
        String cmgCustomerId = cmgCustomer.getCustomerId();
        int cmgCustomerType = cmgCustomer.getCustomerTypeCode();
        ResponseEntity<Void> response = assignmentsController.deleteCustomerAssignment(realCustomerId, realCustomerType, contractPriceProfileSeq,
                cmgCustomerId, cmgCustomerType);
    }

    private void saveAssignmentForCustomer(String defaultCmgCustomerId, String realCustomerId, int realCustomerTypeIdCustomerUnit,
            int contractPriceProfileSeq) {
        try {

            CustomerAssignmentDTO saveAssignmentDTO = buildCustomerAssignmentDTO(defaultCmgCustomerId, realCustomerId, realCustomerTypeIdCustomerUnit,
                    contractPriceProfileSeq);
            ResponseEntity<StatusDTO> response = assignmentsController.saveAssignments(saveAssignmentDTO);
            setAttribute("saveAssignmentDTO", saveAssignmentDTO);
            setAttribute("save_assignment_response", response);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
            setAttribute(CukesConstants.CPP_ERROR_CODE, response.getStatusCodeValue());
            setAttribute(SAVE_CUSTOMER_RESPONSE_ATTRIBUTE, response.getBody());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
            setAttribute(CukesConstants.CPP_ERROR_MESSAGE, cpp.getMessage());
        }
    }

    private CustomerAssignmentDTO buildCustomerAssignmentDTO(String cmgCustomerId, String realCustomerId, int realCustomerType,
            int contractPriceProfileSeq) {
        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();

        realCustomerDTO.setRealCustomerId(realCustomerId);
        realCustomerDTO.setRealCustomerType(realCustomerType);

        realCustomerDTOList.add(realCustomerDTO);
        customerAssignmentDTO.setContractPriceProfileId(CukesConstants.CPP_ID);
        customerAssignmentDTO.setClmContractStartDate(cppDateUtils.getFutureDate());
        customerAssignmentDTO.setClmContractEndDate(cppDateUtils.getFutureDate());
        customerAssignmentDTO.setCmgCustomerId(cmgCustomerId);
        customerAssignmentDTO.setCmgCustomerType(CukesConstants.CMG_CUSTOMER_TYPE_ID);
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);
        return customerAssignmentDTO;
    }

}