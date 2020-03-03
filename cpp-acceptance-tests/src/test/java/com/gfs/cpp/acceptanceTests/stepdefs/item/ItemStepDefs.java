package com.gfs.cpp.acceptanceTests.stepdefs.item;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.web.controller.item.ItemController;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SuppressWarnings("unchecked")
public class ItemStepDefs {

    @Autowired
    private ItemController itemController;

    @When("^a request is made to get item information$")
    public void request_get_item_information() throws Throwable {

        final ResponseEntity<ItemInformationDTO> result = itemController.findItemInformation(CukesConstants.NEW_ITEM_ID_TO_ASSIGN,
                CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.CMG_CUSTOMER_TYPE_ID, getContractPriceProfileSeq());

        setAttribute("itemResponse", result);
    }

    @Then("^the item information is returned$")
    public void return_item_information() throws Throwable {
        final ItemInformationDTO itemInformation = ((ResponseEntity<ItemInformationDTO>) getAttribute("itemResponse")).getBody();

        assertThat(itemInformation.getIsActive(), is(true));
        assertThat(itemInformation.getIsValid(), is(true));
        assertThat(itemInformation.getItemNo(), equalTo(CukesConstants.NEW_ITEM_ID_TO_ASSIGN));
    }

    @When("^a request is made to get item information for item assignment$")
    public void request_find_item_information_for_assignment() throws Throwable {
        final ResponseEntity<ItemInformationDTO> result = itemController.findItemInformation(CukesConstants.ITEM_ID, CukesConstants.DEFAULT_CMG_CUSTOMER_ID,
                CukesConstants.CMG_CUSTOMER_TYPE_ID, getContractPriceProfileSeq());

        setAttribute("itemResponse", result);
    }

    @Then("^the item already exist in database$")
    public void return_item_information_for_assignment() throws Throwable {
        final ItemInformationDTO itemInformation = ((ResponseEntity<ItemInformationDTO>) getAttribute("itemResponse")).getBody();

        assertThat(itemInformation.getIsItemAlreadyExist(), is(true));
    }
}
