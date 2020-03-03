package com.gfs.cpp.acceptanceTests.stepdefs.furtherance;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.corp.component.price.common.types.LessCasePriceRule;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableItems;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableRealCustomer;
import com.gfs.cpp.acceptanceTests.common.data.ChangeTableName;
import com.gfs.cpp.acceptanceTests.common.data.FurtheranceAction;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.acceptanceTests.mocks.ClmApiProxyMocker;
import com.gfs.cpp.acceptanceTests.stepdefs.ActivatePricing.ActivatePricingStepDefs;
import com.gfs.cpp.acceptanceTests.stepdefs.markup.MarkupStepDefs;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceBaseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.dto.furtherance.SplitCaseGridFurtheranceDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;
import com.gfs.cpp.data.furtherance.CppFurtheranceTrackingRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.web.controller.furtherance.FurtheranceController;
import com.gfs.cpp.web.controller.splitcase.SplitCaseController;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class FurtheranceStepDefs {

    private static final String UPDATED_SPLITCASE_FEE = "25.00";

    private static final String MARKUP_NAME = "markupName";

    private static final String DOLLAR_UNIT = "$";

    private static final String UPDATED_MARKUP_VALUE = "30.15";

    private static final Date effectiveDate = new LocalDate(2010, 01, 01).toDate();
    private static final Date expirationDate = new LocalDate(2011, 01, 01).toDate();

    @Autowired
    private FurtheranceController furtheranceController;

    @Autowired
    private CppFurtheranceTrackingRepository cppFurtheranceTrackingRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private CppFurtheranceRepository cppFurtheranceRepository;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private SplitCaseController splitcasecontroller;

    @Autowired
    private ClmApiProxyMocker clmApiProxyMocker;

    @Autowired
    private MarkupStepDefs markupStepDefs;

    @Autowired
    private FurtherananceDataVerifier furtherananceDataVerifier;

    @When("^request is made to check existing furtherance$")
    public void check_for_existing_furtherance() {
        ResponseEntity<Map<String, Boolean>> response = furtheranceController.hasInProgressFurtherance(CukesConstants.PARENT_AGREEMENT_ID);
        setAttribute("HasInProgressFurtheranceResponse", response.getBody());
    }

    @Then("^return in progress flag for furtherance as \"([^\"]*)\"$")
    public void return_in_progress_furtherance_as_false(boolean hasInprogressFurtherance) {

        Map<String, Boolean> result = (Map<String, Boolean>) getAttribute("HasInProgressFurtheranceResponse");

        assertThat(result.get("hasInProgressFurtherance"), equalTo(hasInprogressFurtherance));
    }

    @When("^a request is made to create furtherance$")
    public void on_create_furtherance() {
        try {
            ResponseEntity<FurtheranceBaseDTO> response = furtheranceController.createNewFurtherance(CukesConstants.PARENT_AGREEMENT_ID,
                    CukesConstants.CONTRACT_NAME);
            setAttribute("FurtheranceValidResponse", response.getBody());
        } catch (CPPRuntimeException cpp) {
            setAttribute("FurtheranceInvalidResponse", cpp.getErrorCode());
        }
    }

    @Then("^display error message to user as furtherance can not be created$")
    public void return_error_message_as_contract_in_progress() {

        int errorCode = (Integer) getAttribute("FurtheranceInvalidResponse");

        assertThat(errorCode, equalTo(CPPExceptionType.CANNOT_CREATE_FURTHERANCE.getErrorCode()));
    }

    @When("^a request is made to fetch the furtherance$")
    public void fetch_furtherance_info() {

        ResponseEntity<FurtheranceInformationDTO> response = furtheranceController.fetchFurtheranceInformation(CukesConstants.PARENT_AGREEMENT_ID,
                CukesConstants.CPP_FURTHERANCE_SEQ);
        setAttribute("FurtheranceInformationDTO", response.getBody());

    }

    @Then("^the default values of furtherance is returned$")
    public void return_default_values_of_furtherance() {
        final FurtheranceInformationDTO furtheranceInformationDTO = (FurtheranceInformationDTO) getAttribute("FurtheranceInformationDTO");

        assertThat(furtheranceInformationDTO.getContractPriceProfileSeq(), equalTo(0));
        assertThat(furtheranceInformationDTO.getCppFurtheranceSeq(), equalTo(CukesConstants.CPP_FURTHERANCE_SEQ));
        assertThat(furtheranceInformationDTO.getParentCLMAgreementId(), equalTo(CukesConstants.PARENT_AGREEMENT_ID));
        assertThat(furtheranceInformationDTO.getFurtheranceStatusCode(), equalTo(CukesConstants.FURTHERANCE_INITIATED_CODE));
    }

    @Then("^return furtherance info with new furtherance sequence created$")
    public void return_furtherance_info_with_new_furtherance_seq() {

        FurtheranceBaseDTO result = (FurtheranceBaseDTO) getAttribute("FurtheranceValidResponse");

        assertThat(result.getAgreementId(), equalTo(CukesConstants.AGREEMENT_ID));
        assertThat(result.getCppFurtheranceSeq(), not(0));
        assertThat(result.getContractType(), equalTo(CukesConstants.CLM_CONTRACT_TYPE_REGIIONAL));

    }

    @When("^a request is made to save furtherance information$")
    public void on_save_furtherance() {
        try {
            String furtheranceReferenceContact = CukesConstants.FURTHERANCE_REFERENCE_CONTACT;
            String furtheranceReason = CukesConstants.FURTHERANCE_REASON;
            Date furtheranceEffectiveDate = CukesConstants.FURTHERANCE_EFFECTIVE_DATE;

            FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceDTO(furtheranceReferenceContact, furtheranceReason,
                    furtheranceEffectiveDate);

            setAttribute("savedFurtheranceDTO", furtheranceInformationDTO);
            furtheranceController.saveFurtheranceInformation(furtheranceInformationDTO);

        } catch (CPPRuntimeException cpp) {
            setAttribute("SaveFurtheranceInvalidResponse", cpp.getErrorCode());
        }
    }

    private FurtheranceInformationDTO buildFurtheranceDTO(String furtheranceReferenceContact, String furtheranceReason,
            Date furtheranceEffectiveDate) {
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setContractReferenceTxt(furtheranceReferenceContact);
        furtheranceInformationDTO.setChangeReasonTxt(furtheranceReason);
        furtheranceInformationDTO.setFurtheranceEffectiveDate(furtheranceEffectiveDate);
        furtheranceInformationDTO.setCppFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);
        furtheranceInformationDTO.setParentCLMAgreementId(CukesConstants.PARENT_AGREEMENT_ID);
        return furtheranceInformationDTO;
    }

    @Then("^saved furtherance information with initiated status is returned$")
    public void return_saved_values_of_furtherance() {
        final FurtheranceInformationDTO furtheranceInformationDTO = (FurtheranceInformationDTO) getAttribute("FurtheranceInformationDTO");

        assertThat(furtheranceInformationDTO.getCppFurtheranceSeq(), equalTo(CukesConstants.CPP_FURTHERANCE_SEQ));
        assertThat(furtheranceInformationDTO.getParentCLMAgreementId(), equalTo(CukesConstants.PARENT_AGREEMENT_ID));
        assertThat(furtheranceInformationDTO.getFurtheranceStatusCode(), equalTo(CukesConstants.FURTHERANCE_INITIATED_CODE));
        assertThat(furtheranceInformationDTO.getChangeReasonTxt(), equalTo(CukesConstants.FURTHERANCE_REASON));
        assertThat(furtheranceInformationDTO.getFurtheranceEffectiveDate(), equalTo(CukesConstants.FURTHERANCE_EFFECTIVE_DATE));
        assertThat(furtheranceInformationDTO.getContractReferenceTxt(), equalTo(CukesConstants.FURTHERANCE_REFERENCE_CONTACT));
    }

    @And("^a request is made to update the furtherance information$")
    public void on_update_furtherance() {

        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceDTO(CukesConstants.UPDATED_FURTHERANCE_REFERENCE_CONTACT,
                CukesConstants.UPDATED_FURTHERANCE_REASON, CukesConstants.UPDATED_FURTHERANCE_EFFECTIVE_DATE);

        furtheranceInformationDTO.setContractPriceProfileSeq(getContractPriceProfileSeq());
        saveFurtherance(furtheranceInformationDTO);
    }

    private void saveFurtherance(FurtheranceInformationDTO furtheranceInformationDTO) {
        try {
            furtheranceController.saveFurtheranceInformation(furtheranceInformationDTO);
        } catch (CPPRuntimeException cpp) {
            setAttribute("SaveFurtheranceInvalidResponse", cpp.getErrorCode());
        }
    }

    @Then("^updated furtherance information is returned$")
    public void return_updated_values_of_furtherance() {
        final FurtheranceInformationDTO furtheranceInformationDTO = (FurtheranceInformationDTO) getAttribute("FurtheranceInformationDTO");

        assertThat(furtheranceInformationDTO.getCppFurtheranceSeq(), equalTo(CukesConstants.CPP_FURTHERANCE_SEQ));
        assertThat(furtheranceInformationDTO.getParentCLMAgreementId(), equalTo(CukesConstants.PARENT_AGREEMENT_ID));
        assertThat(furtheranceInformationDTO.getFurtheranceStatusCode(), equalTo(CukesConstants.FURTHERANCE_INITIATED_CODE));
        assertThat(furtheranceInformationDTO.getChangeReasonTxt(), equalTo(CukesConstants.UPDATED_FURTHERANCE_REASON));
        assertThat(furtheranceInformationDTO.getFurtheranceEffectiveDate(), equalTo(CukesConstants.UPDATED_FURTHERANCE_EFFECTIVE_DATE));
        assertThat(furtheranceInformationDTO.getContractReferenceTxt(), equalTo(CukesConstants.UPDATED_FURTHERANCE_REFERENCE_CONTACT));
    }

    @Then("^no change on actual contract pricing details after furtherance$")
    public void return_contract_pricing_values() {

        final ContractPricingDTO contractPricingDTO = (ContractPricingDTO) getAttribute("contractPricingDTO");

        assertThat(contractPricingDTO.getPricingEffectiveDate(), equalTo(CukesConstants.EFFECTIVE_DATE));
        assertThat(contractPricingDTO.getAssessmentFeeFlag(), equalTo(true));
        assertThat(contractPricingDTO.getPriceAuditFlag(), equalTo(true));
        assertThat(contractPricingDTO.getPriceVerificationFlag(), equalTo(true));
        assertThat(contractPricingDTO.getScheduleForCostChange(), equalTo(CukesConstants.SCHEDULE_FOR_COST_GREGORIAN));
        assertThat(contractPricingDTO.getTransferFeeFlag(), equalTo(true));
        assertThat(contractPricingDTO.getVersionNbr(), equalTo(CukesConstants.VERSION_NBR));

    }

    @When("^a request is made to delete \"([^\"]*)\" for furtherance$")
    public void request_to_delete_item_level_markup(AcceptableItems acceptableItem) {

        furtheranceController.deleteItemLevelMarkup(getContractPriceProfileSeq(), CukesConstants.CPP_FURTHERANCE_SEQ,
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.CMG_CUSTOMER_TYPE_ID, acceptableItem.getItemId(), CukesConstants.ITEM_DESC);
        setAttribute("itemId", acceptableItem.getItemId());
    }

    @When("^a request is made to delete \"([^\"]*)\" with a future item for furtherance$")
    public void request_to_delete_item(AcceptableItems acceptableItem) {

        furtheranceController.deleteMappedItemForFurtherance(getContractPriceProfileSeq(), CukesConstants.CPP_FURTHERANCE_SEQ,
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.CMG_CUSTOMER_TYPE_ID, acceptableItem.getItemId(),
                CukesConstants.FUTURE_ITEM_DESC);
        setAttribute("itemId", acceptableItem.getItemId());
    }

    @And("^the CIP record for CMG should be expired for \"([^\"]*)\"$")
    public void check_item_level_markup_expired(AcceptableItems acceptableItem) {
        List<ItemLevelMarkupDTO> result = customerItemPriceRepository.fetchItemTypeMarkups(getContractPriceProfileSeq(),
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID);

        for (ItemLevelMarkupDTO itemLevelMarkupDTO : result) {
            assertThat(itemLevelMarkupDTO.getItemId(), not(acceptableItem.getItemId()));
        }
    }

    @When("^a request is made to update splitcase in furtherance$")
    public void update_splitcase_values() {
        SplitCaseGridFurtheranceDTO splitCaseGridFurtheranceDTO = buildSplitCaseGridFurtheranceDTO();
        furtheranceController.saveSplitCaseFeeForFurtherance(splitCaseGridFurtheranceDTO);
        setAttribute("splitCaseGridFurtheranceDTO", splitCaseGridFurtheranceDTO);
    }

    @Then("^the values of splitcase are updated in furtherance$")
    public void verify_splitcase_values_updated() {

        final ResponseEntity<List<SplitCaseDTO>> splitcase = splitcasecontroller.fetchSplitCaseFee(getContractPriceProfileSeq(),
                cppDateUtils.getFutureDate(), cppDateUtils.getFutureDate());

        assertThat(splitcase.getBody().size(), equalTo(1));
        assertThat(splitcase.getBody().get(0).getSplitCaseFee(), is(UPDATED_SPLITCASE_FEE));
    }

    @When("a request is made to fetch items mapped with a future item for furtherance$")
    public void request_to_fetch_future_items() throws Exception {
        ResponseEntity<ItemAssignmentWrapperDTO> response = furtheranceController.fetchMappedItemsForFurtherance(getContractPriceProfileSeq(),
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.CMG_CUSTOMER_TYPE_ID, CukesConstants.FUTURE_ITEM_DESC);
        setAttribute("mappedItemResponse", response.getBody());
    }

    @Then("^the mapped item details are returned$")
    public void return_future_items() {
        final ItemAssignmentWrapperDTO result = ((ItemAssignmentWrapperDTO) getAttribute("mappedItemResponse"));

        assertThat(result.getFutureItemDesc(), is(CukesConstants.FUTURE_ITEM_DESC));
        assertThat(result.getIsFutureItemSaved(), is(true));
        assertThat(result.getGfsCustomerId(), is(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
        assertThat(result.getGfsCustomerTypeCode(), is(CukesConstants.CMG_CUSTOMER_TYPE_ID));
        assertThat(result.getItemAssignmentList().size(), is(2));

        ItemAssignmentDTO itemAssignmentDTO = result.getItemAssignmentList().get(0);

        assertThat(itemAssignmentDTO.getItemDescription(), is("Test"));
        assertThat(itemAssignmentDTO.getIsItemSaved(), is(true));
        assertThat(itemAssignmentDTO.getItemId(), is(CukesConstants.NEW_ITEM_ID_TO_ASSIGN));

    }

    @Given("^tracking is added for \"([^\"]*)\" action for \"([^\"]*)\" type and \"([^\"]*)\" table$")
    public void verify_tracking(FurtheranceAction furtheranceAction, AcceptableItems item, ChangeTableName changeTableName) {
        List<CPPFurtheranceTrackingDTO> cppFurtheranceTrackingDTOList = cppFurtheranceTrackingRepository
                .fetchFurtheranceDetailsByCPPFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);

        Integer actualTracking = fetchFurtheranceActionCode(item, changeTableName, (String) getAttribute("itemId"));

        assertThat(cppFurtheranceTrackingDTOList.size(), equalTo(1));
        assertThat(actualTracking, equalTo(furtheranceAction.getFurtheranceActionCode()));
    }

    @Given("^tracking is deleted for \"([^\"]*)\" type and \"([^\"]*)\" table for future item$")
    public void verify_tracking_deleted(AcceptableItems item, ChangeTableName changeTableName) {
        List<CPPFurtheranceTrackingDTO> cppFurtheranceTrackingDTOList = cppFurtheranceTrackingRepository
                .fetchFurtheranceDetailsByCPPFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);
        Integer actualTracking = fetchFurtheranceActionCode(item, changeTableName, CukesConstants.NEW_ITEM_ID_FOR_FURTHERANCE);

        assertThat(cppFurtheranceTrackingDTOList.size(), equalTo(0));
        assertThat(actualTracking, nullValue());
    }

    @When("^a request is made to assign items with a future item for \"([^\"]*)\"$")
    public void assign_items_with_future_items(AcceptableItems acceptableItem) throws Exception {

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(acceptableItem.getItemId());
        try {
            ResponseEntity<StatusDTO> response = furtheranceController.assignItemsForFurtherance(itemAssignmentWrapperDTO);

            setAttribute("itemId", acceptableItem.getItemId());
            setAttribute("itemAssignmentWrapperDTO", itemAssignmentWrapperDTO);
            setAttribute("assignItems_response", response);
            setAttribute(CukesConstants.CPP_ERROR_CODE, response.getStatusCodeValue());
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
            setAttribute(CukesConstants.CPP_ERROR_CODE, cpp.getErrorCode());
            setAttribute(CukesConstants.CPP_ERROR_MESSAGE, cpp.getMessage());
        }
    }

    @When("^a request with already existing items is made to assign items with future item for \"([^\"]*)\"$")
    public void assign_already_existing_items_with_future_items(AcceptableItems acceptableItem) throws Exception {

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(acceptableItem.getItemId());

        StatusDTO statusDTO = furtheranceController.assignItemsForFurtherance(itemAssignmentWrapperDTO).getBody();

        setAttribute("statusDTO", statusDTO);
    }

    @When("^a request with duplicate items is made to assign items with future item for \"([^\"]*)\"$")
    public void assign_duplicate_items_with_future_items(AcceptableItems acceptableItem) throws Exception {

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(acceptableItem.getItemId());
        itemAssignmentWrapperDTO.getItemAssignmentList().addAll(buildItemAssignmentDTOList(acceptableItem.getItemId()));

        StatusDTO statusDTO = furtheranceController.assignItemsForFurtherance(itemAssignmentWrapperDTO).getBody();

        setAttribute("statusDTO", statusDTO);
    }

    @And("^the values of item markup are saved for future item for furtherance$")
    public void fetch_item_markup_for_furtherance() {
        List<ProductTypeMarkupDTO> fetchAllMarkup = customerItemPriceRepository.fetchAllMarkup(getContractPriceProfileSeq(),
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID);

        assertThat(fetchAllMarkup.size(), equalTo(5));
        ProductTypeMarkupDTO productTypeMarkupDTO = fetchAllMarkup.get(3);
        assertThat(productTypeMarkupDTO.getItemPriceId(), equalTo(Integer.valueOf(CukesConstants.NEW_ITEM_ID_TO_ASSIGN)));
        assertThat(productTypeMarkupDTO.getMarkupType(), equalTo(2));
        assertThat(productTypeMarkupDTO.getUnit(), equalTo(CukesConstants.DEFAULT_UNIT));
        assertThat(productTypeMarkupDTO.getMarkup(), equalTo(CukesConstants.MARKUP_VALUE));
    }

    @And("^the status of Furtherance is in \"([^\"]*)\"$")

    public void fetch_saved_furtherance_info(FurtheranceStatus furtheranceStatus) {
        FurtheranceInformationDTO furtheranceInformationDTO = (FurtheranceInformationDTO) getAttribute("savedFurtheranceDTO");
        furtheranceInformationDTO.setFurtheranceStatusCode(furtheranceStatus.getCode());
        cppFurtheranceRepository.updateCPPFurtherance(furtheranceInformationDTO, CukesConstants.CURRENT_USER_ID);
    }

    @When("^a request is made to check activate pricing for furtherance is enabled$")
    public void verify_activate_pricing_for_furtherance() {

        ResponseEntity<Map<String, Boolean>> response = furtheranceController.enableActivatePricingForFurtherance(CukesConstants.CPP_FURTHERANCE_SEQ,
                ClmContractStatus.EXECUTED.value);
        setAttribute("activatePricingButtonResponse", response.getBody());

    }

    @Then("^return activate pricing flag for furtherance as \"([^\"]*)\"$")
    public void enable_activate_pricing_for_furtherance(boolean enableActivatePricingFlag) {
        Map<String, Boolean> isEnableMap = (Map<String, Boolean>) getAttribute("activatePricingButtonResponse");

        assertThat(isEnableMap.get(CPPConstants.ENABLE_ACTIVATE_PRICING_BUTTON), equalTo(enableActivatePricingFlag));
    }

    @When("^a request is made to attach furtherance document to clm$")
    public void request_to_attach_furtherance_document_to_clm() {
        FurtheranceBaseDTO furtheranceBaseDTO = new FurtheranceBaseDTO();
        furtheranceBaseDTO.setAgreementId(CukesConstants.PARENT_AGREEMENT_ID);
        furtheranceBaseDTO.setContractType(CukesConstants.CLM_CONTRACT_TYPE_REGIIONAL);
        furtheranceBaseDTO.setCppFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);
        try {
            ResponseEntity<Void> response = furtheranceController.savePricingDocumentForFurtherance(furtheranceBaseDTO);
            setAttribute("FurtheranceAttached_response", response);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }

    }

    @Then("^the furtherance document is attached in CLM$")
    public void the_furtherance_document_is_attached_in_CLM() throws Throwable {
        clmApiProxyMocker.isFurtheranceDocumentAttachedToClm();
    }

    @And("^furtherance document GUID is saved in contract$")
    public void furtherance_document_guid_is_saved_in_contract() throws Throwable {
        FurtheranceInformationDTO actualContract = cppFurtheranceRepository
                .fetchFurtheranceDetailsByFurtheranceSeq((CukesConstants.CPP_FURTHERANCE_SEQ));
        assertThat(actualContract.getFurtheranceDocumentGUID(), is(CukesConstants.EXHIBIT_SYS_ID));
    }

    @And("^a request is made to save \"([^\"]*)\" in markup for furtherance$")
    public void save_markup_for_furtherance(AcceptableItems acceptableItems) {
        MarkupWrapperDTO markupWrapperDTO = buildMarkupWrapper(
                markupStepDefs.buildItemLeveMarkup(acceptableItems.ITEM_TYPE_FOR_FUTHERANCE.ITEM_TYPE_FOR_FUTHERANCE.getItemId(),
                        CukesConstants.FURTHERANCE_ITEM_DESC, CukesConstants.MARKUP_VALUE, DOLLAR_UNIT, CukesConstants.PER_CASE_TYPE, false,
                        effectiveDate, expirationDate),
                markupStepDefs.buildProductTypeMarkup(CukesConstants.MARKUP_VALUE, DOLLAR_UNIT, CukesConstants.PRODUCT_PRICE_ID,
                        CukesConstants.PER_CASE_TYPE, effectiveDate, expirationDate),
                markupStepDefs.buildSubgroupMarkup(CukesConstants.SUBGROUP_ID_VALID, CukesConstants.SUBGROUP_DESCRIPTION, CukesConstants.MARKUP_VALUE,
                        DOLLAR_UNIT, CukesConstants.PER_CASE_TYPE, effectiveDate, expirationDate));
        setAttribute("itemId", acceptableItems.ITEM_TYPE_FOR_FUTHERANCE.ITEM_TYPE_FOR_FUTHERANCE.getItemId());
        furtheranceController.saveMarkupForFurtherance(markupWrapperDTO);
    }

    @And("^a request is made to update \"([^\"]*)\" in markup for furtherance$")
    public void update_markup_for_furtherance(AcceptableItems acceptableItems) {
        MarkupWrapperDTO markupWrapperDTO = null;
        if (acceptableItems.getItemPriceLevelCode() == ItemPriceLevel.ITEM.getCode()) {
            markupWrapperDTO = buildMarkupWrapper(
                    markupStepDefs.buildItemLeveMarkup(acceptableItems.getItemId(), CukesConstants.ITEM_DESC, UPDATED_MARKUP_VALUE, DOLLAR_UNIT,
                            CukesConstants.PER_CASE_TYPE, false, effectiveDate, expirationDate),
                    Collections.<ProductTypeMarkupDTO> emptyList(), Collections.<SubgroupMarkupDTO> emptyList());
        }
        if (acceptableItems.getItemPriceLevelCode() == ItemPriceLevel.PRODUCT_TYPE.getCode()) {
            markupWrapperDTO = buildMarkupWrapper(Collections.<ItemLevelMarkupDTO> emptyList(), markupStepDefs.buildProductTypeMarkup(
                    UPDATED_MARKUP_VALUE, DOLLAR_UNIT, CukesConstants.PRODUCT_PRICE_ID, CukesConstants.PER_CASE_TYPE, effectiveDate, expirationDate),

                    Collections.<SubgroupMarkupDTO> emptyList());
        }
        if (acceptableItems.getItemPriceLevelCode() == ItemPriceLevel.SUBGROUP.getCode()) {
            markupWrapperDTO = buildMarkupWrapper(Collections.<ItemLevelMarkupDTO> emptyList(), Collections.<ProductTypeMarkupDTO> emptyList(),
                    markupStepDefs.buildSubgroupMarkup(acceptableItems.getItemId(), CukesConstants.SUBGROUP_DESCRIPTION, UPDATED_MARKUP_VALUE,
                            DOLLAR_UNIT, CukesConstants.PER_CASE_TYPE, effectiveDate, expirationDate));
        }
        setAttribute("itemId", acceptableItems.getItemId());
        furtheranceController.saveMarkupForFurtherance(markupWrapperDTO);
    }

    @Then("^\"([^\"]*)\" is saved for furtherance$")
    public void item_saved_for_furtherance(AcceptableItems acceptableItems) {
        List<ProductTypeMarkupDTO> fetchAllMarkup = customerItemPriceRepository.fetchAllMarkup(getContractPriceProfileSeq(),
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        Map<Integer, ProductTypeMarkupDTO> productTypeMarkupDTOMap = getProductTypeMarkupDTOMap(fetchAllMarkup);
        assertThat(fetchAllMarkup.size(), equalTo(5));
        assertThat(
                productTypeMarkupDTOMap.containsKey(Integer.parseInt(acceptableItems.ITEM_TYPE_FOR_FUTHERANCE.ITEM_TYPE_FOR_FUTHERANCE.getItemId())),
                equalTo(true));
        assertThat(productTypeMarkupDTOMap.get(Integer.parseInt(acceptableItems.ITEM_TYPE_FOR_FUTHERANCE.ITEM_TYPE_FOR_FUTHERANCE.getItemId()))
                .getMarkup(), equalTo(CukesConstants.MARKUP_VALUE));
        assertThat(productTypeMarkupDTOMap.get(Integer.parseInt(CukesConstants.ITEM_ID)).getMarkup(), equalTo(CukesConstants.MARKUP_VALUE));
        assertThat(productTypeMarkupDTOMap.get(CukesConstants.PRODUCT_PRICE_ID).getMarkup(), equalTo(CukesConstants.MARKUP_VALUE));
        assertThat(productTypeMarkupDTOMap.get(Integer.parseInt(CukesConstants.SUBGROUP_ID_VALID)).getMarkup(), equalTo(CukesConstants.MARKUP_VALUE));

    }

    @Then("^\"([^\"]*)\" in markup is updated for furtherance$")
    public void markups_updated_for_furtherance(AcceptableItems item) {
        List<ProductTypeMarkupDTO> fetchAllMarkup = customerItemPriceRepository.fetchAllMarkup(getContractPriceProfileSeq(),
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        Map<Integer, ProductTypeMarkupDTO> productTypeMarkupDTOMap = getProductTypeMarkupDTOMap(fetchAllMarkup);
        assertThat(fetchAllMarkup.size(), equalTo(4));
        if (item.getItemPriceLevelCode() == ItemPriceLevel.ITEM.getCode()) {
            assertThat(productTypeMarkupDTOMap.get(Integer.parseInt(CukesConstants.ITEM_ID)).getMarkup(), equalTo(UPDATED_MARKUP_VALUE));
        }
        if (item.getItemPriceLevelCode() == ItemPriceLevel.PRODUCT_TYPE.getCode()) {
            assertThat(productTypeMarkupDTOMap.get(CukesConstants.PRODUCT_PRICE_ID).getMarkup(), equalTo(UPDATED_MARKUP_VALUE));
        }
        if (item.getItemPriceLevelCode() == ItemPriceLevel.SUBGROUP.getCode()) {
            assertThat(productTypeMarkupDTOMap.get(Integer.parseInt(CukesConstants.SUBGROUP_ID_VALID)).getMarkup(), equalTo(UPDATED_MARKUP_VALUE));
        }
    }

    @And("^tracking is added for \"([^\"]*)\" action for \"([^\"]*)\" table$")
    public void verify_tracking_for_markup_update(FurtheranceAction furtheranceAction, ChangeTableName changeTableName) {
        List<CPPFurtheranceTrackingDTO> cppFurtheranceTrackingDTOList = cppFurtheranceTrackingRepository
                .fetchFurtheranceDetailsByCPPFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);

        assertThat(cppFurtheranceTrackingDTOList.size(), equalTo(3));
        assertThat(cppFurtheranceTrackingDTOList.get(0).getFurtheranceActionCode(), equalTo(furtheranceAction.getFurtheranceActionCode()));
        assertThat(cppFurtheranceTrackingDTOList.get(1).getFurtheranceActionCode(), equalTo(furtheranceAction.getFurtheranceActionCode()));
        assertThat(cppFurtheranceTrackingDTOList.get(2).getFurtheranceActionCode(), equalTo(furtheranceAction.getFurtheranceActionCode()));
    }

    @Then("^return furtherance info with new furtherance sequence created for latest activated agreement$")
    public void return_furtherance_info_with_latest_activated_agreement() {

        FurtheranceBaseDTO result = (FurtheranceBaseDTO) getAttribute("FurtheranceValidResponse");

        assertThat(result.getAgreementId(), equalTo(CukesConstants.AMENDMENT_AGREEMENT_ID));
        assertThat(result.getCppFurtheranceSeq(), not(0));
        assertThat(result.getContractType(), equalTo(CukesConstants.CLM_CONTRACT_TYPE_REGIONAL_AMENDMENT));

    }

    private Map<Integer, ProductTypeMarkupDTO> getProductTypeMarkupDTOMap(List<ProductTypeMarkupDTO> productTypeMarkupDTOList) {
        Map<Integer, ProductTypeMarkupDTO> productTypeMarkupDTOMap = null;
        for (ProductTypeMarkupDTO productTypeMarkupDTO : productTypeMarkupDTOList) {
            if (productTypeMarkupDTOMap == null) {
                productTypeMarkupDTOMap = new HashMap<>();
            }
            productTypeMarkupDTOMap.put(productTypeMarkupDTO.getItemPriceId(), productTypeMarkupDTO);
        }
        return productTypeMarkupDTOMap;
    }

    private MarkupWrapperDTO buildMarkupWrapper(List<ItemLevelMarkupDTO> itemLevelMarkup, List<ProductTypeMarkupDTO> productTypeMarkupList,
            List<SubgroupMarkupDTO> subgroupMarkupList) {

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();

        markupWrapper.setItemLevelMarkupList(itemLevelMarkup);
        markupWrapper.setSubgroupMarkupList(subgroupMarkupList);
        markupWrapper.setIsMarkupSaved(false);
        markupWrapper.setGfsCustomerId(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        markupWrapper.setProductMarkupList(productTypeMarkupList);
        markupWrapper.setGfsCustomerType(CukesConstants.CMG_CUSTOMER_TYPE_ID);
        markupWrapper.setMarkupName(MARKUP_NAME);
        markupWrapper.setContractPriceProfileSeq(getContractPriceProfileSeq());
        markupWrapper.setCppFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);
        return markupWrapper;
    }

    private ItemAssignmentWrapperDTO buildItemAssignmentWrapperDTO(String itemId) {
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(getContractPriceProfileSeq());
        itemAssignmentWrapperDTO.setCppFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);
        itemAssignmentWrapperDTO.setExceptionName(CukesConstants.CONTRACT_NAME);
        itemAssignmentWrapperDTO.setFutureItemDesc(CukesConstants.FUTURE_ITEM_DESC);
        itemAssignmentWrapperDTO.setGfsCustomerId(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(CukesConstants.CMG_CUSTOMER_TYPE_ID);
        itemAssignmentWrapperDTO.setIsFutureItemSaved(false);
        itemAssignmentWrapperDTO.setItemAssignmentList(buildItemAssignmentDTOList(itemId));
        return itemAssignmentWrapperDTO;
    }

    private List<ItemAssignmentDTO> buildItemAssignmentDTOList(String itemId) {
        List<ItemAssignmentDTO> itemAssignmentList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription(CukesConstants.ITEM_DESC);
        itemAssignmentDTO.setItemId(itemId);
        itemAssignmentList.add(itemAssignmentDTO);
        return itemAssignmentList;
    }

    private SplitCaseGridFurtheranceDTO buildSplitCaseGridFurtheranceDTO() {
        SplitCaseGridFurtheranceDTO splitCaseDTO = new SplitCaseGridFurtheranceDTO();
        splitCaseDTO.setContractPriceProfileSeq(getContractPriceProfileSeq());
        List<SplitCaseDTO> splitCaseFeeValues = new ArrayList<>();
        SplitCaseDTO splitCaseGridDTO = new SplitCaseDTO();
        splitCaseGridDTO.setProductType(CukesConstants.PRODUCT_TYPE);
        splitCaseGridDTO.setSplitCaseFee(UPDATED_SPLITCASE_FEE);
        splitCaseGridDTO.setEffectiveDate(cppDateUtils.getFutureDate());
        splitCaseGridDTO.setExpirationDate(cppDateUtils.getFutureDate());
        splitCaseGridDTO.setUnit(CukesConstants.DEFAULT_UNIT);
        splitCaseGridDTO.setLessCaseRuleId(LessCasePriceRule.FULL_CASE_PRICE_DIV_PACK.getCode());
        splitCaseGridDTO.setItemPriceId(String.valueOf(CukesConstants.PRODUCT_PRICE_ID));
        splitCaseFeeValues.add(splitCaseGridDTO);
        splitCaseDTO.setSplitCaseFeeValues(splitCaseFeeValues);
        splitCaseDTO.setCppFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);
        return splitCaseDTO;
    }

    private CPPFurtheranceTrackingDTO buildSavedFurthranceTracking(int itemLevelCode, String changeTableName, String itemPriceId) {
        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = new CPPFurtheranceTrackingDTO();
        furtheranceTrackingDTO.setCppFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);
        furtheranceTrackingDTO.setItemPriceId(itemPriceId);
        furtheranceTrackingDTO.setGfsCustomerId(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        furtheranceTrackingDTO.setGfsCustomerTypeCode(CPPConstants.CMG_CUSTOMER_TYPE_CODE);
        furtheranceTrackingDTO.setItemPriceLevelCode(itemLevelCode);
        furtheranceTrackingDTO.setChangeTableName(changeTableName);
        return furtheranceTrackingDTO;
    }

    private Integer fetchFurtheranceActionCode(AcceptableItems item, ChangeTableName changeTableName, String itemId) {
        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = new CPPFurtheranceTrackingDTO();
        if (item.getItemPriceLevelCode() == ItemPriceLevel.PRODUCT_TYPE.getCode()) {
            furtheranceTrackingDTO = buildSavedFurthranceTracking(item.getItemPriceLevelCode(), changeTableName.getTableName(),
                    String.valueOf(CukesConstants.PRODUCT_PRICE_ID));
        } else if (item.getItemPriceLevelCode() == ItemPriceLevel.ITEM.getCode()
                || item.getItemPriceLevelCode() == ItemPriceLevel.SUBGROUP.getCode()) {
            furtheranceTrackingDTO = buildSavedFurthranceTracking(item.getItemPriceLevelCode(), changeTableName.getTableName(), itemId);
        }
        return cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(furtheranceTrackingDTO);
    }

    @When("^request is made to activate furtherance price activation for clm status as \"([^\"]*)\"$")
    public void activatePricingForFurtherance(ClmContractStatus clmContractStatus) throws Throwable {
        activate_pricing_for_furtherance(CukesConstants.CPP_FURTHERANCE_SEQ, clmContractStatus.value);
    }

    private void activate_pricing_for_furtherance(int contractPriceProfileSeq, String clmContractStatus) throws Throwable {
        try {
            ResponseEntity<Map<String, String>> response = furtheranceController.activatePricingForFurtherance(contractPriceProfileSeq,
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

    @Then("^display message to user for original contract not in valid clm status$")
    public void return_contract_status_with_message_for_invalid_contract_for_clm_status() {
        final String result = (String) getAttribute(CukesConstants.CPP_ERROR_MESSAGE);
        assertThat(result, CoreMatchers.containsString(
                "The contract is not ready for activating price. Its CLM status is Waiting For Approval. Pricing can be activated only once the CLM status is EXECUTED."));
    }

    @Then("^update furtherance status to \"([^\"]*)\"$")
    public void update_furtherance_status(FurtheranceStatus furtheranceStatus) {
        furtherananceDataVerifier.updateFurtheranceStatus(CukesConstants.CPP_FURTHERANCE_SEQ, furtheranceStatus);
    }

    @Then("^display message to user for furtherance not in valid furtherance status$")
    public void return_contract_status_with_message_for_invalid_contract_for_furtherance_status() {
        final String result = (String) getAttribute(CukesConstants.CPP_ERROR_MESSAGE);
        assertThat(result, CoreMatchers.containsString("Furtherance is in In-Progress. It has to be saved before pricing can be activated"));
    }

    @Then("^existing customer entries for furtherance for \"([^\"]*)\" in CIP table for \"([^\"]*)\" is expired with expiration date as (.+)$$")
    public void verifyExpringExistingCIPEntries(AcceptableRealCustomer realCustomer, AcceptableItems acceptableItems, String expirationDateToSet) {
        Date effectiveDate = convertToDate(expirationDateToSet);
        String result = furtherananceDataVerifier.validateCIPEntriesForExistingContract(acceptableItems, realCustomer, effectiveDate);
        assertThat(result, is(acceptableItems.getItemId()));
    }

    @When("^a request is made to save furtherance information with effective date as (.+)$")
    public void on_save_furtherance_with_effective_date(String furtheranceEffectiveDate) {
        try {
            Date effectiveDate = convertToDate(furtheranceEffectiveDate);
            FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
            furtheranceInformationDTO.setContractReferenceTxt(CukesConstants.FURTHERANCE_REFERENCE_CONTACT);
            furtheranceInformationDTO.setChangeReasonTxt(CukesConstants.FURTHERANCE_REASON);
            furtheranceInformationDTO.setFurtheranceEffectiveDate(effectiveDate);
            furtheranceInformationDTO.setCppFurtheranceSeq(CukesConstants.CPP_FURTHERANCE_SEQ);
            furtheranceInformationDTO.setParentCLMAgreementId(CukesConstants.PARENT_AGREEMENT_ID);

            furtheranceController.saveFurtheranceInformation(furtheranceInformationDTO);

        } catch (CPPRuntimeException cpp) {
            setAttribute("SaveFurtheranceInvalidResponse", cpp.getErrorCode());
        }
    }

    @When("^a request is made to update \"([^\"]*)\" for furtherance$")
    public void on_update_item_in_markup_during_furtherance(AcceptableItems acceptableItem) {

        MarkupWrapperDTO markupWrapperDTO = null;
        if ((CukesConstants.NEW_ITEM_ID_FOR_FURTHERANCE.equals(acceptableItem.getItemId()))
                || (CukesConstants.ITEM_PRICE_ID.equals(acceptableItem.getItemId()))) {

            markupWrapperDTO = buildMarkupWrapper(
                    markupStepDefs.buildItemLeveMarkup(acceptableItem.getItemId(), CukesConstants.ITEM_DESC, UPDATED_MARKUP_VALUE, DOLLAR_UNIT,
                            CukesConstants.PER_CASE_TYPE, Boolean.FALSE, cppDateUtils.getCurrentDate(), cppDateUtils.getFutureDate()),
                    Collections.EMPTY_LIST, Collections.EMPTY_LIST);

        } else if (CukesConstants.SUBGROUP_ID_VALID.equals(acceptableItem.getItemId())) {

            markupWrapperDTO = buildMarkupWrapper(Collections.EMPTY_LIST, Collections.EMPTY_LIST,
                    markupStepDefs.buildSubgroupMarkup(CukesConstants.SUBGROUP_ID_VALID, CukesConstants.SUBGROUP_DESCRIPTION, UPDATED_MARKUP_VALUE,
                            DOLLAR_UNIT, CukesConstants.PER_CASE_TYPE, cppDateUtils.getCurrentDate(), cppDateUtils.getFutureDate()));
        } else {

            markupWrapperDTO = buildMarkupWrapper(Collections.EMPTY_LIST, markupStepDefs.buildProductTypeMarkup(UPDATED_MARKUP_VALUE, DOLLAR_UNIT,
                    CukesConstants.PRODUCT_PRICE_ID, CukesConstants.PER_CASE_TYPE, cppDateUtils.getCurrentDate(), cppDateUtils.getFutureDate()),
                    Collections.EMPTY_LIST);
        }
        furtheranceController.saveMarkupForFurtherance(markupWrapperDTO);

    }

    @When("^a request is made to add \"([^\"]*)\" for furtherance$")
    public void on_adding_item_in_markup_during_furtherance(AcceptableItems acceptableItem) {

        Date expirationDate = convertToDate("farOfDate");
        furtherananceDataVerifier.addItemLevelToMarkup(cppDateUtils.getCurrentDate(), expirationDate, acceptableItem);

    }

    @Then("^new record for subgroup is created with effective date as (.+)$")
    public void verify_subgroup_is_updated(String EffectiveDateToSet) {

        Date effectiveDate = convertToDate(EffectiveDateToSet);
        String itemPriceId = furtherananceDataVerifier.validateSubgroupIsCreatedForRealCustomer(effectiveDate);
        assertThat(itemPriceId, is(String.valueOf(CukesConstants.SUBGROUP_ID_VALID)));

    }

    @Then("^existing splitcase fee is expired with expiration date as (.+)$")
    public void verify_splitcase_fee_is_expired(String expirationDateToSet) {

        Date expirationDate = convertToDate(expirationDateToSet);
        String itemPriceId = furtherananceDataVerifier.validateSplitCaseFeeIsExpiredForRealCustomer(expirationDate);
        assertThat(itemPriceId, is(String.valueOf(CukesConstants.PRODUCT_PRICE_ID)));
    }

    @Then("^new record for splitcase fee is created with effective date as (.+)$")
    public void verify_splitcase_fee_is_updated(String EffectiveDateToSet) {

        Date effectiveDate = convertToDate(EffectiveDateToSet);
        String itemPriceId = furtherananceDataVerifier.validateSplitCaseFeeIsCreatedForRealCustomer(effectiveDate);
        assertThat(itemPriceId, is(String.valueOf(CukesConstants.PRODUCT_PRICE_ID)));

    }

    @Then("^new record for product type is created with effective date as (.+)$")
    public void verify_product_type_is_updated(String EffectiveDateToSet) {

        Date effectiveDate = convertToDate(EffectiveDateToSet);
        String itemPriceId = furtherananceDataVerifier.validateProductTypeIsCreatedForRealCustomer(effectiveDate);
        assertThat(itemPriceId, is(String.valueOf(CukesConstants.PRODUCT_PRICE_ID)));

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

}