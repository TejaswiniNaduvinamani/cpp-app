package com.gfs.cpp.component.splitcase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.splitcase.helper.SplitCaseBuilder;
import com.gfs.cpp.proxy.ItemServiceProxy;

@Configuration
@RunWith(MockitoJUnitRunner.class)
public class SplitCaseBuilderTest {

    @InjectMocks
    private SplitCaseBuilder target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private ItemServiceProxy itemServiceProxy;

    @Test
    public void buildSplitcaseDOWithSplitCaseFee() {
        int contractPriceProfileSeq = 1;
        int cmgCustomerType = 31;
        Date effectiveDate = new LocalDate(9999, 01, 01).toDate();
        Date expirationDate = new LocalDate(9999, 01, 01).toDate();
        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        splitCaseDTO.setEffectiveDate(effectiveDate);
        splitCaseDTO.setExpirationDate(expirationDate);
        splitCaseDTO.setItemPriceId("1");
        splitCaseDTO.setLessCaseRuleId(2);
        splitCaseDTO.setProductType("Meal");
        splitCaseDTO.setUnit("%");
        splitCaseDTO.setSplitCaseFee(CPPConstants.SPLIT_CASE_FEE);

        when(cppDateUtils.getFutureDate()).thenReturn(new LocalDate(9999, 01, 01).toDate());

        SplitCaseDO result = target.buildSplitCaseDO(contractPriceProfileSeq, splitCaseDTO, cmgCustomerType);

        assertThat(result.getEffectiveDate(), equalTo(new LocalDate(9999, 01, 01).toDate()));
        assertThat(result.getExpirationDate(), equalTo(new LocalDate(9999, 01, 01).toDate()));
        assertThat(result.getUnit(), equalTo("%"));
        assertThat(result.getItemPriceId(), equalTo("1"));
        assertThat(result.getProductType(), equalTo("Meal"));
        assertThat(result.getLessCaseRuleId(), equalTo(2));
        assertThat(result.getSplitCaseFee(), equalTo(35.00));

        verify(cppDateUtils, atLeastOnce()).getFutureDate();
    }

    @Test
    public void buildSplitcaseDOWithNoSplitCaseFee() {
        int contractPriceProfileSeq = 1;
        int cmgCustomerType = 31;
        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        splitCaseDTO.setEffectiveDate(new LocalDate(9999, 01, 01).toDate());
        splitCaseDTO.setExpirationDate(new LocalDate(9999, 01, 01).toDate());
        splitCaseDTO.setItemPriceId("1");
        splitCaseDTO.setProductType("Meal");
        splitCaseDTO.setLessCaseRuleId(2);
        splitCaseDTO.setUnit("%");

        SplitCaseDO result = target.buildSplitCaseDO(contractPriceProfileSeq, splitCaseDTO, cmgCustomerType);

        assertThat(result.getUnit(), equalTo("%"));
        assertThat(result.getItemPriceId(), equalTo("1"));
        assertThat(result.getProductType(), equalTo("Meal"));
        assertThat(result.getLessCaseRuleId(), equalTo(2));
        assertThat(result.getSplitCaseFee(), equalTo(CPPConstants.EMPTY_SPLIT_CASE_FEE));
    }

    @Test
    public void buildExistingSplitCaseList() {
        SplitCaseGridDTO splitCaseGridDTO = new SplitCaseGridDTO();
        Date pricingEffectiveDate = new LocalDate(9999, 01, 01).toDate();
        Date pricingExpirationDate = new LocalDate(9999, 01, 01).toDate();
        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        List<SplitCaseDTO> splitCaseGridDTOs = new ArrayList<>();
        splitCaseDTO.setEffectiveDate(pricingEffectiveDate);
        splitCaseDTO.setExpirationDate(pricingExpirationDate);
        splitCaseDTO.setItemPriceId("1");
        splitCaseDTO.setProductType("Meal");
        splitCaseGridDTOs.add(splitCaseDTO);
        splitCaseGridDTO.setSplitCaseFeeValues(splitCaseGridDTOs);
        splitCaseGridDTO.setContractPriceProfileSeq(1);
        Date effectiveDate = new LocalDate(9999, 01, 01).toDate();
        Date expirationDate = new LocalDate(9999, 01, 01).toDate();
        List<SplitCaseDO> existingSplitCaseList = new ArrayList<>();
        SplitCaseDO existingSplitCaseDO = new SplitCaseDO();
        existingSplitCaseDO.setContractPriceProfileSeq(1);
        existingSplitCaseDO.setEffectiveDate(pricingEffectiveDate);
        existingSplitCaseDO.setExpirationDate(pricingExpirationDate);
        existingSplitCaseDO.setGfsCustomerTypeCode(12);
        existingSplitCaseDO.setItemPriceId("1");
        existingSplitCaseDO.setProductType("Veg");
        existingSplitCaseDO.setSplitCaseFee(1);
        existingSplitCaseDO.setUnit("$");
        existingSplitCaseList.add(existingSplitCaseDO);
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        Map<Integer, String> offeringMap = new HashMap<>();
        offeringMap.put(1, "Test");

        when(itemServiceProxy.getAllProductTypesById()).thenReturn(offeringMap);

        List<SplitCaseDO> result = target.buildExistingSplitCaseList(splitCaseGridDTO, existingSplitCaseList);

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getContractPriceProfileSeq(), equalTo(1));
        assertThat(result.get(0).getEffectiveDate(), equalTo(effectiveDate));
        assertThat(result.get(0).getExpirationDate(), equalTo(expirationDate));
        assertThat(result.get(0).getUnit(), equalTo("$"));
        assertThat(result.get(0).getItemPriceId(), equalTo("1"));
        assertThat(result.get(0).getProductType(), equalTo("Test"));
        assertThat(result.get(0).getSplitCaseFee(), equalTo(1.0));

        verify(itemServiceProxy).getAllProductTypesById();
    }

    @Test
    public void fetchSavedSplitCaseGridDTO() {
        Date effectiveDate = new LocalDate(9999, 01, 01).toDate();
        Date expirationDate = new LocalDate(9999, 01, 01).toDate();
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setItemPriceId("1");
        splitCaseDO.setUnit("%");
        splitCaseDO.setSplitCaseFee(35);
        List<SplitCaseDO> splitCaseDOList = new ArrayList<SplitCaseDO>();
        splitCaseDOList.add(splitCaseDO);
        Map<Integer, String> offeringMap = new HashMap<>();
        offeringMap.put(1, "Test");

        when(itemServiceProxy.getAllProductTypesById()).thenReturn(offeringMap);

        List<SplitCaseDTO> result = target.fetchSavedSplitCaseGridDTO(effectiveDate, expirationDate, splitCaseDOList);

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getProductType(), equalTo("Test"));
        assertThat(result.get(0).getSplitCaseFee(), equalTo(CPPConstants.SPLIT_CASE_FEE));
        assertThat(result.get(0).getEffectiveDate(), equalTo(effectiveDate));
        assertThat(result.get(0).getExpirationDate(), equalTo(expirationDate));
        assertThat(result.get(0).getItemPriceId(), equalTo("1"));
        assertThat(result.get(0).getUnit(), equalTo("%"));

        verify(itemServiceProxy).getAllProductTypesById();
    }

    @Test
    public void buildDefaultSplitCaseGridDTO() {
        Map<Integer, String> offeringMap = new HashMap<>();
        offeringMap.put(1, "Test");
        Date effectiveDate = new LocalDate(9999, 01, 01).toDate();
        Date expirationDate = new LocalDate(9999, 01, 01).toDate();

        when(itemServiceProxy.getAllProductTypesById()).thenReturn(offeringMap);

        List<SplitCaseDTO> result = target.buildDefaultSplitCaseGridDTO(effectiveDate, expirationDate);

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getProductType(), equalTo("Test"));
        assertThat(result.get(0).getSplitCaseFee(), equalTo(CPPConstants.SPLIT_CASE_FEE));
        assertThat(result.get(0).getEffectiveDate(), equalTo(effectiveDate));
        assertThat(result.get(0).getExpirationDate(), equalTo(expirationDate));
        assertThat(result.get(0).getItemPriceId(), equalTo("1"));
        assertThat(result.get(0).getUnit(), equalTo("%"));

        verify(itemServiceProxy).getAllProductTypesById();
    }

    @Test
    public void shouldBuildUpdatedSplitCaseListForFurtherance() {

        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        splitCaseDTO.setSplitCaseFee("0.0");
        SplitCaseDO splitCaseDO = target.buildUpdatedSplitCaseListForFurtherance(splitCaseDTO, 1);

        assertThat(splitCaseDTO.getEffectiveDate(), equalTo(splitCaseDO.getEffectiveDate()));
        assertThat(splitCaseDTO.getExpirationDate(), equalTo(splitCaseDO.getExpirationDate()));
        assertThat(splitCaseDTO.getProductType(), equalTo(splitCaseDO.getProductType()));
        assertThat(splitCaseDTO.getItemPriceId(), equalTo(splitCaseDO.getItemPriceId()));
        assertThat(splitCaseDTO.getLessCaseRuleId(), equalTo(splitCaseDO.getLessCaseRuleId()));
        assertThat(splitCaseDTO.getSplitCaseFee(), equalTo(String.valueOf(splitCaseDO.getSplitCaseFee())));
        assertThat(splitCaseDTO.getUnit(), equalTo(splitCaseDO.getUnit()));
    }
}
