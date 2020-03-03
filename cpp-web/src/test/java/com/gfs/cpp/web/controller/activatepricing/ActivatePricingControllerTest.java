package com.gfs.cpp.web.controller.activatepricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.HashMap;
import java.util.Map;

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
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.component.activatepricing.ActivatePricingService;
import com.gfs.cpp.component.contractpricing.ContractPricingService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.web.controller.activatepricing.ActivatePricingController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class ActivatePricingControllerTest extends BaseTestClass {

    public static final String ACTIVATE_PRICING_URL = "/activatePricing/activatePricingForCustomer?contractPriceProfileSeq=1&isAmendment=false&clmContractStatus=Executed";
    public static final String ACTIVATE_PRICING_FOR_AMENDMENT_URL = "/activatePricing/activatePricingForCustomer?contractPriceProfileSeq=1&isAmendment=true&clmContractStatus=Executed";
    public static final String ENABLE_ACTIVATE_PRICING_URL = "/activatePricing/enableActivatePricing?contractPriceProfileSeq=1";

    @InjectMocks
    private ActivatePricingController target;

    @Mock
    private ContractPricingService contractPricingService;

    @Mock
    private ActivatePricingService activatePricingService;

    @Mock
    private CppUserDetailsService cppUserDetailsService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
    }

    @Test
    public void shouldActivatePricingForInitialContract() throws Exception {
        int contractPriceProfileSeq = 1;
        String userName = "vc71u";
        when(cppUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(get(ACTIVATE_PRICING_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        verify(cppUserDetailsService).getCurrentUserId();
        verify(activatePricingService).activatePricing(eq(contractPriceProfileSeq), eq(userName), eq(false), eq(ClmContractStatus.EXECUTED.value));
    }

    @Test
    public void shouldActivatePricingForAmendment() throws Exception {
        int contractPriceProfileSeq = 1;
        String userName = "vc71u";
        when(cppUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(get(ACTIVATE_PRICING_FOR_AMENDMENT_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        verify(cppUserDetailsService).getCurrentUserId();
        verify(activatePricingService).activatePricing(eq(contractPriceProfileSeq), eq(userName), eq(true), eq(ClmContractStatus.EXECUTED.value));
    }

    @Test
    public void shouldEnableActivatePricing() throws Exception {
        int contractPriceProfileSeq = 1;

        Map<String, Boolean> validateActivatePricingEnablerMap = new HashMap<>();
        validateActivatePricingEnablerMap.put(CPPConstants.ENABLE_ACTIVATE_PRICING_BUTTON, true);

        when(activatePricingService.validateActivatePricingEnabler(contractPriceProfileSeq)).thenReturn(validateActivatePricingEnablerMap);

        ResultActions result = mockMvc.perform(get(ENABLE_ACTIVATE_PRICING_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        Map<String, Boolean> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<HashMap<String, Boolean>>() {
                });
        result.andExpect(status().isOk());
        assertThat(resultsobj.get(CPPConstants.ENABLE_ACTIVATE_PRICING_BUTTON), equalTo(true));

        verify(activatePricingService).validateActivatePricingEnabler(eq(contractPriceProfileSeq));
    }
}
