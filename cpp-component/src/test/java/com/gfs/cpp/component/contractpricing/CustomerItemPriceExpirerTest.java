package com.gfs.cpp.component.contractpricing;

import static org.mockito.Mockito.verify;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.component.contractpricing.CustomerItemPriceExpirer;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class CustomerItemPriceExpirerTest {

    @InjectMocks
    private CustomerItemPriceExpirer target;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Test
    public void shouldExpireCustomerItemPrice() throws Exception {

        int contractPriceProfileSeq = -3001;
        Date expirationDate = new LocalDate().toDate();
        String updatedUserId = "update user";

        target.expireNonCMGCustomerItemPrice(contractPriceProfileSeq, expirationDate, updatedUserId);

        verify(customerItemPriceRepository).expireNonCmgPriceForContract(contractPriceProfileSeq, expirationDate, updatedUserId);

    }

    @Test
    public void shouldExpireItemPriceForRealCustomer() throws Exception {

        Date expirationDate = new LocalDate().toDate();
        String updatedUserId = "update user";
        Date newPricingEffectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date newPricingExpiryDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        List<ProductTypeMarkupDO> productTypeMarkupDOList = buildProductTypeMarkupDOList();

        target.expireItemPricingForRealCustomer(expirationDate, updatedUserId, productTypeMarkupDOList, newPricingEffectiveDate,
                newPricingExpiryDate);

        verify(customerItemPriceRepository).expireItemPricingForRealCustomer(expirationDate, updatedUserId, productTypeMarkupDOList, newPricingEffectiveDate,
                newPricingExpiryDate);
    }

    private List<ProductTypeMarkupDO> buildProductTypeMarkupDOList() {
        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setItemPriceId(2);
        productTypeMarkupDO.setGfsCustomerId("1234");
        productTypeMarkupDO.setCustomerTypeCode(3);
        productTypeMarkupDO.setContractPriceProfileSeq(1);
        productTypeMarkupDO.setProductType("1");
        productTypeMarkupDOList.add(productTypeMarkupDO);
        return productTypeMarkupDOList;
    }

}
