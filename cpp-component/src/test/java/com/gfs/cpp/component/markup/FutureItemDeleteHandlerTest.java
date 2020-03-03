package com.gfs.cpp.component.markup;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.furtherance.FurtheranceChangeTracker;
import com.gfs.cpp.component.markup.FutureItemDeleteHandler;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class FutureItemDeleteHandlerTest {

    @InjectMocks
    private FutureItemDeleteHandler target;

    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private CppItemMappingRepository cppItemMappingRepository;

    @Mock
    private FurtheranceChangeTracker furtheranceChangeTracker;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldDeleteFutureItem() throws Exception {
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "13";
        int gfsCustomerType = 31;
        String itemDesc = "test";

        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setCustomerItemDescSeq(10);
        itemLevelMarkupDTO.setItemDesc(itemDesc);
        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(10);
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add("1001");
        itemIdList.add("1002");

        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList)).thenReturn(itemAssignmentDTOList);
        when(customerItemDescPriceRepository.fetchFutureItemForAssignment(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemDesc))
                .thenReturn(itemLevelMarkupDTO);

        target.deleteFutureItemWithAssignedItems(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemDesc);

        verify(customerItemDescPriceRepository).deleteFutureItem(contractPriceProfileSeq, gfsCustomerId, itemDesc);
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(customerItemDescSeqList);
    }

    @Test
    public void shouldDeleteFutureItemAndAssignedItemsWithIt() throws Exception {
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "13";
        int gfsCustomerType = 31;
        String itemDesc = "test";

        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setCustomerItemDescSeq(10);
        itemLevelMarkupDTO.setItemDesc(itemDesc);
        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(10);
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemId("1001");
        ItemAssignmentDTO itemAssignmentDTO1 = new ItemAssignmentDTO();
        itemAssignmentDTO1.setItemId("1002");
        itemAssignmentDTOList.add(itemAssignmentDTO);
        itemAssignmentDTOList.add(itemAssignmentDTO1);
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add("1001");
        itemIdList.add("1002");

        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList)).thenReturn(itemAssignmentDTOList);
        when(customerItemDescPriceRepository.fetchFutureItemForAssignment(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemDesc))
                .thenReturn(itemLevelMarkupDTO);

        target.deleteFutureItemWithAssignedItems(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemDesc);

        verify(customerItemDescPriceRepository).deleteFutureItem(contractPriceProfileSeq, gfsCustomerId, itemDesc);
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(customerItemDescSeqList);
        verify(customerItemDescPriceRepository).fetchFutureItemForAssignment(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemDesc);
        verify(customerItemPriceRepository).deleteExistingItemOrSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemIdList,
                ItemPriceLevel.ITEM.getCode());
    }

    @Test
    public void shouldDeleteFutureItemForFurtherance() throws Exception {
        int contractPriceProfileSeq = 1;
        String cmgCustomerId = "13";
        int cmgCustomerTypeCode = 31;
        String itemDesc = "test";
        int cppFurtheranceSeq = 1;
        String userName = "user";
        String itemPriceId = "123";
        Date expirationDate = new LocalDate(2010, 01, 01).toDate();
        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setCustomerItemDescSeq(10);
        itemLevelMarkupDTO.setItemDesc(itemDesc);
        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(10);
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("test");
        itemAssignmentDTO.setEffectiveDate(new Date());
        itemAssignmentDTO.setExpirationDate(new Date());
        itemAssignmentDTO.setItemId(itemPriceId);
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTO.setItemPriceLevelCode(1);
        itemAssignmentDTO.setCustomerItemDescSeq(100);
        itemAssignmentDTOList.add(itemAssignmentDTO);

        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList)).thenReturn(itemAssignmentDTOList);
        when(customerItemDescPriceRepository.fetchFutureItemForAssignment(contractPriceProfileSeq, cmgCustomerId, cmgCustomerTypeCode, itemDesc))
                .thenReturn(itemLevelMarkupDTO);
        when(cppDateUtils.oneDayPreviousCurrentDate()).thenReturn(expirationDate);

        target.deleteFutureItemWithAssignedItemsForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, cmgCustomerId, cmgCustomerTypeCode,
                itemDesc, userName);

        verify(customerItemDescPriceRepository).deleteFutureItem(contractPriceProfileSeq, cmgCustomerId, itemDesc);
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(customerItemDescSeqList);
        verify(furtheranceChangeTracker).addTrackingForMarkupDelete(cppFurtheranceSeq, cmgCustomerId, cmgCustomerTypeCode, expireItemList, userName);
        verify(cppDateUtils).oneDayPreviousCurrentDate();
        verify(customerItemPriceRepository).expireItemPricing(contractPriceProfileSeq, cmgCustomerId, cmgCustomerTypeCode, expireItemList,
                ItemPriceLevel.ITEM.getCode(), expirationDate, userName);
    }

}
