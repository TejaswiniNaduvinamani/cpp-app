package com.gfs.cpp.web.controller.markup;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.gfs.corp.component.price.common.types.AmountType;
import com.gfs.corp.component.price.common.types.PricingUnitType;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ExceptionMarkupRenameDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupResponseDTO;
import com.gfs.cpp.component.item.ItemService;
import com.gfs.cpp.component.markup.MarkupService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class MarkupControllerTest extends BaseTestClass {

    public static final String FETCH_DEFAULT_MARKUP_URL = "/markup/fetchMarkups?contractPriceProfileSeq=1&pricingEffectiveDate=12/26/2017&pricingExpirationDate=12/30/2017";
    public static final String SAVE_MARKUP_URL = "/markup/saveMarkup";
    public static final String ADD_EXCEPTION_URL = "/markup/addException?contractPriceProfileSeq=1&exceptionName=Exception&pricingEffectiveDate=12/26/2017&pricingExpirationDate=12/30/2017";
    public static final String SAVE_MARKUP_INDICATORS_URL = "/markup/saveMarkupIndicators";
    public static final String RENAME_EXCEPTION_MARKUP_URL = "/markup/renameMarkupException";
    public static final String DELETE_EXCEPTION_URL = "/markup/deleteMarkupException?contractPriceProfileSeq=1&gfsCustomerId=13&markupName=Test";
    public static final String CREATE_DEFAULT_ITEM_LEVEL_MARKUP_URL = "/markup/createDefaultItemLevelMarkup?contractPriceProfileSeq=1&pricingEffectiveDate=12/26/2017&pricingExpirationDate=12/30/2017";
    public static final String DELETE_ITEM_URL = "/markup/deleteItemLevelMarkup?contractPriceProfileSeq=1&gfsCustomerId=13&gfsCustomerType=31&itemId=1&itemDesc=test";
    public static final String FETCH_MARKUP_INDICATORS_URL = "/markup/fetchMarkupIndicators?contractPriceProfileSeq=1";
    public static final String FETCH_ITEM_SUBGROUP_URL = "/markup/findItemSubgroupInformation?subgroupId=11&gfsCustomerId=12345&gfsCustomerType=2&contractPriceProfileSeq=1000";
    public static final String CREATE_DEFAULT_SUBGROUP_URL = "/markup/createDefaultSubgroupMarkup?pricingEffectiveDate=12/26/2017&pricingExpirationDate=12/30/2017";
    public static final String DELETE_SUBGROUP_URL = "/markup/deleteSubgroupMarkup?contractPriceProfileSeq=1&gfsCustomerId=13&gfsCustomerType=31&subgroupId=1";

    private static final Date pricingEffectiveDate = new LocalDate(2017, 12, 26).toDate();
    private static final Date pricingExpirationDate = new LocalDate(2017, 12, 30).toDate();

    @InjectMocks
    private MarkupController target;

    @Mock
    private MarkupService markupService;

    @Mock
    private ItemService itemService;

    @Mock
    private CppUserDetailsService gfsUserDetailsService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
    }

    @Test
    public void shouldSaveMarkup() throws Exception {
        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setContractPriceProfileSeq(1);
        markupWrapper.setGfsCustomerId("Test");
        String json = new ObjectMapper().writeValueAsString(markupWrapper);
        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(
                post(SAVE_MARKUP_URL).requestAttr("markupWrapepr", markupWrapper).content(json).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(markupService).saveMarkup(markupWrapper, userName);
    }

    @Test
    public void shouldFetchMarkups() throws Exception {
        int contractPriceProfileSeq = 1;
        String contractName = "Contract";
        List<MarkupWrapperDTO> markupWrapperList = new ArrayList<>();
        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setMarkupName(contractName);
        markupWrapper.setGfsCustomerId("Test");
        markupWrapper.setIsMarkupSaved(true);
        markupWrapperList.add(markupWrapper);

        doReturn(markupWrapperList).when(markupService).fetchMarkups(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate);

        ResultActions result = mockMvc.perform(get(FETCH_DEFAULT_MARKUP_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        List<MarkupWrapperDTO> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<ArrayList<MarkupWrapperDTO>>() {
                });
        result.andExpect(status().isOk());
        assertThat(resultsobj.get(0).getGfsCustomerId(), equalTo("Test"));
        assertThat(resultsobj.get(0).getIsMarkupSaved(), equalTo(true));
        assertThat(resultsobj.get(0).getMarkupName(), equalTo("Contract"));

        verify(markupService).fetchMarkups(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate);
    }

    @Test
    public void shouldAddException() throws Exception {
        int contractPriceProfileSeq = 1;
        String exceptionName = "Exception";
        String userName = "vc71u";
        String gfsCustomerId = "101";
        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setMarkupName(exceptionName);
        markupWrapper.setGfsCustomerId(gfsCustomerId);

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);
        when(markupService.addExceptionMarkup(contractPriceProfileSeq, exceptionName, pricingEffectiveDate, pricingExpirationDate, userName))
                .thenReturn(markupWrapper);

        ResultActions result = mockMvc.perform(get(ADD_EXCEPTION_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        MarkupWrapperDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<MarkupWrapperDTO>() {
                });

        assertThat(resultsobj.getGfsCustomerId(), equalTo("101"));
        assertThat(resultsobj.getIsMarkupSaved(), equalTo(false));
        assertThat(resultsobj.getMarkupName(), equalTo("Exception"));

        verify(markupService).addExceptionMarkup(contractPriceProfileSeq, exceptionName, pricingEffectiveDate, pricingExpirationDate, userName);
    }

    @Test
    public void shouldSaveMarkupIndicators() throws Exception {
        MarkupRequestDTO markupRequest = new MarkupRequestDTO();
        String json = new ObjectMapper().writeValueAsString(markupRequest);
        String userName = "vc71u";

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(post(SAVE_MARKUP_INDICATORS_URL).requestAttr("markupRequest", markupRequest).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(markupService).saveMarkupIndicators(eq(markupRequest), eq(userName));
    }

    @Test
    public void shouldRenameException() throws Exception {
        ExceptionMarkupRenameDTO renameExceptionDTO = new ExceptionMarkupRenameDTO();
        String json = new ObjectMapper().writeValueAsString(renameExceptionDTO);

        ResultActions result = mockMvc.perform(put(RENAME_EXCEPTION_MARKUP_URL).requestAttr("renameExceptionDTO", renameExceptionDTO).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(markupService).renameExceptionMarkup(renameExceptionDTO);
    }

    @Test
    public void shouldDeleteException() throws Exception {
        int contractPriceProfileSeq = 1;
        String markupId = "13";
        String markupName = "Test";
        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(delete(DELETE_EXCEPTION_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(markupService).deleteExceptionMarkup(contractPriceProfileSeq, markupId, markupName, userName);
    }

    @Test
    public void shouldFetchDefaultItemLevelMarkup() throws Exception {
        int contractPriceProfileSeq = 1;
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        itemLevelMarkupDTO.setExpirationDate(pricingExpirationDate);
        itemLevelMarkupDTO.setItemDesc(StringUtils.EMPTY);
        itemLevelMarkupDTO.setItemId(StringUtils.EMPTY);
        itemLevelMarkupDTO.setMarkup(StringUtils.EMPTY);
        itemLevelMarkupDTO.setMarkupType(PricingUnitType.SELL_UNIT.getCode());
        itemLevelMarkupDTO.setUnit(AmountType.DOLLAR.getCode());
        itemLevelMarkupDTO.setNoItemId(false);
        itemLevelMarkupDTO.setStockingCode(CPPConstants.INDICATOR_ZERO);
        doReturn(itemLevelMarkupDTO).when(markupService).createDefaultItemLevelMarkup(contractPriceProfileSeq, pricingExpirationDate,
                pricingEffectiveDate);

        ResultActions result = mockMvc.perform(get(CREATE_DEFAULT_ITEM_LEVEL_MARKUP_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        ItemLevelMarkupDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), ItemLevelMarkupDTO.class);
        assertThat(resultsobj.getMarkupType(), equalTo(1));
        assertThat(resultsobj.isNoItemId(), equalTo(false));
        assertThat(resultsobj.getStockingCode(), equalTo(0));
        assertThat(resultsobj.getUnit(), equalTo("$"));

        verify(markupService).createDefaultItemLevelMarkup(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate);
    }

    @Test
    public void shouldDeleteItemLevelMarkup() throws Exception {
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "13";
        int gfsCustomerType = 31;
        String itemId = "1";
        String itemDesc = "test";

        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(delete(DELETE_ITEM_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(markupService).deleteItemLevelMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemId, itemDesc, userName);
    }

    @Test
    public void shouldFetchMarkupOnSell() throws Exception {
        int contractPriceProfileSeq = 1;
        Map<String, Boolean> markupIndicatorsMap = new HashMap<>();
        markupIndicatorsMap.put(CPPConstants.MARKUP_ON_SELL, true);
        markupIndicatorsMap.put(CPPConstants.EXPIRE_LOWER, true);
        when(markupService.fetchMarkupIndicators(contractPriceProfileSeq)).thenReturn(markupIndicatorsMap);

        ResultActions result = mockMvc.perform(get(FETCH_MARKUP_INDICATORS_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        Map<String, Boolean> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<HashMap<String, Boolean>>() {
                });
        result.andExpect(status().isOk());
        assertThat(resultsobj.get(CPPConstants.MARKUP_ON_SELL), equalTo(true));

        verify(markupService).fetchMarkupIndicators(contractPriceProfileSeq);
    }

    @Test
    public void shouldFindItemSubgroupInformation() throws Exception {
        SubgroupResponseDTO subgroupInformationDTO = new SubgroupResponseDTO();
        subgroupInformationDTO.setSubgroupDesc("TABLETOP MISC");
        subgroupInformationDTO.setSubgroupId("11");
        String subgroupId = "11";
        String cmgCustomerId = "12345";
        int cmgCustomerTypeCode = 2;
        int contractPriceProfileSeq = 1000;

        when(markupService.findItemSubgroupInformation(subgroupId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq))
                .thenReturn(subgroupInformationDTO);

        ResultActions result = mockMvc.perform(get(FETCH_ITEM_SUBGROUP_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        SubgroupResponseDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                SubgroupResponseDTO.class);

        result.andExpect(status().isOk());

        assertThat(resultsobj.getSubgroupDesc(), equalTo(subgroupInformationDTO.getSubgroupDesc()));

        verify(markupService).findItemSubgroupInformation(subgroupId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq);
    }

    @Test
    public void shouldDeleteSubgroupMarkup() throws Exception {
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "13";
        int gfsCustomerType = 31;
        String subgroupId = "1";

        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(delete(DELETE_SUBGROUP_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(markupService).deleteSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, subgroupId, userName);
    }

    @Test
    public void shouldFetchDefaultSubgroupInformation() throws Exception {
        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        subgroupMarkupDTO.setExpirationDate(pricingExpirationDate);
        subgroupMarkupDTO.setSubgroupDesc(StringUtils.EMPTY);
        subgroupMarkupDTO.setSubgroupId(StringUtils.EMPTY);
        subgroupMarkupDTO.setMarkup(StringUtils.EMPTY);
        subgroupMarkupDTO.setMarkupType(PricingUnitType.CASE.getCode());
        subgroupMarkupDTO.setUnit(AmountType.DOLLAR.getCode());

        doReturn(subgroupMarkupDTO).when(markupService).createDefaultSubgroupMarkup(pricingExpirationDate, pricingEffectiveDate);

        ResultActions result = mockMvc.perform(get(CREATE_DEFAULT_SUBGROUP_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        SubgroupMarkupDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), SubgroupMarkupDTO.class);

        assertThat(resultsobj.getMarkupType(), equalTo(2));
        assertThat(resultsobj.getUnit(), equalTo("$"));

        verify(markupService).createDefaultSubgroupMarkup(pricingExpirationDate, pricingEffectiveDate);
    }

}
