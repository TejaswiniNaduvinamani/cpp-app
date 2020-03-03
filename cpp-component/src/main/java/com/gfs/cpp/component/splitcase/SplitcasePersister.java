package com.gfs.cpp.component.splitcase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.furtherance.SplitCaseGridFurtheranceDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.furtherance.FurtheranceChangeTracker;
import com.gfs.cpp.component.splitcase.helper.SplitCaseBuilder;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@Component
public class SplitcasePersister {

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Autowired
    private SplitCaseBuilder splitCaseBuilder;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private FurtheranceChangeTracker furtheranceChangeTracker;

    public void saveSplicateForFurtherance(SplitCaseGridFurtheranceDTO splitCaseGridFurtheranceDTO, String userName) {
        Map<String, SplitCaseDO> existingSplitCaseByItemPriceId = buildExistingSplitCaseByItemPriceId(
                splitCaseGridFurtheranceDTO.getContractPriceProfileSeq());

        if (!existingSplitCaseByItemPriceId.isEmpty()) {
            CMGCustomerResponseDTO cmgCustomerResponseDTO = contractPriceProfCustomerRepository
                    .fetchDefaultCmg(splitCaseGridFurtheranceDTO.getContractPriceProfileSeq());
            List<SplitCaseDO> updatedSplitCaseDOList = extractUpdatedSplitCaseListForFurtherance(splitCaseGridFurtheranceDTO,
                    existingSplitCaseByItemPriceId);
            saveSplitCaseFeeForFurtherance(updatedSplitCaseDOList, userName, splitCaseGridFurtheranceDTO.getCppFurtheranceSeq(),
                    cmgCustomerResponseDTO);
        }
    }

    private Map<String, SplitCaseDO> buildExistingSplitCaseByItemPriceId(int contractPriceProfileSeq) {
        List<SplitCaseDO> existingSplitCaseList = prcProfLessCaseRuleRepository.fetchSplitCaseGridForCMG(contractPriceProfileSeq);
        Map<String, SplitCaseDO> existingSplitCaseByItemPriceId = new HashMap<>();
        for (SplitCaseDO splitCaseDO : existingSplitCaseList) {
            SplitCaseDO existingSplitCaseDO = existingSplitCaseByItemPriceId.get(splitCaseDO.getItemPriceId());
            if (existingSplitCaseDO == null) {
                existingSplitCaseByItemPriceId.put(splitCaseDO.getItemPriceId(), splitCaseDO);
            }
        }
        return existingSplitCaseByItemPriceId;
    }

    private List<SplitCaseDO> extractUpdatedSplitCaseListForFurtherance(SplitCaseGridFurtheranceDTO splitCaseGridFurtheranceDTO,
            Map<String, SplitCaseDO> existingSplitCaseByItemPriceId) {
        List<SplitCaseDO> updatedSplitCaseDOList = new ArrayList<>();
        for (SplitCaseDTO splitCaseDTO : splitCaseGridFurtheranceDTO.getSplitCaseFeeValues()) {
            if (splitCaseDTO.getSplitCaseFee() != null && existingSplitCaseByItemPriceId.get(splitCaseDTO.getItemPriceId())
                    .getSplitCaseFee() != Double.parseDouble(splitCaseDTO.getSplitCaseFee())) {
                SplitCaseDO splitCaseDO = splitCaseBuilder.buildUpdatedSplitCaseListForFurtherance(splitCaseDTO,
                        splitCaseGridFurtheranceDTO.getContractPriceProfileSeq());
                updatedSplitCaseDOList.add(splitCaseDO);
            }
        }
        return updatedSplitCaseDOList;
    }

    private void saveSplitCaseFeeForFurtherance(List<SplitCaseDO> updatedSplitCaseDOList, String userName, int cppFurtheranceSeq,
            CMGCustomerResponseDTO cmgCustomerResponseDTO) {

        if (CollectionUtils.isNotEmpty(updatedSplitCaseDOList)) {
            prcProfLessCaseRuleRepository.expirePrcProfLessCaseRuleForFurtherance(updatedSplitCaseDOList, userName, cmgCustomerResponseDTO.getId(),
                    cmgCustomerResponseDTO.getTypeCode(), cppDateUtils.oneDayPreviousCurrentDate());
            furtheranceChangeTracker.addTrackingForSplitCaseUpdate(updatedSplitCaseDOList, userName, cppFurtheranceSeq,
                    cmgCustomerResponseDTO.getId(), cmgCustomerResponseDTO.getTypeCode());
            prcProfLessCaseRuleRepository.saveSplitCase(updatedSplitCaseDOList, cmgCustomerResponseDTO.getId(), userName);
        }
    }

}
