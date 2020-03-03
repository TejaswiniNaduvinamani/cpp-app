package com.gfs.cpp.component.markup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.markup.MarkupWrapperDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class MarkupSaver {

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;
    @Autowired
    private MarkupDOBuilder markupDOBuilder;
    @Autowired
    private FutureItemUpdater futureItemUpdater;

    public void saveMarkups(MarkupWrapperDTO markupWrapper, int contractPriceProfileSeq, String userName) {
        List<ProductTypeMarkupDO> newMarkupList = new ArrayList<>();
        List<ProductTypeMarkupDO> updateMarkupList = new ArrayList<>();
        Map<Integer, ProductTypeMarkupDTO> savedExistingMarkupMap = new HashMap<>();

        List<ProductTypeMarkupDTO> savedMarkupList = customerItemPriceRepository.fetchAllMarkup(contractPriceProfileSeq,
                markupWrapper.getGfsCustomerId());
        MarkupWrapperDO markupWrapperDO = markupDOBuilder.buildMarkupWrapperDO(markupWrapper, contractPriceProfileSeq, userName);

        for (ProductTypeMarkupDTO markup : savedMarkupList) {
            savedExistingMarkupMap.put(markup.getItemPriceId(), markup);
        }

        for (ProductTypeMarkupDO markup : markupWrapperDO.getMarkupList()) {
            if (savedExistingMarkupMap.containsKey(markup.getItemPriceId())) {
                updateMarkupList.add(markup);
            } else {
                newMarkupList.add(markup);
            }
        }

        if (CollectionUtils.isNotEmpty(updateMarkupList)) {
            customerItemPriceRepository.updateMarkup(updateMarkupList, userName, contractPriceProfileSeq);
        }
        if (CollectionUtils.isNotEmpty(newMarkupList)) {
            customerItemPriceRepository.saveMarkup(newMarkupList, userName, contractPriceProfileSeq);
        }

        if (CollectionUtils.isNotEmpty(markupWrapperDO.getFutureItemList())) {
            futureItemUpdater.saveFutureItems(markupWrapper.getGfsCustomerId(), markupWrapper.getGfsCustomerType(), contractPriceProfileSeq,
                    markupWrapperDO.getFutureItemList(), userName);
        }
    }

}
