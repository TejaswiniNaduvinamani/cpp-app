package com.gfs.cpp.component.furtherance;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceBaseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.dto.furtherance.SplitCaseGridFurtheranceDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentBuilder;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentHelper;
import com.gfs.cpp.component.markup.FutureItemDeleteHandler;
import com.gfs.cpp.component.markup.ItemMarkupDeleteHandler;
import com.gfs.cpp.component.splitcase.SplitcasePersister;
import com.gfs.cpp.component.statusprocessor.FurtheranceStatusValidator;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@Service
public class FurtheranceService {

    private static final String INVALID_CONTRACT_STATUS = "Contract Status is not valid";

    private static final Logger logger = LoggerFactory.getLogger(FurtheranceService.class);

    @Autowired
    private FurtheranceStatusValidator furtheranceStatusValidator;

    @Autowired
    private CppFurtheranceRepository cppFurtheranceRepository;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private ClmContractTypeRepository clmContractTypeRepository;

    @Autowired
    private FurtheranceMarkupSaver furtheranceMarkupSaver;

    @Autowired
    private FurtheranceChangeTracker furtheranceChangeTracker;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private FutureItemDeleteHandler futureItemDeleteHandler;

    @Autowired
    private FurtherancePricingProcessor furtherancePricingProcessor;

    @Autowired
    private FurtheranceDTOBuilder furtheranceDTOBuilder;

    @Autowired
    private FurtheranceDocumentGenerator furtheranceDocumentGenerator;

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private ItemAssignmentHelper itemAssignmentHelper;

    @Autowired
    private SplitcasePersister splitCasePersister;

    @Autowired
    private ClmApiProxy clmApiProxy;

    @Autowired
    private ItemAssignmentBuilder itemAssignmentBuilder;

    @Autowired
    private ItemMarkupDeleteHandler itemMarkupDeleteHandler;

    public boolean hasInProgressFurtherance(String parentAgreementId) {

        return fetchInProgressFurtherance(parentAgreementId) != null;
    }

    public FurtheranceBaseDTO fetchInProgressFurtheranceInfo(String parentAgreementId) {

        FurtheranceInformationDTO inProgressFurtherance = fetchInProgressFurtherance(parentAgreementId);

        ContractPricingResponseDTO contractPriceProfileDto = contractPriceProfileRepository
                .fetchContractDetailsByCppSeq(inProgressFurtherance.getContractPriceProfileSeq());

        return furtheranceDTOBuilder.buildFurtheranceBaseDTO(contractPriceProfileDto.getClmAgreementId(),
                inProgressFurtherance.getCppFurtheranceSeq(), getContractTypeName(contractPriceProfileDto.getClmContractTypeSeq()));
    }

    public FurtheranceBaseDTO createNewFurtherance(String parentAgreementId, String contractType) {

        furtheranceStatusValidator.validateNewFurtheranceCreation(parentAgreementId, contractType);

        ContractPricingResponseDTO latestActivatedVersion = contractPriceProfileRepository
                .fetchContractDetailsForLatestActivatedContractVersion(parentAgreementId);

        if (latestActivatedVersion == null) {
            logger.error("There is no activated version for this contract {}, furtherance can't be created ", parentAgreementId);
            throw new CPPRuntimeException(CPPExceptionType.CANNOT_CREATE_FURTHERANCE, INVALID_CONTRACT_STATUS);
        }

        return furtheranceDTOBuilder.buildFurtheranceBaseDTO(latestActivatedVersion.getClmAgreementId(),
                cppFurtheranceRepository.fetchCPPFurtheranceNextSequence(), getContractTypeName(latestActivatedVersion.getClmContractTypeSeq()));
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void saveFurtheranceInformation(FurtheranceInformationDTO furtheranceInformationDTO, String userName) {

        FurtheranceInformationDTO savedFurtheranceInformationDTO = getFurtheranceInformationDTO(furtheranceInformationDTO.getCppFurtheranceSeq());
        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_INITIATED.getCode());
        if (savedFurtheranceInformationDTO != null) {
            cppFurtheranceRepository.updateCPPFurtherance(furtheranceInformationDTO, userName);
        } else {
            saveCPPFurtherance(furtheranceInformationDTO, userName);
        }
    }

    public FurtheranceInformationDTO fetchFurtheranceInformation(String parentAgreementId, int furtheranceSeq) {

        FurtheranceInformationDTO furtheranceInformationDTO = getFurtheranceInformationDTO(furtheranceSeq);

        if (furtheranceInformationDTO == null) {
            return furtheranceDTOBuilder.buildDefaultFurtheranceDTO(parentAgreementId, furtheranceSeq);
        }

        return furtheranceInformationDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void saveMarkupForFurtherance(MarkupWrapperDTO markupWrapperDTO, String userName) {

        furtheranceStatusValidator.validateIfFurtheranceEditableStatus(markupWrapperDTO.getContractPriceProfileSeq(),
                markupWrapperDTO.getCppFurtheranceSeq());

        furtheranceMarkupSaver.saveMarkupForFurtherance(markupWrapperDTO, userName);

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void activatePricingForFurtherance(int cppFurtheranceSeq, String userName, String clmContractStatus) {

        logger.info("Started price activation for furtherance for cpp furtherance {}", cppFurtheranceSeq);

        FurtheranceInformationDTO furtheranceInformationDTO = getFurtheranceInformationDTO(cppFurtheranceSeq);

        ContractPricingResponseDTO contractDetails = contractPriceProfileRepository
                .fetchContractDetailsByCppSeq(furtheranceInformationDTO.getContractPriceProfileSeq());

        Date currentDate = cppDateUtils.getCurrentDate();
        furtheranceStatusValidator.validateIfPricingCanBeActivated(contractDetails.getClmContractEndDate(), clmContractStatus, currentDate,
                furtheranceInformationDTO);

        updatePricingEffectiveDate(furtheranceInformationDTO, currentDate);
        Date expirationDateToSetForExistingPricing = cppDateUtils.getPreviousDate(furtheranceInformationDTO.getFurtheranceEffectiveDate());

        furtherancePricingProcessor.expireAndSaveFurtheranceUpdates(expirationDateToSetForExistingPricing, furtheranceInformationDTO, userName);

        cppFurtheranceRepository.updateCPPFurtheranceStatusToPricingActivated(furtheranceInformationDTO, userName);
        logger.info("Completed processing for Price activation for furtherance for cppFurtheranceSeq {}", cppFurtheranceSeq);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deleteItemLevelMarkup(int contractPriceProfileSeq, int cppFurtheranceSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemId, String itemDesc, String userName) {

        if (StringUtils.isNotBlank(itemId)) {
            itemMarkupDeleteHandler.deleteItemMarkupForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode,
                    itemId, userName);
        } else {
            futureItemDeleteHandler.deleteFutureItemWithAssignedItemsForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId,
                    gfsCustomerTypeCode, itemDesc, userName);
        }
    }

    public ItemAssignmentWrapperDTO fetchMappedItemsForFurtherance(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemDesc) {

        FutureItemDescriptionDTO futureItemDescriptionDTO = customerItemDescPriceRepository.fetchFutureItemForFurtherance(contractPriceProfileSeq,
                gfsCustomerId, gfsCustomerTypeCode, itemDesc);

        return itemAssignmentBuilder.buildItemAssignmentWrapperDTO(futureItemDescriptionDTO, contractPriceProfileSeq);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void saveSplitCaseFeeForFurtherance(SplitCaseGridFurtheranceDTO splitCaseGridFurtheranceDTO, String userName) {

        furtheranceStatusValidator.validateIfFurtheranceEditableStatus(splitCaseGridFurtheranceDTO.getContractPriceProfileSeq(),
                splitCaseGridFurtheranceDTO.getCppFurtheranceSeq());

        splitCasePersister.saveSplicateForFurtherance(splitCaseGridFurtheranceDTO, userName);

    }

    public File createFurtheranceDocument(int cppFurtheranceSeq) {
        return furtheranceDocumentGenerator.createFurtheranceDocument(cppFurtheranceSeq);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void savePricingDocumentForFurtherance(FurtheranceBaseDTO furtheranceBaseDTO, String userName) {
        File file = createFurtheranceDocument(furtheranceBaseDTO.getCppFurtheranceSeq());
        if (file == null) {
            logger.error("Pricing document generation failed for furtherance sequesnce {}", furtheranceBaseDTO.getCppFurtheranceSeq());
            throw new CPPRuntimeException(CPPExceptionType.SAVE_EXHIBIT_SERVICE_FAILED, "Furtherance Pricing document generation failed.");
        }
        FurtheranceInformationDTO furtheranceInformationDTO = getFurtheranceInformationDTO(furtheranceBaseDTO.getCppFurtheranceSeq());
        String furtheranceDocumentGuid = furtheranceInformationDTO.getFurtheranceDocumentGUID();

        furtheranceDocumentGuid = clmApiProxy.saveFurtheranceExhibit(file, furtheranceBaseDTO.getAgreementId(), furtheranceDocumentGuid,
                furtheranceBaseDTO.getContractType());

        cppFurtheranceRepository.updateFurtheranceStatusToSavedWithGUID(furtheranceBaseDTO.getCppFurtheranceSeq(), furtheranceDocumentGuid, userName);
    }

    public Map<String, Boolean> validateActivatePricingForFurtherance(int furtheranceSeq, String clmContractStatus) {

        Map<String, Boolean> validateActivatePricingEnablerMap = new HashMap<>();
        boolean enableActivatePriceProfileButton = false;

        FurtheranceInformationDTO furtheranceInformationDTO = getFurtheranceInformationDTO(furtheranceSeq);

        if (furtheranceInformationDTO != null) {
            try {
                furtheranceStatusValidator.validateIfActivatePricingCanBeEnabledForFurtherance(furtheranceInformationDTO, clmContractStatus);
                enableActivatePriceProfileButton = true;
            } catch (CPPRuntimeException cppre) {
                logger.error("Validation to enable activate price profile button for furtherance failed for contract  {}", furtheranceSeq);
            }
        }

        validateActivatePricingEnablerMap.put(CPPConstants.ENABLE_ACTIVATE_PRICING_BUTTON, enableActivatePriceProfileButton);
        return validateActivatePricingEnablerMap;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deleteMappedItemForFurtherance(int contractPriceProfileSeq, int cppFurtheranceSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemId, String itemDesc, String userName) {

        furtheranceStatusValidator.validateIfFurtheranceEditableStatus(contractPriceProfileSeq, cppFurtheranceSeq);
        itemMarkupDeleteHandler.deleteMappledItemMarkupForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId, itemDesc, userName);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public StatusDTO assignItemsForFurtherance(ItemAssignmentWrapperDTO itemAssignmentWrapperDTO, String userName) {
        StatusDTO statusDTO = new StatusDTO();
        furtheranceStatusValidator.validateIfFurtheranceEditableStatus(itemAssignmentWrapperDTO.getContractPriceProfileSeq(),
                itemAssignmentWrapperDTO.getCppFurtheranceSeq());
        try {
            if (CollectionUtils.isNotEmpty(itemAssignmentWrapperDTO.getItemAssignmentList())) {
                itemAssignmentHelper.assignItemsWithFutureItem(itemAssignmentWrapperDTO, userName);
                List<String> itemIdList = new ArrayList<>();
                for (ItemAssignmentDTO itemAssignmentDTO : itemAssignmentWrapperDTO.getItemAssignmentList()) {
                    itemIdList.add(itemAssignmentDTO.getItemId());
                }
                furtheranceChangeTracker.addTrackingForMarkupAdd(itemAssignmentWrapperDTO.getCppFurtheranceSeq(),
                        itemAssignmentWrapperDTO.getGfsCustomerId(), itemAssignmentWrapperDTO.getGfsCustomerTypeCode(), itemIdList, userName);
                statusDTO.setErrorCode(HttpStatus.OK.value());
            }
        } catch (CPPRuntimeException cpp) {
            statusDTO.setErrorCode(cpp.getErrorCode());
            statusDTO.setErrorMessage(cpp.getMessage());
        }
        return statusDTO;
    }

    private void saveCPPFurtherance(FurtheranceInformationDTO furtheranceInformationDTO, String userName) {
        furtheranceInformationDTO.setContractPriceProfileSeq(contractPriceProfileRepository
                .fetchContractDetailsForLatestActivatedContractVersion(furtheranceInformationDTO.getParentCLMAgreementId())
                .getContractPriceProfileSeq());
        cppFurtheranceRepository.saveCPPFurtherance(furtheranceInformationDTO, userName);
    }

    private void updatePricingEffectiveDate(FurtheranceInformationDTO furtheranceInformationDTO, Date currentDate) {
        if (furtheranceInformationDTO.getFurtheranceEffectiveDate().compareTo(currentDate) < 0) {
            furtheranceInformationDTO.setFurtheranceEffectiveDate(cppDateUtils.getCurrentDateAsLocalDate().toDate());
        }
    }

    private String getContractTypeName(int contractTypeSeq) {
        return clmContractTypeRepository.getContractTypeName(contractTypeSeq);
    }

    private FurtheranceInformationDTO getFurtheranceInformationDTO(int cppFurtheranceSeq) {
        return cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
    }

    private FurtheranceInformationDTO fetchInProgressFurtherance(String parentAgreementId) {
        return cppFurtheranceRepository.fetchInProgressFurtheranceDetailsByParentAgreementId(parentAgreementId);
    }

}
