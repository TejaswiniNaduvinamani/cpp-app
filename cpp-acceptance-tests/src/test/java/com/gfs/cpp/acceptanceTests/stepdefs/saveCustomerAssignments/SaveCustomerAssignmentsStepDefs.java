package com.gfs.cpp.acceptanceTests.stepdefs.saveCustomerAssignments;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getExistingContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.common.data.AcceptableCmgCustomer;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableRealCustomer;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.acceptanceTests.mocks.CustomerGroupMembershipQueryServiceMocker;
import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.MarkupAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.web.controller.assignments.AssignmentController;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SuppressWarnings("unchecked")
public class SaveCustomerAssignmentsStepDefs {

    private static final String SAVE_CUSTOMER_RESPONSE_ATTRIBUTE = "saveCustomerErrorType";

    @Autowired
    private AssignmentController assignmentsController;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private CustomerGroupMembershipQueryServiceMocker customerGroupMembershipQueryServiceMock;

    @When("^a request is made to save assignment with real customer$")
    public void save_assignment_real_customer() throws Exception {
        customerGroupMembershipQueryServiceMock.mockFindMemberUnits();
        saveAssignmentForCustomer(CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.REAL_CUSTOMER_ID, CukesConstants.REAL_CUSTOMER_TYPE_ID);
    }

    @Then("^a real customer is assigned to markup$")
    public void return_assignment_real_customer() {

        List<MarkupAssignmentDTO> savedAssignmentList = assignmentsController.fetchMarkup(getContractPriceProfileSeq()).getBody();

        assertThat(savedAssignmentList.size(), is(1));

        assertThat(savedAssignmentList.get(0).getRealCustomerDTOList().size(), is(1));
        assertThat(savedAssignmentList.get(0).getIsAssignmentSaved(), is(true));
        assertThat(savedAssignmentList.get(0).getRealCustomerDTOList().get(0).getRealCustomerId(), is(CukesConstants.REAL_CUSTOMER_ID));
        assertThat(savedAssignmentList.get(0).getRealCustomerDTOList().get(0).getRealCustomerType(), is(CukesConstants.REAL_CUSTOMER_TYPE_ID));
        assertThat(savedAssignmentList.get(0).getRealCustomerDTOList().get(0).getIsCustomerSaved(), is(true));
    }

    @Then("^a real customer \"([^\"]*)\" is assigned to markup \"([^\"]*)\"$")
    public void a_real_customer_is_assigned_to_markup(AcceptableRealCustomer realCustomer, AcceptableCmgCustomer cmgCustomer) throws Exception {

        boolean assigned = false;
        customerGroupMembershipQueryServiceMock.mockFindMemberUnits();

        List<MarkupAssignmentDTO> savedAssignmentList = assignmentsController.fetchMarkup(getContractPriceProfileSeq()).getBody();

        for (MarkupAssignmentDTO markupAssignmentDTO : savedAssignmentList) {
            if (cmgCustomer.getCustomerId().equals(markupAssignmentDTO.getGfsCustomerId())) {
                for (RealCustomerDTO realCustomerDTO : markupAssignmentDTO.getRealCustomerDTOList()) {
                    if (realCustomer.getCustomerId().equals(realCustomerDTO.getRealCustomerId())) {
                        assigned = true;
                        assertThat(realCustomer.getCustomerTypeCode(), equalTo(realCustomerDTO.getRealCustomerType()));
                        assertThat(realCustomerDTO.getIsCustomerSaved(), equalTo(true));
                    }
                }
            }
        }

        assertThat(assigned, equalTo(true));
    }

    @Then("^response received from save assignment with validation error \"([^\"]*)\"$")
    public void response_received_from_save_assignment_with_validation_error(CPPExceptionType exceptionType) throws Exception {
        StatusDTO statusDTO = (StatusDTO) getAttribute(SAVE_CUSTOMER_RESPONSE_ATTRIBUTE);
        assertThat(statusDTO.getErrorCode(), equalTo(exceptionType.getErrorCode()));
    }

    @When("^a request is made to save assignment with real customer with param (.+) and (.+)$")
    public void save_assignment_real_customer(String receievdEffectiveDate, String receievdExpirationDate) throws Exception {
        Date effectiveDate = convertToDate(receievdEffectiveDate);
        Date expirationDate = convertToDate(receievdExpirationDate);
        CustomerAssignmentDTO saveAssignmentDTO = buildCustomerAssignmentDTO(CukesConstants.DEFAULT_CMG_CUSTOMER_ID, effectiveDate, expirationDate,
                CukesConstants.EXISTING_CPP_ID, getExistingContractPriceProfileSeq());

        ResponseEntity<StatusDTO> statusDTO = assignmentsController.saveAssignments(saveAssignmentDTO);

        setAttribute("statusDTO", statusDTO);
        setAttribute("saveAssignmentDTO", saveAssignmentDTO);
    }

    @When("^a request is made to save assignment with real customer (.+) and (.+) for contract$")
    public void save_assignment_real_customer_with_dates(String receievdEffectiveDate, String receievdExpirationDate) throws Exception {
        Date effectiveDate = convertToDate(receievdEffectiveDate);
        Date expirationDate = convertToDate(receievdExpirationDate);
        CustomerAssignmentDTO saveAssignmentDTO = buildCustomerAssignmentDTO(CukesConstants.DEFAULT_CMG_CUSTOMER_ID, effectiveDate, expirationDate,
                CukesConstants.CPP_ID, getContractPriceProfileSeq());
        try {
            ResponseEntity<StatusDTO> response = assignmentsController.saveAssignments(saveAssignmentDTO);

            setAttribute("saveAssignmentDTO", saveAssignmentDTO);
            setAttribute("save_assignment_response", response);
            setAttribute(CukesConstants.CPP_ERROR_CODE, response.getStatusCodeValue());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_CODE, cpp.getErrorCode());
            setAttribute(CukesConstants.CPP_ERROR_MESSAGE, cpp.getMessage());
        }
    }

    @Then("^customer already mapped to other contract exception is thrown for save assignment$")
    public void return_customer_already_mapped_validation_for_save() {
        final ResponseEntity<StatusDTO> response = (ResponseEntity<StatusDTO>) getAttribute("save_assignment_response");
        StatusDTO statusDTO = response.getBody();

        assertThat(statusDTO.getErrorMessage(), is("(Contract Management Group 2014)"));
        assertThat(statusDTO.getErrorCode(), is(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT.getErrorCode()));
    }

    @When("^a request is made to fetch assigned real customer list$")
    public void fetch_assignment_real_customer() {

        List<MarkupAssignmentDTO> fetchAssignmentList = assignmentsController.fetchMarkup(getContractPriceProfileSeq()).getBody();
        setAttribute("fetchAssignmentList", fetchAssignmentList);
    }

    @Then("^the assigned real customer list is returned$")
    public void return_assignment_real_customer_list() {

        CustomerAssignmentDTO saveAssignmentDTO = (CustomerAssignmentDTO) getAttribute("saveAssignmentDTO");
        List<MarkupAssignmentDTO> fetchAssignmentList = (List<MarkupAssignmentDTO>) getAttribute("fetchAssignmentList");

        assertThat(fetchAssignmentList.get(0).getRealCustomerDTOList().size(), is(1));
        assertThat(fetchAssignmentList.get(0).getRealCustomerDTOList().get(0).getRealCustomerId(),
                is(saveAssignmentDTO.getRealCustomerDTOList().get(0).getRealCustomerId()));
        assertThat(fetchAssignmentList.get(0).getRealCustomerDTOList().get(0).getRealCustomerType(),
                is(saveAssignmentDTO.getRealCustomerDTOList().get(0).getRealCustomerType()));
    }

    @Then("^a request is made to assign customer in exception concept$")
    public void request_to_assign_customer_in_exception() {
        CustomerAssignmentDTO saveAssignmentDTO = buildCustomerAssignmentDTO(CukesConstants.EXCEPTION_CUSTOMER_ID,
                CukesConstants.REAL_CUSTOMER_ID_EXCEPTION, CukesConstants.REAL_CUSTOMER_TYPE_ID_EXCEPTION);
        try {
            assignmentsController.saveAssignments(saveAssignmentDTO);
            setAttribute("saveAssignmentDTO", saveAssignmentDTO);

        } catch (CPPRuntimeException cpp) {
            setAttribute("validationCode", cpp.getErrorCode());
            setAttribute("validationMessage", cpp.getMessage());
        }
    }

    @When("^a request is made to delete customer mapping assigned with default concept$")
    public void request_to_delete_customer() throws Exception {
        try {
            ResponseEntity<Void> response = assignmentsController.deleteCustomerAssignment(CukesConstants.REAL_CUSTOMER_ID,
                    CukesConstants.REAL_CUSTOMER_TYPE_ID, getContractPriceProfileSeq(), CukesConstants.DEFAULT_CMG_CUSTOMER_ID,
                    CukesConstants.CMG_CUSTOMER_TYPE_ID);
            setAttribute("deleteResponse", response);
            setAttribute(CukesConstants.CPP_ERROR_CODE, response.getStatusCodeValue());
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
            setAttribute(CukesConstants.CPP_ERROR_CODE, cpp.getErrorCode());
        }
    }

    @Then("^all the customer mappings assigned with all concepts are deleted$")
    public void check_assigned_customers_deleted() {
        ResponseEntity<Void> response = (ResponseEntity<Void>) getAttribute("deleteResponse");
        List<MarkupAssignmentDTO> fetchUpdatedAssignmentList = assignmentsController.fetchMarkup(getContractPriceProfileSeq()).getBody();

        assertThat(response.getStatusCodeValue(), is(200));

        for (MarkupAssignmentDTO updatedAssignmentList : fetchUpdatedAssignmentList) {
            for (RealCustomerDTO realCustomerDTO : updatedAssignmentList.getRealCustomerDTOList()) {
                assertThat(realCustomerDTO.getRealCustomerId(), is(""));
                assertThat(realCustomerDTO.getRealCustomerType(), is(-1));
                assertThat(realCustomerDTO.getRealCustomerName(), is(""));
            }
        }
    }

    @When("^a request is made to delete customer mapping assigned with exception concept$")
    public void request_to_delete_customer_assigned_with_exception_concept() throws Exception {
        ResponseEntity<Void> response = assignmentsController.deleteCustomerAssignment(CukesConstants.REAL_CUSTOMER_ID_EXCEPTION,
                CukesConstants.REAL_CUSTOMER_TYPE_ID_EXCEPTION, getContractPriceProfileSeq(), CukesConstants.EXCEPTION_CUSTOMER_ID,
                CukesConstants.CMG_CUSTOMER_TYPE_ID);
        setAttribute("deleteResponse", response);
    }

    @Then("^the customer mapping assigned with exception concept is deleted$")
    public void check_assigned_customer_with_exception_deleted() {
        ResponseEntity<Void> response = (ResponseEntity<Void>) getAttribute("deleteResponse");
        List<MarkupAssignmentDTO> fetchUpdatedAssignmentList = assignmentsController.fetchMarkup(getContractPriceProfileSeq()).getBody();

        assertThat(response.getStatusCodeValue(), is(200));

        List<RealCustomerDTO> realCustomerDTOListForDefault = fetchUpdatedAssignmentList.get(0).getRealCustomerDTOList();
        for (RealCustomerDTO realCustomerDTO : realCustomerDTOListForDefault) {
            assertThat(realCustomerDTO.getRealCustomerId(), is(CukesConstants.REAL_CUSTOMER_ID));
            assertThat(realCustomerDTO.getRealCustomerType(), is(CukesConstants.REAL_CUSTOMER_TYPE_ID));
            assertThat(realCustomerDTO.getRealCustomerName(), is(CukesConstants.VALID_CUSTOMER_NAME));
        }

        List<RealCustomerDTO> realCustomerDTOListForException = fetchUpdatedAssignmentList.get(1).getRealCustomerDTOList();
        for (RealCustomerDTO realCustomerDTO : realCustomerDTOListForException) {
            assertThat(realCustomerDTO.getRealCustomerId(), is(""));
            assertThat(realCustomerDTO.getRealCustomerType(), is(-1));
            assertThat(realCustomerDTO.getRealCustomerName(), is(""));
        }
    }

    private void saveAssignmentForCustomer(String defaultCmgCustomerId, String realCustomerId, int realCustomerTypeIdCustomerUnit) {
        try {

            CustomerAssignmentDTO saveAssignmentDTO = buildCustomerAssignmentDTO(defaultCmgCustomerId, realCustomerId,
                    realCustomerTypeIdCustomerUnit);

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

    private CustomerAssignmentDTO buildCustomerAssignmentDTO(String cmgCustomerId, Date effectiveDate, Date expirationDate, int cppId,
            int contractPriceProfileSeq) {

        return buildCustomerAssignmentDTO(cmgCustomerId, effectiveDate, expirationDate, cppId, contractPriceProfileSeq,
                CukesConstants.REAL_CUSTOMER_ID, CukesConstants.REAL_CUSTOMER_TYPE_ID);
    }

    private CustomerAssignmentDTO buildCustomerAssignmentDTO(String cmgCustomerId, Date effectiveDate, Date expirationDate, int cppId,
            int contractPriceProfileSeq, String realCustomerId, int realCustomerTypeCode) {
        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();

        realCustomerDTO.setRealCustomerId(realCustomerId);
        realCustomerDTO.setRealCustomerType(realCustomerTypeCode);

        realCustomerDTOList.add(realCustomerDTO);
        customerAssignmentDTO.setContractPriceProfileId(cppId);
        customerAssignmentDTO.setClmContractStartDate(effectiveDate);
        customerAssignmentDTO.setClmContractEndDate(expirationDate);
        customerAssignmentDTO.setCmgCustomerId(cmgCustomerId);
        customerAssignmentDTO.setCmgCustomerType(CukesConstants.CMG_CUSTOMER_TYPE_ID);
        customerAssignmentDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);
        return customerAssignmentDTO;
    }

    private CustomerAssignmentDTO buildCustomerAssignmentDTO(String cmgCustomerId, String realCustomerId, int realCustomerType) {

        return buildCustomerAssignmentDTO(cmgCustomerId, cppDateUtils.getFutureDate(), cppDateUtils.getFutureDate(), CukesConstants.CPP_ID,
                getContractPriceProfileSeq(), realCustomerId, realCustomerType);
    }

    private Date convertToDate(String date) {
        Date returnDate = null;
        String[] days = date.split(" ");
        if (date.contains(CukesConstants.DAYS_FROM_NOW)) {
            returnDate = cppDateUtils.daysBeyondCurrentDate(Integer.parseInt(days[0]));
        } else {
            returnDate = cppDateUtils.daysPreviousCurrentDate(Integer.parseInt(days[0]));
        }

        return returnDate;
    }
}
