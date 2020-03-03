package com.gfs.cpp.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.customer.common.controller.CustomerGroupMembershipQuery;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.CustomerStatusDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.CustomerTypeCodeEnum;

@Component
public class CustomerGroupMemberValidator {

    @Autowired
    private CustomerGroupMembershipQuery customerGroupMembershipQuery;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    @Autowired
    private CustomerQueryProxy customerQueryProxy;

    static final Logger logger = LoggerFactory.getLogger(CustomerGroupMemberValidator.class);

    public void validateIfHasActiveMembers(final CustomerPK customerPk) {

        List<String> memberUnitIdList = customerGroupMembershipQuery.findMemberUnits(customerPk.getId(), customerPk.getTypeCode(),
                cppDateUtils.getCurrentDate());

        if (CollectionUtils.isNotEmpty(memberUnitIdList)) {
            List<CustomerPK> customerPksForMember = buildCustomerPksForMember(memberUnitIdList);
            Map<CustomerPK, CustomerStatusDTO> customerStatusDTOMapForMember = customerQueryProxy
                    .getCustomerStatusDTOByCustomerPk(customerPksForMember);

            if (MapUtils.isNotEmpty(customerStatusDTOMapForMember)) {

                for (CustomerStatusDTO customerStatusDTOForMember : customerStatusDTOMapForMember.values()) {
                    if (customerServiceProxy.isActiveCustomer(customerStatusDTOForMember)) {
                        return;
                    }
                }
            }
        }
        logger.error("Customer {} of type {} don't have any active customer units: ", customerPk.getId(), customerPk.getTypeCode());
        throw new CPPRuntimeException(CPPExceptionType.INACTIVE_CUSTOMER_FOUND, "Inactive customer members found.");
    }

    private List<CustomerPK> buildCustomerPksForMember(List<String> memberUnitIdList) {
        List<CustomerPK> customerPksForMember = new ArrayList<>();
        for (String memberUnitId : memberUnitIdList) {
            CustomerPK customerPkForMember = new CustomerPK(memberUnitId, CustomerTypeCodeEnum.CUSTOMER_UNIT.getGfsCustomerTypeCode());
            customerPksForMember.add(customerPkForMember);
        }
        return customerPksForMember;
    }

}