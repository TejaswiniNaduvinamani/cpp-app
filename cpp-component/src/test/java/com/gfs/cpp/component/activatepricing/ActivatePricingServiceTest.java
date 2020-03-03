package com.gfs.cpp.component.activatepricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.ActivatePricingService;
import com.gfs.cpp.component.activatepricing.ActivatePricingValidator;
import com.gfs.cpp.component.activatepricing.ContractCustomerMappingService;
import com.gfs.cpp.component.activatepricing.CustomerItemPriceProcessor;
import com.gfs.cpp.component.activatepricing.CustomerItemPricingCopier;
import com.gfs.cpp.component.activatepricing.CustomerPriceProfileProcessor;
import com.gfs.cpp.component.activatepricing.CustomerPricingCopier;
import com.gfs.cpp.component.clm.ContractPriceExpirer;
import com.gfs.cpp.component.contractpricing.ContractPriceProfileExpirer;
import com.gfs.cpp.component.contractpricing.CustomerItemPriceExpirer;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class ActivatePricingServiceTest {

    @InjectMocks
    private ActivatePricingService target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private ContractCustomerMappingService contractCustomerMappingService;

    @Mock
    private CustomerItemPriceExpirer customerItemPriceExpirer;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private ContractPriceProfileExpirer contractPriceProfileExpirer;

    @Mock
    private CustomerPricingCopier customerPricingCopier;

    @Mock
    private ActivatePricingValidator activatePricingValidator;

    @Captor
    private ArgumentCaptor<List<ProductTypeMarkupDO>> productTypeMarkupDOCaptor;

    @Mock
    private CustomerItemPricingCopier customerItemPricingCopier;

    @Mock
    private ContractPriceExpirer contractPriceExpirer;

    @Mock
    private CustomerPriceProfileProcessor customerPriceProfileProcessor;

    @Mock
    private CustomerItemPriceProcessor customerItemPriceProcessor;

    @Test
    public void shouldActivatePricingForAmendment() throws ParseException {
        int contractPriceProfileSeq = 1;
        int cppSeqForLatestPricingActivated = 11;
        boolean isAmendment = true;
        String clmContractStatus = ClmContractStatus.EXECUTED.value;
        String userId = "Test";
        int itemPriceId = 2;
        String customerId = "Customer ID";
        int customerTypeCode = 22;
        String parentAgreementId = "ParentAgreementId";
        Date expirationDateForExistingPricing = new LocalDate(2017, 12, 31).toDate();
        Date newEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        ContractPricingResponseDTO contractDetails = getContractPricingResponseDTO();

        Date pricingEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date pricingExpirationDate = new LocalDate(2019, 01, 01).toDate();

        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = getContractCustomerMappingDTOList();

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = Collections.singletonList(productTypeMarkupDTO);

        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setGfsCustomerId(customerId);
        productTypeMarkupDO.setItemPriceId(itemPriceId);
        productTypeMarkupDO.setProductType("1");
        productTypeMarkupDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDO.setCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDO> customerItemMappingWithNoExistingBidEntries = Collections.singletonList(productTypeMarkupDO);

        ContractPricingResponseDTO contractPricingResponseDTOForParentAgreement = getContractPricingResponseDTOForParentAgreement();

        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractDetails);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(buildCMGCustomerResponseDTO());
        when(contractCustomerMappingService.fetchAllConceptCustomerMapping(contractPriceProfileSeq)).thenReturn(contractCustomerMappingDTOList);
        when(cppDateUtils.getCurrentDateAsLocalDate()).thenReturn(new LocalDate(2018, 01, 01));
        when(cppDateUtils.getPreviousDate(newEffectiveDate)).thenReturn(expirationDateForExistingPricing);
        when(customerItemPriceRepository.fetchPricingForRealCustomer(contractPriceProfileSeq)).thenReturn(productTypeMarkupDTOList);
        when(customerItemPricingCopier.extractItemAndCustomerMappingWithNoExistingBidEntries(contractPriceProfileSeq, userId, pricingEffectiveDate,
                pricingExpirationDate, productTypeMarkupDTOList)).thenReturn(customerItemMappingWithNoExistingBidEntries);
        when(cppDateUtils.getCurrentDate()).thenReturn(new LocalDate(2018, 01, 01).toDate());
        doReturn(contractPricingResponseDTOForParentAgreement).when(contractPriceProfileRepository)
                .fetchContractDetailsForLatestActivatedContractVersion(eq(parentAgreementId));

        target.activatePricing(contractPriceProfileSeq, userId, isAmendment, clmContractStatus);

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(contractCustomerMappingService).fetchAllConceptCustomerMapping(contractPriceProfileSeq);
        verify(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(eq(parentAgreementId));
        verify(customerPriceProfileProcessor).expireAndSavePriceProfile(cppSeqForLatestPricingActivated, userId, expirationDateForExistingPricing,
                contractDetails, contractCustomerMappingDTOList);
        verify(customerItemPriceProcessor).expireAndSaveCustomerItemPriceProfile(contractPriceProfileSeq, cppSeqForLatestPricingActivated, userId,
                expirationDateForExistingPricing, contractDetails.getPricingEffectiveDate(), contractDetails.getPricingExpirationDate());

    }

    @Test
    public void shouldActivatePricingForContract() throws ParseException {
        int contractPriceProfileSeq = 1;
        int cppSeqForLatestPricingActivated = -1;
        boolean isAmendment = false;
        String clmContractStatus = ClmContractStatus.EXECUTED.value;
        String userId = "Test";
        int itemPriceId = 2;
        String customerId = "Customer ID";
        int customerTypeCode = 22;
        Date newEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date expirationDateForExistingPricing = new LocalDate(2017, 12, 31).toDate();
        Date pricingEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date pricingExpirationDate = new LocalDate(2019, 01, 01).toDate();

        ContractPricingResponseDTO contractDetails = getContractPricingResponseDTO();

        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = getContractCustomerMappingDTOList();

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = Collections.singletonList(productTypeMarkupDTO);

        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setGfsCustomerId(customerId);
        productTypeMarkupDO.setItemPriceId(itemPriceId);
        productTypeMarkupDO.setProductType("1");
        productTypeMarkupDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDO.setCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDO> customerItemMappingWithNoExistingBidEntries = Collections.singletonList(productTypeMarkupDO);

        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractDetails);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(buildCMGCustomerResponseDTO());
        when(contractCustomerMappingService.fetchAllConceptCustomerMapping(contractPriceProfileSeq)).thenReturn(contractCustomerMappingDTOList);
        when(cppDateUtils.getCurrentDateAsLocalDate()).thenReturn(new LocalDate(2018, 01, 01));
        when(cppDateUtils.getPreviousDate(newEffectiveDate)).thenReturn(expirationDateForExistingPricing);
        when(customerItemPriceRepository.fetchPricingForRealCustomer(contractPriceProfileSeq)).thenReturn(productTypeMarkupDTOList);
        when(customerItemPricingCopier.extractItemAndCustomerMappingWithNoExistingBidEntries(contractPriceProfileSeq, userId, pricingEffectiveDate,
                pricingExpirationDate, productTypeMarkupDTOList)).thenReturn(customerItemMappingWithNoExistingBidEntries);
        when(cppDateUtils.getCurrentDate()).thenReturn(new LocalDate(2018, 01, 01).toDate());
        target.activatePricing(contractPriceProfileSeq, userId, isAmendment, clmContractStatus);

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(contractCustomerMappingService).fetchAllConceptCustomerMapping(contractPriceProfileSeq);
        verify(customerPriceProfileProcessor).expireAndSavePriceProfile(cppSeqForLatestPricingActivated, userId, expirationDateForExistingPricing,
                contractDetails, contractCustomerMappingDTOList);
        verify(customerItemPriceProcessor).expireAndSaveCustomerItemPriceProfile(contractPriceProfileSeq, cppSeqForLatestPricingActivated, userId,
                expirationDateForExistingPricing, contractDetails.getPricingEffectiveDate(), contractDetails.getPricingExpirationDate());

    }

    @Test
    public void shouldValidateActivatePricingEnabler() throws Exception {
        int contractPriceProfileSeq = 1;
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();

        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractDetails);

        Map<String, Boolean> actual = target.validateActivatePricingEnabler(contractPriceProfileSeq);

        assertThat(actual.get(CPPConstants.ENABLE_ACTIVATE_PRICING_BUTTON), equalTo(true));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
    }

    @Test
    public void shouldNotEnableActivatePricing() throws Exception {
        int contractPriceProfileSeq = 1;
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();

        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractDetails);

        doThrow(new CPPRuntimeException(null)).when(activatePricingValidator).validateIfActivatePricingCanBeEnabled(contractPriceProfileSeq,
                contractDetails);

        Map<String, Boolean> actual = target.validateActivatePricingEnabler(contractPriceProfileSeq);

        assertThat(actual.get(CPPConstants.ENABLE_ACTIVATE_PRICING_BUTTON), equalTo(false));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
    }

    @Test
    public void shouldValidateStatusAndCustomerHierachy() throws Exception {
        int contractPriceProfileSeq = 1;
        boolean isAmendment = true;
        String clmContractStatus = ClmContractStatus.EXECUTED.value;
        String parentAgreementId = "ParentAgreementId";
        String userId = "Test";
        int itemPriceId = 2;
        String customerId = "Customer ID";
        int customerTypeCode = 22;
        Date effectiveDate = new LocalDate(2099, 01, 01).toDate();
        Date pricingEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date pricingExpirationDate = new LocalDate(2019, 01, 01).toDate();

        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = new ArrayList<>();

        ContractPricingResponseDTO contractDetails = getContractPricingResponseDTO();
        contractDetails.setPricingEffectiveDate(effectiveDate);

        ContractPricingResponseDTO contractPricingResponseDTOForParentAgreement = getContractPricingResponseDTOForParentAgreement();

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = Collections.singletonList(productTypeMarkupDTO);

        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setGfsCustomerId(customerId);
        productTypeMarkupDO.setItemPriceId(itemPriceId);
        productTypeMarkupDO.setProductType("1");
        productTypeMarkupDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDO.setCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDO> customerItemMappingWithNoExistingBidEntries = Collections.singletonList(productTypeMarkupDO);

        when(customerItemPriceRepository.fetchPricingForRealCustomer(contractPriceProfileSeq)).thenReturn(productTypeMarkupDTOList);
        when(customerItemPricingCopier.extractItemAndCustomerMappingWithNoExistingBidEntries(contractPriceProfileSeq, userId, pricingEffectiveDate,
                pricingExpirationDate, productTypeMarkupDTOList)).thenReturn(customerItemMappingWithNoExistingBidEntries);

        doReturn(contractDetails).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        doReturn(contractCustomerMappingDTOList).when(contractCustomerMappingService).fetchAllConceptCustomerMapping(contractPriceProfileSeq);
        doReturn(contractPricingResponseDTOForParentAgreement).when(contractPriceProfileRepository)
                .fetchContractDetailsForLatestActivatedContractVersion(eq(parentAgreementId));
        when(cppDateUtils.getCurrentDate()).thenReturn(new LocalDate(2018, 01, 01).toDate());
        target.activatePricing(contractPriceProfileSeq, userId, isAmendment, clmContractStatus);

        verify(activatePricingValidator).validateContract(eq(contractPriceProfileSeq), eq(contractDetails), any(Date.class), eq(isAmendment),
                eq(clmContractStatus));
        verify(activatePricingValidator).validateCustomerMembershipWithDefaultCustomerMapping(eq(contractPriceProfileSeq),
                eq(contractCustomerMappingDTOList));
        verify(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(eq(parentAgreementId));

    }

    @Test
    public void shouldFetchCPPSequenceForLatestActivatedVersion() {
        String parentAgreementId = "ParentAgreementId";
        ContractPricingResponseDTO contractPricingResponseDTOForParentAgreement = getContractPricingResponseDTOForParentAgreement();

        doReturn(contractPricingResponseDTOForParentAgreement).when(contractPriceProfileRepository)
                .fetchContractDetailsForLatestActivatedContractVersion(eq(parentAgreementId));

        target.fetchCPPSequenceForLatestActivatedVersion(parentAgreementId);

        verify(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(eq(parentAgreementId));
    }

    private CMGCustomerResponseDTO buildCMGCustomerResponseDTO() {
        CMGCustomerResponseDTO contractLevelConcept = new CMGCustomerResponseDTO();
        contractLevelConcept.setDefaultCustomerInd(1);
        contractLevelConcept.setId("Customer ID");
        contractLevelConcept.setTypeCode(31);
        return contractLevelConcept;
    }

    private ContractPricingResponseDTO getContractPricingResponseDTOForParentAgreement() {
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("ParentAgreementId");
        contractDetails.setClmContractTypeSeq(11);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(11);
        contractDetails.setContractPriceProfStatusCode(50);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 01).toDate());
        contractDetails.setClmParentAgreementId("ParentAgreementId");
        return contractDetails;
    }

    private ContractPricingResponseDTO getContractPricingResponseDTO() {
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 01).toDate());
        contractDetails.setClmParentAgreementId("ParentAgreementId");
        return contractDetails;
    }

    private List<ContractCustomerMappingDTO> getContractCustomerMappingDTOList() {
        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = new ArrayList<ContractCustomerMappingDTO>();
        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("Customer ID");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(31);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);
        contractCustomerMappingDTOList.add(contractCustomerMappingDTO);

        return contractCustomerMappingDTOList;
    }

}
