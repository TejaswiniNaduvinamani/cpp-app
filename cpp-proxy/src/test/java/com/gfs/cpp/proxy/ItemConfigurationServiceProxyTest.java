package com.gfs.cpp.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.item.common.dto.ItemSubCategoryDTO;
import com.gfs.corp.item.common.service.ItemConfigurationService;
import com.gfs.cpp.common.constants.CPPConstants;

@RunWith(MockitoJUnitRunner.class)
public class ItemConfigurationServiceProxyTest {

    @InjectMocks
    private ItemConfigurationServiceProxy target;

    @Mock
    private ItemConfigurationService itemConfigurationService;

    @Test
    public void shouldFindItemSubgroupInformation() {

        List<ItemSubCategoryDTO> itemSubCategoryDTOList = new ArrayList<>();
        ItemSubCategoryDTO itemSubCategoryDTO = new ItemSubCategoryDTO();
        itemSubCategoryDTO.setSubCategoryId(11);
        itemSubCategoryDTO.setDescription("TABLETOP MISC");
        itemSubCategoryDTOList.add(itemSubCategoryDTO);
        Map<String, String> subgroupInformationMap = new HashMap<>();

        when(itemConfigurationService.getItemSubCategories(CPPConstants.LANGUAGE_CODE)).thenReturn(itemSubCategoryDTOList);

        subgroupInformationMap = target.getAllSubCategories(CPPConstants.LANGUAGE_CODE);

        assertThat(subgroupInformationMap.get(itemSubCategoryDTO.getSubCategoryId().toString()), equalTo(itemSubCategoryDTO.getDescription()));

        verify(itemConfigurationService).getItemSubCategories(CPPConstants.LANGUAGE_CODE);

    }

    @Test
    public void shouldGetSubgroupDescriptionById() {

        List<ItemSubCategoryDTO> itemSubCategoryDTOList = new ArrayList<>();
        ItemSubCategoryDTO itemSubCategoryDTO = new ItemSubCategoryDTO();
        itemSubCategoryDTO.setSubCategoryId(11);
        itemSubCategoryDTO.setDescription("TABLETOP MISC");
        itemSubCategoryDTOList.add(itemSubCategoryDTO);

        when(itemConfigurationService.getItemSubCategories(CPPConstants.LANGUAGE_CODE)).thenReturn(itemSubCategoryDTOList);

        String subgroupDesc = target.getSubgroupDescriptionById("11");

        assertThat(subgroupDesc, equalTo(itemSubCategoryDTO.getDescription()));

        verify(itemConfigurationService).getItemSubCategories(CPPConstants.LANGUAGE_CODE);

    }

}
