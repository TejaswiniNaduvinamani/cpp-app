package com.gfs.cpp.web.controller.assignments;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.gfs.cpp.common.dto.assignments.CMGCustomerTypeDTO;
import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentValidationDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.assignments.MarkupAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.component.assignment.AssignmentService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.web.controller.assignments.AssignmentController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class AssignmentControllerTest extends BaseTestClass {

    @InjectMocks
    private AssignmentController target;

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private CppUserDetailsService gfsUserDetailsService;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(target).build();
    }

    public static final String FETCH_MARKUP_URL = "/assignments/fetchMarkup?contractPriceProfileSeq=1";
    public static final String FETCH_CUSTOMER_TYPES_URL = "/assignments/fetchCustomerTypes";
    public static final String FIND_CUSTOMER_URL = "/assignments/findCustomer?gfsCustomerId=1&gfsCustomerType=31&contractPriceProfileSeq=1&cmgCustomerId=1&cmgCustomerType=31";
    public static final String SAVE_ASSIGNMENTS_URL = "/assignments/saveAssignments";
    public static final String FIND_ALL_FUTURE_ITEMS_URL = "/assignments/fetchAllFutureItems?contractPriceProfileSeq=2938";
    public static final String DELETE_CUSTOMER_MAPPING_URL = "/assignments/deleteCustomerAssignment?realCustomerId=1&realCustomerType=17&contractPriceProfileSeq=21&cmgCustomerId=1&cmgCustomerType=31";
    public static final String ASSIGN_ITEMS_URL = "/assignments/assignItems";
    public static final String DELETE_ITEM_MAPPING_URL = "/assignments/deleteItemAssignment?contractPriceProfileSeq=1&gfsCustomerId=1&gfsCustomerTypeCode=31&itemId=1000&futureItemDesc=chicken";

    @Test
    public void shouldFetchMarkupTest() throws Exception {
        int contractPriceProfileSeq = 1;
        List<MarkupAssignmentDTO> resultList = new ArrayList<>();

        when(assignmentService.fetchMarkupAssignment(eq(contractPriceProfileSeq))).thenReturn(resultList);

        ResultActions result = mockMvc.perform(get(FETCH_MARKUP_URL).contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(status().isOk());

        verify(assignmentService).fetchMarkupAssignment(eq(contractPriceProfileSeq));
    }

    @Test
    public void shouldFetchCustomerTypesTest() throws Exception {

        List<CMGCustomerTypeDTO> customerTypeList = new ArrayList<>();
        CMGCustomerTypeDTO cmgCustomer = new CMGCustomerTypeDTO();
        cmgCustomer.setCustomerTypeId(1);
        cmgCustomer.setCustomerTypeValue("TEST_CMG");
        customerTypeList.add(cmgCustomer);

        when(assignmentService.fetchCustomerTypes()).thenReturn(customerTypeList);

        ResultActions result = mockMvc.perform(get(FETCH_CUSTOMER_TYPES_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        ArrayList<CMGCustomerTypeDTO> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<ArrayList<CMGCustomerTypeDTO>>() {
                });

        assertThat(resultsobj.get(0).getCustomerTypeId(), equalTo(1));
        assertThat(resultsobj.get(0).getCustomerTypeValue(), equalTo("TEST_CMG"));
        result.andExpect(status().isOk());

        verify(assignmentService).fetchCustomerTypes();
    }

    @Test
    public void shouldFindCustomerTest() throws Exception {

        RealCustomerResponseDTO realCustomerResponseDTO = new RealCustomerResponseDTO();
        String customerName = "Heinz Ketchup";
        realCustomerResponseDTO.setCustomerName(customerName);
        String gfsCustomerId = "1";
        int gfsCustomerType = 31;
        String cmgCustomerId = "1";
        int cmgCustomerType = 31;
        when(assignmentService.findCustomerName(eq(gfsCustomerId), eq(gfsCustomerType), eq(1), eq(cmgCustomerId), eq(cmgCustomerType)))
                .thenReturn(realCustomerResponseDTO);

        ResultActions result = mockMvc.perform(get(FIND_CUSTOMER_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        RealCustomerResponseDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<RealCustomerResponseDTO>() {
                });

        assertThat(resultsobj.getCustomerName(), equalTo(customerName));
        result.andExpect(status().isOk());

        verify(assignmentService).findCustomerName(eq(gfsCustomerId), eq(gfsCustomerType), eq(1), eq(gfsCustomerId), eq(gfsCustomerType));
    }

    @Test
    public void shouldThrowCPPExceptionWhenFindCustomerTestAccrossContracts() throws Exception {

        String gfsCustomerId = "1";
        int gfsCustomerType = 31;
        String cmgCustomerId = "1";
        int cmgCustomerType = 31;
        String conceptName = "Test";
        int contractPriceProfileId = 1;
        RealCustomerResponseDTO realCustomerResponseDTO = new RealCustomerResponseDTO();
        realCustomerResponseDTO.setCustomerName(StringUtils.EMPTY);
        realCustomerResponseDTO.setValidationCode(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT.getErrorCode());
        realCustomerResponseDTO.setValidationMessage(
                String.format("This customer has already been assigned to %s concept (CPP ID: %d)", conceptName, contractPriceProfileId));

        when(assignmentService.findCustomerName(eq(gfsCustomerId), eq(gfsCustomerType), eq(1), eq(cmgCustomerId), eq(cmgCustomerType)))
                .thenReturn(realCustomerResponseDTO);

        ResultActions result = mockMvc.perform(get(FIND_CUSTOMER_URL).contentType(MediaType.APPLICATION_JSON_VALUE));
        RealCustomerResponseDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<RealCustomerResponseDTO>() {
                });
        assertThat(resultsobj.getValidationMessage(),
                equalTo(String.format("This customer has already been assigned to %s concept (CPP ID: %d)", conceptName, contractPriceProfileId)));
        result.andExpect(status().isOk());

        verify(assignmentService).findCustomerName(eq(gfsCustomerId), eq(gfsCustomerType), eq(1), eq(gfsCustomerId), eq(gfsCustomerType));
    }

    @Test
    public void shouldThrowCPPExceptionWhenFindCustomerTestForDefaultConcept() throws Exception {

        String gfsCustomerId = "1";
        int gfsCustomerType = 31;
        String cmgCustomerId = "1";
        int cmgCustomerType = 31;
        RealCustomerResponseDTO realCustomerResponseDTO = new RealCustomerResponseDTO();
        realCustomerResponseDTO.setCustomerName(StringUtils.EMPTY);
        realCustomerResponseDTO.setValidationCode(CPPExceptionType.HAS_MULTIPLE_CUSTOMER_MAPPING.getErrorCode());
        realCustomerResponseDTO.setValidationMessage("Multiple customer assignments are not allowed on the default/contract level concept");
        when(assignmentService.findCustomerName(eq(gfsCustomerId), eq(gfsCustomerType), eq(1), eq(cmgCustomerId), eq(cmgCustomerType)))
                .thenReturn(realCustomerResponseDTO);

        ResultActions result = mockMvc.perform(get(FIND_CUSTOMER_URL).contentType(MediaType.APPLICATION_JSON_VALUE));
        RealCustomerResponseDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<RealCustomerResponseDTO>() {
                });
        assertThat(resultsobj.getValidationMessage(), equalTo("Multiple customer assignments are not allowed on the default/contract level concept"));
        result.andExpect(status().isOk());

        verify(assignmentService).findCustomerName(eq(gfsCustomerId), eq(gfsCustomerType), eq(1), eq(gfsCustomerId), eq(gfsCustomerType));
    }

    @Test
    public void shouldSaveAssignments() throws Exception {
        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setCmgCustomerId("1");
        customerAssignmentDTO.setCmgCustomerType(31);
        customerAssignmentDTO.setContractPriceProfileSeq(100);
        customerAssignmentDTO.setRealCustomerDTOList(new ArrayList<RealCustomerDTO>());
        String json = new ObjectMapper().writeValueAsString(customerAssignmentDTO);
        String userName = "vc71u";
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCode(HttpStatus.OK.value());
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);
        when(assignmentService.saveAssignments(customerAssignmentDTO, userName)).thenReturn(statusDTO);

        ResultActions result = mockMvc.perform(post(SAVE_ASSIGNMENTS_URL).requestAttr("saveAssignmentDTO", customerAssignmentDTO).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(assignmentService).saveAssignments(eq(customerAssignmentDTO), eq(userName));
    }

    @Test
    public void shouldFetchAllFutureItemsTest() throws Exception {

        List<ItemAssignmentWrapperDTO> itemAssignmentWrapperDTOList = new ArrayList<>();
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        List<ItemAssignmentDTO> itemAssignmentList = new ArrayList<>();
        ItemAssignmentDTO itemAssigmentDTO = new ItemAssignmentDTO();
        itemAssigmentDTO.setItemDescription(StringUtils.EMPTY);
        itemAssigmentDTO.setItemId(StringUtils.EMPTY);
        itemAssignmentList.add(itemAssigmentDTO);
        itemAssignmentWrapperDTO.setExceptionName("Test");
        itemAssignmentWrapperDTO.setFutureItemDesc("Test");
        itemAssignmentWrapperDTO.setIsFutureItemSaved(false);
        itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentList);
        itemAssignmentWrapperDTOList.add(itemAssignmentWrapperDTO);

        int contractPriceProfileSeq = 2938;
        when(assignmentService.fetchAllFutureItems(contractPriceProfileSeq)).thenReturn(itemAssignmentWrapperDTOList);
        ResultActions result = mockMvc.perform(get(FIND_ALL_FUTURE_ITEMS_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        ArrayList<ItemAssignmentWrapperDTO> resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(),
                new TypeReference<ArrayList<ItemAssignmentWrapperDTO>>() {
                });

        assertThat(resultsobj.get(0).getExceptionName(), equalTo("Test"));
        assertThat(resultsobj.get(0).getFutureItemDesc(), equalTo("Test"));
        assertThat(resultsobj.get(0).getItemAssignmentList(), equalTo(itemAssignmentList));
        assertThat(resultsobj.get(0).getIsFutureItemSaved(), equalTo(false));
        result.andExpect(status().isOk());

        verify(assignmentService).fetchAllFutureItems(contractPriceProfileSeq);

    }

    @Test
    public void shouldDeleteCustomerMapping() throws Exception {
        String realCustomerId = "1";
        int realCustomerType = 17;
        int contractPriceProfileSeq = 21;
        String cmgCustomerId = "1";
        int cmgCustomerType = 31;
        ResultActions result = mockMvc.perform(delete(DELETE_CUSTOMER_MAPPING_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(assignmentService).deleteCustomerAssignment(realCustomerId, realCustomerType, contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
    }

    @Test
    public void shouldAssignItems() throws Exception {
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(1);
        itemAssignmentWrapperDTO.setExceptionName("test");
        itemAssignmentWrapperDTO.setFutureItemDesc("test_item");
        itemAssignmentWrapperDTO.setGfsCustomerId("1");
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(31);
        itemAssignmentWrapperDTO.setIsFutureItemSaved(false);
        List<ItemAssignmentDTO> itemAssignmentList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("item");
        itemAssignmentDTO.setItemId("1");
        itemAssignmentList.add(itemAssignmentDTO);
        itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentList);
        ItemAssignmentValidationDTO itemAssignmentValidatioDTO = new ItemAssignmentValidationDTO();
        itemAssignmentValidatioDTO.setIsItemAlreadyExist(false);
        itemAssignmentValidatioDTO.setDuplicateItemIdList(new ArrayList<String>());

        String json = new ObjectMapper().writeValueAsString(itemAssignmentWrapperDTO);
        String userName = "vc71u";
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCode(HttpStatus.OK.value());

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);
        when(assignmentService.assignItems(itemAssignmentWrapperDTO, userName)).thenReturn(statusDTO);

        ResultActions result = mockMvc.perform(post(ASSIGN_ITEMS_URL).requestAttr("futureItemResponseDTO", itemAssignmentWrapperDTO).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(assignmentService).assignItems(eq(itemAssignmentWrapperDTO), eq(userName));
    }

    @Test
    public void shouldThrowCPPRuntimeExceptionWhenValidationFailedForAssignItems() throws Exception {
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(1);
        itemAssignmentWrapperDTO.setExceptionName("test");
        itemAssignmentWrapperDTO.setFutureItemDesc("test_item");
        itemAssignmentWrapperDTO.setGfsCustomerId("1");
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(31);
        itemAssignmentWrapperDTO.setIsFutureItemSaved(false);
        List<ItemAssignmentDTO> itemAssignmentList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("item");
        itemAssignmentDTO.setItemId("1");
        itemAssignmentList.add(itemAssignmentDTO);
        itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentList);
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setErrorCode(CPPExceptionType.ITEM_ALREADY_EXIST.getErrorCode());
        statusDTO.setErrorMessage(String.format("(%s)", "1"));
        statusDTO.setErrorType(CPPExceptionType.ITEM_ALREADY_EXIST.toString());
        String json = new ObjectMapper().writeValueAsString(itemAssignmentWrapperDTO);
        String userName = "vc71u";

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);
        when(assignmentService.assignItems(itemAssignmentWrapperDTO, userName)).thenReturn(statusDTO);

        ResultActions result = mockMvc.perform(post(ASSIGN_ITEMS_URL).requestAttr("futureItemResponseDTO", itemAssignmentWrapperDTO).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        StatusDTO resultsobj = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<StatusDTO>() {
        });

        assertThat(resultsobj.getErrorCode(), equalTo(CPPExceptionType.ITEM_ALREADY_EXIST.getErrorCode()));
        assertThat(resultsobj.getErrorMessage(), equalTo("(1)"));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(assignmentService).assignItems(eq(itemAssignmentWrapperDTO), eq(userName));
    }

    @Test
    public void shouldDeleteItemMapping() throws Exception {
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        int contractPriceProfileSeq = 1;
        String itemId = "1000";
        String futureItemDesc = "chicken";
        String userName = "vc71u";

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(delete(DELETE_ITEM_MAPPING_URL).contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());

        verify(gfsUserDetailsService).getCurrentUserId();
        verify(assignmentService).deleteItemAssignment(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, itemId, futureItemDesc, userName);
    }

}
