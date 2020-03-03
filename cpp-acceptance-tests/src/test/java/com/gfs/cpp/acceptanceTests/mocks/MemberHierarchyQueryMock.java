package com.gfs.cpp.acceptanceTests.mocks;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.customer.common.controller.MemberHierarchyQuery;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.HierarchyAncestorNodeDTO;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;

@Component
public class MemberHierarchyQueryMock implements Resettable {

    @Autowired
    private MemberHierarchyQuery memberHierarchyQueryService;

    public MemberHierarchyQueryMock() {
    }

    Map<CustomerPK, HierarchyAncestorNodeDTO> hierarchyDetails = new HashMap<>();

    HierarchyAncestorNodeDTO ancestorNode = null;

    @SuppressWarnings("unchecked")
    @Override
    public void reset() {

        ancestorNode = buildAncestorNode(new CustomerPK(CukesConstants.REAL_CUSTOMER_ID, CukesConstants.REAL_CUSTOMER_TYPE_ID));

        when(memberHierarchyQueryService.loadMemberAncestralHierarchy(any(Set.class), any(Date.class))).then(

                new Answer<Map<CustomerPK, HierarchyAncestorNodeDTO>>() {
                    @Override
                    public Map<CustomerPK, HierarchyAncestorNodeDTO> answer(InvocationOnMock invocation) throws Throwable {
                        // Set<CustomerPK> members, Date membershipDate
                        Set<CustomerPK> members = invocation.getArgumentAt(0, Set.class);
                        for (CustomerPK customers : members) {

                            HierarchyAncestorNodeDTO customerNode = new HierarchyAncestorNodeDTO();
                            customerNode.setCustomerId(customers.getId());
                            customerNode.setCustomerName("CustomerName");
                            customerNode.setCustomerStatusCode(1);
                            customerNode.setCustomerTypeCode(customers.getTypeCode());
                            customerNode.setAncestor(ancestorNode);

                            hierarchyDetails.put(customers, customerNode);
                        }
                        return hierarchyDetails;
                    }
                });

    }

    private HierarchyAncestorNodeDTO buildAncestorNode(CustomerPK customers) {
        HierarchyAncestorNodeDTO spmgNode = new HierarchyAncestorNodeDTO();
        spmgNode.setCustomerId("SPMG-CustomerID-" + customers.getId());
        spmgNode.setCustomerName("SPMG-CustomerID-" + customers.getId());
        spmgNode.setCustomerStatusCode(1);
        spmgNode.setCustomerTypeCode(4 + customers.getTypeCode());
        spmgNode.setAncestor(null);

        HierarchyAncestorNodeDTO pmgNode = new HierarchyAncestorNodeDTO();
        pmgNode.setCustomerId("PMG-CustomerID-" + customers.getId());
        pmgNode.setCustomerName("PMG-CustomerID-" + customers.getId());
        pmgNode.setCustomerStatusCode(1);
        pmgNode.setCustomerTypeCode(3 + customers.getTypeCode());
        pmgNode.setAncestor(spmgNode);

        HierarchyAncestorNodeDTO familyNode = new HierarchyAncestorNodeDTO();
        familyNode.setCustomerId("Family-Customer-" + customers.getId());
        familyNode.setCustomerName("Family-Customer-" + customers.getId());
        familyNode.setCustomerStatusCode(1);
        familyNode.setCustomerTypeCode(2 + customers.getTypeCode());
        familyNode.setAncestor(pmgNode);
        return familyNode;
    }

    public void setMemberRelation(String ancestorCustomerId, int ancestorTypeCode) {

        HierarchyAncestorNodeDTO ancestorNode = new HierarchyAncestorNodeDTO();
        ancestorNode.setCustomerId(ancestorCustomerId);
        ancestorNode.setCustomerTypeCode(ancestorTypeCode);

        this.ancestorNode = ancestorNode;

    }

}
