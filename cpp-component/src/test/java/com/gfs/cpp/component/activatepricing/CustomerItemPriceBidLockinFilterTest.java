package com.gfs.cpp.component.activatepricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.HierarchyAncestorNodeDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.component.activatepricing.CustomerItemPriceBidLockinFilter;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class CustomerItemPriceBidLockinFilterTest {

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @InjectMocks
    private CustomerItemPriceBidLockinFilter target;

    @Captor
    private ArgumentCaptor<List<CustomerPK>> customerListCaptor;

    @Test
    public void shouldFilterMarkupsIfExistingPricingPresentForCustomerHierarchy() {

        int itemPriceId = 2;
        String customerId = "123456";
        String familyId = "12345";
        Integer customerTypeCode = 0;
        int familyTypeCode = 3;

        List<String> itemidList = new ArrayList<String>();
        itemidList.add(String.valueOf(itemPriceId));

        CustomerPK customerPK = new CustomerPK(customerId, customerTypeCode);
        CustomerPK familyCustomerPK = new CustomerPK(familyId, familyTypeCode);

        Date effectiveDate = new LocalDate(2018, 01, 05).toDate();
        Date expirationDate = new LocalDate(2019, 01, 05).toDate();

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDTO> productTypeMarkupList = Collections.singletonList(productTypeMarkupDTO);

        HierarchyAncestorNodeDTO hierarchyAncestorNodeDTO = new HierarchyAncestorNodeDTO();
        hierarchyAncestorNodeDTO.setCustomerId(customerId);
        hierarchyAncestorNodeDTO.setCustomerName("user");
        hierarchyAncestorNodeDTO.setCustomerStatusCode(1);
        hierarchyAncestorNodeDTO.setCustomerTypeCode(customerTypeCode);

        Map<CustomerPK, List<CustomerPK>> customerHierarchy = new HashMap<>();
        customerHierarchy.put(customerPK, Arrays.asList(customerPK, familyCustomerPK));

        when(customerItemPriceRepository.fetchExistingBidLockinEntriesForCustomer(customerListCaptor.capture(), eq(itemidList), eq(effectiveDate),
                eq(expirationDate))).thenReturn(productTypeMarkupList);

        List<ProductTypeMarkupDTO> actual = target.filterMarkupsHavingBidLockinsAtHierarchy(productTypeMarkupList, customerHierarchy, effectiveDate,
                expirationDate);

        assertThat(actual, equalTo(Collections.EMPTY_LIST));

        verify(customerItemPriceRepository).fetchExistingBidLockinEntriesForCustomer(customerListCaptor.capture(), eq(itemidList), eq(effectiveDate),
                eq(expirationDate));
        List<CustomerPK> actualCustomerList = customerListCaptor.getValue();
        assertThat(actualCustomerList.size(), equalTo(2));
        assertThat(actualCustomerList.contains(familyCustomerPK), equalTo(true));
        assertThat(actualCustomerList.contains(customerPK), equalTo(true));
    }

    @Test
    public void shouldFNotilterMarkupsIfExistingPricingPresentNotForSameItem() {

        int itemPriceId = 2;
        String customerId = "123456";
        Integer customerTypeCode = 0;

        CustomerPK customerPK = new CustomerPK(customerId, customerTypeCode);

        Date effectiveDate = new LocalDate(2018, 01, 05).toDate();
        Date expirationDate = new LocalDate(2019, 01, 05).toDate();

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        ProductTypeMarkupDTO existingMarkup = new ProductTypeMarkupDTO();
        existingMarkup.setGfsCustomerId(customerId);
        existingMarkup.setItemPriceId(-2);
        existingMarkup.setGfsCustomerTypeCode(customerTypeCode);

        HierarchyAncestorNodeDTO hierarchyAncestorNodeDTO = new HierarchyAncestorNodeDTO();
        hierarchyAncestorNodeDTO.setCustomerId(customerId);
        hierarchyAncestorNodeDTO.setCustomerName("user");
        hierarchyAncestorNodeDTO.setCustomerStatusCode(1);
        hierarchyAncestorNodeDTO.setCustomerTypeCode(customerTypeCode);

        Map<CustomerPK, List<CustomerPK>> customerHierarchy = new HashMap<>();
        customerHierarchy.put(customerPK, Collections.singletonList(customerPK));

        List<String> itemidList = Arrays.asList(String.valueOf(itemPriceId), "-2");

        when(customerItemPriceRepository.fetchExistingBidLockinEntriesForCustomer(customerListCaptor.capture(), eq(itemidList), eq(effectiveDate),
                eq(expirationDate))).thenReturn(Collections.singletonList(existingMarkup));

        List<ProductTypeMarkupDTO> actual = target.filterMarkupsHavingBidLockinsAtHierarchy(Arrays.asList(productTypeMarkupDTO, existingMarkup),
                customerHierarchy, effectiveDate, expirationDate);

        assertThat(actual, equalTo(Collections.singletonList(productTypeMarkupDTO)));

        verify(customerItemPriceRepository).fetchExistingBidLockinEntriesForCustomer(customerListCaptor.capture(), eq(itemidList), eq(effectiveDate),
                eq(expirationDate));
    }

    @Test
    public void shouldNotFilterWhenNoExistingPricing() {

        int itemPriceId = 2;
        String customerId = "123456";
        Integer customerTypeCode = 0;

        List<String> itemidList = new ArrayList<String>();
        itemidList.add(String.valueOf(itemPriceId));

        CustomerPK customerPK = new CustomerPK(customerId, customerTypeCode);

        Date effectiveDate = new LocalDate(2018, 01, 05).toDate();
        Date expirationDate = new LocalDate(2019, 01, 05).toDate();

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        HierarchyAncestorNodeDTO hierarchyAncestorNodeDTO = new HierarchyAncestorNodeDTO();
        hierarchyAncestorNodeDTO.setCustomerId(customerId);
        hierarchyAncestorNodeDTO.setCustomerName("user");
        hierarchyAncestorNodeDTO.setCustomerStatusCode(1);
        hierarchyAncestorNodeDTO.setCustomerTypeCode(customerTypeCode);

        Map<CustomerPK, List<CustomerPK>> customerHierarchy = new HashMap<>();
        customerHierarchy.put(customerPK, Collections.singletonList(customerPK));

        when(customerItemPriceRepository.fetchExistingBidLockinEntriesForCustomer(customerListCaptor.capture(), eq(itemidList), eq(effectiveDate),
                eq(expirationDate))).thenReturn(null);

        List<ProductTypeMarkupDTO> actual = target.filterMarkupsHavingBidLockinsAtHierarchy(Collections.singletonList(productTypeMarkupDTO),
                customerHierarchy, effectiveDate, expirationDate);

        verify(customerItemPriceRepository).fetchExistingBidLockinEntriesForCustomer(customerListCaptor.capture(), eq(itemidList), eq(effectiveDate),
                eq(expirationDate));

        assertThat(actual, equalTo(Collections.singletonList(productTypeMarkupDTO)));
    }

}
