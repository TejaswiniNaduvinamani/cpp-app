package com.gfs.cpp.component.assignment;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.assignment.ItemAssignmentDuplicateValidator;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class ItemAssignmentDuplicateValidatorTest {

    private static final String ITEM_ID = "-100129";
    private static final String GFS_CUSTOMER_ID = "customer";
    private static final int CUSTOMER_TYPE_CODE = 31;

    @InjectMocks
    private ItemAssignmentDuplicateValidator target;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Test
    public void shouldReturnFlagFalseWhenItemsNotMappedToConceptForAssignItem() throws Exception {
        int contractPriceProfileSeq = -1001;
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq);
        List<String> existingItemList = new ArrayList<>();

        doReturn(existingItemList).when(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(Collections.singletonList(ITEM_ID),
                GFS_CUSTOMER_ID, CUSTOMER_TYPE_CODE, contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());

        target.validateOnAssignItems(itemAssignmentWrapperDTO);

        verify(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(Collections.singletonList(ITEM_ID), GFS_CUSTOMER_ID, CUSTOMER_TYPE_CODE,
                contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());
    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldReturnFlagTrueWhenDuplicateItemsOnRequestForAssignItem() throws Exception {
        int contractPriceProfileSeq = -1001;
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq);
        itemAssignmentWrapperDTO.getItemAssignmentList().addAll(buildItemAssignmentDTOList());

        target.validateOnAssignItems(itemAssignmentWrapperDTO);

    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldReturnTrueWhenItemsAlreadyMappedForAssignItem() throws Exception {
        int contractPriceProfileSeq = -1001;
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq);
        List<String> existingItemList = new ArrayList<>();
        existingItemList.add(ITEM_ID);

        doReturn(existingItemList).when(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(Collections.singletonList(ITEM_ID),
                GFS_CUSTOMER_ID, CUSTOMER_TYPE_CODE, contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());

        target.validateOnAssignItems(itemAssignmentWrapperDTO);

        verify(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(Collections.singletonList(ITEM_ID), GFS_CUSTOMER_ID, CUSTOMER_TYPE_CODE,
                contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());
    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldReturnTrueWhenItemsAlreadyMappedForFindItemInformation() throws Exception {
        int contractPriceProfileSeq = -1001;
        List<String> existingItemList = new ArrayList<>();
        existingItemList.add(ITEM_ID);

        doReturn(existingItemList).when(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(Collections.singletonList(ITEM_ID),
                GFS_CUSTOMER_ID, CUSTOMER_TYPE_CODE, contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());

        target.validateOnFindItemInformation(ITEM_ID, GFS_CUSTOMER_ID, CUSTOMER_TYPE_CODE, contractPriceProfileSeq);

        verify(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(Collections.singletonList(ITEM_ID), GFS_CUSTOMER_ID, CUSTOMER_TYPE_CODE,
                contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());
    }

    @Test
    public void shouldReturnFlagFalseWhenItemsNotMappedToConceptForFindItemInformation() throws Exception {
        int contractPriceProfileSeq = -1001;
        List<String> existingItemList = new ArrayList<>();

        doReturn(existingItemList).when(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(Collections.singletonList(ITEM_ID),
                GFS_CUSTOMER_ID, CUSTOMER_TYPE_CODE, contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());

        target.validateOnFindItemInformation(ITEM_ID, GFS_CUSTOMER_ID, CUSTOMER_TYPE_CODE, contractPriceProfileSeq);

        verify(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(Collections.singletonList(ITEM_ID), GFS_CUSTOMER_ID, CUSTOMER_TYPE_CODE,
                contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());
    }

    private ItemAssignmentWrapperDTO buildItemAssignmentWrapperDTO(int contractPriceProfileSeq) {
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        itemAssignmentWrapperDTO.setExceptionName("exception name");
        itemAssignmentWrapperDTO.setFutureItemDesc("item future desc");
        itemAssignmentWrapperDTO.setGfsCustomerId(GFS_CUSTOMER_ID);
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(CUSTOMER_TYPE_CODE);
        itemAssignmentWrapperDTO.setIsFutureItemSaved(true);
        List<ItemAssignmentDTO> itemAssignmentDTOList = buildItemAssignmentDTOList();
        itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentDTOList);
        return itemAssignmentWrapperDTO;
    }

    private List<ItemAssignmentDTO> buildItemAssignmentDTOList() {
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("item desc");
        itemAssignmentDTO.setItemId(ITEM_ID);
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTOList.add(itemAssignmentDTO);
        return itemAssignmentDTOList;
    }

}
