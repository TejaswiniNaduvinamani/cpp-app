package com.gfs.cpp.component.assignment.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@Component
public class ItemAssignmentHelper {

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private CppItemMappingRepository cppItemMappingRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private ItemAssignmentDuplicateValidator itemAssignmentDuplicateValidator;

    @Autowired
    private ItemAssignmentBuilder itemAssignmentBuilder;

    @Autowired
    private MarkupDOBuilder markupDOBuilder;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    public List<ItemAssignmentWrapperDTO> fetchAllFutureItems(int contractPriceProfileSeq) {
        List<ItemAssignmentWrapperDTO> itemAssignmentWrapperDTOList = new ArrayList<>();
        List<FutureItemDescriptionDTO> futureItemDescriptionDTOList = customerItemDescPriceRepository.fetchAllFutureItems(contractPriceProfileSeq);
        CMGCustomerResponseDTO cmgCustomerResponseDTO = contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq);

        for (FutureItemDescriptionDTO futureItemDescriptionDTO : futureItemDescriptionDTOList) {
            ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = itemAssignmentBuilder.buildItemAssignmentWrapperDTO(futureItemDescriptionDTO,
                    contractPriceProfileSeq);

            if (futureItemDescriptionDTO.getGfsCustomerId().equals(cmgCustomerResponseDTO.getId())) {
                ContractPricingResponseDTO contractPriceProfileDetails = contractPriceProfileRepository
                        .fetchContractDetailsByCppSeq(contractPriceProfileSeq);
                itemAssignmentWrapperDTO.setExceptionName(contractPriceProfileDetails.getClmContractName());
            } else {
                CMGContractDTO cmgContractDTO = customerServiceProxy.fetchCustomerGroup(futureItemDescriptionDTO.getGfsCustomerId(),
                        futureItemDescriptionDTO.getGfsCustomerTypeCode());
                itemAssignmentWrapperDTO.setExceptionName(cmgContractDTO.getContractName());
            }

            itemAssignmentWrapperDTOList.add(itemAssignmentWrapperDTO);
        }

        return itemAssignmentWrapperDTOList;
    }

    public void expireItemMapping(int customerItemDescSeq, String itemId, String userName) {
        List<ItemAssignmentDO> itemAssignmentDOList = new ArrayList<>();
        itemAssignmentDOList.add(itemAssignmentBuilder.buildItemAssignmentDO(customerItemDescSeq, itemId));

        cppItemMappingRepository.expireOrUpdateItems(itemAssignmentDOList, cppDateUtils.oneDayPreviousCurrentDate(), userName);
    }

    public void assignItemsWithFutureItem(ItemAssignmentWrapperDTO itemAssignmentWrapperDTO, String userName) {

        itemAssignmentDuplicateValidator.validateOnAssignItems(itemAssignmentWrapperDTO);
        ItemLevelMarkupDTO futureItemDTO = customerItemDescPriceRepository.fetchFutureItemForAssignment(
                itemAssignmentWrapperDTO.getContractPriceProfileSeq(), itemAssignmentWrapperDTO.getGfsCustomerId(),
                itemAssignmentWrapperDTO.getGfsCustomerTypeCode(), itemAssignmentWrapperDTO.getFutureItemDesc());

        List<ProductTypeMarkupDO> markupDOList = markupDOBuilder.buildMarkupDOListForAssignment(itemAssignmentWrapperDTO, futureItemDTO);
        saveOrUpdateAssignmentMapping(itemAssignmentWrapperDTO, userName, futureItemDTO);
        customerItemPriceRepository.saveMarkup(markupDOList, userName, itemAssignmentWrapperDTO.getContractPriceProfileSeq());
    }

    private void saveOrUpdateAssignmentMapping(ItemAssignmentWrapperDTO itemAssignmentWrapperDTO, String userName, ItemLevelMarkupDTO itemMarkupDTO) {
        List<ItemAssignmentDO> newItemAssignmentDOList = new ArrayList<>();
        List<ItemAssignmentDO> updateItemAssignmentDOList = new ArrayList<>();
        Map<String, ItemAssignmentDTO> savedExistingItemAssignmentDTO = new HashMap<>();
        List<ItemAssignmentDO> itemAssignmentDOList = itemAssignmentBuilder
                .buildItemAssignmentDOList(itemAssignmentWrapperDTO.getItemAssignmentList(), itemMarkupDTO.getCustomerItemDescSeq());
        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(itemMarkupDTO.getCustomerItemDescSeq());
        List<ItemAssignmentDTO> savedItemAssignmentDTO = cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList);
        for (ItemAssignmentDTO itemAssignmentDTO : savedItemAssignmentDTO) {
            savedExistingItemAssignmentDTO.put(itemAssignmentDTO.getItemId(), itemAssignmentDTO);
        }
        for (ItemAssignmentDO itemAssignmentDO : itemAssignmentDOList) {
            if (savedExistingItemAssignmentDTO.containsKey(itemAssignmentDO.getItemPriceId())) {
                updateItemAssignmentDOList.add(itemAssignmentDO);
            } else {
                newItemAssignmentDOList.add(itemAssignmentDO);
            }
        }
        if (CollectionUtils.isNotEmpty(updateItemAssignmentDOList)) {
            cppItemMappingRepository.expireOrUpdateItems(updateItemAssignmentDOList, cppDateUtils.getFutureDate(), userName);
        }
        if (CollectionUtils.isNotEmpty(newItemAssignmentDOList)) {
            cppItemMappingRepository.saveItems(newItemAssignmentDOList, userName);
        }
    }

}
