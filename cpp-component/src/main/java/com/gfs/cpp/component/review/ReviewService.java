package com.gfs.cpp.component.review;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.corp.component.price.common.types.CostChangeFrequency;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingReviewDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.review.ReviewDTO;
import com.gfs.cpp.common.dto.review.SavePricingExhibitDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.distributioncenter.DistributionCenterService;
import com.gfs.cpp.component.document.documentgenerator.ExhibitDocxGenerator;
import com.gfs.cpp.component.document.documentgenerator.MarkupContractLanguageHelper;
import com.gfs.cpp.component.review.helper.MarkupReviewBuilder;
import com.gfs.cpp.component.splitcase.helper.SplitCaseReviewDTOBuilder;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.contractpricing.CostModelMapRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;
import com.gfs.cpp.data.review.ReviewRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@Service("reviewService")
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private DistributionCenterService distributionCenterService;
    @Autowired
    private MarkupReviewBuilder markupReviewBuilder;
    @Autowired
    private ExhibitDocxGenerator exhibitDocxGenerator;
    @Autowired
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;
    @Autowired
    private PrcProfVerificationPrivlgRepository prcProfVerificationPrivlgRepository;
    @Autowired
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;
    @Autowired
    private ClmApiProxy clmApiProxy;
    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;
    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;
    @Autowired
    private MarkupContractLanguageHelper markupContractLanguageHelper;
    @Autowired
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;
    @Autowired
    private SplitCaseReviewDTOBuilder splitCaseReviewDTOBuilder;
    @Autowired
    private CostModelMapRepository costModelMapRepository;

    public static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    public ReviewDTO fetchReviewData(int contractPriceProfileSeq) {
        return buildReviewDTO(contractPriceProfileSeq);
    }

    ContractPricingReviewDTO fetchContractPricingReviewData(int contractPriceProfileSeq) {
        ContractPricingReviewDTO contractPricingReviewDTO = new ContractPricingReviewDTO();
        CMGCustomerResponseDTO cmgCustomerResponseDTO = contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq);
        buildScheduleForCost(contractPriceProfileSeq, contractPricingReviewDTO, cmgCustomerResponseDTO);
        int priceAuditInd = buildPriceAudit(contractPriceProfileSeq, contractPricingReviewDTO, cmgCustomerResponseDTO);
        int prcProfVerificationInd = buildPriceVerification(contractPriceProfileSeq, contractPricingReviewDTO);
        buildCostProducts(contractPriceProfileSeq, contractPricingReviewDTO, priceAuditInd, prcProfVerificationInd);
        return contractPricingReviewDTO;

    }

    List<String> fetchDistributionCenterReviewData(int contractPriceProfileSeq) {
        return distributionCenterService.fetchDistributionCentersbyContractPriceProfileSeq(contractPriceProfileSeq);
    }

    MarkupReviewDTO fetchMarkUpReviewData(int contractPriceProfileSeq, Date pricingExpirationDate, Date pricingEffectiveDate) {

        MarkupReviewDTO markupDTO = markupReviewBuilder.buildFetchMarkupDTO(contractPriceProfileSeq, pricingEffectiveDate, pricingExpirationDate);
        markupContractLanguageHelper.setMarkupTypeLanguage(markupDTO, contractPriceProfileSeq);
        return markupDTO;
    }

    SplitCaseReviewDTO fetchSplitCaseReviewData(int contractPriceProfileSeq) {
        return splitCaseReviewDTOBuilder.buildSplitCaseReviewDTO(contractPriceProfileSeq);
    }

    public File createExhibitDocument(ReviewDTO review, int contractPriceProfileSeq) {
        File file = null;
        contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(contractPriceProfileSeq);
        try {
            file = exhibitDocxGenerator.generateExhibitDocument(review, contractPriceProfileSeq);
        } catch (InvalidFormatException | IOException e) {
            logger.error("Request to Create Docx Service failed" + e);
            throw new CPPRuntimeException(CPPExceptionType.SERVICE_FAILED, " Service failed :" + e);
        }
        return file;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void savePricingExhibit(SavePricingExhibitDTO savePricingExhibitDTO, String userName) {
        ReviewDTO review = fetchReviewData(savePricingExhibitDTO.getContractPriceProfileSeq());
        File file = createExhibitDocument(review, savePricingExhibitDTO.getContractPriceProfileSeq());

        if (file == null) {
            logger.error("Pricing Exhibit document generation failed for contractPriceProfileSeq {}",
                    savePricingExhibitDTO.getContractPriceProfileSeq());
            throw new CPPRuntimeException(CPPExceptionType.SAVE_EXHIBIT_SERVICE_FAILED, "Pricing Exhibit document generation failed.");
        }

        ContractPricingResponseDTO contractPriceProfile = contractPriceProfileRepository
                .fetchContractDetailsByCppSeq(savePricingExhibitDTO.getContractPriceProfileSeq());
        String exhibitSysId = contractPriceProfile.getPricingExhibitSysId();
        exhibitSysId = clmApiProxy.savePricingExhibit(file, savePricingExhibitDTO.getContractAgeementId(), exhibitSysId,
                savePricingExhibitDTO.getContractTypeName());
        contractPriceProfileRepository.updatePricingExhibitGuid(savePricingExhibitDTO.getContractPriceProfileSeq(), exhibitSysId, userName);
    }

    private ReviewDTO buildReviewDTO(int contractPriceProfileSeq) {
        ReviewDTO reviewDTO = new ReviewDTO();
        ContractPricingReviewDTO contractPricingReviewDTO = fetchContractPricingReviewData(contractPriceProfileSeq);
        MarkupReviewDTO markupReviewDTO = fetchMarkUpReviewData(contractPriceProfileSeq, null, null);
        List<String> distributionCenter = fetchDistributionCenterReviewData(contractPriceProfileSeq);
        SplitCaseReviewDTO splitCaseReviewDTO = fetchSplitCaseReviewData(contractPriceProfileSeq);
        reviewDTO.setContractPricingReviewDTO(contractPricingReviewDTO);
        reviewDTO.setDistributionCenter(distributionCenter);
        reviewDTO.setMarkupReviewDTO(markupReviewDTO);
        reviewDTO.setSplitCaseReviewDTO(splitCaseReviewDTO);
        return reviewDTO;
    }

    void buildCostProducts(int contractPriceProfileSeq, ContractPricingReviewDTO contractPricingReviewDTO, int priceAuditInd,
            int prcProfVerificationInd) {
        ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        if (contractPricingResponseDTO != null) {
            Integer costModel = costModelMapRepository.fetchCostModelId(contractPricingResponseDTO.getTransferFeeFlag(),
                    contractPricingResponseDTO.getAssessmentFeeFlag(), priceAuditInd, prcProfVerificationInd);
            if (costModel != null) {
                setCostModelFeeFlags(contractPricingReviewDTO, contractPricingResponseDTO, String.valueOf(costModel));
            } else {
                setCostModelFeeFlags(contractPricingReviewDTO, contractPricingResponseDTO, CPPConstants.REVIEW_DEFAULT);
            }
        } else {
            contractPricingReviewDTO.setGfsTransferFee(CPPConstants.NO);
            contractPricingReviewDTO.setGfsLabelAssesmentFee(CPPConstants.NO);
            int contractLanguageSeqForCostModel = reviewRepository.fetchContractLanguageSeq(CPPConstants.COST_MODEL);
            contractPricingReviewDTO
                    .setCostOfProductsContractLanguage(reviewRepository.fetchContractLanguage(contractLanguageSeqForCostModel, CPPConstants.REVIEW_DEFAULT));
        }
    }

    int buildPriceVerification(int contractPriceProfileSeq, ContractPricingReviewDTO contractPricingReviewDTO) {
        String cppAttributeColumn;
        int prcProfPricePriviligeInd = prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(contractPriceProfileSeq);
        if (prcProfPricePriviligeInd == 0) {
            contractPricingReviewDTO.setPriceVerification(CPPConstants.NO);
            cppAttributeColumn = CPPConstants.ZERO_INDICATOR;
        } else {
            contractPricingReviewDTO.setPriceVerification(CPPConstants.YES);
            cppAttributeColumn = CPPConstants.ONE_INDICATOR;
        }
        int contractLanguageSeqForVerfification = reviewRepository.fetchContractLanguageSeq(CPPConstants.PRICE_VERIFICATION_PRVG);
        contractPricingReviewDTO
                .setPriceVerificationContractLanguage(reviewRepository.fetchContractLanguage(contractLanguageSeqForVerfification, cppAttributeColumn));
        return prcProfPricePriviligeInd;
    }

    int buildPriceAudit(int contractPriceProfileSeq, ContractPricingReviewDTO contractPricingReviewDTO,
            CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        String cppAttributeColumn;
        int prcProfAuditAuthInd = prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, cmgCustomerResponseDTO.getId(),
                cmgCustomerResponseDTO.getTypeCode());
        if (prcProfAuditAuthInd == 0) {
            contractPricingReviewDTO.setFormalPriceAudit(CPPConstants.NO);
            cppAttributeColumn = CPPConstants.ZERO_INDICATOR;
        } else {
            contractPricingReviewDTO.setFormalPriceAudit(CPPConstants.YES);
            cppAttributeColumn = CPPConstants.ONE_INDICATOR;
        }
        int contractLanguageSeqForAudit = reviewRepository.fetchContractLanguageSeq(CPPConstants.FORMAL_PRICE_AUDIT_PRVG);
        contractPricingReviewDTO
                .setFormalPriceAuditContractLanguage(reviewRepository.fetchContractLanguage(contractLanguageSeqForAudit, cppAttributeColumn));
        return prcProfAuditAuthInd;
    }

    void buildScheduleForCost(int contractPriceProfileSeq, ContractPricingReviewDTO contractPricingReviewDTO,
            CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        String cppAttributeColumn;
        int costRunSchedule = prcProfCostRunSchedGroupRepository.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, cmgCustomerResponseDTO.getId(),
                cmgCustomerResponseDTO.getTypeCode());
        if (costRunSchedule == CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M) {
            contractPricingReviewDTO.setScheduleForCost(CPPConstants.GFS_FISCAL_CALNEDAR);
            cppAttributeColumn = CPPConstants.REVIEW_DEFAULT;
        } else {
            contractPricingReviewDTO.setScheduleForCost(CPPConstants.GREGORIAN_CALENDAR);
            cppAttributeColumn = CostChangeFrequency.WEEKLY.getCode();
        }
        int contractLanguageSeqForSchedule = reviewRepository.fetchContractLanguageSeq(CPPConstants.COST_SCHEDULE_PACK);
        contractPricingReviewDTO
                .setScheduleForCostContractLanguage(reviewRepository.fetchContractLanguage(contractLanguageSeqForSchedule, cppAttributeColumn));
    }

    void setCostModelFeeFlags(ContractPricingReviewDTO contractPricingReviewDTO, ContractPricingResponseDTO contractPricingResponseDTO,
            String cppAttributeColumn) {
        contractPricingReviewDTO.setGfsTransferFee(contractPricingResponseDTO.getTransferFeeFlag() == 0 ? CPPConstants.NO : CPPConstants.YES);
        contractPricingReviewDTO.setGfsLabelAssesmentFee(contractPricingResponseDTO.getAssessmentFeeFlag() == 0 ? CPPConstants.NO : CPPConstants.YES);
        int contractLanguageSeqForCostModel = reviewRepository.fetchContractLanguageSeq(CPPConstants.COST_MODEL);
        contractPricingReviewDTO
                .setCostOfProductsContractLanguage(reviewRepository.fetchContractLanguage(contractLanguageSeqForCostModel, cppAttributeColumn));
    }
}
