package com.gfs.cpp.component.splitcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.component.splitcase.helper.SplitCaseBuilder;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@Service("splitCaseService")
public class SplitCaseService {

    @Autowired
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    @Autowired
    private SplitCaseBuilder splitCaseBuilder;

    public final Logger logger = LoggerFactory.getLogger(SplitCaseService.class);

    public SplitCaseGridDTO fetchSplitCaseFee(int contractPriceProfileSeq, Date pricingEffectiveDate, Date pricingExpirationDate) {
        SplitCaseGridDTO splitCaseDTO = new SplitCaseGridDTO();
        List<SplitCaseDO> result = prcProfLessCaseRuleRepository.fetchSplitCaseGridForCMG(contractPriceProfileSeq);
        if (result.isEmpty()) {
            splitCaseDTO.setSplitCaseFeeValues(splitCaseBuilder.buildDefaultSplitCaseGridDTO(pricingEffectiveDate, pricingExpirationDate));
        } else {
            splitCaseDTO.setIsSplitCaseSaved(true);
            splitCaseDTO.setSplitCaseFeeValues(splitCaseBuilder.fetchSavedSplitCaseGridDTO(pricingEffectiveDate, pricingExpirationDate, result));
        }
        sortSplitCaseFeesByItemId(splitCaseDTO.getSplitCaseFeeValues());
        return splitCaseDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void saveOrUpdateSplitCase(SplitCaseGridDTO splitCaseGridDTO, String userName) {
        contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(splitCaseGridDTO.getContractPriceProfileSeq());
        List<SplitCaseDO> splitCaseList = new ArrayList<>(splitCaseGridDTO.getSplitCaseFeeValues().size());
        for (SplitCaseDTO splitCaseDTO : splitCaseGridDTO.getSplitCaseFeeValues()) {
            SplitCaseDO splitCaseDO = splitCaseBuilder.buildSplitCaseDO(splitCaseGridDTO.getContractPriceProfileSeq(), splitCaseDTO,
                    CPPConstants.CMG_CUSTOMER_TYPE_CODE);
            splitCaseList.add(splitCaseDO);
        }
        CMGCustomerResponseDTO cmgCustomerResponseDTO = contractPriceProfCustomerRepository.fetchDefaultCmg(splitCaseGridDTO.getContractPriceProfileSeq());
        List<SplitCaseDO> existingSplitCaseList = prcProfLessCaseRuleRepository.fetchSplitCaseGridForCMG(splitCaseGridDTO.getContractPriceProfileSeq());
        if (existingSplitCaseList.isEmpty()) {
            prcProfLessCaseRuleRepository.saveSplitCase(splitCaseList, cmgCustomerResponseDTO.getId(), userName);
        } else {
            existingSplitCaseList = splitCaseBuilder.buildExistingSplitCaseList(splitCaseGridDTO, existingSplitCaseList);
            updateSplitCaseForCMG(splitCaseList, existingSplitCaseList, cmgCustomerResponseDTO, userName);
        }
    }

    private void updateSplitCaseForCMG(List<SplitCaseDO> splitCaseList, List<SplitCaseDO> oldList, CMGCustomerResponseDTO cmgCustomerResponseDTO,
            String userName) {

        Collection<SplitCaseDO> updatedSplitcaseList = CollectionUtils.subtract(splitCaseList, oldList);
        if (CollectionUtils.isNotEmpty(updatedSplitcaseList)) {
            prcProfLessCaseRuleRepository.updateSplitCase(updatedSplitcaseList, userName, cmgCustomerResponseDTO.getId(),
                    cmgCustomerResponseDTO.getTypeCode());
        }

    }
    
    private void sortSplitCaseFeesByItemId(List<SplitCaseDTO> splitCaseItemList ) {
    	Collections.sort(splitCaseItemList, new Comparator<SplitCaseDTO>() {
    		public int compare(SplitCaseDTO o1, SplitCaseDTO o2) {
                Integer itemPriceId1 = Integer.parseInt(o1.getItemPriceId());
                Integer itemPriceId2 = Integer.parseInt(o2.getItemPriceId());
                return (itemPriceId1).compareTo(itemPriceId2);
    		}
    	});
    }

}
