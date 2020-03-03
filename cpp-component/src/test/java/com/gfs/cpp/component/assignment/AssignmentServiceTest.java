package com.gfs.cpp.component.assignment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.assignments.CMGCustomerTypeDTO;
import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.assignments.MarkupAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.model.assignments.CustomerAssignmentDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.component.assignment.AssignmentService;
import com.gfs.cpp.component.assignment.CustomerAssignmentBuilder;
import com.gfs.cpp.component.assignment.FindCustomerAssignmentValidator;
import com.gfs.cpp.component.assignment.ItemAssignmentDuplicateValidator;
import com.gfs.cpp.component.assignment.MarkupAssignmentFetcher;
import com.gfs.cpp.component.assignment.SaveCustomerAssignmentValidator;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentBuilder;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentHelper;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentServiceTest {

    @InjectMocks
    private AssignmentService target;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Mock
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private MarkupAssignmentFetcher markupAssignmentFetcher;

    @Mock
    private ItemAssignmentHelper itemAssignmentHelper;

    @Mock
    private ItemAssignmentBuilder itemAssignmentBuilder;

    @Mock
    private CppItemMappingRepository cppItemMappingRepository;

    @Mock
    private ItemAssignmentDuplicateValidator itemAssignmentDuplicateValidator;

    @Captor
    private ArgumentCaptor<List<CustomerAssignmentDO>> customerAssignmentDOCaptor;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;
    @Mock
    private CustomerAssignmentBuilder customerAssignmentBuilder;
    @Mock
    private FindCustomerAssignmentValidator findCustomerAssignmentValidator;
    @Mock
    private SaveCustomerAssignmentValidator saveCustomerAssignmentValidator;

    @Test
    public void shouldReturnCustomerTypes() {

        List<CMGCustomerTypeDTO> result = target.fetchCustomerTypes();

        assertThat(result.size(), is(11));
        assertThat(result.get(0).getCustomerTypeId(), is(22));
        assertThat(result.get(0).getCustomerTypeValue(), is("Super Street Managed"));

        for (CMGCustomerTypeDTO cmgCustomerTypeDTO : result) {
            assertThat(cmgCustomerTypeDTO.equals(31), equalTo(false));
        }

    }

    @Test
    public void shouldFetchMarkupAssignment() {
        int contractPriceProfileSeq = 1;
        List<ItemLevelMarkupDTO> itemList = new ArrayList<>();
        ItemLevelMarkupDTO itemMarkup = new ItemLevelMarkupDTO();
        itemMarkup.setItemId("123");
        itemMarkup.setEffectiveDate(new LocalDate(2010, 01, 01).toDate());
        itemMarkup.setExpirationDate(new LocalDate(2011, 01, 01).toDate());
        itemMarkup.setItemDesc("test");
        itemMarkup.setMarkup("test");
        itemMarkup.setMarkupType(2);
        itemMarkup.setUnit("$");
        itemMarkup.setStockingCode(1);
        itemList.add(itemMarkup);

        ProductTypeMarkupDTO markup = new ProductTypeMarkupDTO();
        List<ProductTypeMarkupDTO> markupList = new ArrayList<>();
        markup.setMarkup("10.43");
        markup.setUnit("$");
        markup.setEffectiveDate(new LocalDate(2010, 01, 01).toDate());
        markup.setExpirationDate(new LocalDate(2012, 01, 01).toDate());
        markup.setItemPriceId(1);
        markup.setProductType("2");
        markup.setGfsCustomerId("1");
        markup.setMarkupType(2);
        markupList.add(markup);

        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<RealCustomerDTO>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerGroupType("SPMG");
        realCustomerDTO.setRealCustomerId("2");
        realCustomerDTO.setRealCustomerName("Chilli's");
        realCustomerDTO.setRealCustomerType(22);
        realCustomerDTOList.add(realCustomerDTO);

        List<MarkupAssignmentDTO> markupAssigmentList = new ArrayList<>();
        MarkupAssignmentDTO markupAssigmentDTO = new MarkupAssignmentDTO();
        markupAssigmentDTO.setGfsCustomerId("1");
        markupAssigmentDTO.setGfsCustomerType("31");
        markupAssigmentDTO.setGfsCustomerTypeId(1);
        markupAssigmentDTO.setIsAssignmentSaved(true);
        markupAssigmentDTO.setIsDefault(true);
        markupAssigmentDTO.setItemList(itemList);
        markupAssigmentDTO.setMarkupList(markupList);
        markupAssigmentDTO.setMarkupName("test");
        markupAssigmentDTO.setRealCustomerDTOList(realCustomerDTOList);
        markupAssigmentList.add(markupAssigmentDTO);

        when(markupAssignmentFetcher.fetchMarkupAssignment(eq(contractPriceProfileSeq))).thenReturn(markupAssigmentList);

        List<MarkupAssignmentDTO> actual = target.fetchMarkupAssignment(contractPriceProfileSeq);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getGfsCustomerId(), equalTo("1"));
        assertThat(actual.get(0).getGfsCustomerType(), equalTo("31"));
        assertThat(actual.get(0).getGfsCustomerTypeId(), equalTo(1));
        assertThat(actual.get(0).getIsAssignmentSaved(), equalTo(true));
        assertThat(actual.get(0).getIsDefault(), equalTo(true));
        assertThat(actual.get(0).getItemList().size(), equalTo(1));
        assertThat(actual.get(0).getItemList().get(0).getEffectiveDate(), equalTo(new LocalDate(2010, 01, 01).toDate()));
        assertThat(actual.get(0).getItemList().get(0).getExpirationDate(), equalTo(new LocalDate(2011, 01, 01).toDate()));
        assertThat(actual.get(0).getItemList().get(0).getIsItemSaved(), equalTo(false));
        assertThat(actual.get(0).getItemList().get(0).getItemDesc(), equalTo("test"));
        assertThat(actual.get(0).getItemList().get(0).getItemId(), equalTo("123"));
        assertThat(actual.get(0).getItemList().get(0).getMarkup(), equalTo("test"));
        assertThat(actual.get(0).getItemList().get(0).getMarkupType(), equalTo(2));
        assertThat(actual.get(0).getItemList().get(0).getStockingCode(), equalTo(1));
        assertThat(actual.get(0).getItemList().get(0).getUnit(), equalTo("$"));
        assertThat(actual.get(0).getMarkupName(), equalTo("test"));
        assertThat(actual.get(0).getRealCustomerDTOList().size(), equalTo(1));
        assertThat(actual.get(0).getRealCustomerDTOList().get(0).getIsCustomerSaved(), equalTo(false));
        assertThat(actual.get(0).getRealCustomerDTOList().get(0).getRealCustomerGroupType(), equalTo("SPMG"));
        assertThat(actual.get(0).getRealCustomerDTOList().get(0).getRealCustomerId(), equalTo("2"));
        assertThat(actual.get(0).getRealCustomerDTOList().get(0).getRealCustomerName(), equalTo("Chilli's"));
        assertThat(actual.get(0).getRealCustomerDTOList().get(0).getRealCustomerType(), equalTo(22));
        assertThat(actual.get(0).getMarkupList().size(), equalTo(1));
        assertThat(actual.get(0).getMarkupList().get(0).getEffectiveDate(), equalTo(new LocalDate(2010, 01, 01).toDate()));
        assertThat(actual.get(0).getMarkupList().get(0).getExpirationDate(), equalTo(new LocalDate(2012, 01, 01).toDate()));
        assertThat(actual.get(0).getMarkupList().get(0).getGfsCustomerId(), equalTo("1"));
        assertThat(actual.get(0).getMarkupList().get(0).getItemPriceId(), equalTo(1));
        assertThat(actual.get(0).getMarkupList().get(0).getMarkup(), equalTo("10.43"));
        assertThat(actual.get(0).getMarkupList().get(0).getMarkupType(), equalTo(2));
        assertThat(actual.get(0).getMarkupList().get(0).getProductType(), equalTo("2"));
        assertThat(actual.get(0).getMarkupList().get(0).getUnit(), equalTo("$"));

        verify(markupAssignmentFetcher).fetchMarkupAssignment(eq(contractPriceProfileSeq));
    }

    @Test
    public void shouldFetchMarkupAssignments() throws Exception {
        int contractPriceProfileSeq = -1010;
        List<MarkupAssignmentDTO> markupAssignments = new ArrayList<>();

        doReturn(markupAssignments).when(markupAssignmentFetcher).fetchMarkupAssignment(contractPriceProfileSeq);

        List<MarkupAssignmentDTO> actual = target.fetchMarkupAssignment(contractPriceProfileSeq);

        assertThat(actual, is(markupAssignments));
        verify(markupAssignmentFetcher).fetchMarkupAssignment(contractPriceProfileSeq);
    }

    @Test
    public void shouldReturnCustomer() throws Exception {

        String customerName = "John";
        String gfsCustomerId = "1";
        int gfsCustomerType = 31;
        String cmgCustomerId = "1";
        int cmgCustomerType = 31;
        int contractPriceProfileSeq = -101;
        CustomerAssignmentDTO custoemrAssignmentDTO = new CustomerAssignmentDTO();

        doReturn(custoemrAssignmentDTO).when(customerAssignmentBuilder).buildCustomerAssignmentDTO(gfsCustomerId, gfsCustomerType,
                contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
        when(customerServiceProxy.findCustomerName(eq(gfsCustomerId), eq(gfsCustomerType))).thenReturn(customerName);

        RealCustomerResponseDTO result = target.findCustomerName(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerType);

        assertThat(result.getCustomerName(), is(customerName));
        verify(customerAssignmentBuilder).buildCustomerAssignmentDTO(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerType);
        verify(findCustomerAssignmentValidator).validateCustomer(custoemrAssignmentDTO);
        verify(customerServiceProxy).findCustomerName(eq(gfsCustomerId), eq(gfsCustomerType));
    }

    @Test
    public void shouldReturnEmptyCustomerWhenNoCustomerFound() throws Exception {

        String gfsCustomerId = "1";
        int gfsCustomerType = 31;
        String customerName = StringUtils.EMPTY;
        String cmgCustomerId = "1";
        int cmgCustomerType = 31;
        int contractPriceProfileSeq = -101;
        CustomerAssignmentDTO custoemrAssignmentDTO = new CustomerAssignmentDTO();

        when(customerServiceProxy.findCustomerName(eq(gfsCustomerId), eq(gfsCustomerType))).thenReturn(customerName);
        doReturn(custoemrAssignmentDTO).when(customerAssignmentBuilder).buildCustomerAssignmentDTO(gfsCustomerId, gfsCustomerType,
                contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);

        RealCustomerResponseDTO result = target.findCustomerName(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerType);

        assertThat(result.getCustomerName(), is(StringUtils.EMPTY));
        verify(customerAssignmentBuilder).buildCustomerAssignmentDTO(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerType);
        verify(findCustomerAssignmentValidator).validateCustomer(custoemrAssignmentDTO);
        verify(customerServiceProxy).findCustomerName(eq(gfsCustomerId), eq(gfsCustomerType));
    }

    @Test
    public void shouldReturnThrowBusinessValidationForFindCustomer() throws Exception {

        String gfsCustomerId = "1";
        int gfsCustomerType = 31;
        String cmgCustomerId = "1";
        int cmgCustomerType = 31;
        int contractPriceProfileSeq = -101;
        CustomerAssignmentDTO custoemrAssignmentDTO = new CustomerAssignmentDTO();

        doReturn(custoemrAssignmentDTO).when(customerAssignmentBuilder).buildCustomerAssignmentDTO(gfsCustomerId, gfsCustomerType,
                contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
        doThrow(new CPPRuntimeException(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT)).when(findCustomerAssignmentValidator)
                .validateCustomer(custoemrAssignmentDTO);

        RealCustomerResponseDTO result = target.findCustomerName(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerType);

        assertThat(result.getCustomerName(), is(StringUtils.EMPTY));
        assertThat(result.getValidationCode(), is(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT.getErrorCode()));

        verify(findCustomerAssignmentValidator).validateCustomer(custoemrAssignmentDTO);
    }

    @Test
    public void shouldThrowBusinessValidationForFindCustomerWhenDefaultMappedToUnit() throws Exception {

        String gfsCustomerId = "1";
        int gfsCustomerType = 31;
        String cmgCustomerId = "1";
        int cmgCustomerType = 31;
        int contractPriceProfileSeq = -101;
        CustomerAssignmentDTO custoemrAssignmentDTO = new CustomerAssignmentDTO();

        doReturn(custoemrAssignmentDTO).when(customerAssignmentBuilder).buildCustomerAssignmentDTO(gfsCustomerId, gfsCustomerType,
                contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
        doThrow(new CPPRuntimeException(CPPExceptionType.CUSTOMER_MAPPED_TO_DEFAULT_IS_UNIT_TYPE)).when(findCustomerAssignmentValidator)
                .validateCustomer(custoemrAssignmentDTO);

        RealCustomerResponseDTO result = target.findCustomerName(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerType);

        assertThat(result.getCustomerName(), is(StringUtils.EMPTY));
        assertThat(result.getValidationCode(), is(CPPExceptionType.CUSTOMER_MAPPED_TO_DEFAULT_IS_UNIT_TYPE.getErrorCode()));
        verify(findCustomerAssignmentValidator).validateCustomer(custoemrAssignmentDTO);
    }

    @Test
    public void shouldThrowBusinessValidationForFindCustomerWhenDefaultNotMapped() throws Exception {

        String gfsCustomerId = "1";
        int gfsCustomerType = 31;
        String cmgCustomerId = "1";
        int cmgCustomerType = 31;
        int contractPriceProfileSeq = -101;
        CustomerAssignmentDTO custoemrAssignmentDTO = new CustomerAssignmentDTO();

        doReturn(custoemrAssignmentDTO).when(customerAssignmentBuilder).buildCustomerAssignmentDTO(gfsCustomerId, gfsCustomerType,
                contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
        doThrow(new CPPRuntimeException(CPPExceptionType.NO_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT)).when(findCustomerAssignmentValidator)
                .validateCustomer(custoemrAssignmentDTO);

        RealCustomerResponseDTO result = target.findCustomerName(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerType);

        assertThat(result.getCustomerName(), is(StringUtils.EMPTY));
        assertThat(result.getValidationCode(), is(CPPExceptionType.NO_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT.getErrorCode()));
        verify(findCustomerAssignmentValidator).validateCustomer(custoemrAssignmentDTO);
    }

    @Test
    public void shouldThrowCPPRuntimeExceptionForFindCustomer() throws Exception {

        String gfsCustomerId = "1";
        int gfsCustomerType = 31;
        String cmgCustomerId = "1";
        int cmgCustomerType = 31;
        int contractPriceProfileSeq = -101;
        CustomerAssignmentDTO custoemrAssignmentDTO = new CustomerAssignmentDTO();

        doReturn(custoemrAssignmentDTO).when(customerAssignmentBuilder).buildCustomerAssignmentDTO(gfsCustomerId, gfsCustomerType,
                contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
        doThrow(new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS)).when(findCustomerAssignmentValidator)
                .validateCustomer(custoemrAssignmentDTO);

        try {
            target.findCustomerName(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
            fail("expected cppruntime exception");
        } catch (CPPRuntimeException ce) {
            assertThat(ce.getType(), equalTo(CPPExceptionType.NOT_VALID_STATUS));
        }

        verify(findCustomerAssignmentValidator).validateCustomer(custoemrAssignmentDTO);
    }

    @Test
    public void shouldSaveAssignments() throws ParseException {

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setCmgCustomerId("1");
        customerAssignmentDTO.setCmgCustomerType(31);
        customerAssignmentDTO.setContractPriceProfileSeq(100);
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<RealCustomerDTO>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerGroupType("SPMG");
        realCustomerDTO.setRealCustomerId("2");
        realCustomerDTO.setRealCustomerName("Chilli's");
        realCustomerDTO.setRealCustomerType(22);
        realCustomerDTOList.add(realCustomerDTO);
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);
        String userName = "vc71u";

        when(contractPriceProfCustomerRepository.fetchCPPCustomerSeq("1", 31, 100)).thenReturn(1);

        target.saveAssignments(customerAssignmentDTO, userName);

        verify(contractPriceProfCustomerRepository).fetchCPPCustomerSeq("1", 31, 100);
        verify(cppConceptMappingRepository).saveAssignments(customerAssignmentDOCaptor.capture(), eq(userName));
        verify(saveCustomerAssignmentValidator).validateCustomer(customerAssignmentDTO);
        verify(contractPriceProfileStatusValidator).validateIfCustomerAssignmentEditableStatus(customerAssignmentDTO.getContractPriceProfileSeq());

        List<CustomerAssignmentDO> actualSaveAssignmentDOList = customerAssignmentDOCaptor.getValue();

        assertThat(actualSaveAssignmentDOList.get(0).getCppCustomerSeq(), equalTo(1));
        assertThat(actualSaveAssignmentDOList.get(0).getGfsCustomerId(), equalTo("2"));
        assertThat(actualSaveAssignmentDOList.get(0).getGfsCustomerType(), equalTo(22));
    }

    @Test
    public void shouldSaveAssignmentsBusinessValidationForvalidateCustomers() throws ParseException {

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setCmgCustomerId("1");
        customerAssignmentDTO.setCmgCustomerType(31);
        customerAssignmentDTO.setContractPriceProfileSeq(100);
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<RealCustomerDTO>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerGroupType("SPMG");
        realCustomerDTO.setRealCustomerId("2");
        realCustomerDTO.setRealCustomerName("Chilli's");
        realCustomerDTO.setRealCustomerType(22);
        realCustomerDTOList.add(realCustomerDTO);
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);
        String userName = "vc71u";

        doThrow(new CPPRuntimeException(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT)).when(saveCustomerAssignmentValidator)
                .validateCustomer(customerAssignmentDTO);

        StatusDTO statusDTO = target.saveAssignments(customerAssignmentDTO, userName);

        verify(saveCustomerAssignmentValidator).validateCustomer(customerAssignmentDTO);
        verify(contractPriceProfileStatusValidator).validateIfCustomerAssignmentEditableStatus(customerAssignmentDTO.getContractPriceProfileSeq());

        assertThat(statusDTO.getErrorCode(), is(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT.getErrorCode()));
        assertThat(statusDTO.getErrorType(), is(CPPExceptionType.CUSTOMER_ALREADY_MAPPED_TO_ACTIVE_CONTRACT.toString()));
    }

    @Test
    public void shouldThrowBusinessValidationForvalidateCustomersWhenDefaultNotMapped() throws ParseException {

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setCmgCustomerId("1");
        customerAssignmentDTO.setCmgCustomerType(31);
        customerAssignmentDTO.setContractPriceProfileSeq(100);
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<RealCustomerDTO>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerGroupType("SPMG");
        realCustomerDTO.setRealCustomerId("2");
        realCustomerDTO.setRealCustomerName("Chilli's");
        realCustomerDTO.setRealCustomerType(22);
        realCustomerDTOList.add(realCustomerDTO);
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);
        String userName = "vc71u";

        doThrow(new CPPRuntimeException(CPPExceptionType.NO_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT)).when(saveCustomerAssignmentValidator)
                .validateCustomer(customerAssignmentDTO);

        StatusDTO statusDTO = target.saveAssignments(customerAssignmentDTO, userName);

        verify(saveCustomerAssignmentValidator).validateCustomer(customerAssignmentDTO);
        verify(contractPriceProfileStatusValidator).validateIfCustomerAssignmentEditableStatus(customerAssignmentDTO.getContractPriceProfileSeq());

        assertThat(statusDTO.getErrorCode(), is(CPPExceptionType.NO_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT.getErrorCode()));
        assertThat(statusDTO.getErrorType(), is(CPPExceptionType.NO_CUSTOMER_MAPPED_TO_DEFAULT_CONCEPT.toString()));
    }

    @Test
    public void shouldThrowBusinessValidationForvalidateCustomersWhenDefaultIsUnit() throws ParseException {

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setCmgCustomerId("1");
        customerAssignmentDTO.setCmgCustomerType(31);
        customerAssignmentDTO.setContractPriceProfileSeq(100);
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<RealCustomerDTO>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerGroupType("SPMG");
        realCustomerDTO.setRealCustomerId("2");
        realCustomerDTO.setRealCustomerName("Chilli's");
        realCustomerDTO.setRealCustomerType(22);
        realCustomerDTOList.add(realCustomerDTO);
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);
        String userName = "vc71u";

        doThrow(new CPPRuntimeException(CPPExceptionType.CUSTOMER_MAPPED_TO_DEFAULT_IS_UNIT_TYPE)).when(saveCustomerAssignmentValidator)
                .validateCustomer(customerAssignmentDTO);

        StatusDTO statusDTO = target.saveAssignments(customerAssignmentDTO, userName);

        verify(saveCustomerAssignmentValidator).validateCustomer(customerAssignmentDTO);
        verify(contractPriceProfileStatusValidator).validateIfCustomerAssignmentEditableStatus(customerAssignmentDTO.getContractPriceProfileSeq());

        assertThat(statusDTO.getErrorCode(), is(CPPExceptionType.CUSTOMER_MAPPED_TO_DEFAULT_IS_UNIT_TYPE.getErrorCode()));
        assertThat(statusDTO.getErrorType(), is(CPPExceptionType.CUSTOMER_MAPPED_TO_DEFAULT_IS_UNIT_TYPE.toString()));
    }

    @Test
    public void shouldSaveAssignmentsBusinessIfCustomerAssignmentEditableStatus() throws ParseException {

        CustomerAssignmentDTO customerAssignmentDTO = new CustomerAssignmentDTO();
        customerAssignmentDTO.setCmgCustomerId("1");
        customerAssignmentDTO.setCmgCustomerType(31);
        customerAssignmentDTO.setContractPriceProfileSeq(100);
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<RealCustomerDTO>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerGroupType("SPMG");
        realCustomerDTO.setRealCustomerId("2");
        realCustomerDTO.setRealCustomerName("Chilli's");
        realCustomerDTO.setRealCustomerType(22);
        realCustomerDTOList.add(realCustomerDTO);
        customerAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);
        String userName = "vc71u";

        doThrow(new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS)).when(contractPriceProfileStatusValidator)
                .validateIfCustomerAssignmentEditableStatus(customerAssignmentDTO.getContractPriceProfileSeq());

        try {
            target.saveAssignments(customerAssignmentDTO, userName);
        } catch (CPPRuntimeException ce) {
            assertThat(ce.getType(), equalTo(CPPExceptionType.NOT_VALID_STATUS));
        }

        verify(contractPriceProfileStatusValidator).validateIfCustomerAssignmentEditableStatus(customerAssignmentDTO.getContractPriceProfileSeq());

    }

    @Test
    public void shouldFetchAllFutureItems() {
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTOList = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode);

        when(itemAssignmentHelper.fetchAllFutureItems(eq(contractPriceProfileSeq)))
                .thenReturn(Collections.singletonList(itemAssignmentWrapperDTOList));

        List<ItemAssignmentWrapperDTO> result = target.fetchAllFutureItems(contractPriceProfileSeq);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getContractPriceProfileSeq(), is(1));
        assertThat(result.get(0).getExceptionName(), is("test"));
        assertThat(result.get(0).getFutureItemDesc(), is("test"));
        assertThat(result.get(0).getGfsCustomerId(), is("1"));
        assertThat(result.get(0).getIsFutureItemSaved(), is(true));
        assertThat(result.get(0).getItemAssignmentList().get(0).getItemDescription(), is("test"));
        assertThat(result.get(0).getItemAssignmentList().get(0).getItemId(), is("1"));
        assertThat(result.get(0).getItemAssignmentList().get(0).getIsItemSaved(), is(true));

        verify(itemAssignmentHelper).fetchAllFutureItems(contractPriceProfileSeq);
    }

    @Test
    public void shouldDeleteAllCustomerMapping() {
        String realCustomerId = "1";
        int realCustomerType = 31;
        int contractPriceProfileSeq = 21;
        String gfsCustomerId = "2";
        int gfsCustomerType = 21;

        CustomerAssignmentDO customerAssignmentDO = new CustomerAssignmentDO();
        customerAssignmentDO.setGfsCustomerId("1");
        customerAssignmentDO.setGfsCustomerType(31);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setDefaultCustomerInd(1);
        cmgCustomerResponseDTO.setId(gfsCustomerId);
        cmgCustomerResponseDTO.setTypeCode(gfsCustomerType);

        when(contractPriceProfCustomerRepository.fetchGFSCustomerDetailForCustomer(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerType)))
                .thenReturn(cmgCustomerResponseDTO);

        target.deleteCustomerAssignment(realCustomerId, realCustomerType, contractPriceProfileSeq, gfsCustomerId, gfsCustomerType);

        verify(cppConceptMappingRepository).deleteAllCustomerMapping(eq(contractPriceProfileSeq));
        verify(contractPriceProfileStatusValidator).validateIfCustomerAssignmentEditableStatus(eq(contractPriceProfileSeq));
        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailForCustomer(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerType));
    }

    @Test
    public void shouldDeleteCustomerMappingForException() {
        String realCustomerId = "1";
        int realCustomerType = 31;
        int contractPriceProfileSeq = 21;
        String gfsCustomerId = "2";
        int gfsCustomerType = 21;

        CustomerAssignmentDO customerAssignmentDO = new CustomerAssignmentDO();
        customerAssignmentDO.setGfsCustomerId("1");
        customerAssignmentDO.setGfsCustomerType(31);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setDefaultCustomerInd(0);
        cmgCustomerResponseDTO.setId(gfsCustomerId);
        cmgCustomerResponseDTO.setTypeCode(gfsCustomerType);

        when(contractPriceProfCustomerRepository.fetchGFSCustomerDetailForCustomer(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerType)))
                .thenReturn(cmgCustomerResponseDTO);

        target.deleteCustomerAssignment(realCustomerId, realCustomerType, contractPriceProfileSeq, gfsCustomerId, gfsCustomerType);

        verify(cppConceptMappingRepository).deleteCustomerMapping(eq(customerAssignmentDO));
        verify(contractPriceProfileStatusValidator).validateIfCustomerAssignmentEditableStatus(eq(contractPriceProfileSeq));
        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailForCustomer(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerType));
    }

    @Test
    public void shouldAssignItemsIfValidationIsSuccessful() throws ParseException {
        String userName = "vc71u";
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode);

        StatusDTO statusDTO = target.assignItems(itemAssignmentWrapperDTO, userName);

        assertThat(statusDTO.getErrorCode(), equalTo(HttpStatus.OK.value()));

        verify(contractPriceProfileStatusValidator).validateIfItemAssignmentEditableStatus(eq(contractPriceProfileSeq));
        verify(itemAssignmentHelper).assignItemsWithFutureItem(eq(itemAssignmentWrapperDTO), eq(userName));
    }

    @Test
    public void shouldUpdateItemsIfAlreadyExist() throws ParseException {
        String userName = "vc71u";
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode);

        StatusDTO statusDTO = target.assignItems(itemAssignmentWrapperDTO, userName);

        assertThat(statusDTO.getErrorCode(), equalTo(HttpStatus.OK.value()));

        verify(contractPriceProfileStatusValidator).validateIfItemAssignmentEditableStatus(eq(contractPriceProfileSeq));
        verify(itemAssignmentHelper).assignItemsWithFutureItem(eq(itemAssignmentWrapperDTO), eq(userName));
    }

    @Test
    public void shouldThrowBusinessValidationIfValidationFails() throws ParseException {

        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        String userName = "vc71u";

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode);

        doThrow(new CPPRuntimeException(CPPExceptionType.ITEM_ALREADY_EXIST, "")).when(itemAssignmentHelper)
                .assignItemsWithFutureItem(eq(itemAssignmentWrapperDTO), eq(userName));

        try {
            target.assignItems(itemAssignmentWrapperDTO, userName);
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.ITEM_ALREADY_EXIST));
            assertThat(ex.getMessage(), equalTo(""));
        }

        verify(contractPriceProfileStatusValidator).validateIfItemAssignmentEditableStatus(eq(contractPriceProfileSeq));
        verify(itemAssignmentHelper).assignItemsWithFutureItem(eq(itemAssignmentWrapperDTO), eq(userName));
    }

    @Test
    public void shouldThrowCPPRuntimeExceptionIfValidationFails() throws ParseException {

        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        String userName = "vc71u";

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode);

        doThrow(new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS, "")).when(contractPriceProfileStatusValidator)
                .validateIfItemAssignmentEditableStatus(eq(contractPriceProfileSeq));

        try {
            target.assignItems(itemAssignmentWrapperDTO, userName);
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.NOT_VALID_STATUS));
            assertThat(ex.getMessage(), equalTo(""));
        }

        verify(contractPriceProfileStatusValidator).validateIfItemAssignmentEditableStatus(eq(contractPriceProfileSeq));
    }

    @Test
    public void shouldDeleteItemAssignment() {

        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        int contractPriceProfileSeq = 1;
        int customerItemDescSeq = 1;
        String itemId = "1000";
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);
        String futureItemDesc = "chicken";
        String userName = "vc71u";
        ItemLevelMarkupDTO futureItemDTO = buildFutureItemDTO();

        when(customerItemDescPriceRepository.fetchFutureItemForAssignment(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerTypeCode),
                eq(futureItemDesc))).thenReturn(futureItemDTO);

        target.deleteItemAssignment(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, itemId, futureItemDesc, userName);

        verify(customerItemDescPriceRepository).fetchFutureItemForAssignment(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerTypeCode),
                eq(futureItemDesc));
        verify(itemAssignmentHelper).expireItemMapping(eq(customerItemDescSeq), eq(itemId), eq(userName));
        verify(customerItemPriceRepository).deleteExistingItemOrSubgroupMarkup(eq(contractPriceProfileSeq), eq(gfsCustomerId), eq(gfsCustomerTypeCode),
                eq(itemIdList), eq(ItemPriceLevel.ITEM.getCode()));
        verify(contractPriceProfileStatusValidator).validateIfItemAssignmentEditableStatus(contractPriceProfileSeq);
    }

    private ItemAssignmentWrapperDTO buildItemAssignmentWrapperDTO(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode) {
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        itemAssignmentWrapperDTO.setExceptionName("test");
        itemAssignmentWrapperDTO.setFutureItemDesc("test");
        itemAssignmentWrapperDTO.setGfsCustomerId(gfsCustomerId);
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        itemAssignmentWrapperDTO.setIsFutureItemSaved(true);
        List<ItemAssignmentDTO> itemAssignmentDTOList = buildItemAssignmentDTOList();
        itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentDTOList);
        return itemAssignmentWrapperDTO;
    }

    private List<ItemAssignmentDTO> buildItemAssignmentDTOList() {
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("test");
        itemAssignmentDTO.setItemId("1");
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTOList.add(itemAssignmentDTO);
        return itemAssignmentDTOList;
    }

    private ItemLevelMarkupDTO buildFutureItemDTO() {
        ItemLevelMarkupDTO futureItemDTO = new ItemLevelMarkupDTO();
        futureItemDTO.setCustomerItemDescSeq(1);
        futureItemDTO.setItemDesc("test");
        futureItemDTO.setMarkup("2.00");
        futureItemDTO.setMarkupType(1);
        futureItemDTO.setUnit("%");
        return futureItemDTO;
    }
}
