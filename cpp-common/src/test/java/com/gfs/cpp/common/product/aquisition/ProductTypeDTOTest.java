package com.gfs.cpp.common.product.aquisition;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.product.aquisition.ProductTypeDTO;

@RunWith(MockitoJUnitRunner.class)
public class ProductTypeDTOTest {
    
    @InjectMocks
    private ProductTypeDTO productTypeDTO;
    
    @Test
    public void testMethods() {
        productTypeDTO.setLanguageTypeCode("");
        productTypeDTO.setOfferingCategoryAbbreviation("");
        productTypeDTO.setOfferingCategoryDescription("");
        productTypeDTO.setOfferingCategoryDisplaySequence(1);
        productTypeDTO.setOfferingCategoryId(1);
        productTypeDTO.setOfferingTypeId(1);
        
        final ProductTypeDTO actual = SerializationUtils.clone(productTypeDTO);
        assertThat(productTypeDTO.equals(actual), is(true));
        assertThat(productTypeDTO.hashCode(), is(actual.hashCode()));
        assertThat(productTypeDTO.toString()!=null, is(true));
        
        assertThat(actual.getLanguageTypeCode(), is(productTypeDTO.getLanguageTypeCode()));
        assertThat(actual.getOfferingCategoryAbbreviation(), is(productTypeDTO.getOfferingCategoryAbbreviation()));
        assertThat(actual.getOfferingCategoryDescription(), is(productTypeDTO.getOfferingCategoryDescription()));
        assertThat(actual.getOfferingCategoryDisplaySequence(), is(productTypeDTO.getOfferingCategoryDisplaySequence()));
        assertThat(actual.getOfferingCategoryId(), is(productTypeDTO.getOfferingTypeId()));
    }
}
