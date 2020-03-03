package com.gfs.cpp.component.customerinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.assignments.ItemAssignmentDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class ContractItemCopier {

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private CppItemMappingRepository cppItemMappingRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private MarkupDOBuilder markupDOBuilder;
  
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void copyAllItemAndMappings(int contractPriceProfileSequence, int cppSeqForLatestContractVersion, String userId,
            Date expirationDate, Date farOutDate) {

        logger.info("Copying item details and its mapping for the contract {} from an existing contract with cpp seq {}",
                contractPriceProfileSequence, cppSeqForLatestContractVersion);

        copyCustomerItemDescAndMapping(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, expirationDate, farOutDate);
        copyCIPEntriesForCMG(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, farOutDate);

    }

    public void copyCustomerItemDescAndMapping(int contractPriceProfileSequence, int cppSeqForLatestContractVersion, String userId,
           Date expirationDate, Date farOutDate) {
        logger.info("Copying customer item description & the mapping for contract {} from an existing contract with cpp seq {}",
                contractPriceProfileSequence, cppSeqForLatestContractVersion);

        List<FutureItemDescriptionDTO> futuredItemDescriptionList = customerItemDescPriceRepository.fetchAllFutureItems(cppSeqForLatestContractVersion);
        for (FutureItemDescriptionDTO futureItemDescriptionDTO : futuredItemDescriptionList) {
            int cppCustomerItemDescPriceSeq = customerItemDescPriceRepository.fetchCPPCustomerItemDescPriceNextSequence();
            customerItemDescPriceRepository.saveFutureItemForCPPSeq(futureItemDescriptionDTO, userId, contractPriceProfileSequence,
                    cppCustomerItemDescPriceSeq);
            copyCPPItemMapping(userId, futureItemDescriptionDTO.getCustomerItemDescSeq(), cppCustomerItemDescPriceSeq, expirationDate, farOutDate);
        }
    }

    private void copyCPPItemMapping(String userId, int existingCustomerItemDescPriceSeq, int newCustomerItemDescPriceSeq,
            Date expirationDate, Date farOutDate) {

        List<Integer> existingCustomerItemDescPriceSeqList = new ArrayList<>();
        existingCustomerItemDescPriceSeqList.add(existingCustomerItemDescPriceSeq);
        List<ItemAssignmentDTO> itemAssignmentDTOList = cppItemMappingRepository.fetchAssignedItemsForAConcept(existingCustomerItemDescPriceSeqList);
        List<ItemAssignmentDO> newItemAssignmentDOList = buildItemAssignmentDO(itemAssignmentDTOList, newCustomerItemDescPriceSeq, farOutDate);
        if (CollectionUtils.isNotEmpty(newItemAssignmentDOList)) {
            cppItemMappingRepository.saveCPPItemMapping(newItemAssignmentDOList, userId, expirationDate);
        }
    }

    private List<ItemAssignmentDO> buildItemAssignmentDO(List<ItemAssignmentDTO> itemAssignmentDTOList, int newCustomerItemDescPriceSeq, Date farOutDate) {

        List<ItemAssignmentDO> itemAssignmentDOList = new ArrayList<>();
        for (ItemAssignmentDTO itemAssignmentDTO : ListUtils.emptyIfNull(itemAssignmentDTOList)) {
            ItemAssignmentDO itemAssignmentDO = new ItemAssignmentDO();
            itemAssignmentDO.setEffectiveDate(itemAssignmentDTO.getEffectiveDate());
            itemAssignmentDO.setExpirationDate(farOutDate);
            itemAssignmentDO.setCustomerItemDescSeq(newCustomerItemDescPriceSeq);
            itemAssignmentDO.setItemPriceId(itemAssignmentDTO.getItemId());
            itemAssignmentDO.setItemPriceLevelCode(itemAssignmentDTO.getItemPriceLevelCode());
            itemAssignmentDOList.add(itemAssignmentDO);
        }
        return itemAssignmentDOList;
    }

    public void copyCIPEntriesForCMG(int contractPriceProfileSequence, int cppSeqForLatestContractVersion, String userId, Date farOutDate) {
        logger.info("Copying existing CIP entries from contract {}", cppSeqForLatestContractVersion);
        List<ProductTypeMarkupDTO> productTypeMarkupList = customerItemPriceRepository.fetchMarkupsForCMGs(cppSeqForLatestContractVersion);
        if (productTypeMarkupList != null && !productTypeMarkupList.isEmpty()) {
            List<ProductTypeMarkupDO> productTypeMarkupDOList = markupDOBuilder.buildProductMarkupDOListForAmendment(userId, contractPriceProfileSequence, farOutDate, productTypeMarkupList);
            customerItemPriceRepository.saveMarkup(productTypeMarkupDOList, userId, contractPriceProfileSequence);
        }
    }

}
