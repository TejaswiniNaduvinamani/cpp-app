package com.gfs.cpp.component.markup.builder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.component.markup.ItemLevelMarkupExtractor;
import com.gfs.cpp.component.markup.ProductTypeMarkupExtractor;
import com.gfs.cpp.component.markup.SubgroupMarkupExtractor;
import com.gfs.cpp.component.markup.builder.MarkupGridBuilder;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class MarkupGridBuilderTest {

    private static final String GFS_CUSTOMER_ID = "1";
    private static final Date pricingEffectiveDate = new LocalDate(2018, 01, 01).toDate();
    private static final Date pricingExpirationDate = new LocalDate(9999, 01, 01).toDate();

    @InjectMocks
    private MarkupGridBuilder target;

    @Mock
    private ProductTypeMarkupExtractor productTypeMarkupExtractor;

    @Mock
    private ItemLevelMarkupExtractor itemLevelMarkupExtractor;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    @Mock
    private SubgroupMarkupExtractor subgroupMarkupExtractor;;

    @Test
    public void shouldBuildMarkupGridForItemLevelMarkup() {

        List<ProductTypeMarkupDTO> allMarkupsForCmg = new ArrayList<>();
        Map<String, ItemInformationDTO> allItemsByItemId = new HashMap<>();
        int contractPriceProfileSeq = 1;
        String itemNo = "1";
        String exceptionName = "test";

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        allMarkupsForCmg.add(productTypeMarkupDTO);

        List<String> allItemLevelItemids = new ArrayList<>();
        allItemLevelItemids.add(itemNo);

        List<ItemLevelMarkupDTO> itemLevelMarkupList = new ArrayList<>();
        ItemLevelMarkupDTO itemLevelMarkup = new ItemLevelMarkupDTO();
        itemLevelMarkup.setItemDesc("Test");
        itemLevelMarkup.setItemId("1");
        itemLevelMarkupList.add(itemLevelMarkup);

        when(productTypeMarkupExtractor.extractProductTypeMarkup(allMarkupsForCmg)).thenReturn(Collections.<ProductTypeMarkupDTO> emptyList());
        when(itemLevelMarkupExtractor.extractItemTypeMarkups(allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq, GFS_CUSTOMER_ID,
                pricingEffectiveDate, pricingExpirationDate)).thenReturn(itemLevelMarkupList);
        when(subgroupMarkupExtractor.extractSubgroupMarkups(allMarkupsForCmg, pricingEffectiveDate, pricingExpirationDate))
                .thenReturn(Collections.<SubgroupMarkupDTO> emptyList());
        when(customerServiceProxy.fetchGroupName(GFS_CUSTOMER_ID, 31)).thenReturn(exceptionName);

        MarkupGridDTO result = target.buildMarkupGrid(GFS_CUSTOMER_ID, allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq,
                pricingEffectiveDate, pricingExpirationDate);

        assertThat(result.getGfsCustomerId(), equalTo(GFS_CUSTOMER_ID));
        assertThat(result.getMarkupName(), equalTo(exceptionName));
        assertThat(result.getProductTypeMarkups(), equalTo(Collections.<ProductTypeMarkupDTO> emptyList()));
        assertThat(result.getItemMarkups(), equalTo(itemLevelMarkupList));
        assertThat(result.getSubgroupMarkups(), equalTo(Collections.<SubgroupMarkupDTO> emptyList()));

        verify(productTypeMarkupExtractor).extractProductTypeMarkup(allMarkupsForCmg);
        verify(itemLevelMarkupExtractor).extractItemTypeMarkups(allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq, GFS_CUSTOMER_ID,
                pricingEffectiveDate, pricingExpirationDate);
        verify(subgroupMarkupExtractor).extractSubgroupMarkups(allMarkupsForCmg, pricingEffectiveDate, pricingExpirationDate);
        verify(customerServiceProxy).fetchGroupName(GFS_CUSTOMER_ID, 31);
    }

    @Test
    public void shouldBuildMarkupGridForSubgroupMarkup() {

        List<ProductTypeMarkupDTO> allMarkupsForCmg = new ArrayList<>();
        Map<String, ItemInformationDTO> allItemsByItemId = new HashMap<>();
        int contractPriceProfileSeq = 1;
        String subgroupId = "1";
        String exceptionName = "test";

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        allMarkupsForCmg.add(productTypeMarkupDTO);

        List<SubgroupMarkupDTO> subgroupMarkupList = new ArrayList<>();
        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setSubgroupId(subgroupId);
        subgroupMarkupDTO.setSubgroupDesc("Test");
        subgroupMarkupList.add(subgroupMarkupDTO);

        when(productTypeMarkupExtractor.extractProductTypeMarkup(allMarkupsForCmg)).thenReturn(Collections.<ProductTypeMarkupDTO> emptyList());
        when(itemLevelMarkupExtractor.extractItemTypeMarkups(allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq, GFS_CUSTOMER_ID,
                pricingEffectiveDate, pricingExpirationDate)).thenReturn(Collections.<ItemLevelMarkupDTO> emptyList());
        when(subgroupMarkupExtractor.extractSubgroupMarkups(allMarkupsForCmg, pricingEffectiveDate, pricingExpirationDate))
                .thenReturn(subgroupMarkupList);
        when(customerServiceProxy.fetchGroupName(GFS_CUSTOMER_ID, 31)).thenReturn(exceptionName);

        MarkupGridDTO result = target.buildMarkupGrid(GFS_CUSTOMER_ID, allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq,
                pricingEffectiveDate, pricingExpirationDate);

        assertThat(result.getGfsCustomerId(), equalTo(GFS_CUSTOMER_ID));
        assertThat(result.getMarkupName(), equalTo(exceptionName));
        assertThat(result.getProductTypeMarkups(), equalTo(Collections.<ProductTypeMarkupDTO> emptyList()));
        assertThat(result.getItemMarkups(), equalTo(Collections.<ItemLevelMarkupDTO> emptyList()));
        assertThat(result.getSubgroupMarkups(), equalTo(subgroupMarkupList));

        verify(productTypeMarkupExtractor).extractProductTypeMarkup(allMarkupsForCmg);
        verify(itemLevelMarkupExtractor).extractItemTypeMarkups(allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq, GFS_CUSTOMER_ID,
                pricingEffectiveDate, pricingExpirationDate);
        verify(subgroupMarkupExtractor).extractSubgroupMarkups(allMarkupsForCmg, pricingEffectiveDate, pricingExpirationDate);
        verify(customerServiceProxy).fetchGroupName(GFS_CUSTOMER_ID, 31);
    }

    @Test
    public void shouldBuildMarkupGridForProductTypeMarkup() {

        List<ProductTypeMarkupDTO> allMarkupsForCmg = new ArrayList<>();
        Map<String, ItemInformationDTO> allItemsByItemId = new HashMap<>();
        int contractPriceProfileSeq = 1;
        int itemPriceId = 1;
        String exceptionName = "test";

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setProductType("2");
        allMarkupsForCmg.add(productTypeMarkupDTO);

        when(productTypeMarkupExtractor.extractProductTypeMarkup(allMarkupsForCmg)).thenReturn(allMarkupsForCmg);
        when(itemLevelMarkupExtractor.extractItemTypeMarkups(allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq, GFS_CUSTOMER_ID,
                pricingEffectiveDate, pricingExpirationDate)).thenReturn(Collections.<ItemLevelMarkupDTO> emptyList());
        when(subgroupMarkupExtractor.extractSubgroupMarkups(allMarkupsForCmg, pricingEffectiveDate, pricingExpirationDate))
                .thenReturn(Collections.<SubgroupMarkupDTO> emptyList());
        when(customerServiceProxy.fetchGroupName(GFS_CUSTOMER_ID, 31)).thenReturn(exceptionName);

        MarkupGridDTO result = target.buildMarkupGrid(GFS_CUSTOMER_ID, allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq,
                pricingEffectiveDate, pricingExpirationDate);

        assertThat(result.getGfsCustomerId(), equalTo(GFS_CUSTOMER_ID));
        assertThat(result.getMarkupName(), equalTo(exceptionName));
        assertThat(result.getProductTypeMarkups(), equalTo(allMarkupsForCmg));
        assertThat(result.getItemMarkups(), equalTo(Collections.<ItemLevelMarkupDTO> emptyList()));
        assertThat(result.getSubgroupMarkups(), equalTo(Collections.<SubgroupMarkupDTO> emptyList()));

        verify(productTypeMarkupExtractor).extractProductTypeMarkup(allMarkupsForCmg);
        verify(itemLevelMarkupExtractor).extractItemTypeMarkups(allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq, GFS_CUSTOMER_ID,
                pricingEffectiveDate, pricingExpirationDate);
        verify(subgroupMarkupExtractor).extractSubgroupMarkups(allMarkupsForCmg, pricingEffectiveDate, pricingExpirationDate);
        verify(customerServiceProxy).fetchGroupName(GFS_CUSTOMER_ID, 31);
    }
}
