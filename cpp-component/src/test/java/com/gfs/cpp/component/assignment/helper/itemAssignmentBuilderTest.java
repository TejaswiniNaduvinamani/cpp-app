package com.gfs.cpp.component.assignment.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.model.assignments.ItemAssignmentDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentBuilder;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.proxy.ItemQueryProxy;

@RunWith(MockitoJUnitRunner.class)
public class itemAssignmentBuilderTest {

    @InjectMocks
    private ItemAssignmentBuilder target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private ItemQueryProxy itemQueryProxy;

    @Mock
    private CppItemMappingRepository cppItemMappingRepository;

    @Test
    public void shouldBuildItemAssignmentWrapperDTO() throws ParseException {

        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "1001";
        int gfsCustomerTypeCode = 31;
        String itemId = "681204";

        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);

        FutureItemDescriptionDTO futureItemDescriptionDTO = buildFutureItemDescriptionDTO();
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);
        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(futureItemDescriptionDTO.getCustomerItemDescSeq());
        List<ItemInformationDTO> itemInformationDTOList = buildItemInformationDTOList();

        when(itemQueryProxy.findItemInformationList(itemIdList)).thenReturn(itemInformationDTOList);
        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(eq(customerItemDescSeqList)))
                .thenReturn(itemAssignmentWrapperDTO.getItemAssignmentList());

        ItemAssignmentWrapperDTO result = target.buildItemAssignmentWrapperDTO(futureItemDescriptionDTO, contractPriceProfileSeq);

        List<ItemAssignmentDTO> itemAssignmentDTOList = result.getItemAssignmentList();

        assertThat(result.getContractPriceProfileSeq(), is(1));
        assertThat(result.getFutureItemDesc(), is("chicken"));
        assertThat(result.getGfsCustomerId(), is("1001"));
        assertThat(result.getIsFutureItemSaved(), is(true));
        assertThat(itemAssignmentDTOList.size(), is(1));
        assertThat(itemAssignmentDTOList.get(0).getCustomerItemDescSeq(), is(1));
        assertThat(itemAssignmentDTOList.get(0).getIsItemSaved(), is(true));
        assertThat(itemAssignmentDTOList.get(0).getItemDescription(), is("Test"));
        assertThat(itemAssignmentDTOList.get(0).getItemId(), is("681204"));

        verify(itemQueryProxy).findItemInformationList(itemIdList);
        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(eq(customerItemDescSeqList));
    }

    @Test
    public void shouldBuildDefaultItemAssignmentWrapperDTOIfNoItemsAreMapped() throws ParseException {

        int contractPriceProfileSeq = 1;

        FutureItemDescriptionDTO futureItemDescriptionDTO = buildFutureItemDescriptionDTO();
        List<ItemAssignmentDTO> itemAssignmentDTOs = new ArrayList<>();
        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(futureItemDescriptionDTO.getCustomerItemDescSeq());

        when(cppItemMappingRepository.fetchAssignedItemsForAConcept(eq(customerItemDescSeqList))).thenReturn(itemAssignmentDTOs);

        ItemAssignmentWrapperDTO result = target.buildItemAssignmentWrapperDTO(futureItemDescriptionDTO, contractPriceProfileSeq);

        List<ItemAssignmentDTO> itemAssignmentDTOList = result.getItemAssignmentList();

        assertThat(result.getContractPriceProfileSeq(), is(1));
        assertThat(result.getFutureItemDesc(), is("chicken"));
        assertThat(result.getGfsCustomerId(), is("1001"));
        assertThat(result.getIsFutureItemSaved(), is(false));
        assertThat(itemAssignmentDTOList.size(), is(1));
        assertThat(itemAssignmentDTOList.get(0).getIsItemSaved(), is(false));
        assertThat(itemAssignmentDTOList.get(0).getItemDescription(), is(StringUtils.EMPTY));
        assertThat(itemAssignmentDTOList.get(0).getItemId(), is(StringUtils.EMPTY));

        verify(cppItemMappingRepository).fetchAssignedItemsForAConcept(eq(customerItemDescSeqList));
    }

    @Test
    public void shouldBuildItemAssignmentDOList() throws ParseException {

        int contractPriceProfileSeq = 1;
        int customerItemDescSeq = 1;
        String gfsCustomerId = "1001";
        int gfsCustomerTypeCode = 31;
        String itemId = "681204";
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);

        List<ItemAssignmentDO> result = target.buildItemAssignmentDOList(itemAssignmentWrapperDTO.getItemAssignmentList(), customerItemDescSeq);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getCustomerItemDescSeq(), is(customerItemDescSeq));
        assertThat(result.get(0).getItemPriceId(), is(itemId));
        assertThat(result.get(0).getItemPriceLevelCode(), is(0));
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
        itemAssignmentDTO.setCustomerItemDescSeq(1);
        itemAssignmentDTO.setItemId(itemId);
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTO.setItemPriceLevelCode(ItemPriceLevel.ITEM.getCode());
        itemAssignmentDTOList.add(itemAssignmentDTO);
        itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentDTOList);
        return itemAssignmentWrapperDTO;
    }

    private FutureItemDescriptionDTO buildFutureItemDescriptionDTO() {
        FutureItemDescriptionDTO futureItemDescriptionDTO = new FutureItemDescriptionDTO();
        futureItemDescriptionDTO.setCustomerItemDescSeq(1);
        futureItemDescriptionDTO.setFutureItemDesc("chicken");
        futureItemDescriptionDTO.setGfsCustomerId("1001");
        futureItemDescriptionDTO.setGfsCustomerTypeCode(31);
        return futureItemDescriptionDTO;
    }

    private List<ItemInformationDTO> buildItemInformationDTOList() {
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setIsActive(true);
        itemInformationDTO.setIsValid(true);
        itemInformationDTO.setItemDescription("Test");
        itemInformationDTO.setItemNo("681204");
        itemInformationDTO.setStockingCode("3");
        itemInformationDTO.setItemStatusCode("AC");
        List<ItemInformationDTO> itemInformationDTOList = new ArrayList<>();
        itemInformationDTOList.add(itemInformationDTO);
        return itemInformationDTOList;
    }
}
