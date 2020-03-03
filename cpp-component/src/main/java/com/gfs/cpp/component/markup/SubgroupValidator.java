package com.gfs.cpp.component.markup;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class SubgroupValidator {

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    public static final Logger logger = LoggerFactory.getLogger(SubgroupValidator.class);

    public void validateIfInvalidSubgroup(String subgroupDescription, String subgroupId) {

        if (StringUtils.isBlank(subgroupDescription)) {
            logger.error("Invalid subgroup found for subgroup id {} ", subgroupId);
            throw new CPPRuntimeException(CPPExceptionType.INVALID_SUBGROUP, "Invalid Subgroup found");
        }
    }

    public void validateIfSubgroupAlreadyExist(String cmgCustomerId, int cmgCustomerTypeCode, int contractPriceProfileSeq, String subgroupId) {
        List<String> subgroupIdList = new ArrayList<>();
        subgroupIdList.add(subgroupId);
        List<String> duplicateSubgroupId = customerItemPriceRepository.fetchAlreadyExistingItemsOrSubgroups(subgroupIdList, cmgCustomerId,
                cmgCustomerTypeCode, contractPriceProfileSeq, ItemPriceLevel.SUBGROUP.getCode());

        if (!CollectionUtils.isEmpty(duplicateSubgroupId) && duplicateSubgroupId.contains(subgroupId)) {
            logger.error("Duplicate Subgroup found for subgroup id {} ", subgroupId);
            throw new CPPRuntimeException(CPPExceptionType.SUBGROUP_ALREADY_EXIST, "Duplicate Subgroup found");
        }
    }

}
