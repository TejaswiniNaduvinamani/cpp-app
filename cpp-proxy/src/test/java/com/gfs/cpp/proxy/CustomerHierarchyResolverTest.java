package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.HierarchyAncestorNodeDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.proxy.CustomerHierarchyResolver;

@RunWith(MockitoJUnitRunner.class)
public class CustomerHierarchyResolverTest {

    @InjectMocks
    private CustomerHierarchyResolver target;

    @Test
    public void shouldResolveToReturnOnlySameLevel() {

        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = new HashMap<>();
        HierarchyAncestorNodeDTO hierarchyAncestorNodeDTO = new HierarchyAncestorNodeDTO();
        hierarchyAncestorNodeDTO.setCustomerId("12");
        hierarchyAncestorNodeDTO.setCustomerName("pmg");
        hierarchyAncestorNodeDTO.setCustomerStatusCode(1);
        hierarchyAncestorNodeDTO.setCustomerTypeCode(15);

        CustomerPK customerPK = new CustomerPK();
        customerPK.setId("12");
        customerPK.setTypeCode(15);

        customerPKHierarchyAncestorNodeDTOMap.put(customerPK, hierarchyAncestorNodeDTO);

        List<CustomerPK> expectedCustomerList = new ArrayList<CustomerPK>();
        expectedCustomerList.add(new CustomerPK("12", 15));

        Map<CustomerPK, List<CustomerPK>> actual = target.resolve(customerPKHierarchyAncestorNodeDTOMap);

        assertThat(actual.get(customerPK), is(expectedCustomerList));

    }

    @Test
    public void shouldReturnAncestors() {

        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = new HashMap<>();
        HierarchyAncestorNodeDTO hierarchyAncestorNodeDTO = new HierarchyAncestorNodeDTO();
        hierarchyAncestorNodeDTO.setCustomerId("1234");
        hierarchyAncestorNodeDTO.setCustomerName("unit");
        hierarchyAncestorNodeDTO.setCustomerStatusCode(1);
        hierarchyAncestorNodeDTO.setCustomerTypeCode(0);
        HierarchyAncestorNodeDTO ancestorNodeDTO = new HierarchyAncestorNodeDTO();
        ancestorNodeDTO.setCustomerId("123");
        ancestorNodeDTO.setCustomerName("family");
        ancestorNodeDTO.setCustomerStatusCode(1);
        ancestorNodeDTO.setCustomerTypeCode(3);
        hierarchyAncestorNodeDTO.setAncestor(ancestorNodeDTO);
        CustomerPK customerPK = new CustomerPK();
        customerPK.setId("1234");
        customerPK.setTypeCode(0);
        customerPKHierarchyAncestorNodeDTOMap.put(customerPK, hierarchyAncestorNodeDTO);

        HierarchyAncestorNodeDTO hierarchyAncestorNodeDTO2 = new HierarchyAncestorNodeDTO();
        hierarchyAncestorNodeDTO2.setCustomerId("4567");
        hierarchyAncestorNodeDTO2.setCustomerName("family");
        hierarchyAncestorNodeDTO2.setCustomerStatusCode(2);
        hierarchyAncestorNodeDTO2.setCustomerTypeCode(4);
        HierarchyAncestorNodeDTO ancestorNodeDTO2 = new HierarchyAncestorNodeDTO();
        ancestorNodeDTO2.setCustomerId("456");
        ancestorNodeDTO2.setCustomerName("PMG");
        ancestorNodeDTO2.setCustomerStatusCode(2);
        ancestorNodeDTO2.setCustomerTypeCode(5);
        hierarchyAncestorNodeDTO2.setAncestor(ancestorNodeDTO2);
        CustomerPK customerPK2 = new CustomerPK();
        customerPK2.setId("4567");
        customerPK2.setTypeCode(4);
        customerPKHierarchyAncestorNodeDTOMap.put(customerPK2, hierarchyAncestorNodeDTO2);
        List<CustomerPK> expectedCustomerList = new ArrayList<CustomerPK>();
        expectedCustomerList.add(new CustomerPK("1234", 0));
        expectedCustomerList.add(new CustomerPK("123", 3));

        List<CustomerPK> expectedCustomerList2 = new ArrayList<CustomerPK>();
        expectedCustomerList2.add(new CustomerPK("4567", 4));
        expectedCustomerList2.add(new CustomerPK("456", 5));

        Map<CustomerPK, List<CustomerPK>> actual = target.resolve(customerPKHierarchyAncestorNodeDTOMap);

        assertThat(actual.get(customerPK), is(expectedCustomerList));
        assertThat(actual.get(customerPK2), is(expectedCustomerList2));

    }

    @Test
    public void shouldNotFilterIfNoAncestorFoundForRealCustomer() throws Exception {

        String defaultCustomerId = "defaultCustomer";
        int defaultCustomerType = 15;

        String realCustomerId = "realCustomerId";
        int realCustomerType = 0;

        RealCustomerDTO realCustomerDto = new RealCustomerDTO();
        realCustomerDto.setRealCustomerId(realCustomerId);
        realCustomerDto.setRealCustomerType(realCustomerType);

        CustomerPK realCustomerPk = new CustomerPK(realCustomerId, realCustomerType);

        HierarchyAncestorNodeDTO hierarchyForRealCustomer = new HierarchyAncestorNodeDTO();

        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = Collections.singletonMap(realCustomerPk,
                hierarchyForRealCustomer);

        List<RealCustomerDTO> actual = target.filtersCustomersNotAMember(defaultCustomerId, defaultCustomerType,
                Collections.singletonList(realCustomerDto), customerPKHierarchyAncestorNodeDTOMap);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0), equalTo(realCustomerDto));

    }

    @Test
    public void shouldNotFilterIfNoneOfAncestorIsDefaultCustomer() throws Exception {

        String defaultCustomerId = "defaultCustomer";
        int defaultCustomerType = 15;

        String realCustomerId = "realCustomerId";
        int realCustomerType = 0;

        RealCustomerDTO realCustomerDto = new RealCustomerDTO();
        realCustomerDto.setRealCustomerId(realCustomerId);
        realCustomerDto.setRealCustomerType(realCustomerType);

        CustomerPK realCustomerPk = new CustomerPK(realCustomerId, realCustomerType);

        HierarchyAncestorNodeDTO hierarchyForRealCustomer = new HierarchyAncestorNodeDTO();

        HierarchyAncestorNodeDTO firstAncestor = new HierarchyAncestorNodeDTO();
        firstAncestor.setCustomerId("nodefault");
        firstAncestor.setCustomerTypeCode(defaultCustomerType);

        hierarchyForRealCustomer.setAncestor(firstAncestor);

        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = Collections.singletonMap(realCustomerPk,
                hierarchyForRealCustomer);

        List<RealCustomerDTO> actual = target.filtersCustomersNotAMember(defaultCustomerId, defaultCustomerType,
                Collections.singletonList(realCustomerDto), customerPKHierarchyAncestorNodeDTOMap);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0), equalTo(realCustomerDto));

    }

    @Test
    public void shouldNotFilterIfNoneOfAncestorIsDefaultCustomerMultiAncestor() throws Exception {

        String defaultCustomerId = "defaultCustomer";
        int defaultCustomerType = 15;

        String realCustomerId = "realCustomerId";
        int realCustomerType = 0;

        RealCustomerDTO realCustomerDto = new RealCustomerDTO();
        realCustomerDto.setRealCustomerId(realCustomerId);
        realCustomerDto.setRealCustomerType(realCustomerType);

        CustomerPK realCustomerPk = new CustomerPK(realCustomerId, realCustomerType);

        HierarchyAncestorNodeDTO hierarchyForRealCustomer = new HierarchyAncestorNodeDTO();

        HierarchyAncestorNodeDTO firstAncestor = new HierarchyAncestorNodeDTO();
        firstAncestor.setCustomerId("nodefault");
        firstAncestor.setCustomerTypeCode(defaultCustomerType);

        HierarchyAncestorNodeDTO secondAncestor = new HierarchyAncestorNodeDTO();
        secondAncestor.setCustomerId(defaultCustomerId);
        secondAncestor.setCustomerTypeCode(30);

        firstAncestor.setAncestor(secondAncestor);

        hierarchyForRealCustomer.setAncestor(firstAncestor);

        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = Collections.singletonMap(realCustomerPk,
                hierarchyForRealCustomer);

        List<RealCustomerDTO> actual = target.filtersCustomersNotAMember(defaultCustomerId, defaultCustomerType,
                Collections.singletonList(realCustomerDto), customerPKHierarchyAncestorNodeDTOMap);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0), equalTo(realCustomerDto));

    }

    @Test
    public void shouldFilterIfFirstLevelAncestorIsDefaultCustomer() throws Exception {

        String defaultCustomerId = "defaultCustomer";
        int defaultCustomerType = 15;

        String realCustomerId = "realCustomerId";
        int realCustomerType = 0;

        RealCustomerDTO realCustomerDto = new RealCustomerDTO();
        realCustomerDto.setRealCustomerId(realCustomerId);
        realCustomerDto.setRealCustomerType(realCustomerType);

        CustomerPK realCustomerPk = new CustomerPK(realCustomerId, realCustomerType);

        HierarchyAncestorNodeDTO hierarchyForRealCustomer = new HierarchyAncestorNodeDTO();

        HierarchyAncestorNodeDTO firstAncestor = new HierarchyAncestorNodeDTO();
        firstAncestor.setCustomerId(defaultCustomerId);
        firstAncestor.setCustomerTypeCode(defaultCustomerType);

        HierarchyAncestorNodeDTO secondAncestor = new HierarchyAncestorNodeDTO();
        secondAncestor.setCustomerId(defaultCustomerId);
        secondAncestor.setCustomerTypeCode(30);

        firstAncestor.setAncestor(secondAncestor);

        hierarchyForRealCustomer.setAncestor(firstAncestor);

        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = Collections.singletonMap(realCustomerPk,
                hierarchyForRealCustomer);

        List<RealCustomerDTO> actual = target.filtersCustomersNotAMember(defaultCustomerId, defaultCustomerType,
                Collections.singletonList(realCustomerDto), customerPKHierarchyAncestorNodeDTOMap);

        assertThat(actual.isEmpty(), equalTo(true));

    }

    @Test
    public void shouldFilterIfSecondLevelAncestorIsDefaultCustomer() throws Exception {

        String defaultCustomerId = "defaultCustomer";
        int defaultCustomerType = 15;

        String realCustomerId = "realCustomerId";
        int realCustomerType = 0;

        RealCustomerDTO realCustomerDto = new RealCustomerDTO();
        realCustomerDto.setRealCustomerId(realCustomerId);
        realCustomerDto.setRealCustomerType(realCustomerType);

        CustomerPK realCustomerPk = new CustomerPK(realCustomerId, realCustomerType);

        HierarchyAncestorNodeDTO hierarchyForRealCustomer = new HierarchyAncestorNodeDTO();

        HierarchyAncestorNodeDTO firstAncestor = new HierarchyAncestorNodeDTO();
        firstAncestor.setCustomerId("nocustomer");
        firstAncestor.setCustomerTypeCode(defaultCustomerType);

        HierarchyAncestorNodeDTO secondAncestor = new HierarchyAncestorNodeDTO();
        secondAncestor.setCustomerId(defaultCustomerId);
        secondAncestor.setCustomerTypeCode(defaultCustomerType);

        firstAncestor.setAncestor(secondAncestor);

        hierarchyForRealCustomer.setAncestor(firstAncestor);

        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = Collections.singletonMap(realCustomerPk,
                hierarchyForRealCustomer);

        List<RealCustomerDTO> actual = target.filtersCustomersNotAMember(defaultCustomerId, defaultCustomerType,
                Collections.singletonList(realCustomerDto), customerPKHierarchyAncestorNodeDTOMap);

        assertThat(actual.isEmpty(), equalTo(true));

    }

    @Test
    public void shouldNotFilterIfNoAncestorIsAvailableForTheCustomerToMap() throws Exception {

        String defaultCustomerId = "defaultCustomer";
        int defaultCustomerType = 15;

        String realCustomerId = "realCustomerId";
        int realCustomerType = 0;

        RealCustomerDTO realCustomerDto = new RealCustomerDTO();
        realCustomerDto.setRealCustomerId(realCustomerId);
        realCustomerDto.setRealCustomerType(realCustomerType);

        CustomerPK realCustomerPk = new CustomerPK(realCustomerId, realCustomerType);

        HierarchyAncestorNodeDTO hierarchyForRealCustomer = new HierarchyAncestorNodeDTO();

        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = Collections.singletonMap(realCustomerPk,
                hierarchyForRealCustomer);

        List<RealCustomerDTO> actual = target.filtersCustomersNotAMember(defaultCustomerId, defaultCustomerType,
                Collections.singletonList(realCustomerDto), customerPKHierarchyAncestorNodeDTOMap);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0), equalTo(realCustomerDto));

    }

}
