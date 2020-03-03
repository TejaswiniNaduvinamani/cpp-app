package com.gfs.cpp.web.controller.splitcase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
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
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;
import com.gfs.cpp.component.splitcase.SplitCaseService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.web.controller.splitcase.SplitCaseController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class SplitCaseControllerTest extends BaseTestClass {

    public static final String FETCH_SPLITCASE_URL = "/splitcase/fetchSplitCase?contractPriceProfileId=1&pricingEffectiveDate=01/01/2017&pricingExpirationDate=01/01/2017";
    public static final String SAVE_SPLITCASE_URL = "/splitcase/saveSplitCaseFee";

    @InjectMocks
    private SplitCaseController splitCaseController;

    @Mock
    private CppUserDetailsService gfsUserDetailsService;
    @Mock
    private SplitCaseService splitCaseService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(splitCaseController).build();
    }

    @Test
    public void saveSplitCaseFeeTest() throws Exception {

        SplitCaseGridDTO splitCaseGridDTO = new SplitCaseGridDTO();
        String json = new ObjectMapper().writeValueAsString(splitCaseGridDTO);
        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);
        ResultActions result = mockMvc.perform(
                post(SAVE_SPLITCASE_URL).requestAttr("splitCaseDTO", splitCaseGridDTO).content(json).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(splitCaseService).saveOrUpdateSplitCase(splitCaseGridDTO, userName);
    }

    @Test
    public void fetchSplitCaseFeeTest() throws Exception {
        Integer contractPriceProfileSeq = 1;
        Date effDate = new LocalDate(2017, 01, 01).toDate();
        Date expDate = new LocalDate(2017, 01, 01).toDate();
        List<SplitCaseDTO> splitCaseGridList = new ArrayList<>();
        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        splitCaseDTO.setItemPriceId("1");
        splitCaseDTO.setProductType("2");
        splitCaseDTO.setSplitCaseFee("35");
        splitCaseDTO.setUnit("%");
        splitCaseGridList.add(splitCaseDTO);
        SplitCaseGridDTO splitCaseGridDTO = new SplitCaseGridDTO();
        splitCaseGridDTO.setSplitCaseFeeValues(splitCaseGridList);
        when(splitCaseService.fetchSplitCaseFee(contractPriceProfileSeq, effDate, expDate)).thenReturn(splitCaseGridDTO);
        ResultActions result = mockMvc.perform(get(FETCH_SPLITCASE_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        ArrayList<SplitCaseDTO> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<ArrayList<SplitCaseDTO>>() {
                });

        assertThat(resultsobj.get(0).getItemPriceId(), equalTo("1"));
        assertThat(resultsobj.get(0).getProductType(), equalTo("2"));
        assertThat(resultsobj.get(0).getSplitCaseFee(), equalTo("35"));
        result.andExpect(status().isOk());

        verify(splitCaseService).fetchSplitCaseFee(contractPriceProfileSeq, effDate, expDate);

    }
}
