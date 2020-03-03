package com.gfs.cpp.acceptanceTests.stepdefs.itemAssignments;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.common.data.AcceptableItems;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableMarkupType;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.web.controller.assignments.AssignmentController;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SuppressWarnings("unchecked")
public class ItemAssignmentsStepDefs {

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private AssignmentController assignmentsController;

    @When("a request is made to fetch all future items$")
    public void request_to_fetch_future_items() throws Exception {
        ResponseEntity<List<ItemAssignmentWrapperDTO>> response = assignmentsController.fetchAllFutureItems(getContractPriceProfileSeq());
        setAttribute("futureItemResponse", response.getBody());
    }

    @Then("^the future items are returned$")
    public void return_future_items() {
        final List<ItemAssignmentWrapperDTO> result = ((List<ItemAssignmentWrapperDTO>) getAttribute("futureItemResponse"));

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getExceptionName(), equalTo(CukesConstants.CONTRACT_NAME));
        assertThat(result.get(0).getFutureItemDesc(), equalTo(CukesConstants.FUTURE_ITEM_DESC));
        assertThat(result.get(0).getItemAssignmentList().size(), equalTo(1));
        assertThat(result.get(0).getIsFutureItemSaved(), equalTo(false));
        assertThat(result.get(0).getGfsCustomerId(), equalTo(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
        assertThat(result.get(0).getGfsCustomerTypeCode(), equalTo(CukesConstants.CMG_CUSTOMER_TYPE_ID));

    }

    @When("^a request is made to assign items \"([^\"]*)\" with \"([^\"]*)\" future item$")
    public void assign_items_with_future_items(AcceptableMarkupType itemMarkup, AcceptableMarkupType futureItemMarkup) throws Exception {

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(itemMarkup.getItemDesc(), futureItemMarkup.getItemDesc());
        try {
            ResponseEntity<StatusDTO> response = assignmentsController.assignItems(itemAssignmentWrapperDTO);

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

    @Then("^an item is assigned to future item and concept$")
    public void return_assignment_item() {

        List<ItemAssignmentWrapperDTO> itemAssignmentWrapperDTOList = assignmentsController.fetchAllFutureItems(getContractPriceProfileSeq())
                .getBody();

        assertThat(itemAssignmentWrapperDTOList.size(), equalTo(1));
        assertThat(itemAssignmentWrapperDTOList.get(0).getExceptionName(), equalTo(CukesConstants.CONTRACT_NAME));
        assertThat(itemAssignmentWrapperDTOList.get(0).getFutureItemDesc(), equalTo(CukesConstants.FUTURE_ITEM_DESC));
        assertThat(itemAssignmentWrapperDTOList.get(0).getGfsCustomerId(), equalTo(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
        assertThat(itemAssignmentWrapperDTOList.get(0).getGfsCustomerTypeCode(), equalTo(CukesConstants.CMG_CUSTOMER_TYPE_ID));
        assertThat(itemAssignmentWrapperDTOList.get(0).getIsFutureItemSaved(), equalTo(true));
    }

    @And("^the values of item markup are saved for future item$")
    public void fetch_item_markup() {
        List<ProductTypeMarkupDTO> fetchAllMarkup = customerItemPriceRepository.fetchAllMarkup(getContractPriceProfileSeq(),
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID);

        assertThat(fetchAllMarkup.size(), equalTo(4));
        ProductTypeMarkupDTO productTypeMarkupDTO = fetchAllMarkup.get(3);
        assertThat(productTypeMarkupDTO.getItemPriceId(), equalTo(Integer.valueOf(CukesConstants.NEW_ITEM_ID_TO_ASSIGN)));
        assertThat(productTypeMarkupDTO.getMarkupType(), equalTo(2));
        assertThat(productTypeMarkupDTO.getUnit(), equalTo(CukesConstants.DEFAULT_UNIT));
        assertThat(productTypeMarkupDTO.getMarkup(), equalTo(CukesConstants.MARKUP_VALUE));
    }

    @When("^a request with already existing items is made to assign items with future item$")
    public void assign_already_existing_items_with_future_items() throws Exception {

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(CukesConstants.ITEM_DESC, CukesConstants.FUTURE_ITEM_DESC);

        StatusDTO statusDTO = assignmentsController.assignItems(itemAssignmentWrapperDTO).getBody();

        setAttribute("statusDTO", statusDTO);
    }

    @Then("^the items already exist status returned for \"([^\"]*)\"$")
    public void return_item_already_exist(AcceptableItems acceptableItem) {

        StatusDTO statusDTO = (StatusDTO) getAttribute("statusDTO");

        assertThat(statusDTO.getErrorCode(), equalTo(CPPExceptionType.ITEM_ALREADY_EXIST.getErrorCode()));
        assertThat(statusDTO.getErrorMessage(), equalTo("(" + acceptableItem.getItemId() + ")"));
    }

    @When("^a request with duplicate items is made to assign items with future item$")
    public void assign_duplicate_items_with_future_items() throws Exception {

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(CukesConstants.ITEM_DESC, CukesConstants.FUTURE_ITEM_DESC);
        itemAssignmentWrapperDTO.getItemAssignmentList().addAll(buildItemAssignmentDTOList(CukesConstants.ITEM_DESC));

        StatusDTO statusDTO = assignmentsController.assignItems(itemAssignmentWrapperDTO).getBody();

        setAttribute("statusDTO", statusDTO);
    }

    @When("^a request is made to expire the assignment of a future item$")
    public void delete_item_markup_mapping() {
        try {
            ResponseEntity<Void> response = assignmentsController.deleteItemAssignment(getContractPriceProfileSeq(),
                    CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.CMG_CUSTOMER_TYPE_ID, CukesConstants.NEW_ITEM_ID_TO_ASSIGN,
                    CukesConstants.FUTURE_ITEM_DESC);
            setAttribute("expireFutureItems_response", response);
            setAttribute(CukesConstants.CPP_ERROR_CODE, response.getStatusCodeValue());
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
            setAttribute(CukesConstants.CPP_ERROR_CODE, cpp.getErrorCode());
        }
    }

    @When("^the future item mapping is expired$")
    public void item_markup_mapping_deleted() {
        List<ItemAssignmentWrapperDTO> itemAssignmentWrapperDTOList = assignmentsController.fetchAllFutureItems(getContractPriceProfileSeq())
                .getBody();

        assertThat(itemAssignmentWrapperDTOList.get(0).getItemAssignmentList().size(), equalTo(1));
        assertThat(itemAssignmentWrapperDTOList.get(0).getItemAssignmentList().get(0).getIsItemSaved(), equalTo(false));
        assertThat(itemAssignmentWrapperDTOList.get(0).getItemAssignmentList().get(0).getItemId(), equalTo(""));
        assertThat(itemAssignmentWrapperDTOList.get(0).getItemAssignmentList().get(0).getItemDescription(), equalTo(""));
    }

    @When("^the future item mapping is expired for \"([^\"]*)\"$")
    public void item_markup_mapping_deleted_for_furtherance(AcceptableItems acceptableItem) {
        List<ItemAssignmentWrapperDTO> itemAssignmentWrapperDTOList = assignmentsController.fetchAllFutureItems(getContractPriceProfileSeq())
                .getBody();

        for (ItemAssignmentDTO itemAssignmentDTO : itemAssignmentWrapperDTOList.get(0).getItemAssignmentList()) {
            assertThat(itemAssignmentDTO.getItemId(), not(acceptableItem.getItemId()));
        }
    }

    private ItemAssignmentWrapperDTO buildItemAssignmentWrapperDTO(String itemDesc, String futureDesc) {
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(getContractPriceProfileSeq());
        itemAssignmentWrapperDTO.setExceptionName(CukesConstants.CONTRACT_NAME);
        itemAssignmentWrapperDTO.setFutureItemDesc(futureDesc);
        itemAssignmentWrapperDTO.setGfsCustomerId(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(CukesConstants.CMG_CUSTOMER_TYPE_ID);
        itemAssignmentWrapperDTO.setIsFutureItemSaved(false);
        itemAssignmentWrapperDTO.setItemAssignmentList(buildItemAssignmentDTOList(itemDesc));
        return itemAssignmentWrapperDTO;
    }

    private List<ItemAssignmentDTO> buildItemAssignmentDTOList(String ItemMarkup) {
        List<ItemAssignmentDTO> itemAssignmentList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription(CukesConstants.ITEM_DESC);
        itemAssignmentDTO.setItemId(CukesConstants.NEW_ITEM_ID_TO_ASSIGN);
        itemAssignmentList.add(itemAssignmentDTO);
        return itemAssignmentList;
    }
}
