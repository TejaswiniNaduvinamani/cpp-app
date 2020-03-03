package com.gfs.cpp.component.markup;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.gfs.corp.component.price.common.types.AmountType;
import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.corp.component.price.common.types.PricingUnitType;
import com.gfs.corp.customer.common.exception.CustomerServiceException;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerRequestDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.ExceptionMarkupRenameDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupExceptionDTO;
import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.model.markup.FutureItemDO;
import com.gfs.cpp.common.model.markup.MarkupWrapperDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentHelper;
import com.gfs.cpp.component.contractpricing.ContractPricingService;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.component.markup.builder.MarkupDTOBuilder;
import com.gfs.cpp.component.markup.builder.MarkupWrapperBuilder;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;
import com.gfs.cpp.proxy.ItemConfigurationServiceProxy;
import com.gfs.cpp.proxy.ItemInformationDTOBuilder;
import com.gfs.cpp.proxy.ItemQueryProxy;

@RunWith(MockitoJUnitRunner.class)
public class MarkupServiceTest {

    private static final Date pricingEffectiveDate = new LocalDate(2010, 01, 01).toDate();
    private static final Date pricingExpirationDate = new LocalDate(2012, 01, 01).toDate();

    @InjectMocks
    private MarkupService target;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Mock
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Mock
    private CppItemMappingRepository cppItemMappingRepository;

    @Mock
    private ContractPricingService contractPricingService;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    @Mock
    private ItemQueryProxy itemQueryProxy;

    @Mock
    private ItemInformationDTOBuilder itemInformationDTOBuilder;

    @Mock
    private MarkupDOBuilder markupDOBuilder;

    @Mock
    private MarkupFetcher markupFetcher;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    @Mock
    private ItemAssignmentHelper itemAssignmentHelper;

    @Mock
    private FutureItemUpdater futureItemUpdater;

    @Mock
    private SubgroupValidator subgroupValidator;

    @Mock
    private ItemConfigurationServiceProxy itemConfigurationServiceProxy;

    @Mock
    private SubgroupResponseDTOBuilder subgroupResponseDTOBuilder;

    @Mock
    private MarkupSaver markupSaver;

    @Mock
    private MarkupOnSellUpdater markupOnSellUpdater;

    @Mock
    private MarkupDTOBuilder markupDTOBuilder;

    @Mock
    private MarkupWrapperBuilder markupWrapperBuilder;

    @Mock
    private FutureItemDeleteHandler futureItemDeleteHandler;

    @Test
    public void shouldSaveMarkups() throws ParseException, CustomerServiceException {

        String gfsCustomerId = "124";
        int customerTypeCode = 31;
        int contractPriceProfileSeq = 124;
        String userName = "v8cq5";

        ProductTypeMarkupDTO markup = new ProductTypeMarkupDTO();

        List<ProductTypeMarkupDTO> productTypeMarkupList = new ArrayList<>();
        markup.setMarkup("10.43");
        markup.setUnit("$");
        markup.setEffectiveDate(pricingEffectiveDate);
        markup.setExpirationDate(pricingExpirationDate);
        markup.setItemPriceId(1);
        markup.setProductType("2");
        markup.setMarkupType(2);
        productTypeMarkupList.add(markup);

        List<ProductTypeMarkupDO> markupDOList = new ArrayList<>();
        ProductTypeMarkupDO markupDO = new ProductTypeMarkupDO();
        markupDO.setMarkup(new BigDecimal("2.43"));
        markupDO.setUnit("$");
        markupDO.setEffectiveDate(new Date());
        markupDO.setExpirationDate(new Date());
        markupDO.setItemPriceId(5);
        markupDO.setProductType("5");
        markupDO.setMarkupType(2);
        markupDOList.add(markupDO);

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        FutureItemDO futureItemDO = new FutureItemDO();
        futureItemDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        futureItemDO.setCustomerTypeCode(customerTypeCode);
        futureItemDO.setGfsCustomerId(gfsCustomerId);
        futureItemDO.setItemDesc("test1");
        futureItemDO.setMarkup(new BigDecimal("10.43"));
        futureItemDO.setUnit("$");
        List<FutureItemDO> futureItemList = new ArrayList<>();
        futureItemList.add(futureItemDO);

        markupWrapper.setIsMarkupSaved(false);
        markupWrapper.setGfsCustomerId(gfsCustomerId);
        markupWrapper.setGfsCustomerType(customerTypeCode);
        markupWrapper.setMarkupName("Test");
        markupWrapper.setContractPriceProfileSeq(124);
        markupWrapper.setProductMarkupList(productTypeMarkupList);

        MarkupWrapperDO markupWrapperDO = new MarkupWrapperDO();
        markupWrapperDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapperDO.setMarkupList(markupDOList);
        markupWrapperDO.setUserName(userName);
        markupWrapperDO.setFutureItemList(futureItemList);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(gfsCustomerId);
        cmgCustomerResponseDTO.setTypeCode(customerTypeCode);

        when(markupDOBuilder.buildMarkupWrapperDO(markupWrapper, contractPriceProfileSeq, userName)).thenReturn(markupWrapperDO);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgCustomerResponseDTO);

        target.saveMarkup(markupWrapper, userName);

        verify(markupSaver).saveMarkups(markupWrapper, contractPriceProfileSeq, userName);
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(markupWrapperDO.getContractPriceProfileSeq());
    }

    @Test
    public void shouldNotSaveFutureItemWhenNoFutureItemAvaialble() throws Exception {

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();

        String gfsCustomerId = "124";
        int customerTypeCode = 31;
        int contractPriceProfileSeq = 124;
        String userName = "v8cq5";

        List<ProductTypeMarkupDO> markupDOList = new ArrayList<>();
        ProductTypeMarkupDO markupDO = new ProductTypeMarkupDO();
        markupDO.setMarkup(new BigDecimal("2.43"));
        markupDO.setUnit("$");
        markupDO.setEffectiveDate(new Date());
        markupDO.setExpirationDate(new Date());
        markupDO.setItemPriceId(5);
        markupDO.setProductType("5");
        markupDO.setMarkupType(2);
        markupDOList.add(markupDO);

        MarkupWrapperDO markupWrapperDO = new MarkupWrapperDO();
        markupWrapperDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapperDO.setMarkupList(markupDOList);
        markupWrapperDO.setUserName(userName);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(gfsCustomerId);
        cmgCustomerResponseDTO.setTypeCode(customerTypeCode);

        when(markupDOBuilder.buildMarkupWrapperDO(markupWrapper, contractPriceProfileSeq, userName)).thenReturn(markupWrapperDO);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgCustomerResponseDTO);
        when(customerItemPriceRepository.fetchSellPriceIndCount(contractPriceProfileSeq)).thenReturn(1);
        when(customerItemDescPriceRepository.fetchMarkupAmountTypeCodeCount(contractPriceProfileSeq)).thenReturn(1);
        when(prcProfPricingRuleOvrdRepository.fetchSellPriceCount(eq(1))).thenReturn(0);

        target.saveMarkup(markupWrapper, userName);

        verify(futureItemUpdater, never()).saveFutureItems(anyString(), anyInt(), anyInt(), anyListOf(FutureItemDO.class), anyString());

    }

    @Test
    public void shouldUpdateMarkupIfExisting() throws ParseException, CustomerServiceException {
        ProductTypeMarkupDTO markup = new ProductTypeMarkupDTO();
        List<ProductTypeMarkupDTO> productTypeMarkupList = new ArrayList<>();
        markup.setMarkup("10.43");
        markup.setUnit("$");
        markup.setEffectiveDate(pricingEffectiveDate);
        markup.setExpirationDate(pricingExpirationDate);
        markup.setItemPriceId(1);
        markup.setProductType("2");
        markup.setMarkupType(2);
        productTypeMarkupList.add(markup);

        List<ProductTypeMarkupDTO> savedMarkupList = new ArrayList<>();
        savedMarkupList.add(markup);

        ProductTypeMarkupDO markupDO1 = new ProductTypeMarkupDO();
        List<ProductTypeMarkupDO> markupDOList = new ArrayList<>();
        markupDO1.setMarkup(new BigDecimal("11.43"));
        markupDO1.setUnit("%");
        markupDO1.setEffectiveDate(new Date());
        markupDO1.setExpirationDate(new Date());
        markupDO1.setItemPriceId(1);
        markupDO1.setProductType("2");
        markupDO1.setMarkupType(2);
        markupDOList.add(markupDO1);

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        List<FutureItemDO> futureItemList = new ArrayList<>();

        markupWrapper.setIsMarkupSaved(false);
        markupWrapper.setGfsCustomerId("2");
        markupWrapper.setMarkupName("Test");
        markupWrapper.setContractPriceProfileSeq(124);
        markupWrapper.setProductMarkupList(productTypeMarkupList);

        MarkupWrapperDO markupWrapperDO = new MarkupWrapperDO();
        markupWrapperDO.setContractPriceProfileSeq(124);
        markupWrapperDO.setMarkupList(markupDOList);
        markupWrapperDO.setUserName("v8cq5");
        markupWrapperDO.setFutureItemList(futureItemList);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("124");
        cmgCustomerResponseDTO.setTypeCode(31);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(124)).thenReturn(cmgCustomerResponseDTO);
        doNothing().when(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(124);

        target.saveMarkup(markupWrapper, "v8cq5");

        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(124);
    }

    @Test
    public void shouldAddExceptionMarkup() throws Exception {

        MarkupExceptionDTO markupExceptionDTO = new MarkupExceptionDTO();
        markupExceptionDTO.setContractPriceProfileSeq(124);
        markupExceptionDTO.setExceptionName("Test");

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setContractPriceProfileId(1);

        CMGCustomerRequestDTO cmgCustomerDetailDTO = new CMGCustomerRequestDTO();
        cmgCustomerDetailDTO.setGroupName("1-Test");
        cmgCustomerDetailDTO.setGroupDesc(CPPConstants.CUSTOMER_GROUP_CMG);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("124");
        cmgCustomerResponseDTO.setTypeCode(31);
        int cppCustomerSeq = 2;

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setMarkupName("Test");
        markupWrapper.setGfsCustomerId("124");
        markupWrapper.setIsMarkupSaved(false);
        markupWrapper.setContractPriceProfileSeq(124);
        markupWrapper.setGfsCustomerType(31);

        String userName = "vc71u";
        int contractPriceProfileSeq = 124;
        String exceptionName = "Test";

        when(contractPriceProfCustomerRepository.fetchCPPCustomerNextSequence()).thenReturn(cppCustomerSeq);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPricingResponseDTO);
        when(customerServiceProxy.createCMGCustomerGroup(contractPricingResponseDTO.getContractPriceProfileId(), exceptionName))
                .thenReturn(cmgCustomerResponseDTO);
        when(markupWrapperBuilder.buildDefaultMarkupWrapper(contractPriceProfileSeq, cmgCustomerResponseDTO.getId(), exceptionName,
                pricingEffectiveDate, pricingExpirationDate)).thenReturn(markupWrapper);

        MarkupWrapperDTO result = target.addExceptionMarkup(contractPriceProfileSeq, exceptionName, pricingEffectiveDate, pricingExpirationDate,
                userName);

        assertThat(result.getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(result.getGfsCustomerId(), equalTo("124"));
        assertThat(result.getGfsCustomerType(), equalTo(31));
        assertThat(result.getIsMarkupSaved(), equalTo(false));
        assertThat(result.getMarkupName(), equalTo("Test"));

        verify(contractPriceProfCustomerRepository).saveContractPriceProfCustomer(markupExceptionDTO.getContractPriceProfileSeq(),
                cmgCustomerResponseDTO.getId(), cmgCustomerResponseDTO.getTypeCode(), userName, 0, cppCustomerSeq);
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(markupExceptionDTO.getContractPriceProfileSeq());
        verify(customerServiceProxy).createCMGCustomerGroup(contractPricingResponseDTO.getContractPriceProfileId(), exceptionName);

    }

    @Test
    public void shouldSaveMarkupIndicators() throws Exception {
        MarkupRequestDTO markupRequest = new MarkupRequestDTO();
        markupRequest.setContractPriceProfileSeq(1);
        markupRequest.setEffectiveDate(pricingEffectiveDate);
        markupRequest.setExpirationDate(pricingExpirationDate);
        String userName = "vc71u";
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(1);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(eq(1))).thenReturn(cmgCustomerResponseDTO);

        target.saveMarkupIndicators(markupRequest, userName);

        verify(contractPriceProfileRepository).updateExpireLowerIndicator(1, false, userName);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(eq(1));
        verify(markupOnSellUpdater).saveOrUpdateMarkupOnSellIndicator(markupRequest, userName, cmgCustomerResponseDTO.getId(),
                cmgCustomerResponseDTO.getTypeCode());
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(markupRequest.getContractPriceProfileSeq());
    }

    @Test
    public void shouldUpdateMarkupIndicatorsIfExisting() throws Exception {
        MarkupRequestDTO markupRequest = new MarkupRequestDTO();
        markupRequest.setContractPriceProfileSeq(1);
        markupRequest.setEffectiveDate(pricingEffectiveDate);
        markupRequest.setExpirationDate(pricingExpirationDate);
        markupRequest.setExpireLower(true);
        String userName = "vc71u";
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(1);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(eq(1))).thenReturn(cmgCustomerResponseDTO);

        target.saveMarkupIndicators(markupRequest, userName);

        verify(contractPriceProfileRepository).updateExpireLowerIndicator(1, true, userName);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(eq(1));
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(markupRequest.getContractPriceProfileSeq());
    }

    @Test
    public void shouldRenameExceptionMarkup() throws Exception {
        String gfsCustomerId = "1050";
        int contractPriceProfileSeq = 1000;

        ExceptionMarkupRenameDTO renameExceptionDTO = new ExceptionMarkupRenameDTO();
        renameExceptionDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        renameExceptionDTO.setGfsCustomerId(gfsCustomerId);
        renameExceptionDTO.setExceptionName("Testing");
        renameExceptionDTO.setNewExceptionName("Updated");
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();

        when(customerServiceProxy.createCMGCustomerGroup(contractPriceProfileSeq, "Testing")).thenReturn(cmgCustomerResponseDTO);

        target.renameExceptionMarkup(renameExceptionDTO);

        verify(customerServiceProxy).updateCMGCustomer(CPPConstants.CUSTOMER_GROUP_CMG, contractPriceProfileSeq + "-Updated", gfsCustomerId);
    }

    @Test
    public void shouldDeleteExceptionMarkup() throws Exception {
        int contractPriceProfileSeq = 1;
        CMGCustomerResponseDTO cmgResponseDTO = new CMGCustomerResponseDTO();
        cmgResponseDTO.setId("1");
        cmgResponseDTO.setTypeCode(31);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgResponseDTO);
        when(contractPriceProfCustomerRepository.deleteCPPCustomer(Matchers.any(Integer.class), Matchers.any(String.class))).thenReturn(200);

        target.deleteExceptionMarkup(1, "15", "Test", "v8cq5");

        verify(customerItemPriceRepository, never()).deleteExceptionData(1, "Test");
        verify(contractPriceProfCustomerRepository, never()).deleteCPPCustomer(1, "Test");
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(contractPriceProfileSeq);
    }

    @Test
    public void shouldCreateDefaultItemLevelMarkup() throws Exception {
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setMarkupType(2);
        itemLevelMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        itemLevelMarkupDTO.setExpirationDate(pricingExpirationDate);
        itemLevelMarkupDTO.setUnit("$");
        when(markupDTOBuilder.buildDefaultItemLevelMarkupDTO(pricingExpirationDate, pricingEffectiveDate)).thenReturn(itemLevelMarkupDTO);

        ItemLevelMarkupDTO result = target.createDefaultItemLevelMarkup(1, pricingExpirationDate, pricingEffectiveDate);

        assertThat(result.getMarkupType(), equalTo(2));
        assertThat(result.getEffectiveDate(), equalTo(pricingEffectiveDate));
        assertThat(result.getExpirationDate(), equalTo(pricingExpirationDate));
        assertThat(result.isNoItemId(), equalTo(false));
        assertThat(result.getStockingCode(), equalTo(0));
        assertThat(result.getUnit(), equalTo("$"));
    }

    @Test
    public void shouldCreateDefaultMarkup() throws Exception {
        int contractPriceProfileSeq = 1;
        String exceptionName = "Test";
        String gfsCustomerId = "100";
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("100");

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setMarkupName(exceptionName);
        markupWrapper.setGfsCustomerId(gfsCustomerId);
        markupWrapper.setIsMarkupSaved(false);
        ProductTypeMarkupDTO markup = new ProductTypeMarkupDTO();
        List<ProductTypeMarkupDTO> markupList = new ArrayList<>();
        markup.setGfsCustomerId(gfsCustomerId);
        markup.setMarkup("");
        markup.setUnit("$");
        markup.setEffectiveDate(pricingEffectiveDate);
        markup.setExpirationDate(pricingExpirationDate);
        markup.setItemPriceId(1);
        markup.setProductType("test");
        markup.setMarkupType(2);
        markupList.add(markup);

        when(markupWrapperBuilder.buildDefaultMarkupWrapper(contractPriceProfileSeq, cmgCustomerResponseDTO.getId(), exceptionName,
                pricingEffectiveDate, pricingExpirationDate)).thenReturn(markupWrapper);

        MarkupWrapperDTO defaultMarkupList = target.fetchDefaultException(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate,
                exceptionName, gfsCustomerId);

        assertThat(defaultMarkupList.getMarkupName(), equalTo("Test"));

        verify(markupWrapperBuilder).buildDefaultMarkupWrapper(contractPriceProfileSeq, cmgCustomerResponseDTO.getId(), exceptionName,
                pricingEffectiveDate, pricingExpirationDate);
    }

    @Test
    public void shouldFetchSavedMarkups() throws Exception {
        int contractPriceProfileSeq = 1;
        List<MarkupWrapperDTO> markupWrapperList = new ArrayList<>();
        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setMarkupName("test");
        markupWrapper.setGfsCustomerId("1");
        markupWrapper.setIsMarkupSaved(true);
        markupWrapperList.add(markupWrapper);

        when(markupFetcher.fetchAllMarkups(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate)).thenReturn(markupWrapperList);

        List<MarkupWrapperDTO> savedMarkupList = target.fetchMarkups(1, pricingExpirationDate, pricingEffectiveDate);

        assertThat(savedMarkupList.size(), equalTo(1));
        assertThat(savedMarkupList.get(0).getMarkupName(), equalTo("test"));
        assertThat(savedMarkupList.get(0).getIsMarkupSaved(), equalTo(true));
        assertThat(savedMarkupList.get(0).getGfsCustomerId(), equalTo("1"));

        verify(markupFetcher).fetchAllMarkups(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate);
    }

    @Test
    public void shouldDeleteExistingItem() throws Exception {
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "13";
        int gfsCustomerType = 31;
        String itemId = "1";
        String itemDesc = "test";
        String userName = "v8cq5";
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);
        CMGCustomerResponseDTO cmgResponseDTO = new CMGCustomerResponseDTO();
        cmgResponseDTO.setId("1");
        cmgResponseDTO.setTypeCode(31);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgResponseDTO);
        target.deleteItemLevelMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemId, itemDesc, userName);

        verify(customerItemPriceRepository).deleteExistingItemOrSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemIdList,
                ItemPriceLevel.ITEM.getCode());
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(contractPriceProfileSeq);
        verify(markupOnSellUpdater).updateMarkupOnSell(contractPriceProfileSeq, userName);
    }

    @Test
    public void shouldDeleteFutureItem() throws Exception {
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "13";
        String itemId = "";
        int gfsCustomerType = 31;
        String itemDesc = "test";
        String userName = "v8cq5";
        CMGCustomerResponseDTO cmgResponseDTO = new CMGCustomerResponseDTO();
        cmgResponseDTO.setId("1");
        cmgResponseDTO.setTypeCode(31);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgResponseDTO);
        target.deleteItemLevelMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemId, itemDesc, userName);

        verify(futureItemDeleteHandler).deleteFutureItemWithAssignedItems(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemDesc);
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(contractPriceProfileSeq);
        verify(markupOnSellUpdater).updateMarkupOnSell(contractPriceProfileSeq, userName);
    }

    @Test
    public void shouldFetchMarkupOnSellTrue() throws Exception {
        int contractPriceProfileSeq = 1;

        when(prcProfPricingRuleOvrdRepository.fetchSellPriceCount(contractPriceProfileSeq)).thenReturn(1);
        when(prcProfPricingRuleOvrdRepository.fetchPrcProfOvrdInd(contractPriceProfileSeq)).thenReturn(1);
        when(contractPriceProfileRepository.fetchExpireLowerIndicator(contractPriceProfileSeq)).thenReturn(1);

        Map<String, Boolean> actual = target.fetchMarkupIndicators(contractPriceProfileSeq);

        assertThat(actual.get(CPPConstants.EXPIRE_LOWER), equalTo(true));
        assertThat(actual.get(CPPConstants.MARKUP_ON_SELL), equalTo(true));

        verify(prcProfPricingRuleOvrdRepository).fetchSellPriceCount(contractPriceProfileSeq);
        verify(prcProfPricingRuleOvrdRepository).fetchPrcProfOvrdInd(contractPriceProfileSeq);
        verify(contractPriceProfileRepository).fetchExpireLowerIndicator(contractPriceProfileSeq);
    }

    @Test
    public void shouldFetchMarkupOnSellFalse() throws Exception {
        int contractPriceProfileSeq = 1;

        when(prcProfPricingRuleOvrdRepository.fetchSellPriceCount(contractPriceProfileSeq)).thenReturn(1);
        when(prcProfPricingRuleOvrdRepository.fetchPrcProfOvrdInd(contractPriceProfileSeq)).thenReturn(0);
        when(contractPriceProfileRepository.fetchExpireLowerIndicator(contractPriceProfileSeq)).thenReturn(0);

        Map<String, Boolean> actual = target.fetchMarkupIndicators(contractPriceProfileSeq);

        assertThat(actual.get(CPPConstants.EXPIRE_LOWER), equalTo(false));
        assertThat(actual.get(CPPConstants.MARKUP_ON_SELL), equalTo(false));

        verify(prcProfPricingRuleOvrdRepository).fetchSellPriceCount(contractPriceProfileSeq);
        verify(prcProfPricingRuleOvrdRepository).fetchPrcProfOvrdInd(contractPriceProfileSeq);
        verify(contractPriceProfileRepository).fetchExpireLowerIndicator(contractPriceProfileSeq);
    }

    @Test
    public void shouldNotFetchMarkupOnSell() throws Exception {
        int contractPriceProfileSeq = 1;

        when(prcProfPricingRuleOvrdRepository.fetchSellPriceCount(contractPriceProfileSeq)).thenReturn(1);
        when(prcProfPricingRuleOvrdRepository.fetchPrcProfOvrdInd(contractPriceProfileSeq)).thenReturn(0);
        when(contractPriceProfileRepository.fetchExpireLowerIndicator(contractPriceProfileSeq)).thenReturn(null);

        Map<String, Boolean> actual = target.fetchMarkupIndicators(contractPriceProfileSeq);

        assertThat(actual.get(CPPConstants.EXPIRE_LOWER), equalTo(null));
        assertThat(actual.get(CPPConstants.MARKUP_ON_SELL), equalTo(false));

        verify(prcProfPricingRuleOvrdRepository).fetchSellPriceCount(contractPriceProfileSeq);
        verify(prcProfPricingRuleOvrdRepository).fetchPrcProfOvrdInd(contractPriceProfileSeq);
        verify(contractPriceProfileRepository).fetchExpireLowerIndicator(contractPriceProfileSeq);
    }

    @Test
    public void shouldCreateDefaultSubgroup() {

        SubgroupMarkupDTO subgroupMarkupDTO = buildDefaultSubgroupMarkupDTO(pricingExpirationDate, pricingEffectiveDate);

        when(markupDTOBuilder.buildDefaultSubgroupMarkupDTO(pricingExpirationDate, pricingEffectiveDate)).thenReturn(subgroupMarkupDTO);

        SubgroupMarkupDTO result = target.createDefaultSubgroupMarkup(pricingExpirationDate, pricingEffectiveDate);

        assertThat(result.getMarkupType(), equalTo(2));
        assertThat(result.getUnit(), equalTo("$"));
        assertThat(result.getEffectiveDate(), equalTo(pricingEffectiveDate));
        assertThat(result.getExpirationDate(), equalTo(pricingExpirationDate));
        assertThat(result.getIsSubgroupSaved(), equalTo(false));

    }

    @Test
    public void shouldFindItemSubgroupInformation() {
        String subgroupId = "11";
        String cmgCustomerId = "12345";
        int cmgCustomerTypeCode = 2;
        int contractPriceProfileSeq = 1000;
        String subgroupDescription = "TABLETOP MISC";

        SubgroupResponseDTO subgroupResponseDTO = new SubgroupResponseDTO();
        subgroupResponseDTO.setSubgroupDesc("TABLETOP MISC");
        subgroupResponseDTO.setSubgroupId(subgroupId);
        subgroupResponseDTO.setValidationCode(HttpStatus.OK.value());
        subgroupResponseDTO.setValidationMessage(StringUtils.EMPTY);

        when(itemConfigurationServiceProxy.getSubgroupDescriptionById(subgroupId)).thenReturn(subgroupDescription);
        when(subgroupResponseDTOBuilder.buildSubgroupResponseDTO(subgroupId, subgroupResponseDTO.getSubgroupDesc(),
                subgroupResponseDTO.getValidationCode(), subgroupResponseDTO.getValidationMessage())).thenReturn(subgroupResponseDTO);

        SubgroupResponseDTO result = target.findItemSubgroupInformation(subgroupId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq);

        assertThat(result.getSubgroupId(), equalTo(subgroupId));
        assertThat(result.getSubgroupDesc(), equalTo(subgroupDescription));
        assertThat(result.getValidationCode(), equalTo(HttpStatus.OK.value()));
        assertThat(result.getValidationMessage(), equalTo(StringUtils.EMPTY));

        verify(subgroupValidator).validateIfSubgroupAlreadyExist(cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq, subgroupId);
        verify(itemConfigurationServiceProxy).getSubgroupDescriptionById(subgroupId);
        verify(subgroupValidator).validateIfInvalidSubgroup(subgroupDescription, subgroupId);
    }

    @Test
    public void shouldThrowExceptionIfSubgroupIsInvalid() {
        String subgroupId = "12";
        String cmgCustomerId = "12345";
        int cmgCustomerTypeCode = 2;
        int contractPriceProfileSeq = 1000;
        String subgroupDescription = "TABLETOP MISC";
        SubgroupResponseDTO subgroupResponseDTO = new SubgroupResponseDTO();
        subgroupResponseDTO.setSubgroupDesc(StringUtils.EMPTY);
        subgroupResponseDTO.setSubgroupId(subgroupId);
        subgroupResponseDTO.setValidationCode(CPPExceptionType.INVALID_SUBGROUP.getErrorCode());
        subgroupResponseDTO.setValidationMessage("Invalid Subgroup found");

        when(itemConfigurationServiceProxy.getSubgroupDescriptionById(subgroupId)).thenReturn(subgroupDescription);
        doThrow(new CPPRuntimeException(CPPExceptionType.INVALID_SUBGROUP, "Invalid Subgroup found")).when(subgroupValidator)
                .validateIfInvalidSubgroup(subgroupDescription, subgroupId);
        when(subgroupResponseDTOBuilder.buildSubgroupResponseDTO(subgroupId, subgroupResponseDTO.getSubgroupDesc(),
                subgroupResponseDTO.getValidationCode(), subgroupResponseDTO.getValidationMessage())).thenReturn(subgroupResponseDTO);

        SubgroupResponseDTO result = target.findItemSubgroupInformation(subgroupId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq);

        assertThat(result.getSubgroupDesc(), equalTo(StringUtils.EMPTY));
        assertThat(result.getValidationCode(), is(CPPExceptionType.INVALID_SUBGROUP.getErrorCode()));

        verify(itemConfigurationServiceProxy).getSubgroupDescriptionById(subgroupId);
        verify(subgroupValidator).validateIfInvalidSubgroup(subgroupDescription, subgroupId);
        verify(subgroupResponseDTOBuilder).buildSubgroupResponseDTO(subgroupId, subgroupResponseDTO.getSubgroupDesc(),
                subgroupResponseDTO.getValidationCode(), subgroupResponseDTO.getValidationMessage());
    }

    @Test
    public void shouldThrowExceptionIfSubgroupAlreadyExist() {
        String subgroupId = "12";
        String cmgCustomerId = "12345";
        int cmgCustomerTypeCode = 2;
        int contractPriceProfileSeq = 1000;
        Map<String, String> subgroupInformationMap = new HashMap<>();
        subgroupInformationMap.put("12", "TABLETOP MISC");

        SubgroupResponseDTO subgroupResponseDTO = new SubgroupResponseDTO();
        subgroupResponseDTO.setSubgroupDesc(StringUtils.EMPTY);
        subgroupResponseDTO.setSubgroupId(subgroupId);
        subgroupResponseDTO.setValidationCode(CPPExceptionType.SUBGROUP_ALREADY_EXIST.getErrorCode());
        subgroupResponseDTO.setValidationMessage("Duplicate Subgroup found");

        doThrow(new CPPRuntimeException(CPPExceptionType.SUBGROUP_ALREADY_EXIST, "Duplicate Subgroup found")).when(subgroupValidator)
                .validateIfSubgroupAlreadyExist(cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq, subgroupId);
        when(subgroupResponseDTOBuilder.buildSubgroupResponseDTO(subgroupId, subgroupResponseDTO.getSubgroupDesc(),
                subgroupResponseDTO.getValidationCode(), subgroupResponseDTO.getValidationMessage())).thenReturn(subgroupResponseDTO);

        SubgroupResponseDTO result = target.findItemSubgroupInformation(subgroupId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq);

        assertThat(result.getSubgroupDesc(), equalTo(StringUtils.EMPTY));
        assertThat(result.getValidationCode(), is(CPPExceptionType.SUBGROUP_ALREADY_EXIST.getErrorCode()));

        verify(subgroupValidator).validateIfSubgroupAlreadyExist(cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq, subgroupId);
        verify(subgroupResponseDTOBuilder).buildSubgroupResponseDTO(subgroupId, subgroupResponseDTO.getSubgroupDesc(),
                subgroupResponseDTO.getValidationCode(), subgroupResponseDTO.getValidationMessage());

    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldThrowExceptionOnFindubgroup() {
        String subgroupId = "12";
        String cmgCustomerId = "12345";
        int cmgCustomerTypeCode = 2;
        int contractPriceProfileSeq = 1000;
        Map<String, String> subgroupInformationMap = new HashMap<>();
        subgroupInformationMap.put("11", "TABLETOP MISC");

        doThrow(new CPPRuntimeException(CPPExceptionType.SERVICE_FAILED)).when(subgroupValidator).validateIfSubgroupAlreadyExist(cmgCustomerId,
                cmgCustomerTypeCode, contractPriceProfileSeq, subgroupId);

        target.findItemSubgroupInformation(subgroupId, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq);

        verify(subgroupValidator).validateIfSubgroupAlreadyExist(cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq, subgroupId);
    }

    private SubgroupMarkupDTO buildDefaultSubgroupMarkupDTO(Date pricingExpirationDate, Date pricingEffectiveDate) {
        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        subgroupMarkupDTO.setExpirationDate(pricingExpirationDate);
        subgroupMarkupDTO.setSubgroupDesc(StringUtils.EMPTY);
        subgroupMarkupDTO.setSubgroupId(StringUtils.EMPTY);
        subgroupMarkupDTO.setMarkup(StringUtils.EMPTY);
        subgroupMarkupDTO.setMarkupType(PricingUnitType.CASE.getCode());
        subgroupMarkupDTO.setUnit(AmountType.DOLLAR.getCode());
        subgroupMarkupDTO.setIsSubgroupSaved(false);
        return subgroupMarkupDTO;
    }

    @Test
    public void shouldDeleteExistingSubgroup() throws Exception {
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "13";
        int gfsCustomerType = 31;
        String subgroupId = "1";
        String userName = "v8cq5";
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(subgroupId);
        CMGCustomerResponseDTO cmgResponseDTO = new CMGCustomerResponseDTO();
        cmgResponseDTO.setId("1");
        cmgResponseDTO.setTypeCode(31);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgResponseDTO);
        target.deleteSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, subgroupId, userName);

        verify(customerItemPriceRepository).deleteExistingItemOrSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemIdList,
                ItemPriceLevel.SUBGROUP.getCode());
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(contractPriceProfileSeq);
        verify(markupOnSellUpdater).updateMarkupOnSell(contractPriceProfileSeq, userName);
    }

    @Test
    public void shouldNotDeleteSubgroupWhenNoSubgroupIdFound() throws Exception {
        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "13";
        int gfsCustomerType = 31;
        String subgroupId = "";
        String userName = "v8cq5";

        target.deleteSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, subgroupId, userName);

        verify(customerItemPriceRepository, never()).deleteExistingItemOrSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType,
                new ArrayList<String>(), ItemPriceLevel.SUBGROUP.getCode());

    }

}
