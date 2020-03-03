package com.gfs.cpp.web.controller.search;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.dto.search.ContractSearchResultDTO;
import com.gfs.cpp.component.search.ContractSearchService;
import com.gfs.cpp.web.controller.search.ContractSearchController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class ContractSearchControllerTest extends BaseTestClass {

    public static final String SEARCH_CONTRACTS_BY_CONTRACT_NAME_URL = "/search/searchContractsByContractName?searchContractName=test";
    public static final String SEARCH_CONTRACTS_BY_CUSTOMER_URL = "/search/searchContractsByCustomer?gfsCustomerId=1001&gfsCustomerTypeCode=3";
    public static final String SEARCH_CONTRACTS_BY_CPPID_URL = "/search/searchContractsByCPPId?cppId=1001";

    private static final String aggreementId = "test agreement id";
    private static final String contractName = "TEST_CPP";
    private static final String contractType = "Regional-Original";
    private static final String customerName = "Brinker's International";

    @InjectMocks
    private ContractSearchController target;

    @Mock
    private ContractSearchService contractSearchService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
    }

    @Test
    public void shouldSearchContractByName() throws Exception {
        String contractName = "test";
        List<ContractSearchResultDTO> searchResult = new ArrayList<>();
        ContractSearchResultDTO contractSearchResultDTO = new ContractSearchResultDTO();
        contractSearchResultDTO.setAgreementId(aggreementId);
        contractSearchResultDTO.setContractName(contractName);
        contractSearchResultDTO.setContractType(contractType);
        searchResult.add(contractSearchResultDTO);
        doReturn(searchResult).when(contractSearchService).searchContractsByContractName(contractName);

        ResultActions result = mockMvc.perform(get(SEARCH_CONTRACTS_BY_CONTRACT_NAME_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        List<ContractSearchResultDTO> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<ArrayList<ContractSearchResultDTO>>() {
                });

        result.andExpect(status().isOk());
        assertThat(resultsobj.get(0).getAgreementId(), equalTo(contractSearchResultDTO.getAgreementId()));
        assertThat(resultsobj.get(0).getContractName(), equalTo(contractSearchResultDTO.getContractName()));
        assertThat(resultsobj.get(0).getContractType(), equalTo(contractSearchResultDTO.getContractType()));

        verify(contractSearchService).searchContractsByContractName(contractName);
    }

    @Test
    public void shouldSearchContractByCustomer() throws Exception {

        List<ContractSearchResultDTO> contractSearchResultDTOList = buildContractSearchResultDTOList();

        doReturn(contractSearchResultDTOList).when(contractSearchService).searchContractsByCustomer("1001", 3);

        ResultActions result = mockMvc.perform(get(SEARCH_CONTRACTS_BY_CUSTOMER_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        List<ContractSearchResultDTO> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<ArrayList<ContractSearchResultDTO>>() {
                });

        result.andExpect(status().isOk());
        assertThat(resultsobj.get(0).getAgreementId(), equalTo(aggreementId));
        assertThat(resultsobj.get(0).getContractName(), equalTo(contractName));
        assertThat(resultsobj.get(0).getContractType(), equalTo(contractType));
        assertThat(resultsobj.get(0).getCustomerName(), equalTo(customerName));

        verify(contractSearchService).searchContractsByCustomer("1001", 3);
    }

    @Test
    public void shouldSearchContractByCPPId() throws Exception {

        List<ContractSearchResultDTO> contractSearchResultDTOList = buildContractSearchResultDTOList();

        doReturn(contractSearchResultDTOList).when(contractSearchService).searchContractsByCPPId(1001);

        ResultActions result = mockMvc.perform(get(SEARCH_CONTRACTS_BY_CPPID_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        List<ContractSearchResultDTO> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<ArrayList<ContractSearchResultDTO>>() {
                });

        result.andExpect(status().isOk());
        assertThat(resultsobj.get(0).getAgreementId(), equalTo(aggreementId));
        assertThat(resultsobj.get(0).getContractName(), equalTo(contractName));
        assertThat(resultsobj.get(0).getContractType(), equalTo(contractType));
        assertThat(resultsobj.get(0).getCustomerName(), equalTo(customerName));

        verify(contractSearchService).searchContractsByCPPId(1001);
    }

    private List<ContractSearchResultDTO> buildContractSearchResultDTOList() {

        List<ContractSearchResultDTO> contractSearchResultDTOList = new ArrayList<>();
        ContractSearchResultDTO contractSearchResultDTO = new ContractSearchResultDTO();
        contractSearchResultDTO.setAgreementId(aggreementId);
        contractSearchResultDTO.setContractName(contractName);
        contractSearchResultDTO.setContractType(contractType);
        contractSearchResultDTO.setCustomerName(customerName);
        contractSearchResultDTOList.add(contractSearchResultDTO);

        return contractSearchResultDTOList;
    }
}
