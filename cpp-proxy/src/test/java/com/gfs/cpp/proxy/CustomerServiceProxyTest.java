package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.gfs.corp.customer.common.CustomerStatus;
import com.gfs.corp.customer.common.CustomerType;
import com.gfs.corp.customer.common.controller.CustomerNotFoundException;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.corp.customer.common.dto.CustomerStatusDTO;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGContractDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerRequestDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.proxy.common.CppRestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceProxyTest {

    private static final String GFS_CUSTOMER_GROUP_ID = "10000012";
    private static final int CONTRACT_PRICE_PROFILE_SEQ = 1;
    private static final String EXCEPTION_NAME = "exception";
    private static final String GROUP_NAME = "testgroup";
    private static final String GROUP_DESC = "group desc";
    private static final String BASE_URL = "baseUrl/";
    private static final String UPDATE_CUSTOMER_GROUP_URL = "v2/update/customerGroup/CMG/{customerGroupId}";
    private static final String CREATE_CUSTOMER_GROUP_URL = "v2/create/customerGroup/CMG";
    private static final String FIND_CUSTOMER_GROUP_URL = "v1/groupQuery/findGroupByIdAndType/{groupId}/{groupType}";

    private final Integer customerFamilyTypeCode = CustomerType.CUSTOMER_FAMILY.getCode();

    @InjectMocks
    private CustomerServiceProxy target;

    @Mock
    private CppRestTemplate cppRestTemplate;

    @Captor
    private ArgumentCaptor<Map<String, Object>> urlVariablesCaptor;
    @Captor
    private ArgumentCaptor<CMGCustomerRequestDTO> cmgCustomerRequestCaptor;

    @Mock
    private CustomerQueryProxy customerQueryProxy;

    @Mock
    private CustomerGroupMemberValidator customerGroupMemberValidator;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "customerServiceUrl", BASE_URL);
    }

    @Test
    public void shouldCreateCmgCustomer() throws Exception {

        CMGCustomerResponseDTO expectedResponse = new CMGCustomerResponseDTO();
        expectedResponse.setId("1");

        doReturn(expectedResponse).when(cppRestTemplate).postForObject(eq(BASE_URL + CREATE_CUSTOMER_GROUP_URL), cmgCustomerRequestCaptor.capture(),
                eq(CMGCustomerResponseDTO.class));

        assertThat(target.createCMGCustomerGroup(CONTRACT_PRICE_PROFILE_SEQ, EXCEPTION_NAME), equalTo(expectedResponse));

        verify(cppRestTemplate).postForObject(eq(BASE_URL + CREATE_CUSTOMER_GROUP_URL), cmgCustomerRequestCaptor.capture(),
                eq(CMGCustomerResponseDTO.class));

        CMGCustomerRequestDTO cmgCustomerRequestDTO = cmgCustomerRequestCaptor.getValue();

        assertThat(cmgCustomerRequestDTO.getGroupDesc(), equalTo(CPPConstants.CUSTOMER_GROUP_CMG));
        assertThat(cmgCustomerRequestDTO.getGroupName(), equalTo(CONTRACT_PRICE_PROFILE_SEQ + "-" + EXCEPTION_NAME));

    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldCreateCmgCustomerException() throws Exception {

        CMGCustomerRequestDTO cmgCustomerRequest = new CMGCustomerRequestDTO();
        cmgCustomerRequest.setGroupDesc(GROUP_DESC);
        cmgCustomerRequest.setGroupName(GROUP_NAME);
        CMGCustomerResponseDTO expectedResponse = new CMGCustomerResponseDTO();

        doReturn(expectedResponse).when(cppRestTemplate).postForObject(eq(BASE_URL + CREATE_CUSTOMER_GROUP_URL), eq(cmgCustomerRequest),
                eq(CMGCustomerResponseDTO.class));

        assertThat(target.createCMGCustomerGroup(1, EXCEPTION_NAME), equalTo(expectedResponse));

        verify(cppRestTemplate).postForObject(eq(BASE_URL + CREATE_CUSTOMER_GROUP_URL), eq(cmgCustomerRequest), eq(CMGCustomerResponseDTO.class));

    }

    @Test
    public void shouldUpdateCmgCustomer() throws Exception {

        CMGCustomerRequestDTO cmgCustomerRequest = new CMGCustomerRequestDTO();
        cmgCustomerRequest.setGroupDesc(GROUP_DESC);
        cmgCustomerRequest.setGroupName(GROUP_NAME);

        target.updateCMGCustomer(GROUP_DESC, GROUP_NAME, GFS_CUSTOMER_GROUP_ID);

        verify(cppRestTemplate).put(eq(BASE_URL + UPDATE_CUSTOMER_GROUP_URL), eq(cmgCustomerRequest), urlVariablesCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariablesCaptor.getValue();

        assertThat((String) actualUrlVariables.get("customerGroupId"), equalTo(GFS_CUSTOMER_GROUP_ID));
    }

    @Test
    public void shouldUpdateCmgName() throws Exception {

        int contractPriceProfileId = 101;
        String newName = "name";

        target.updateCmgName(GFS_CUSTOMER_GROUP_ID, contractPriceProfileId, newName);

        verify(cppRestTemplate).put(eq(BASE_URL + UPDATE_CUSTOMER_GROUP_URL), cmgCustomerRequestCaptor.capture(), urlVariablesCaptor.capture());

        CMGCustomerRequestDTO actualRequest = cmgCustomerRequestCaptor.getValue();

        assertThat(actualRequest.getGroupDesc(), equalTo(CPPConstants.CUSTOMER_GROUP_CMG));
        assertThat(actualRequest.getGroupName(), equalTo(contractPriceProfileId + "-" + newName));

        Map<String, Object> actualUrlVariables = urlVariablesCaptor.getValue();

        assertThat((String) actualUrlVariables.get("customerGroupId"), equalTo(GFS_CUSTOMER_GROUP_ID));
    }

    @Test
    public void findCustomerWithActiveStatusWithHyphenName() throws CustomerNotFoundException {
        final String customerId = "768857655";
        final int statusCode = CustomerStatus.ACTIVE.code;
        final String customerName = "john-jo";
        final Integer customerTypeCode = CustomerType.CUSTOMER_TYPE_UNIT.getCode();

        final List<CustomerPK> customerPks = buildCustomerPks(customerId, customerTypeCode);

        final Map<CustomerPK, CustomerStatusDTO> customerStatusDtoByCustomerPk = buildCustomerStatusDtoByCustomerPk(customerPks.get(0), statusCode,
                customerName);

        when(customerQueryProxy.getCustomerStatusDTOByCustomerPk(customerPks)).thenReturn(customerStatusDtoByCustomerPk);

        final String result = target.findCustomerName(customerId, customerTypeCode);

        assertThat(result, CoreMatchers.notNullValue());
        assertThat(result, equalTo(customerName));

    }

    @Test
    public void findCustomerWithActiveStatusReturnsCustomer() throws CustomerNotFoundException {
        final String customerId = "768857655";
        final int statusCode = CustomerStatus.ACTIVE.code;
        final String customerName = "john jo";
        final Integer customerTypeCode = CustomerType.CUSTOMER_TYPE_UNIT.getCode();

        final List<CustomerPK> customerPks = buildCustomerPks(customerId, customerTypeCode);

        final Map<CustomerPK, CustomerStatusDTO> customerStatusDtoByCustomerPk = buildCustomerStatusDtoByCustomerPk(customerPks.get(0), statusCode,
                customerName);

        when(customerQueryProxy.getCustomerStatusDTOByCustomerPk(customerPks)).thenReturn(customerStatusDtoByCustomerPk);

        final String result = target.findCustomerName(customerId, customerTypeCode);

        assertThat(result, CoreMatchers.notNullValue());
        assertThat(result, equalTo(customerName));

    }

    @Test
    public void findCustomerWithInactiveStatusReturnsInvalidCustomer() throws CustomerNotFoundException {
        final String customerId = "767611";
        final int statusCode = CustomerStatus.INACTIVE.code;
        final String customerName = "john vo";

        final List<CustomerPK> customerPks = buildCustomerPks(customerId, customerFamilyTypeCode);

        final Map<CustomerPK, CustomerStatusDTO> customerStatusDtoByCustomerPk = buildCustomerStatusDtoByCustomerPk(customerPks.get(0), statusCode,
                customerName);

        when(customerQueryProxy.getCustomerStatusDTOByCustomerPk(customerPks)).thenReturn(customerStatusDtoByCustomerPk);

        try {
            target.findCustomerName(customerId, customerFamilyTypeCode);
            fail("expected exception");
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.INACTIVE_CUSTOMER_FOUND));
        }
    }

    @Test
    public void findCustomerWithActiveFamilyMemberStatusReturnsCustomer() throws CustomerNotFoundException {
        final String customerId = "768857655";
        final int statusCode = CustomerStatus.ACTIVE.code;
        final String customerName = "john jo";

        final List<CustomerPK> customerPks = buildCustomerPks(customerId, customerFamilyTypeCode);

        final Map<CustomerPK, CustomerStatusDTO> customerStatusDtoByCustomerPk = buildCustomerStatusDtoByCustomerPk(customerPks.get(0), statusCode,
                customerName);

        when(customerQueryProxy.getCustomerStatusDTOByCustomerPk(customerPks)).thenReturn(customerStatusDtoByCustomerPk);

        final String result = target.findCustomerName(customerId, customerFamilyTypeCode);

        assertThat(result, CoreMatchers.notNullValue());
        assertThat(result, equalTo(customerName));

        verify(customerGroupMemberValidator).validateIfHasActiveMembers(customerPks.get(0));
        verify(customerQueryProxy).getCustomerStatusDTOByCustomerPk(customerPks);
    }

    @Test
    public void findCustomerWithEmptyCustomerIdReturnsNull() {
        final String customerId = "";

        try {
            target.findCustomerName(customerId, customerFamilyTypeCode);
            fail("expected exception");
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.INVALID_CUSTOMER_FOUND));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFetchCustomerGroup() throws Exception {

        String groupId = "1234";
        Integer groupTypecode = 30;

        String customerId = "100010";
        String customeerName = "desc";

        doReturn(customerId + "-" + customeerName).when(cppRestTemplate).getForObject(eq(BASE_URL + FIND_CUSTOMER_GROUP_URL), eq(String.class),
                any(Map.class));

        CMGContractDTO actual = target.fetchCustomerGroup(groupId, groupTypecode);
        assertThat(actual.getContractName(), equalTo(customeerName));
        assertThat(actual.getContractPriceProfileId(), equalTo(customerId));

        verify(cppRestTemplate).getForObject(eq(BASE_URL + FIND_CUSTOMER_GROUP_URL), eq(String.class), urlVariablesCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariablesCaptor.getValue();

        assertThat((String) actualUrlVariables.get("groupId"), equalTo(groupId));
        assertThat((Integer) actualUrlVariables.get("groupType"), equalTo(groupTypecode));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFetchCustomerGroupName() throws Exception {

        String groupId = "1234";
        Integer groupTypecode = 30;

        String customerId = "100010";
        String customerName = "desc";

        doReturn(customerId + "-" + customerName).when(cppRestTemplate).getForObject(eq(BASE_URL + FIND_CUSTOMER_GROUP_URL), eq(String.class),
                any(Map.class));

        assertThat(target.fetchGroupName(groupId, groupTypecode), equalTo(customerName));

        verify(cppRestTemplate).getForObject(eq(BASE_URL + FIND_CUSTOMER_GROUP_URL), eq(String.class), urlVariablesCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariablesCaptor.getValue();

        assertThat((String) actualUrlVariables.get("groupId"), equalTo(groupId));
        assertThat((Integer) actualUrlVariables.get("groupType"), equalTo(groupTypecode));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldNotFetchCustomerGroup() throws Exception {

        String groupId = "1234";
        Integer groupTypecode = 30;

        when(cppRestTemplate.getForObject(eq(BASE_URL + FIND_CUSTOMER_GROUP_URL), eq(String.class), any(Map.class))).thenReturn("");

        CMGContractDTO actual = target.fetchCustomerGroup(groupId, groupTypecode);
        assertThat(actual.getContractName(), equalTo(""));
        assertThat(actual.getContractPriceProfileId(), equalTo(null));

        verify(cppRestTemplate).getForObject(eq(BASE_URL + FIND_CUSTOMER_GROUP_URL), eq(String.class), urlVariablesCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariablesCaptor.getValue();

        assertThat((String) actualUrlVariables.get("groupId"), equalTo(groupId));
        assertThat((Integer) actualUrlVariables.get("groupType"), equalTo(groupTypecode));
    }

    private List<CustomerPK> buildCustomerPks(final String customerId, final Integer customerTypeCode) {

        final List<CustomerPK> customerPks = new ArrayList<>();
        final CustomerPK customerPk = new CustomerPK(customerId, customerTypeCode);
        customerPks.add(customerPk);
        return customerPks;
    }

    private Map<CustomerPK, CustomerStatusDTO> buildCustomerStatusDtoByCustomerPk(final CustomerPK customerPk, final int statusCode,
            final String customerName) {

        final Map<CustomerPK, CustomerStatusDTO> customerStatusDtoByCustomerPk = new HashMap<>();
        final CustomerStatusDTO customerStatusDto = new CustomerStatusDTO();
        customerStatusDto.setCustomerId(customerPk.getId());
        customerStatusDto.setCustomerTypeCode(customerPk.getTypeCode());
        customerStatusDto.setCustomerName(customerName);
        customerStatusDto.setCustomerStatusCode(statusCode);

        customerStatusDtoByCustomerPk.put(customerPk, customerStatusDto);
        return customerStatusDtoByCustomerPk;
    }
}
