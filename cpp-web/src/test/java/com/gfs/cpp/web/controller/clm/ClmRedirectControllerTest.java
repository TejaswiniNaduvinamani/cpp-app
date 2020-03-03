package com.gfs.cpp.web.controller.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.web.controller.clm.ClmRedirectController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClmRedirectControllerTest extends BaseTestClass {
	
    public static final String RETURN_TO_CLM_URL = "/clm/returnToClm";
    public static final String CLM_URL = "TestUrl";
	
	@InjectMocks
	private ClmRedirectController target;
	
    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
        ReflectionTestUtils.setField(target, "clmUrl", CLM_URL);
    }
    
    @Test
    public void returnToClm() throws Exception {
    	String clmUrl = "TestUrl";
    	
    	 ResultActions result = mockMvc.perform(get(RETURN_TO_CLM_URL).contentType(MediaType.APPLICATION_JSON_VALUE));
    	 
    	 Map<String, String> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                 new TypeReference<HashMap<String, String>>() {
                 });

 		assertThat(resultsobj.get(CPPConstants.CLM_URL_KEY), equalTo(clmUrl));
         result.andExpect(status().isOk());
    }
}
