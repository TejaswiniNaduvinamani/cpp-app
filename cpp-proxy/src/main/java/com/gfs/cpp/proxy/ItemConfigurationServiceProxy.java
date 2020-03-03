package com.gfs.cpp.proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.gfs.corp.item.common.dto.ItemSubCategoryDTO;
import com.gfs.corp.item.common.service.ItemConfigurationService;
import com.gfs.cpp.common.constants.CPPConstants;

@Component
public class ItemConfigurationServiceProxy {

    @Autowired
    private ItemConfigurationService itemConfigurationService;

    public static final Logger logger = LoggerFactory.getLogger(ItemConfigurationServiceProxy.class);

    @Cacheable(value = "subCategoryCache")
    public Map<String, String> getAllSubCategories(String languageTypeCode) {

        logger.info("ItemConfigurationService is called to fetch subgroup information for caching");

        Map<String, String> subCategoryInformationMap = new HashMap<>();

        List<ItemSubCategoryDTO> itemSubCategoryDTOList = itemConfigurationService.getItemSubCategories(languageTypeCode);

        for (ItemSubCategoryDTO itemSubCategoryDTO : itemSubCategoryDTOList) {
            subCategoryInformationMap.put(itemSubCategoryDTO.getSubCategoryId().toString(), itemSubCategoryDTO.getDescription());
        }

        return subCategoryInformationMap;
    }

    public String getSubgroupDescriptionById(String subgroupId) {
        Map<String, String> subgroupMap = getAllSubCategories(CPPConstants.LANGUAGE_CODE);
        return subgroupMap.get(subgroupId);
    }

}
