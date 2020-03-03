package com.gfs.cpp.component.item;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.item.common.exception.MissingRequiredParameterException;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentValidationDTO;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.assignment.ItemAssignmentDuplicateValidator;
import com.gfs.cpp.component.item.ItemService;
import com.gfs.cpp.proxy.ItemInformationDTOBuilder;
import com.gfs.cpp.proxy.ItemQueryProxy;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemService target;

    @Mock
    private ItemQueryProxy itemQueryProxy;

    @Mock
    private ItemInformationDTOBuilder itemInformationDTOBuilder;

    @Mock
    private ItemAssignmentDuplicateValidator itemAssignmentDuplicateValidator;

    @Test
    public void shouldFindItemInformation() throws MissingRequiredParameterException {
        String offeringId = "681204";
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setIsActive(true);
        itemInformationDTO.setIsValid(true);
        itemInformationDTO.setItemDescription("Test");
        itemInformationDTO.setItemNo("681204");
        itemInformationDTO.setStockingCode("3");
        itemInformationDTO.setItemStatusCode("AC");

        when(itemQueryProxy.findItemInformation(offeringId)).thenReturn(itemInformationDTO);
        ItemInformationDTO itemInformation = target.findItemInformation("681204");

        assertThat(itemInformation.getIsActive(), equalTo(true));
        assertThat(itemInformation.getIsValid(), equalTo(true));
        assertThat(itemInformation.getStockingCode(), equalTo("3"));
        assertThat(itemInformation.getItemNo(), equalTo("681204"));
        assertThat(itemInformation.getItemStatusCode(), equalTo("AC"));

        verify(itemQueryProxy).findItemInformation(offeringId);
    }

    @Test
    public void shouldReturnItemAlreadyExistFlagTrueWhenFindItemInformation() throws MissingRequiredParameterException {

        String itemId = "681204";
        String cmgCustomerId = "1";
        int cmgCustomerTypeCode = 31;
        int contractPriceProfileSeq = 1;

        ItemAssignmentValidationDTO itemAssignmentValidationDTO = new ItemAssignmentValidationDTO();
        List<String> duplicateItemIdList = new ArrayList<>();
        duplicateItemIdList.add("1");
        itemAssignmentValidationDTO.setDuplicateItemIdList(duplicateItemIdList);
        itemAssignmentValidationDTO.setIsItemAlreadyExist(true);

        doThrow(new CPPRuntimeException(CPPExceptionType.ITEM_ALREADY_EXIST, "")).when(itemAssignmentDuplicateValidator)
                .validateOnFindItemInformation(eq(itemId), eq(cmgCustomerId), eq(cmgCustomerTypeCode), eq(contractPriceProfileSeq));
        ItemInformationDTO itemInformation = target.findItemInformation(itemId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq);

        assertThat(itemInformation.getIsItemAlreadyExist(), equalTo(true));

        verify(itemAssignmentDuplicateValidator).validateOnFindItemInformation(eq(itemId), eq(cmgCustomerId), eq(cmgCustomerTypeCode),
                eq(contractPriceProfileSeq));
    }

    @Test
    public void shouldReturnItemAlreadyExistFlagFalseWhenFindItemInformation() throws MissingRequiredParameterException {

        String itemId = "681204";
        String cmgCustomerId = "1";
        int cmgCustomerTypeCode = 31;
        int contractPriceProfileSeq = 1;

        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setIsActive(true);
        itemInformationDTO.setIsValid(true);
        itemInformationDTO.setItemDescription("Test");
        itemInformationDTO.setItemNo(itemId);
        itemInformationDTO.setStockingCode("3");
        itemInformationDTO.setItemStatusCode("AC");

        ItemAssignmentValidationDTO itemAssignmentValidationDTO = new ItemAssignmentValidationDTO();
        List<String> duplicateItemIdList = new ArrayList<>();
        itemAssignmentValidationDTO.setDuplicateItemIdList(duplicateItemIdList);
        itemAssignmentValidationDTO.setIsItemAlreadyExist(false);

        when(itemQueryProxy.findItemInformation(itemId)).thenReturn(itemInformationDTO);
        ItemInformationDTO itemInformation = target.findItemInformation(itemId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq);

        assertThat(itemInformation.getIsActive(), equalTo(true));
        assertThat(itemInformation.getIsValid(), equalTo(true));
        assertThat(itemInformation.getStockingCode(), equalTo("3"));
        assertThat(itemInformation.getItemNo(), equalTo("681204"));
        assertThat(itemInformation.getItemStatusCode(), equalTo("AC"));
        assertThat(itemInformation.getIsItemAlreadyExist(), equalTo(false));

        verify(itemQueryProxy).findItemInformation(itemId);
        verify(itemAssignmentDuplicateValidator).validateOnFindItemInformation(eq(itemId), eq(cmgCustomerId), eq(cmgCustomerTypeCode),
                eq(contractPriceProfileSeq));
    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldThrowExceptionWhenFindItemInformation() throws MissingRequiredParameterException {

        String itemId = "681204";
        String cmgCustomerId = "1";
        int cmgCustomerTypeCode = 31;
        int contractPriceProfileSeq = 1;

        ItemAssignmentValidationDTO itemAssignmentValidationDTO = new ItemAssignmentValidationDTO();
        List<String> duplicateItemIdList = new ArrayList<>();
        duplicateItemIdList.add("1");
        itemAssignmentValidationDTO.setDuplicateItemIdList(duplicateItemIdList);
        itemAssignmentValidationDTO.setIsItemAlreadyExist(true);

        doThrow(new CPPRuntimeException(CPPExceptionType.SERVICE_FAILED, "")).when(itemAssignmentDuplicateValidator)
                .validateOnFindItemInformation(eq(itemId), eq(cmgCustomerId), eq(cmgCustomerTypeCode), eq(contractPriceProfileSeq));
        ItemInformationDTO itemInformation = target.findItemInformation(itemId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq);

        assertThat(itemInformation.getIsItemAlreadyExist(), equalTo(true));

        verify(itemAssignmentDuplicateValidator).validateOnFindItemInformation(eq(itemId), eq(cmgCustomerId), eq(cmgCustomerTypeCode),
                eq(contractPriceProfileSeq));
    }
}
