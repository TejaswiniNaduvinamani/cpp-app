package com.gfs.cpp.component.assignment.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGContractDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.model.assignments.ItemAssignmentDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.assignment.ItemAssignmentDuplicateValidator;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentBuilder;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentHelper;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class ItemAssignmentHelperTest {

    @InjectMocks
    private ItemAssignmentHelper target;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    @Mock
    private ItemAssignmentBuilder itemAssignmentBuilder;

    @Mock
    private MarkupDOBuilder markupDOBuilder;

    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Mock
    private CppItemMappingRepository cppItemMappingRepository;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private ItemAssignmentDuplicateValidator itemAssignmentDuplicateValidator;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Test
    public void shouldReturnSavedFutureItemsForDefault() {

        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        String itemId = "1";

        List<FutureItemDescriptionDTO> futureItemDescriptionDTOList = buildFutureItemDescriptionDTO();
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(gfsCustomerId);
        cmgCustomerResponseDTO.setTypeCode(gfsCustomerTypeCode);

        when(itemAssignmentBuilder.buildItemAssignmentWrapperDTO(futureItemDescriptionDTOList.get(0), contractPriceProfileSeq))
                .thenReturn(itemAssignmentWrapperDTO);
        when(customerItemDescPriceRepository.fetchAllFutureItems(eq(contractPriceProfileSeq))).thenReturn(futureItemDescriptionDTOList);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgCustomerResponseDTO);

        List<ItemAssignmentWrapperDTO> result = target.fetchAllFutureItems(contractPriceProfileSeq);

        verify(customerItemDescPriceRepository).fetchAllFutureItems(eq(contractPriceProfileSeq));
        verify(itemAssignmentBuilder).buildItemAssignmentWrapperDTO(eq(futureItemDescriptionDTOList.get(0)), eq(contractPriceProfileSeq));
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getContractPriceProfileSeq(), is(1));
        assertThat(result.get(0).getExceptionName(), is(buildContractPriceProfileDetails().getClmContractName()));
        assertThat(result.get(0).getFutureItemDesc(), is("test"));
        assertThat(result.get(0).getGfsCustomerId(), is("1"));
        assertThat(result.get(0).getIsFutureItemSaved(), is(true));
        assertThat(result.get(0).getItemAssignmentList().get(0).getItemDescription(), is("test"));
        assertThat(result.get(0).getItemAssignmentList().get(0).getItemId(), is("1"));
        assertThat(result.get(0).getItemAssignmentList().get(0).getIsItemSaved(), is(true));
    }

    @Test
    public void shouldReturnSavedFutureItemsForException() {

        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        String itemId = "1";

        List<FutureItemDescriptionDTO> futureItemDescriptionDTOList = buildFutureItemDescriptionDTO();
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);

        CMGContractDTO cmgContractDTO = new CMGContractDTO();
        cmgContractDTO.setContractName("test");
        cmgContractDTO.setContractPriceProfileId("1");

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1001");
        cmgCustomerResponseDTO.setTypeCode(gfsCustomerTypeCode);

        when(itemAssignmentBuilder.buildItemAssignmentWrapperDTO(futureItemDescriptionDTOList.get(0), contractPriceProfileSeq))
                .thenReturn(itemAssignmentWrapperDTO);
        when(customerServiceProxy.fetchCustomerGroup(eq(gfsCustomerId), eq(gfsCustomerTypeCode))).thenReturn(cmgContractDTO);
        when(customerItemDescPriceRepository.fetchAllFutureItems(eq(contractPriceProfileSeq))).thenReturn(futureItemDescriptionDTOList);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgCustomerResponseDTO);

        List<ItemAssignmentWrapperDTO> result = target.fetchAllFutureItems(contractPriceProfileSeq);

        verify(customerServiceProxy).fetchCustomerGroup(eq(gfsCustomerId), eq(gfsCustomerTypeCode));
        verify(customerItemDescPriceRepository).fetchAllFutureItems(eq(contractPriceProfileSeq));
        verify(itemAssignmentBuilder).buildItemAssignmentWrapperDTO(eq(futureItemDescriptionDTOList.get(0)), eq(contractPriceProfileSeq));
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getContractPriceProfileSeq(), is(1));
        assertThat(result.get(0).getExceptionName(), is("test"));
        assertThat(result.get(0).getFutureItemDesc(), is("test"));
        assertThat(result.get(0).getGfsCustomerId(), is("1"));
        assertThat(result.get(0).getIsFutureItemSaved(), is(true));
        assertThat(result.get(0).getItemAssignmentList().get(0).getItemDescription(), is("test"));
        assertThat(result.get(0).getItemAssignmentList().get(0).getItemId(), is("1"));
        assertThat(result.get(0).getItemAssignmentList().get(0).getIsItemSaved(), is(true));
    }

    @Test
    public void shouldAssignNewItemsWithFutureItem() {

        int contractPriceProfileSeq = 1;
        int customerItemDescSeq = 1;
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        String itemId = "1";
        String userName = "dummy";
        String itemDesc = " chicken";

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);
        ItemLevelMarkupDTO itemLevelMarkupDTO = buildSavedFutureItemDTO(itemDesc);
        List<ItemAssignmentDO> itemAssignmentDOList = buildItemAssignmentDOList(customerItemDescSeq, itemId);
        List<ProductTypeMarkupDO> markupDOList = buildMarkupDOList(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode);

        when(markupDOBuilder.buildMarkupDOListForAssignment(itemAssignmentWrapperDTO, itemLevelMarkupDTO)).thenReturn(markupDOList);
        when(customerItemDescPriceRepository.fetchFutureItemForAssignment(itemAssignmentWrapperDTO.getContractPriceProfileSeq(),
                itemAssignmentWrapperDTO.getGfsCustomerId(), itemAssignmentWrapperDTO.getGfsCustomerTypeCode(),
                itemAssignmentWrapperDTO.getFutureItemDesc())).thenReturn(itemLevelMarkupDTO);
        when(itemAssignmentBuilder.buildItemAssignmentDOList(itemAssignmentWrapperDTO.getItemAssignmentList(),
                itemLevelMarkupDTO.getCustomerItemDescSeq())).thenReturn(itemAssignmentDOList);

        target.assignItemsWithFutureItem(itemAssignmentWrapperDTO, userName);

        verify(markupDOBuilder).buildMarkupDOListForAssignment(itemAssignmentWrapperDTO, itemLevelMarkupDTO);
        verify(customerItemDescPriceRepository).fetchFutureItemForAssignment(itemAssignmentWrapperDTO.getContractPriceProfileSeq(),
                itemAssignmentWrapperDTO.getGfsCustomerId(), itemAssignmentWrapperDTO.getGfsCustomerTypeCode(),
                itemAssignmentWrapperDTO.getFutureItemDesc());
        verify(itemAssignmentBuilder).buildItemAssignmentDOList(itemAssignmentWrapperDTO.getItemAssignmentList(),
                itemLevelMarkupDTO.getCustomerItemDescSeq());
        verify(cppItemMappingRepository).saveItems(itemAssignmentDOList, userName);

    }

    @Test
    public void shouldUpdateItemsWithFutureItemWhenRequestToAssignItems() {

        int contractPriceProfileSeq = 1;
        int customerItemDescSeq = 1;
        String gfsCustomerId = "1";
        int gfsCustomerTypeCode = 31;
        String itemId = "1";
        String userName = "dummy";
        String itemDesc = " chicken";
        Date expirationDate = new LocalDate(9999, 01, 01).toDate();

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);
        ItemLevelMarkupDTO itemLevelMarkupDTO = buildSavedFutureItemDTO(itemDesc);
        List<ItemAssignmentDO> itemAssignmentDOList = buildItemAssignmentDOList(customerItemDescSeq, itemId);
        List<ProductTypeMarkupDO> markupDOList = buildMarkupDOList(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode);
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("Test");
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTO.setItemId("1");
        itemAssignmentDTOList.add(itemAssignmentDTO);

        List<Integer> futureItemDTOList = new ArrayList<>();
        futureItemDTOList.add(itemLevelMarkupDTO.getCustomerItemDescSeq());

        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(eq(futureItemDTOList))).thenReturn(itemAssignmentDTOList);
        when(markupDOBuilder.buildMarkupDOListForAssignment(itemAssignmentWrapperDTO, itemLevelMarkupDTO)).thenReturn(markupDOList);
        when(customerItemDescPriceRepository.fetchFutureItemForAssignment(itemAssignmentWrapperDTO.getContractPriceProfileSeq(),
                itemAssignmentWrapperDTO.getGfsCustomerId(), itemAssignmentWrapperDTO.getGfsCustomerTypeCode(),
                itemAssignmentWrapperDTO.getFutureItemDesc())).thenReturn(itemLevelMarkupDTO);
        when(itemAssignmentBuilder.buildItemAssignmentDOList(itemAssignmentWrapperDTO.getItemAssignmentList(),
                itemLevelMarkupDTO.getCustomerItemDescSeq())).thenReturn(itemAssignmentDOList);
        when(cppDateUtils.getFutureDate()).thenReturn(expirationDate);

        target.assignItemsWithFutureItem(itemAssignmentWrapperDTO, userName);

        verify(markupDOBuilder).buildMarkupDOListForAssignment(itemAssignmentWrapperDTO, itemLevelMarkupDTO);
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(eq(futureItemDTOList));
        verify(cppItemMappingRepository).expireOrUpdateItems(itemAssignmentDOList, expirationDate, userName);
        verify(customerItemDescPriceRepository).fetchFutureItemForAssignment(itemAssignmentWrapperDTO.getContractPriceProfileSeq(),
                itemAssignmentWrapperDTO.getGfsCustomerId(), itemAssignmentWrapperDTO.getGfsCustomerTypeCode(),
                itemAssignmentWrapperDTO.getFutureItemDesc());
        verify(itemAssignmentBuilder).buildItemAssignmentDOList(itemAssignmentWrapperDTO.getItemAssignmentList(),
                itemLevelMarkupDTO.getCustomerItemDescSeq());
        verify(cppDateUtils).getFutureDate();

    }

    @Test
    public void shouldExpireItemMapping() {
        int customerItemDescSeq = 1;
        String itemId = "1000";
        String userName = "vc71u";

        Date expirationDate = new LocalDate().toDate();

        List<ItemAssignmentDO> itemAssignmentDOList = buildItemAssignmentDOList(customerItemDescSeq, itemId);

        when(cppDateUtils.oneDayPreviousCurrentDate()).thenReturn(expirationDate);
        when(itemAssignmentBuilder.buildItemAssignmentDO(customerItemDescSeq, itemId)).thenReturn(itemAssignmentDOList.get(0));

        target.expireItemMapping(customerItemDescSeq, itemId, userName);

        verify(cppDateUtils).oneDayPreviousCurrentDate();
        verify(itemAssignmentBuilder).buildItemAssignmentDO(customerItemDescSeq, itemId);
        verify(cppItemMappingRepository).expireOrUpdateItems(eq(itemAssignmentDOList), eq(expirationDate), eq(userName));
    }

    private List<ItemAssignmentDO> buildItemAssignmentDOList(int customerItemDescSeq, String itemId) {
        List<ItemAssignmentDO> itemAssignmentDOList = new ArrayList<>();
        ItemAssignmentDO itemAssignmentDO = new ItemAssignmentDO();
        itemAssignmentDO.setCustomerItemDescSeq(customerItemDescSeq);
        itemAssignmentDO.setItemPriceId(itemId);
        itemAssignmentDOList.add(itemAssignmentDO);
        return itemAssignmentDOList;
    }

    private List<FutureItemDescriptionDTO> buildFutureItemDescriptionDTO() {
        List<FutureItemDescriptionDTO> futureItemDescriptionDTOList = new ArrayList<>();
        FutureItemDescriptionDTO futureItemDescriptionDTO = new FutureItemDescriptionDTO();
        futureItemDescriptionDTO.setCustomerItemDescSeq(1);
        futureItemDescriptionDTO.setFutureItemDesc("test");
        futureItemDescriptionDTO.setGfsCustomerId("1");
        futureItemDescriptionDTO.setGfsCustomerTypeCode(31);
        futureItemDescriptionDTOList.add(futureItemDescriptionDTO);
        return futureItemDescriptionDTOList;
    }

    private List<ProductTypeMarkupDO> buildMarkupDOList(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode) {
        List<ProductTypeMarkupDO> markupDOList = new ArrayList<>();
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDO.setGfsCustomerId(gfsCustomerId);
        productTypeMarkupDO.setItemPriceId(1);
        productTypeMarkupDO.setCustomerTypeCode(gfsCustomerTypeCode);
        markupDOList.add(productTypeMarkupDO);
        return markupDOList;
    }

    private ItemLevelMarkupDTO buildSavedFutureItemDTO(String futureItemDesc) {
        ItemLevelMarkupDTO itemMarkup = new ItemLevelMarkupDTO();
        itemMarkup.setItemId("123");
        itemMarkup.setEffectiveDate(new LocalDate(2010, 01, 01).toDate());
        itemMarkup.setExpirationDate(new LocalDate(2011, 01, 01).toDate());
        itemMarkup.setItemDesc(futureItemDesc);
        itemMarkup.setMarkup("test");
        itemMarkup.setMarkupType(2);
        itemMarkup.setUnit("$");
        itemMarkup.setCustomerItemDescSeq(100);
        return itemMarkup;
    }

    private ItemAssignmentWrapperDTO buildItemAssignmentWrapperDTO(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemId) {
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        itemAssignmentWrapperDTO.setExceptionName("test");
        itemAssignmentWrapperDTO.setFutureItemDesc("test");
        itemAssignmentWrapperDTO.setGfsCustomerId(gfsCustomerId);
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        itemAssignmentWrapperDTO.setIsFutureItemSaved(true);
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("test");
        itemAssignmentDTO.setItemId(itemId);
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTOList.add(itemAssignmentDTO);
        itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentDTOList);
        return itemAssignmentWrapperDTO;
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
