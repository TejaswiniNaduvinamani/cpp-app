package com.gfs.cpp.acceptanceTests.stepdefs.authorization;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.acceptanceTests.mocks.CppUserDetailsServiceMocker;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.service.authorization.AuthorizationDetailsDTO;
import com.gfs.cpp.web.controller.authorization.AuthorizationController;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AuthorizationStepDefs {

    private static final String AUTHROIZATION_DETAILS_KEY = "authroizationDetails";

    @Autowired
    private CppUserDetailsServiceMocker cppUserDetailsServiceMocker;

    @Autowired
    private AuthorizationController authorizationController;

    @When("^the user is logged in as \"(.*?)\" User$")
    public void the_user_is_logged_in_as_User(String role) throws Throwable {
        cppUserDetailsServiceMocker.setRoleName(role);
    }

    @When("^the request is made to get authorization details$")
    public void the_request_is_made_to_get_authorization_details() throws Throwable {

        ResponseEntity<AuthorizationDetailsDTO> fetchAuthorizationDetails = authorizationController
                .fetchAuthorizationDetails(getContractPriceProfileSeq(), false, ClmContractStatus.DRAFT.value);

        setAttribute(AUTHROIZATION_DETAILS_KEY, fetchAuthorizationDetails.getBody());

    }

    @When("^the request is made to get authorization details for amendment with clm status \"([^\"]*)\"$")
    public void the_request_is_made_to_get_authorization_details_for_amendment_with_clm_status(ClmContractStatus clmStatus) throws Exception {
        ResponseEntity<AuthorizationDetailsDTO> fetchAuthorizationDetails = authorizationController
                .fetchAuthorizationDetails(getContractPriceProfileSeq(), true, clmStatus.value);

        setAttribute(AUTHROIZATION_DETAILS_KEY, fetchAuthorizationDetails.getBody());
    }

    @Then("^the cpp status in authorization details returned as \"([^\"]*)\"$")
    public void the_cpp_status_in_authorization_details_returned_as(String statusDesc) throws Exception {
        AuthorizationDetailsDTO authorizationDetails = (AuthorizationDetailsDTO) getAttribute(AUTHROIZATION_DETAILS_KEY);
        assertThat(authorizationDetails.getCppStatus(), equalTo(statusDesc));
    }

    @Then("^price profile editable is returned as \"(true|false)\" in authorization details$")
    public void price_profile_editable_is_returned_as_in_authorization_details(boolean isEditable) throws Throwable {
        AuthorizationDetailsDTO authorizationDetails = (AuthorizationDetailsDTO) getAttribute(AUTHROIZATION_DETAILS_KEY);
        assertThat(authorizationDetails.isPriceProfileEditable(), equalTo(isEditable));
    }

    @Then("^customer assignment editable is returned as \"(true|false)\" in authorization details$")
    public void customer_assignment_editable_is_returned_as_in_authorization_details(boolean isEditable) throws Throwable {
        AuthorizationDetailsDTO authorizationDetails = (AuthorizationDetailsDTO) getAttribute(AUTHROIZATION_DETAILS_KEY);
        assertThat(authorizationDetails.isCustomerAssignmentEditable(), equalTo(isEditable));
    }

    @Then("^item assignment editable is returned as \"(true|false)\" in authorization details$")
    public void item_assignment_editable_is_returned_as_in_authorization_details(boolean isEditable) throws Throwable {
        AuthorizationDetailsDTO authorizationDetails = (AuthorizationDetailsDTO) getAttribute(AUTHROIZATION_DETAILS_KEY);
        assertThat(authorizationDetails.isItemAssignmentEditable(), equalTo(isEditable));
    }

    @Then("^cost model editable is returned as \"(true|false)\" in authorization details$")
    public void cost_model_editable_is_returned_as_in_authorization_details(boolean isEditable) throws Exception {
        AuthorizationDetailsDTO authorizationDetails = (AuthorizationDetailsDTO) getAttribute(AUTHROIZATION_DETAILS_KEY);
        assertThat(authorizationDetails.isItemAssignmentEditable(), equalTo(isEditable));
    }

    @When("^the request is made to check if furtherance is editable$")
    public void the_request_is_made_to_check_if_furtherance_is_editable() throws Exception {
        ResponseEntity<Map<String, Boolean>> isEditableResponse = authorizationController.canEditFurtherance(CukesConstants.CPP_FURTHERANCE_SEQ);

        setAttribute("isFurtheranceEditable", isEditableResponse.getBody());
    }

    @Then("^furtherance editable is returned as \"(true|false)\" in authorization details$")
    public void furtherance_editable_is_returned_as_in_authorization_details(boolean isEditable) throws Exception {
        Map<String, Boolean> isFurtheranceEditable = (Map<String, Boolean>) getAttribute("isFurtheranceEditable");

        assertThat(isFurtheranceEditable.get("canEditFurtherance"), equalTo(isEditable));

    }

    @When("^request is made to if user is authorized to view only$")
    public void request_is_made_to_if_user_is_authorized_to_view_only() throws Exception {
        ResponseEntity<Map<String, Boolean>> isEditableResponse = authorizationController.isUserAuthorizedToViewOnly();

        setAttribute("isUserAuthorizedToViewOnlyResponse", isEditableResponse.getBody());
    }

    @Then("^the authorized to view only return as \"(true|false)\"$")
    public void the_authorized_to_view_only_return_as(boolean isEditable) throws Exception {

        Map<String, Boolean> isUserAuthorizedToViewOnlyResponse = (Map<String, Boolean>) getAttribute("isUserAuthorizedToViewOnlyResponse");

        assertThat(isUserAuthorizedToViewOnlyResponse.get("isUserAuthorizedToViewOnly"), equalTo(isEditable));
    }

}
