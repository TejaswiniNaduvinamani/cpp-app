package com.gfs.cpp.component.splitcase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.common.product.aquisition.ProductTypeDTO;
import com.gfs.cpp.component.splitcase.SplitCaseService;
import com.gfs.cpp.component.splitcase.helper.SplitCaseBuilder;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@Configuration
@RunWith(MockitoJUnitRunner.class)
public class SplitCaseServiceTest {

    @InjectMocks
    private SplitCaseService splitCaseService;

    @Mock
    NamedParameterJdbcTemplate cppJdbcTemplate;

    @Mock
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Captor
    private ArgumentCaptor<Collection<SplitCaseDO>> updatedSplitcaseList;

    @Mock
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    @Mock
    private SplitCaseBuilder splitCaseBuilder;

    @Test
    public void fetchSplitCaseFeeDefaultTest() {
        int contractPriceProfileSeq = 1;
        Date effectiveDate = new LocalDate(2017, 01, 01).toDate();
        Date expirationDate = new LocalDate(2017, 01, 01).toDate();
        ProductTypeDTO productTypeDTO = new ProductTypeDTO();
        productTypeDTO.setOfferingCategoryId(1);
        productTypeDTO.setOfferingCategoryDescription("Test");
        List<ProductTypeDTO> productTypeDTOs = new ArrayList<>();
        productTypeDTOs.add(productTypeDTO);
        SplitCaseDTO splitCaseGridDTO = new SplitCaseDTO();
        List<SplitCaseDTO> splitCaseGridDTOList = new ArrayList<>();
        splitCaseGridDTO.setEffectiveDate(effectiveDate);
        splitCaseGridDTO.setSplitCaseFee(CPPConstants.SPLIT_CASE_FEE);
        splitCaseGridDTO.setExpirationDate(expirationDate);
        splitCaseGridDTO.setItemPriceId("1");
        splitCaseGridDTO.setProductType("Meal");
        splitCaseGridDTO.setUnit("%");
        splitCaseGridDTOList.add(splitCaseGridDTO);

        when(splitCaseBuilder.buildDefaultSplitCaseGridDTO(effectiveDate, expirationDate)).thenReturn(splitCaseGridDTOList);

        final SplitCaseGridDTO splitCaseDTO = splitCaseService.fetchSplitCaseFee(contractPriceProfileSeq, effectiveDate, expirationDate);
        List<SplitCaseDTO> result = splitCaseDTO.getSplitCaseFeeValues();

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getEffectiveDate(), equalTo(effectiveDate));
        assertThat(result.get(0).getExpirationDate(), equalTo(expirationDate));
        assertThat(result.get(0).getUnit(), equalTo("%"));
        assertThat(result.get(0).getItemPriceId(), equalTo("1"));
        assertThat(result.get(0).getProductType(), equalTo("Meal"));
        assertThat(result.get(0).getSplitCaseFee(), equalTo("35.00"));

    }

    @Test
    public void fetchSplitCaseFeeTest() throws KeyManagementException, NoSuchAlgorithmException, IOException, ParseException {
        int contractPriceProfileSeq = 1;
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setItemPriceId("1");
        splitCaseDO.setSplitCaseFee(35);
        List<SplitCaseDO> splitCaseDOList = new ArrayList<SplitCaseDO>();
        splitCaseDOList.add(splitCaseDO);
        SplitCaseDTO splitCaseGridDTO = new SplitCaseDTO();
        SplitCaseDTO splitCaseGridDTO2 = new SplitCaseDTO();
        List<SplitCaseDTO> splitCaseGridDTOList = new ArrayList<>();
        splitCaseGridDTO.setEffectiveDate(effectiveDate);
        splitCaseGridDTO.setSplitCaseFee(CPPConstants.SPLIT_CASE_FEE);
        splitCaseGridDTO.setExpirationDate(expirationDate);
        splitCaseGridDTO.setItemPriceId("1");
        splitCaseGridDTO.setProductType("Meal");
        splitCaseGridDTO2.setEffectiveDate(effectiveDate);
        splitCaseGridDTO2.setSplitCaseFee(CPPConstants.SPLIT_CASE_FEE);
        splitCaseGridDTO2.setExpirationDate(expirationDate);
        splitCaseGridDTO2.setItemPriceId("2");
        splitCaseGridDTO2.setProductType("Frozen");
        splitCaseGridDTOList.add(splitCaseGridDTO);
        splitCaseGridDTOList.add(splitCaseGridDTO2);

        when(prcProfLessCaseRuleRepository.fetchSplitCaseGridForCMG(eq(contractPriceProfileSeq))).thenReturn(splitCaseDOList);
        when(splitCaseBuilder.fetchSavedSplitCaseGridDTO(effectiveDate, expirationDate, splitCaseDOList)).thenReturn(splitCaseGridDTOList);

        final SplitCaseGridDTO splitCaseDTO = splitCaseService.fetchSplitCaseFee(contractPriceProfileSeq, effectiveDate, expirationDate);
        List<SplitCaseDTO> result = splitCaseDTO.getSplitCaseFeeValues();

        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0).getEffectiveDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE)));
        assertThat(result.get(0).getExpirationDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE)));
        assertThat(result.get(0).getItemPriceId(), equalTo("1"));
        assertThat(result.get(0).getProductType(), equalTo("Meal"));
        assertThat(result.get(0).getSplitCaseFee(), equalTo("35.00"));
        assertThat(result.get(1).getItemPriceId(), equalTo("2"));
        assertThat(result.get(1).getProductType(), equalTo("Frozen"));
        assertThat(result.get(1).getSplitCaseFee(), equalTo("35.00"));

        verify(prcProfLessCaseRuleRepository).fetchSplitCaseGridForCMG(eq(contractPriceProfileSeq));
    }

    @Test
    public void saveSplitCaseTest() throws Exception {
        SplitCaseGridDTO splitCaseDTO = new SplitCaseGridDTO();
        Date effDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        SplitCaseDTO splitCaseGridDTO = new SplitCaseDTO();
        List<SplitCaseDTO> splitCaseGridDTOs = new ArrayList<>();
        splitCaseGridDTO.setEffectiveDate(effDate);
        splitCaseGridDTO.setExpirationDate(expDate);
        splitCaseGridDTO.setItemPriceId("1");
        splitCaseGridDTO.setLessCaseRuleId(2);
        splitCaseGridDTO.setProductType("Meal");
        splitCaseGridDTOs.add(splitCaseGridDTO);
        splitCaseDTO.setSplitCaseFeeValues(splitCaseGridDTOs);
        splitCaseDTO.setContractPriceProfileSeq(1);
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        splitCaseDO.setContractPriceProfileSeq(splitCaseDTO.getContractPriceProfileSeq());
        splitCaseDO.setProductType(splitCaseGridDTO.getProductType());
        splitCaseDO.setSplitCaseFee(0);
        splitCaseDO.setUnit(splitCaseGridDTO.getUnit());
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setItemPriceId(splitCaseGridDTO.getItemPriceId());
        splitCaseDO.setGfsCustomerTypeCode(31);
        splitCaseDO.setLessCaseRuleId(2);
        String userName = "vc71u";
        List<SplitCaseDO> oldList = new ArrayList<>();
        SplitCaseDO oldSplitCaseDO = new SplitCaseDO();
        oldSplitCaseDO.setContractPriceProfileSeq(1);
        oldSplitCaseDO.setEffectiveDate(effectiveDate);
        oldSplitCaseDO.setExpirationDate(expirationDate);
        oldSplitCaseDO.setGfsCustomerTypeCode(12);
        oldSplitCaseDO.setItemPriceId("1");
        oldSplitCaseDO.setProductType("Veg");
        oldSplitCaseDO.setSplitCaseFee(1);
        oldSplitCaseDO.setLessCaseRuleId(1);
        oldSplitCaseDO.setUnit("$");
        oldList.add(oldSplitCaseDO);
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        Map<Integer, String> offeringMap = new HashMap<>();
        offeringMap.put(1, "Test");

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(splitCaseDTO.getContractPriceProfileSeq())).thenReturn(cmgCustomerResponseDTO);
        when(prcProfLessCaseRuleRepository.fetchSplitCaseGridForCMG(splitCaseDTO.getContractPriceProfileSeq())).thenReturn(oldList);
        when(splitCaseBuilder.buildSplitCaseDO(1, splitCaseGridDTO, 31)).thenReturn(splitCaseDO);
        when(splitCaseBuilder.buildExistingSplitCaseList(splitCaseDTO, oldList)).thenReturn(oldList);

        splitCaseService.saveOrUpdateSplitCase(splitCaseDTO, userName);

        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(splitCaseDTO.getContractPriceProfileSeq());
        verify(prcProfLessCaseRuleRepository).fetchSplitCaseGridForCMG(splitCaseDTO.getContractPriceProfileSeq());
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(splitCaseDTO.getContractPriceProfileSeq());
        verify(splitCaseBuilder).buildSplitCaseDO(1, splitCaseGridDTO, 31);
        verify(splitCaseBuilder).buildExistingSplitCaseList(splitCaseDTO, oldList);
    }

    @Test
    public void saveSplitCaseTestFeeNotNull() throws Exception {
        SplitCaseGridDTO splitCaseDTO = new SplitCaseGridDTO();
        SplitCaseDTO splitCaseGridDTO = new SplitCaseDTO();
        List<SplitCaseDTO> splitCaseGridDTOs = new ArrayList<>();
        splitCaseGridDTO.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        splitCaseGridDTO.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        splitCaseGridDTO.setSplitCaseFee("0");
        splitCaseGridDTO.setLessCaseRuleId(2);
        splitCaseGridDTOs.add(splitCaseGridDTO);
        splitCaseDTO.setSplitCaseFeeValues(splitCaseGridDTOs);
        splitCaseDTO.setContractPriceProfileSeq(1);
        List<SplitCaseDO> splitCaseDOList = new ArrayList<>(splitCaseDTO.getSplitCaseFeeValues().size());
        SplitCaseDO splitCaseDO = new SplitCaseDO();
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        splitCaseDO.setContractPriceProfileSeq(splitCaseDTO.getContractPriceProfileSeq());
        splitCaseDO.setProductType(splitCaseGridDTO.getProductType());
        splitCaseDO.setSplitCaseFee(0);
        splitCaseDO.setUnit(splitCaseGridDTO.getUnit());
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setItemPriceId(splitCaseGridDTO.getItemPriceId());
        splitCaseDO.setGfsCustomerTypeCode(31);
        splitCaseDO.setLessCaseRuleId(2);
        splitCaseDOList.add(splitCaseDO);
        String userName = "vc71u";
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");

        when(splitCaseBuilder.buildSplitCaseDO(1, splitCaseGridDTO, 31)).thenReturn(splitCaseDO);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(splitCaseDTO.getContractPriceProfileSeq())).thenReturn(cmgCustomerResponseDTO);

        splitCaseService.saveOrUpdateSplitCase(splitCaseDTO, userName);

        verify(prcProfLessCaseRuleRepository).saveSplitCase(splitCaseDOList, "1", userName);
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(splitCaseDTO.getContractPriceProfileSeq());
        verify(splitCaseBuilder).buildSplitCaseDO(1, splitCaseGridDTO, 31);
    }

    @Test(expected = Exception.class)
    public void saveSplitCaseTestException() throws Exception {
        int contractPriceProfileSeq = 12;
        SplitCaseGridDTO splitCaseGridDTO = new SplitCaseGridDTO();
        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        List<SplitCaseDTO> splitCaseGridDTOs = new ArrayList<>();
        splitCaseGridDTOs.add(splitCaseDTO);
        splitCaseGridDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        splitCaseGridDTO.setSplitCaseFeeValues(splitCaseGridDTOs);
        String userName = "vc71u";

        doThrow(RuntimeException.class).when(contractPriceProfCustomerRepository).fetchGfsCustomerId(contractPriceProfileSeq, true);
        splitCaseService.saveOrUpdateSplitCase(splitCaseGridDTO, userName);

        verify(contractPriceProfCustomerRepository).fetchGfsCustomerId(eq(contractPriceProfileSeq), true);
    }
}
