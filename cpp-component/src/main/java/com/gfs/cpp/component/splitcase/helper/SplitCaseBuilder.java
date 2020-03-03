package com.gfs.cpp.component.splitcase.helper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.AmountType;
import com.gfs.corp.component.price.common.types.LessCasePriceRule;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.proxy.ItemServiceProxy;

@Component
public class SplitCaseBuilder {

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private ItemServiceProxy itemServiceProxy;

    public SplitCaseDO buildSplitCaseDO(int contractPriceProfileSeq, SplitCaseDTO splitCaseDTO, int cmgCustomerType) {
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        splitCaseDO.setProductType(splitCaseDTO.getProductType());
        splitCaseDO.setSplitCaseFee(
                splitCaseDTO.getSplitCaseFee() != null ? Double.parseDouble(splitCaseDTO.getSplitCaseFee()) : CPPConstants.EMPTY_SPLIT_CASE_FEE);
        splitCaseDO.setUnit(splitCaseDTO.getUnit());
        splitCaseDO.setEffectiveDate(cppDateUtils.getFutureDate());
        splitCaseDO.setExpirationDate(cppDateUtils.getFutureDate());
        splitCaseDO.setItemPriceId(splitCaseDTO.getItemPriceId());
        splitCaseDO.setGfsCustomerTypeCode(cmgCustomerType);
        splitCaseDO.setLessCaseRuleId(splitCaseDTO.getLessCaseRuleId());
        return splitCaseDO;
    }

    public List<SplitCaseDTO> buildDefaultSplitCaseGridDTO(Date pricingEffectiveDate, Date pricingExpirationDate) {
        List<SplitCaseDTO> splitCaseGridDTOList = new ArrayList<>();
        Map<Integer, String> offeringMap = itemServiceProxy.getAllProductTypesById();
        for (Entry<Integer, String> offeringEntry : offeringMap.entrySet()) {
            SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
            splitCaseDTO.setItemPriceId(offeringEntry.getKey().toString());
            splitCaseDTO.setProductType(offeringEntry.getValue());
            splitCaseDTO.setSplitCaseFee(CPPConstants.SPLIT_CASE_FEE);
            splitCaseDTO.setUnit(AmountType.PERCENT.getCode());
            splitCaseDTO.setEffectiveDate(pricingEffectiveDate);
            splitCaseDTO.setExpirationDate(pricingExpirationDate);
            splitCaseDTO.setLessCaseRuleId(LessCasePriceRule.FULL_CASE_PRICE_DIV_PACK.getCode());
            splitCaseGridDTOList.add(splitCaseDTO);
        }
        return splitCaseGridDTOList;
    }

    public List<SplitCaseDO> buildExistingSplitCaseList(SplitCaseGridDTO splitCaseDTO, List<SplitCaseDO> existingSplitCaseList) {
        Map<Integer, String> offeringMap = itemServiceProxy.getAllProductTypesById();
        for (SplitCaseDO splitCase : existingSplitCaseList) {
            splitCase.setProductType(offeringMap.get(Integer.parseInt(splitCase.getItemPriceId())));
            splitCase.setContractPriceProfileSeq(splitCaseDTO.getContractPriceProfileSeq());
            splitCase.setGfsCustomerTypeCode(CPPConstants.CMG_CUSTOMER_TYPE_CODE);
        }
        return existingSplitCaseList;
    }

    public SplitCaseDO buildUpdatedSplitCaseListForFurtherance(SplitCaseDTO splitCaseDTO, int contractPriceProfileSeq) {
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        if (splitCaseDTO.getSplitCaseFee() != null) {
            splitCaseDO.setContractPriceProfileSeq(contractPriceProfileSeq);
            splitCaseDO.setEffectiveDate(cppDateUtils.getFutureDate());
            splitCaseDO.setExpirationDate(cppDateUtils.getFutureDate());
            splitCaseDO.setItemPriceId(splitCaseDTO.getItemPriceId());
            splitCaseDO.setLessCaseRuleId(splitCaseDTO.getLessCaseRuleId());
            splitCaseDO.setSplitCaseFee(Double.parseDouble(splitCaseDTO.getSplitCaseFee()));
            splitCaseDO.setUnit(splitCaseDTO.getUnit());
        }
        return splitCaseDO;
    }

    public List<SplitCaseDTO> fetchSavedSplitCaseGridDTO(Date pricingEffectiveDate, Date pricingExpirationDate, List<SplitCaseDO> result) {
        Map<Integer, String> offeringMap = itemServiceProxy.getAllProductTypesById();
        List<SplitCaseDTO> splitCaseGridDTOList = new ArrayList<>();
        for (SplitCaseDO splitCase : result) {
            buildSplitCaseGridDTO(pricingEffectiveDate, pricingExpirationDate, offeringMap, splitCaseGridDTOList, splitCase);
        }
        return splitCaseGridDTOList;
    }

    private void buildSplitCaseGridDTO(Date pricingEffectiveDate, Date pricingExpirationDate, Map<Integer, String> offeringMap,
            List<SplitCaseDTO> splitCaseDTOList, SplitCaseDO splitCase) {
        DecimalFormat decimalAmountFormat = new DecimalFormat(CPPConstants.AMOUNT_FORMAT);
        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        splitCaseDTO.setProductType(offeringMap.get(Integer.parseInt(splitCase.getItemPriceId())));
        splitCaseDTO.setEffectiveDate(pricingEffectiveDate);
        splitCaseDTO.setExpirationDate(pricingExpirationDate);
        splitCaseDTO.setSplitCaseFee(decimalAmountFormat.format(splitCase.getSplitCaseFee()));
        splitCaseDTO.setItemPriceId(splitCase.getItemPriceId());
        splitCaseDTO.setUnit(splitCase.getUnit());
        splitCaseDTO.setLessCaseRuleId(splitCase.getLessCaseRuleId());
        splitCaseDTOList.add(splitCaseDTO);
    }
}
