package com.gfs.cpp.component.markup;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.component.markup.MarkupFetcher;
import com.gfs.cpp.component.markup.builder.MarkupDTOBuilder;
import com.gfs.cpp.component.markup.builder.MarkupGridBuilder;
import com.gfs.cpp.component.markup.builder.MarkupWrapperBuilder;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;
import com.gfs.cpp.proxy.ItemConfigurationServiceProxy;
import com.gfs.cpp.proxy.ItemQueryProxy;

@RunWith(MockitoJUnitRunner.class)
public class MarkupFetcherTest {

    private static final String GFS_CUSTOMER_ID = "1";
    private static final String EXCPETION_GFS_CUSTOMER_ID = "2";
    private static final int contractPriceProfileSeq = 1;
    private static final Date pricingEffectiveDate = new LocalDate(2018, 01, 01).toDate();
    private static final Date pricingExpirationDate = new LocalDate(9999, 01, 01).toDate();

    @InjectMocks
    private MarkupFetcher target;

    @Mock
    private MarkupDTOBuilder markupDTOBuilder;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    @Mock
    private MarkupWrapperBuilder markupWrapperBuilder;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private ItemQueryProxy itemQueryProxy;

    @Mock
    private MarkupGridBuilder markupGridBuilder;

    @Mock
    private ItemConfigurationServiceProxy itemConfigurationServiceProxy;

    @Captor
    private ArgumentCaptor<List<String>> allItemLevelItemids;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Test
    public void shouldFetchAllMarkupsForProductMarkup() {

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(GFS_CUSTOMER_ID);
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setGfsCustomerTypeCode(31);
        productTypeMarkupDTO.setProductType("2");

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = new ArrayList<>();
        productTypeMarkupDTOList.add(productTypeMarkupDTO);

        ProductTypeMarkupDTO productTypeMarkupDTOForException = new ProductTypeMarkupDTO();
        productTypeMarkupDTOForException.setGfsCustomerId(EXCPETION_GFS_CUSTOMER_ID);
        productTypeMarkupDTOForException.setItemPriceId(1);
        productTypeMarkupDTOForException.setGfsCustomerTypeCode(31);
        productTypeMarkupDTOForException.setProductType("2");

        List<ProductTypeMarkupDTO> productTypeMarkupDTOListForException = new ArrayList<>();
        productTypeMarkupDTOListForException.add(productTypeMarkupDTOForException);

        MarkupGridDTO markupGrid = new MarkupGridDTO();

        markupGrid.setGfsCustomerId(GFS_CUSTOMER_ID);
        markupGrid.setMarkupName("markupName");
        markupGrid.setProductTypeMarkups(productTypeMarkupDTOList);

        Map<String, ItemInformationDTO> allItemsByItemId = new HashMap<>();

        List<CMGCustomerResponseDTO> cmgCustomerList = new ArrayList<>();
        CMGCustomerResponseDTO defaultCustomerId = new CMGCustomerResponseDTO();
        CMGCustomerResponseDTO exceptionCustomerResponseDTO = new CMGCustomerResponseDTO();
        defaultCustomerId.setId(GFS_CUSTOMER_ID);
        defaultCustomerId.setTypeCode(31);
        exceptionCustomerResponseDTO.setId(EXCPETION_GFS_CUSTOMER_ID);
        exceptionCustomerResponseDTO.setTypeCode(31);
        cmgCustomerList.add(defaultCustomerId);
        cmgCustomerList.add(exceptionCustomerResponseDTO);

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setGfsCustomerId(GFS_CUSTOMER_ID);
        markupWrapper.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapper.setSubgroupMarkupList(Collections.<SubgroupMarkupDTO> emptyList());
        markupWrapper.setItemLevelMarkupList(Collections.<ItemLevelMarkupDTO> emptyList());
        markupWrapper.setProductMarkupList(productTypeMarkupDTOList);

        MarkupWrapperDTO markupWrapperForException = new MarkupWrapperDTO();
        markupWrapperForException.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapperForException.setGfsCustomerId(EXCPETION_GFS_CUSTOMER_ID);
        markupWrapperForException.setMarkupName("exceptionMarkupName");
        markupWrapperForException.setProductMarkupList(productTypeMarkupDTOListForException);

        when(customerItemPriceRepository.fetchMarkupsForCMGs(contractPriceProfileSeq)).thenReturn(productTypeMarkupDTOList);
        when(markupGridBuilder.buildMarkupGrid(GFS_CUSTOMER_ID, productTypeMarkupDTOList, allItemsByItemId, contractPriceProfileSeq,
                pricingEffectiveDate, pricingExpirationDate)).thenReturn(markupGrid);
        when(contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(contractPriceProfileSeq)).thenReturn(cmgCustomerList);
        when(customerServiceProxy.fetchGroupName(EXCPETION_GFS_CUSTOMER_ID, 31)).thenReturn("exceptionMarkupName");
        when(markupWrapperBuilder.buildMarkupWrapperFromGrid(contractPriceProfileSeq, markupGrid)).thenReturn(markupWrapper);
        when(markupWrapperBuilder.buildDefaultMarkupWrapper(contractPriceProfileSeq, EXCPETION_GFS_CUSTOMER_ID, "exceptionMarkupName",
                pricingEffectiveDate, pricingExpirationDate)).thenReturn(markupWrapperForException);

        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());

        List<MarkupWrapperDTO> result = target.fetchAllMarkups(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate);

        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0).getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(result.get(1).getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(result.get(0).getGfsCustomerId(), equalTo(GFS_CUSTOMER_ID));
        assertThat(result.get(1).getGfsCustomerId(), equalTo(EXCPETION_GFS_CUSTOMER_ID));
        assertThat(result.get(0).getProductMarkupList(), equalTo(productTypeMarkupDTOList));
        assertThat(result.get(1).getProductMarkupList(), equalTo(productTypeMarkupDTOListForException));
        assertThat(result.get(0).getSubgroupMarkupList(), equalTo(Collections.<SubgroupMarkupDTO> emptyList()));

        assertThat(result.get(0).getMarkupName(), equalTo((String) buildContractPriceProfileDetails().getClmContractName()));
        assertThat(result.get(1).getMarkupName(), equalTo("exceptionMarkupName"));

        verify(customerItemPriceRepository).fetchMarkupsForCMGs(contractPriceProfileSeq);
        verify(markupGridBuilder).buildMarkupGrid(GFS_CUSTOMER_ID, productTypeMarkupDTOList, allItemsByItemId, contractPriceProfileSeq,
                pricingEffectiveDate, pricingExpirationDate);
        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailsList(contractPriceProfileSeq);
        verify(customerServiceProxy).fetchGroupName(EXCPETION_GFS_CUSTOMER_ID, 31);
        verify(markupWrapperBuilder).buildMarkupWrapperFromGrid(contractPriceProfileSeq, markupGrid);
        verify(markupWrapperBuilder).buildDefaultMarkupWrapper(contractPriceProfileSeq, EXCPETION_GFS_CUSTOMER_ID, "exceptionMarkupName",
                pricingEffectiveDate, pricingExpirationDate);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
    }

    @Test
    public void shouldFetchAllMarkupsForItemLevelMarkup() {

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(GFS_CUSTOMER_ID);
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setGfsCustomerTypeCode(31);
        productTypeMarkupDTO.setProductType("0");

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = new ArrayList<>();
        productTypeMarkupDTOList.add(productTypeMarkupDTO);

        List<ItemLevelMarkupDTO> itemLevelMarkupList = new ArrayList<>();
        ItemLevelMarkupDTO itemLevelMarkup = new ItemLevelMarkupDTO();
        itemLevelMarkup.setUnit("$");
        itemLevelMarkup.setEffectiveDate(pricingEffectiveDate);
        itemLevelMarkup.setExpirationDate(pricingExpirationDate);
        itemLevelMarkup.setItemDesc("Test");
        itemLevelMarkup.setItemId("1");
        itemLevelMarkup.setMarkupType(2);
        itemLevelMarkup.setNoItemId(false);
        itemLevelMarkupList.add(itemLevelMarkup);

        MarkupGridDTO markupGrid = new MarkupGridDTO();

        markupGrid.setGfsCustomerId(GFS_CUSTOMER_ID);
        markupGrid.setMarkupName("markupName");
        markupGrid.setProductTypeMarkups(productTypeMarkupDTOList);

        Map<String, ItemInformationDTO> allItemsByItemId = new HashMap<>();
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setItemNo("1");
        itemInformationDTO.setItemDescription("Test");

        List<CMGCustomerResponseDTO> cmgCustomerList = new ArrayList<>();
        CMGCustomerResponseDTO defaultCustomerId = new CMGCustomerResponseDTO();
        defaultCustomerId.setId(GFS_CUSTOMER_ID);
        defaultCustomerId.setTypeCode(31);
        cmgCustomerList.add(defaultCustomerId);

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setGfsCustomerId(GFS_CUSTOMER_ID);
        markupWrapper.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapper.setSubgroupMarkupList(Collections.<SubgroupMarkupDTO> emptyList());
        markupWrapper.setItemLevelMarkupList(itemLevelMarkupList);
        markupWrapper.setProductMarkupList(Collections.<ProductTypeMarkupDTO> emptyList());

        List<String> allItemLevelItemids = new ArrayList<>();
        allItemLevelItemids.add(itemInformationDTO.getItemNo());

        when(customerItemPriceRepository.fetchMarkupsForCMGs(contractPriceProfileSeq)).thenReturn(productTypeMarkupDTOList);
        when(markupGridBuilder.buildMarkupGrid(GFS_CUSTOMER_ID, productTypeMarkupDTOList, allItemsByItemId, contractPriceProfileSeq,
                pricingEffectiveDate, pricingExpirationDate)).thenReturn(markupGrid);
        when(contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(contractPriceProfileSeq)).thenReturn(cmgCustomerList);
        when(markupWrapperBuilder.buildMarkupWrapperFromGrid(contractPriceProfileSeq, markupGrid)).thenReturn(markupWrapper);
        when(itemQueryProxy.getItemInformationByItemId(allItemLevelItemids)).thenReturn(allItemsByItemId);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());

        List<MarkupWrapperDTO> result = target.fetchAllMarkups(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate);

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(result.get(0).getGfsCustomerId(), equalTo(GFS_CUSTOMER_ID));
        assertThat(result.get(0).getProductMarkupList(), equalTo(Collections.<ProductTypeMarkupDTO> emptyList()));
        assertThat(result.get(0).getSubgroupMarkupList(), equalTo(Collections.<SubgroupMarkupDTO> emptyList()));
        assertThat(result.get(0).getItemLevelMarkupList(), equalTo(itemLevelMarkupList));
        assertThat(result.get(0).getMarkupName(), equalTo((String) buildContractPriceProfileDetails().getClmContractName()));

        verify(customerItemPriceRepository).fetchMarkupsForCMGs(contractPriceProfileSeq);
        verify(markupGridBuilder).buildMarkupGrid(GFS_CUSTOMER_ID, productTypeMarkupDTOList, allItemsByItemId, contractPriceProfileSeq,
                pricingEffectiveDate, pricingExpirationDate);
        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailsList(contractPriceProfileSeq);
        verify(markupWrapperBuilder).buildMarkupWrapperFromGrid(contractPriceProfileSeq, markupGrid);
        verify(itemQueryProxy).getItemInformationByItemId(allItemLevelItemids);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
    }

    @Test
    public void shouldFetchAllMarkupsForSubgoupMarkup() {

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(GFS_CUSTOMER_ID);
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setGfsCustomerTypeCode(31);
        productTypeMarkupDTO.setProductType("1");

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = new ArrayList<>();
        productTypeMarkupDTOList.add(productTypeMarkupDTO);

        List<SubgroupMarkupDTO> subgroupMarkupList = new ArrayList<>();
        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        subgroupMarkupDTO.setExpirationDate(pricingExpirationDate);
        subgroupMarkupDTO.setIsSubgroupSaved(true);
        subgroupMarkupDTO.setMarkup("12");
        subgroupMarkupDTO.setMarkupType(2);
        subgroupMarkupDTO.setSubgroupDesc("FLOOR MATS");
        subgroupMarkupDTO.setSubgroupId("1");
        subgroupMarkupDTO.setUnit("%");
        subgroupMarkupList.add(subgroupMarkupDTO);

        MarkupGridDTO markupGrid = new MarkupGridDTO();

        markupGrid.setGfsCustomerId(GFS_CUSTOMER_ID);
        markupGrid.setMarkupName("markupName");
        markupGrid.setProductTypeMarkups(productTypeMarkupDTOList);

        Map<String, ItemInformationDTO> allItemsByItemId = new HashMap<>();
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setItemNo("1");
        itemInformationDTO.setItemDescription("Test");

        List<CMGCustomerResponseDTO> cmgCustomerList = new ArrayList<>();
        CMGCustomerResponseDTO defaultCustomerId = new CMGCustomerResponseDTO();
        defaultCustomerId.setId(GFS_CUSTOMER_ID);
        defaultCustomerId.setTypeCode(31);
        cmgCustomerList.add(defaultCustomerId);

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setGfsCustomerId(GFS_CUSTOMER_ID);
        markupWrapper.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupWrapper.setSubgroupMarkupList(subgroupMarkupList);
        markupWrapper.setItemLevelMarkupList(Collections.<ItemLevelMarkupDTO> emptyList());
        markupWrapper.setProductMarkupList(Collections.<ProductTypeMarkupDTO> emptyList());

        List<String> allItemLevelItemids = new ArrayList<>();
        allItemLevelItemids.add(itemInformationDTO.getItemNo());

        when(customerItemPriceRepository.fetchMarkupsForCMGs(contractPriceProfileSeq)).thenReturn(productTypeMarkupDTOList);
        when(markupGridBuilder.buildMarkupGrid(GFS_CUSTOMER_ID, productTypeMarkupDTOList, allItemsByItemId, contractPriceProfileSeq,
                pricingEffectiveDate, pricingExpirationDate)).thenReturn(markupGrid);
        when(contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(contractPriceProfileSeq)).thenReturn(cmgCustomerList);
        when(markupWrapperBuilder.buildMarkupWrapperFromGrid(contractPriceProfileSeq, markupGrid)).thenReturn(markupWrapper);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());

        List<MarkupWrapperDTO> result = target.fetchAllMarkups(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate);

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(result.get(0).getGfsCustomerId(), equalTo(GFS_CUSTOMER_ID));
        assertThat(result.get(0).getProductMarkupList(), equalTo(Collections.<ProductTypeMarkupDTO> emptyList()));
        assertThat(result.get(0).getSubgroupMarkupList(), equalTo(subgroupMarkupList));
        assertThat(result.get(0).getItemLevelMarkupList(), equalTo(Collections.<ItemLevelMarkupDTO> emptyList()));
        assertThat(result.get(0).getMarkupName(), equalTo(buildContractPriceProfileDetails().getClmContractName()));

        verify(customerItemPriceRepository).fetchMarkupsForCMGs(contractPriceProfileSeq);
        verify(markupGridBuilder).buildMarkupGrid(GFS_CUSTOMER_ID, productTypeMarkupDTOList, allItemsByItemId, contractPriceProfileSeq,
                pricingEffectiveDate, pricingExpirationDate);
        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailsList(contractPriceProfileSeq);
        verify(markupWrapperBuilder).buildMarkupWrapperFromGrid(contractPriceProfileSeq, markupGrid);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
    }

    private ContractPricingResponseDTO buildContractPriceProfileDetails() {
        String clmContractName = "clmContractName";
        int cppId = 123;
        ContractPricingResponseDTO contractPriceProfileDetails = new ContractPricingResponseDTO();

        contractPriceProfileDetails.setContractPriceProfileId(cppId);
        contractPriceProfileDetails.setClmContractName(clmContractName);
        return contractPriceProfileDetails;
    }

}
