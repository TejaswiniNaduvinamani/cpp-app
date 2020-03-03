package com.gfs.cpp.component.furtherance;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceAction;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.component.furtherance.FurtheranceChangeTracker;
import com.gfs.cpp.component.furtherance.FurtheranceDTOBuilder;
import com.gfs.cpp.data.furtherance.CppFurtheranceTrackingRepository;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceChangeTrackerTest {

    private static final String userName = "vc71u";
    private static final String itemPriceId = "1";
    private static final int gfsCustomerTypeCode = 31;
    private static final String gfsCustomerId = "1";
    private static final int cppFurtheranceSeq = 1;
    private static final String CUSTOMER_ITEM_PRICE = "CUSTOMER_ITEM_PRICE";
    private static final String PRC_PROF_LESSCASE_RULE = "PRC_PROF_LESSCASE_RULE";

    @InjectMocks
    private FurtheranceChangeTracker target;

    @Mock
    private FurtheranceDTOBuilder furtheranceDTOBuilder;

    @Mock
    private CppFurtheranceTrackingRepository cppFurtheranceTrackingRepository;

    @Captor
    ArgumentCaptor<List<CPPFurtheranceTrackingDTO>> furtheranceTrackingDTOListCaptor;

    @Test
    public void shouldAddDeleteTrackingForItemDeleteWithTrackingActionAdded() {

        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = buildCPPFurtheranceTrackingDTO(ItemPriceLevel.ITEM.getCode(),
                FurtheranceAction.DELETED.getCode(), CUSTOMER_ITEM_PRICE);

        when(furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.DELETED.getCode())).thenReturn(furtheranceTrackingDTO);
        when(cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(furtheranceTrackingDTO)).thenReturn(1);

        target.addTrackingForMarkupDelete(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList, userName);

        verify(furtheranceDTOBuilder).buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.DELETED.getCode());
        verify(cppFurtheranceTrackingRepository).fetchFurtheranceActionCode(furtheranceTrackingDTO);
        verify(cppFurtheranceTrackingRepository).deleteTrackingEntry(furtheranceTrackingDTO, FurtheranceAction.ADDED.getCode());

    }

    @Test
    public void shouldAddDeleteTrackingForItemDeleteWithTrackingActionUpdated() {

        CPPFurtheranceTrackingDTO cppFurtheranceTrackingDTO = buildCPPFurtheranceTrackingDTO(ItemPriceLevel.ITEM.getCode(),
                FurtheranceAction.DELETED.getCode(), CUSTOMER_ITEM_PRICE);
        List<CPPFurtheranceTrackingDTO> cppFurtheranceTrackingDTOList = new ArrayList<>();
        cppFurtheranceTrackingDTOList.add(cppFurtheranceTrackingDTO);
        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        when(furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.DELETED.getCode())).thenReturn(cppFurtheranceTrackingDTO);
        when(cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(cppFurtheranceTrackingDTO)).thenReturn(FurtheranceAction.UPDATED.getCode());

        target.addTrackingForMarkupDelete(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList, userName);

        verify(furtheranceDTOBuilder).buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.DELETED.getCode());
        verify(cppFurtheranceTrackingRepository).fetchFurtheranceActionCode(cppFurtheranceTrackingDTO);
        verify(cppFurtheranceTrackingRepository).deleteTrackingEntry(cppFurtheranceTrackingDTO, FurtheranceAction.UPDATED.getCode());
        verify(cppFurtheranceTrackingRepository).batchInsertRecordsInFurtheranceTracking(cppFurtheranceTrackingDTOList, userName);
    }

    @Test
    public void shouldAddDeleteTrackingForItemDeleteWithNoTrackingEntry() {

        CPPFurtheranceTrackingDTO cppFurtheranceTrackingDTO = buildCPPFurtheranceTrackingDTO(ItemPriceLevel.ITEM.getCode(),
                FurtheranceAction.DELETED.getCode(), CUSTOMER_ITEM_PRICE);
        List<CPPFurtheranceTrackingDTO> cppFurtheranceTrackingDTOList = new ArrayList<>();
        cppFurtheranceTrackingDTOList.add(cppFurtheranceTrackingDTO);
        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        when(furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.DELETED.getCode())).thenReturn(cppFurtheranceTrackingDTO);
        when(cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(cppFurtheranceTrackingDTO)).thenReturn(null);

        target.addTrackingForMarkupDelete(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList, userName);

        verify(furtheranceDTOBuilder).buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.DELETED.getCode());
        verify(cppFurtheranceTrackingRepository).fetchFurtheranceActionCode(cppFurtheranceTrackingDTO);
        verify(cppFurtheranceTrackingRepository).batchInsertRecordsInFurtheranceTracking(cppFurtheranceTrackingDTOList, userName);
    }

    @Test
    public void shouldNotAddDeleteTrackingForItemWhenExistingDeleteEntryFound() {

        CPPFurtheranceTrackingDTO cppFurtheranceTrackingDTO = buildCPPFurtheranceTrackingDTO(ItemPriceLevel.ITEM.getCode(),
                FurtheranceAction.DELETED.getCode(), CUSTOMER_ITEM_PRICE);
        List<CPPFurtheranceTrackingDTO> cppFurtheranceTrackingDTOList = new ArrayList<>();
        cppFurtheranceTrackingDTOList.add(cppFurtheranceTrackingDTO);
        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        when(furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.DELETED.getCode())).thenReturn(cppFurtheranceTrackingDTO);
        when(cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(cppFurtheranceTrackingDTO)).thenReturn(FurtheranceAction.DELETED.getCode());

        target.addTrackingForMarkupDelete(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList, userName);

        verify(cppFurtheranceTrackingRepository, never()).batchInsertRecordsInFurtheranceTracking(anyListOf(CPPFurtheranceTrackingDTO.class),
                any(String.class));
        verify(furtheranceDTOBuilder).buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.DELETED.getCode());
        verify(cppFurtheranceTrackingRepository).fetchFurtheranceActionCode(cppFurtheranceTrackingDTO);

    }

    @Test
    public void shouldAddTrackingForMarkupAdd() {

        String userName = "dummy";
        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        List<CPPFurtheranceTrackingDTO> furtheranceTrackingDTOList = new ArrayList<>();
        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = buildCPPFurtheranceTrackingDTO(ItemPriceLevel.ITEM.getCode(),
                FurtheranceAction.ADDED.getCode(), CUSTOMER_ITEM_PRICE);
        furtheranceTrackingDTOList.add(furtheranceTrackingDTO);

        when(furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.ADDED.getCode())).thenReturn(furtheranceTrackingDTO);

        target.addTrackingForMarkupAdd(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList, userName);

        verify(furtheranceDTOBuilder).buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.ADDED.getCode());
        verify(cppFurtheranceTrackingRepository).batchInsertRecordsInFurtheranceTracking(furtheranceTrackingDTOList, userName);

    }

    @Test
    public void shouldAddTrackingWithUpdatedWhenFoundExistingDeletedTracking() {

        String userName = "dummy";
        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = buildCPPFurtheranceTrackingDTO(ItemPriceLevel.ITEM.getCode(),
                FurtheranceAction.ADDED.getCode(), CUSTOMER_ITEM_PRICE);

        when(cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(furtheranceTrackingDTO)).thenReturn(FurtheranceAction.DELETED.getCode());

        when(furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.ADDED.getCode())).thenReturn(furtheranceTrackingDTO);

        target.addTrackingForMarkupAdd(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList, userName);

        verify(cppFurtheranceTrackingRepository).batchInsertRecordsInFurtheranceTracking(furtheranceTrackingDTOListCaptor.capture(), eq(userName));
        verify(cppFurtheranceTrackingRepository).deleteTrackingEntry(furtheranceTrackingDTO, FurtheranceAction.DELETED.getCode());
        verify(cppFurtheranceTrackingRepository).fetchFurtheranceActionCode(furtheranceTrackingDTO);

        verify(furtheranceDTOBuilder).buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.ADDED.getCode());

        List<CPPFurtheranceTrackingDTO> actualTrackingList = furtheranceTrackingDTOListCaptor.getValue();
        assertThat(actualTrackingList.size(), equalTo(1));
        CPPFurtheranceTrackingDTO actualTracking = actualTrackingList.get(0);
        assertThat(actualTracking.getFurtheranceActionCode(), equalTo(FurtheranceAction.UPDATED.getCode()));

    }

    @Test
    public void shouldAddTrackingForSplitCaseUpdate() {

        List<SplitCaseDO> updatedSplitcaseList = new ArrayList<>();
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setItemPriceId(itemPriceId);
        updatedSplitcaseList.add(splitCaseDO);

        List<CPPFurtheranceTrackingDTO> furtheranceWrapperList = new ArrayList<>();
        CPPFurtheranceTrackingDTO furtheranceWrapperDTO = buildCPPFurtheranceTrackingDTO(ItemPriceLevel.PRODUCT_TYPE.getCode(),
                FurtheranceAction.UPDATED.getCode(), PRC_PROF_LESSCASE_RULE);
        furtheranceWrapperList.add(furtheranceWrapperDTO);

        when(furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.PRODUCT_TYPE.getCode(), PRC_PROF_LESSCASE_RULE, FurtheranceAction.UPDATED.getCode()))
                        .thenReturn(furtheranceWrapperDTO);

        when(cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(furtheranceWrapperDTO)).thenReturn(null);

        target.addTrackingForSplitCaseUpdate(updatedSplitcaseList, userName, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode);

        verify(furtheranceDTOBuilder).buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.PRODUCT_TYPE.getCode(), PRC_PROF_LESSCASE_RULE, FurtheranceAction.UPDATED.getCode());
        verify(cppFurtheranceTrackingRepository).fetchFurtheranceActionCode(furtheranceWrapperDTO);
        verify(cppFurtheranceTrackingRepository).batchInsertRecordsInFurtheranceTracking(furtheranceWrapperList, userName);

    }

    @Test
    public void shouldNotAddTrackingForSplitCaseUpdateWhenTrackingFoundAlready() {

        List<SplitCaseDO> updatedSplitcaseList = new ArrayList<>();
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setItemPriceId(itemPriceId);
        updatedSplitcaseList.add(splitCaseDO);

        List<CPPFurtheranceTrackingDTO> furtheranceWrapperList = new ArrayList<>();
        CPPFurtheranceTrackingDTO furtheranceWrapperDTO = buildCPPFurtheranceTrackingDTO(ItemPriceLevel.PRODUCT_TYPE.getCode(),
                FurtheranceAction.UPDATED.getCode(), PRC_PROF_LESSCASE_RULE);
        furtheranceWrapperList.add(furtheranceWrapperDTO);

        when(furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.PRODUCT_TYPE.getCode(), PRC_PROF_LESSCASE_RULE, FurtheranceAction.UPDATED.getCode()))
                        .thenReturn(furtheranceWrapperDTO);

        when(cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(furtheranceWrapperDTO)).thenReturn(FurtheranceAction.UPDATED.getCode());

        target.addTrackingForSplitCaseUpdate(updatedSplitcaseList, userName, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode);

        verify(cppFurtheranceTrackingRepository, never()).batchInsertRecordsInFurtheranceTracking(anyListOf(CPPFurtheranceTrackingDTO.class),
                any(String.class));
        verify(furtheranceDTOBuilder).buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.PRODUCT_TYPE.getCode(), PRC_PROF_LESSCASE_RULE, FurtheranceAction.UPDATED.getCode());
        verify(cppFurtheranceTrackingRepository).fetchFurtheranceActionCode(furtheranceWrapperDTO);

    }

    @Test
    public void shouldAddTrackingForMarkupUpdate() {

        List<SplitCaseDO> updatedSplitcaseList = new ArrayList<>();
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setItemPriceId(itemPriceId);
        updatedSplitcaseList.add(splitCaseDO);

        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        List<CPPFurtheranceTrackingDTO> furtheranceWrapperList = new ArrayList<>();
        CPPFurtheranceTrackingDTO furtheranceWrapperDTO = buildCPPFurtheranceTrackingDTO(ItemPriceLevel.PRODUCT_TYPE.getCode(),
                FurtheranceAction.UPDATED.getCode(), CUSTOMER_ITEM_PRICE);
        furtheranceWrapperList.add(furtheranceWrapperDTO);

        when(furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.UPDATED.getCode())).thenReturn(furtheranceWrapperDTO);

        when(cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(furtheranceWrapperDTO)).thenReturn(null);

        target.addTrackingForMarkupUpdate(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList, userName,
                ItemPriceLevel.ITEM.getCode());

        verify(furtheranceDTOBuilder).buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.UPDATED.getCode());
        verify(cppFurtheranceTrackingRepository).fetchFurtheranceActionCode(furtheranceWrapperDTO);
        verify(cppFurtheranceTrackingRepository).batchInsertRecordsInFurtheranceTracking(furtheranceWrapperList, userName);

    }

    @Test
    public void shouldSkipAddTrackingForMarkupUpdateWhenActionFoundAlready() {

        List<SplitCaseDO> updatedSplitcaseList = new ArrayList<>();
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setItemPriceId(itemPriceId);
        updatedSplitcaseList.add(splitCaseDO);

        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        List<CPPFurtheranceTrackingDTO> furtheranceWrapperList = new ArrayList<>();
        CPPFurtheranceTrackingDTO furtheranceWrapperDTO = buildCPPFurtheranceTrackingDTO(ItemPriceLevel.PRODUCT_TYPE.getCode(),
                FurtheranceAction.UPDATED.getCode(), CUSTOMER_ITEM_PRICE);
        furtheranceWrapperList.add(furtheranceWrapperDTO);

        when(furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.UPDATED.getCode())).thenReturn(furtheranceWrapperDTO);

        when(cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(furtheranceWrapperDTO)).thenReturn(FurtheranceAction.UPDATED.getCode());

        target.addTrackingForMarkupUpdate(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, expireItemList, userName,
                ItemPriceLevel.ITEM.getCode());

        verify(furtheranceDTOBuilder).buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE, FurtheranceAction.UPDATED.getCode());
        verify(cppFurtheranceTrackingRepository).fetchFurtheranceActionCode(furtheranceWrapperDTO);
        verify(cppFurtheranceTrackingRepository, never()).batchInsertRecordsInFurtheranceTracking(anyListOf(CPPFurtheranceTrackingDTO.class),
                any(String.class));

    }

    private CPPFurtheranceTrackingDTO buildCPPFurtheranceTrackingDTO(int itemCode, int furtheranceAction, String changeTable) {
        CPPFurtheranceTrackingDTO furtheranceTrackingDTO = new CPPFurtheranceTrackingDTO();
        furtheranceTrackingDTO.setGfsCustomerId(gfsCustomerId);
        furtheranceTrackingDTO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        furtheranceTrackingDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceTrackingDTO.setItemPriceLevelCode(itemCode);
        furtheranceTrackingDTO.setItemPriceId(itemPriceId);
        furtheranceTrackingDTO.setChangeTableName(changeTable);
        furtheranceTrackingDTO.setFurtheranceActionCode(furtheranceAction);
        return furtheranceTrackingDTO;
    }
}
