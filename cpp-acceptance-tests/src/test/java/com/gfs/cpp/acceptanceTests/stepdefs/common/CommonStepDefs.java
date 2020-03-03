package com.gfs.cpp.acceptanceTests.stepdefs.common;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setContractPriceProfileSeq;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.acceptanceTests.common.data.AcceptableItems;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableMarkupType;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.acceptanceTests.stepdefs.ActivatePricing.ActivatePricingStepDefs;
import com.gfs.cpp.acceptanceTests.stepdefs.clm.integration.ClmIntegrationStepDefs;
import com.gfs.cpp.acceptanceTests.stepdefs.contractPricing.ContractPricingStepDefs;
import com.gfs.cpp.acceptanceTests.stepdefs.distributionCenter.DistributionCenterStepDefs;
import com.gfs.cpp.acceptanceTests.stepdefs.furtherance.FurtheranceStepDefs;
import com.gfs.cpp.acceptanceTests.stepdefs.itemAssignments.ItemAssignmentsStepDefs;
import com.gfs.cpp.acceptanceTests.stepdefs.markup.MarkupStepDefs;
import com.gfs.cpp.acceptanceTests.stepdefs.saveCustomerAssignments.SaveCustomerAssignmentsStepDefs;
import com.gfs.cpp.acceptanceTests.stepdefs.splitcaseFee.SplitcaseFeeStepDefs;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class CommonStepDefs {

    @Autowired
    private ContractPricingStepDefs contractPricingStepDefs;
    @Autowired
    private DistributionCenterStepDefs distributionCenterStepDefs;
    @Autowired
    private MarkupStepDefs markupStepDefs;
    @Autowired
    private SplitcaseFeeStepDefs splitcaseFeeStepDefs;
    @Autowired
    private CPPDateUtils cppDateUtils;
    @Autowired
    private ClmIntegrationStepDefs clmIntegrationStepDefs;
    @Autowired
    private SaveCustomerAssignmentsStepDefs saveCustomerAssignmentsStepDefs;
    @Autowired
    private ActivatePricingStepDefs activatePricingStepDefs;
    @Autowired
    private FurtheranceStepDefs furtheranceStepDefs;
    @Autowired
    private ItemAssignmentsStepDefs itemAssignmentsStepDefs;

    public static final String DAYS_FROM_NOW = "days from now";

    public static final String MARKUP_VALUE = "10.43";

    @Given("^all pricing information for the contract is completed$")
    public void in_cpp_application() throws Exception {
        contractPricingStepDefs.in_cpp_application_markup();
        distributionCenterStepDefs.distribution_centers_saved();
        markupStepDefs.enter_markup_values(AcceptableMarkupType.PRODUCT_TYPE_CASE, MARKUP_VALUE);
        markupStepDefs.enter_markup_values(AcceptableMarkupType.ITEM_CASE, MARKUP_VALUE);
        markupStepDefs.enter_markup_values(AcceptableMarkupType.SUBGROUP_CASE, MARKUP_VALUE);
        markupStepDefs.enter_markup_values(AcceptableMarkupType.FUTURE_ITEM, MARKUP_VALUE);
        markupStepDefs.request_to_save_markup();
        markupStepDefs.save_markup_indicators();
        splitcaseFeeStepDefs.enter_splitcase_values();
    }

    @Given("^request is made to save new contract price with date range as (.+) and (.+)$")
    public void create_contract_with_date_range(String receivedEffectiveDate, String receivedExpirationDate) throws Exception {
        Date effectiveDate = convertToDate(receivedEffectiveDate);
        Date expirationDate = convertToDate(receivedExpirationDate);
        create_contract_for_pricing_dates(effectiveDate, expirationDate);

    }

    @Given("^request is made to save the contract price including cip entries with date range as (.+) and (.+)$")
    public void create_contract_with_cip_for_date_range(String receivedEffectiveDate, String receivedExpirationDate) throws Exception {
        Date effectiveDate = convertToDate(receivedEffectiveDate);
        Date expirationDate = convertToDate(receivedExpirationDate);
        create_contract_for_pricing_dates(effectiveDate, expirationDate);
        markupStepDefs.enter_markup_values_for_item_with_date_range(effectiveDate, expirationDate, CukesConstants.ITEM_ID);
        markupStepDefs.enter_markup_values_for_subgroup_with_date_range(effectiveDate, expirationDate);
    }

    @Given("^request is made to save the contract price including cip entries for \"([^\"]*)\" with date range as (.+) and (.+)$")
    public void create_contract_with_cip_for_different_item_for_date_range(AcceptableItems itemType, String receivedEffectiveDate,
            String receivedExpirationDate) throws Exception {
        Date effectiveDate = convertToDate(receivedEffectiveDate);
        Date expirationDate = convertToDate(receivedExpirationDate);
        create_contract_for_pricing_dates(effectiveDate, expirationDate);
        markupStepDefs.enter_markup_values_for_item_with_date_range(effectiveDate, expirationDate, itemType.getItemId());
    }

    private void create_contract_for_pricing_dates(Date effectiveDate, Date expirationDate) throws Exception, ParseException {
        Integer currentContractPriceSeq = contractPricingStepDefs.in_cpp_application_markup_with_date_range(effectiveDate, expirationDate,
                Integer.valueOf(CukesConstants.CPP_ID));
        setContractPriceProfileSeq(currentContractPriceSeq);
        distributionCenterStepDefs.distribution_centers_saved_with_date_range(effectiveDate, expirationDate);
        markupStepDefs.enter_product_level_markup_values_with_date_range(effectiveDate, expirationDate);
        markupStepDefs.save_markup_indicators_with_date_range(effectiveDate, expirationDate);
        splitcaseFeeStepDefs.enter_splitcase_values_with_date_range(effectiveDate, expirationDate);
    }

    @Then("^an exception is returned from the request$")
    public void an_exception_is_returned_from_the_request() throws Exception {
        assertThat((Boolean) getAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME), equalTo(true));
    }

    @Then("^an exception is not returned from the request$")
    public void an_exception_is_not_returned_from_the_request() throws Exception {
        assertThat(getAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME), nullValue());
    }

    @Then("^the exception type is \"([^\"]*)\"$")
    public void the_exception_type_is(String errorType) throws Exception {
        assertThat(getAttribute(CukesConstants.CPP_ERROR_TYPE).toString(), equalTo(errorType));
    }

    @Given("^a request is made to save furtherance$")
    public void create_furtherance() throws Throwable {
        in_cpp_application();
        clmIntegrationStepDefs.the_status_of_contract_is_moved_to(ContractPriceProfileStatus.CONTRACT_APPROVED);
        saveCustomerAssignmentsStepDefs.save_assignment_real_customer();
        itemAssignmentsStepDefs.assign_items_with_future_items(AcceptableMarkupType.ITEM_CASE, AcceptableMarkupType.FUTURE_ITEM);
        activatePricingStepDefs.activate_pricing_for_contract();
        furtheranceStepDefs.on_save_furtherance();
    }

    private Date convertToDate(String date) {
        Date returnDate = null;
        date = date.replaceAll("_", " ");
        String[] days = date.split(" ");
        if (date.contains(DAYS_FROM_NOW)) {
            returnDate = cppDateUtils.daysBeyondCurrentDate(Integer.parseInt(days[0]));
        } else {
            returnDate = cppDateUtils.daysPreviousCurrentDate(Integer.parseInt(days[0]));
        }

        return returnDate;
    }

}
