package com.gfs.cpp.component.markup;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.markup.FutureItemDO;
import com.gfs.cpp.common.model.markup.MarkupWrapperDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class MarkupSaverTest {

    private static final String GFS_CUSTOMER_ID = "1";
    private static final String userName = "vc71u";

    @InjectMocks
    private MarkupSaver target;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private MarkupDOBuilder markupDOBuilder;

    @Mock
    private FutureItemUpdater futureItemUpdater;

    @Test
    public void shouldUpdateMarkups() {

        int contractPriceProfileSeq = 1;
        List<ProductTypeMarkupDTO> savedMarkupList = new ArrayList<>();
        ProductTypeMarkupDTO savedProductTypeMarkupDTO = new ProductTypeMarkupDTO();
        savedProductTypeMarkupDTO.setItemPriceId(12);
        savedMarkupList.add(savedProductTypeMarkupDTO);

        List<ProductTypeMarkupDO> updateMarkupDOList = new ArrayList<>();
        ProductTypeMarkupDO updateProductTypeMarkupDO = new ProductTypeMarkupDO();
        updateProductTypeMarkupDO.setItemPriceId(12);
        updateMarkupDOList.add(updateProductTypeMarkupDO);

        List<ProductTypeMarkupDO> newMarkupDOList = new ArrayList<>();
        ProductTypeMarkupDO savedProductTypeMarkupDO = new ProductTypeMarkupDO();
        savedProductTypeMarkupDO.setItemPriceId(11);
        newMarkupDOList.add(savedProductTypeMarkupDO);
        newMarkupDOList.add(updateProductTypeMarkupDO);

        List<ProductTypeMarkupDO> savedMarkupDOList = new ArrayList<>();
        savedMarkupDOList.add(savedProductTypeMarkupDO);

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setGfsCustomerId(GFS_CUSTOMER_ID);
        markupWrapper.setGfsCustomerType(31);
        markupWrapper.setProductMarkupList(savedMarkupList);
        markupWrapper.setItemLevelMarkupList(new ArrayList<ItemLevelMarkupDTO>());
        markupWrapper.setContractPriceProfileSeq(contractPriceProfileSeq);

        FutureItemDO futureItemDO = new FutureItemDO();
        futureItemDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        futureItemDO.setCustomerTypeCode(31);
        futureItemDO.setGfsCustomerId(GFS_CUSTOMER_ID);
        futureItemDO.setItemDesc("test1");
        futureItemDO.setMarkup(new BigDecimal("10.43"));
        futureItemDO.setUnit("$");
        List<FutureItemDO> futureItemList = new ArrayList<>();
        futureItemList.add(futureItemDO);

        MarkupWrapperDO markupWrapperDO = new MarkupWrapperDO();
        markupWrapperDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapperDO.setMarkupList(newMarkupDOList);
        markupWrapperDO.setUserName(userName);
        markupWrapperDO.setFutureItemList(futureItemList);

        when(customerItemPriceRepository.fetchAllMarkup(contractPriceProfileSeq, GFS_CUSTOMER_ID)).thenReturn(savedMarkupList);
        when(markupDOBuilder.buildMarkupWrapperDO(markupWrapper, contractPriceProfileSeq, userName)).thenReturn(markupWrapperDO);

        target.saveMarkups(markupWrapper, contractPriceProfileSeq, userName);

        verify(customerItemPriceRepository).fetchAllMarkup(contractPriceProfileSeq, GFS_CUSTOMER_ID);
        verify(futureItemUpdater).saveFutureItems(GFS_CUSTOMER_ID, markupWrapper.getGfsCustomerType(), contractPriceProfileSeq, futureItemList,
                userName);
        verify(customerItemPriceRepository).updateMarkup(updateMarkupDOList, userName, contractPriceProfileSeq);
        verify(customerItemPriceRepository).saveMarkup(savedMarkupDOList, userName, contractPriceProfileSeq);
    }

    @Test
    public void shouldSaveMarkups() {

        int contractPriceProfileSeq = 1;
        int customerTypeCode = 31;

        List<ProductTypeMarkupDTO> markupList = new ArrayList<>();
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setItemPriceId(12);
        markupList.add(productTypeMarkupDTO);

        List<ProductTypeMarkupDO> markupDOList = new ArrayList<>();
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setItemPriceId(1);
        markupDOList.add(productTypeMarkupDO);

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setGfsCustomerId(GFS_CUSTOMER_ID);
        markupWrapper.setGfsCustomerType(31);
        markupWrapper.setProductMarkupList(markupList);
        markupWrapper.setItemLevelMarkupList(new ArrayList<ItemLevelMarkupDTO>());
        markupWrapper.setContractPriceProfileSeq(contractPriceProfileSeq);

        FutureItemDO futureItemDO = new FutureItemDO();
        futureItemDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        futureItemDO.setCustomerTypeCode(customerTypeCode);
        futureItemDO.setGfsCustomerId(GFS_CUSTOMER_ID);
        futureItemDO.setItemDesc("test1");
        futureItemDO.setMarkup(new BigDecimal("10.43"));
        futureItemDO.setUnit("$");
        List<FutureItemDO> futureItemList = new ArrayList<>();
        futureItemList.add(futureItemDO);

        MarkupWrapperDO markupWrapperDO = new MarkupWrapperDO();
        markupWrapperDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapperDO.setMarkupList(markupDOList);
        markupWrapperDO.setUserName(userName);
        markupWrapperDO.setFutureItemList(futureItemList);

        when(markupDOBuilder.buildMarkupWrapperDO(markupWrapper, contractPriceProfileSeq, userName)).thenReturn(markupWrapperDO);
        when(customerItemPriceRepository.fetchAllMarkup(contractPriceProfileSeq, GFS_CUSTOMER_ID)).thenReturn(markupList);

        target.saveMarkups(markupWrapper, contractPriceProfileSeq, userName);

        verify(customerItemPriceRepository).fetchAllMarkup(contractPriceProfileSeq, GFS_CUSTOMER_ID);
        verify(futureItemUpdater).saveFutureItems(GFS_CUSTOMER_ID, markupWrapper.getGfsCustomerType(), contractPriceProfileSeq, futureItemList,
                userName);
        verify(customerItemPriceRepository).saveMarkup(markupDOList, userName, contractPriceProfileSeq);
        verify(markupDOBuilder).buildMarkupWrapperDO(markupWrapper, contractPriceProfileSeq, userName);
    }

}
