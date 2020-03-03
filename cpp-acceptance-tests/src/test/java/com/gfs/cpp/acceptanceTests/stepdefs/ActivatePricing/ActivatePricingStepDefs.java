package com.gfs.cpp.acceptanceTests.stepdefs.ActivatePricing;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAmendmentContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setExistingContractPriceProfileSeq;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.common.data.AcceptableItems;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableRealCustomer;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.acceptanceTests.mocks.CLMGetAgreementMocker;
import com.gfs.cpp.acceptanceTests.mocks.CustomerGroupServiceProxyMocker;
import com.gfs.cpp.acceptanceTests.stepdefs.contractPricing.ContractPricingStepDefs;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.web.controller.activatepricing.ActivatePricingController;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ActivatePricingStepDefs {

    @Autowired
    CustomerGroupServiceProxyMocker customerServiceProxyMocker;

    @Autowired
    private ActivatePricingController activatePricingController;

    @Autowired
    private ValidateCustomerPricing validateCustomerPricing;

    @Autowired
    private CustomerPricingPersister customerPricingPersister;

    @Autowired
    private ContractPricingStepDefs contractPricingStepDefs;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private CLMGetAgreementMocker cLMGetAgreementMocker;

    public static final String DAYS_FROM_NOW = "days from now";

    @Then("^display message to user for contract is in draft status and not editable$")
    public void return_contract_status_with_message_for_invalid_contract() {
        final String result = (String) getAttribute(CukesConstants.CPP_ERROR_MESSAGE);
        assertThat(result, CoreMatchers
                .containsString("The contract is not ready for activating price. It is in " + ContractPriceProfileStatus.DRAFT.desc + " status."));
    }

    @Then("^display message to user for contract not ready for activation and has missing concept to customer mapping$")
    public void return_message_for_contract_with_no_customer_mapping() {
        final String result = (String) getAttribute(CukesConstants.CPP_ERROR_MESSAGE);
        assertThat(result, CoreMatchers
                .containsString("The contract is not ready for price activation. There are concepts on the contract without valid mapping(s)."));
    }

    private void activate_pricing(int contractPriceProfileSeq, boolean isAmendment, String clmContractStatus) throws Throwable {
        try {
            ResponseEntity<Map<String, String>> response = activatePricingController.activatePricing(contractPriceProfileSeq, isAmendment,
                    clmContractStatus);
            setAttribute("PricingActivatedMsg", response.getBody().get(CPPConstants.MESSAGE));
            setAttribute("PricingActivated_response", response);
            setAttribute(CukesConstants.CPP_ERROR_CODE, response.getStatusCodeValue());
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
            setAttribute(CukesConstants.CPP_ERROR_CODE, cpp.getErrorCode());
            setAttribute(CukesConstants.CPP_ERROR_MESSAGE, cpp.getMessage());
        }
    }

    @When("^request is made to activate pricing$")
    public void activate_pricing_for_contract() throws Throwable {
        activate_pricing(getContractPriceProfileSeq(), false, null);

    }

    @When("^request is made to activate pricing for amendment$")
    public void activate_pricing_for_amendment() throws Throwable {
        activate_pricing(getAmendmentContractPriceProfileSeq(), true,
                cLMGetAgreementMocker.getClmContractStatus(CukesConstants.AMENDMENT_AGREEMENT_ID));
    }

    @Then("^entries for real customer \"([^\"]*)\" are saved in PRC tables with effective pricing start date as (.+)$")
    public void verify_pricing_entries_in_prc(AcceptableRealCustomer realCustomer, String receivedEffectiveDate) {
        Date effectiveDate = convertToDate(receivedEffectiveDate);
        validateCustomerPricing.validatePricingEntries(realCustomer, effectiveDate);
    }

    @Then("^new entries for the customer \"([^\"]*)\" are saved to CIP tables for product type markup with effective pricing start date as (.+)$")
    public void verify_product_in_cip_table_for_product_type_markup(AcceptableRealCustomer realCustomer, String receivedEffectiveDate) {
        Date customerPricingEffectiveDate = convertToDate(receivedEffectiveDate);
        validateCustomerPricing.verifyCIPEntriesForProductTypeMarkup(realCustomer, customerPricingEffectiveDate);
    }

    @Then("^display message to user for activation of contract$")
    public void display_msg_for_successfull_pricing_activation() throws Throwable {
        final String result = (String) getAttribute("PricingActivatedMsg");
        assertThat(result, CoreMatchers.containsString("Pricing activated for the contract"));
    }

    @Then("^existing customer pricing entries for \"([^\"]*)\" in PRC_PROF table for an pricing activated contract are expired with expiration date as (.+)$")
    public void verifyExpringExistingPRCEntriesForPricingActivateContract(AcceptableRealCustomer realCustomer, String receivedExpirationDate) {
        Date expirationDate = convertToDate(receivedExpirationDate);
        validateCustomerPricing.validatePricingEntriesForPricingActivateContract(realCustomer, expirationDate);

    }

    @Then("^existing customer pricing entries for \"([^\"]*)\" in PRC_PROF table are expired with expiration date as (.+)$")
    public void verifyExpringExistingPRCEntries(AcceptableRealCustomer realCustomer, String receivedExpirationDate) {
        Date expirationDate = convertToDate(receivedExpirationDate);
        validateCustomerPricing.validatePricingEntriesForExistingContract(realCustomer, expirationDate);
    }

    @Then("^existing customer entries for \"([^\"]*)\" in CIP table are expired with expiration date as (.+)$")
    public void verifyExpringExistingCIPEntries(AcceptableRealCustomer realCustomer, String receivedExpirationDate) {
        Date customerPricingEexpirationDate = convertToDate(receivedExpirationDate);
        validateCustomerPricing.validateCIPEntriesForExistingContract(realCustomer, customerPricingEexpirationDate);

    }

    @When("there exists a contract with pricing date range as (.+) and (.+)$")
    public void create_existing_contract(String receievdEffectiveDate, String receievdExpirationDate) {
        try {
            Date effectiveDate = convertToDate(receievdEffectiveDate);
            Date expirationDate = convertToDate(receievdExpirationDate);
            int existingContractPriceSeq = contractPricingStepDefs.in_cpp_application_markup_with_date_range(effectiveDate, expirationDate,
                    Integer.valueOf(CukesConstants.EXISTING_CPP_ID));
            setExistingContractPriceProfileSeq(existingContractPriceSeq);
            customerPricingPersister.savePricingForExistingContract(effectiveDate, expirationDate);
        } catch (Exception e) {
            fail();
        }

    }

    @When("there exists a contract with item \"([^\"]*)\" with pricing date range as (.+) and (.+)$")
    public void create_existing_contract_with_item_markup(AcceptableItems acceptableItem, String receievdEffectiveDate,
            String receievdExpirationDate) {
        try {
            Date effectiveDate = convertToDate(receievdEffectiveDate);
            Date expirationDate = convertToDate(receievdExpirationDate);
            int existingContractPriceSeq = contractPricingStepDefs.in_cpp_application_markup_with_date_range(effectiveDate, expirationDate,
                    Integer.valueOf(CukesConstants.EXISTING_CPP_ID));
            setExistingContractPriceProfileSeq(existingContractPriceSeq);
            customerPricingPersister.savePricingForExistingContract(effectiveDate, expirationDate);
            customerPricingPersister.savePricingForItemMarkupForExistingContractWithoutBidReasonCode(effectiveDate, expirationDate,
                    Integer.parseInt(acceptableItem.getItemId()));
        } catch (Exception e) {
            fail();
        }

    }

    @Then("there exists competetive entry for an item for customer \"([^\"]*)\" for date range as (.+) and (.+)$")
    public void existing_contract_updated_for_new_item_markup(AcceptableRealCustomer realCustomer, String receievdEffectiveDate,
            String receievdExpirationDate) {
        try {
            Date effectiveDate = convertToDate(receievdEffectiveDate);
            Date expirationDate = convertToDate(receievdExpirationDate);
            customerPricingPersister.savePricingForRealUnitTypeCustomerForAnItemMarkupForExistingContractWithBidReasonCode(effectiveDate,
                    expirationDate, Integer.parseInt(CukesConstants.ITEM_ID));
        } catch (Exception e) {
            fail();
        }
    }

    @When("request is made to save item level markup with date range as (.+) and (.+)$")
    public void save_item_level_markup(String receievdEffectiveDate, String receievdExpirationDate) {
        try {
            Date effectiveDate = convertToDate(receievdEffectiveDate);
            Date expirationDate = convertToDate(receievdExpirationDate);
            customerPricingPersister.saveCIPEntriesForRealCustomerForItemMarkup(effectiveDate, expirationDate);
        } catch (Exception e) {
            fail();
        }
    }

    @When("there exists contract with an item with Bid reason code for date range as (.+) and (.+)$")
    public void create_existing_contract_with_item_having_bid_reason_code(String receievdEffectiveDate, String receievdExpirationDate) {
        try {
            Date effectiveDate = convertToDate(receievdEffectiveDate);
            Date expirationDate = convertToDate(receievdExpirationDate);
            Integer existingContractPriceSeq = contractPricingStepDefs.in_cpp_application_markup_with_date_range(effectiveDate, expirationDate,
                    Integer.valueOf(CukesConstants.EXISTING_CPP_ID));
            setExistingContractPriceProfileSeq(existingContractPriceSeq);
            customerPricingPersister.savePricingForExistingContractWithBidReasonCode(effectiveDate, expirationDate,
                    Integer.parseInt(CukesConstants.ITEM_ID));

        } catch (Exception e) {
            fail();
        }
    }
    /* @When("there exists contract with a different item with Bid reason code for date range as (.+) and (.+)$") public void
     * create_existing_contract_with_different_item_having_bid_reason_code(String receievdEffectiveDate, String receievdExpirationDate) { try { Date
     * effectiveDate = convertToDate(receievdEffectiveDate); Date expirationDate = convertToDate(receievdExpirationDate); Integer
     * existingContractPriceSeq = contractPricingStepDefs.in_cpp_application_markup_with_date_range( cppDateUtils.formatDateToString(effectiveDate),
     * cppDateUtils.formatDateToString(expirationDate), Integer.valueOf(CukesConstants.EXISTING_CPP_ID));
     * setExistingContractPriceProfileSeq(existingContractPriceSeq);
     * customerPricingPersister.savePricingForExistingContractWithBidReasonCode(effectiveDate, expirationDate,
     * Integer.parseInt(CukesConstants.NEW_ITEM_ID_TO_ASSIGN)); } catch (Exception e) { fail(); } } */

    @Then("^existing bid pricing in cip for \"([^\"]*)\" is not expired and has expiration date as (.+)$")
    public void verifyExistingCIPBidPricingNotExpred(AcceptableRealCustomer realCustomer, String receievdExpirationDate) {
        Date expirationDate = convertToDate(receievdExpirationDate);
        validateCustomerPricing.validateCIPEntriesForExistingContract(realCustomer, expirationDate);

    }

    @Then("^existing customer entries for \"([^\"]*)\" in CIP table for \"([^\"]*)\" is not expired and has expiration date as (.+)$")
    public void verifyExistingCIPPricingForItemIsNotExpred(AcceptableRealCustomer realCustomer, AcceptableItems acceptableItem,
            String receievdExpirationDate) {
        Date expirationDate = convertToDate(receievdExpirationDate);
        validateCustomerPricing.validateCIPEntriesForItemMarkupForExistingContract(realCustomer, expirationDate,
                Integer.parseInt(acceptableItem.getItemId()));

    }

    @Then("^new entries for the customer for \"([^\"]*)\" is (.+) to CIP tables with effective pricing as (.+)$")
    public void verify_item_in_cip_table_saved_or_not_saved(AcceptableItems acceptableItem, String savedOrNoTSaved, String receivedEffectiveDate) {

        if ("SAVED".equals(savedOrNoTSaved)) {
            verify_pricing_in_cip_table_saved(receivedEffectiveDate, Integer.parseInt(acceptableItem.getItemId()));
        } else {
            verify_pricing_in_cip_table_not_saved(receivedEffectiveDate, Integer.parseInt(acceptableItem.getItemId()));
        }
    }

    @Then("^new entries for the customer are (.+) to CIP tables with effective pricing as (.+)$")
    public void verify_product_in_cip_table_saved_or_not_saved(String savedOrNoTSaved, String receivedEffectiveDate) {

        if ("SAVED".equals(savedOrNoTSaved)) {
            verify_pricing_in_cip_table_saved(receivedEffectiveDate, Integer.parseInt(CukesConstants.ITEM_ID));
        } else {
            verify_pricing_in_cip_table_not_saved(receivedEffectiveDate, Integer.parseInt(CukesConstants.ITEM_ID));
        }
    }

    @Then("^an exception is returned of the type \"(INVALID_CONTRACT|OK)\" for activate pricing$")
    public void an_exception_is_returned_of_the_type_for_activate_pricing(String exception) {
        String response = getAttribute("cpp_error_type").toString();
        assertThat(response, equalTo(exception));
    }

    @When("^request is made to check if activate pricing is enabled$")
    public void activate_pricing_enabled() throws Throwable {
        ResponseEntity<Map<String, Boolean>> response = activatePricingController.enableActivatePricing(getContractPriceProfileSeq());
        setAttribute("EnableActivatePricing", response.getBody());
    }

    @Then("^activate pricing is \"([^\"]*)\"$")
    public void verify_activate_pricing_enabled(String value) {
        Map<String, Boolean> response = (Map<String, Boolean>) getAttribute("EnableActivatePricing");
        if (value.equals("ENABLED")) {
            assertThat(response.get(CPPConstants.ENABLE_ACTIVATE_PRICING_BUTTON), equalTo(true));
        } else if (value.equals("DISABLED")) {
            assertThat(response.get(CPPConstants.ENABLE_ACTIVATE_PRICING_BUTTON), equalTo(false));
        }
    }

    @Given("^the status of amendment in CPP is \"([^\"]*)\"$")
    public void the_status_of_contract_is_updated(ContractPriceProfileStatus cppStatus) throws Throwable {
        contractPriceProfileRepository.updateContractStatusWithLastUpdateUserIdByCppSeq(getAmendmentContractPriceProfileSeq(), cppStatus.code,
                CukesConstants.CURRENT_USER_ID);
    }

    @When("^the status of amendment \"([^\"]*)\" in CPP is \"([^\"]*)\"$")
    public void the_status_of_amendment_in_CPP_is(String agreementId, ContractPriceProfileStatus cppStatus) throws Exception {
        contractPriceProfileRepository.updateContractStatusWithLastUpdateUserIdByCppSeq(getAmendmentContractPriceProfileSeq(agreementId), cppStatus.code,
                CukesConstants.CURRENT_USER_ID);
    }

    @Given("^status of amendment in CLM is \"([^\"]*)\"$")
    public void the_current_status_of_contract_in_CLM_is(ClmContractStatus clmContractStatus) throws Throwable {
        setAmendmentStatus(CukesConstants.AMENDMENT_AGREEMENT_ID, clmContractStatus);
    }

    @Given("^an amendment is created in CLM with effectiveDate passing contract expiration date and its status as \"([^\"]*)\"$")
    public void the_amendment_effective_date_past_contract_expiration_date(ClmContractStatus clmContractStatus) throws Throwable {
        setAmendmentStatus(CukesConstants.AMENDMENT_AGREEMENT_ID, clmContractStatus);
        cLMGetAgreementMocker.setAmendmentEffectiveDate(cppDateUtils.daysBeyondCurrentDate(5));
        cLMGetAgreementMocker.setContractExpirationDate(cppDateUtils.daysBeyondCurrentDate(2));
    }

    @Given("^an amendment is created in CLM with current status as \"([^\"]*)\"$")
    public void the_amendment_status_of_contract_in_CLM_is(ClmContractStatus clmContractStatus) throws Throwable {
        setAmendmentStatus(CukesConstants.AMENDMENT_AGREEMENT_ID, clmContractStatus);
    }

    @When("^an amendment \"([^\"]*)\" is created in CLM with current status as \"([^\"]*)\"$")
    public void an_amendment_is_created_in_CLM_with_current_status_as(String agreementId, ClmContractStatus clmContractStatus) throws Exception {
        setAmendmentStatus(agreementId, clmContractStatus);
    }

    @Given("^amendment effective date in CLM is set to (.+)$")
    public void update_amendment_effective_date_in_CLM(String amendmentEffectiveDate) throws Throwable {
        Date effectiveDate = convertToDate(amendmentEffectiveDate);
        cLMGetAgreementMocker.setAmendmentEffectiveDate(effectiveDate);
    }

    @Given("^contract expiration date in CLM is set to (.+)$")
    public void update_contract_expiration_date_in_CLM(String contractExpirationDate) throws Throwable {
        Date expirationDate = convertToDate(contractExpirationDate);
        cLMGetAgreementMocker.setContractExpirationDate(expirationDate);
    }

    @Then("^an exception message is displayed for CLM status not in Executed state$")
    public void return_contract_status_with_message_for_invalid_clm_staus() {
        final String result = (String) getAttribute(CukesConstants.CPP_ERROR_MESSAGE);
        assertThat(result, CoreMatchers.containsString("The contract is not ready for activating price. It is in Draft status"));
    }

    @Then("^status of amendment updated as \"([^\"]*)\"$")
    public void status_of_contract_updated_as(ContractPriceProfileStatus status) throws Throwable {
        ContractPricingResponseDTO contractPriceProfile = contractPriceProfileRepository.fetchContractDetailsByCppSeq(getAmendmentContractPriceProfileSeq());

        assertThat(contractPriceProfile.getContractPriceProfStatusCode(), equalTo(status.code));
    }

    public void verify_pricing_in_cip_table_saved(String receivedEffectiveDate, int itemId) {
        Date customerPricingEffectiveDate = convertToDate(receivedEffectiveDate);
        validateCustomerPricing.verifyCIPEntriesSaved(customerPricingEffectiveDate, itemId);
    }

    public void verify_pricing_in_cip_table_not_saved(String receivedEffectiveDate, int itemId) {
        Date customerPricingEffectiveDate = convertToDate(receivedEffectiveDate);
        validateCustomerPricing.verifyCIPEntriesNotSaved(customerPricingEffectiveDate, itemId);
    }

    private Date convertToDate(String date) {
        Date returnDate = null;
        date = date.replaceAll("_", " ");
        String[] days = date.split(" ");
        if ("farOfDate".equalsIgnoreCase(date)) {
            returnDate = cppDateUtils.getFutureDate();
        } else if (date.contains(ActivatePricingStepDefs.DAYS_FROM_NOW)) {
            returnDate = cppDateUtils.daysBeyondCurrentDate(Integer.parseInt(days[0]));
        } else {
            returnDate = cppDateUtils.daysPreviousCurrentDate(Integer.parseInt(days[0]));
        }
        return returnDate;
    }

    private void setAmendmentStatus(String agreementId, ClmContractStatus clmContractStatus) {
        cLMGetAgreementMocker.setClmContractStatus(agreementId, clmContractStatus);
        cLMGetAgreementMocker.setAmendment(agreementId, true);
    }
}
