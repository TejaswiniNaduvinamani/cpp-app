package com.gfs.cpp.proxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.customer.common.controller.MemberHierarchyQuery;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.HierarchyAncestorNodeDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.proxy.CustomerHierarchyResolver;
import com.gfs.cpp.proxy.ProductTypeMarkupDTOToCustomerPKTransformer;

@Component
public class MemberHierarchyQueryProxy {

    private static final Logger logger = LoggerFactory.getLogger(MemberHierarchyQueryProxy.class);

    @Autowired
    private MemberHierarchyQuery memberHierarchyQueryService;

    @Autowired
    private CustomerHierarchyResolver customerHierarchyResolver;

    @Autowired
    private ProductTypeMarkupDTOToCustomerPKTransformer productTypeMarkupDTOToCustomerPKTransformer;

    @Autowired
    private RealCustomerDTOToCustomerPKTransformer realCustomerDTOToCustomerPKTransformer;

    @Autowired
    private CPPDateUtils cppDateUtils;

    public Map<CustomerPK, List<CustomerPK>> findCustomerHierarchy(final List<ProductTypeMarkupDTO> productTypeMarkupList, final Date effectiveDate) {
        logger.info("Find customer hierarchy for each of the customer Id & Type with effective date as {}", effectiveDate);

        final List<CustomerPK> customerPKS = CollectionUtils.collect(productTypeMarkupList, productTypeMarkupDTOToCustomerPKTransformer,
                new ArrayList<CustomerPK>());

        final Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = memberHierarchyQueryService
                .loadMemberAncestralHierarchy(new HashSet<>(customerPKS), cppDateUtils.getCurrentDate());

        return customerHierarchyResolver.resolve(customerPKHierarchyAncestorNodeDTOMap);
    }

    public List<RealCustomerDTO> filterCustomersNotMemberOfDefaultCustomer(String defaultCustomerId, int defaultCustomerType,
            List<RealCustomerDTO> realCustomersToValidate) {

        Set<CustomerPK> customerPKS = CollectionUtils.collect(realCustomersToValidate, realCustomerDTOToCustomerPKTransformer,
                new HashSet<CustomerPK>());

        Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap = memberHierarchyQueryService
                .loadMemberAncestralHierarchy(customerPKS, cppDateUtils.getCurrentDate());

        return customerHierarchyResolver.filtersCustomersNotAMember(defaultCustomerId, defaultCustomerType, realCustomersToValidate,
                customerPKHierarchyAncestorNodeDTOMap);

    }

}
