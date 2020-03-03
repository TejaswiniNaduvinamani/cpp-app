package com.gfs.cpp.web.controller.authorization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
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
import com.gfs.cpp.common.service.authorization.AuthorizationDetailsDTO;
import com.gfs.cpp.component.authorization.AuthorizationService;
import com.gfs.cpp.web.controller.authorization.AuthorizationController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuthorizationControllerTest extends BaseTestClass {

    @InjectMocks
    private AuthorizationController target;

    @Mock
    private AuthorizationService authorizationService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
    }

    private static final String FETCH_AUTHORIZATION_ADDITONAL_URL = "/authorization/fetchAuthorizationDetails?contractPriceProfileSeq=1&isAmendment=false&clmStatus=EXECUTED";

    private static final String CHECK_FURTHERNACE_EDITABLE_URL = "/authorization/canEditFurtherance?furtheranceSeq=101";

    private static final String CHECK_USER_AUTHORIZED_TO_VIEW_URL = "/authorization/isUserAuthorizedToViewOnly";

    @Test
    public void shouldReturnAuthorizationDetailsWhenRequestedWithAmendmentFlagAndClmStatus() throws Exception {
        AuthorizationDetailsDTO authorizationDetails = new AuthorizationDetailsDTO();

        doReturn(authorizationDetails).when(authorizationService).buildAuthorizationDetails(1, false, "EXECUTED");

        ResultActions response = mockMvc.perform(get(FETCH_AUTHORIZATION_ADDITONAL_URL).contentType(MediaType.APPLICATION_JSON_VALUE));
        response.andExpect(status().isOk());

        AuthorizationDetailsDTO actual = new ObjectMapper().readValue(response.andReturn().getResponse().getContentAsString(),
                AuthorizationDetailsDTO.class);

        assertThat(actual, equalTo(authorizationDetails));

        verify(authorizationService).buildAuthorizationDetails(1, false, "EXECUTED");
    }

    @Test
    public void shouldCheckIfFurtheranceIsEditable() throws Exception {

        doReturn(true).when(authorizationService).canEditFurtherance(101);

        ResultActions response = mockMvc.perform(get(CHECK_FURTHERNACE_EDITABLE_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        Map<String, Boolean> actual = new ObjectMapper().readValue(response.andReturn().getResponse().getContentAsString(),
                new TypeReference<HashMap<String, Boolean>>() {
                });

        response.andExpect(status().isOk());

        assertThat(actual.get("canEditFurtherance"), equalTo(true));

        verify(authorizationService).canEditFurtherance(101);

    }
    
    @Test
    public void shouldCheckIfUserAuthorizedToViewOlny() throws Exception {

        doReturn(true).when(authorizationService).isUserAuthorizedToViewOnly();

        ResultActions response = mockMvc.perform(get(CHECK_USER_AUTHORIZED_TO_VIEW_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        Map<String, Boolean> actual = new ObjectMapper().readValue(response.andReturn().getResponse().getContentAsString(),
                new TypeReference<HashMap<String, Boolean>>() {
                });

        response.andExpect(status().isOk());

        assertThat(actual.get("isUserAuthorizedToViewOnly"), equalTo(true));

        verify(authorizationService).isUserAuthorizedToViewOnly();

    }
}
