package com.gfs.cpp.acceptanceTests.stepdefs.distributionCenter;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterDTO;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSaveDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.web.controller.distributioncenter.DistributionCenterController;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SuppressWarnings("unchecked")
public class DistributionCenterStepDefs {

    private static final String DC_1 = "11";
    private static final String DC_2 = "99999";
    private static final String DC_4 = "999";

    @Autowired
    private DistributionCenterController distributionCenterController;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @When("^a request is made to get the list of all distribution centers$")
    public void request_to_get_dc_list() {
        final ResponseEntity<List<DistributionCenterDTO>> dc_list = distributionCenterController.fetchDistributionCenters();
        setAttribute("dcList", dc_list);
    }

    @Then("^a list of all distribution center is returned$")
    public void return_dc_list() {
        final List<DistributionCenterDTO> dcList = ((ResponseEntity<List<DistributionCenterDTO>>) getAttribute("dcList")).getBody();
        assertThat(dcList.size(), is(1));
        assertThat(dcList.get(0).getDcNumber(), is(String.valueOf(CukesConstants.DC_NUMBER)));
        assertThat(dcList.get(0).getName(), is(CukesConstants.DC_NAME));
    }

    @And("^request is made to save the distribution center for contract price$")
    public void distribution_centers_saved() throws Exception {
        saveDistributionCenter(cppDateUtils.getFutureDate(), cppDateUtils.getFutureDate());
    }

    @Then("^the selected distribution centers are saved$")
    public void save_dc_list() {
        final ResponseEntity<List<DistributionCenterDetailDO>> savedDcList = distributionCenterController
                .fetchSavedDistributionCenters(getContractPriceProfileSeq());

        DistributionCenterSaveDTO distributionCenterSaveDTO = (DistributionCenterSaveDTO) getAttribute("distributionCenterSaveDTO");

        assertThat(savedDcList.getBody().size(), is(2));
        assertThat(savedDcList.getBody().get(0).getContractPriceProfileSeq(), is(distributionCenterSaveDTO.getContractPriceProfileSeq()));
        assertThat(distributionCenterSaveDTO.getDistributionCenters().contains(savedDcList.getBody().get(0).getDcCode()), is(true));
        assertThat(savedDcList.getBody().get(1).getContractPriceProfileSeq(), is(distributionCenterSaveDTO.getContractPriceProfileSeq()));
        assertThat(distributionCenterSaveDTO.getDistributionCenters().contains(savedDcList.getBody().get(1).getDcCode()), is(true));
    }

    private void saveDistributionCenter(Date effectiveDate, Date expirationDate) {
        try {
            DistributionCenterSaveDTO distributionCenterSaveDTO = new DistributionCenterSaveDTO();
            distributionCenterSaveDTO.setContractPriceProfileSeq(getContractPriceProfileSeq());
            distributionCenterSaveDTO.setCreateUserID(CukesConstants.CURRENT_USER_ID);
            ArrayList<String> distributionCenters = new ArrayList<>();
            distributionCenters.add(DC_1);
            distributionCenters.add(DC_2);
            distributionCenterSaveDTO.setDistributionCenters(distributionCenters);
            distributionCenterSaveDTO.setEffectiveDate(effectiveDate);
            distributionCenterSaveDTO.setExpirationDate(expirationDate);

            final ResponseEntity<String> response = distributionCenterController.saveDistributionCenters(distributionCenterSaveDTO);

            setAttribute("distributionCenterSaveDTO", distributionCenterSaveDTO);
            setAttribute("save_distributionCenter_response", response);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, response.getStatusCode().getReasonPhrase());
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @When("^a request is made to get the list of saved distribution centers$")
    public void request_to_get_saved_dc_list() {
        final ResponseEntity<List<DistributionCenterDetailDO>> saved_dc_list = distributionCenterController
                .fetchSavedDistributionCenters(getContractPriceProfileSeq());
        setAttribute("saved_dc_list", saved_dc_list);
    }

    @Then("^a list of all saved distribution center is returned$")
    public void return_saved_dc_list() {
        final List<DistributionCenterDetailDO> dcList = ((ResponseEntity<List<DistributionCenterDetailDO>>) getAttribute("saved_dc_list")).getBody();
        assertThat(dcList.size(), is(2));
        assertThat(dcList.get(0).getContractPriceProfileSeq(), is(getContractPriceProfileSeq()));
        assertThat(dcList.get(0).getDcCode(), is(DC_1));
        assertThat(dcList.get(1).getContractPriceProfileSeq(), is(getContractPriceProfileSeq()));
        assertThat(dcList.get(1).getDcCode(), is(DC_2));

    }

    @When("^some distribution centers are updated$")
    public void update_distribution_center() throws ParseException {
        update_dc_list();
    }

    @When("^some distribution centers are deselected$")
    public void update_dc_list() throws ParseException {
        ArrayList<String> dcCodes = new ArrayList<>();
        dcCodes.add(DC_2);
        dcCodes.add(DC_4);

        DistributionCenterSaveDTO distributionCenterUpdateDTO = new DistributionCenterSaveDTO();
        distributionCenterUpdateDTO.setContractPriceProfileSeq(getContractPriceProfileSeq());
        distributionCenterUpdateDTO.setCreateUserID(CukesConstants.CURRENT_USER_ID);
        distributionCenterUpdateDTO.setDistributionCenters(dcCodes);
        distributionCenterUpdateDTO.setEffectiveDate(cppDateUtils.getFutureDate());
        distributionCenterUpdateDTO.setExpirationDate(cppDateUtils.getFutureDate());

        distributionCenterController.saveDistributionCenters(distributionCenterUpdateDTO);

        setAttribute("distributionCenterSaveDTO", distributionCenterUpdateDTO);
    }

    @Then("^the deselected distribution centers are deleted$")
    public void delete_dc_list() {
        final ResponseEntity<List<DistributionCenterDetailDO>> savedDcList = distributionCenterController
                .fetchSavedDistributionCenters(getContractPriceProfileSeq());

        boolean isDeleted = true;
        List<DistributionCenterDetailDO> dcsSaved = savedDcList.getBody();

        for (DistributionCenterDetailDO distributionCenterDetailDO : dcsSaved) {
            if (DC_1.equals(distributionCenterDetailDO.getDcCode())) {
                isDeleted = false;
                break;
            }
        }

        assertThat(isDeleted, equalTo(true));

    }

    @And("^new distribution centers are saved$")
    public void save_new_dc_center() {
        final ResponseEntity<List<DistributionCenterDetailDO>> savedDcList = distributionCenterController
                .fetchSavedDistributionCenters(getContractPriceProfileSeq());
        List<String> dcList = new ArrayList<>();
        dcList.add(savedDcList.getBody().get(0).getDcCode());
        dcList.add(savedDcList.getBody().get(1).getDcCode());

        DistributionCenterSaveDTO distributionCenterSaveDTO = (DistributionCenterSaveDTO) getAttribute("distributionCenterSaveDTO");
        assertThat(savedDcList.getBody().size(), is(2));
        assertThat(dcList.contains(DC_4), is(true));
        assertThat(dcList.contains(DC_4), is(distributionCenterSaveDTO.getDistributionCenters().contains(DC_4)));

    }

    public void distribution_centers_saved_with_date_range(Date effectiveDate, Date expirationDate) throws Exception {
        saveDistributionCenter(effectiveDate, expirationDate);
    }
}
