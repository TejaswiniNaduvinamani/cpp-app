package com.gfs.cpp.acceptanceTests.stepdefs.findAssignments;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.common.data.AcceptableCmgCustomer;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableRealCustomer;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.acceptanceTests.mocks.CustomerGroupMembershipQueryServiceMocker;
import com.gfs.cpp.acceptanceTests.mocks.MemberHierarchyQueryMock;
import com.gfs.cpp.common.dto.assignments.CMGCustomerTypeDTO;
import com.gfs.cpp.common.dto.assignments.MarkupAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.web.controller.assignments.AssignmentController;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SuppressWarnings("unchecked")
public class FindAssignmentsStepDefs {

    private static final String FIND_CUSTOMER_RESPONSE_ATTRIBUTE = "findCustomerErrorType";

    @Autowired
    private AssignmentController assignmentsController;

    @Autowired
    private MemberHierarchyQueryMock memberHierarchyQueryMock;

    @Autowired
    private CustomerGroupMembershipQueryServiceMocker customerGroupMembershipQueryServiceMock;

    private static final Integer EXPIRE_IND = 1;

    @When("a request is made to get the default values of Pricing$")
    public void request_to_get_assignments() throws Exception {
        ResponseEntity<List<MarkupAssignmentDTO>> response = assignmentsController.fetchMarkup(getContractPriceProfileSeq());
        setAttribute("Markup", response.getBody());
    }

    @Then("^the Pricing value is returned$")
    public void return_assignments() {
        final List<MarkupAssignmentDTO> result = ((List<MarkupAssignmentDTO>) getAttribute("Markup"));

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getGfsCustomerId(), is(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
        assertThat(result.get(0).getMarkupName(), is(CukesConstants.CONTRACT_NAME));
        assertThat(result.get(0).getGfsCustomerType(), is(CukesConstants.MARKUP_TYPE_CMG));
        assertThat(result.get(0).getMarkupList().size(), is(1));
        assertThat(result.get(0).getMarkupList().get(0).getMarkup(), is(CukesConstants.MARKUP_VALUE));
        assertThat(result.get(0).getMarkupList().get(0).getItemPriceId(), is(CukesConstants.PRODUCT_PRICE_ID));
        assertThat(result.get(0).getMarkupList().get(0).getUnit(), is(CukesConstants.DEFAULT_UNIT));
        assertThat(result.get(0).getMarkupList().get(0).getMarkupType(), is(CukesConstants.PER_CASE_TYPE));
        assertThat(result.get(0).getMarkupList().get(0).getProductType(), is(CukesConstants.PRODUCT_TYPE));

        assertThat(result.get(0).getItemList().size(), is(2));
        assertThat(result.get(0).getItemList().get(0).getItemDesc(), is("Red Chilli"));
        assertThat(result.get(0).getItemList().get(0).getMarkup(), is(CukesConstants.MARKUP_VALUE));
        assertThat(result.get(0).getItemList().get(0).getUnit(), is(CukesConstants.DEFAULT_UNIT));
        assertThat(result.get(0).getExpireLowerInd(), is(EXPIRE_IND));
    }

    @When("a request is made to get the list of valid customer types$")
    public void request_to_get_customer_types() throws Exception {
        ResponseEntity<List<CMGCustomerTypeDTO>> response = assignmentsController.fetchCustomerTypes();
        setAttribute("customerTypes", response.getBody());
    }

    @Then("^the list of valid customer types is returned$")
    public void return_customer_types() {
        final List<CMGCustomerTypeDTO> result = ((List<CMGCustomerTypeDTO>) getAttribute("customerTypes"));
        assertThat(result.size(), is(11));
        for (CMGCustomerTypeDTO cmgCustomerTypeDTO : result) {
            assertThat(cmgCustomerTypeDTO.getCustomerTypeId().equals(31), is(false));
        }

        assertThat(result.get(0).getCustomerTypeValue(), is("Super Street Managed"));
    }

    @When("^a request is made to find the customer Pricing with valid customer id$")
    public void request_to_find_valid_customer() {
        customerGroupMembershipQueryServiceMock.mockFindMemberUnits();
        ResponseEntity<RealCustomerResponseDTO> response = assignmentsController.findCustomer(CukesConstants.REAL_CUSTOMER_ID,
                CukesConstants.REAL_CUSTOMER_TYPE_ID, getContractPriceProfileSeq(), CukesConstants.DEFAULT_CMG_CUSTOMER_ID,
                CukesConstants.CMG_CUSTOMER_TYPE_ID);
        setAttribute(FIND_CUSTOMER_RESPONSE_ATTRIBUTE, response.getBody());
    }

    @Then("^the valid customer name is returned$")
    public void return_valid_customer() {
        final RealCustomerResponseDTO result = (RealCustomerResponseDTO) getAttribute(FIND_CUSTOMER_RESPONSE_ATTRIBUTE);

        assertThat(result.getCustomerName(), is(CukesConstants.VALID_CUSTOMER_NAME));
    }

    @Then("^customer name of \"([^\"]*)\" is returned$")
    public void customer_name_of_is_returned(AcceptableRealCustomer realCustomer) throws Exception {

        final RealCustomerResponseDTO result = (RealCustomerResponseDTO) getAttribute(FIND_CUSTOMER_RESPONSE_ATTRIBUTE);

        assertThat(result.getCustomerName(), is(realCustomer.getCustomerId() + "-" + CukesConstants.CONTRACT_NAME));
    }

    @When("^a request is made to find the customer \"([^\"]*)\" to assign to \"([^\"]*)\"$")
    public void a_request_is_made_to_find_the_customer_to_assign_to(AcceptableRealCustomer realCustomer, AcceptableCmgCustomer cmgCustomer)
            throws Exception {
        customerGroupMembershipQueryServiceMock.mockFindMemberUnits();
        ResponseEntity<RealCustomerResponseDTO> response = assignmentsController.findCustomer(realCustomer.getCustomerId(),
                realCustomer.getCustomerTypeCode(), getContractPriceProfileSeq(), cmgCustomer.getCustomerId(), cmgCustomer.getCustomerTypeCode());
        setAttribute(FIND_CUSTOMER_RESPONSE_ATTRIBUTE, response.getBody());

    }

    @Then("^response received from find customer with validation error \"([^\"]*)\"$")
    public void response_received_from_find_customer_with_validation_error(CPPExceptionType exceptionType) throws Exception {
        RealCustomerResponseDTO realCustomerResponse = (RealCustomerResponseDTO) getAttribute(FIND_CUSTOMER_RESPONSE_ATTRIBUTE);
        assertThat(realCustomerResponse.getValidationCode(), equalTo(exceptionType.getErrorCode()));
    }

    @Given("^\"([^\"]*)\" is member of \"([^\"]*)\"$")
    public void is_member_of(AcceptableRealCustomer memberCustomer, AcceptableRealCustomer customer) throws Exception {
        memberHierarchyQueryMock.setMemberRelation(customer.getCustomerId(), customer.getCustomerTypeCode());
    }

    @Then("^Blank customer name is returned from find customer request$")
    public void blank_customer_name_is_returned_from_find_customer_request() throws Exception {
        RealCustomerResponseDTO realCustomerResponse = (RealCustomerResponseDTO) getAttribute(FIND_CUSTOMER_RESPONSE_ATTRIBUTE);
        assertThat(realCustomerResponse.getCustomerName(), equalTo(""));
    }

    @When("^a request is made to find the customer Pricing with valid customer id for new contract$")
    public void request_to_find_valid_customer_new_seq() {
        customerGroupMembershipQueryServiceMock.mockFindMemberUnits();
        ResponseEntity<RealCustomerResponseDTO> response = assignmentsController.findCustomer(CukesConstants.REAL_CUSTOMER_ID,
                CukesConstants.REAL_CUSTOMER_TYPE_ID, getContractPriceProfileSeq(), CukesConstants.DEFAULT_CMG_CUSTOMER_ID,
                CukesConstants.CMG_CUSTOMER_TYPE_ID);
        setAttribute(FIND_CUSTOMER_RESPONSE_ATTRIBUTE, response.getBody());
    }

    @Then("^customer already mapped to active contract exception is thrown$")
    public void return_customer_already_mapped_to_active_contract_exception() {
        RealCustomerResponseDTO realCustomerResponseDTO = (RealCustomerResponseDTO) getAttribute(FIND_CUSTOMER_RESPONSE_ATTRIBUTE);

        assertThat(realCustomerResponseDTO.getValidationMessage(),
                is("{\"conceptName\":\"contract name for cucumber test\",\"contractPriceProfileId\":\"999999998\"}"));
        assertThat(realCustomerResponseDTO.getValidationCode(), is(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT.getErrorCode()));
    }

}
