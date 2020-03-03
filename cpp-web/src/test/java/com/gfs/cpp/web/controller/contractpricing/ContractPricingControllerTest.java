package com.gfs.cpp.web.controller.contractpricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.component.contractpricing.ContractPricingService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.web.controller.contractpricing.ContractPricingController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class ContractPricingControllerTest extends BaseTestClass {

    public static final String CONTRACT_PRICING_URL = "/contractPricing/savePricingInformation";
    public static final String FETCH_CPPSEQ_URL = "/contractPricing/fetchCPPSequence?contractPriceProfileId=1";
    public static final String FETCH_PRICING_INFORMATION_URL = "/contractPricing/fetchPricingInformation?contractPriceProfileSeq=1&agreementId=test&contractStatus=test";
    public static final String DELETE_PRICING_EXHIBIT_URL = "/contractPricing/deletePricingExhibit?agreementId=test";

    @InjectMocks
    private ContractPricingController contractPricingController;

    @Mock
    private ContractPricingService contractPricingService;

    @Mock
    private CppUserDetailsService gfsUserDetailsService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(contractPricingController).build();
    }

    @Test
    public void saveContractPricing() throws Exception {
        ContractPricingDTO contractPricingDTO = new ContractPricingDTO();
        String json = new ObjectMapper().writeValueAsString(contractPricingDTO);
        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);
        ResultActions result = mockMvc.perform(post(CONTRACT_PRICING_URL).requestAttr("contractPricingDTO", contractPricingDTO).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(contractPricingService).saveContractPricing(eq(contractPricingDTO), eq(userName));
    }

    @Test
    public void fetchCPPSequence() throws Exception {
        Integer contractPriceProfileId = 1;
        when(contractPricingService.fetchCPPSequence(contractPriceProfileId)).thenReturn(0);
        ResultActions result = mockMvc.perform(get(FETCH_CPPSEQ_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(contractPricingService).fetchCPPSequence(contractPriceProfileId);
    }

    @Test
    public void fetchPricingInformation() throws Exception {
        int contractPriceProfileSeq = 1;
        ContractPricingDTO contractPricingDTO = new ContractPricingDTO();
        ContractPricingDTO contractPricingDTOresultsobj = new ContractPricingDTO();
        contractPricingDTO.setAssessmentFeeFlag(Boolean.TRUE);
        contractPricingDTO.setPriceAuditFlag(Boolean.TRUE);
        contractPricingDTO.setTransferFeeFlag(Boolean.TRUE);
        contractPricingDTO.setPriceVerificationFlag(Boolean.TRUE);
        contractPricingDTO.setContractName("Test");
        contractPricingDTO.setContractType("test");
        contractPricingDTO.setContractPriceProfileId(1);
        contractPricingDTO.setPricingEffectiveDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-01-01 00:00:00"));
        contractPricingDTO.setPricingExpirationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-01-01 00:00:00"));
        contractPricingDTO.setAgreementId("test");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getDefault());
        when(contractPricingService.fetchPricingInformation(contractPriceProfileSeq, "test", "test")).thenReturn(contractPricingDTO);
        ResultActions result = mockMvc.perform(get(FETCH_PRICING_INFORMATION_URL).contentType(MediaType.APPLICATION_JSON_VALUE));
        contractPricingDTOresultsobj = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ContractPricingDTO.class);
        result.andExpect(status().isOk());

        assertThat(contractPricingDTOresultsobj.getAssessmentFeeFlag(), is(contractPricingDTO.getAssessmentFeeFlag()));
        assertThat(contractPricingDTOresultsobj.getContractName(), is(contractPricingDTO.getContractName()));
        assertThat(contractPricingDTOresultsobj.getContractPriceProfileId(), is(contractPricingDTO.getContractPriceProfileId()));
        assertThat(contractPricingDTOresultsobj.getContractType(), is(contractPricingDTO.getContractType()));
        assertThat(contractPricingDTOresultsobj.getPriceAuditFlag(), is(contractPricingDTO.getPriceAuditFlag()));
        assertThat(contractPricingDTOresultsobj.getPriceVerificationFlag(), is(contractPricingDTO.getPriceVerificationFlag()));
        assertThat(contractPricingDTOresultsobj.getPricingEffectiveDate(), is(contractPricingDTO.getPricingEffectiveDate()));
        assertThat(contractPricingDTOresultsobj.getPricingExpirationDate(), is(contractPricingDTO.getPricingExpirationDate()));
        assertThat(contractPricingDTOresultsobj.getScheduleForCostChange(), is(contractPricingDTO.getScheduleForCostChange()));
        assertThat(contractPricingDTOresultsobj.getTransferFeeFlag(), is(contractPricingDTO.getTransferFeeFlag()));

        verify(contractPricingService).fetchPricingInformation(contractPriceProfileSeq, "test", "test");
    }

    @Test
    public void deletePricingExhibit() throws Exception {
        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(delete(DELETE_PRICING_EXHIBIT_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().is2xxSuccessful());
        verify(gfsUserDetailsService).getCurrentUserId();
    }

}
