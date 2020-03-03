package com.gfs.cpp.component.customerinfo;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.assignments.ItemAssignmentDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.component.customerinfo.ContractItemCopier;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class ContractItemCopierTest {

    @InjectMocks
    private ContractItemCopier target;

    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Mock
    private CppItemMappingRepository cppItemMappingRepository;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private MarkupDOBuilder markupDOBuilder;

    private Date farOutDate;

    @Before
    public void setupFarOfDate() throws ParseException {
        farOutDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
    }

    @Test
    public void shouldCopyAllItemAndMappings() {

        int contractPriceProfileSequence = 3;
        int cppSeqForLatestContractVersion = 2;
        String userId = "Test";
        Date effectiveDate = new Date();
        Date expirationDate = new Date();
        int cppCustomerItemDescPriceNextSeq = 23;
        int existingCustomerItemDescPriceSeq = 22;
        List<Integer> existingCustomerItemDescPriceSeqList = new ArrayList<>();
        existingCustomerItemDescPriceSeqList.add(existingCustomerItemDescPriceSeq);
        int itemPriceId = 2;
        String customerId = "123456";
        Integer customerTypeCode = 0;

        FutureItemDescriptionDTO futureItemDescriptionDTO = new FutureItemDescriptionDTO();
        futureItemDescriptionDTO.setCustomerItemDescSeq(existingCustomerItemDescPriceSeq);
        futureItemDescriptionDTO.setFutureItemDesc("test");
        futureItemDescriptionDTO.setGfsCustomerId("1");
        futureItemDescriptionDTO.setGfsCustomerTypeCode(31);

        List<FutureItemDescriptionDTO> futuredItemDescriptionList = new ArrayList<>();
        futuredItemDescriptionList.add(futureItemDescriptionDTO);

        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("Test");
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTO.setItemId("1");
        itemAssignmentDTO.setEffectiveDate(effectiveDate);
        itemAssignmentDTO.setExpirationDate(farOutDate);
        itemAssignmentDTOList.add(itemAssignmentDTO);

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        List<ItemAssignmentDO> itemAssignmentDOList = new ArrayList<>();
        ItemAssignmentDO itemAssignmentDO = new ItemAssignmentDO();
        itemAssignmentDO.setCustomerItemDescSeq(cppCustomerItemDescPriceNextSeq);
        itemAssignmentDO.setItemPriceId("1");
        itemAssignmentDO.setEffectiveDate(effectiveDate);
        itemAssignmentDO.setExpirationDate(farOutDate);
        itemAssignmentDOList.add(itemAssignmentDO);

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = Collections.singletonList(productTypeMarkupDTO);

        List<ProductTypeMarkupDO> productTypeMarkupDOList = buildProductTypeMarkupDOList();

        when(customerItemDescPriceRepository.fetchAllFutureItems(cppSeqForLatestContractVersion)).thenReturn(futuredItemDescriptionList);
        when(customerItemDescPriceRepository.fetchCPPCustomerItemDescPriceNextSequence()).thenReturn(cppCustomerItemDescPriceNextSeq);
        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(existingCustomerItemDescPriceSeqList)).thenReturn(itemAssignmentDTOList);
        when(customerItemPriceRepository.fetchMarkupsForCMGs(cppSeqForLatestContractVersion)).thenReturn(productTypeMarkupDTOList);
        when(markupDOBuilder.buildProductMarkupDOListForAmendment(userId, contractPriceProfileSequence, farOutDate, productTypeMarkupDTOList))
                .thenReturn(productTypeMarkupDOList);

        target.copyAllItemAndMappings(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, expirationDate, farOutDate);

        verify(customerItemDescPriceRepository).fetchAllFutureItems(eq(cppSeqForLatestContractVersion));
        verify(customerItemDescPriceRepository).fetchCPPCustomerItemDescPriceNextSequence();
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(eq(existingCustomerItemDescPriceSeqList));
        verify(customerItemPriceRepository).fetchMarkupsForCMGs(eq(cppSeqForLatestContractVersion));
        verify(markupDOBuilder).buildProductMarkupDOListForAmendment(eq(userId), eq(contractPriceProfileSequence), eq(farOutDate),
                eq(productTypeMarkupDTOList));
        verify(customerItemPriceRepository).saveMarkup(eq(productTypeMarkupDOList), eq(userId), eq(contractPriceProfileSequence));
        verify(cppItemMappingRepository).saveCPPItemMapping(eq(itemAssignmentDOList), eq(userId), eq(expirationDate));
        verify(customerItemDescPriceRepository).saveFutureItemForCPPSeq(eq(futureItemDescriptionDTO), eq(userId), eq(contractPriceProfileSequence),
                eq(cppCustomerItemDescPriceNextSeq));
    }

    private List<ProductTypeMarkupDO> buildProductTypeMarkupDOList() {
        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setItemPriceId(2);
        productTypeMarkupDO.setGfsCustomerId("1234");
        productTypeMarkupDO.setCustomerTypeCode(3);
        productTypeMarkupDO.setContractPriceProfileSeq(1);
        productTypeMarkupDO.setProductType("1");
        productTypeMarkupDOList.add(productTypeMarkupDO);
        return productTypeMarkupDOList;
    }

    @Test
    public void shouldCopyCustomerItemDescAndMapping() {
        int contractPriceProfileSequence = 3;
        int cppSeqForLatestContractVersion = 2;
        String userId = "Test";
        Date effectiveDate = new Date();
        Date expirationDate = new Date();
        int cppCustomerItemDescPriceNextSeq = 23;
        int existingCustomerItemDescPriceSeq = 22;
        List<Integer> existingCustomerItemDescPriceSeqList = new ArrayList<>();
        existingCustomerItemDescPriceSeqList.add(existingCustomerItemDescPriceSeq);
        int itemPriceId = 2;
        String customerId = "123456";
        Integer customerTypeCode = 0;

        FutureItemDescriptionDTO futureItemDescriptionDTO = new FutureItemDescriptionDTO();
        futureItemDescriptionDTO.setCustomerItemDescSeq(existingCustomerItemDescPriceSeq);
        futureItemDescriptionDTO.setFutureItemDesc("test");
        futureItemDescriptionDTO.setGfsCustomerId("1");
        futureItemDescriptionDTO.setGfsCustomerTypeCode(31);

        List<FutureItemDescriptionDTO> futuredItemDescriptionList = new ArrayList<>();
        futuredItemDescriptionList.add(futureItemDescriptionDTO);

        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("Test");
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTO.setItemId("1");
        itemAssignmentDTO.setEffectiveDate(effectiveDate);
        itemAssignmentDTO.setExpirationDate(farOutDate);
        itemAssignmentDTOList.add(itemAssignmentDTO);

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        List<ItemAssignmentDO> itemAssignmentDOList = new ArrayList<>();
        ItemAssignmentDO itemAssignmentDO = new ItemAssignmentDO();
        itemAssignmentDO.setCustomerItemDescSeq(cppCustomerItemDescPriceNextSeq);
        itemAssignmentDO.setItemPriceId("1");
        itemAssignmentDO.setEffectiveDate(effectiveDate);
        itemAssignmentDO.setExpirationDate(farOutDate);
        itemAssignmentDOList.add(itemAssignmentDO);

        when(customerItemDescPriceRepository.fetchAllFutureItems(cppSeqForLatestContractVersion)).thenReturn(futuredItemDescriptionList);
        when(customerItemDescPriceRepository.fetchCPPCustomerItemDescPriceNextSequence()).thenReturn(cppCustomerItemDescPriceNextSeq);
        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(existingCustomerItemDescPriceSeqList)).thenReturn(itemAssignmentDTOList);

        target.copyCustomerItemDescAndMapping(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, expirationDate, farOutDate);

        verify(customerItemDescPriceRepository).fetchAllFutureItems(eq(cppSeqForLatestContractVersion));
        verify(customerItemDescPriceRepository).fetchCPPCustomerItemDescPriceNextSequence();
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(eq(existingCustomerItemDescPriceSeqList));
        verify(cppItemMappingRepository).saveCPPItemMapping(eq(itemAssignmentDOList), eq(userId), eq(expirationDate));
        verify(customerItemDescPriceRepository).saveFutureItemForCPPSeq(eq(futureItemDescriptionDTO), eq(userId), eq(contractPriceProfileSequence),
                eq(cppCustomerItemDescPriceNextSeq));

    }

    @Test
    public void shouldCopyCIPEntriesForCMG() {

        int contractPriceProfileSequence = 3;
        int cppSeqForLatestContractVersion = 2;
        String userId = "Test";
        int itemPriceId = 2;
        String customerId = "123456";
        Integer customerTypeCode = 0;

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(customerId);
        productTypeMarkupDTO.setItemPriceId(itemPriceId);
        productTypeMarkupDTO.setGfsCustomerTypeCode(customerTypeCode);

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = Collections.singletonList(productTypeMarkupDTO);

        List<ProductTypeMarkupDO> productTypeMarkupDOList = buildProductTypeMarkupDOList();

        when(customerItemPriceRepository.fetchMarkupsForCMGs(cppSeqForLatestContractVersion)).thenReturn(productTypeMarkupDTOList);
        when(markupDOBuilder.buildProductMarkupDOListForAmendment(userId, contractPriceProfileSequence, farOutDate, productTypeMarkupDTOList))
                .thenReturn(productTypeMarkupDOList);

        target.copyCIPEntriesForCMG(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate);

        verify(customerItemPriceRepository).fetchMarkupsForCMGs(eq(cppSeqForLatestContractVersion));
        verify(markupDOBuilder).buildProductMarkupDOListForAmendment(eq(userId), eq(contractPriceProfileSequence), eq(farOutDate),
                eq(productTypeMarkupDTOList));
        verify(customerItemPriceRepository).saveMarkup(eq(productTypeMarkupDOList), eq(userId), eq(contractPriceProfileSequence));

    }

}
