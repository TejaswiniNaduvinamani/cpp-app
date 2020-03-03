package com.gfs.cpp.acceptanceTests.stepdefs.markup;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableMarkupType;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableSubgroups;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.acceptanceTests.mocks.CustomerGroupServiceProxyMocker;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.ExceptionMarkupRenameDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentHelper;
import com.gfs.cpp.component.markup.MarkupService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.web.controller.markup.MarkupController;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import junit.framework.AssertionFailedError;

@SuppressWarnings("unchecked")
public class MarkupStepDefs {

    private static final String MARKUP_NAME = "markupName";

    private static final String DOLLAR_UNIT = "$";

    private static final String UPDATED_MARKUP_VALUE = "11.43";

    private static final Date effectiveDate = new LocalDate(2010, 01, 01).toDate();
    private static final Date expirationDate = new LocalDate(2011, 01, 01).toDate();

    @Autowired
    private MarkupController markupController;

    @Autowired
    private MarkupService markupService;

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private CustomerGroupServiceProxyMocker customerServiceProxyMocker;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private ItemAssignmentHelper itemAssignmentHelper;

    @When("^a request is made to get the default values of markup$")
    public void request_to_get_default_markup() throws Exception {
        final ResponseEntity<List<MarkupWrapperDTO>> markup = markupController.fetchMarkups(getContractPriceProfileSeq(),
                cppDateUtils.getFutureDate(), cppDateUtils.getFutureDate());

        setAttribute("markup", markup);
    }

    @Then("^the default markup structure values is returned$")
    public void return_default_markup() {
        final List<MarkupWrapperDTO> markup = ((ResponseEntity<List<MarkupWrapperDTO>>) getAttribute("markup")).getBody();

        assertThat(markup.size(), is(1));
        assertThat(markup.get(0).getGfsCustomerId(), is(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
        assertThat(markup.get(0).getProductMarkupList().size(), is(4));
        assertThat(markup.get(0).getMarkupName(), is(CukesConstants.CONTRACT_NAME));

        assertThat(markup.get(0).getProductMarkupList().get(0).getItemPriceId(), is(CukesConstants.PRODUCT_PRICE_ID));
        assertThat(markup.get(0).getProductMarkupList().get(0).getEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(markup.get(0).getProductMarkupList().get(0).getExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(markup.get(0).getProductMarkupList().get(0).getProductType(), is(CukesConstants.PRODUCT_TYPE));

        assertThat(markup.get(0).getProductMarkupList().get(1).getItemPriceId(), is(2));
        assertThat(markup.get(0).getProductMarkupList().get(1).getEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(markup.get(0).getProductMarkupList().get(1).getExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(markup.get(0).getProductMarkupList().get(1).getProductType(), is(CukesConstants.PRODUCT_TYPE));

        assertThat(markup.get(0).getProductMarkupList().get(2).getItemPriceId(), is(3));
        assertThat(markup.get(0).getProductMarkupList().get(2).getEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(markup.get(0).getProductMarkupList().get(2).getExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(markup.get(0).getProductMarkupList().get(2).getProductType(), is(CukesConstants.PRODUCT_TYPE));

        assertThat(markup.get(0).getProductMarkupList().get(3).getItemPriceId(), is(4));
        assertThat(markup.get(0).getProductMarkupList().get(3).getEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(markup.get(0).getProductMarkupList().get(3).getExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(markup.get(0).getProductMarkupList().get(3).getProductType(), is(CukesConstants.PRODUCT_TYPE));
    }

    @When("^a request is made to get the default values of markup exception$")
    public void request_to_get_default_markup_exception() throws Exception {
        final MarkupWrapperDTO markup = markupService.fetchDefaultException(getContractPriceProfileSeq(), cppDateUtils.getFutureDate(),
                cppDateUtils.getFutureDate(), CukesConstants.EXCEPTION_NAME, CukesConstants.EXCEPTION_CUSTOMER_ID);

        setAttribute("markup", markup);
    }

    @Then("^the default values of markup exception is returned$")
    public void return_default_markup_exception() {
        final MarkupWrapperDTO markup = (MarkupWrapperDTO) getAttribute("markup");

        assertThat(markup.getGfsCustomerId(), is(CukesConstants.EXCEPTION_CUSTOMER_ID));
        assertThat(markup.getProductMarkupList().size(), is(4));
        assertThat(markup.getMarkupName(), is(CukesConstants.EXCEPTION_NAME));
        assertThat(markup.getProductMarkupList().get(0).getEffectiveDate(), is(cppDateUtils.getFutureDate()));
        assertThat(markup.getProductMarkupList().get(0).getExpirationDate(), is(cppDateUtils.getFutureDate()));
        assertThat(markup.getProductMarkupList().get(0).getItemPriceId(), is(CukesConstants.PRODUCT_PRICE_ID));
        assertThat(markup.getProductMarkupList().get(0).getProductType(), is(CukesConstants.PRODUCT_TYPE));
    }

    @When("^a request is made to add a markup exception$")
    public void request_add_exception() throws Exception {
        addException(CukesConstants.EXCEPTION_NAME, CukesConstants.EXCEPTION_CUSTOMER_ID);
    }

    private void addException(String exceptionName, String exceptionCustomerId) {
        try {
            customerServiceProxyMocker.mockCreateCustomer(exceptionCustomerId);
            MarkupWrapperDTO response = markupService.addExceptionMarkup(getContractPriceProfileSeq(), exceptionName, effectiveDate, expirationDate,
                    "vc71u");
            assertThat(response.getGfsCustomerId(), is(CukesConstants.EXCEPTION_CUSTOMER_ID));
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @Then("^the newly added exception is returned$")
    public void return_add_exception() throws Throwable {

        List<CMGCustomerResponseDTO> fetchGFSCustomerDetails = contractPriceProfCustomerRepository
                .fetchGFSCustomerDetailsList(getContractPriceProfileSeq());

        boolean exceptionCustomerFound = false;
        for (CMGCustomerResponseDTO cmgCustomerResponseDTO : fetchGFSCustomerDetails) {

            if (CukesConstants.EXCEPTION_CUSTOMER_ID.equals(cmgCustomerResponseDTO.getId())) {
                exceptionCustomerFound = true;
                break;
            }
        }
        assertThat(exceptionCustomerFound, equalTo(true));
    }

    @When("^a request is made to save the markup for product for new contract$")
    public void enter_product_level_markup_values_for_new_contract() throws Exception {
        saveMarkupForNewContract(Collections.EMPTY_LIST, buildProductTypeMarkup(CukesConstants.MARKUP_VALUE, DOLLAR_UNIT,
                CukesConstants.PRODUCT_PRICE_ID, CukesConstants.PER_CASE_TYPE, effectiveDate, cppDateUtils.getFutureDate()), Collections.EMPTY_LIST);
    }

    @When("^a request is made to save markup exception$")
    public void save_assignment_with_markup_exception() throws Throwable {
        saveMarkupWithException(CukesConstants.EXCEPTION_NAME, CukesConstants.EXCEPTION_CUSTOMER_ID, Collections.EMPTY_LIST, Collections.EMPTY_LIST,
                buildProductTypeMarkup(CukesConstants.MARKUP_VALUE, DOLLAR_UNIT, CukesConstants.PRODUCT_PRICE_ID, CukesConstants.PER_CASE_TYPE,
                        effectiveDate, cppDateUtils.getFutureDate()));
    }

    @Given("^a request is made to save markup exception for customer \"([^\"]*)\"$")
    public void a_request_is_made_to_save_markup_exception_for_customer(String exceptionName) throws Exception {
        saveMarkupWithException(exceptionName, exceptionName, Collections.EMPTY_LIST, Collections.EMPTY_LIST,
                buildProductTypeMarkup(CukesConstants.MARKUP_VALUE, DOLLAR_UNIT, CukesConstants.PRODUCT_PRICE_ID, CukesConstants.PER_CASE_TYPE,
                        effectiveDate, cppDateUtils.getFutureDate()));
    }

    public void enter_product_level_markup_values_with_date_range(Date effectiveDate, Date expirationDate) {
        saveMarkup(Collections.EMPTY_LIST, buildProductTypeMarkup(CukesConstants.MARKUP_VALUE, DOLLAR_UNIT, CukesConstants.PRODUCT_PRICE_ID,
                CukesConstants.PER_CASE_TYPE, effectiveDate, expirationDate));
    }

    public void saveMarkup(List<ItemLevelMarkupDTO> itemLevelMarkupList, List<ProductTypeMarkupDTO> productLevelMarkupList) {
        try {
            MarkupWrapperDTO markupWrapper = buildMarkupWrapper(itemLevelMarkupList, productLevelMarkupList, new ArrayList<SubgroupMarkupDTO>());

            ResponseEntity<Void> response = markupController.saveMarkup(markupWrapper);
            setAttribute("save_markup_response", response);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    public void saveMarkupForNewContract(List<ItemLevelMarkupDTO> itemLevelMarkupList, List<ProductTypeMarkupDTO> productLevelMarkupList,
            List<SubgroupMarkupDTO> subgroupMarkupList) {
        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();

        markupWrapper.setItemLevelMarkupList(itemLevelMarkupList);
        markupWrapper.setSubgroupMarkupList(subgroupMarkupList);
        markupWrapper.setIsMarkupSaved(false);
        markupWrapper.setGfsCustomerId(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        markupWrapper.setProductMarkupList(productLevelMarkupList);
        markupWrapper.setMarkupName(MARKUP_NAME);
        markupWrapper.setContractPriceProfileSeq(getContractPriceProfileSeq());

        markupController.saveMarkup(markupWrapper);
    }

    private void saveMarkupWithException(String exceptionName, String customerId, List<ItemLevelMarkupDTO> itemLevelMarkupList,
            List<SubgroupMarkupDTO> subgroupMarkupList, List<ProductTypeMarkupDTO> productLevelMarkupList) throws Exception {
        addException(customerId, customerId);
        MarkupWrapperDTO markupWrapper = buildMarkupWrapper(itemLevelMarkupList, productLevelMarkupList, subgroupMarkupList);
        markupWrapper.setGfsCustomerId(customerId);
        markupController.saveMarkup(markupWrapper);
    }

    public void enter_markup_values_for_item_with_date_range(Date effectiveDate, Date expirationDate, String itemId) throws Exception {
        MarkupWrapperDTO markupWrapper = buildMarkupWrapper(buildItemLeveMarkup(itemId, CukesConstants.ITEM_DESC, CukesConstants.MARKUP_VALUE,
                DOLLAR_UNIT, CukesConstants.PER_CASE_TYPE, false, effectiveDate, expirationDate), Collections.EMPTY_LIST, Collections.EMPTY_LIST);

        markupController.saveMarkup(markupWrapper);
    }

    public void enter_markup_values_for_subgroup_with_date_range(Date effectiveDate, Date expirationDate) throws Exception {

        MarkupWrapperDTO markupWrapper = buildMarkupWrapper(Collections.EMPTY_LIST, Collections.EMPTY_LIST,
                buildSubgroupMarkup(CukesConstants.SUBGROUP_ID_VALID, CukesConstants.SUBGROUP_DESCRIPTION, CukesConstants.MARKUP_VALUE, DOLLAR_UNIT,
                        CukesConstants.PER_CASE_TYPE, effectiveDate, expirationDate));

        markupController.saveMarkup(markupWrapper);
    }

    @When("^a request is made to delete the markup$")
    public void delete_product_markup() throws Exception {
        try {
            ResponseEntity<Void> response = markupController.deleteException(getContractPriceProfileSeq(), CukesConstants.EXCEPTION_CUSTOMER_ID,
                    CukesConstants.EXCEPTION_NAME);
            setAttribute("delete_markup_response", response);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @And("^a request is made to delete the existing item \"([^\"]*)\" markup$")
    public void delete_existing_item(AcceptableMarkupType markup) throws Exception {
        try {
            ResponseEntity<Void> response = markupController.deleteItemLevelMarkup(getContractPriceProfileSeq(),
                    CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.CMG_CUSTOMER_TYPE_ID, markup.getItemPriceId(), markup.getItemDesc());
            setAttribute("delete_item_response", response);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @Then("^the existing \"([^\"]*)\" markup are deleted$")
    public void markup_deleted(AcceptableMarkupType markup) {
        if (!markup.getItemPriceId().equals(StringUtils.EMPTY)) {
            String customerId = markup.getItemPriceCode() == ItemPriceLevel.PRODUCT_TYPE.getCode() ? CukesConstants.EXCEPTION_CUSTOMER_ID
                    : CukesConstants.DEFAULT_CMG_CUSTOMER_ID;
            List<ProductTypeMarkupDTO> fetchAllMarkup = customerItemPriceRepository.fetchAllMarkup(getContractPriceProfileSeq(), customerId);
            for (ProductTypeMarkupDTO productTypeDTO : fetchAllMarkup) {
                if (productTypeDTO.getItemPriceId() == Integer.parseInt(markup.getItemPriceId())) {
                    throw new AssertionFailedError();
                }
            }
        } else {
            List<ItemLevelMarkupDTO> fetchFutureItem = customerItemDescPriceRepository.fetchFutureItemTypeMarkups(getContractPriceProfileSeq(),
                    CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.CMG_CUSTOMER_TYPE_ID);
            for (ItemLevelMarkupDTO itemLevelMarkupDTO : fetchFutureItem) {
                if (itemLevelMarkupDTO.getItemDesc().equals(markup.getItemDesc())) {
                    throw new AssertionFailedError();
                }
            }
        }
    }

    @Then("^the future items markup and assigned items are deleted$")
    public void item_markup_mapping_deleted() {

        List<ItemAssignmentWrapperDTO> itemAssignmentWrapperDTOList = itemAssignmentHelper.fetchAllFutureItems(getContractPriceProfileSeq());

        assertThat(itemAssignmentWrapperDTOList.isEmpty(), is(true));
    }

    @And("^the values of assigned item markup are updated for future item$")

    public void fetch_assigned_item_markup() {
        List<ProductTypeMarkupDTO> fetchAllMarkup = customerItemPriceRepository.fetchAllMarkup(getContractPriceProfileSeq(),
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID);

        assertThat(fetchAllMarkup.size(), equalTo(1));
        ProductTypeMarkupDTO productTypeMarkupDTO = fetchAllMarkup.get(0);
        assertThat(productTypeMarkupDTO.getItemPriceId(), equalTo(Integer.valueOf(CukesConstants.NEW_ITEM_ID_TO_ASSIGN)));
        assertThat(productTypeMarkupDTO.getMarkupType(), equalTo(CukesConstants.PER_CASE_TYPE));
        assertThat(productTypeMarkupDTO.getUnit(), equalTo(DOLLAR_UNIT));
        assertThat(productTypeMarkupDTO.getMarkup(), equalTo(UPDATED_MARKUP_VALUE));
    }

    @When("^a request is made to rename the markup$")
    public void rename_markup() throws Exception {
        try {
            final ResponseEntity<Void> renameMarkup = markupController.renameMarkupException(buildMarkupRenameDTO());
            setAttribute("rename_markup_response", renameMarkup);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, renameMarkup.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @And("^a request is made to save the markup indicators$")
    public void save_markup_indicators() throws Exception {
        try {
            ResponseEntity<Void> response = markupController.saveMarkupIndicators(buildMarkupRequestDTO(effectiveDate, expirationDate));
            setAttribute("saveMarkupIndicators_response", response);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @When("^a request is made to fetch the markup indicators$")
    public void fetch_markup_indicators() throws Exception {
        final ResponseEntity<Map<String, Boolean>> markupOnSellMap = markupController.fetchMarkupIndicators(getContractPriceProfileSeq());

        setAttribute("markupOnSellMap", markupOnSellMap);
    }

    @Then("^the value of markup indicators returned$")
    public void markup_indicators() {
        final Map<String, Boolean> markupOnSellMap = ((ResponseEntity<Map<String, Boolean>>) getAttribute("markupOnSellMap")).getBody();

        assertThat(markupOnSellMap.get(CPPConstants.MARKUP_ON_SELL), equalTo(true));
        assertThat(markupOnSellMap.get(CPPConstants.EXPIRE_LOWER), equalTo(true));
    }

    @When("^a request is made to fetch if real customer is assigned to a concept$")
    public void fetch_real_customer_mapped_indicator() throws Exception {
        final ResponseEntity<List<MarkupWrapperDTO>> markupWrapperDTOList = markupController.fetchMarkups(getContractPriceProfileSeq(), null, null);

        setAttribute("markupWrapperDTOList", markupWrapperDTOList.getBody());
    }

    private MarkupRequestDTO buildMarkupRequestDTO(Date effectiveDate, Date expirationDate) {
        MarkupRequestDTO markupRequest = new MarkupRequestDTO();
        markupRequest.setContractPriceProfileSeq(getContractPriceProfileSeq());
        markupRequest.setEffectiveDate(effectiveDate != null ? effectiveDate : new LocalDate(2010, 01, 01).toDate());
        markupRequest.setExpirationDate(expirationDate != null ? expirationDate : new LocalDate(2011, 01, 01).toDate());
        markupRequest.setMarkupOnSell(true);
        markupRequest.setExpireLower(true);
        return markupRequest;
    }

    private ExceptionMarkupRenameDTO buildMarkupRenameDTO() {
        ExceptionMarkupRenameDTO markupRename = new ExceptionMarkupRenameDTO();
        markupRename.setContractPriceProfileSeq(getContractPriceProfileSeq());
        markupRename.setGfsCustomerId(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        markupRename.setExceptionName(CukesConstants.EXCEPTION_NAME);
        markupRename.setNewExceptionName(CukesConstants.NEW_EXCEPTION_NAME);
        return markupRename;
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
        return markupWrapper;
    }

    public List<ItemLevelMarkupDTO> buildItemLeveMarkup(String itemId, String itemDesc, String markup, String unit, int markupType, boolean noItemId,
            Date effectiveDate, Date expirationDate) {

        List<ItemLevelMarkupDTO> itemLevelMarkupList = new ArrayList<>();
        ItemLevelMarkupDTO itemLevelMarkup = buildItemLevelMarkupDTO(itemId, itemDesc, markup, unit, markupType, noItemId, effectiveDate,
                expirationDate);
        itemLevelMarkupList.add(itemLevelMarkup);
        return itemLevelMarkupList;
    }

    private ItemLevelMarkupDTO buildItemLevelMarkupDTO(String itemId, String itemDesc, String markup, String unit, int markupType, boolean noItemId,
            Date effectiveDate, Date expirationDate) {
        ItemLevelMarkupDTO itemLevelMarkup = new ItemLevelMarkupDTO();
        itemLevelMarkup.setMarkup(markup);
        itemLevelMarkup.setUnit(unit);
        itemLevelMarkup.setEffectiveDate(effectiveDate);
        itemLevelMarkup.setExpirationDate(expirationDate);
        itemLevelMarkup.setItemDesc(itemDesc);
        itemLevelMarkup.setItemId(itemId);
        itemLevelMarkup.setMarkupType(markupType);
        itemLevelMarkup.setNoItemId(noItemId);
        return itemLevelMarkup;
    }

    public List<SubgroupMarkupDTO> buildSubgroupMarkup(String subgroupId, String subgroupDesc, String markup, String unit, int markupType,
            Date effectiveDate, Date expirationDate) {

        List<SubgroupMarkupDTO> subgroupMarkupList = new ArrayList<>();
        SubgroupMarkupDTO subgroupMarkup = buildSubgroupMarkupDTO(subgroupId, subgroupDesc, markup, unit, markupType, effectiveDate, expirationDate);
        subgroupMarkupList.add(subgroupMarkup);
        return subgroupMarkupList;
    }

    private SubgroupMarkupDTO buildSubgroupMarkupDTO(String subgroupId, String subgroupDesc, String markup, String unit, int markupType,
            Date effectiveDate, Date expirationDate) {
        SubgroupMarkupDTO subgroupMarkup = new SubgroupMarkupDTO();
        subgroupMarkup.setMarkup(markup);
        subgroupMarkup.setUnit(unit);
        subgroupMarkup.setEffectiveDate(effectiveDate);
        subgroupMarkup.setExpirationDate(expirationDate);
        subgroupMarkup.setSubgroupDesc(subgroupDesc);
        subgroupMarkup.setSubgroupId(subgroupId);
        subgroupMarkup.setMarkupType(markupType);
        return subgroupMarkup;
    }

    public List<ProductTypeMarkupDTO> buildProductTypeMarkup(String markup, String unit, int productPriceId, int markupType, Date effectiveDate,
            Date expirationDate) {
        List<ProductTypeMarkupDTO> markupList = new ArrayList<>();
        ProductTypeMarkupDTO markupDto = buildProductTypeDTO(markup, unit, productPriceId, markupType, effectiveDate, expirationDate);
        markupList.add(markupDto);
        return markupList;
    }

    private ProductTypeMarkupDTO buildProductTypeDTO(String markup, String unit, int productPriceId, int markupType, Date effectiveDate,
            Date expirationDate) {
        ProductTypeMarkupDTO markupDto = new ProductTypeMarkupDTO();
        markupDto.setMarkup(markup);
        markupDto.setUnit(unit);
        markupDto.setEffectiveDate(effectiveDate);
        markupDto.setExpirationDate(expirationDate);
        markupDto.setItemPriceId(productPriceId);
        markupDto.setProductType(CukesConstants.PRODUCT_TYPE);
        markupDto.setMarkupType(markupType);
        return markupDto;
    }

    public void save_markup_indicators_with_date_range(Date effectiveDate, Date expirationDate) throws ParseException {

        markupController.saveMarkupIndicators(buildMarkupRequestDTO(effectiveDate, expirationDate));
    }

    @And("^a request is made to get subgroup information for \"([^\"]*)\"$")
    public void request_get_subgroup_information(AcceptableSubgroups subgroupType) throws Throwable {

        final ResponseEntity<SubgroupResponseDTO> result = markupController.findItemSubgroupInformation(subgroupType.getSubgroupId(),
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.CMG_CUSTOMER_TYPE_ID, getContractPriceProfileSeq());

        setAttribute("subgroupResponse", result);
    }

    @Then("^the subgroup information is returned$")
    public void return_subgroup_information() throws Throwable {
        final SubgroupResponseDTO subgroupInformation = ((ResponseEntity<SubgroupResponseDTO>) getAttribute("subgroupResponse")).getBody();

        assertThat(subgroupInformation.getSubgroupDesc(), is(CukesConstants.SUBGROUP_DESCRIPTION));
        assertThat(subgroupInformation.getValidationCode(), is(200));
        assertThat(subgroupInformation.getValidationMessage(), is(StringUtils.EMPTY));
        assertThat(subgroupInformation.getSubgroupId(), equalTo(CukesConstants.SUBGROUP_ID_VALID));
    }

    @Then("^exception is thrown with validation error \"([^\"]*)\"$")
    public void throw_invalid_subgroup_exception(CPPExceptionType errorType) throws Throwable {
        final SubgroupResponseDTO subgroupInformation = ((ResponseEntity<SubgroupResponseDTO>) getAttribute("subgroupResponse")).getBody();

        assertThat(subgroupInformation.getSubgroupDesc(), is(StringUtils.EMPTY));
        assertThat(subgroupInformation.getValidationCode(), is(errorType.getErrorCode()));
    }

    @When("^a request is made to delete the existing subgroup \"([^\"]*)\" markup$")

    public void enter_subgroup_markup_for_delete_existing(AcceptableMarkupType markup) {
        markupController.deleteSubgroupMarkup(getContractPriceProfileSeq(), CukesConstants.DEFAULT_CMG_CUSTOMER_ID,
                CukesConstants.CMG_CUSTOMER_TYPE_ID, markup.getItemPriceId());
    }

    @Then("^a markup added \"([^\"]*)\" with value \"([^\"]*)\"$")
    public void enter_markup_values(AcceptableMarkupType markup, String markupValue) {

        boolean noItemId = markup.getItemPriceId().equals(StringUtils.EMPTY) ? true : false;

        if (markup.getItemPriceCode() == ItemPriceLevel.ITEM.getCode()) {
            ItemLevelMarkupDTO itemLevelMarkup = buildItemLevelMarkupDTO(markup.getItemPriceId(), markup.getItemDesc(), markupValue, markup.getUnit(),
                    markup.getMarkupType(), noItemId, effectiveDate, expirationDate);
            addItemToWrapperDTO(itemLevelMarkup);
        } else if (markup.getItemPriceCode() == ItemPriceLevel.SUBGROUP.getCode()) {
            SubgroupMarkupDTO subgroupMarkup = buildSubgroupMarkupDTO(markup.getItemPriceId(), CukesConstants.SUBGROUP_DESCRIPTION, markupValue,
                    markup.getUnit(), markup.getMarkupType(), effectiveDate, expirationDate);
            addSubgroupToWrapperDTO(subgroupMarkup);
        } else {
            ProductTypeMarkupDTO productLevelMarkup = buildProductTypeDTO(markupValue, markup.getUnit(), Integer.parseInt(markup.getItemPriceId()),
                    markup.getMarkupType(), effectiveDate, expirationDate);
            addProductToWrapperDTO(productLevelMarkup);
        }
    }

    @When("^a request is made to save the markup$")
    public void request_to_save_markup() {
        try {
            MarkupWrapperDTO markupWrapperDTO = (MarkupWrapperDTO) getAttribute("saveMarkupWrapperDTO");
            ResponseEntity<Void> response = markupController.saveMarkup(markupWrapperDTO);
            setAttribute("save_markup_response", response);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @Then("^the values of \"([^\"]*)\" markup are saved with value \"([^\"]*)\"$")
    public void the_values_of_markup_are_saved(AcceptableMarkupType markup, String markupValue) {
        List<ProductTypeMarkupDTO> fetchAllMarkup = customerItemPriceRepository.fetchAllMarkup(getContractPriceProfileSeq(),
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID);

        List<ItemLevelMarkupDTO> fetchFutureItem = customerItemDescPriceRepository.fetchFutureItemTypeMarkups(getContractPriceProfileSeq(),
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.CMG_CUSTOMER_TYPE_ID);
        if (!markup.getItemPriceId().equals(StringUtils.EMPTY)) {
            assertThat(fetchAllMarkup.size(), equalTo(1));

            ProductTypeMarkupDTO productTypeMarkupDTO = fetchAllMarkup.get(0);

            assertThat(productTypeMarkupDTO.getItemPriceId(), equalTo(Integer.parseInt(markup.getItemPriceId())));
            assertThat(productTypeMarkupDTO.getMarkupType(), equalTo(markup.getMarkupType()));
            assertThat(productTypeMarkupDTO.getUnit(), equalTo(markup.getUnit()));
            assertThat(productTypeMarkupDTO.getMarkup(), equalTo(markupValue));
        } else {
            assertThat(fetchFutureItem.size(), equalTo(1));

            ItemLevelMarkupDTO itemLevelMarkupDTO = fetchFutureItem.get(0);

            assertThat(itemLevelMarkupDTO.getMarkupType(), equalTo(markup.getMarkupType()));
            assertThat(itemLevelMarkupDTO.getUnit(), equalTo(markup.getUnit()));
            assertThat(itemLevelMarkupDTO.getMarkup(), equalTo(markupValue));
        }

    }

    private void addProductToWrapperDTO(ProductTypeMarkupDTO productLevelMarkup) {
        MarkupWrapperDTO markupWrapperDTO = getMarkupWrapperDTO();
        if (!markupWrapperDTO.getProductMarkupList().isEmpty()) {
            markupWrapperDTO.getProductMarkupList().add(productLevelMarkup);
        } else {
            List<ProductTypeMarkupDTO> productLevelMarkupList = new ArrayList<>();
            productLevelMarkupList.add(productLevelMarkup);
            markupWrapperDTO.setProductMarkupList(productLevelMarkupList);
        }

        setAttribute("saveMarkupWrapperDTO", markupWrapperDTO);
    }

    private void addItemToWrapperDTO(ItemLevelMarkupDTO itemLevelMarkupDTO) {
        MarkupWrapperDTO markupWrapperDTO = getMarkupWrapperDTO();
        if (!markupWrapperDTO.getItemLevelMarkupList().isEmpty()) {
            markupWrapperDTO.getItemLevelMarkupList().add(itemLevelMarkupDTO);
        } else {
            List<ItemLevelMarkupDTO> itemLevelMarkupDTOList = new ArrayList<>();
            itemLevelMarkupDTOList.add(itemLevelMarkupDTO);
            markupWrapperDTO.setItemLevelMarkupList(itemLevelMarkupDTOList);
        }

        setAttribute("saveMarkupWrapperDTO", markupWrapperDTO);
    }

    private void addSubgroupToWrapperDTO(SubgroupMarkupDTO subgroupMarkupDTO) {
        MarkupWrapperDTO markupWrapperDTO = getMarkupWrapperDTO();
        if (!markupWrapperDTO.getSubgroupMarkupList().isEmpty()) {
            markupWrapperDTO.getSubgroupMarkupList().add(subgroupMarkupDTO);
        } else {
            List<SubgroupMarkupDTO> subgroupMarkupDTOList = new ArrayList<>();
            subgroupMarkupDTOList.add(subgroupMarkupDTO);
            markupWrapperDTO.setSubgroupMarkupList(subgroupMarkupDTOList);
        }

        setAttribute("saveMarkupWrapperDTO", markupWrapperDTO);
    }

    private MarkupWrapperDTO getMarkupWrapperDTO() {
        MarkupWrapperDTO markupWrapperDTO = (MarkupWrapperDTO) getAttribute("saveMarkupWrapperDTO");
        if (markupWrapperDTO == null) {
            markupWrapperDTO = new MarkupWrapperDTO();
            markupWrapperDTO.setContractPriceProfileSeq(getContractPriceProfileSeq());
            markupWrapperDTO.setIsMarkupSaved(false);
            markupWrapperDTO.setGfsCustomerId(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
            markupWrapperDTO.setGfsCustomerType(CukesConstants.CMG_CUSTOMER_TYPE_ID);
            markupWrapperDTO.setMarkupName(MARKUP_NAME);
            markupWrapperDTO.setItemLevelMarkupList(Collections.EMPTY_LIST);
            markupWrapperDTO.setSubgroupMarkupList(Collections.EMPTY_LIST);
            markupWrapperDTO.setProductMarkupList(Collections.EMPTY_LIST);
        }
        return markupWrapperDTO;
    }

}
