package com.gfs.cpp.component.furtherance;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
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

import com.gfs.cpp.common.dto.furtherance.FurtheranceAction;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.activatepricing.CustomerItemPriceProcessor;
import com.gfs.cpp.component.activatepricing.CustomerItemPricingCopier;
import com.gfs.cpp.component.furtherance.FurtherancePricingProcessor;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@RunWith(MockitoJUnitRunner.class)
public class FurtherancePricingProcessorTest {

    private static final String userName = "test user";
    private static final Date expirationDate = new LocalDate(2010, 01, 01).toDate();

    @InjectMocks
    private FurtherancePricingProcessor target;

    @Mock
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private CustomerItemPriceProcessor customerItemPriceProcessor;

    @Mock
    private CustomerItemPricingCopier customerItemPricingCopier;

    @Test
    public void shouldExpireAndSaveFurtheranceUpdates() {
        int cppFurtheranceSeq = 2000;
        Date furtheranceEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date farOffDate = new LocalDate(9999, 01, 01).toDate();

        Map<Integer, List<ProductTypeMarkupDTO>> markupsForRealCustomer = new HashMap<>();
        markupsForRealCustomer.put(FurtheranceAction.UPDATED.getCode(), buildProductTypeMarkupDTO());
        markupsForRealCustomer.put(FurtheranceAction.ADDED.getCode(), null);
        markupsForRealCustomer.put(FurtheranceAction.DELETED.getCode(), null);

        when(cppDateUtils.getFutureDate()).thenReturn(farOffDate);
        when(prcProfLessCaseRuleRepository.fetchPrcProfLessCaseForRealCustomersForFurtherance(cppFurtheranceSeq, furtheranceEffectiveDate))
                .thenReturn(buildPrcProfLessCaseRuleDOList());
        when(customerItemPriceRepository.fetchMarkupsForRealCustomersForFurtherance(cppFurtheranceSeq)).thenReturn(markupsForRealCustomer);
        when(customerItemPricingCopier.extractItemAndCustomerMappingWithNoExistingBidEntries(
                buildFurtheranceInformationDTO().getContractPriceProfileSeq(), userName,
                buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate,
                markupsForRealCustomer.get(FurtheranceAction.DELETED.getCode()))).thenReturn(buildProductTypeMarkupDOList());

        target.expireAndSaveFurtheranceUpdates(expirationDate, buildFurtheranceInformationDTO(), userName);

        verify(cppDateUtils).getFutureDate();
        verify(prcProfLessCaseRuleRepository, times(1)).fetchPrcProfLessCaseForRealCustomersForFurtherance(cppFurtheranceSeq, furtheranceEffectiveDate);
        verify(prcProfLessCaseRuleRepository, times(1)).expirePrcProfLessCaseRuleForFurtherance(buildPrcProfLessCaseRuleDOList(), expirationDate, userName);
        verify(prcProfLessCaseRuleRepository, times(1)).savePrcProfLessCaseRuleForFurtheranceUpdates(buildPrcProfLessCaseRuleDOList(), userName);
        verify(customerItemPriceRepository, times(1)).fetchMarkupsForRealCustomersForFurtherance(cppFurtheranceSeq);
        verify(customerItemPriceRepository, times(0)).expireItemPricingForDeletedItemsDuringFurtherance(expirationDate, userName,
                buildProductTypeMarkupDOList());
        verify(customerItemPriceProcessor, times(1)).expireAndSaveForMarkupToBeAdded(buildFurtheranceInformationDTO().getContractPriceProfileSeq(),
                userName, expirationDate, buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate, buildProductTypeMarkupDTO());
        verify(customerItemPricingCopier, times(0)).extractItemAndCustomerMappingWithNoExistingBidEntries(
                buildFurtheranceInformationDTO().getContractPriceProfileSeq(), userName,
                buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate,
                markupsForRealCustomer.get(FurtheranceAction.DELETED.getCode()));
    }

    @Test
    public void shouldExpireAndSaveFurtheranceUpdatesForAddedItems() {
        int cppFurtheranceSeq = 2000;
        Date furtheranceEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date farOffDate = new LocalDate(9999, 01, 01).toDate();

        Map<Integer, List<ProductTypeMarkupDTO>> markupsForRealCustomer = new HashMap<>();
        markupsForRealCustomer.put(FurtheranceAction.UPDATED.getCode(), null);
        markupsForRealCustomer.put(FurtheranceAction.ADDED.getCode(), buildProductTypeMarkupDTO());
        markupsForRealCustomer.put(FurtheranceAction.DELETED.getCode(), null);

        when(cppDateUtils.getFutureDate()).thenReturn(farOffDate);
        when(prcProfLessCaseRuleRepository.fetchPrcProfLessCaseForRealCustomersForFurtherance(cppFurtheranceSeq, furtheranceEffectiveDate))
                .thenReturn(buildPrcProfLessCaseRuleDOList());
        when(customerItemPriceRepository.fetchMarkupsForRealCustomersForFurtherance(cppFurtheranceSeq)).thenReturn(markupsForRealCustomer);
        when(customerItemPricingCopier.extractItemAndCustomerMappingWithNoExistingBidEntries(
                buildFurtheranceInformationDTO().getContractPriceProfileSeq(), userName,
                buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate,
                markupsForRealCustomer.get(FurtheranceAction.DELETED.getCode()))).thenReturn(buildProductTypeMarkupDOList());

        target.expireAndSaveFurtheranceUpdates(expirationDate, buildFurtheranceInformationDTO(), userName);

        verify(cppDateUtils).getFutureDate();
        verify(prcProfLessCaseRuleRepository, times(1)).fetchPrcProfLessCaseForRealCustomersForFurtherance(cppFurtheranceSeq, furtheranceEffectiveDate);
        verify(prcProfLessCaseRuleRepository, times(1)).expirePrcProfLessCaseRuleForFurtherance(buildPrcProfLessCaseRuleDOList(), expirationDate, userName);
        verify(prcProfLessCaseRuleRepository, times(1)).savePrcProfLessCaseRuleForFurtheranceUpdates(buildPrcProfLessCaseRuleDOList(), userName);
        verify(customerItemPriceRepository, times(1)).fetchMarkupsForRealCustomersForFurtherance(cppFurtheranceSeq);
        verify(customerItemPriceRepository, times(0)).expireItemPricingForDeletedItemsDuringFurtherance(expirationDate, userName,
                buildProductTypeMarkupDOList());
        verify(customerItemPriceProcessor, times(1)).expireAndSaveForMarkupToBeAdded(buildFurtheranceInformationDTO().getContractPriceProfileSeq(),
                userName, expirationDate, buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate, buildProductTypeMarkupDTO());
        verify(customerItemPricingCopier, times(0)).extractItemAndCustomerMappingWithNoExistingBidEntries(
                buildFurtheranceInformationDTO().getContractPriceProfileSeq(), userName,
                buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate,
                markupsForRealCustomer.get(FurtheranceAction.DELETED.getCode()));
    }

    @Test
    public void shouldExpireAndSaveFurtheranceUpdatesForDeletedItems() {
        int cppFurtheranceSeq = 2000;
        Date furtheranceEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date farOffDate = new LocalDate(9999, 01, 01).toDate();

        Map<Integer, List<ProductTypeMarkupDTO>> markupsForRealCustomer = new HashMap<>();
        markupsForRealCustomer.put(FurtheranceAction.UPDATED.getCode(), null);
        markupsForRealCustomer.put(FurtheranceAction.ADDED.getCode(), null);
        markupsForRealCustomer.put(FurtheranceAction.DELETED.getCode(), buildProductTypeMarkupDTO());

        when(cppDateUtils.getFutureDate()).thenReturn(farOffDate);
        when(prcProfLessCaseRuleRepository.fetchPrcProfLessCaseForRealCustomersForFurtherance(cppFurtheranceSeq, furtheranceEffectiveDate))
                .thenReturn(buildPrcProfLessCaseRuleDOList());
        when(customerItemPriceRepository.fetchMarkupsForRealCustomersForFurtherance(cppFurtheranceSeq)).thenReturn(markupsForRealCustomer);
        when(customerItemPricingCopier.extractItemAndCustomerMappingWithNoExistingBidEntries(
                buildFurtheranceInformationDTO().getContractPriceProfileSeq(), userName,
                buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate,
                markupsForRealCustomer.get(FurtheranceAction.DELETED.getCode()))).thenReturn(buildProductTypeMarkupDOList());

        target.expireAndSaveFurtheranceUpdates(expirationDate, buildFurtheranceInformationDTO(), userName);

        verify(cppDateUtils).getFutureDate();
        verify(prcProfLessCaseRuleRepository, times(1)).fetchPrcProfLessCaseForRealCustomersForFurtherance(cppFurtheranceSeq, furtheranceEffectiveDate);
        verify(prcProfLessCaseRuleRepository, times(1)).expirePrcProfLessCaseRuleForFurtherance(buildPrcProfLessCaseRuleDOList(), expirationDate, userName);
        verify(prcProfLessCaseRuleRepository, times(1)).savePrcProfLessCaseRuleForFurtheranceUpdates(buildPrcProfLessCaseRuleDOList(), userName);
        verify(customerItemPriceRepository, times(1)).fetchMarkupsForRealCustomersForFurtherance(cppFurtheranceSeq);
        verify(customerItemPriceRepository, times(1)).expireItemPricingForDeletedItemsDuringFurtherance(expirationDate, userName,
                buildProductTypeMarkupDOList());
        verify(customerItemPriceProcessor, times(0)).expireAndSaveForMarkupToBeAdded(buildFurtheranceInformationDTO().getContractPriceProfileSeq(),
                userName, expirationDate, buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate, buildProductTypeMarkupDTO());
        verify(customerItemPricingCopier, times(1)).extractItemAndCustomerMappingWithNoExistingBidEntries(
                buildFurtheranceInformationDTO().getContractPriceProfileSeq(), userName,
                buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate,
                markupsForRealCustomer.get(FurtheranceAction.DELETED.getCode()));
    }

    @Test
    public void shouldNotExpireSplitCase() {
        int cppFurtheranceSeq = 2000;
        Date furtheranceEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date farOffDate = new LocalDate(9999, 01, 01).toDate();

        when(cppDateUtils.getFutureDate()).thenReturn(farOffDate);
        when(prcProfLessCaseRuleRepository.fetchPrcProfLessCaseForRealCustomersForFurtherance(cppFurtheranceSeq, furtheranceEffectiveDate)).thenReturn(null);

        target.expireAndSaveFurtheranceUpdates(expirationDate, buildFurtheranceInformationDTO(), userName);

        verify(cppDateUtils).getFutureDate();
        verify(prcProfLessCaseRuleRepository, times(1)).fetchPrcProfLessCaseForRealCustomersForFurtherance(cppFurtheranceSeq, furtheranceEffectiveDate);
        verify(prcProfLessCaseRuleRepository, times(0)).expirePrcProfLessCaseRuleForFurtherance(null, expirationDate, userName);
        verify(prcProfLessCaseRuleRepository, times(0)).savePrcProfLessCaseRuleForFurtheranceUpdates(null, userName);
    }

    @Test
    public void shouldNotExpireCustomerItemPrice() {
        int cppFurtheranceSeq = 2000;
        Date farOffDate = new LocalDate(9999, 01, 01).toDate();

        Map<Integer, List<ProductTypeMarkupDTO>> markupsForRealCustomer = new HashMap<>();
        markupsForRealCustomer.put(FurtheranceAction.UPDATED.getCode(), null);
        markupsForRealCustomer.put(FurtheranceAction.ADDED.getCode(), null);
        markupsForRealCustomer.put(FurtheranceAction.DELETED.getCode(), null);

        when(cppDateUtils.getFutureDate()).thenReturn(farOffDate);
        when(customerItemPriceRepository.fetchMarkupsForRealCustomersForFurtherance(cppFurtheranceSeq)).thenReturn(markupsForRealCustomer);
        when(customerItemPricingCopier.extractItemAndCustomerMappingWithNoExistingBidEntries(
                buildFurtheranceInformationDTO().getContractPriceProfileSeq(), userName,
                buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate,
                markupsForRealCustomer.get(FurtheranceAction.DELETED.getCode()))).thenReturn(null);

        target.expireAndSaveFurtheranceUpdates(expirationDate, buildFurtheranceInformationDTO(), userName);

        verify(cppDateUtils).getFutureDate();
        verify(customerItemPriceRepository, times(1)).fetchMarkupsForRealCustomersForFurtherance(cppFurtheranceSeq);
        verify(customerItemPriceRepository, times(0)).expireItemPricingForDeletedItemsDuringFurtherance(expirationDate, userName, null);
        verify(customerItemPriceProcessor, times(0)).expireAndSaveForMarkupToBeAdded(buildFurtheranceInformationDTO().getContractPriceProfileSeq(),
                userName, expirationDate, buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate,
                new ArrayList<ProductTypeMarkupDTO>());
        verify(customerItemPricingCopier, times(0)).extractItemAndCustomerMappingWithNoExistingBidEntries(
                buildFurtheranceInformationDTO().getContractPriceProfileSeq(), userName,
                buildFurtheranceInformationDTO().getFurtheranceEffectiveDate(), farOffDate, null);
    }

    @Test
    public void shouldExpirePrcProfLessCaseRuleForFurtherance() {

        target.expirePrcProfLessCaseRuleForFurtherance(buildPrcProfLessCaseRuleDOList(), expirationDate, userName);
        verify(prcProfLessCaseRuleRepository, times(1)).expirePrcProfLessCaseRuleForFurtherance(buildPrcProfLessCaseRuleDOList(), expirationDate, userName);
    }

    @Test
    public void shouldSavePrcProfLessCaseRule() {

        target.savePrcProfLessCaseRule(buildPrcProfLessCaseRuleDOList(), userName);
        verify(prcProfLessCaseRuleRepository, times(1)).savePrcProfLessCaseRuleForFurtheranceUpdates(buildPrcProfLessCaseRuleDOList(), userName);
    }

    private List<ProductTypeMarkupDO> buildProductTypeMarkupDOList() {
        int contractPriceProfileSeq = -3001;
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setGfsCustomerId("-10001234");
        productTypeMarkupDO.setItemPriceId(4);
        productTypeMarkupDO.setProductType("2");
        productTypeMarkupDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDO.setCustomerTypeCode(24);
        return Collections.singletonList(productTypeMarkupDO);
    }

    private List<ProductTypeMarkupDTO> buildProductTypeMarkupDTO() {
        int contractPriceProfileSeq = -3001;
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId("-10001234");
        productTypeMarkupDTO.setItemPriceId(4);
        productTypeMarkupDTO.setProductType("2");
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setGfsCustomerTypeCode(24);
        return Collections.singletonList(productTypeMarkupDTO);
    }

    private List<PrcProfLessCaseRuleDO> buildPrcProfLessCaseRuleDOList() {
        List<PrcProfLessCaseRuleDO> splitCaseList = new ArrayList<>();
        PrcProfLessCaseRuleDO splitCaseDO = new PrcProfLessCaseRuleDO();
        Date effectiveDate = new LocalDate(2010, 01, 01).toDate();
        Date expirationDate = new LocalDate(2020, 01, 01).toDate();
        splitCaseDO.setContractPriceProfileSeq(-3001);
        splitCaseDO.setCreateUserId("check user");
        splitCaseDO.setLastUpdateUserId("check user");
        splitCaseDO.setCwMarkupAmnt(21);
        splitCaseDO.setCwMarkupAmountTypeCode("$");
        splitCaseDO.setEffectiveDate(effectiveDate);
        splitCaseDO.setExpirationDate(expirationDate);
        splitCaseDO.setGfsCustomerId("-10001234");
        splitCaseDO.setGfsCustomerTypeCode(24);
        splitCaseDO.setItemPriceId("4");
        splitCaseDO.setItemPriceLevelCode(2);
        splitCaseDO.setLesscaseRuleId(2);
        splitCaseDO.setMarkupAppliedBeforeDivInd(0);
        splitCaseDO.setNonCwMarkupAmnt(20);
        splitCaseDO.setNonCwMarkupAmntTypeCode("%");
        splitCaseList.add(splitCaseDO);
        return splitCaseList;
    }

    private FurtheranceInformationDTO buildFurtheranceInformationDTO() {
        int contractPriceProfileSeq = -3001;
        int cppFurtheranceSeq = 2000;
        String parentAgreementId = "parent-clm-agreement-id";
        FurtheranceInformationDTO savedFurtheranceInformationDTO = new FurtheranceInformationDTO();
        savedFurtheranceInformationDTO.setChangeReasonTxt("reason text");
        savedFurtheranceInformationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        savedFurtheranceInformationDTO.setContractReferenceTxt("reference text");
        savedFurtheranceInformationDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        savedFurtheranceInformationDTO.setFurtheranceEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        savedFurtheranceInformationDTO.setFurtheranceStatusCode(1);
        savedFurtheranceInformationDTO.setParentCLMAgreementId(parentAgreementId);
        return savedFurtheranceInformationDTO;
    }
}
