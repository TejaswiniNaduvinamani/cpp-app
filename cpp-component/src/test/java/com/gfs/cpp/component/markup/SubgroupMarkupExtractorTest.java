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

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.component.markup.SubgroupMarkupExtractor;
import com.gfs.cpp.component.markup.builder.MarkupDTOBuilder;
import com.gfs.cpp.proxy.ItemConfigurationServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class SubgroupMarkupExtractorTest {

    private static final String GFS_CUSTOMER_ID = "1";
    private static final Date pricingEffectiveDate = new LocalDate(2018, 01, 01).toDate();
    private static final Date pricingExpirationDate = new LocalDate(9999, 01, 01).toDate();

    @InjectMocks
    private SubgroupMarkupExtractor target;

    @Mock
    private MarkupDTOBuilder markupDTOBuilder;

    @Mock
    private ItemConfigurationServiceProxy itemConfigurationServiceProxy;;

    @Test
    public void shouldExtractSubgroupMarkups() {
        int contractPriceProfileSeq = 1;
        String subgroupDesc = "Test";
        String subgroupId = "12";
        String markup = "1.00";
        String unit = "$";
        int markupType = 1;

        List<ProductTypeMarkupDTO> allMarkupsForCmg = new ArrayList<>();
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        productTypeMarkupDTO.setExpirationDate(pricingExpirationDate);
        productTypeMarkupDTO.setGfsCustomerId(GFS_CUSTOMER_ID);
        productTypeMarkupDTO.setItemPriceId(12);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setUnit(unit);
        productTypeMarkupDTO.setMarkupType(markupType);
        productTypeMarkupDTO.setMarkup(markup);
        allMarkupsForCmg.add(productTypeMarkupDTO);

        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setSubgroupId(subgroupId);
        subgroupMarkupDTO.setSubgroupDesc(subgroupDesc);
        subgroupMarkupDTO.setMarkup(markup);
        subgroupMarkupDTO.setMarkupType(markupType);
        subgroupMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        subgroupMarkupDTO.setExpirationDate(pricingExpirationDate);
        subgroupMarkupDTO.setUnit(unit);

        Map<String, String> subgroupDescMap = new HashMap<>();
        subgroupDescMap.put(subgroupId, subgroupDesc);

        when(markupDTOBuilder.buildSubgroupMarkupDTO(productTypeMarkupDTO, subgroupDesc, pricingEffectiveDate, pricingExpirationDate))
                .thenReturn(subgroupMarkupDTO);
        when(itemConfigurationServiceProxy.getAllSubCategories(CPPConstants.LANGUAGE_CODE)).thenReturn(subgroupDescMap);

        List<SubgroupMarkupDTO> result = target.extractSubgroupMarkups(allMarkupsForCmg, pricingEffectiveDate, pricingExpirationDate);

        assertThat(result.get(0).getEffectiveDate(), equalTo(pricingEffectiveDate));
        assertThat(result.get(0).getExpirationDate(), equalTo(pricingExpirationDate));
        assertThat(result.get(0).getSubgroupDesc(), equalTo(subgroupDesc));
        assertThat(result.get(0).getSubgroupId(), equalTo(subgroupId));
        assertThat(result.get(0).getMarkup(), equalTo(markup));
        assertThat(result.get(0).getMarkupType(), equalTo(markupType));

        verify(markupDTOBuilder).buildSubgroupMarkupDTO(productTypeMarkupDTO, subgroupDesc, pricingEffectiveDate, pricingExpirationDate);
        verify(itemConfigurationServiceProxy).getAllSubCategories(CPPConstants.LANGUAGE_CODE);
    }

    @Test
    public void shouldReturnEmptyListWhenProductTypeIsDifferent() {
        String markup = "1.00";
        String unit = "$";
        int markupType = 1;

        List<ProductTypeMarkupDTO> allMarkupsForCmg = new ArrayList<>();
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setItemPriceId(12);
        productTypeMarkupDTO.setProductType("0");
        productTypeMarkupDTO.setUnit(unit);
        productTypeMarkupDTO.setMarkupType(markupType);
        productTypeMarkupDTO.setMarkup(markup);
        allMarkupsForCmg.add(productTypeMarkupDTO);

        List<SubgroupMarkupDTO> result = target.extractSubgroupMarkups(allMarkupsForCmg, pricingEffectiveDate, pricingExpirationDate);

        assertThat(result.size(), equalTo(0));

    }

}
