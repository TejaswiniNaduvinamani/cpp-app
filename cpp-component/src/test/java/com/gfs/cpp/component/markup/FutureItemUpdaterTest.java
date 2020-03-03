package com.gfs.cpp.component.markup;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.corp.customer.common.exception.CustomerServiceException;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.model.markup.FutureItemDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.furtherance.FurtheranceChangeTracker;
import com.gfs.cpp.component.markup.FutureItemUpdater;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class FutureItemUpdaterTest {

    @InjectMocks
    private FutureItemUpdater target;

    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Mock
    private CppItemMappingRepository cppItemMappingRepository;

    @Mock
    private MarkupDOBuilder markupDOBuilder;

    @Captor
    private ArgumentCaptor<List<ProductTypeMarkupDO>> markupDOListCaptor;

    @Mock
    private FurtheranceChangeTracker furtheranceChangeTracker;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldSaveFutureItems() throws ParseException, CustomerServiceException {

        String gfsCustomerId = "124";
        int contractPriceProfileSeq = 124;
        int gfsCustomerTypeCode = 31;
        String userName = "v8cq5";
        String futureItemDesc = "future item";

        List<FutureItemDO> futureItemList = buildFutureItemDOList(gfsCustomerId, contractPriceProfileSeq, gfsCustomerTypeCode, futureItemDesc);

        List<ItemLevelMarkupDTO> savedfutureItemList = new ArrayList<>();

        when(customerItemDescPriceRepository.fetchFutureItemTypeMarkups(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerTypeCode)))
                .thenReturn(savedfutureItemList);

        target.saveFutureItems(gfsCustomerId, gfsCustomerTypeCode, contractPriceProfileSeq, futureItemList, userName);

        verify(customerItemDescPriceRepository).saveFutureItems(futureItemList, userName, contractPriceProfileSeq);
        verify(customerItemDescPriceRepository).fetchFutureItemTypeMarkups(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerTypeCode));

    }

    @Test
    public void shouldUpdateFutureItemIfExisting() throws ParseException, CustomerServiceException {

        String gfsCustomerId = "124";
        int contractPriceProfileSeq = 124;
        int gfsCustomerTypeCode = 31;
        String userName = "v8cq5";
        String futureItemDesc = "future item";

        List<FutureItemDO> futureItemList = buildFutureItemDOList(gfsCustomerId, contractPriceProfileSeq, gfsCustomerTypeCode, futureItemDesc);

        List<ItemLevelMarkupDTO> savedfutureItemList = buildSavedFutureItemDTOList(futureItemDesc);

        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(100);
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();

        when(customerItemDescPriceRepository.fetchFutureItemTypeMarkups(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerTypeCode)))
                .thenReturn(savedfutureItemList);
        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList)).thenReturn(itemAssignmentDTOList);

        target.saveFutureItems(gfsCustomerId, gfsCustomerTypeCode, contractPriceProfileSeq, futureItemList, userName);

        verify(customerItemDescPriceRepository).updateFutureItems(futureItemList, userName, contractPriceProfileSeq);
        verify(customerItemDescPriceRepository).fetchFutureItemTypeMarkups(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerTypeCode));
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(customerItemDescSeqList);

    }

    @Test
    public void shouldUpdateFutureItemsAndAssignedItemsIfExisting() throws ParseException, CustomerServiceException {

        String gfsCustomerId = "124";
        int contractPriceProfileSeq = 124;
        int gfsCustomerTypeCode = 31;
        String userName = "v8cq5";
        String futureItemDesc = "future item";

        List<FutureItemDO> futureItemList = buildFutureItemDOList(gfsCustomerId, contractPriceProfileSeq, gfsCustomerTypeCode, futureItemDesc);

        List<ItemLevelMarkupDTO> savedfutureItemList = buildSavedFutureItemDTOList(futureItemDesc);

        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(100);

        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("test");
        itemAssignmentDTO.setEffectiveDate(new Date());
        itemAssignmentDTO.setExpirationDate(new Date());
        itemAssignmentDTO.setItemId("123");
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTO.setItemPriceLevelCode(1);
        itemAssignmentDTO.setCustomerItemDescSeq(100);
        itemAssignmentDTOList.add(itemAssignmentDTO);

        ProductTypeMarkupDO markupDO = new ProductTypeMarkupDO();
        ItemLevelMarkupDTO futureItemDTO = new ItemLevelMarkupDTO();

        when(customerItemDescPriceRepository.fetchFutureItemTypeMarkups(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerTypeCode)))
                .thenReturn(savedfutureItemList);
        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList)).thenReturn(itemAssignmentDTOList);
        when(markupDOBuilder.buildMarkupDO(gfsCustomerId, gfsCustomerTypeCode, futureItemDTO, itemAssignmentDTO.getItemId())).thenReturn(markupDO);
        when(customerItemDescPriceRepository.fetchFutureItemForAssignment(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, futureItemDesc))
                .thenReturn(futureItemDTO);

        target.saveFutureItems(gfsCustomerId, gfsCustomerTypeCode, contractPriceProfileSeq, futureItemList, userName);

        verify(customerItemDescPriceRepository).updateFutureItems(futureItemList, userName, contractPriceProfileSeq);
        verify(customerItemPriceRepository).updateMarkup(markupDOListCaptor.capture(), eq(userName), eq(contractPriceProfileSeq));

        List<ProductTypeMarkupDO> actualMarkupDOs = markupDOListCaptor.getValue();
        assertThat(actualMarkupDOs.size(), equalTo(1));
        assertThat(actualMarkupDOs.get(0), equalTo(markupDO));

        verify(customerItemDescPriceRepository).fetchFutureItemTypeMarkups(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerTypeCode));
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(customerItemDescSeqList);
    }

    @Test
    public void shouldSaveFutureItemsForFurtherance() {
        String gfsCustomerId = "test user";
        int contractPriceProfileSeq = 1;
        int gfsCustomerTypeCode = 2;
        String futureItemDesc = "futureItemDesc";
        String markup = "12";
        String userName = "user";
        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(buildItemLevelMarkupDTO(futureItemDesc, markup).get(0).getCustomerItemDescSeq());
        MarkupWrapperDTO markupWrapperDTO = buildMarkupWrapperDTO(gfsCustomerId, contractPriceProfileSeq, gfsCustomerTypeCode, futureItemDesc,
                markup);
        List<FutureItemDO> futureItemList = buildFutureItemDOList(gfsCustomerId, contractPriceProfileSeq, gfsCustomerTypeCode, futureItemDesc);
        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();
        productTypeMarkupDOList.add(buildProductTypeDO(123, markup));
        List<String> updatedFutureItemIdList = new ArrayList<>();
        updatedFutureItemIdList.add("123");
        FutureItemDO futureItemDO = buildFutureItemDO(gfsCustomerId, contractPriceProfileSeq, gfsCustomerTypeCode, futureItemDesc);
        List<ItemAssignmentDTO> allItemsInAConcept = buildItemAssignmentDTO(futureItemDesc);

        when(customerItemDescPriceRepository.fetchFutureItemTypeMarkups(markupWrapperDTO.getContractPriceProfileSeq(), markupWrapperDTO.getGfsCustomerId(),
                markupWrapperDTO.getGfsCustomerType())).thenReturn(buildItemLevelMarkupDTO(futureItemDesc, markup));
        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList)).thenReturn(allItemsInAConcept);
        when(customerItemDescPriceRepository.fetchFutureItemForAssignment(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, futureItemDesc))
                .thenReturn(buildItemLevelMarkupDTO(futureItemDesc, markup).get(0));

        target.saveFutureItemsForFurtherance(markupWrapperDTO, futureItemList, userName);

        verify(customerItemDescPriceRepository).fetchFutureItemTypeMarkups(markupWrapperDTO.getContractPriceProfileSeq(),
                markupWrapperDTO.getGfsCustomerId(), markupWrapperDTO.getGfsCustomerType());
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(customerItemDescSeqList);
        verify(customerItemDescPriceRepository).updateFutureItems(futureItemList, userName, markupWrapperDTO.getContractPriceProfileSeq());
        verify(furtheranceChangeTracker).addTrackingForMarkupUpdate(markupWrapperDTO.getCppFurtheranceSeq(), gfsCustomerId, gfsCustomerTypeCode,
                updatedFutureItemIdList, userName, ItemPriceLevel.ITEM.getCode());
        verify(customerItemPriceRepository).expireItemPricing(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, updatedFutureItemIdList,
                ItemPriceLevel.ITEM.getCode(), cppDateUtils.oneDayPreviousCurrentDate(), userName);
        verify(markupDOBuilder).buildProductTypeMarkupDO(gfsCustomerId, gfsCustomerTypeCode, allItemsInAConcept.get(0), futureItemDO);
    }

    @Test
    public void shouldThrowExceptionOnSaveFutureItemsForFurtheranceWhenNoExistingFutureItemFound() {
        String gfsCustomerId = "test user";
        int contractPriceProfileSeq = 1;
        int gfsCustomerTypeCode = 2;
        String futureItemDesc = "futureItemDesc";
        String itemDesc = "item desc";
        String markup = "12";
        String userName = "user";
        MarkupWrapperDTO markupWrapperDTO = buildMarkupWrapperDTO(gfsCustomerId, contractPriceProfileSeq, gfsCustomerTypeCode, futureItemDesc,
                markup);
        List<FutureItemDO> futureItemList = buildFutureItemDOList(gfsCustomerId, contractPriceProfileSeq, gfsCustomerTypeCode, futureItemDesc);

        when(customerItemDescPriceRepository.fetchFutureItemTypeMarkups(markupWrapperDTO.getContractPriceProfileSeq(), markupWrapperDTO.getGfsCustomerId(),
                markupWrapperDTO.getGfsCustomerType())).thenReturn(buildItemLevelMarkupDTO(itemDesc, markup));
        try {
            target.saveFutureItemsForFurtherance(markupWrapperDTO, futureItemList, userName);
        } catch (CPPRuntimeException e) {
            assertThat(e.getErrorCode(), equalTo(CPPExceptionType.FUTURE_ITEM_ADDED_IN_FURTHERANCE.getErrorCode()));
        }
        verify(customerItemDescPriceRepository).fetchFutureItemTypeMarkups(markupWrapperDTO.getContractPriceProfileSeq(),
                markupWrapperDTO.getGfsCustomerId(), markupWrapperDTO.getGfsCustomerType());
    }

    private MarkupWrapperDTO buildMarkupWrapperDTO(String gfsCustomerId, int contractPriceProfileSeq, int gfsCustomerTypeCode, String itemDesc,
            String markup) {
        MarkupWrapperDTO markupWrapperDTO = new MarkupWrapperDTO();
        markupWrapperDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapperDTO.setCppFurtheranceSeq(1);
        markupWrapperDTO.setGfsCustomerId(gfsCustomerId);
        markupWrapperDTO.setGfsCustomerType(gfsCustomerTypeCode);
        markupWrapperDTO.setItemLevelMarkupList(buildItemLevelMarkupDTO(itemDesc, markup));
        markupWrapperDTO.setMarkupName("test markup");
        markupWrapperDTO.setProductMarkupList(buildProductTypeList());
        markupWrapperDTO.setSubgroupMarkupList(new ArrayList<SubgroupMarkupDTO>());
        return markupWrapperDTO;
    }

    private List<ProductTypeMarkupDTO> buildProductTypeList() {
        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = new ArrayList<>();
        ProductTypeMarkupDTO productTypeMarkupDTO = buildProductTypeDTO();
        productTypeMarkupDTOList.add(productTypeMarkupDTO);
        productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setItemPriceId(2);
        productTypeMarkupDTO.setMarkup("2");
        productTypeMarkupDTOList.add(productTypeMarkupDTO);
        productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setItemPriceId(3);
        productTypeMarkupDTO.setMarkup("4");
        productTypeMarkupDTOList.add(productTypeMarkupDTO);
        return productTypeMarkupDTOList;
    }

    private List<ItemLevelMarkupDTO> buildItemLevelMarkupDTO(String itemDesc, String markup) {
        List<ItemLevelMarkupDTO> itemLevelMarkupList = new ArrayList<>();
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setCustomerItemDescSeq(1);
        itemLevelMarkupDTO.setItemDesc(itemDesc);
        itemLevelMarkupDTO.setItemId("123");
        itemLevelMarkupDTO.setMarkup(markup);
        itemLevelMarkupList.add(itemLevelMarkupDTO);
        return itemLevelMarkupList;
    }

    private ProductTypeMarkupDTO buildProductTypeDTO() {
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setContractPriceProfileSeq(1);
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setMarkup("1");
        return productTypeMarkupDTO;
    }

    private List<FutureItemDO> buildFutureItemDOList(String gfsCustomerId, int contractPriceProfileSeq, int gfsCustomerTypeCode,
            String futureItemDesc) {
        FutureItemDO futureItemDO = buildFutureItemDO(gfsCustomerId, contractPriceProfileSeq, gfsCustomerTypeCode, futureItemDesc);
        List<FutureItemDO> futureItemList = new ArrayList<>();
        futureItemList.add(futureItemDO);
        return futureItemList;
    }

    private FutureItemDO buildFutureItemDO(String gfsCustomerId, int contractPriceProfileSeq, int gfsCustomerTypeCode, String futureItemDesc) {
        FutureItemDO futureItemDO = new FutureItemDO();
        futureItemDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        futureItemDO.setCustomerTypeCode(gfsCustomerTypeCode);
        futureItemDO.setGfsCustomerId(gfsCustomerId);
        futureItemDO.setItemDesc(futureItemDesc);
        futureItemDO.setMarkup(new BigDecimal("10.43"));
        futureItemDO.setUnit("$");
        return futureItemDO;
    }

    private List<ItemLevelMarkupDTO> buildSavedFutureItemDTOList(String futureItemDesc) {
        List<ItemLevelMarkupDTO> savedfutureItemList = new ArrayList<>();
        ItemLevelMarkupDTO itemMarkup = new ItemLevelMarkupDTO();
        itemMarkup.setItemId("123");
        itemMarkup.setEffectiveDate(new LocalDate(2010, 01, 01).toDate());
        itemMarkup.setExpirationDate(new LocalDate(2011, 01, 01).toDate());
        itemMarkup.setItemDesc(futureItemDesc);
        itemMarkup.setMarkup("test");
        itemMarkup.setMarkupType(2);
        itemMarkup.setUnit("$");
        itemMarkup.setCustomerItemDescSeq(100);
        savedfutureItemList.add(itemMarkup);
        return savedfutureItemList;
    }

    private List<ItemAssignmentDTO> buildItemAssignmentDTO(String itemDescription) {
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setCustomerItemDescSeq(1);
        itemAssignmentDTO.setItemDescription(itemDescription);
        itemAssignmentDTO.setItemId("123");
        itemAssignmentDTOList.add(itemAssignmentDTO);
        return itemAssignmentDTOList;
    }

    private ProductTypeMarkupDO buildProductTypeDO(int itemPriceId, String markUp) {
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setContractPriceProfileSeq(1);
        productTypeMarkupDO.setItemPriceId(itemPriceId);
        productTypeMarkupDO.setMarkup(new BigDecimal(markUp));
        return productTypeMarkupDO;
    }

}
