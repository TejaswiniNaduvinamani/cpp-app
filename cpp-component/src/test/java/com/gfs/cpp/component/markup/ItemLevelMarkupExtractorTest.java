package com.gfs.cpp.component.markup;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.component.markup.ItemLevelMarkupExtractor;
import com.gfs.cpp.component.markup.builder.MarkupDTOBuilder;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class ItemLevelMarkupExtractorTest {

    private static final String GFS_CUSTOMER_ID = "1";
    private static final Date pricingEffectiveDate = new LocalDate(2018, 01, 01).toDate();
    private static final Date pricingExpirationDate = new LocalDate(9999, 01, 01).toDate();

    @InjectMocks
    private ItemLevelMarkupExtractor target;

    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Mock
    private CppItemMappingRepository cppItemMappingRepository;

    @Mock
    private MarkupDTOBuilder markupDTOBuilder;

    @Test
    public void shouldExtractItemTypeMarkups() {

        int contractPriceProfileSeq = 1;
        String itemDesc = "Test";
        String itemNo = "12";
        int stockingCode = 0;
        String markup = "1";
        String unit = "$";
        int markupType = 1;

        List<ProductTypeMarkupDTO> allMarkupsForCmg = new ArrayList<>();
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        productTypeMarkupDTO.setExpirationDate(pricingExpirationDate);
        productTypeMarkupDTO.setGfsCustomerId(GFS_CUSTOMER_ID);
        productTypeMarkupDTO.setItemPriceId(12);
        productTypeMarkupDTO.setProductType("0");
        productTypeMarkupDTO.setUnit("$");

        allMarkupsForCmg.add(productTypeMarkupDTO);

        Map<String, ItemInformationDTO> allItemsByItemId = new HashMap<>();
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setIsActive(true);
        itemInformationDTO.setItemDescription(itemDesc);
        itemInformationDTO.setItemNo(itemNo);
        itemInformationDTO.setStockingCode("0");
        allItemsByItemId.put(itemNo, itemInformationDTO);

        List<ItemLevelMarkupDTO> futureItems = new ArrayList<>();
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setCustomerItemDescSeq(1);
        itemLevelMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        itemLevelMarkupDTO.setExpirationDate(pricingExpirationDate);
        itemLevelMarkupDTO.setItemDesc(itemDesc);
        itemLevelMarkupDTO.setItemId(itemNo);
        itemLevelMarkupDTO.setStockingCode(stockingCode);
        itemLevelMarkupDTO.setMarkupType(markupType);
        itemLevelMarkupDTO.setUnit(unit);
        itemLevelMarkupDTO.setMarkup(markup);
        futureItems.add(itemLevelMarkupDTO);

        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(itemLevelMarkupDTO.getCustomerItemDescSeq());

        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemId("10");
        itemAssignmentDTOList.add(itemAssignmentDTO);

        when(customerItemDescPriceRepository.fetchFutureItemTypeMarkups(contractPriceProfileSeq, GFS_CUSTOMER_ID, 31)).thenReturn(futureItems);
        when(markupDTOBuilder.buildItemLevelMarkupDTO(itemLevelMarkupDTO, StringUtils.EMPTY, true, pricingEffectiveDate, pricingExpirationDate))
                .thenReturn(itemLevelMarkupDTO);
        when(markupDTOBuilder.buildItemLevelMarkupDTO(itemInformationDTO, true, productTypeMarkupDTO)).thenReturn(itemLevelMarkupDTO);
        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList)).thenReturn(itemAssignmentDTOList);

        List<ItemLevelMarkupDTO> result = target.extractItemTypeMarkups(allMarkupsForCmg, allItemsByItemId, contractPriceProfileSeq, GFS_CUSTOMER_ID,
                pricingEffectiveDate, pricingExpirationDate);

        assertThat(result.get(0).getEffectiveDate(), equalTo(pricingEffectiveDate));
        assertThat(result.get(0).getExpirationDate(), equalTo(pricingExpirationDate));
        assertThat(result.get(0).getCustomerItemDescSeq(), equalTo(1));
        assertThat(result.get(0).getItemDesc(), equalTo(itemDesc));
        assertThat(result.get(0).getMarkup(), equalTo(markup));
        assertThat(result.get(0).getUnit(), equalTo(unit));
        assertThat(result.get(0).getStockingCode(), equalTo(stockingCode));

        verify(customerItemDescPriceRepository).fetchFutureItemTypeMarkups(contractPriceProfileSeq, GFS_CUSTOMER_ID, 31);
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(customerItemDescSeqList);

    }

}
