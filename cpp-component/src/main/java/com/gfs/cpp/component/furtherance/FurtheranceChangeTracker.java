package com.gfs.cpp.component.furtherance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceAction;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.data.furtherance.CppFurtheranceTrackingRepository;

@Component
public class FurtheranceChangeTracker {

    private static final String CUSTOMER_ITEM_PRICE = "CUSTOMER_ITEM_PRICE";

    private static final String PRC_PROF_LESSCASE_RULE = "PRC_PROF_LESSCASE_RULE";

    @Autowired
    private FurtheranceDTOBuilder furtheranceDTOBuilder;

    @Autowired
    private CppFurtheranceTrackingRepository cppFurtheranceTrackingRepository;

    public void addTrackingForMarkupUpdate(int cppFurtheranceSeq, String gfsCustomerId, int gfsCustomerTypeCode, List<String> itemIdList,
            String userName, int itemCode) {

        List<CPPFurtheranceTrackingDTO> cppFurtheranceTrackingDTOList = new ArrayList<>();

        for (String itemId : itemIdList) {

            CPPFurtheranceTrackingDTO cppFurtheranceTrackingDTO = furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq,
                    gfsCustomerId, gfsCustomerTypeCode, itemId, itemCode, CUSTOMER_ITEM_PRICE, FurtheranceAction.UPDATED.code);

            Integer existingTrackingAction = cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(cppFurtheranceTrackingDTO);

            if (existingTrackingAction == null) {
                cppFurtheranceTrackingDTO.setFurtheranceActionCode(FurtheranceAction.UPDATED.code);
                cppFurtheranceTrackingDTOList.add(cppFurtheranceTrackingDTO);
            }
        }
        if (CollectionUtils.isNotEmpty(cppFurtheranceTrackingDTOList))
            cppFurtheranceTrackingRepository.batchInsertRecordsInFurtheranceTracking(cppFurtheranceTrackingDTOList, userName);

    }

    public void addTrackingForMarkupAdd(int cppFurtheranceSeq, String gfsCustomerId, int gfsCustomerTypeCode, List<String> itemIdList,
            String userName) {

        List<CPPFurtheranceTrackingDTO> cppFurtheranceTrackingDTOList = new ArrayList<>();

        for (String itemId : itemIdList) {
            CPPFurtheranceTrackingDTO cppFurtheranceTrackingDTO = furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq,
                    gfsCustomerId, gfsCustomerTypeCode, itemId, ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE,
                    FurtheranceAction.ADDED.getCode());

            Integer existingTrackingAction = cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(cppFurtheranceTrackingDTO);

            if (existingTrackingAction != null && FurtheranceAction.DELETED.getCode() == existingTrackingAction) {
                cppFurtheranceTrackingRepository.deleteTrackingEntry(cppFurtheranceTrackingDTO, FurtheranceAction.DELETED.getCode());
                cppFurtheranceTrackingDTO.setFurtheranceActionCode(FurtheranceAction.UPDATED.getCode());
            }

            cppFurtheranceTrackingDTOList.add(cppFurtheranceTrackingDTO);
        }
        if (CollectionUtils.isNotEmpty(cppFurtheranceTrackingDTOList))
            cppFurtheranceTrackingRepository.batchInsertRecordsInFurtheranceTracking(cppFurtheranceTrackingDTOList, userName);
    }

    public void addTrackingForMarkupDelete(int cppFurtheranceSeq, String gfsCustomerId, int gfsCustomerTypeCode, List<String> itemIdList,
            String userName) {
        List<CPPFurtheranceTrackingDTO> cppFurtheranceTrackingDTOList = new ArrayList<>();

        for (String itemId : itemIdList) {
            CPPFurtheranceTrackingDTO cppFurtheranceTrackingDTO = furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq,
                    gfsCustomerId, gfsCustomerTypeCode, itemId, ItemPriceLevel.ITEM.getCode(), CUSTOMER_ITEM_PRICE,
                    FurtheranceAction.DELETED.getCode());

            Integer existingTrackingAction = cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(cppFurtheranceTrackingDTO);

            if (existingTrackingAction == null) {
                cppFurtheranceTrackingDTOList.add(cppFurtheranceTrackingDTO);
            } else {
                if (FurtheranceAction.ADDED.getCode() == existingTrackingAction) {
                    cppFurtheranceTrackingRepository.deleteTrackingEntry(cppFurtheranceTrackingDTO, FurtheranceAction.ADDED.getCode());
                } else if (FurtheranceAction.UPDATED.getCode() == existingTrackingAction) {
                    cppFurtheranceTrackingRepository.deleteTrackingEntry(cppFurtheranceTrackingDTO, FurtheranceAction.UPDATED.getCode());
                    cppFurtheranceTrackingDTOList.add(cppFurtheranceTrackingDTO);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(cppFurtheranceTrackingDTOList))
            cppFurtheranceTrackingRepository.batchInsertRecordsInFurtheranceTracking(cppFurtheranceTrackingDTOList, userName);
    }

    public void addTrackingForSplitCaseUpdate(List<SplitCaseDO> updatedSplitCaseList, String userName, int cppFurtheranceSeq, String gfsCustomerId,
            int gfsCustomerTypeCode) {
        List<CPPFurtheranceTrackingDTO> furtheranceWrapperDTOList = new ArrayList<>();
        for (SplitCaseDO splitCaseDO : updatedSplitCaseList) {

            CPPFurtheranceTrackingDTO furtheranceWrapperDTO = furtheranceDTOBuilder.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, gfsCustomerId,
                    gfsCustomerTypeCode, splitCaseDO.getItemPriceId(), ItemPriceLevel.PRODUCT_TYPE.getCode(), PRC_PROF_LESSCASE_RULE,
                    FurtheranceAction.UPDATED.getCode());

            Integer existingTrackingAction = cppFurtheranceTrackingRepository.fetchFurtheranceActionCode(furtheranceWrapperDTO);
            if (existingTrackingAction == null) {
                furtheranceWrapperDTOList.add(furtheranceWrapperDTO);
            }
        }
        if (!furtheranceWrapperDTOList.isEmpty()) {
            cppFurtheranceTrackingRepository.batchInsertRecordsInFurtheranceTracking(furtheranceWrapperDTOList, userName);
        }
    }
}
