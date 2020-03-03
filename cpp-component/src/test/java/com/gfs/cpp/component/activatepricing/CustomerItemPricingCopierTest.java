package com.gfs.cpp.component.activatepricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.CustomerItemPriceBidLockinFilter;
import com.gfs.cpp.component.activatepricing.CustomerItemPricingCopier;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.proxy.CustomerHierarchyResolver;
import com.gfs.cpp.proxy.MemberHierarchyQueryProxy;
import com.gfs.cpp.proxy.ProductTypeMarkupDTOToCustomerPKTransformer;

@RunWith(MockitoJUnitRunner.class)
public class CustomerItemPricingCopierTest {

    @InjectMocks
    private CustomerItemPricingCopier target;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private CustomerItemPriceBidLockinFilter customerItemPriceBidLockinFilter;

    @Mock
    private MemberHierarchyQueryProxy memberHierarchyQueryProxy;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private CustomerHierarchyResolver customerHierarchyResolver;

    @Mock
    private ProductTypeMarkupDTOToCustomerPKTransformer productTypeMarkupDTOToCustomerPKTransformer;

    @Mock
    private MarkupDOBuilder markupDOBuilder;

    @Captor
    private ArgumentCaptor<List<ProductTypeMarkupDTO>> markupDTOListWithNonBidPricing;

    private List<ProductTypeMarkupDTO> buildProductTypeMarkupList() {
        List<ProductTypeMarkupDTO> productTypeMarkupList = new ArrayList<>();
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId("1");
        productTypeMarkupDTO.setItemPriceId(2);
        productTypeMarkupDTO.setProductType("2");
        productTypeMarkupDTO.setGfsCustomerTypeCode(12);
        productTypeMarkupDTO.setCustomerItemPriceSeq(1);
        productTypeMarkupDTO.setContractPriceProfileSeq(1);
        productTypeMarkupList.add(productTypeMarkupDTO);

        productTypeMarkupList.addAll(buildItemTypeMarkupList());

        return productTypeMarkupList;
    }

    private List<ProductTypeMarkupDTO> buildItemTypeMarkupList() {
        List<ProductTypeMarkupDTO> productTypeMarkupList = new ArrayList<>();
        ProductTypeMarkupDTO itemTypeMarkupDTO = new ProductTypeMarkupDTO();
        itemTypeMarkupDTO.setGfsCustomerId("1");
        itemTypeMarkupDTO.setItemPriceId(3);
        itemTypeMarkupDTO.setProductType("0");
        itemTypeMarkupDTO.setGfsCustomerTypeCode(3);
        itemTypeMarkupDTO.setCustomerItemPriceSeq(1);
        itemTypeMarkupDTO.setContractPriceProfileSeq(2);
        productTypeMarkupList.add(itemTypeMarkupDTO);
        return productTypeMarkupList;
    }

    private List<ProductTypeMarkupDO> buildProductTypeMarkupDOList() {
        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setItemPriceId(2);
        productTypeMarkupDO.setGfsCustomerId("1234");
        productTypeMarkupDO.setCustomerTypeCode(12);
        productTypeMarkupDO.setContractPriceProfileSeq(1);
        productTypeMarkupDO.setProductType("2");
        productTypeMarkupDOList.add(productTypeMarkupDO);

        productTypeMarkupDOList.addAll(buildItemTypeMarkupDOList());

        return productTypeMarkupDOList;
    }

    private List<ProductTypeMarkupDO> buildItemTypeMarkupDOList() {
        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();

        ProductTypeMarkupDO itemTypeMarkupDO = new ProductTypeMarkupDO();
        itemTypeMarkupDO.setItemPriceId(3);
        itemTypeMarkupDO.setGfsCustomerId("1234");
        itemTypeMarkupDO.setCustomerTypeCode(3);
        itemTypeMarkupDO.setContractPriceProfileSeq(2);
        itemTypeMarkupDO.setProductType("0");
        productTypeMarkupDOList.add(itemTypeMarkupDO);

        return productTypeMarkupDOList;
    }

    private Map<CustomerPK, List<CustomerPK>> buildCustomerHierarcyMapWithListOfAncestors() {
        Map<CustomerPK, List<CustomerPK>> customerHierarchy = new HashMap<>();
        List<CustomerPK> customerPKList = buildCustomerPKList();
        customerHierarchy.put(buildCustomerPK(), customerPKList);

        return customerHierarchy;
    }

    private CustomerPK buildCustomerPK() {
        CustomerPK customerPK = new CustomerPK();
        customerPK.setId("1");
        customerPK.setTypeCode(12);
        return customerPK;
    }

    private List<CustomerPK> buildCustomerPKList() {
        List<CustomerPK> customerPKList = new ArrayList<CustomerPK>();
        CustomerPK customerPK = buildCustomerPK();
        customerPKList.add(customerPK);
        return customerPKList;
    }

    @Test
    public void shouldNotExtractItemAndCustomerMappingWithNoExistingBidEntries() {

        int contractPriceProfileSeq = 1;
        String userId = "test user";
        int itemPriceId = 2;
        Date effectiveDate = new LocalDate(2018, 01, 05).toDate();
        Date expirationDate = new LocalDate(2019, 01, 05).toDate();

        List<ProductTypeMarkupDTO> productTypeMarkupList = buildProductTypeMarkupList();

        Map<CustomerPK, List<CustomerPK>> customerHierarchy = buildCustomerHierarcyMapWithListOfAncestors();

        List<String> itemidList = new ArrayList<String>();
        itemidList.add(String.valueOf(itemPriceId));

        when(memberHierarchyQueryProxy.findCustomerHierarchy(buildItemTypeMarkupList(), effectiveDate)).thenReturn(customerHierarchy);
        when(customerItemPriceBidLockinFilter.filterMarkupsHavingBidLockinsAtHierarchy(buildItemTypeMarkupList(), customerHierarchy, effectiveDate,
                expirationDate)).thenReturn(Collections.<ProductTypeMarkupDTO> emptyList());

        List<ProductTypeMarkupDO> returnList = target.extractItemAndCustomerMappingWithNoExistingBidEntries(contractPriceProfileSeq, userId,
                effectiveDate, expirationDate, productTypeMarkupList);

        verify(memberHierarchyQueryProxy).findCustomerHierarchy(buildItemTypeMarkupList(), effectiveDate);
        verify(customerItemPriceBidLockinFilter).filterMarkupsHavingBidLockinsAtHierarchy(buildItemTypeMarkupList(), customerHierarchy, effectiveDate,
                expirationDate);
        verify(customerItemPriceRepository, never()).saveMarkup(anyListOf(ProductTypeMarkupDO.class), any(String.class), any(Integer.class));
        assertThat(returnList.isEmpty(), equalTo(true));
    }

    @Test
    public void shouldSaveNonBidItemAndCustomerMapping() {

        int contractPriceProfileSeq = 1;
        String userId = "test user";
        List<ProductTypeMarkupDO> productTypeMarkupDOList = buildProductTypeMarkupDOList();

        target.saveNonBidItemAndCustomerMapping(contractPriceProfileSeq, userId, productTypeMarkupDOList);

        verify(customerItemPriceRepository).saveMarkup(productTypeMarkupDOList, userId, contractPriceProfileSeq);

    }

    @Test
    public void shouldNotSaveNonBidItemAndCustomerMapping() {

        int contractPriceProfileSeq = 1;
        String userId = "test user";
        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();

        target.saveNonBidItemAndCustomerMapping(contractPriceProfileSeq, userId, productTypeMarkupDOList);

        verify(customerItemPriceRepository, never()).saveMarkup(anyListOf(ProductTypeMarkupDO.class), any(String.class), any(Integer.class));

    }

    @Test
    public void shouldSaveCIPEntriesForRealCustomerForItemLevelPriceCode() {

        int contractPriceProfileSeq = 1;
        String userId = "test user";
        int itemPriceId = 2;
        Date effectiveDate = new LocalDate(2018, 01, 05).toDate();
        Date expirationDate = new LocalDate(2019, 01, 05).toDate();

        List<ProductTypeMarkupDTO> productTypeMarkupList = buildProductTypeMarkupList();

        Map<CustomerPK, List<CustomerPK>> customerHierarchy = buildCustomerHierarcyMapWithListOfAncestors();

        List<ProductTypeMarkupDO> productTypeMarkupDOList = buildProductTypeMarkupDOList();

        List<String> itemidList = new ArrayList<String>();
        itemidList.add(String.valueOf(itemPriceId));

        when(memberHierarchyQueryProxy.findCustomerHierarchy(buildItemTypeMarkupList(), effectiveDate)).thenReturn(customerHierarchy);
        when(customerItemPriceBidLockinFilter.filterMarkupsHavingBidLockinsAtHierarchy(buildItemTypeMarkupList(), customerHierarchy, effectiveDate,
                expirationDate)).thenReturn(productTypeMarkupList);
        when(markupDOBuilder.buildProductMarkupDOList(eq(userId), eq(effectiveDate), eq(expirationDate), markupDTOListWithNonBidPricing.capture()))
                .thenReturn(productTypeMarkupDOList);

        List<ProductTypeMarkupDO> returnList = target.extractItemAndCustomerMappingWithNoExistingBidEntries(contractPriceProfileSeq, userId,
                effectiveDate, expirationDate, productTypeMarkupList);

        verify(memberHierarchyQueryProxy).findCustomerHierarchy(buildItemTypeMarkupList(), effectiveDate);
        verify(customerItemPriceBidLockinFilter).filterMarkupsHavingBidLockinsAtHierarchy(buildItemTypeMarkupList(), customerHierarchy, effectiveDate,
                expirationDate);
        verify(markupDOBuilder).buildProductMarkupDOList(eq(userId), eq(effectiveDate), eq(expirationDate), markupDTOListWithNonBidPricing.capture());
        assertThat(returnList.equals(productTypeMarkupDOList), equalTo(true));
    }

}
