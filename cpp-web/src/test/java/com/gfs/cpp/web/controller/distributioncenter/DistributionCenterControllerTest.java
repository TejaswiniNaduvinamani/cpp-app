package com.gfs.cpp.web.controller.distributioncenter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterDTO;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSaveDTO;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDO;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;
import com.gfs.cpp.component.distributioncenter.DistributionCenterService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.web.controller.distributioncenter.DistributionCenterController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class DistributionCenterControllerTest extends BaseTestClass {

    public static final String CONTRACT_DIST_URL = "/distributionCenter/distributionCentersForCompany";
    public static final String SAVE_CONTRACT_DIST_URL = "/distributionCenter/saveDistributionCenters";
    public static final String FETCH_SAVE_CONTRACT_DIST_URL = "/distributionCenter/fetchSavedDistributionCenters?contractPriceProfileSeq=629";

    @InjectMocks
    DistributionCenterController target;

    @Mock
    DistributionCenterDO distributionCenterDO;

    @Mock
    DistributionCenterService distributionCenterService;

    @Mock
    private CppUserDetailsService gfsUserDetailsService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
    }

    @Test
    public void fetchDistributionCenters() throws Exception {
        ArrayList<DistributionCenterDTO> distCenterList = new ArrayList<>();
        DistributionCenterDTO distList = new DistributionCenterDTO();
        distList.setDcNumber("1");
        distList.setName("aberdeen");
        distList.setShortName("abd");
        distList.setWmsDBInstanceId("abc1");
        distCenterList.add(distList);
        when(distributionCenterService.fetchAllDistirbutionCenters(1, "AC", "en")).thenReturn(distCenterList);
        ResultActions result = mockMvc.perform(get(CONTRACT_DIST_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(distributionCenterService).fetchAllDistirbutionCenters(1, "AC", "en");
    }

    @Test
    public void saveDistributionCenters() throws Exception {
        ArrayList<String> dcCodes = new ArrayList<String>();
        dcCodes.add("22");
        dcCodes.add("23");
        distributionCenterDO.setContractPriceProfileSeq(1);
        distributionCenterDO.setDcCodes(dcCodes);
        DistributionCenterSaveDTO distributionCenterSaveDTO = new DistributionCenterSaveDTO();
        String json = new ObjectMapper().writeValueAsString(distributionCenterSaveDTO);
        distributionCenterSaveDTO.setContractPriceProfileSeq(distributionCenterDO.getContractPriceProfileSeq());
        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);
        ResultActions result = mockMvc.perform(post(SAVE_CONTRACT_DIST_URL).content(json).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(distributionCenterService).saveDistributionCenters(eq(distributionCenterSaveDTO), eq(userName));
    }

    @Test
    public void fetchSavedDistributionCenters() throws Exception {
        Integer contractPriceProfileSeq = 629;
        ArrayList<DistributionCenterDetailDO> distCenter = new ArrayList<>();
        DistributionCenterDetailDO distCenterDO = new DistributionCenterDetailDO();
        distCenterDO.setContractPriceProfileSeq(1);
        distCenterDO.setDcCode("1");
        distCenterDO.setEffectiveDate(new Date());
        distCenterDO.setExpirationDate(new Date());
        distCenter.add(distCenterDO);

        when(distributionCenterService.fetchSavedDistributionCenters(contractPriceProfileSeq)).thenReturn(distCenter);
        ResultActions result = mockMvc.perform(get(FETCH_SAVE_CONTRACT_DIST_URL).contentType(MediaType.APPLICATION_JSON_VALUE));
        ArrayList<DistributionCenterDetailDO> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<ArrayList<DistributionCenterDetailDO>>() {
                });

        assertThat(resultsobj.get(0).getContractPriceProfileSeq(), equalTo(1));
        assertThat(resultsobj.get(0).getDcCode(), equalTo("1"));
        result.andExpect(status().isOk());

        verify(distributionCenterService).fetchSavedDistributionCenters(contractPriceProfileSeq);
    }

}
