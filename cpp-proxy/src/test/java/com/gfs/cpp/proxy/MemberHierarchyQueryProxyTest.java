package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.customer.common.controller.MemberHierarchyQuery;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.HierarchyAncestorNodeDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.proxy.ProductTypeMarkupDTOToCustomerPKTransformer;

@RunWith(MockitoJUnitRunner.class)
public class MemberHierarchyQueryProxyTest {

    @InjectMocks
    private MemberHierarchyQueryProxy target;

    @Mock
    private MemberHierarchyQuery memberHierarchyQueryService;

    @Mock
    private CustomerHierarchyResolver customerHierarchyResolver;

    @Mock
    private ProductTypeMarkupDTOToCustomerPKTransformer productTypeMarkupDTOToCustomerPKTransformer;

    @Mock
    private RealCustomerDTOToCustomerPKTransformer realCustomerDTOToCustomerPKTransformer;

    @Captor
    ArgumentCaptor<Set<CustomerPK>> customePkSetCaptor;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldFindCustomerHierarchy() {
        List<ProductTypeMarkupDTO> productTypeMarkupList = buildProductTypeMarkupList();
        Date membershipDate = new LocalDate(2018, 01, 05).toDate();
        ProductTypeMarkupDTO productTypeMarkupDTO = productTypeMarkupList.get(0);
        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = buildCustomerHierarchy();
        Map<CustomerPK, List<CustomerPK>> customerHierarchy = buildCustomerHierarcyMapWithListOfAncestors();

        doReturn(membershipDate).when(cppDateUtils).getCurrentDate();
        when(productTypeMarkupDTOToCustomerPKTransformer.transform(productTypeMarkupDTO)).thenReturn(buildCustomerPK());
        when(memberHierarchyQueryService.loadMemberAncestralHierarchy(new HashSet<>(buildCustomerPKList()), membershipDate))
                .thenReturn(customerPKHierarchyAncestorNodeDTOMap);
        when(customerHierarchyResolver.resolve(customerPKHierarchyAncestorNodeDTOMap)).thenReturn(customerHierarchy);

        Map<CustomerPK, List<CustomerPK>> returnedMap = target.findCustomerHierarchy(productTypeMarkupList, membershipDate);

        verify(cppDateUtils).getCurrentDate();
        verify(productTypeMarkupDTOToCustomerPKTransformer).transform(productTypeMarkupDTO);
        verify(memberHierarchyQueryService).loadMemberAncestralHierarchy(new HashSet<>(buildCustomerPKList()), membershipDate);
        verify(customerHierarchyResolver).resolve(customerPKHierarchyAncestorNodeDTOMap);
        assertThat(returnedMap.get(buildCustomerPK()), equalTo(buildCustomerPKList()));
    }

    @Test
    public void shouldFilterCustomersNotAMember() throws Exception {

        String defaultCustomerId = "default customer";
        int defaultCustomerType = 15;
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        List<RealCustomerDTO> realCustomersToValidate = Collections.singletonList(realCustomerDTO);
        Date membershipDate = new LocalDate().toDate();
        CustomerPK realCustomerPk = new CustomerPK("reaCustomerId", 3);

        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = new HashMap<>();

        doReturn(membershipDate).when(cppDateUtils).getCurrentDate();
        doReturn(realCustomerPk).when(realCustomerDTOToCustomerPKTransformer).transform(realCustomerDTO);
        doReturn(customerPKHierarchyAncestorNodeDTOMap).when(memberHierarchyQueryService).loadMemberAncestralHierarchy(customePkSetCaptor.capture(),
                eq(membershipDate));

        target.filterCustomersNotMemberOfDefaultCustomer(defaultCustomerId, defaultCustomerType, realCustomersToValidate);

        verify(cppDateUtils).getCurrentDate();
        verify(customerHierarchyResolver).filtersCustomersNotAMember(defaultCustomerId, defaultCustomerType, realCustomersToValidate,
                customerPKHierarchyAncestorNodeDTOMap);
        verify(realCustomerDTOToCustomerPKTransformer).transform(realCustomerDTO);
        verify(memberHierarchyQueryService).loadMemberAncestralHierarchy(customePkSetCaptor.capture(), eq(membershipDate));

        Set<CustomerPK> actualSetRequestedHiearchy = customePkSetCaptor.getValue();
        assertThat(actualSetRequestedHiearchy.size(), equalTo(1));
        assertThat(actualSetRequestedHiearchy.iterator().next(), equalTo(realCustomerPk));

    }

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
        return productTypeMarkupList;
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

    private Map<CustomerPK, HierarchyAncestorNodeDTO> buildCustomerHierarchy() {
        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = new HashMap<>();
        List<CustomerPK> customerPKList = new ArrayList<CustomerPK>();
        CustomerPK customerPK = buildCustomerPK();
        customerPKList.add(customerPK);

        HierarchyAncestorNodeDTO hierarchyAncestorNodeDTO = new HierarchyAncestorNodeDTO();
        hierarchyAncestorNodeDTO.setCustomerId("1");
        hierarchyAncestorNodeDTO.setCustomerName("user");
        hierarchyAncestorNodeDTO.setCustomerStatusCode(40);
        hierarchyAncestorNodeDTO.setCustomerTypeCode(12);

        customerPKHierarchyAncestorNodeDTOMap.put(customerPK, hierarchyAncestorNodeDTO);
        return customerPKHierarchyAncestorNodeDTOMap;
    }

    private Map<CustomerPK, List<CustomerPK>> buildCustomerHierarcyMapWithListOfAncestors() {
        Map<CustomerPK, List<CustomerPK>> customerHierarchy = new HashMap<>();
        List<CustomerPK> customerPKList = buildCustomerPKList();
        customerHierarchy.put(buildCustomerPK(), customerPKList);

        return customerHierarchy;
    }
}
