package com.gfs.cpp.acceptanceTests.stepdefs.review;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.xml.sax.SAXException;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.acceptanceTests.mocks.ClmApiProxyMocker;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.review.ReviewDTO;
import com.gfs.cpp.common.dto.review.SavePricingExhibitDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.web.controller.review.ReviewController;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ReviewStepDefs {

    @Autowired
    private ReviewController reviewController;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;
    @Autowired
    private ClmApiProxyMocker clmApiProxyMocker;

    @When("^a request is made to get the all saved values of a contract for Review$")
    public void request_to_get_review_data()
            throws KeyManagementException, NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, JAXBException {
        final ResponseEntity<ReviewDTO> response = reviewController.fetchReviewData(getContractPriceProfileSeq());
        setAttribute("response", response.getBody());
    }

    @Then("^all saved values of a contract is returned$")
    public void all_price_details_of_contract_returned() {
        final ReviewDTO response = (ReviewDTO) getAttribute("response");

        verifyContractData(response);

        assertThat(response.getDistributionCenter().get(0), is(CukesConstants.DC_NAME));

        verifyMarkup(response);
        verifiySplitCase(response);
    }

    @When("^a request is made to attach exhibit in CLM$")
    public void a_request_is_made_to_attach_exhibit_in_CLM() throws Throwable {
        try {
            SavePricingExhibitDTO savePricingExhibitDTO = new SavePricingExhibitDTO();
            savePricingExhibitDTO.setContractAgeementId(CukesConstants.AGREEMENT_ID);
            savePricingExhibitDTO.setContractPriceProfileSeq(getContractPriceProfileSeq());
            savePricingExhibitDTO.setContractTypeName(CukesConstants.CLM_CONTRACT_TYPE_STREET);
            ResponseEntity<Void> response = reviewController.savePricingExhibit(savePricingExhibitDTO);
            setAttribute("ExhibitAttached_response", response);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @Then("^the exhibit is attached in CLM$")
    public void the_exhibit_is_attached_in_CLM() throws Throwable {
        clmApiProxyMocker.isExhibitAttachedToClm();
    }

    @Then("^exhibit sys id is saved in contract$")
    public void exhit_sys_id_is_saved_in_contract() throws Throwable {
        ContractPricingResponseDTO actualContract = contractPriceProfileRepository.fetchContractDetailsByCppSeq(getContractPriceProfileSeq());
        assertThat(actualContract.getPricingExhibitSysId(), is(CukesConstants.EXHIBIT_SYS_ID));
    }

    private void verifiySplitCase(final ReviewDTO response) {
        assertThat(response.getSplitCaseReviewDTO().getSplitCaseFeeValues().get(0).getProductType(), is(CukesConstants.PRODUCT_TYPE));
        assertThat(response.getSplitCaseReviewDTO().getFeeTypeContractLanguage(), notNullValue());
        assertThat(response.getSplitCaseReviewDTO().getFeeType(), is(2));
        assertThat(response.getSplitCaseReviewDTO().getSplitCaseFeeValues().get(0).getProductType(), is(CukesConstants.PRODUCT_TYPE));
        assertThat(response.getSplitCaseReviewDTO().getSplitCaseFeeValues().get(0).getSplitCaseFee(), is(CukesConstants.DEFAULT_SPLITCASE_FEE));
        assertThat(response.getSplitCaseReviewDTO().getSplitCaseFeeValues().get(0).getUnit(), is(CukesConstants.DEFAULT_UNIT));
        assertThat(response.getSplitCaseReviewDTO().getSplitCaseFeeValues().get(0).getItemPriceId(),
                is(String.valueOf(CukesConstants.PRODUCT_PRICE_ID)));
    }

    private void verifyMarkup(final ReviewDTO response) {
        assertThat(response.getMarkupReviewDTO().getMarkupBasedOnSellContractLanguage1(), notNullValue());
        assertThat(response.getMarkupReviewDTO().getMarkupBasedOnSellContractLanguage2(), notNullValue());
        assertThat(response.getMarkupReviewDTO().getMarkupTypeDefinitionPerCaseLanguage(), notNullValue());
        assertThat(response.getMarkupReviewDTO().getMarkupTypeDefinitionPerWeightLanguage(), nullValue());
        assertThat(response.getMarkupReviewDTO().getMarkupTypeDefinitionSellUnitLanguage(), nullValue());
        assertThat(response.getMarkupReviewDTO().getMarkupBasedOnSell(), is(CukesConstants.YES));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getMarkupName(), is(CukesConstants.CONTRACT_NAME));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getProductTypeMarkups().get(0).getProductType(),
                is(CukesConstants.PRODUCT_TYPE));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getProductTypeMarkups().get(0).getItemPriceId(),
                is(CukesConstants.PRODUCT_PRICE_ID));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getProductTypeMarkups().get(0).getMarkup(),
                is(CukesConstants.MARKUP_VALUE));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getProductTypeMarkups().get(0).getUnit(),
                is(CukesConstants.DEFAULT_UNIT));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getProductTypeMarkups().get(0).getMarkupType(),
                is(CukesConstants.PER_CASE_TYPE));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getSubgroupMarkups().get(0).getSubgroupId(),
                is(CukesConstants.SUBGROUP_ID_VALID));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getSubgroupMarkups().get(0).getSubgroupDesc(),
                is(CukesConstants.SUBGROUP_DESCRIPTION));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getSubgroupMarkups().get(0).getMarkup(),
                is(CukesConstants.MARKUP_VALUE));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getSubgroupMarkups().get(0).getUnit(),
                is(CukesConstants.DEFAULT_UNIT));
        assertThat(response.getMarkupReviewDTO().getMarkupGridDTOs().get(0).getSubgroupMarkups().get(0).getIsSubgroupSaved(),
                is(true));
    }

    private void verifyContractData(final ReviewDTO response) {
        assertThat(response.getContractPricingReviewDTO().getScheduleForCost(), is(CukesConstants.GREGORIAN_CALENDAR));
        assertThat(response.getContractPricingReviewDTO().getFormalPriceAudit(), is(CukesConstants.YES));
        assertThat(response.getContractPricingReviewDTO().getPriceVerification(), is(CukesConstants.YES));
        assertThat(response.getContractPricingReviewDTO().getGfsTransferFee(), is(CukesConstants.YES));
        assertThat(response.getContractPricingReviewDTO().getGfsLabelAssesmentFee(), is(CukesConstants.YES));
    }

}