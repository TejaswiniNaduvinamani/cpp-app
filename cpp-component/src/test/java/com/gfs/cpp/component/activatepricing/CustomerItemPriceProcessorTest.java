package com.gfs.cpp.component.activatepricing;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.component.activatepricing.CustomerItemPriceProcessor;
import com.gfs.cpp.component.activatepricing.CustomerItemPricingCopier;
import com.gfs.cpp.component.clm.ContractPriceExpirer;
import com.gfs.cpp.component.contractpricing.CustomerItemPriceExpirer;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class CustomerItemPriceProcessorTest {

    @InjectMocks
    private CustomerItemPriceProcessor target;

    @Mock
    private ContractPriceExpirer contractPriceExpirer;

    @Mock
    private CustomerItemPricingCopier customerItemPricingCopier;

    @Mock
    private CustomerItemPriceExpirer customerItemPriceExpirer;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Test
    public void shouldExpireAndSaveCustomerItemPriceProfile() {

        int latestActivatedPricingCPPSeq = 1;
        Date newEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date newExpirationDate = new LocalDate(2019, 01, 01).toDate();
        Date expirationDateForExistingPricing = new LocalDate(2017, 12, 31).toDate();
        ContractPricingResponseDTO contractDetails = getContractPricingResponseDTO();
        int contractPriceProfileSeq = 1;
        String userId = "Test";
        int itemPriceId = 2;
        String customerId = "Customer ID";
        int customerTypeCode = 22;

        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setGfsCustomerId(customerId);
        productTypeMarkupDO.setItemPriceId(itemPriceId);
        productTypeMarkupDO.setProductType("1");
        productTypeMarkupDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDO.setCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDO> customerItemMappingWithNoExistingBidEntries = Collections.singletonList(productTypeMarkupDO);

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = Collections.singletonList(productTypeMarkupDTO);

        when(customerItemPriceRepository.fetchPricingForRealCustomer(contractPriceProfileSeq)).thenReturn(productTypeMarkupDTOList);

        when(customerItemPricingCopier.extractItemAndCustomerMappingWithNoExistingBidEntries(contractPriceProfileSeq, userId, newEffectiveDate,
                newExpirationDate, productTypeMarkupDTOList)).thenReturn(customerItemMappingWithNoExistingBidEntries);

        target.expireAndSaveCustomerItemPriceProfile(contractPriceProfileSeq, latestActivatedPricingCPPSeq, userId, expirationDateForExistingPricing,
                contractDetails.getPricingEffectiveDate(), contractDetails.getPricingExpirationDate());

        verify(contractPriceExpirer).expireCustomerItemPricingForCPPSequence(latestActivatedPricingCPPSeq, expirationDateForExistingPricing, userId);
        verify(customerItemPriceExpirer).expireItemPricingForRealCustomer(expirationDateForExistingPricing, userId,
                customerItemMappingWithNoExistingBidEntries, newEffectiveDate, newExpirationDate);

        verify(customerItemPricingCopier).saveNonBidItemAndCustomerMapping(contractPriceProfileSeq, userId,
                customerItemMappingWithNoExistingBidEntries);
    }

    @Test
    public void shouldExpireItemPricingForRealCustomer() {

        String userId = "Test";
        String customerId = "Customer ID";
        int customerTypeCode = 22;
        int itemPriceId = 2;
        int contractPriceProfileSeq = 1;
        Date expirationDateForExistingPricing = new LocalDate(2017, 12, 31).toDate();
        Date newEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date newExpirationDate = new LocalDate(2019, 01, 01).toDate();

        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setGfsCustomerId(customerId);
        productTypeMarkupDO.setItemPriceId(itemPriceId);
        productTypeMarkupDO.setProductType("1");
        productTypeMarkupDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDO.setCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDO> customerItemMappingWithNoExistingBidEntries = Collections.singletonList(productTypeMarkupDO);

        target.expireItemPricingForRealCustomer(expirationDateForExistingPricing, userId, customerItemMappingWithNoExistingBidEntries,
                newEffectiveDate, newExpirationDate);

        verify(customerItemPriceExpirer).expireItemPricingForRealCustomer(expirationDateForExistingPricing, userId,
                customerItemMappingWithNoExistingBidEntries, newEffectiveDate, newExpirationDate);

    }

    @Test
    public void shouldSavePricingForRealCustomers() {
        int contractPriceProfileSeq = 1;
        String userId = "Test";
        int itemPriceId = 2;
        String customerId = "Customer ID";
        int customerTypeCode = 22;
        int latestActivatedPricingCPPSeq = 1;
        String lastUpdateUserId = "Test";

        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setGfsCustomerId(customerId);
        productTypeMarkupDO.setItemPriceId(itemPriceId);
        productTypeMarkupDO.setProductType("1");
        productTypeMarkupDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDO.setCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDO> customerItemMappingWithNoExistingBidEntries = Collections.singletonList(productTypeMarkupDO);

        target.savePricingForRealCustomers(latestActivatedPricingCPPSeq, lastUpdateUserId, customerItemMappingWithNoExistingBidEntries);

        verify(customerItemPricingCopier).saveNonBidItemAndCustomerMapping(contractPriceProfileSeq, userId,
                customerItemMappingWithNoExistingBidEntries);
    }

    @Test
    public void shouldExpireCustomerItemPricingForCPPSequence() {
        int latestActivatedPricingCPPSeq = 1;
        String lastUpdateUserId = "test";
        Date expirationDate = new LocalDate(2017, 12, 31).toDate();
        target.expireCustomerItemPricingForCPPSequence(latestActivatedPricingCPPSeq, lastUpdateUserId, expirationDate);

        verify(contractPriceExpirer).expireCustomerItemPricingForCPPSequence(latestActivatedPricingCPPSeq, expirationDate, lastUpdateUserId);
    }

    @Test
    public void shouldNotExpireCustomerItemPricingForCPPSequence() {
        int latestActivatedPricingCPPSeq = -1;
        String lastUpdateUserId = "test";
        Date expirationDate = new LocalDate(2017, 12, 31).toDate();
        target.expireCustomerItemPricingForCPPSequence(latestActivatedPricingCPPSeq, lastUpdateUserId, expirationDate);

        verify(contractPriceExpirer, never()).expireCustomerItemPricingForCPPSequence(latestActivatedPricingCPPSeq, expirationDate, lastUpdateUserId);
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

    @Test
    public void shouldExpireAndSaveForMarkupToBeAdded() {
        Date newEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date newExpirationDate = new LocalDate(2019, 01, 01).toDate();
        Date expirationDateForExistingPricing = new LocalDate(2017, 12, 31).toDate();
        ContractPricingResponseDTO contractDetails = getContractPricingResponseDTO();
        int contractPriceProfileSeq = 1;
        String userId = "Test";
        int itemPriceId = 2;
        String customerId = "Customer ID";
        int customerTypeCode = 22;

        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setGfsCustomerId(customerId);
        productTypeMarkupDO.setItemPriceId(itemPriceId);
        productTypeMarkupDO.setProductType("1");
        productTypeMarkupDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDO.setCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDO> customerItemMappingWithNoExistingBidEntries = Collections.singletonList(productTypeMarkupDO);

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = Collections.singletonList(productTypeMarkupDTO);

        when(customerItemPriceRepository.fetchPricingForRealCustomer(contractPriceProfileSeq)).thenReturn(productTypeMarkupDTOList);

        when(customerItemPricingCopier.extractItemAndCustomerMappingWithNoExistingBidEntries(contractPriceProfileSeq, userId, newEffectiveDate,
                newExpirationDate, productTypeMarkupDTOList)).thenReturn(customerItemMappingWithNoExistingBidEntries);

        target.expireAndSaveForMarkupToBeAdded(contractPriceProfileSeq, userId, expirationDateForExistingPricing,
                contractDetails.getPricingEffectiveDate(), contractDetails.getPricingExpirationDate(), productTypeMarkupDTOList);

        verify(customerItemPricingCopier).extractItemAndCustomerMappingWithNoExistingBidEntries(contractPriceProfileSeq, userId, newEffectiveDate,
                newExpirationDate, productTypeMarkupDTOList);

        verify(customerItemPriceExpirer).expireItemPricingForRealCustomer(expirationDateForExistingPricing, userId,
                customerItemMappingWithNoExistingBidEntries, newEffectiveDate, newExpirationDate);

        verify(customerItemPricingCopier).saveNonBidItemAndCustomerMapping(contractPriceProfileSeq, userId,
                customerItemMappingWithNoExistingBidEntries);

    }

}
