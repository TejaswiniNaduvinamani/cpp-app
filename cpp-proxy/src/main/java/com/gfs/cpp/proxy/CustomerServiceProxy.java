package com.gfs.cpp.proxy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gfs.corp.customer.common.CustomerStatus;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.CustomerStatusDTO;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGContractDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerRequestDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CustomerTypeCodeEnum;
import com.gfs.cpp.proxy.common.CppRestTemplate;

@Component
public class CustomerServiceProxy {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceProxy.class);

    private static final String UPDATE_CUSTOMER_GROUP_URL = "v2/update/customerGroup/CMG/{customerGroupId}";
    private static final String CREATE_CUSTOMER_GROUP_URL = "v2/create/customerGroup/CMG";
    private static final String FIND_CUSTOMER_GROUP_URL = "v1/groupQuery/findGroupByIdAndType/{groupId}/{groupType}";

    @Autowired
    private CppRestTemplate cppRestTemplate;

    @Autowired
    private CustomerQueryProxy customerQueryProxy;

    @Autowired
    private CustomerGroupMemberValidator customerGroupMemberValidator;

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    public CMGCustomerResponseDTO createCMGCustomerGroup(int contractPriceProfileId, String newGroupName) {
        CMGCustomerRequestDTO cmgCustomerRequest = new CMGCustomerRequestDTO();
        String groupName = buildGroupName(contractPriceProfileId, newGroupName);
        cmgCustomerRequest.setGroupDesc(CPPConstants.CUSTOMER_GROUP_CMG);
        cmgCustomerRequest.setGroupName(groupName);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = cppRestTemplate.postForObject(customerServiceUrl + CREATE_CUSTOMER_GROUP_URL,
                cmgCustomerRequest, CMGCustomerResponseDTO.class);

        if (null == cmgCustomerResponseDTO || StringUtils.isBlank(cmgCustomerResponseDTO.getId())) {
            logger.error(" CMG customer not created for contract price profile sequence {} ", groupName);
            throw new CPPRuntimeException(CPPExceptionType.CREATE_CMG_CUSTOMER_FAILED, String.format("Customer Not created for : %s.", groupName));
        }
        return cmgCustomerResponseDTO;
    }

    public void updateCMGCustomer(String grouDesc, String groupName, String customerGroupId) {

        CMGCustomerRequestDTO cmgCustomerRequest = new CMGCustomerRequestDTO();
        cmgCustomerRequest.setGroupDesc(grouDesc);
        cmgCustomerRequest.setGroupName(groupName);

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("customerGroupId", customerGroupId);

        cppRestTemplate.put(customerServiceUrl + UPDATE_CUSTOMER_GROUP_URL, cmgCustomerRequest, uriVariables);
    }

    public void updateCmgName(String cmgId, int contractPriceProfileId, String newName) {
        updateCMGCustomer(CPPConstants.CUSTOMER_GROUP_CMG, buildGroupName(contractPriceProfileId, newName), cmgId);
    }

    private String buildGroupName(int contractPriceProfileId, String newName) {
        return contractPriceProfileId + CPPConstants.HYPHEN + newName;
    }

    public CMGContractDTO fetchCustomerGroup(String groupId, Integer groupTypecode) {

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("groupId", groupId);
        uriVariables.put("groupType", groupTypecode);

        String response = cppRestTemplate.getForObject(customerServiceUrl + FIND_CUSTOMER_GROUP_URL, String.class, uriVariables);

        CMGContractDTO cmgContractDTO = new CMGContractDTO();

        if (StringUtils.isNotBlank(response) && response.contains(CPPConstants.HYPHEN)) {
            String[] responsesStringArray = response.split(CPPConstants.HYPHEN, CPPConstants.INDICATOR_TWO);
            cmgContractDTO.setContractPriceProfileId(responsesStringArray[0]);
            cmgContractDTO.setContractName(responsesStringArray[1]);
        } else {
            cmgContractDTO.setContractName(response);
        }
        return cmgContractDTO;
    }

    // TODO: update all the place to use this method instead of fetchCutomerGroup
    public String fetchGroupName(String groupId, Integer groupTypecode) {
        CMGContractDTO fetchCustomerGroup = fetchCustomerGroup(groupId, groupTypecode);
        return fetchCustomerGroup.getContractName();
    }

    public String findCustomerName(String customerId, Integer customerTypeCode) {

        CustomerPK customerPk = new CustomerPK(customerId, customerTypeCode);

        final Map<CustomerPK, CustomerStatusDTO> customerStatusDTOByCustomerPk = customerQueryProxy
                .getCustomerStatusDTOByCustomerPk(Arrays.asList(customerPk));

        if (customerStatusDTOByCustomerPk != null && !customerStatusDTOByCustomerPk.isEmpty()) {
            return retrieveCustomerFromCustomerQueryResponse(customerPk, customerStatusDTOByCustomerPk);
        } else {
            logger.error("Customer Not Found for Customer Id {}", customerId);
            throw new CPPRuntimeException(CPPExceptionType.INVALID_CUSTOMER_FOUND, "Invalid Customer found.");
        }
    }

    private String retrieveCustomerFromCustomerQueryResponse(final CustomerPK customerPk,
            final Map<CustomerPK, CustomerStatusDTO> customerStatusDTOByCustomerPk) {

        final CustomerStatusDTO customerStatusDTO = customerStatusDTOByCustomerPk.get(customerPk);

        if (customerStatusDTO != null && isActiveCustomer(customerStatusDTO)) {

            if (CustomerTypeCodeEnum.CUSTOMER_UNIT.getGfsCustomerTypeCode() != customerPk.getTypeCode()) {
                customerGroupMemberValidator.validateIfHasActiveMembers(customerPk);
            }

            return customerStatusDTO.getCustomerName();
        } else {
            logger.error("Customer is found to be not active: {} ", customerPk.getId());
            throw new CPPRuntimeException(CPPExceptionType.INACTIVE_CUSTOMER_FOUND, "Inactive Customer found.");
        }
    }

    public boolean isActiveCustomer(final CustomerStatusDTO customerStatus) {

        return customerStatus.getCustomerStatusCode() == CustomerStatus.ACTIVE.code;
    }

}
