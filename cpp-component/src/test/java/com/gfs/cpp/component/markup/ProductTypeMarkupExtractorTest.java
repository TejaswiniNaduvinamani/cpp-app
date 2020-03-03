package com.gfs.cpp.component.markup;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.component.markup.ProductTypeMarkupExtractor;
import com.gfs.cpp.component.markup.builder.MarkupDTOBuilder;
import com.gfs.cpp.proxy.ItemServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class ProductTypeMarkupExtractorTest {

    @InjectMocks
    private ProductTypeMarkupExtractor target;

    @Mock
    private ItemServiceProxy itemServiceProxy;

    @Mock
    private MarkupDTOBuilder markupDTOBuilder;

    @Test
    public void shouldExtractProductTypeMarkup() {
        List<ProductTypeMarkupDTO> allMarkupsForCmg = new ArrayList<>();
        String markup = "1.00";
        Map<Integer, String> productTypeMap = new HashMap<>();
        productTypeMap.put(1, "Test one");
        productTypeMap.put(2, "Test two");

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setProductType("2");
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setMarkup("1");

        ProductTypeMarkupDTO secondProductTypeMarkupDTO = new ProductTypeMarkupDTO();
        secondProductTypeMarkupDTO.setProductType("2");
        secondProductTypeMarkupDTO.setItemPriceId(2);
        secondProductTypeMarkupDTO.setMarkup("1");

        ProductTypeMarkupDTO itemProductTypeMarkupDTO = new ProductTypeMarkupDTO();
        itemProductTypeMarkupDTO.setProductType("0");

        ProductTypeMarkupDTO subGroupProductTypeMarkupDTO = new ProductTypeMarkupDTO();
        subGroupProductTypeMarkupDTO.setProductType("1");

        allMarkupsForCmg.add(productTypeMarkupDTO);
        allMarkupsForCmg.add(secondProductTypeMarkupDTO);
        allMarkupsForCmg.add(subGroupProductTypeMarkupDTO);
        allMarkupsForCmg.add(itemProductTypeMarkupDTO);

        when(itemServiceProxy.getAllProductTypesById()).thenReturn(productTypeMap);
        when(markupDTOBuilder.formatAmount("1")).thenReturn(markup);

        List<ProductTypeMarkupDTO> result = target.extractProductTypeMarkup(allMarkupsForCmg);

        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0).getItemPriceId(), equalTo(1));
        assertThat(result.get(0).getProductType(), equalTo("Test one"));
        assertThat(result.get(1).getItemPriceId(), equalTo(2));
        assertThat(result.get(1).getProductType(), equalTo("Test two"));

        verify(itemServiceProxy).getAllProductTypesById();
        verify(markupDTOBuilder, atLeastOnce()).formatAmount("1");

    }

}
