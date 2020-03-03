package com.gfs.cpp.acceptanceTests.stepdefs.splitcaseFee;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.corp.component.price.common.types.LessCasePriceRule;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.web.controller.splitcase.SplitCaseController;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SuppressWarnings("unchecked")
public class SplitcaseFeeStepDefs {

    public static final String UPDATED_SPLITCASE_FEE = "25.00";

    @Autowired
    private SplitCaseController splitcasecontroller;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @When("^a request is made to get the default values of splitcase$")
    public void request_to_get_splitcase() throws Exception {
        final ResponseEntity<List<SplitCaseDTO>> splitcase = splitcasecontroller.fetchSplitCaseFee(getContractPriceProfileSeq(),
                cppDateUtils.getFutureDate(), cppDateUtils.getFutureDate());
        setAttribute("splitcase", splitcase);
    }

    @Then("^the default values of splitcase is returned$")
    public void return_splitcase() {
        final List<SplitCaseDTO> splitcase = ((ResponseEntity<List<SplitCaseDTO>>) getAttribute("splitcase")).getBody();
        assertThat(splitcase.size(), is(4));
        assertThat(splitcase.get(0).getItemPriceId(), is(String.valueOf(CukesConstants.PRODUCT_PRICE_ID)));
        assertThat(splitcase.get(0).getProductType(), is(CukesConstants.PRODUCT_TYPE));
        assertThat(splitcase.get(0).getEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(splitcase.get(0).getExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(splitcase.get(0).getSplitCaseFee(), is(CPPConstants.SPLIT_CASE_FEE));
        
        assertThat(splitcase.get(1).getItemPriceId(), is("2"));
        assertThat(splitcase.get(1).getEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(splitcase.get(1).getExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(splitcase.get(1).getProductType(), is(CukesConstants.PRODUCT_TYPE));
        
        assertThat(splitcase.get(2).getItemPriceId(), is("3"));
        assertThat(splitcase.get(2).getEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(splitcase.get(2).getExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(splitcase.get(2).getProductType(), is(CukesConstants.PRODUCT_TYPE));
        
        assertThat(splitcase.get(3).getItemPriceId(), is("4"));
        assertThat(splitcase.get(3).getEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(splitcase.get(3).getExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(splitcase.get(3).getProductType(), is(CukesConstants.PRODUCT_TYPE));

    }

    @When("^a request is made to get the saved values of splitcase$")
    public void request_to_get_saved_splitcase() throws Exception {
        final ResponseEntity<List<SplitCaseDTO>> saved_splitcase = splitcasecontroller.fetchSplitCaseFee(getContractPriceProfileSeq(),
                cppDateUtils.getFutureDate(), cppDateUtils.getFutureDate());
        setAttribute("saved_splitcase", saved_splitcase);
    }

    @Then("^a list of all saved values of splitcase is returned$")
    public void return_saved_splitcase() {
        final List<SplitCaseDTO> saved_splitcase = ((ResponseEntity<List<SplitCaseDTO>>) getAttribute("saved_splitcase")).getBody();
        assertThat(saved_splitcase.size(), is(1));
        assertThat(saved_splitcase.get(0).getProductType(), is(CukesConstants.PRODUCT_TYPE));
        assertThat(saved_splitcase.get(0).getSplitCaseFee(), is(CukesConstants.DEFAULT_SPLITCASE_FEE));
        assertThat(saved_splitcase.get(0).getUnit(), is(CukesConstants.DEFAULT_UNIT));
        assertThat(saved_splitcase.get(0).getEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(saved_splitcase.get(0).getExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(saved_splitcase.get(0).getItemPriceId(), is(String.valueOf(CukesConstants.PRODUCT_PRICE_ID)));
    }

    private SplitCaseGridDTO buildSplitCaseDTO(String fee, String unit, Date effectiveDate, Date expirationDate, int feeType) {
        SplitCaseGridDTO splitCaseDTO = new SplitCaseGridDTO();
        splitCaseDTO.setContractPriceProfileSeq(getContractPriceProfileSeq());
        List<SplitCaseDTO> splitCaseFeeValues = new ArrayList<>();
        SplitCaseDTO splitCaseGridDTO = new SplitCaseDTO();
        splitCaseGridDTO.setProductType(CukesConstants.PRODUCT_TYPE);
        splitCaseGridDTO.setSplitCaseFee(fee);
        splitCaseGridDTO.setEffectiveDate(effectiveDate);
        splitCaseGridDTO.setExpirationDate(expirationDate);
        splitCaseGridDTO.setUnit(unit);
        splitCaseGridDTO.setLessCaseRuleId(feeType);
        splitCaseGridDTO.setItemPriceId(String.valueOf(CukesConstants.PRODUCT_PRICE_ID));
        splitCaseFeeValues.add(splitCaseGridDTO);
        splitCaseDTO.setSplitCaseFeeValues(splitCaseFeeValues);
        return splitCaseDTO;
    }

    @When("^request is made to save splitcase values$")
    public void enter_splitcase_values() throws Exception {
    	enter_splitcase_values_fee_type(LessCasePriceRule.FULL_CASE_PRICE_DIV_PACK.getCode());
    }

    @When("^request is made to save splitcase values with fee type \"([^\"]*)\"$")
    public void enter_splitcase_values_fee_type(int feeType) throws Exception {
        try {
            SplitCaseGridDTO splitCaseDTO = buildSplitCaseDTO(CukesConstants.DEFAULT_SPLITCASE_FEE, CukesConstants.DEFAULT_UNIT,
                    cppDateUtils.getFutureDate(), cppDateUtils.getFutureDate(), feeType);
            final ResponseEntity<String> response = splitcasecontroller.saveSplitCase(splitCaseDTO);
            setAttribute("splitcase_fee", splitCaseDTO);
            setAttribute("save_splitcaseFee_response", response);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @Then("^the values of splitcase are saved$")
    public void save_splitcase() {
        final ResponseEntity<List<SplitCaseDTO>> actualSplitCaseDTO = splitcasecontroller.fetchSplitCaseFee(getContractPriceProfileSeq(), null,
                null);
        final SplitCaseGridDTO expectedSplitCaseDTO = (SplitCaseGridDTO) getAttribute("splitcase_fee");

        assertThat(actualSplitCaseDTO.getBody().size(), is(1));
        assertThat(actualSplitCaseDTO.getBody().get(0).getItemPriceId(), is(expectedSplitCaseDTO.getSplitCaseFeeValues().get(0).getItemPriceId()));
        assertThat(actualSplitCaseDTO.getBody().get(0).getProductType(), is(expectedSplitCaseDTO.getSplitCaseFeeValues().get(0).getProductType()));
        assertThat(actualSplitCaseDTO.getBody().get(0).getSplitCaseFee(), is(expectedSplitCaseDTO.getSplitCaseFeeValues().get(0).getSplitCaseFee()));
        assertThat(actualSplitCaseDTO.getBody().get(0).getUnit(), is(expectedSplitCaseDTO.getSplitCaseFeeValues().get(0).getUnit()));
    }

    @Then("^the values of splitcase are saved with \"([^\"]*)\"$")
    public void save_splitcase_with_fee_type(String feeValue) {
        final ResponseEntity<List<SplitCaseDTO>> actualSplitCaseDTO = splitcasecontroller.fetchSplitCaseFee(getContractPriceProfileSeq(), null,
                null);
        assertThat(actualSplitCaseDTO.getBody().size(), is(1));
        assertThat(LessCasePriceRule.getByCode(actualSplitCaseDTO.getBody().get(0).getLessCaseRuleId()).toString(), is(feeValue));
    }

    @When("^request is made to edit values of splitcase is entered$")
    public void enter_edit_splitcase_values() throws Exception {

        SplitCaseGridDTO updateSplitCaseDTO = buildSplitCaseDTO(UPDATED_SPLITCASE_FEE, CukesConstants.PERCENT_UNIT, cppDateUtils.getFutureDate(),
                cppDateUtils.getFutureDate(), LessCasePriceRule.FULL_CASE_PRICE_DIV_PACK.getCode());
        splitcasecontroller.saveSplitCase(updateSplitCaseDTO);
        setAttribute("updateSplitCaseDTO", updateSplitCaseDTO);
    }

    @Then("^the values of splitcase are updated$")
    public void update_splitcase() {
        SplitCaseGridDTO updateSplitCaseDTO = (SplitCaseGridDTO) getAttribute("updateSplitCaseDTO");
        final ResponseEntity<List<SplitCaseDTO>> updated_splitcase = splitcasecontroller.fetchSplitCaseFee(getContractPriceProfileSeq(), null,
                null);

        assertThat(updated_splitcase.getBody().size(), is(1));
        assertThat(updated_splitcase.getBody().get(0).getSplitCaseFee(), is(updateSplitCaseDTO.getSplitCaseFeeValues().get(0).getSplitCaseFee()));
        assertThat(updated_splitcase.getBody().get(0).getUnit(), is(updateSplitCaseDTO.getSplitCaseFeeValues().get(0).getUnit()));

    }

    public void enter_splitcase_values_with_date_range(Date effectiveDate, Date expirationDate) throws Exception {
        SplitCaseGridDTO splitCaseDTO = buildSplitCaseDTO(CukesConstants.DEFAULT_SPLITCASE_FEE, CukesConstants.DEFAULT_UNIT, effectiveDate,
                expirationDate, LessCasePriceRule.FULL_CASE_PRICE_DIV_PACK.getCode());
        splitcasecontroller.saveSplitCase(splitCaseDTO);
        setAttribute("splitcase_fee", splitCaseDTO);
    }
}
