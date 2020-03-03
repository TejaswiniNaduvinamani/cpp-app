package com.gfs.cpp.component.markup;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
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
import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentHelper;
import com.gfs.cpp.component.furtherance.FurtheranceChangeTracker;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class ItemMarkupDeleteHandlerTest {

    private static final String userName = "test user";
    private static final String itemPriceId = "1";
    private static final int gfsCustomerTypeCode = 31;
    private static final String gfsCustomerId = "1";
    private static final int cppFurtheranceSeq = 1;
    private static final int contractPriceProfileSeq = 1;
    private static final Date expirationDate = new LocalDate(2010, 01, 01).toDate();
    private static final String itemDesc = "item desc";

    @InjectMocks
    private ItemMarkupDeleteHandler target;

    @Mock
    private CPPDateUtils cppDateUtils;
    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;
    @Mock
    private FurtheranceChangeTracker furtheranceChangeTracker;
    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;
    @Mock
    private ItemAssignmentHelper itemAssignmentHelper;

    @Test
    public void shouldDeleteItemLevelMarkupWithTrackingEntry() {

        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);
        when(cppDateUtils.oneDayPreviousCurrentDate()).thenReturn(expirationDate);
        when(customerItemPriceRepository.fetchAlreadyExistingItemsOrSubgroups(expireItemList, gfsCustomerId, gfsCustomerTypeCode, contractPriceProfileSeq,
                ItemPriceLevel.ITEM.getCode())).thenReturn(expireItemList);

        target.deleteItemMarkupForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId, userName);

        verify(cppDateUtils).oneDayPreviousCurrentDate();
        verify(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(expireItemList, gfsCustomerId, gfsCustomerTypeCode, contractPriceProfileSeq,
                ItemPriceLevel.ITEM.getCode());
        verify(furtheranceChangeTracker).addTrackingForMarkupDelete(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList, userName);
        verify(customerItemPriceRepository).expireItemPricing(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList,
                ItemPriceLevel.ITEM.getCode(), expirationDate, userName);

    }

    @Test
    public void shouldNotDeleteItemLevelMarkupAndNoEntryInTrackingIsMade() {

        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        when(customerItemPriceRepository.fetchAlreadyExistingItemsOrSubgroups(expireItemList, gfsCustomerId, gfsCustomerTypeCode, contractPriceProfileSeq,
                ItemPriceLevel.ITEM.getCode())).thenReturn(null);

        target.deleteItemMarkupForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId, userName);

        verify(furtheranceChangeTracker, never()).addTrackingForMarkupDelete(anyInt(), any(String.class), anyInt(), anyListOf(String.class),
                any(String.class));
        verify(customerItemPriceRepository, never()).expireItemPricing(anyInt(), any(String.class), anyInt(), anyListOf(String.class), anyInt(),
                any(Date.class), any(String.class));
        verify(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(expireItemList, gfsCustomerId, gfsCustomerTypeCode, contractPriceProfileSeq,
                ItemPriceLevel.ITEM.getCode());
    }

    @Test
    public void shouldDeleteMappedItemForFurtherance() {

        int customerItemDesc = 1;
        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);
        when(cppDateUtils.oneDayPreviousCurrentDate()).thenReturn(expirationDate);

        FutureItemDescriptionDTO futureItemDescriptionDTO = buildFutureItemDescriptionDTO(customerItemDesc);

        doReturn(futureItemDescriptionDTO).when(customerItemDescPriceRepository).fetchFutureItemForFurtherance(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode, itemDesc);

        target.deleteMappledItemMarkupForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                itemDesc, userName);

        verify(itemAssignmentHelper).expireItemMapping(customerItemDesc, itemPriceId, userName);
        verify(furtheranceChangeTracker).addTrackingForMarkupDelete(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList, userName);
        verify(customerItemPriceRepository).expireItemPricing(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList,
                ItemPriceLevel.ITEM.getCode(), expirationDate, userName);

        verify(cppDateUtils).oneDayPreviousCurrentDate();
        verify(customerItemDescPriceRepository).fetchFutureItemForFurtherance(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, itemDesc);

    }

    private FutureItemDescriptionDTO buildFutureItemDescriptionDTO(int customerItemDesc) {
        FutureItemDescriptionDTO futureItemDescriptionDTO = new FutureItemDescriptionDTO();
        futureItemDescriptionDTO.setCustomerItemDescSeq(customerItemDesc);
        futureItemDescriptionDTO.setFutureItemDesc("chcicken");
        futureItemDescriptionDTO.setGfsCustomerId("1001");
        futureItemDescriptionDTO.setGfsCustomerTypeCode(31);
        return futureItemDescriptionDTO;
    }

}
