package com.gfs.cpp.component.review;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingReviewDTO;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.review.ReviewDTO;
import com.gfs.cpp.common.dto.review.SavePricingExhibitDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.product.aquisition.ProductTypeDTO;
import com.gfs.cpp.component.contractpricing.ContractPricingService;
import com.gfs.cpp.component.distributioncenter.DistributionCenterService;
import com.gfs.cpp.component.document.documentgenerator.DocumentBuildHelper;
import com.gfs.cpp.component.document.documentgenerator.ExhibitDocxGenerator;
import com.gfs.cpp.component.document.documentgenerator.MarkupContractLanguageHelper;
import com.gfs.cpp.component.document.documentgenerator.MarkupDataTableCreator;
import com.gfs.cpp.component.item.ItemService;
import com.gfs.cpp.component.review.ReviewService;
import com.gfs.cpp.component.review.helper.MarkupReviewBuilder;
import com.gfs.cpp.component.splitcase.SplitCaseService;
import com.gfs.cpp.component.splitcase.helper.SplitCaseReviewDTOBuilder;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.contractpricing.CostModelMapRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;
import com.gfs.cpp.data.review.ReviewRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@Configuration
@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest {

    private static final int transferFee = 0;

    private static final int labelAssesmentInd = 0;

    private static final int priceAuditInd = 0;

    private static final int prcProfVerificationInd = 0;

    @InjectMocks
    private ReviewService target;

    @Mock
    private ContractPriceProfileRepository contractPricingRepository;

    @Mock
    private PrcProfLessCaseRuleRepository splitCaseRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private SplitCaseService splitCaseService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private DistributionCenterService distributionCenterService;

    @Mock
    private ContractPricingService contractPricingService;

    @Mock
    private MarkupDataTableCreator markupDataTableCreator;

    @Mock
    private DocumentBuildHelper documentBuildHelper;

    @Mock
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;

    @Mock
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private CostModelMapRepository costModelMapRepository;

    @Mock
    private PrcProfVerificationPrivlgRepository prcProfVerificationPrivlgRepository;

    @Mock
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;

    @Mock
    private MarkupReviewBuilder markupReviewBuilder;

    @Mock
    private ExhibitDocxGenerator exhibitDocxGenerator;

    @Mock
    private ClmApiProxy clmApiProxy;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private MarkupContractLanguageHelper markupContractLanguageHelper;

    @Mock
    Environment environment;

    @Mock
    private ReviewDTO dto;

    @Mock
    private SplitCaseReviewDTOBuilder splitCaseReviewDTOBuilder;

    @Mock
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    @Mock
    private File file;

    @Test
    public void shouldReturnReviewDTOWithAllFalseFlags() {
        Integer contractPriceProfileSeq = 1;
        Map<String, String> contrcatNameMap = new HashMap<>();
        contrcatNameMap.put("1", "Test");
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        List<ProductTypeMarkupDTO> fetchProductTypeMarkups = new ArrayList<>();
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setGfsCustomerId("1");
        productTypeMarkupDTO.setMarkup("5");
        fetchProductTypeMarkups.add(productTypeMarkupDTO);
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setAssessmentFeeFlag(labelAssesmentInd);
        contractPricingResponseDTO.setTransferFeeFlag(transferFee);
        Map<String, Integer> gfsCustomerDetailsMap = new HashMap<>();
        gfsCustomerDetailsMap.put("1", 1);

        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);

        SplitCaseReviewDTO splitCaseReviewDTO = buildSplitCaseReviewDTO();

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(markupReviewBuilder.buildFetchMarkupDTO(contractPriceProfileSeq, null, null)).thenReturn(markupDTO);
        when(reviewRepository.fetchContractLanguageSeq("Test")).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(1, "Test")).thenReturn("");
        when(prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M);
        when(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(priceAuditInd);
        when(prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(1)).thenReturn(prcProfVerificationInd);
        when(costModelMapRepository.fetchCostModelId(transferFee, labelAssesmentInd, priceAuditInd, prcProfVerificationInd)).thenReturn(70);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPricingResponseDTO);
        when(splitCaseReviewDTOBuilder.buildSplitCaseReviewDTO(contractPriceProfileSeq)).thenReturn(splitCaseReviewDTO);

        ReviewDTO reviewDTO = target.fetchReviewData(contractPriceProfileSeq);

        verify(prcProfCostRunSchedGroupRepository).fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(prcProfAuditAuthorityRepository).fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(costModelMapRepository).fetchCostModelId(transferFee, labelAssesmentInd, priceAuditInd, prcProfVerificationInd);
        verify(prcProfVerificationPrivlgRepository).fetchPricePriviligeIndicator(contractPriceProfileSeq);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(reviewRepository).fetchContractLanguage(0, "DEFAULT");
        verify(markupReviewBuilder).buildFetchMarkupDTO(contractPriceProfileSeq, null, null);
        verify(markupContractLanguageHelper).setMarkupTypeLanguage(markupDTO, contractPriceProfileSeq);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);

        assertThat(reviewDTO.getContractPricingReviewDTO().getScheduleForCost(), is(CPPConstants.GFS_FISCAL_CALNEDAR));
        assertThat(reviewDTO.getContractPricingReviewDTO().getFormalPriceAudit(), is(CPPConstants.NO));
        assertThat(reviewDTO.getContractPricingReviewDTO().getPriceVerification(), is(CPPConstants.NO));
        assertThat(reviewDTO.getContractPricingReviewDTO().getGfsTransferFee(), is(CPPConstants.NO));
        assertThat(reviewDTO.getContractPricingReviewDTO().getGfsLabelAssesmentFee(), is(CPPConstants.NO));
        assertThat(reviewDTO.getMarkupReviewDTO().getMarkupBasedOnSell(), is(CPPConstants.NO));
        assertThat(reviewDTO.getMarkupReviewDTO().getMarkupGridDTOs().size(), is(1));
        assertThat(reviewDTO.getSplitCaseReviewDTO().getSplitCaseFeeValues().size(), is(1));
    }

    @Test
    public void shouldReturnReviewDTOWithMarkupFalseFlag() {
        Integer contractPriceProfileSeq = 1;
        List<ProductTypeDTO> productTypeDTOs = new ArrayList<ProductTypeDTO>();
        Map<String, String> contrcatNameMap = new HashMap<>();
        contrcatNameMap.put("1", "Test");
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        List<ProductTypeMarkupDTO> fetchProductTypeMarkups = new ArrayList<>();
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setMarkup("5");
        productTypeMarkupDTO.setGfsCustomerId("1");
        fetchProductTypeMarkups.add(productTypeMarkupDTO);
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setAssessmentFeeFlag(1);
        contractPricingResponseDTO.setTransferFeeFlag(1);
        ProductTypeDTO productTypeDTO = new ProductTypeDTO();
        productTypeDTO.setOfferingCategoryId(1);
        productTypeDTOs.add(productTypeDTO);
        Map<String, Integer> gfsCustomerDetailsMap = new HashMap<>();
        gfsCustomerDetailsMap.put("1", 1);

        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setItemDescription("ITEM_DESC");
        itemInformationDTO.setStockingCode("0");
        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);
        SplitCaseReviewDTO splitCaseReviewDTO = buildSplitCaseReviewDTO();

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(markupReviewBuilder.buildFetchMarkupDTO(contractPriceProfileSeq, null, null)).thenReturn(markupDTO);
        when(reviewRepository.fetchContractLanguageSeq("Test")).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(1, "Test")).thenReturn("");
        when(prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M);
        when(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(0);
        when(prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(1)).thenReturn(0);
        when(costModelMapRepository.fetchCostModelId(1, 1, priceAuditInd, prcProfVerificationInd)).thenReturn(70);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPricingResponseDTO);
        when(itemService.findItemInformation("12")).thenReturn(itemInformationDTO);
        when(splitCaseReviewDTOBuilder.buildSplitCaseReviewDTO(contractPriceProfileSeq)).thenReturn(splitCaseReviewDTO);

        ReviewDTO reviewDTO = target.fetchReviewData(contractPriceProfileSeq);
        verify(prcProfCostRunSchedGroupRepository).fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(prcProfAuditAuthorityRepository).fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(costModelMapRepository).fetchCostModelId(1, 1, priceAuditInd, prcProfVerificationInd);
        verify(prcProfVerificationPrivlgRepository).fetchPricePriviligeIndicator(contractPriceProfileSeq);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(reviewRepository).fetchContractLanguage(0, "DEFAULT");
        verify(markupContractLanguageHelper).setMarkupTypeLanguage(markupDTO, contractPriceProfileSeq);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);
        verify(splitCaseReviewDTOBuilder).buildSplitCaseReviewDTO(contractPriceProfileSeq);

        assertThat(reviewDTO.getContractPricingReviewDTO().getScheduleForCost(), is(CPPConstants.GFS_FISCAL_CALNEDAR));
        assertThat(reviewDTO.getContractPricingReviewDTO().getFormalPriceAudit(), is(CPPConstants.NO));
        assertThat(reviewDTO.getContractPricingReviewDTO().getPriceVerification(), is(CPPConstants.NO));
        assertThat(reviewDTO.getContractPricingReviewDTO().getGfsTransferFee(), is(CPPConstants.YES));
        assertThat(reviewDTO.getContractPricingReviewDTO().getGfsLabelAssesmentFee(), is(CPPConstants.YES));
        assertThat(reviewDTO.getMarkupReviewDTO().getMarkupBasedOnSell(), is(CPPConstants.NO));
        assertThat(reviewDTO.getMarkupReviewDTO().getMarkupGridDTOs().size(), is(1));
        assertThat(reviewDTO.getSplitCaseReviewDTO().getSplitCaseFeeValues().size(), is(1));
    }

    @Test
    public void shouldReturnReviewDTOWithMarkupTrueFlag() {
        Integer contractPriceProfileSeq = 1;
        Map<String, String> contrcatNameMap = new HashMap<>();
        contrcatNameMap.put("1", "Test");
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        List<ProductTypeMarkupDTO> fetchProductTypeMarkups = new ArrayList<>();
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setGfsCustomerId("1");
        productTypeMarkupDTO.setMarkup("5");
        fetchProductTypeMarkups.add(productTypeMarkupDTO);
        List<ProductTypeDTO> productTypeDTOs = new ArrayList<ProductTypeDTO>();
        ProductTypeDTO productTypeDTO = new ProductTypeDTO();
        productTypeDTO.setOfferingCategoryId(1);
        productTypeDTOs.add(productTypeDTO);
        Map<String, Integer> gfsCustomerDetailsMap = new HashMap<>();
        gfsCustomerDetailsMap.put("1", 1);
        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        markupDTO.setPricingOverrideInd(true);
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);
        ReviewDTO review = buildReviewDTO();

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(markupReviewBuilder.buildFetchMarkupDTO(contractPriceProfileSeq, null, null)).thenReturn(markupDTO);
        when(reviewRepository.fetchContractLanguageSeq("Test")).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(1, "Test")).thenReturn("");
        when(prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(1);
        when(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(1);
        when(prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(contractPriceProfileSeq)).thenReturn(1);
        when(splitCaseReviewDTOBuilder.buildSplitCaseReviewDTO(contractPriceProfileSeq)).thenReturn(review.getSplitCaseReviewDTO());

        doThrow(new RuntimeException()).when(itemService).findItemInformation("12");

        ReviewDTO reviewDTO = target.fetchReviewData(contractPriceProfileSeq);

        assertThat(reviewDTO.getContractPricingReviewDTO().getScheduleForCost(), is(CPPConstants.GREGORIAN_CALENDAR));
        assertThat(reviewDTO.getContractPricingReviewDTO().getFormalPriceAudit(), is(CPPConstants.YES));
        assertThat(reviewDTO.getContractPricingReviewDTO().getPriceVerification(), is(CPPConstants.YES));
        assertThat(reviewDTO.getContractPricingReviewDTO().getGfsTransferFee(), is(CPPConstants.NO));
        assertThat(reviewDTO.getContractPricingReviewDTO().getGfsLabelAssesmentFee(), is(CPPConstants.NO));
        assertThat(reviewDTO.getMarkupReviewDTO().getMarkupBasedOnSell(), is(CPPConstants.NO));
        assertThat(reviewDTO.getMarkupReviewDTO().getMarkupGridDTOs().size(), is(1));
        assertThat(reviewDTO.getSplitCaseReviewDTO().getSplitCaseFeeValues().size(), is(1));

        verify(reviewRepository).fetchContractLanguage(0, "W");
        verify(prcProfCostRunSchedGroupRepository).fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(prcProfAuditAuthorityRepository).fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(prcProfVerificationPrivlgRepository).fetchPricePriviligeIndicator(1);
        verify(markupContractLanguageHelper).setMarkupTypeLanguage(markupDTO, contractPriceProfileSeq);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);
        verify(splitCaseReviewDTOBuilder).buildSplitCaseReviewDTO(contractPriceProfileSeq);
    }

    @Test
    public void shouldReturnReviewDTOWithAuditPriceFlags() {
        Integer contractPriceProfileSeq = 1;
        Map<String, String> contrcatNameMap = new HashMap<>();
        contrcatNameMap.put("1", "Test");
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        List<ProductTypeMarkupDTO> fetchProductTypeMarkups = new ArrayList<>();
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setGfsCustomerId("1");
        productTypeMarkupDTO.setMarkup("5");
        fetchProductTypeMarkups.add(productTypeMarkupDTO);
        List<ProductTypeDTO> productTypeDTOs = new ArrayList<ProductTypeDTO>();
        ProductTypeDTO productTypeDTO = new ProductTypeDTO();
        productTypeDTO.setOfferingCategoryId(1);
        productTypeDTOs.add(productTypeDTO);
        Map<String, Integer> gfsCustomerDetailsMap = new HashMap<>();
        gfsCustomerDetailsMap.put("1", 1);
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setItemDescription("ITEM_DESC");
        itemInformationDTO.setStockingCode("0");
        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);
        SplitCaseReviewDTO splitCaseReviewDTO = buildSplitCaseReviewDTO();

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(markupReviewBuilder.buildFetchMarkupDTO(contractPriceProfileSeq, null, null)).thenReturn(markupDTO);
        when(reviewRepository.fetchContractLanguageSeq("Test")).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(1, "Test")).thenReturn("");
        when(prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(1);
        when(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(1);
        when(prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(contractPriceProfileSeq)).thenReturn(1);
        when(itemService.findItemInformation("12")).thenReturn(itemInformationDTO);
        when(splitCaseReviewDTOBuilder.buildSplitCaseReviewDTO(contractPriceProfileSeq)).thenReturn(splitCaseReviewDTO);

        ReviewDTO reviewDTO = target.fetchReviewData(contractPriceProfileSeq);

        assertThat(reviewDTO.getContractPricingReviewDTO().getScheduleForCost(), is(CPPConstants.GREGORIAN_CALENDAR));
        assertThat(reviewDTO.getContractPricingReviewDTO().getFormalPriceAudit(), is(CPPConstants.YES));
        assertThat(reviewDTO.getContractPricingReviewDTO().getPriceVerification(), is(CPPConstants.YES));
        assertThat(reviewDTO.getContractPricingReviewDTO().getGfsTransferFee(), is(CPPConstants.NO));
        assertThat(reviewDTO.getContractPricingReviewDTO().getGfsLabelAssesmentFee(), is(CPPConstants.NO));
        assertThat(reviewDTO.getMarkupReviewDTO().getMarkupBasedOnSell(), is(CPPConstants.NO));
        assertThat(reviewDTO.getMarkupReviewDTO().getMarkupGridDTOs().size(), is(1));
        assertThat(reviewDTO.getSplitCaseReviewDTO().getSplitCaseFeeValues().size(), is(1));

        verify(prcProfCostRunSchedGroupRepository).fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(splitCaseReviewDTOBuilder).buildSplitCaseReviewDTO(contractPriceProfileSeq);
        verify(prcProfAuditAuthorityRepository).fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(prcProfVerificationPrivlgRepository).fetchPricePriviligeIndicator(contractPriceProfileSeq);
        verify(reviewRepository).fetchContractLanguage(0, "W");
        verify(markupContractLanguageHelper).setMarkupTypeLanguage(markupDTO, contractPriceProfileSeq);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);

    }

    @Test
    public void shouldReturnReviewDTOWithFalseFlags() {
        Integer contractPriceProfileSeq = 1;
        Map<String, String> contrcatNameMap = new HashMap<>();
        contrcatNameMap.put("1", "Test");
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        List<ProductTypeMarkupDTO> fetchProductTypeMarkups = new ArrayList<>();
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setMarkup("5");
        productTypeMarkupDTO.setGfsCustomerId("1");
        fetchProductTypeMarkups.add(productTypeMarkupDTO);
        List<ProductTypeDTO> productTypeDTOs = new ArrayList<ProductTypeDTO>();
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setAssessmentFeeFlag(labelAssesmentInd);
        contractPricingResponseDTO.setTransferFeeFlag(transferFee);
        ProductTypeDTO productTypeDTO = new ProductTypeDTO();
        productTypeDTO.setOfferingCategoryId(1);
        productTypeDTOs.add(productTypeDTO);
        Map<String, Integer> gfsCustomerDetailsMap = new HashMap<>();
        gfsCustomerDetailsMap.put("1", 1);
        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);
        SplitCaseReviewDTO splitCaseReviewDTO = buildSplitCaseReviewDTO();

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(markupReviewBuilder.buildFetchMarkupDTO(contractPriceProfileSeq, null, null)).thenReturn(markupDTO);
        when(reviewRepository.fetchContractLanguageSeq("Test")).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(contractPriceProfileSeq, "Test")).thenReturn("");
        when(markupReviewBuilder.buildFetchMarkupDTO(contractPriceProfileSeq, null, null)).thenReturn(markupDTO);
        when(costModelMapRepository.fetchCostModelId(transferFee, labelAssesmentInd, priceAuditInd, prcProfVerificationInd)).thenReturn(10);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPricingResponseDTO);
        when(splitCaseReviewDTOBuilder.buildSplitCaseReviewDTO(contractPriceProfileSeq)).thenReturn(splitCaseReviewDTO);

        ReviewDTO reviewDTO = target.fetchReviewData(contractPriceProfileSeq);

        verify(costModelMapRepository).fetchCostModelId(transferFee, labelAssesmentInd, priceAuditInd, prcProfVerificationInd);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(markupReviewBuilder).buildFetchMarkupDTO(contractPriceProfileSeq, null, null);
        verify(reviewRepository, atLeastOnce()).fetchContractLanguage(0, "W");
        verify(markupContractLanguageHelper).setMarkupTypeLanguage(markupDTO, contractPriceProfileSeq);
        verify(contractPriceProfCustomerRepository).fetchDefaultCmg(contractPriceProfileSeq);
        verify(splitCaseReviewDTOBuilder).buildSplitCaseReviewDTO(contractPriceProfileSeq);

        assertThat(reviewDTO.getContractPricingReviewDTO().getScheduleForCost(), is(CPPConstants.GREGORIAN_CALENDAR));
        assertThat(reviewDTO.getContractPricingReviewDTO().getFormalPriceAudit(), is(CPPConstants.NO));
        assertThat(reviewDTO.getContractPricingReviewDTO().getPriceVerification(), is(CPPConstants.NO));
        assertThat(reviewDTO.getContractPricingReviewDTO().getGfsTransferFee(), is(CPPConstants.NO));
        assertThat(reviewDTO.getContractPricingReviewDTO().getGfsLabelAssesmentFee(), is(CPPConstants.NO));
        assertThat(reviewDTO.getMarkupReviewDTO().getMarkupBasedOnSell(), is(CPPConstants.NO));
        assertThat(reviewDTO.getMarkupReviewDTO().getMarkupGridDTOs().size(), is(1));
    }

    @Test
    public void shouldBuildCostProducts() {
        int contractPriceProfileSeq = 1;
        int priceAuditInd = 1;
        int pricePriceVerifInd = 1;
        ContractPricingReviewDTO contractPricingReviewDTO = new ContractPricingReviewDTO();
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setAssessmentFeeFlag(labelAssesmentInd);
        contractPricingResponseDTO.setTransferFeeFlag(transferFee);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPricingResponseDTO);
        when(costModelMapRepository.fetchCostModelId(transferFee, labelAssesmentInd, priceAuditInd, pricePriceVerifInd)).thenReturn(70);

        target.buildCostProducts(contractPriceProfileSeq, contractPricingReviewDTO, priceAuditInd, pricePriceVerifInd);

        verify(costModelMapRepository).fetchCostModelId(transferFee, labelAssesmentInd, priceAuditInd, pricePriceVerifInd);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
    }

    @Test
    public void shouldBuildCostProductsWhenNoDefault() {
        int contractPriceProfileSeq = 1;
        int priceAuditInd = 1;
        int pricePriceVerifInd = 1;
        ContractPricingReviewDTO contractPricingReviewDTO = new ContractPricingReviewDTO();
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setAssessmentFeeFlag(labelAssesmentInd);
        contractPricingResponseDTO.setTransferFeeFlag(transferFee);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPricingResponseDTO);
        when(costModelMapRepository.fetchCostModelId(transferFee, labelAssesmentInd, priceAuditInd, pricePriceVerifInd)).thenReturn(null);

        target.buildCostProducts(contractPriceProfileSeq, contractPricingReviewDTO, priceAuditInd, pricePriceVerifInd);

        verify(costModelMapRepository).fetchCostModelId(transferFee, labelAssesmentInd, priceAuditInd, pricePriceVerifInd);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
    }

    @Test
    public void shoulBuildPriceVerification() {
        int contractPriceProfileSeq = 1;
        ContractPricingReviewDTO contractPricingReviewDTO = new ContractPricingReviewDTO();
        when(prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(contractPriceProfileSeq)).thenReturn(0);

        target.buildPriceVerification(contractPriceProfileSeq, contractPricingReviewDTO);

        verify(prcProfVerificationPrivlgRepository).fetchPricePriviligeIndicator(contractPriceProfileSeq);
    }

    @Test
    public void shouldBuildPriceAudit() {
        int contractPriceProfileSeq = 1;
        ContractPricingReviewDTO contractPricingReviewDTO = new ContractPricingReviewDTO();
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);
        when(reviewRepository.fetchContractLanguageSeq(CPPConstants.FORMAL_PRICE_AUDIT_PRVG)).thenReturn(1);
        when(prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(1);

        target.buildPriceAudit(contractPriceProfileSeq, contractPricingReviewDTO, cMGCustomerResponseDTO);

        verify(prcProfAuditAuthorityRepository).fetchPriceAuditIndicator(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
        verify(reviewRepository).fetchContractLanguageSeq(CPPConstants.FORMAL_PRICE_AUDIT_PRVG);
    }

    @Test
    public void shouldBuildScheduleForCost() {
        int contractPriceProfileSeq = 1;
        ContractPricingReviewDTO contractPricingReviewDTO = new ContractPricingReviewDTO();
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);

        when(prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode())).thenReturn(1);

        target.buildScheduleForCost(contractPriceProfileSeq, contractPricingReviewDTO, cMGCustomerResponseDTO);

        verify(prcProfCostRunSchedGroupRepository).fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cMGCustomerResponseDTO.getId(),
                cMGCustomerResponseDTO.getTypeCode());
    }

    @Test
    public void shouldSetContractFeeFlags() {
        ContractPricingReviewDTO contractPricingReviewDTO = new ContractPricingReviewDTO();
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setAssessmentFeeFlag(labelAssesmentInd);
        contractPricingResponseDTO.setTransferFeeFlag(transferFee);
        int cppAttributeSeq = 1;

        when(reviewRepository.fetchContractLanguageSeq(CPPConstants.COST_MODEL)).thenReturn(1);
        when(reviewRepository.fetchContractLanguage(cppAttributeSeq, "Test")).thenReturn("");

        target.setCostModelFeeFlags(contractPricingReviewDTO, contractPricingResponseDTO, "70");

        verify(reviewRepository).fetchContractLanguage(cppAttributeSeq, "70");
    }

    @Test
    public void shouldCreateExhibitDocument() throws InvalidFormatException, IOException {

        int contractPriceProfileSeq = 1;
        ReviewDTO review = new ReviewDTO();
        when(exhibitDocxGenerator.generateExhibitDocument(review, contractPriceProfileSeq)).thenReturn(file);

        File resultFile = target.createExhibitDocument(review, contractPriceProfileSeq);

        assertThat(resultFile.getName(), is(file.getName()));
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(contractPriceProfileSeq);

    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldThrowExceptionWhenCreateExhibitDocument() throws InvalidFormatException, IOException {
        int contractPriceProfileSeq = 1;
        ReviewDTO review = new ReviewDTO();
        Mockito.doThrow(new InvalidFormatException(null)).when(exhibitDocxGenerator).generateExhibitDocument(review, contractPriceProfileSeq);

        target.createExhibitDocument(review, contractPriceProfileSeq);

        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(contractPriceProfileSeq);
    }

    @Test
    public void shouldSavePricingExhibit() throws IOException, InvalidFormatException {

        int contractPriceProfileSeq = 1;
        String contractAgeementId = "Test";
        String contractTypeName = "DAN";
        String exhibitSysId = "123";
        String userName = "vc71u";

        SavePricingExhibitDTO savePricingExhibitDTO = new SavePricingExhibitDTO();
        savePricingExhibitDTO.setContractAgeementId(contractAgeementId);
        savePricingExhibitDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        savePricingExhibitDTO.setContractTypeName(contractTypeName);
        ContractPricingResponseDTO contractPriceProfile = new ContractPricingResponseDTO();
        contractPriceProfile.setPricingExhibitSysId(exhibitSysId);

        ReviewDTO review = buildReviewDTO();

        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(markupReviewBuilder.buildFetchMarkupDTO(contractPriceProfileSeq, null, null)).thenReturn(markupDTO);
        when(exhibitDocxGenerator.generateExhibitDocument(review, contractPriceProfileSeq)).thenReturn(file);
        when(splitCaseReviewDTOBuilder.buildSplitCaseReviewDTO(contractPriceProfileSeq)).thenReturn(review.getSplitCaseReviewDTO());
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPriceProfile);
        when(clmApiProxy.savePricingExhibit(file, contractAgeementId, exhibitSysId, contractTypeName)).thenReturn(exhibitSysId);

        target.savePricingExhibit(savePricingExhibitDTO, userName);

        verify(markupReviewBuilder).buildFetchMarkupDTO(contractPriceProfileSeq, null, null);
        verify(exhibitDocxGenerator).generateExhibitDocument(review, contractPriceProfileSeq);
        verify(contractPriceProfileRepository, atLeastOnce()).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(clmApiProxy).savePricingExhibit(file, contractAgeementId, exhibitSysId, contractTypeName);
        verify(markupContractLanguageHelper).setMarkupTypeLanguage(markupDTO, contractPriceProfileSeq);
        verify(splitCaseReviewDTOBuilder).buildSplitCaseReviewDTO(contractPriceProfileSeq);
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(contractPriceProfileSeq);

    }

    @Test
    public void shouldSavePricingExhibitThrowCppRunTimeException() throws IOException {

        int contractPriceProfileSeq = 1;
        String contractAgeementId = "Test";
        String contractTypeName = "DAN";
        String userName = "vc71u";

        SavePricingExhibitDTO savePricingExhibitDTO = new SavePricingExhibitDTO();
        savePricingExhibitDTO.setContractAgeementId(contractAgeementId);
        savePricingExhibitDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        savePricingExhibitDTO.setContractTypeName(contractTypeName);
        SplitCaseReviewDTO splitCaseReviewDTO = buildSplitCaseReviewDTO();

        MarkupReviewDTO markupDTO = shouldBuildMarkupDTO();
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(1);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cMGCustomerResponseDTO);
        when(markupReviewBuilder.buildFetchMarkupDTO(contractPriceProfileSeq, null, null)).thenReturn(markupDTO);
        when(splitCaseReviewDTOBuilder.buildSplitCaseReviewDTO(contractPriceProfileSeq)).thenReturn(splitCaseReviewDTO);

        try {
            target.savePricingExhibit(savePricingExhibitDTO, userName);
            fail("expected exception");
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), is(CPPExceptionType.SAVE_EXHIBIT_SERVICE_FAILED));
        }

        verify(markupReviewBuilder).buildFetchMarkupDTO(contractPriceProfileSeq, null, null);
        verify(splitCaseReviewDTOBuilder).buildSplitCaseReviewDTO(contractPriceProfileSeq);
        verify(markupContractLanguageHelper).setMarkupTypeLanguage(markupDTO, contractPriceProfileSeq);
        verify(contractPriceProfileStatusValidator).validateIfContractPricingEditableStatus(contractPriceProfileSeq);
    }

    private MarkupReviewDTO shouldBuildMarkupDTO() {
        MarkupGridDTO markupGridDTO = new MarkupGridDTO();
        ProductTypeMarkupDTO productTypeMarkupDTOOne = new ProductTypeMarkupDTO();
        productTypeMarkupDTOOne.setItemPriceId(1);
        productTypeMarkupDTOOne.setGfsCustomerId("1");
        productTypeMarkupDTOOne.setMarkup("5");
        productTypeMarkupDTOOne.setMarkupType(1);
        productTypeMarkupDTOOne.setUnit("%");
        ProductTypeMarkupDTO productTypeMarkupDTOTwo = new ProductTypeMarkupDTO();
        productTypeMarkupDTOTwo.setItemPriceId(1);
        productTypeMarkupDTOTwo.setGfsCustomerId("1");
        productTypeMarkupDTOTwo.setMarkup("5");
        productTypeMarkupDTOTwo.setMarkupType(2);
        productTypeMarkupDTOTwo.setUnit("%");
        ProductTypeMarkupDTO productTypeMarkupDTOThree = new ProductTypeMarkupDTO();
        productTypeMarkupDTOThree.setItemPriceId(1);
        productTypeMarkupDTOThree.setGfsCustomerId("1");
        productTypeMarkupDTOThree.setMarkup("5");
        productTypeMarkupDTOThree.setMarkupType(3);
        productTypeMarkupDTOThree.setUnit("%");
        List<ProductTypeMarkupDTO> productTypeMarkupList = new ArrayList<>();
        List<ItemLevelMarkupDTO> item = new ArrayList<>();
        ItemLevelMarkupDTO itemLevelMarkupDTOOne = new ItemLevelMarkupDTO();
        itemLevelMarkupDTOOne.setMarkupType(1);
        ItemLevelMarkupDTO itemLevelMarkupDTOTwo = new ItemLevelMarkupDTO();
        itemLevelMarkupDTOTwo.setMarkupType(2);
        ItemLevelMarkupDTO itemLevelMarkupDTOThree = new ItemLevelMarkupDTO();
        itemLevelMarkupDTOThree.setMarkupType(3);
        item.add(itemLevelMarkupDTOOne);
        item.add(itemLevelMarkupDTOTwo);
        item.add(itemLevelMarkupDTOThree);
        productTypeMarkupList.add(productTypeMarkupDTOOne);
        productTypeMarkupList.add(productTypeMarkupDTOTwo);
        productTypeMarkupList.add(productTypeMarkupDTOThree);
        markupGridDTO.setProductTypeMarkups(productTypeMarkupList);
        markupGridDTO.setItemMarkups(item);
        List<MarkupGridDTO> markupGridDTOs = new ArrayList<>();
        markupGridDTOs.add(markupGridDTO);
        MarkupReviewDTO markupDTO = new MarkupReviewDTO();
        markupDTO.setPricingOverrideInd(false);
        markupDTO.setMarkupBasedOnSell(CPPConstants.NO);
        markupDTO.setMarkupGridDTOs(markupGridDTOs);
        return markupDTO;
    }

    private ReviewDTO buildReviewDTO() {
        ReviewDTO review = new ReviewDTO();
        ContractPricingReviewDTO contractPricingReviewDTO = new ContractPricingReviewDTO();
        contractPricingReviewDTO.setScheduleForCost(CPPConstants.GREGORIAN_CALENDAR);
        contractPricingReviewDTO.setFormalPriceAudit(CPPConstants.NO);
        contractPricingReviewDTO.setPriceVerification(CPPConstants.NO);
        contractPricingReviewDTO.setGfsLabelAssesmentFee(CPPConstants.NO);
        contractPricingReviewDTO.setGfsTransferFee(CPPConstants.NO);
        List<String> distributionCenter = new ArrayList<>();
        SplitCaseReviewDTO splitCaseReviewDTO = buildSplitCaseReviewDTO();
        MarkupReviewDTO markupReviewDTO = shouldBuildMarkupDTO();
        review.setContractPricingReviewDTO(contractPricingReviewDTO);
        review.setDistributionCenter(distributionCenter);
        review.setMarkupReviewDTO(markupReviewDTO);
        review.setSplitCaseReviewDTO(splitCaseReviewDTO);
        return review;
    }

    private SplitCaseReviewDTO buildSplitCaseReviewDTO() {
        SplitCaseReviewDTO splitCaseReviewDTO = new SplitCaseReviewDTO();
        splitCaseReviewDTO.setFeeType(2);
        splitCaseReviewDTO.setFeeTypeContractLanguage("Contract language test");
        SplitCaseDTO splitCaseGridDTO = new SplitCaseDTO();
        splitCaseGridDTO.setItemPriceId("1212");
        splitCaseGridDTO.setLessCaseRuleId(2);
        splitCaseGridDTO.setEffectiveDate(new LocalDate(9999, 01, 01).toDate());
        List<SplitCaseDTO> splitCaseFee = new ArrayList<>();
        splitCaseFee.add(splitCaseGridDTO);
        splitCaseReviewDTO.setSplitCaseFeeValues(splitCaseFee);
        return splitCaseReviewDTO;
    }
}