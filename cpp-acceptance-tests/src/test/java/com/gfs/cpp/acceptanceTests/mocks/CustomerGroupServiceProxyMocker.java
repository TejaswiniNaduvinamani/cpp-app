package com.gfs.cpp.acceptanceTests.mocks;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerRequestDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.proxy.common.CppRestTemplate;

@Component
public class CustomerGroupServiceProxyMocker implements Resettable {

    private static final String FIND_CUSTOMER_GROUP_URL = "v1/groupQuery/findGroupByIdAndType/{groupId}/{groupType}";
    private static final String CREATE_CUSTOMER_GROUP_URL = "v2/create/customerGroup/CMG";
    private static final String CUSTOMER_SERVICE_HOST = "https://wlstst1.gfs.com:7105/custgroupservice/";
    private static final String UPDATE_CUSTOMER_GROUP_URL = "v2/update/customerGroup/CMG/{customerGroupId}";

    @Autowired
    private CppRestTemplate cppRestTemplate;

    private CMGCustomerResponseDTO response = new CMGCustomerResponseDTO();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Class<Map<String, Object>> mapClass = (Class) Map.class;

    private ArgumentCaptor<Map<String, Object>> urlVariableCaptor = ArgumentCaptor.forClass(mapClass);

    @Override
    public void reset() {
        mockCreateCustomer(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        mockFindCustomerGroup();
    }

    public void mockCreateCustomer(final String gfsCustomerId) {
        when(cppRestTemplate.postForObject(eq(CUSTOMER_SERVICE_HOST + CREATE_CUSTOMER_GROUP_URL), any(CMGCustomerRequestDTO.class),
                eq(CMGCustomerResponseDTO.class))).then(new Answer<CMGCustomerResponseDTO>() {

                    @Override
                    public CMGCustomerResponseDTO answer(InvocationOnMock invocation) throws Throwable {

                        response.setId(gfsCustomerId);
                        response.setTypeCode(CukesConstants.CMG_CUSTOMER_TYPE_ID);
                        return response;
                    }

                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindCustomerGroup() {
        when(cppRestTemplate.getForObject(eq(CUSTOMER_SERVICE_HOST + FIND_CUSTOMER_GROUP_URL), any(Class.class), any(Map.class)))
                .then(new Answer<String>() {

                    @Override
                    public String answer(InvocationOnMock invocation) throws Throwable {
                        Map<String, ?> uriVariables = invocation.getArgumentAt(2, Map.class);
                        String groupId = (String) uriVariables.get("groupId");
                        if (groupId.equals(CukesConstants.GFS_CUSTOMER_ID_INACTIVE)) {
                            return "";
                        } else {
                            return groupId + "-" + CukesConstants.CONTRACT_NAME;
                        }
                    }
                });
    }

    public void verifyCustomerNameUpdatedTo(String customerGroupId, String newName) {

        @SuppressWarnings({ "unchecked", "rawtypes" })
        Class<CMGCustomerRequestDTO> requestClass = (Class) CMGCustomerRequestDTO.class;

        ArgumentCaptor<CMGCustomerRequestDTO> requestCaptor = ArgumentCaptor.forClass(requestClass);

        verify(cppRestTemplate).put(eq(CUSTOMER_SERVICE_HOST + UPDATE_CUSTOMER_GROUP_URL), requestCaptor.capture(), urlVariableCaptor.capture());
    }

}
