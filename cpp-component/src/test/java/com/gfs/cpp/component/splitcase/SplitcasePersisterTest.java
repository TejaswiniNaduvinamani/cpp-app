package com.gfs.cpp.component.splitcase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.furtherance.SplitCaseGridFurtheranceDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.furtherance.FurtheranceChangeTracker;
import com.gfs.cpp.component.splitcase.SplitcasePersister;
import com.gfs.cpp.component.splitcase.helper.SplitCaseBuilder;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@RunWith(MockitoJUnitRunner.class)
public class SplitcasePersisterTest {

    private static final String itemPriceId = "1";
    private static final int gfsCustomerTypeCode = 31;
    private static final int cppFurtheranceSeq = 1;
    private static final int contractPriceProfileSeq = 1;
    private static final Date expirationDate = new LocalDate(2010, 01, 01).toDate();
    private static final Date EFFECTIVE_DATE = new LocalDate(2009, 01, 01).toDate();

    @InjectMocks
    private SplitcasePersister target;

    @Mock
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;
    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;
    @Mock
    private SplitCaseBuilder splitCaseBuilder;
    @Mock
    private CPPDateUtils cppDateUtils;
    @Mock
    private FurtheranceChangeTracker furtheranceChangeTracker;

    @Test
    public void shouldSaveSplitcaseFee() throws Exception {
        SplitCaseGridFurtheranceDTO splitCaseGridDTO = buildSplitCaseGridFurtherance();
        String userName = "vc71u";
        List<SplitCaseDO> oldList = new ArrayList<>();
        SplitCaseDO oldSplitCaseDO = buildSplitCaseDO(itemPriceId, 2);
        oldList.add(buildSplitCaseDO(itemPriceId, 2));

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(31);

        when(prcProfLessCaseRuleRepository.fetchSplitCaseGridForCMG(contractPriceProfileSeq)).thenReturn(oldList);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgCustomerResponseDTO);
        when(splitCaseBuilder.buildUpdatedSplitCaseListForFurtherance(splitCaseGridDTO.getSplitCaseFeeValues().get(0), contractPriceProfileSeq))
                .thenReturn(oldSplitCaseDO);
        when(cppDateUtils.oneDayPreviousCurrentDate()).thenReturn(expirationDate);

        target.saveSplicateForFurtherance(splitCaseGridDTO, userName);

        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);
        verify(prcProfLessCaseRuleRepository).fetchSplitCaseGridForCMG(contractPriceProfileSeq);
        verify(splitCaseBuilder).buildUpdatedSplitCaseListForFurtherance(splitCaseGridDTO.getSplitCaseFeeValues().get(0), contractPriceProfileSeq);
        verify(prcProfLessCaseRuleRepository).expirePrcProfLessCaseRuleForFurtherance(oldList, userName, "1", gfsCustomerTypeCode, expirationDate);
        verify(furtheranceChangeTracker).addTrackingForSplitCaseUpdate(oldList, userName, cppFurtheranceSeq, "1", gfsCustomerTypeCode);
        verify(prcProfLessCaseRuleRepository).saveSplitCase(oldList, "1", userName);
    }

    private SplitCaseGridFurtheranceDTO buildSplitCaseGridFurtherance() {
        List<SplitCaseDTO> splitCaseFeeValues = new ArrayList<>();
        SplitCaseDTO splitCaseDTO = buildSplitCaseDTO(itemPriceId, "1");
        splitCaseFeeValues.add(splitCaseDTO);

        SplitCaseGridFurtheranceDTO splitCaseGridDTO = new SplitCaseGridFurtheranceDTO();
        splitCaseGridDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        splitCaseGridDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        splitCaseGridDTO.setSplitCaseFeeValues(splitCaseFeeValues);
        return splitCaseGridDTO;
    }

    private SplitCaseDTO buildSplitCaseDTO(String itemPriceId, String splitCaseFee) {
        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        splitCaseDTO.setEffectiveDate(EFFECTIVE_DATE);
        splitCaseDTO.setExpirationDate(expirationDate);
        splitCaseDTO.setLessCaseRuleId(1);
        splitCaseDTO.setUnit("$");
        splitCaseDTO.setSplitCaseFee(splitCaseFee);
        splitCaseDTO.setItemPriceId(itemPriceId);
        return splitCaseDTO;
    }

    private SplitCaseDO buildSplitCaseDO(String itemPriceId, double splitcaseFee) {
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        splitCaseDO.setProductType("1");
        splitCaseDO.setSplitCaseFee(splitcaseFee);
        splitCaseDO.setUnit("$");
        splitCaseDO.setEffectiveDate(EFFECTIVE_DATE);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setItemPriceId(itemPriceId);
        splitCaseDO.setGfsCustomerTypeCode(31);
        splitCaseDO.setLessCaseRuleId(2);
        return splitCaseDO;
    }

}
