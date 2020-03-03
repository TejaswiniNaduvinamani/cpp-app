package com.gfs.cpp.web.controller.furtherance;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.furtherance.FurtheranceBaseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceDocumentDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.dto.furtherance.SplitCaseGridFurtheranceDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.component.furtherance.FurtheranceService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.proxy.clm.FileRemover;
import com.gfs.cpp.web.controller.furtherance.FurtheranceController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class FurtheranceControllerTest extends BaseTestClass {

    public static final String SAVE_FURTHERANCE_INFORMATION_URL = "/furtherance/saveFurtheranceInformation";
    public static final String FETCH_FURTHERANCE_INFORMATION_URL = "/furtherance/fetchFurtheranceInformation?parentAgreementId=parent-clm-agreement-id&cppFurtheranceSeq=1";
    public static final String FETCH_IN_PROGRESS_FURTHERANCE_URL = "/furtherance/hasInProgressFurtherance?parentAgreementId=test";
    public static final String FETCH_FURTHERANCE_BASE_INFO_URL = "/furtherance/fetchInProgressFurtheranceInfo?parentAgreementId=test";
    public static final String CREATE_NEW_FURTHERANCE_URL = "/furtherance/createNewFurtherance?parentAgreementId=test&contractType=testContract";
    public static final String FURTHERANCE_ACTIVATE_PRICING_URL = "/furtherance/activatePricingForFurtherance?cppFurtheranceSeq=1&clmContractStatus=Executed";
    public static final String DELETE_ITEM_FURTHERANCE_URL = "/furtherance/deleteItemLevelMarkup?contractPriceProfileSeq=1&cppFurtheranceSeq=1&gfsCustomerId=13&gfsCustomerTypeCode=31&itemId=1&itemDesc=item";
    public static final String SAVE_SPLITCASE_FEE_FURTHERANCE_URL = "/furtherance/saveSplitCaseFee";
    public static final String CREATE_FURTHERANCE_DOCUMENT_URL = "/furtherance/createFurtheranceDocument?cppFurtheranceSeq=1";
    public static final String FETCH_MAPPED_ITEMS_FOR_FURTHERANCE_URL = "/furtherance/fetchMappedItemsForFurtherance?contractPriceProfileSeq=1001&gfsCustomerId=100&gfsCustomerTypeCode=31&itemDesc=test_futureItem";
    public static final String ENABLE_ACTIVATE_PRICING_URL = "/furtherance/enableActivatePricingForFurtherance?cppFurtheranceSeq=1&clmContractStatus=test";
    public static final String SAVE_PRICING_FURTHERANCE_URL = "/furtherance/savePricingDocumentForFurtherance";
    public static final String DELETE_MAPPED_ITEM_FOR_FURTHERANCE_URL = "/furtherance/deleteMappedItemForFurtherance?contractPriceProfileSeq=1&cppFurtheranceSeq=1&gfsCustomerId=13&gfsCustomerTypeCode=31&itemId=1&itemDesc=item";
    public static final String ASSIGN_ITEMS_FOR_FURTHERANCE_URL = "/furtherance/assignItemsForFurtherance";
    public static final String SAVE_MARKUP_FOR_FURTHERANCE_URL = "/furtherance/saveMarkupForFurtherance";

    @InjectMocks
    private FurtheranceController target;

    @Mock
    private FurtheranceService furtheranceService;
    
    @Mock
    private FileRemover fileRemover;

    @Mock
    private CppUserDetailsService gfsUserDetailsService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
    }

    @Test
    public void shouldDeleteItem() throws Exception {
        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(delete(DELETE_ITEM_FURTHERANCE_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(furtheranceService).deleteItemLevelMarkup(1, 1, "13", 31, "1", "item", userName);

    }

    @Test
    public void shouldFetchHasInProgressFurtherance() throws Exception {
        String parentAgreementId = "test";

        doReturn(false).when(furtheranceService).hasInProgressFurtherance(parentAgreementId);

        ResultActions result = mockMvc.perform(get(FETCH_IN_PROGRESS_FURTHERANCE_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        Map<String, Boolean> actual = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<HashMap<String, Boolean>>() {
                });

        result.andExpect(status().isOk());

        assertThat(actual.get("hasInProgressFurtherance"), equalTo(false));

        verify(furtheranceService).hasInProgressFurtherance(parentAgreementId);

    }

    @Test
    public void shouldSaveFurtheranceInformation() throws Exception {

        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceInformationDTO();

        String json = new ObjectMapper().writeValueAsString(furtheranceInformationDTO);
        String userName = "vc71u";

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(post(SAVE_FURTHERANCE_INFORMATION_URL)
                .requestAttr("furtheranceInformationDTO", furtheranceInformationDTO).content(json).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(furtheranceService).saveFurtheranceInformation(furtheranceInformationDTO, userName);

    }

    @Test
    public void shouldFetchFurtheranceInfo() throws Exception {
        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceInformationDTO();

        doReturn(furtheranceInformationDTO).when(furtheranceService).fetchFurtheranceInformation(furtheranceInformationDTO.getParentCLMAgreementId(),
                1);

        ResultActions result = mockMvc.perform(get(FETCH_FURTHERANCE_INFORMATION_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        FurtheranceInformationDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                FurtheranceInformationDTO.class);

        result.andExpect(status().isOk());
        assertThat(resultsobj.getChangeReasonTxt(), equalTo("reason text"));
        assertThat(resultsobj.getContractPriceProfileSeq(), equalTo(1));
        assertThat(resultsobj.getContractReferenceTxt(), equalTo("reference text"));
        assertThat(resultsobj.getCppFurtheranceSeq(), equalTo(1));
        assertThat(resultsobj.getFurtheranceDocumentGUID(), equalTo("1234"));
        assertThat(resultsobj.getFurtheranceStatusCode(), equalTo(1));
        assertThat(resultsobj.getParentCLMAgreementId(), equalTo(furtheranceInformationDTO.getParentCLMAgreementId()));

        verify(furtheranceService).fetchFurtheranceInformation(furtheranceInformationDTO.getParentCLMAgreementId(), 1);
    }

    @Test
    public void shouldFetchInProgressFurtheranceInfo() throws Exception {
        FurtheranceBaseDTO furtheranceBaseDTO = buildFurtheranceBaseDTO();

        doReturn(furtheranceBaseDTO).when(furtheranceService).fetchInProgressFurtheranceInfo(furtheranceBaseDTO.getAgreementId());

        ResultActions result = mockMvc.perform(get(FETCH_FURTHERANCE_BASE_INFO_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        FurtheranceBaseDTO actual = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<FurtheranceBaseDTO>() {
                });

        result.andExpect(status().isOk());

        assertThat(actual.getAgreementId(), equalTo(furtheranceBaseDTO.getAgreementId()));
        assertThat(actual.getContractType(), equalTo(furtheranceBaseDTO.getContractType()));

        verify(furtheranceService).fetchInProgressFurtheranceInfo(furtheranceBaseDTO.getAgreementId());

    }

    @Test
    public void shouldCreateNewFurtherance() throws Exception {

        FurtheranceBaseDTO furtheranceBaseDTO = buildFurtheranceBaseDTO();

        doReturn(furtheranceBaseDTO).when(furtheranceService).createNewFurtherance(furtheranceBaseDTO.getAgreementId(),
                furtheranceBaseDTO.getContractType());

        ResultActions result = mockMvc.perform(get(CREATE_NEW_FURTHERANCE_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        FurtheranceBaseDTO actual = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<FurtheranceBaseDTO>() {
                });

        result.andExpect(status().isOk());

        assertThat(actual.getAgreementId(), equalTo(furtheranceBaseDTO.getAgreementId()));
        assertThat(actual.getContractType(), equalTo(furtheranceBaseDTO.getContractType()));

        verify(furtheranceService).createNewFurtherance(furtheranceBaseDTO.getAgreementId(), furtheranceBaseDTO.getContractType());

    }

    @Test
    public void shouldActivatePricingForFurtherance() throws Exception {
        int cppFurtheranceSeq = 1;
        String userName = "test";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(get(FURTHERANCE_ACTIVATE_PRICING_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        verify(gfsUserDetailsService).getCurrentUserId();
        verify(furtheranceService).activatePricingForFurtherance(eq(cppFurtheranceSeq), eq(userName), eq(ClmContractStatus.EXECUTED.value));
    }

    @Test
    public void shouldEnableActivatePricingForFurtheranceButton() throws Exception {
        int cppFurtheranceSeq = 1;
        String clmContractStatus = "test";
        Map<String, Boolean> response = new HashMap<>();
        response.put("enableActivatePricingButton", true);

        when(furtheranceService.validateActivatePricingForFurtherance(cppFurtheranceSeq, clmContractStatus)).thenReturn(response);
        ResultActions result = mockMvc.perform(get(ENABLE_ACTIVATE_PRICING_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        Map<String, Boolean> actual = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<Map<String, Boolean>>() {
                });

        result.andExpect(status().isOk());
        assertThat(actual.get(CPPConstants.ENABLE_ACTIVATE_PRICING_BUTTON), equalTo(true));

        verify(furtheranceService).validateActivatePricingForFurtherance(cppFurtheranceSeq, clmContractStatus);
    }

    @Test
    public void shouldSaveSplitcaseFee() throws Exception {
        SplitCaseGridFurtheranceDTO splitCaseGridFurtheranceDTO = new SplitCaseGridFurtheranceDTO();

        String json = new ObjectMapper().writeValueAsString(splitCaseGridFurtheranceDTO);
        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(post(SAVE_SPLITCASE_FEE_FURTHERANCE_URL)
                .requestAttr("splitCaseGridFurtheranceDTO", splitCaseGridFurtheranceDTO).content(json).contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(furtheranceService).saveSplitCaseFeeForFurtherance(splitCaseGridFurtheranceDTO, userName);
    }

    @Test
    public void shouldSaveMarkupForFurthernce() throws Exception {
        MarkupWrapperDTO markupWrapperDTO = buildMarkupWrapperDTO();
        String userName = "vc71u";
        String json = new ObjectMapper().writeValueAsString(markupWrapperDTO);

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(post(SAVE_MARKUP_FOR_FURTHERANCE_URL).requestAttr("markupWrapperDTO", markupWrapperDTO).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(furtheranceService).saveMarkupForFurtherance(markupWrapperDTO, userName);

    }

    @Test
    public void shouldFetchMappedItemsForFurtherance() throws Exception {
        int contractPriceProfileSeq = 1001;
        String gfsCustomerId = "100";
        int gfsCustomerTypeCode = 31;
        String itemDesc = "test_futureItem";
        String itemId = "1234";

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);

        doReturn(itemAssignmentWrapperDTO).when(furtheranceService).fetchMappedItemsForFurtherance(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode, itemDesc);

        ResultActions result = mockMvc.perform(get(FETCH_MAPPED_ITEMS_FOR_FURTHERANCE_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        ItemAssignmentWrapperDTO actual = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<ItemAssignmentWrapperDTO>() {
                });

        result.andExpect(status().isOk());

        ItemAssignmentDTO itemAssignmentDTO = actual.getItemAssignmentList().get(0);

        assertThat(actual.getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(actual.getFutureItemDesc(), equalTo(itemDesc));
        assertThat(actual.getGfsCustomerId(), equalTo(gfsCustomerId));
        assertThat(itemAssignmentDTO.getCustomerItemDescSeq(), equalTo(1000));
        assertThat(itemAssignmentDTO.getItemDescription(), equalTo("test"));
        assertThat(itemAssignmentDTO.getItemId(), equalTo(itemId));

        verify(furtheranceService).fetchMappedItemsForFurtherance(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, itemDesc);

    }

    @Test
    public void shouldCreateFurtheranceDocument() throws Exception {
        FurtheranceDocumentDTO furtheranceExhibit = new FurtheranceDocumentDTO();
        buildFurtheranceDocumentDTO(furtheranceExhibit);
        File file = new File(CPPConstants.DEFAULT_DOCX_NAME + CPPConstants.DOCX);
        file.createNewFile();

        when(furtheranceService.createFurtheranceDocument(1)).thenReturn(file);
        when(fileRemover.deleteFile(file)).thenReturn(true);

        ResultActions result = mockMvc.perform(get(CREATE_FURTHERANCE_DOCUMENT_URL));
        result.andExpect(status().isOk());

        verify(furtheranceService).createFurtheranceDocument(1);
        verify(fileRemover).deleteFile(file);
    }

    @Test
    public void shouldSavePricingDocumentForFurtherance() throws Exception {

        FurtheranceBaseDTO furtheranceBaseDTO = buildFurtheranceBaseDTO();
        String userName = "test user";

        String json = new ObjectMapper().writeValueAsString(furtheranceBaseDTO);

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(post(SAVE_PRICING_FURTHERANCE_URL).requestAttr("savePricingFurtherance", furtheranceBaseDTO)
                .content(json).contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(status().isOk());

        verify(furtheranceService).savePricingDocumentForFurtherance(furtheranceBaseDTO, userName);
    }

    @Test
    public void shouldDeleteMappedItemForFurtherance() throws Exception {
        String userName = "vc71u";
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(delete(DELETE_MAPPED_ITEM_FOR_FURTHERANCE_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(furtheranceService).deleteMappedItemForFurtherance(1, 1, "13", 31, "1", "item", userName);

    }

    @Test
    public void shouldAssignItemsForFurtherance() throws Exception {

        int contractPriceProfileSeq = 100;
        String gfsCustomerId = "1001";
        int gfsCustomerTypeCode = 31;
        String itemId = "1234";

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);

        String json = new ObjectMapper().writeValueAsString(itemAssignmentWrapperDTO);
        String userName = "vc71u";

        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCode(HttpStatus.OK.value());

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);
        when(furtheranceService.assignItemsForFurtherance(itemAssignmentWrapperDTO, userName)).thenReturn(statusDTO);

        ResultActions result = mockMvc.perform(post(ASSIGN_ITEMS_FOR_FURTHERANCE_URL)
                .requestAttr("itemAssignmentWrapperDTO", itemAssignmentWrapperDTO).content(json).contentType(MediaType.APPLICATION_JSON_VALUE));

        StatusDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<StatusDTO>() {
        });

        result.andExpect(status().isOk());
        assertThat(resultsobj.getErrorCode(), equalTo(HttpStatus.OK.value()));

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(furtheranceService).assignItemsForFurtherance(itemAssignmentWrapperDTO, userName);

    }

    @Test
    public void shouldThrowCPPRuntimeExceptionWhenAssignItemsForFurtherance() throws Exception {

        int contractPriceProfileSeq = 100;
        String gfsCustomerId = "1001";
        int gfsCustomerTypeCode = 31;
        String itemId = "1234";

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);

        String json = new ObjectMapper().writeValueAsString(itemAssignmentWrapperDTO);
        String userName = "vc71u";

        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCode(CPPExceptionType.ITEM_ALREADY_EXIST.getErrorCode());
        statusDTO.setErrorMessage(String.format("(%s)", "1"));
        statusDTO.setErrorType(CPPExceptionType.ITEM_ALREADY_EXIST.toString());

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);
        when(furtheranceService.assignItemsForFurtherance(itemAssignmentWrapperDTO, userName)).thenReturn(statusDTO);

        ResultActions result = mockMvc.perform(post(ASSIGN_ITEMS_FOR_FURTHERANCE_URL)
                .requestAttr("itemAssignmentWrapperDTO", itemAssignmentWrapperDTO).content(json).contentType(MediaType.APPLICATION_JSON_VALUE));

        StatusDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<StatusDTO>() {
        });

        assertThat(resultsobj.getErrorCode(), equalTo(CPPExceptionType.ITEM_ALREADY_EXIST.getErrorCode()));
        assertThat(resultsobj.getErrorMessage(), equalTo("(1)"));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(furtheranceService).assignItemsForFurtherance(itemAssignmentWrapperDTO, userName);
    }

    private ItemAssignmentWrapperDTO buildItemAssignmentWrapperDTO(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemId) {
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        itemAssignmentWrapperDTO.setFutureItemDesc("test_futureItem");
        itemAssignmentWrapperDTO.setGfsCustomerId(gfsCustomerId);
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        itemAssignmentWrapperDTO.setIsFutureItemSaved(true);
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setCustomerItemDescSeq(1000);
        itemAssignmentDTO.setItemDescription("test");
        itemAssignmentDTO.setItemId(itemId);
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTOList.add(itemAssignmentDTO);
        itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentDTOList);
        return itemAssignmentWrapperDTO;
    }

    private MarkupWrapperDTO buildMarkupWrapperDTO() {
        MarkupWrapperDTO markupWrapperDTO = new MarkupWrapperDTO();
        markupWrapperDTO.setContractPriceProfileSeq(1);
        markupWrapperDTO.setCppFurtheranceSeq(1);
        markupWrapperDTO.setItemLevelMarkupList(new ArrayList<ItemLevelMarkupDTO>());
        markupWrapperDTO.setMarkupName("test markup");
        markupWrapperDTO.setProductMarkupList(new ArrayList<ProductTypeMarkupDTO>());
        markupWrapperDTO.setSubgroupMarkupList(new ArrayList<SubgroupMarkupDTO>());
        return markupWrapperDTO;
    }

    private FurtheranceBaseDTO buildFurtheranceBaseDTO() {
        FurtheranceBaseDTO furtheranceBaseDTO = new FurtheranceBaseDTO();
        furtheranceBaseDTO.setAgreementId("test");
        furtheranceBaseDTO.setCppFurtheranceSeq(1);
        furtheranceBaseDTO.setContractType("testContract");
        return furtheranceBaseDTO;
    }

    private FurtheranceInformationDTO buildFurtheranceInformationDTO() {
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setChangeReasonTxt("reason text");
        furtheranceInformationDTO.setContractPriceProfileSeq(1);
        furtheranceInformationDTO.setContractReferenceTxt("reference text");
        furtheranceInformationDTO.setCppFurtheranceSeq(1);
        furtheranceInformationDTO.setFurtheranceDocumentGUID("1234");
        furtheranceInformationDTO.setFurtheranceEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_INITIATED.getCode());
        furtheranceInformationDTO.setParentCLMAgreementId("parent-clm-agreement-id");
        return furtheranceInformationDTO;
    }

    private void buildFurtheranceDocumentDTO(FurtheranceDocumentDTO furtheranceExhibit) {
        List<MarkupGridDTO> markupGridDTOList = new ArrayList<>();
        List<SplitCaseDTO> splitCaseFeeValues = new ArrayList<>();

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();
        markupGridDTO.setMarkupName("TEST");
        markupGridDTOList.add(markupGridDTO);
        MarkupReviewDTO markupDTO = new MarkupReviewDTO();
        markupDTO.setMarkupGridDTOs(markupGridDTOList);

        SplitCaseDTO splitCaseGridDTO = new SplitCaseDTO();
        splitCaseFeeValues.add(splitCaseGridDTO);
        SplitCaseReviewDTO splitCaseDTO = new SplitCaseReviewDTO();
        splitCaseDTO.setSplitCaseFeeValues(splitCaseFeeValues);

        furtheranceExhibit.setMarkupDTO(markupDTO);
        furtheranceExhibit.setSplitCaseDTO(splitCaseDTO);
    }
}
