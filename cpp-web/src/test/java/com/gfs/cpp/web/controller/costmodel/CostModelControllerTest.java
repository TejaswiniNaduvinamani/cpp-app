package com.gfs.cpp.web.controller.costmodel;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.dto.costmodel.CostModelResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.component.costmodel.CostModelService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.web.controller.costmodel.CostModelController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class CostModelControllerTest extends BaseTestClass {

    public static final String FETCH_ALL_ACTIVE_COST_MODELS_URL = "/costModel/fetchAllActiveCostModels";
    public static final String FETCH_COST_GRID_URL = "/costModel/fetchCostGrid?contractPriceProfileSeq=1";
    public static final String SAVE_UPDATED_COST_MODEL_URL = "/costModel/saveCostGrid";

    @InjectMocks
    CostModelController target;

    @Mock
    private CostModelService costModelService;

    @Mock
    private CppUserDetailsService gfsUserDetailsService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
    }

    @Test
    public void testFetchAllActiveCostModels() throws Exception {

        List<CostModelResponseDTO> costModelDTOList = new ArrayList<>();

        CostModelResponseDTO costModelResponseDTO = new CostModelResponseDTO();
        costModelResponseDTO.setCostModelId(71);
        costModelResponseDTO.setCostModelTypeValue("Default Cost Model Name");

        costModelDTOList.add(costModelResponseDTO);

        when(costModelService.fetchAllActiveCostModels()).thenReturn(costModelDTOList);

        ResultActions result = mockMvc.perform(get(FETCH_ALL_ACTIVE_COST_MODELS_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(costModelService, times(1)).fetchAllActiveCostModels();
    }

    @Test
    public void testFetchCostModelForGrid() throws Exception {

        Integer contractPriceProfileSeq = 1;
        List<PrcProfNonBrktCstMdlDTO> PrcProfNonBrktCstMdlDTOList = new ArrayList<>();
        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = buildPrcProfNonBrktCstMdlDTO(contractPriceProfileSeq);
        PrcProfNonBrktCstMdlDTOList.add(prcProfNonBrktCstMdlDTO);

        when(costModelService.fetchProductTypeAndCostModel(contractPriceProfileSeq)).thenReturn(PrcProfNonBrktCstMdlDTOList);

        ResultActions result = mockMvc.perform(get(FETCH_COST_GRID_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(costModelService, times(1)).fetchProductTypeAndCostModel(contractPriceProfileSeq);
    }

    @Test
    public void testSaveUpdatedCostModel() throws Exception {

        int contractPriceProfileSeq = 1;
        List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTOList = new ArrayList<>();
        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = buildPrcProfNonBrktCstMdlDTO(contractPriceProfileSeq);
        prcProfNonBrktCstMdlDTOList.add(prcProfNonBrktCstMdlDTO);
        String userName = "user name";
        String json = new ObjectMapper().writeValueAsString(prcProfNonBrktCstMdlDTOList);

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(post(SAVE_UPDATED_COST_MODEL_URL)
                .requestAttr("prcProfNonBrktCstMdlDTOList", prcProfNonBrktCstMdlDTOList).content(json).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();

        verify(costModelService).updateCostModel(prcProfNonBrktCstMdlDTOList, userName);
    }

    private PrcProfNonBrktCstMdlDTO buildPrcProfNonBrktCstMdlDTO(Integer contractPriceProfileSeq) {

        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfNonBrktCstMdlDTO.setCostModelId(1);
        prcProfNonBrktCstMdlDTO.setCostModelTypeValue("costModelTypeValue");
        prcProfNonBrktCstMdlDTO.setGfsCustomerId("gfsCustomerId");
        prcProfNonBrktCstMdlDTO.setGfsCustomerTypeCode(1);
        prcProfNonBrktCstMdlDTO.setGroupType("groupType");
        prcProfNonBrktCstMdlDTO.setItemPriceId("itemPriceId");
        prcProfNonBrktCstMdlDTO.setItemPriceLevelCode(1);
        return prcProfNonBrktCstMdlDTO;
    }
}
