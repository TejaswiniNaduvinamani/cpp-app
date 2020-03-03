package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.proxy.ProductTypeMarkupDTOToCustomerPKTransformer;

@RunWith(MockitoJUnitRunner.class)
public class ProductTypeMarkupDTOToCustomerPKTransformerTest {

    @InjectMocks
    private ProductTypeMarkupDTOToCustomerPKTransformer target;

    @Test
    public void shouldTransform() {

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId("1");
        productTypeMarkupDTO.setEffectiveDate(new LocalDate(2010, 01, 01).toDate());
        productTypeMarkupDTO.setExpirationDate(new LocalDate(2011, 01, 01).toDate());
        productTypeMarkupDTO.setItemPriceId(2);
        productTypeMarkupDTO.setMarkup("");
        productTypeMarkupDTO.setMarkupType(2);
        productTypeMarkupDTO.setProductType("2");
        productTypeMarkupDTO.setUnit("");
        productTypeMarkupDTO.setGfsCustomerTypeCode(12);
        productTypeMarkupDTO.setCustomerItemPriceSeq(1);
        productTypeMarkupDTO.setContractPriceProfileSeq(1);
        productTypeMarkupDTO.setPriceLockedInTypeCode(0);
        productTypeMarkupDTO.setHoldCostFirmInd(1);
        productTypeMarkupDTO.setPriceLockinReasonCode(2);
        productTypeMarkupDTO.setPriceMaintenanceSourceCode(3);

        CustomerPK actual = target.transform(productTypeMarkupDTO);

        assertThat(actual.getId(), equalTo(productTypeMarkupDTO.getGfsCustomerId()));
        assertThat(actual.getTypeCode(), equalTo(productTypeMarkupDTO.getGfsCustomerTypeCode()));

    }

}
