package com.gfs.cpp.component.furtherance;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.model.markup.FutureItemDO;
import com.gfs.cpp.common.model.markup.MarkupWrapperDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.markup.FutureItemUpdater;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceMarkupSaverTest {

    @InjectMocks
    private FurtheranceMarkupSaver target;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private MarkupDOBuilder markupDOBuilder;

    @Mock
    private FurtheranceChangeTracker furtheranceChangeTracker;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private FutureItemUpdater futureItemUpdater;

    @Test
    public void shouldSaveMarkupForFurtherance() {
        MarkupWrapperDTO markupWrapperDTO = buildMarkupWrapperDTO();
        MarkupWrapperDO markupWrapperDO = buildMarkupWrapperDO();
        String userName = "test user";
        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = buildProductTypeList();
        productTypeMarkupDTOList.get(0).setMarkup("2");
        ProductTypeMarkupDTO productTypeMarkupDTO = buildProductTypeDTO();

        List<String> assignedItemList = new ArrayList<>();
        assignedItemList.add("1");

        List<ProductTypeMarkupDO> updatedProductDOList = new ArrayList<>();
        ProductTypeMarkupDO existingProductType = buildProductTypeDO(1, "1");
        updatedProductDOList.add(existingProductType);
        List<String> updateIdList = new ArrayList<>();
        updateIdList.add(String.valueOf(markupWrapperDTO.getProductMarkupList().get(0).getItemPriceId()));

        when(customerItemPriceRepository.fetchAllMarkup(markupWrapperDTO.getContractPriceProfileSeq(), markupWrapperDTO.getGfsCustomerId()))
                .thenReturn(productTypeMarkupDTOList);
        when(markupDOBuilder.buildProductTypeMarkupDO(markupWrapperDTO, productTypeMarkupDTO)).thenReturn(existingProductType);
        when(markupDOBuilder.buildItemLevelMarkupDO(markupWrapperDTO, markupWrapperDTO.getItemLevelMarkupList().get(0)))
                .thenReturn(buildProductTypeDO(Integer.parseInt(markupWrapperDTO.getItemLevelMarkupList().get(0).getItemId()),
                        markupWrapperDTO.getItemLevelMarkupList().get(0).getMarkup()));
        when(markupDOBuilder.buildSubgroupMarkupDO(markupWrapperDTO.getGfsCustomerId(), markupWrapperDTO.getGfsCustomerType(),
                markupWrapperDTO.getSubgroupMarkupList().get(0)))
                        .thenReturn(buildProductTypeDO(Integer.parseInt(markupWrapperDTO.getSubgroupMarkupList().get(0).getSubgroupId()),
                                markupWrapperDTO.getSubgroupMarkupList().get(0).getMarkup()));
        when(markupDOBuilder.buildMarkupWrapperDO(markupWrapperDTO, markupWrapperDTO.getContractPriceProfileSeq(), userName))
                .thenReturn(markupWrapperDO);
        when(cppDateUtils.oneDayPreviousCurrentDate()).thenReturn(new LocalDate(9999, 01, 01).toDate());

        target.saveMarkupForFurtherance(markupWrapperDTO, userName);

        verify(customerItemPriceRepository).fetchAllMarkup(markupWrapperDTO.getContractPriceProfileSeq(), markupWrapperDTO.getGfsCustomerId());
        verify(markupDOBuilder).buildProductTypeMarkupDO(markupWrapperDTO, productTypeMarkupDTO);
        verify(markupDOBuilder).buildItemLevelMarkupDO(markupWrapperDTO, markupWrapperDTO.getItemLevelMarkupList().get(0));
        verify(markupDOBuilder).buildSubgroupMarkupDO(markupWrapperDTO.getGfsCustomerId(), markupWrapperDTO.getGfsCustomerType(),
                markupWrapperDTO.getSubgroupMarkupList().get(0));
        verify(customerItemPriceRepository).expireItemPricing(markupWrapperDTO.getContractPriceProfileSeq(), markupWrapperDTO.getGfsCustomerId(),
                markupWrapperDTO.getGfsCustomerType(), updateIdList, ItemPriceLevel.PRODUCT_TYPE.getCode(), cppDateUtils.oneDayPreviousCurrentDate(),
                userName);
        verify(customerItemPriceRepository).saveMarkup(updatedProductDOList, userName, markupWrapperDTO.getContractPriceProfileSeq());
        verify(furtheranceChangeTracker).addTrackingForMarkupUpdate(markupWrapperDTO.getCppFurtheranceSeq(), markupWrapperDTO.getGfsCustomerId(),
                markupWrapperDTO.getGfsCustomerType(), updateIdList, userName, ItemPriceLevel.PRODUCT_TYPE.getCode());
        verify(futureItemUpdater).saveFutureItemsForFurtherance(markupWrapperDTO, markupWrapperDO.getFutureItemList(), userName);
        verify(markupDOBuilder).buildMarkupWrapperDO(markupWrapperDTO, markupWrapperDTO.getContractPriceProfileSeq(), userName);

    }

    @Test
    public void shouldSaveMarkupForFurtheranceWhenNewItemAdded() {
        MarkupWrapperDTO markupWrapperDTO = buildMarkupWrapperDTO();
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setItemId("5");
        itemLevelMarkupDTO.setMarkup("5");
        markupWrapperDTO.getItemLevelMarkupList().remove(0);
        markupWrapperDTO.getItemLevelMarkupList().add(itemLevelMarkupDTO);
        MarkupWrapperDO markupWrapperDO = buildMarkupWrapperDO();
        String userName = "test user";
        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = buildProductTypeList();
        productTypeMarkupDTOList.get(0).setMarkup("2");
        ProductTypeMarkupDTO productTypeMarkupDTO = buildProductTypeDTO();

        List<String> assignedItemList = new ArrayList<>();
        assignedItemList.add("1");

        List<ProductTypeMarkupDO> updatedProductDOList = new ArrayList<>();
        ProductTypeMarkupDO existingProductType = buildProductTypeDO(1, "1");
        updatedProductDOList.add(existingProductType);
        List<String> updateIdList = new ArrayList<>();
        updateIdList.add(String.valueOf(markupWrapperDTO.getProductMarkupList().get(0).getItemPriceId()));

        when(customerItemPriceRepository.fetchAllMarkup(markupWrapperDTO.getContractPriceProfileSeq(), markupWrapperDTO.getGfsCustomerId()))
                .thenReturn(productTypeMarkupDTOList);
        when(markupDOBuilder.buildProductTypeMarkupDO(markupWrapperDTO, productTypeMarkupDTO)).thenReturn(existingProductType);
        when(markupDOBuilder.buildItemLevelMarkupDO(markupWrapperDTO, markupWrapperDTO.getItemLevelMarkupList().get(0)))
                .thenReturn(buildProductTypeDO(Integer.parseInt(markupWrapperDTO.getItemLevelMarkupList().get(0).getItemId()),
                        markupWrapperDTO.getItemLevelMarkupList().get(0).getMarkup()));
        when(markupDOBuilder.buildSubgroupMarkupDO(markupWrapperDTO.getGfsCustomerId(), markupWrapperDTO.getGfsCustomerType(),
                markupWrapperDTO.getSubgroupMarkupList().get(0)))
                        .thenReturn(buildProductTypeDO(Integer.parseInt(markupWrapperDTO.getSubgroupMarkupList().get(0).getSubgroupId()),
                                markupWrapperDTO.getSubgroupMarkupList().get(0).getMarkup()));
        when(markupDOBuilder.buildMarkupWrapperDO(markupWrapperDTO, markupWrapperDTO.getContractPriceProfileSeq(), userName))
                .thenReturn(markupWrapperDO);

        when(cppDateUtils.oneDayPreviousCurrentDate()).thenReturn(new LocalDate(9999, 01, 01).toDate());

        target.saveMarkupForFurtherance(markupWrapperDTO, userName);

        verify(customerItemPriceRepository).fetchAllMarkup(markupWrapperDTO.getContractPriceProfileSeq(), markupWrapperDTO.getGfsCustomerId());
        verify(markupDOBuilder).buildProductTypeMarkupDO(markupWrapperDTO, productTypeMarkupDTO);
        verify(markupDOBuilder).buildItemLevelMarkupDO(markupWrapperDTO, markupWrapperDTO.getItemLevelMarkupList().get(0));
        verify(markupDOBuilder).buildSubgroupMarkupDO(markupWrapperDTO.getGfsCustomerId(), markupWrapperDTO.getGfsCustomerType(),
                markupWrapperDTO.getSubgroupMarkupList().get(0));
        verify(customerItemPriceRepository).expireItemPricing(markupWrapperDTO.getContractPriceProfileSeq(), markupWrapperDTO.getGfsCustomerId(),
                markupWrapperDTO.getGfsCustomerType(), updateIdList, ItemPriceLevel.PRODUCT_TYPE.getCode(), cppDateUtils.oneDayPreviousCurrentDate(),
                userName);
        verify(customerItemPriceRepository).saveMarkup(updatedProductDOList, userName, markupWrapperDTO.getContractPriceProfileSeq());
        verify(furtheranceChangeTracker).addTrackingForMarkupUpdate(markupWrapperDTO.getCppFurtheranceSeq(), markupWrapperDTO.getGfsCustomerId(),
                markupWrapperDTO.getGfsCustomerType(), updateIdList, userName, ItemPriceLevel.PRODUCT_TYPE.getCode());
        verify(futureItemUpdater).saveFutureItemsForFurtherance(markupWrapperDTO, markupWrapperDO.getFutureItemList(), userName);

        verify(markupDOBuilder).buildMarkupWrapperDO(markupWrapperDTO, markupWrapperDTO.getContractPriceProfileSeq(), userName);

    }

    private MarkupWrapperDTO buildMarkupWrapperDTO() {
        MarkupWrapperDTO markupWrapperDTO = new MarkupWrapperDTO();
        markupWrapperDTO.setContractPriceProfileSeq(1);
        markupWrapperDTO.setCppFurtheranceSeq(1);
        markupWrapperDTO.setGfsCustomerId("user");
        markupWrapperDTO.setGfsCustomerType(1);
        markupWrapperDTO.setItemLevelMarkupList(buildItemLevelMarkupDTO());
        markupWrapperDTO.setMarkupName("test markup");
        markupWrapperDTO.setProductMarkupList(buildProductTypeList());
        markupWrapperDTO.setSubgroupMarkupList(buildSubgroupMarkupDTOList());
        return markupWrapperDTO;
    }

    private MarkupWrapperDO buildMarkupWrapperDO() {
        MarkupWrapperDO markupWrapperDO = new MarkupWrapperDO();
        FutureItemDO futureItemDO = new FutureItemDO();
        List<FutureItemDO> futureItemList = new ArrayList<>();
        futureItemList.add(futureItemDO);
        markupWrapperDO.setContractPriceProfileSeq(1);
        markupWrapperDO.setFutureItemList(futureItemList);
        markupWrapperDO.setMarkupList(new ArrayList<ProductTypeMarkupDO>());
        markupWrapperDO.setUserName("test user");
        return markupWrapperDO;
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

    private List<ItemLevelMarkupDTO> buildItemLevelMarkupDTO() {
        List<ItemLevelMarkupDTO> itemLevelMarkupList = new ArrayList<>();
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setCustomerItemDescSeq(1);
        itemLevelMarkupDTO.setItemDesc("item desc");
        itemLevelMarkupDTO.setItemId("2");
        itemLevelMarkupDTO.setMarkup("1");
        itemLevelMarkupList.add(itemLevelMarkupDTO);
        return itemLevelMarkupList;
    }

    private List<SubgroupMarkupDTO> buildSubgroupMarkupDTOList() {
        List<SubgroupMarkupDTO> subgroupMarkupDTOList = new ArrayList<SubgroupMarkupDTO>();
        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setSubgroupId("3");
        subgroupMarkupDTO.setMarkup("3");
        subgroupMarkupDTO.setCustomerItemDescSeq(2);
        subgroupMarkupDTOList.add(subgroupMarkupDTO);
        return subgroupMarkupDTOList;
    }

    private ProductTypeMarkupDTO buildProductTypeDTO() {
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setContractPriceProfileSeq(1);
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setMarkup("1");
        return productTypeMarkupDTO;
    }

    private ProductTypeMarkupDO buildProductTypeDO(int itemPriceId, String markUp) {
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setContractPriceProfileSeq(1);
        productTypeMarkupDO.setItemPriceId(itemPriceId);
        productTypeMarkupDO.setMarkup(new BigDecimal(markUp));
        return productTypeMarkupDO;
    }

}
