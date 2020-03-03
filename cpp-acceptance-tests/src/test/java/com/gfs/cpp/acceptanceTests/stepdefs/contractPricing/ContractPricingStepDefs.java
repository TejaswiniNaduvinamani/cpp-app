package com.gfs.cpp.acceptanceTests.stepdefs.contractPricing;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setContractPriceProfileSeq;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.acceptanceTests.mocks.ClmApiProxyMocker;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.clm.ClmContractDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.web.controller.contractpricing.ContractPricingController;
import com.gfs.cpp.web.controller.customerinfo.CPPInformationController;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContractPricingStepDefs {

    private static final String CONTRACT_INFORMATION_RESPONSE = "ContractInformationResponse";

    @Autowired
    private ContractPricingController contractPricingController;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private ClmApiProxyMocker clmApiProxyMocker;

    @Autowired
    private CPPInformationController cppInformationController;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Given("^request is made to save the contract price$")
    public void in_cpp_application_markup() throws Exception {

        Integer currentContractPriceSeq = createContract(CukesConstants.EFFECTIVE_DATE, cppDateUtils.getFutureDate(),
                Integer.valueOf(CukesConstants.CPP_ID));
        setContractPriceProfileSeq(currentContractPriceSeq);

    }

    private Integer createContract(Date effectiveDate, Date expirationDate, int cppProfileId) {
        ContractPricingDTO contractPricingDTO = new ContractPricingDTO();
        contractPricingDTO.setVersionNbr(CukesConstants.VERSION_NBR);
        contractPricingDTO.setContractName(CukesConstants.CONTRACT_NAME);
        contractPricingDTO.setContractPriceProfileId(cppProfileId);
        contractPricingDTO.setContractPriceProfileSeq(contractPriceProfileRepository.fetchCPPNextSequence());
        contractPricingDTO.setPricingEffectiveDate(effectiveDate);
        contractPricingDTO.setClmContractStartDate(CukesConstants.CLM_CONTRACT_START_DATE);
        contractPricingDTO.setClmContractEndDate(expirationDate);
        contractPricingDTO.setPricingExpirationDate(cppDateUtils.getFutureDate());
        contractPricingDTO.setPriceAuditFlag(true);
        contractPricingDTO.setPriceVerificationFlag(true);
        contractPricingDTO.setAssessmentFeeFlag(true);
        contractPricingDTO.setTransferFeeFlag(true);
        contractPricingDTO.setScheduleForCostChange(CukesConstants.SCHEDULE_FOR_COST_GREGORIAN);
        contractPricingDTO.setContractType(CukesConstants.CLM_CONTRACT_TYPE_REGIIONAL);
        contractPricingDTO.setAgreementId(CukesConstants.AGREEMENT_ID);
        contractPricingDTO.setParentAgreementId(CukesConstants.PARENT_AGREEMENT_ID);

        setAttribute("contractPricingDTO", contractPricingDTO);

        ResponseEntity<Map<String, Integer>> saveContractPricing = contractPricingController.saveContractPricing(contractPricingDTO);
        setAttribute("response", saveContractPricing);

        ResponseEntity<Map<String, Integer>> fetchCPPSequenceResponse = contractPricingController.fetchCPPSequence(Integer.valueOf(cppProfileId));
        return fetchCPPSequenceResponse.getBody().get(CPPConstants.CONTRACT_PRICE_PROFILE_SEQ);

    }

    @When("^a request is made to get the default values of contract princing information$")
    public void request_to_get_default_contract_values() throws Exception {
        ClmContractDTO clmContractDTO = (ClmContractDTO) getAttribute(CONTRACT_INFORMATION_RESPONSE);
        ResponseEntity<ContractPricingDTO> response;
        if (clmContractDTO != null) {
            response = contractPricingController.fetchPricingInformation(CukesConstants.CONTRACT_PRICE_PROFILE_SEQ, clmContractDTO.getAgreementId(),
                    clmContractDTO.getContractStatus());
        } else {
            response = contractPricingController.fetchPricingInformation(CukesConstants.CONTRACT_PRICE_PROFILE_SEQ, CukesConstants.AGREEMENT_ID,
                    "status");
        }
        setAttribute("contractPricing", response.getBody());
    }

    @Then("^the default values of contract princing information is returned$")
    public void return_to_get_default_contract_values() throws Exception {
        ContractPricingDTO contractPricingDTO = (ContractPricingDTO) getAttribute("contractPricing");
        assert (contractPricingDTO != null);
        assertThat(contractPricingDTO.getAssessmentFeeFlag(), is(Boolean.FALSE));
        assertThat(contractPricingDTO.getPriceAuditFlag(), is(Boolean.FALSE));
        assertThat(contractPricingDTO.getContractPriceProfileSeq(), is(CukesConstants.CPP_ID));
        assertThat(contractPricingDTO.getScheduleForCostChange(), is(CukesConstants.GFS_FISCAL));
        assertThat(contractPricingDTO.getPriceVerificationFlag(), is(Boolean.FALSE));
        assertThat(contractPricingDTO.getTransferFeeFlag(), is(Boolean.FALSE));
        assertThat(contractPricingDTO.getPricingExpirationDate(), is(cppDateUtils.getFutureDate()));
    }

    @SuppressWarnings("unchecked")
    @Then("^the values of contract pricing are saved$")
    public void save_pricing_info() {
        final ResponseEntity<Map<String, Integer>> response = (ResponseEntity<Map<String, Integer>>) getAttribute("response");
        assertThat(response.getStatusCodeValue(), is(200));

        final ContractPricingDTO contractPricingDTO = (ContractPricingDTO) getAttribute("contractPricingDTO");

        final ResponseEntity<ContractPricingDTO> saved_pricing = contractPricingController.fetchPricingInformation(getContractPriceProfileSeq(),
                CukesConstants.AGREEMENT_ID, "Draft");

        ContractPricingDTO savedContractPricing = saved_pricing.getBody();

        assertThat(savedContractPricing.getClmContractStartDate(), is(contractPricingDTO.getClmContractStartDate()));
        assertThat(savedContractPricing.getClmContractEndDate(), is(contractPricingDTO.getClmContractEndDate()));
        assertThat(savedContractPricing.getAssessmentFeeFlag(), is(contractPricingDTO.getAssessmentFeeFlag()));
        assertThat(savedContractPricing.getPriceAuditFlag(), is(contractPricingDTO.getPriceAuditFlag()));
        assertThat(savedContractPricing.getPriceVerificationFlag(), is(contractPricingDTO.getPriceVerificationFlag()));
        assertThat(savedContractPricing.getPricingEffectiveDate(), is(contractPricingDTO.getPricingEffectiveDate()));
        assertThat(savedContractPricing.getPricingExpirationDate(), is(contractPricingDTO.getPricingExpirationDate()));
        assertThat(savedContractPricing.getScheduleForCostChange(), is(contractPricingDTO.getScheduleForCostChange()));
        assertThat(savedContractPricing.getTransferFeeFlag(), is(contractPricingDTO.getTransferFeeFlag()));
    }

    @When("^a request is made to get the existing values of contract princing information$")
    public void request_to_get_saved_contract() throws Exception {
        ClmContractDTO clmContractDTO = (ClmContractDTO) getAttribute(CONTRACT_INFORMATION_RESPONSE);
        final ResponseEntity<ContractPricingDTO> saved_pricing;
        if (clmContractDTO != null) {
            saved_pricing = contractPricingController.fetchPricingInformation(getContractPriceProfileSeq(), clmContractDTO.getAgreementId(),
                    clmContractDTO.getContractStatus());
        } else {
            saved_pricing = contractPricingController.fetchPricingInformation(getContractPriceProfileSeq(), CukesConstants.AGREEMENT_ID, "Draft");
        }
        setAttribute("saved_pricing", saved_pricing);
    }

    @SuppressWarnings("unchecked")
    @Then("^the existing values of contract princing information is returned$")
    public void return_saved_contract() {
        final ResponseEntity<ContractPricingDTO> saved_pricing = (ResponseEntity<ContractPricingDTO>) getAttribute("saved_pricing");
        assertThat(saved_pricing.getBody().getAssessmentFeeFlag(), is(Boolean.TRUE));
        assertThat(saved_pricing.getBody().getPriceAuditFlag(), is(Boolean.TRUE));
        assertThat(saved_pricing.getBody().getPriceVerificationFlag(), is(Boolean.TRUE));
        assertThat(saved_pricing.getBody().getPricingEffectiveDate(), is(CukesConstants.EFFECTIVE_DATE));
        assertThat(saved_pricing.getBody().getPricingExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(saved_pricing.getBody().getScheduleForCostChange(), is(CukesConstants.SCHEDULE_FOR_COST_GREGORIAN));
        assertThat(saved_pricing.getBody().getTransferFeeFlag(), is(Boolean.TRUE));
    }

    @When("^a request is made to update the saved values of contract princing information$")
    public void update_saved_contract() throws Exception {
        try {
            final ContractPricingDTO updatedPricing = new ContractPricingDTO();

            updatedPricing.setContractPriceProfileSeq(getContractPriceProfileSeq());
            updatedPricing.setAssessmentFeeFlag(Boolean.FALSE);
            updatedPricing.setContractName(CukesConstants.CONTRACT_NAME);
            updatedPricing.setContractPriceProfileId(CukesConstants.CPP_ID);
            updatedPricing.setContractType(CukesConstants.CLM_CONTRACT_TYPE_STREET);
            updatedPricing.setPriceAuditFlag(Boolean.FALSE);
            updatedPricing.setPriceVerificationFlag(Boolean.FALSE);
            updatedPricing.setPricingEffectiveDate(cppDateUtils.getFutureDate());
            updatedPricing.setPricingExpirationDate(cppDateUtils.getFutureDate());
            updatedPricing.setScheduleForCostChange(CukesConstants.GFS_FISCAL);
            updatedPricing.setTransferFeeFlag(Boolean.FALSE);

            ResponseEntity<Map<String, Integer>> saveContractPricing = contractPricingController.saveContractPricing(updatedPricing);
            setAttribute("update_contractPricing_response", saveContractPricing);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, saveContractPricing.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @Then("^the existing values of contract princing information is updated$")
    public void contract_values_are_updated() {
        final ResponseEntity<ContractPricingDTO> saved_pricing = contractPricingController.fetchPricingInformation(getContractPriceProfileSeq(),
                CukesConstants.AGREEMENT_ID, "Draft");

        assertThat(saved_pricing.getBody().getContractPriceProfileSeq(), is(getContractPriceProfileSeq()));
        assertThat(saved_pricing.getBody().getAssessmentFeeFlag(), is(Boolean.FALSE));
        assertThat(saved_pricing.getBody().getPriceAuditFlag(), is(Boolean.FALSE));
        assertThat(saved_pricing.getBody().getPriceVerificationFlag(), is(Boolean.FALSE));
        assertThat(saved_pricing.getBody().getPricingEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(saved_pricing.getBody().getPricingExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(saved_pricing.getBody().getScheduleForCostChange(), is(CukesConstants.GFS_FISCAL));
        assertThat(saved_pricing.getBody().getTransferFeeFlag(), is(Boolean.FALSE));

    }

    public Integer in_cpp_application_markup_with_date_range(Date effectiveDate, Date expirationDate, int cppProfileId) throws Exception {
        return createContract(effectiveDate, expirationDate, cppProfileId);
    }

    @Given("^request is made to save the contract price for new contract$")
    public void in_cpp_application_contract() throws Exception {

        createContract(CukesConstants.EFFECTIVE_DATE, cppDateUtils.getFutureDate(), Integer.valueOf(CukesConstants.CPP_ID) - 1);

    }

    @When("^a request is made to delete pricing exhibit from clm$")
    public void delete_pricing_exhibit_from_clm() {
        try {
            ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository
                    .fetchContractDetailsByAgreementId(CukesConstants.AGREEMENT_ID);
            ResponseEntity<Void> response = contractPricingController.deletePricingExhibit(CukesConstants.AGREEMENT_ID);
            setAttribute("response", response);
            setAttribute("exhibitSysId", contractPricingResponseDTO.getPricingExhibitSysId());
        } catch (CPPRuntimeException cppre) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cppre.getType());
        }

    }

    @Then("^the pricing exhibit is deleted from clm$")
    public void verify_pricing_exhibit_deleted() {
        clmApiProxyMocker.isAssociatedDocumentDeleted();
    }

    @Then("^exception should be thrown as contract is not editable$")
    public void verify_exception_is_thrown() {
        assertThat((CPPExceptionType) getAttribute(CukesConstants.CPP_ERROR_TYPE), is(CPPExceptionType.NOT_VALID_STATUS));
        assertThat((Boolean) getAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME), is(true));
    }

    @When("^request is made to get the CPP Information$")
    public void request_is_made_to_get_the_CPP_Information() throws Exception {
        try {
            ResponseEntity<ClmContractDTO> result = cppInformationController.fetchContractPriceProfileInfo(CukesConstants.AGREEMENT_ID,
                    CukesConstants.CLM_CONTRACT_TYPE_STREET);
            setAttribute(CONTRACT_INFORMATION_RESPONSE, result.getBody());
        } catch (CPPRuntimeException cppre) {
            setAttribute("ContractInformationUnSuccessfulResponse", cppre.getErrorCode());
        }
    }

    @Then("^the latest values of contract from CLM is returned$")
    public void the_latest_values_of_contract_from_CLM_is_returned() throws Exception {
        ClmContractDTO actualContractDate = (ClmContractDTO) getAttribute(CONTRACT_INFORMATION_RESPONSE);

        assertThat(actualContractDate.getContractStartDate(), equalTo(CukesConstants.CLM_CONTRACT_START_DATE));
        assertThat(actualContractDate.getContractEndDate(), equalTo(CukesConstants.CLM_CONTRACT_END_DATE));
        assertThat(actualContractDate.getContractType(), equalTo(CukesConstants.CLM_CONTRACT_TYPE_STREET));
        assertThat(actualContractDate.getAgreementId(), equalTo(CukesConstants.AGREEMENT_ID));

    }

}
