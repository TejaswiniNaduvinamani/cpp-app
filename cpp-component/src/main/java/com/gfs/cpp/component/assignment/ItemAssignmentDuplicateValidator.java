package com.gfs.cpp.component.assignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class ItemAssignmentDuplicateValidator {

    static final Logger logger = LoggerFactory.getLogger(ItemAssignmentDuplicateValidator.class);

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    public void validateOnFindItemInformation(String itemId, String cmgCustomerId, int cmgCustomerTypeCode, int contractPriceProfileSeq) {

        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);
        validateIfItemAlreadyExist(cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq, itemIdList);
    }

    public void validateOnAssignItems(ItemAssignmentWrapperDTO itemAssignmentWrapperDTO) {

        List<String> itemIdList = new ArrayList<>();
        Set<String> itemIdSet = new HashSet<>();
        List<String> duplicateItemIdList = new ArrayList<>();

        for (ItemAssignmentDTO itemAssignmentDTO : itemAssignmentWrapperDTO.getItemAssignmentList()) {
            itemIdList.add(itemAssignmentDTO.getItemId());
            if (!itemIdSet.add(itemAssignmentDTO.getItemId())) {
                duplicateItemIdList.add(itemAssignmentDTO.getItemId());
            }
        }
        if (CollectionUtils.isNotEmpty(duplicateItemIdList)) {
            logger.error("Assign items validation failed {} ", itemAssignmentWrapperDTO.getContractPriceProfileSeq());
            throw new CPPRuntimeException(CPPExceptionType.ITEM_ALREADY_EXIST, String.format("(%s)", StringUtils.join(duplicateItemIdList, ",")));
        }

        validateIfItemAlreadyExist(itemAssignmentWrapperDTO.getGfsCustomerId(), itemAssignmentWrapperDTO.getGfsCustomerTypeCode(),
                itemAssignmentWrapperDTO.getContractPriceProfileSeq(), itemIdList);
    }

    private void validateIfItemAlreadyExist(String cmgCustomerId, int cmgCustomerTypeCode, int contractPriceProfileSeq, List<String> itemIdList) {
        List<String> duplicateItemIdList = customerItemPriceRepository.fetchAlreadyExistingItemsOrSubgroups(itemIdList, cmgCustomerId, cmgCustomerTypeCode,
                contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());
        if (CollectionUtils.isNotEmpty(duplicateItemIdList)) {
            throw new CPPRuntimeException(CPPExceptionType.ITEM_ALREADY_EXIST, String.format("(%s)", StringUtils.join(duplicateItemIdList, ",")));
        }
    }

}
