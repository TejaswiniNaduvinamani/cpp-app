package com.gfs.cpp.web.controller.customerinfo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.dto.clm.ClmContractDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.component.customerinfo.CPPInformationService;
import com.gfs.cpp.web.controller.customerinfo.CPPInformationController;
import com.gfs.cpp.web.util.BaseTestClass;

public class CPPInformationControllerTest extends BaseTestClass {

    @InjectMocks
    private CPPInformationController target;

    @Mock
    private CPPInformationService cppInformationService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
    }

    private static final String FETCH_CONTRACT_PRICE_PROFILE_INFO_URL = "/customer/fetchContractPriceProfileInfo?agreementId=test&contractType=test";

    @Test
    public void shouldFetchPriceProfileInformation() throws Exception {
        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileId(1);
        cppInformationDTO.setContractPriceProfileSeq(1);
        cppInformationDTO.setVersionNumber(1);

        ClmContractDTO clmContractDTO = new ClmContractDTO();
        clmContractDTO.setAgreementId("test");
        clmContractDTO.setIsAmendment(true);
        clmContractDTO.setContractName("test");
        clmContractDTO.setContractStatus("DRAFT");
        clmContractDTO.setContractType("test");
        clmContractDTO.setParentAgreementId("test");
        clmContractDTO.setCppInformationDto(cppInformationDTO);
        clmContractDTO.setContractStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-01-01 00:00:00"));
        clmContractDTO.setContractEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-01-01 00:00:00"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getDefault());
        when(cppInformationService.fetchContractPriceProfileInfo("test", "test")).thenReturn(clmContractDTO);

        ResultActions response = mockMvc.perform(get(FETCH_CONTRACT_PRICE_PROFILE_INFO_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        ClmContractDTO actual = objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), ClmContractDTO.class);

        response.andExpect(status().isOk());

        assertThat(actual.getCppInformationDto().getContractPriceProfileId(),
                equalTo(clmContractDTO.getCppInformationDto().getContractPriceProfileId()));
        assertThat(actual.getCppInformationDto().getContractPriceProfileSeq(),
                equalTo(clmContractDTO.getCppInformationDto().getContractPriceProfileSeq()));
        assertThat(actual.getCppInformationDto().getVersionNumber(), equalTo(clmContractDTO.getCppInformationDto().getVersionNumber()));
        assertThat(actual.getAgreementId(), equalTo(clmContractDTO.getAgreementId()));
        assertThat(actual.getContractName(), equalTo(clmContractDTO.getContractName()));
        assertThat(actual.getContractType(), equalTo(clmContractDTO.getContractType()));
        assertThat(actual.getParentAgreementId(), equalTo(clmContractDTO.getParentAgreementId()));
        assertThat(actual.getIsAmendment(), equalTo(clmContractDTO.getIsAmendment()));

        verify(cppInformationService).fetchContractPriceProfileInfo("test", "test");
    }

}