package com.gfs.cpp.acceptanceTests.stepdefs.costModel;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.dto.costmodel.CostModelResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.web.controller.costmodel.CostModelController;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CostModelStepDefs {

    @Autowired
    private CostModelController costModelController;

    @Autowired
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;

    @When("^a request is made to fetch product type$")
    public void request_to_fetch_productType() {
        List<PrcProfNonBrktCstMdlDTO> result = costModelController.fetchCostModelForGrid(getContractPriceProfileSeq());
        setAttribute("productTypeList", result);
    }

    @Then("^list of Product types is returned$")
    public void list_of_product_type_returned() {
        List<PrcProfNonBrktCstMdlDTO> result = (List<PrcProfNonBrktCstMdlDTO>) getAttribute("productTypeList");

        assertThat(result.size(), equalTo(6));
    }

    @When("^a request is made to fetch active cost model$")
    public void request_to_fetch_active_cost_model() {
        ResponseEntity<List<CostModelResponseDTO>> result = costModelController.fetchAllActiveCostModels();
        setAttribute("activeCostModelList", result.getBody());
    }

    @Then("^list of active cost model is returned$")
    public void list_of_active_cost_model_returned() {
        List<CostModelResponseDTO> result = (List<CostModelResponseDTO>) getAttribute("activeCostModelList");

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getCostModelId(), equalTo(CukesConstants.COST_MODEL_ID));
        assertThat(result.get(0).getCostModelTypeValue(), equalTo(CukesConstants.COST_MODEL_NAME));

    }

    @When("^a request is made to update cost model for product type$")
    public void request_to_update_cost_model() {
        try {
            List<PrcProfNonBrktCstMdlDTO> costModelList = buildCostModelList();
            costModelList.get(2).setCostModelId(CukesConstants.UPDATE_COST_MODEL_ID_FOR_PRODUCT_TYPE);
            costModelController.saveUpdatedCostModel(costModelList);
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @Then("^product type will be updated with selected cost model$")
    public void updated_cost_model() {
        List<PrcProfNonBrktCstMdlDTO> savedPrcProfNonBrktCstMdlDTO = prcProfNonBrktCstMdlRepository
                .fetchPrcProfNonBrktCstMdlForCMGForCPPSeq(getContractPriceProfileSeq());

        assertThat(savedPrcProfNonBrktCstMdlDTO.size(), equalTo(6));
        assertThat(savedPrcProfNonBrktCstMdlDTO.get(2).getCostModelId(), equalTo(CukesConstants.UPDATE_COST_MODEL_ID_FOR_PRODUCT_TYPE));

    }

    @When("^a request is made to update cost model for item$")
    public void request_to_update_cost_model_for_item() {
        try {
            List<PrcProfNonBrktCstMdlDTO> costModelList = buildCostModelList();
            costModelList.get(0).setCostModelId(CukesConstants.UPDATE_COST_MODEL_ID_FOR_ITEM);
            costModelController.saveUpdatedCostModel(costModelList);
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @Then("^item will be updated with selected cost model$")
    public void updated_cost_model_for_item() {
        List<PrcProfNonBrktCstMdlDTO> savedPrcProfNonBrktCstMdlDTO = prcProfNonBrktCstMdlRepository
                .fetchPrcProfNonBrktCstMdlForCMGForCPPSeq(getContractPriceProfileSeq());

        assertThat(savedPrcProfNonBrktCstMdlDTO.size(), equalTo(6));
        assertThat(savedPrcProfNonBrktCstMdlDTO.get(0).getCostModelId(), equalTo(CukesConstants.UPDATE_COST_MODEL_ID_FOR_ITEM));
    }

    @When("^a request is made to update cost model for subgroup$")
    public void request_to_update_cost_model_for_subgroup() {
        try {
            List<PrcProfNonBrktCstMdlDTO> costModelList = buildCostModelList();
            costModelList.get(1).setCostModelId(CukesConstants.UPDATE_COST_MODEL_ID_FOR_SUBGROUP);
            costModelController.saveUpdatedCostModel(costModelList);
        } catch (CPPRuntimeException cpp) {
            setAttribute(CukesConstants.CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME, true);
            setAttribute(CukesConstants.CPP_ERROR_TYPE, cpp.getType());
        }
    }

    @Then("^subgroup will be updated with selected cost model$")
    public void updated_cost_model_for_subgroup() {
        List<PrcProfNonBrktCstMdlDTO> savedPrcProfNonBrktCstMdlDTO = prcProfNonBrktCstMdlRepository
                .fetchPrcProfNonBrktCstMdlForCMGForCPPSeq(getContractPriceProfileSeq());

        assertThat(savedPrcProfNonBrktCstMdlDTO.size(), equalTo(6));
        assertThat(savedPrcProfNonBrktCstMdlDTO.get(1).getCostModelId(), equalTo(CukesConstants.UPDATE_COST_MODEL_ID_FOR_SUBGROUP));

    }

    @When("^a request is made to update cost model with no change$")
    public void request_to_update_cost_model_when_no_change() {
        List<PrcProfNonBrktCstMdlDTO> costModelList = buildCostModelList();
        costModelController.saveUpdatedCostModel(costModelList);
    }

    @Then("^no product type will be updated$")
    public void no_update_made_for_cost_model() {
        List<PrcProfNonBrktCstMdlDTO> savedPrcProfNonBrktCstMdlDTO = prcProfNonBrktCstMdlRepository
                .fetchPrcProfNonBrktCstMdlForCMGForCPPSeq(getContractPriceProfileSeq());

        assertThat(savedPrcProfNonBrktCstMdlDTO.size(), equalTo(6));
        for (int i = 0; i < savedPrcProfNonBrktCstMdlDTO.size(); i++) {
            assertThat(savedPrcProfNonBrktCstMdlDTO.get(i).getCostModelId(), equalTo(CukesConstants.SAVED_COST_MODEL_ID));
        }
    }

    private List<PrcProfNonBrktCstMdlDTO> buildCostModelList() {
        request_to_fetch_productType();
        List<PrcProfNonBrktCstMdlDTO> costModelList = (List<PrcProfNonBrktCstMdlDTO>) getAttribute("productTypeList");
        return costModelList;
    }

}
