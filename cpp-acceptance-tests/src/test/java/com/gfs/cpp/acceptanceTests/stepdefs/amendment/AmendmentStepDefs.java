package com.gfs.cpp.acceptanceTests.stepdefs.amendment;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAmendmentContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.dto.clm.ClmContractDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.web.controller.customerinfo.CPPInformationController;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AmendmentStepDefs {

    @Autowired
    private CPPInformationController cppInformationController;

    @Autowired
    private ValidateAmendmentContract validateAmendmentContract;

    @Then("^request is sent to launch the amendment in CPP$")
    public void invoke_amendment_process() {
        launchAmedment(CukesConstants.AMENDMENT_AGREEMENT_ID);
    }

    private void launchAmedment(String agreementId) {
        try {
            ResponseEntity<ClmContractDTO> result = cppInformationController.fetchContractPriceProfileInfo(agreementId,
                    CukesConstants.CLM_CONTRACT_TYPE_REGIONAL_AMENDMENT);
            setAmendmentContractPriceProfileSeq(agreementId, result.getBody().getCppInformationDto().getContractPriceProfileSeq());
            setAttribute("AmendmentSuccessfulResponse", result.getBody());
        } catch (CPPRuntimeException cppre) {
            setAttribute("AmendmentUnSuccessfulResponse", cppre.getErrorCode());
        }
    }

    @When("^request is sent to launch the amendment \"([^\"]*)\" in CPP$")
    public void request_is_sent_to_launch_the_amendment_in_CPP(String agreementId) throws Exception {
        launchAmedment(agreementId);
    }

    @Then("^an exception is returned of type (.+)$")
    public void validate_exception_type_in_response(CPPExceptionType exceptionType) {
        final Integer unSuccessfulResult = (Integer) getAttribute("AmendmentUnSuccessfulResponse");
        if (exceptionType.equals(CPPExceptionType.IN_PROGREESS_VERSION_FOUND)) {
            assertThat(unSuccessfulResult, equalTo(CPPExceptionType.IN_PROGREESS_VERSION_FOUND.getErrorCode()));
        } else if (exceptionType.equals(CPPExceptionType.NEW_CONTRACT_INVALID_STATUS)) {
            assertThat(unSuccessfulResult, equalTo(CPPExceptionType.NEW_CONTRACT_INVALID_STATUS.getErrorCode()));
        } else if (exceptionType.equals(CPPExceptionType.IN_PROGRESS_FURTHERANCE_FOUND)) {
            assertThat(unSuccessfulResult, equalTo(CPPExceptionType.IN_PROGRESS_FURTHERANCE_FOUND.getErrorCode()));
        } else if (exceptionType.equals(CPPExceptionType.INVALID_AMENDMENT_EFFECTIVE_DATE)) {
            assertThat(unSuccessfulResult, equalTo(CPPExceptionType.INVALID_AMENDMENT_EFFECTIVE_DATE.getErrorCode()));
        }
    }

    @Then("^amendment created successfully$")
    public void validate_amendment_response() {
        final ClmContractDTO successfulResult = (ClmContractDTO) getAttribute("AmendmentSuccessfulResponse");
        assertThat(successfulResult.getAgreementId(), equalTo(CukesConstants.AMENDMENT_AGREEMENT_ID));
    }

    @Then("^all the contract data of previous active version is copied$")
    public void validate_entries_in_CPP_PRC_Tables() {
        final ClmContractDTO successfulResult = (ClmContractDTO) getAttribute("AmendmentSuccessfulResponse");
        validateAmendmentContract.validateAmendmentContractEntries(successfulResult);
    }

}
