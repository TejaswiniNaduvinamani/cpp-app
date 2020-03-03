package com.gfs.cpp.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.gfs.corp.customer.common.dto.HierarchyAncestorNodeDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.corp.customer.common.dto.CustomerPK;

@Component
public class CustomerHierarchyResolver {

    public Map<CustomerPK, List<CustomerPK>> resolve(final Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap) {
        final Map<CustomerPK, List<CustomerPK>> resolvedCustomerHierarchies = new HashMap<>();

        for (final Map.Entry<CustomerPK, HierarchyAncestorNodeDTO> entry : customerPKHierarchyAncestorNodeDTOMap.entrySet()) {
            final List<CustomerPK> customerHierarchies = new ArrayList<>();

            resolveHierarchies(entry.getValue(), customerHierarchies);
            resolvedCustomerHierarchies.put(new CustomerPK(entry.getKey().getId(), entry.getKey().getTypeCode()), customerHierarchies);

        }
        return resolvedCustomerHierarchies;
    }

    private void resolveHierarchies(final HierarchyAncestorNodeDTO hierarchyAncestorNode, final List<CustomerPK> customerHierarchies) {

        customerHierarchies.add(new CustomerPK(hierarchyAncestorNode.getCustomerId(), hierarchyAncestorNode.getCustomerTypeCode()));

        final HierarchyAncestorNodeDTO ancestor = hierarchyAncestorNode.getAncestor();
        if (ancestor != null) {
            resolveHierarchies(ancestor, customerHierarchies);
        }
    }

    public List<RealCustomerDTO> filtersCustomersNotAMember(String defaultCustomerId, int defaultCustomerType,
            List<RealCustomerDTO> realCustomersToValidate, Map<CustomerPK, HierarchyAncestorNodeDTO> customerPKHierarchyAncestorNodeDTOMap) {

        List<RealCustomerDTO> customersNotAMember = new ArrayList<>();

        for (RealCustomerDTO realCustomerDTO : realCustomersToValidate) {

            HierarchyAncestorNodeDTO hierarchyAncestorNodeDTO = customerPKHierarchyAncestorNodeDTOMap
                    .get(new CustomerPK(realCustomerDTO.getRealCustomerId(), realCustomerDTO.getRealCustomerType()));

            HierarchyAncestorNodeDTO ancestor = hierarchyAncestorNodeDTO.getAncestor();

            if (ancestor == null || !isMember(defaultCustomerId, defaultCustomerType, ancestor)) {
                customersNotAMember.add(realCustomerDTO);
            }

        }

        return customersNotAMember;
    }

    boolean isMember(String defaultCustomerId, int defaultCustomerType, HierarchyAncestorNodeDTO hierarchyAncestorNodeDTO) {

        if (hierarchyAncestorNodeDTO != null) {
            if (hierarchyAncestorNodeDTO.getCustomerId().equals(defaultCustomerId)
                    && hierarchyAncestorNodeDTO.getCustomerTypeCode() == defaultCustomerType) {
                return true;
            }
            return isMember(defaultCustomerId, defaultCustomerType, hierarchyAncestorNodeDTO.getAncestor());
        }

        return false;
    }
}
