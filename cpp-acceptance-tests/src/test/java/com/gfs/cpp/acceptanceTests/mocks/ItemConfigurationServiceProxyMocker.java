package com.gfs.cpp.acceptanceTests.mocks;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.item.common.dto.ItemSubCategoryDTO;
import com.gfs.corp.item.common.service.ItemConfigurationService;

@Component
public class ItemConfigurationServiceProxyMocker implements Resettable {

    @Autowired
    private ItemConfigurationService itemConfigurationService;

    Map<String, String> subCategoryInformationMap = new HashMap<>();

    List<ItemSubCategoryDTO> itemSubCategoryDTOList = new ArrayList<>();

    @Override
    public void reset() {

        when(itemConfigurationService.getItemSubCategories(eq("en"))).then(new Answer<List<ItemSubCategoryDTO>>() {
            @Override
            public List<ItemSubCategoryDTO> answer(InvocationOnMock invocation) throws Throwable {
                ItemSubCategoryDTO itemSubCategoryDTO = new ItemSubCategoryDTO();
                itemSubCategoryDTO.setSubCategoryId(11);
                itemSubCategoryDTO.setDescription("TABLETOP MISC.");
                itemSubCategoryDTOList.add(itemSubCategoryDTO);
                itemSubCategoryDTO = new ItemSubCategoryDTO();
                itemSubCategoryDTO.setSubCategoryId(12);
                itemSubCategoryDTO.setDescription("FLOOR CARE");
                itemSubCategoryDTOList.add(itemSubCategoryDTO);
                return itemSubCategoryDTOList;
            }
        });

    }

}
