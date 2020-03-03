package com.gfs.cpp.acceptanceTests.stepdefs.clm.integration;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAmendmentContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getExistingContractPriceProfileSeq;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.acceptanceTests.mocks.CLMGetAgreementMocker;
import com.gfs.cpp.acceptanceTests.mocks.ClmApiProxyMocker;
import com.gfs.cpp.acceptanceTests.stepdefs.ActivatePricing.ValidateCustomerPricing;
import com.gfs.cpp.common.dto.clm.ClmContractAction;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.integration.mdp.clm.CLMIntegrationQueueListenerMDP;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ClmIntegrationStepDefs {

    private static final String UPDATE_USER_ID = "cpp acceptance test";

    @Autowired
    private CLMIntegrationQueueListenerMDP cLMIntegrationQueueListenerMDP;
    @Autowired
    private ClmApiProxyMocker clmApiProxyMocker;
    @Autowired
    private CLMGetAgreementMocker cLMGetAgreementMocker;
    @Autowired
    private ValidateCustomerPricing validateCustomerPricing;
    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Given("^the message with action \"([^\"]*)\" is sent for non pricing contract type$")
    public void the_message_with_action_is_sent_for_non_pricing_contract_type(ClmContractAction clmAction) throws Throwable {
        CppTextMessage contractMessage = buildTextMessage(clmAction, "nonpricingtype", CukesConstants.AGREEMENT_ID);
        cLMIntegrationQueueListenerMDP.onMessage(contractMessage);
    }

    @Given("^the message with action \"([^\"]*)\" is sent for pricing contract type$")
    public void the_message_with_action_is_sent_for_pricing_contract_type(ClmContractAction clmAction) throws Throwable {
        receiveMessage(clmAction, CukesConstants.CLM_CONTRACT_TYPE_REGIIONAL, CukesConstants.AGREEMENT_ID);
    }

    private void receiveMessage(ClmContractAction clmAction, String contractType, String agreementId) throws JMSException {
        CppTextMessage contractMessage = buildTextMessage(clmAction, contractType, agreementId);
        cLMIntegrationQueueListenerMDP.onMessage(contractMessage);
    }

    @When("^the message with action \"([^\"]*)\" is sent for amendment pricing contract type$")
    public void the_message_with_action_is_sent_for_amendment_pricing_contract_type(ClmContractAction clmAction) throws Exception {
        receiveMessage(clmAction, CukesConstants.CLM_CONTRACT_TYPE_REGIONAL_AMENDMENT, CukesConstants.AMENDMENT_AGREEMENT_ID);
    }

    private CppTextMessage buildTextMessage(ClmContractAction action, String contractType, String instanceId) throws JMSException {
        CppTextMessage textMessage = new CppTextMessage();

        StringBuilder builder = new StringBuilder("{");

        builder.append("\"Action\":\"" + action.value + "\",");
        builder.append("\"InstanceId\":\"" + instanceId + "\",");
        builder.append("\"EventType\":\"" + contractType + "\"");
        builder.append("}");

        textMessage.setText(builder.toString());
        return textMessage;
    }

    @Then("^cpp url is updated in the contract$")
    public void cpp_url_is_updated_in_the_contract() throws Throwable {
        clmApiProxyMocker.isCppUrlUpdated();
    }

    @Then("^amendment cpp url is updated in the amendment contract$")
    public void cpp_url_is_updated_in_the_amendment_contract() throws Exception {
        clmApiProxyMocker.isCppAmendmentUrlUpdated();
    }

    @Then("^furtherance cpp url is updated in the contract$")
    public void furtherance_cpp_url_is_updated_in_the_contract() throws Exception {
        clmApiProxyMocker.isFurtheranceUrlUpdated();
    }

    @When("^the contract is created as pricing not required$")
    public void the_pricing_is_set_as_not_required() throws Throwable {
        cLMGetAgreementMocker.setPricingRequired("No");
    }

    @Then("^cpp url is not updated in the contract$")
    public void cpp_url_is_not_updated_in_the_contract() throws Throwable {
        clmApiProxyMocker.isCppUrlNotUpdated();
    }

    @Then("^status of contract updated as \"([^\"]*)\"$")
    public void status_of_contract_updated_as(ContractPriceProfileStatus status) throws Throwable {
        ContractPricingResponseDTO contractPriceProfile = contractPriceProfileRepository.fetchContractDetailsByCppSeq(getContractPriceProfileSeq());

        assertThat(contractPriceProfile.getContractPriceProfStatusCode(), equalTo(status.code));
    }

    @Given("^the status of contract is in \"([^\"]*)\"$")
    public void the_status_of_contract_is_moved_to(ContractPriceProfileStatus cppStatus) throws Throwable {
        contractPriceProfileRepository.updateContractStatusWithLastUpdateUserIdByCppSeq(getContractPriceProfileSeq(), cppStatus.code, UPDATE_USER_ID);
    }

    @Given("^the status of contract is in \"([^\"]*)\" for existing sequence$")
    public void the_status_of_existing_contract_is_moved_to(ContractPriceProfileStatus cppStatus) throws Throwable {
        contractPriceProfileRepository.updateContractStatusWithLastUpdateUserIdByCppSeq(getExistingContractPriceProfileSeq(), cppStatus.code,
                UPDATE_USER_ID);
    }

    @Given("^the current status of contract in CLM is \"([^\"]*)\"$")
    public void the_current_status_of_contract_in_CLM_is(ClmContractStatus clmContractStatus) throws Throwable {
        cLMGetAgreementMocker.setClmContractStatus(CukesConstants.AGREEMENT_ID, clmContractStatus);
    }

    @Then("^all pricing data for the contract is expired$")
    public void all_pricing_data_for_the_contract_is_expired() throws Throwable {
        verifyAllPricingExpiredFor(getContractPriceProfileSeq());
    }

    @Then("^all pricing data for the contract amendment is expired$")
    public void all_pricing_data_for_the_contract_amendment_is_expired() throws Exception {
        verifyAllPricingExpiredFor(getAmendmentContractPriceProfileSeq(CukesConstants.AMENDMENT_AGREEMENT_ID));
    }

    private void verifyAllPricingExpiredFor(Integer contractPriceProfileSeq) {
        assertThat(validateCustomerPricing.fetchActiveCustomerItemPricingCount(contractPriceProfileSeq), equalTo(0));
        assertThat(validateCustomerPricing.fetchActiveAuditAuthorityCountForContract(contractPriceProfileSeq), equalTo(0));
        assertThat(validateCustomerPricing.fetchActiveCostScheduleCountForContract(contractPriceProfileSeq), equalTo(0));
        assertThat(validateCustomerPricing.fetchActiveLessCaseRuleCountForContract(contractPriceProfileSeq), equalTo(0));
        assertThat(validateCustomerPricing.fetchActiveNonBrktCstModelCountForContract(contractPriceProfileSeq), equalTo(0));
        assertThat(validateCustomerPricing.fetchActivePriceRuleOvrdCountForContract(contractPriceProfileSeq), equalTo(0));
    }

    @Then("^all pricing data for the contract amendment is not expired$")
    public void all_pricing_data_for_the_contract_amendment_is_not_expired() throws Exception {
        verifyPricingNotExpiredFor(getAmendmentContractPriceProfileSeq(CukesConstants.AMENDMENT_AGREEMENT_ID));
    }

    private void verifyPricingNotExpiredFor(Integer contractPriceProfileSeq) {
        assertThat(validateCustomerPricing.fetchActiveCustomerItemPricingCount(contractPriceProfileSeq), not(0));
        assertThat(validateCustomerPricing.fetchActiveAuditAuthorityCountForContract(contractPriceProfileSeq), not(0));
        assertThat(validateCustomerPricing.fetchActiveCostScheduleCountForContract(contractPriceProfileSeq), not(0));
        assertThat(validateCustomerPricing.fetchActiveLessCaseRuleCountForContract(contractPriceProfileSeq), not(0));
        assertThat(validateCustomerPricing.fetchActiveNonBrktCstModelCountForContract(contractPriceProfileSeq), not(0));
        assertThat(validateCustomerPricing.fetchActivePriceRuleOvrdCountForContract(contractPriceProfileSeq), not(0));
    }

    @Given("^the amendment is created in CLM$")
    public void the_amendment_is_created_in_CLM() throws Exception {
        cLMGetAgreementMocker.setAmendment(CukesConstants.AMENDMENT_AGREEMENT_ID, true);
    }

    @Given("^pricing applicable for amendment is set to \"(Yes|No)\"$")
    public void pricing_applicable_for_amendment_is_set_to(String isPricingAppliable) throws Exception {
        cLMGetAgreementMocker.setAmendmentPricing(isPricingAppliable);
    }

    @Given("^the contract name changed in CLM$")
    public void the_contract_name_changed_in_CLM() throws Exception {
        cLMGetAgreementMocker.setContractName(CukesConstants.UPDATED_CONTRACT_NAME);
    }

    @Then("^CMG name is updated$")
    public void cmg_name_is_updated() throws Exception {
        assertThat(contractPriceProfileRepository.fetchContractDetailsByCppSeq(getContractPriceProfileSeq()).getClmContractName(),
                equalTo(CukesConstants.UPDATED_CONTRACT_NAME));
    }
}
