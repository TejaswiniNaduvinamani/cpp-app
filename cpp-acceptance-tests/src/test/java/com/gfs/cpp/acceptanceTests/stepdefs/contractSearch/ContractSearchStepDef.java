package com.gfs.cpp.acceptanceTests.stepdefs.contractSearch;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.common.data.AcceptableRealCustomer;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.dto.search.ContractSearchResultDTO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.web.controller.search.ContractSearchController;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContractSearchStepDef {

    @Autowired
    private ContractSearchController contractSearchController;

    @When("^a request is made to search contracts by \"([^\"]*)\"$")
    public void search_contract_by_contract_name(String contractName) {
        ResponseEntity<List<ContractSearchResultDTO>> contractSearchResultList = contractSearchController.searchContractsByContractName(contractName);
        List<ContractSearchResultDTO> result = contractSearchResultList.getBody();
        setAttribute("searchresult", result);
    }

    @Then("^list of contracts returned with furtherance flag as Yes")
    public void return_search_results() {
        List<ContractSearchResultDTO> result = (List<ContractSearchResultDTO>) getAttribute("searchresult");
        ContractPricingDTO contractPricingDTO = (ContractPricingDTO) getAttribute("contractPricingDTO");

        assertThat(result.get(0).getAgreementId(), equalTo(contractPricingDTO.getAgreementId()));
        assertThat(result.get(0).getContractName(), equalTo(contractPricingDTO.getContractName()));
        assertThat(result.get(0).getContractPriceProfileId(), equalTo(contractPricingDTO.getContractPriceProfileId()));
        assertThat(result.get(0).getCustomerName(), equalTo(CukesConstants.REAL_CUSTOMER_ID + "-" + contractPricingDTO.getContractName()));
        assertThat(result.get(0).getEffectiveDate(), equalTo(contractPricingDTO.getPricingEffectiveDate()));
        assertThat(result.get(0).getIsFurtheranceExist(), equalTo(CPPConstants.YES));
        assertThat(result.get(0).getParentAgreementId(), equalTo(contractPricingDTO.getParentAgreementId()));
        assertThat(result.get(0).getStatus(), equalTo(ContractPriceProfileStatus.PRICING_ACTIVATED.getDesc()));
        assertThat(result.get(0).getVersion(), equalTo(contractPricingDTO.getVersionNbr()));
    }

    @Then("^list of contracts returned with furthernace flag as No")
    public void return_search_results_with_no_furtherance_flag() {
        List<ContractSearchResultDTO> result = (List<ContractSearchResultDTO>) getAttribute("searchresult");
        ContractPricingDTO contractPricingDTO = (ContractPricingDTO) getAttribute("contractPricingDTO");

        assertThat(result.get(0).getAgreementId(), equalTo(contractPricingDTO.getAgreementId()));
        assertThat(result.get(0).getContractName(), equalTo(contractPricingDTO.getContractName()));
        assertThat(result.get(0).getContractPriceProfileId(), equalTo(contractPricingDTO.getContractPriceProfileId()));
        assertThat(result.get(0).getCustomerName(), equalTo(null));
        assertThat(result.get(0).getEffectiveDate(), equalTo(contractPricingDTO.getPricingEffectiveDate()));
        assertThat(result.get(0).getIsFurtheranceExist(), equalTo(CPPConstants.NO));
        assertThat(result.get(0).getParentAgreementId(), equalTo(contractPricingDTO.getParentAgreementId()));
        assertThat(result.get(0).getStatus(), equalTo(ContractPriceProfileStatus.DRAFT.getDesc()));
        assertThat(result.get(0).getVersion(), equalTo(contractPricingDTO.getVersionNbr()));
    }

    @Then("^empty list is returned")
    public void return_empty_list() {
        List<ContractSearchResultDTO> result = (List<ContractSearchResultDTO>) getAttribute("searchresult");

        assertThat(result.isEmpty(), equalTo(true));
    }

    @When("^a request is made to search contracts by customer \"([^\"]*)\"$")
    public void search_contract_by_customer(AcceptableRealCustomer customer) {
        ResponseEntity<List<ContractSearchResultDTO>> contractSearchResultList = contractSearchController
                .searchContractsByCustomer(customer.getCustomerId(), customer.getCustomerTypeCode());
        List<ContractSearchResultDTO> result = contractSearchResultList.getBody();
        setAttribute("searchresult", result);
    }

    @Then("^list of contracts with customer name assigned returned with furthernace flag as No")
    public void return_search_results_with_cutomer_name_with_no_furtherance_flag() {
        List<ContractSearchResultDTO> result = (List<ContractSearchResultDTO>) getAttribute("searchresult");
        ContractPricingDTO contractPricingDTO = (ContractPricingDTO) getAttribute("contractPricingDTO");

        assertThat(result.get(0).getAgreementId(), equalTo(contractPricingDTO.getAgreementId()));
        assertThat(result.get(0).getContractName(), equalTo(contractPricingDTO.getContractName()));
        assertThat(result.get(0).getContractPriceProfileId(), equalTo(contractPricingDTO.getContractPriceProfileId()));
        assertThat(result.get(0).getCustomerName(), equalTo(CukesConstants.REAL_CUSTOMER_ID + "-" + contractPricingDTO.getContractName()));
        assertThat(result.get(0).getEffectiveDate(), equalTo(contractPricingDTO.getPricingEffectiveDate()));
        assertThat(result.get(0).getIsFurtheranceExist(), equalTo(CPPConstants.NO));
        assertThat(result.get(0).getParentAgreementId(), equalTo(contractPricingDTO.getParentAgreementId()));
        assertThat(result.get(0).getStatus(), equalTo(ContractPriceProfileStatus.CONTRACT_APPROVED.getDesc()));
        assertThat(result.get(0).getVersion(), equalTo(contractPricingDTO.getVersionNbr()));
    }

    @When("^a request is made to search contracts by CPP ID \"([^\"]*)\"$")
    public void search_contract_by_cppid(int cppId) {
        ResponseEntity<List<ContractSearchResultDTO>> contractSearchResultList = contractSearchController.searchContractsByCPPId(cppId);
        List<ContractSearchResultDTO> result = contractSearchResultList.getBody();
        setAttribute("searchresult", result);
    }

}
