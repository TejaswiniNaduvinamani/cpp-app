package com.gfs.cpp.component.markup;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.ExceptionMarkupRenameDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.markup.builder.MarkupDTOBuilder;
import com.gfs.cpp.component.markup.builder.MarkupWrapperBuilder;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;
import com.gfs.cpp.proxy.ItemConfigurationServiceProxy;

@Service
public class MarkupService {

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    @Autowired
    private MarkupFetcher markupFetcher;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    @Autowired
    private ItemConfigurationServiceProxy itemConfigurationServiceProxy;

    @Autowired
    private SubgroupValidator subgroupValidator;

    @Autowired
    private SubgroupResponseDTOBuilder subgroupResponseDTOBuilder;

    @Autowired
    private MarkupWrapperBuilder markupWrapperBuilder;

    @Autowired
    private MarkupDTOBuilder markupDTOBuilder;

    @Autowired
    private MarkupSaver markupSaver;

    @Autowired
    private MarkupOnSellUpdater markupOnSellUpdater;

    @Autowired
    private FutureItemDeleteHandler futureItemDeleteHandler;

    public static final Logger logger = LoggerFactory.getLogger(MarkupService.class);

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void saveMarkup(MarkupWrapperDTO markupWrapper, String userName) {
        int contractPriceProfileSeq = markupWrapper.getContractPriceProfileSeq();
        contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(contractPriceProfileSeq);
        markupSaver.saveMarkups(markupWrapper, contractPriceProfileSeq, userName);
        markupOnSellUpdater.updateMarkupOnSell(contractPriceProfileSeq, userName);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void saveMarkupIndicators(MarkupRequestDTO markupRequest, String userName) {
        contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(markupRequest.getContractPriceProfileSeq());
        CMGCustomerResponseDTO cmgCustomerResponseDTO = fetchDefaultCustomerDetail(markupRequest.getContractPriceProfileSeq());
        contractPriceProfileRepository.updateExpireLowerIndicator(markupRequest.getContractPriceProfileSeq(), markupRequest.isExpireLower(),
                userName);
        markupOnSellUpdater.saveOrUpdateMarkupOnSellIndicator(markupRequest, userName, cmgCustomerResponseDTO.getId(),
                cmgCustomerResponseDTO.getTypeCode());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public MarkupWrapperDTO addExceptionMarkup(int contractPriceProfileSeq, String exceptionName, Date pricingEffectiveDate,
            Date pricingExpirationDate, String userName) {
        contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(contractPriceProfileSeq);
        ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = customerServiceProxy
                .createCMGCustomerGroup(contractPricingResponseDTO.getContractPriceProfileId(), exceptionName);

        int cppCustomerSeq = contractPriceProfCustomerRepository.fetchCPPCustomerNextSequence();

        contractPriceProfCustomerRepository.saveContractPriceProfCustomer(contractPriceProfileSeq, cmgCustomerResponseDTO.getId(),
                cmgCustomerResponseDTO.getTypeCode(), userName, CPPConstants.INDICATOR_ZERO, cppCustomerSeq);

        return fetchDefaultException(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate, exceptionName,
                cmgCustomerResponseDTO.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void renameExceptionMarkup(ExceptionMarkupRenameDTO renameExceptionDTO) {
        contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(renameExceptionDTO.getContractPriceProfileSeq());
        customerServiceProxy.updateCMGCustomer(CPPConstants.CUSTOMER_GROUP_CMG, buildGroupName(renameExceptionDTO),
                renameExceptionDTO.getGfsCustomerId());
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deleteExceptionMarkup(int contractPriceProfileSeq, String gfsCustomerId, String markupName, String userName) {
        contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(contractPriceProfileSeq);

        int customerDeleted = deleteCPPCustomer(contractPriceProfileSeq, gfsCustomerId);

        if (customerDeleted > 0) {
            customerItemPriceRepository.deleteExceptionData(contractPriceProfileSeq, gfsCustomerId);
            customerItemDescPriceRepository.deleteAllFutureItem(contractPriceProfileSeq, gfsCustomerId);
            markupOnSellUpdater.updateMarkupOnSell(contractPriceProfileSeq, userName);
        }
    }

    public SubgroupMarkupDTO createDefaultSubgroupMarkup(Date pricingExpirationDate, Date pricingEffectiveDate) {
        return markupDTOBuilder.buildDefaultSubgroupMarkupDTO(pricingExpirationDate, pricingEffectiveDate);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deleteItemLevelMarkup(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerType, String itemId, String itemDesc,
            String userName) {
        if (StringUtils.isNotBlank(itemId) || StringUtils.isNotBlank(itemDesc)) {
            contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(contractPriceProfileSeq);
            if (StringUtils.isNotBlank(itemId)) {
                logger.info("Deleting existing items for markup grid: {}", gfsCustomerId);
                List<String> itemIdList = new ArrayList<>();
                itemIdList.add(itemId);
                customerItemPriceRepository.deleteExistingItemOrSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemIdList,
                        ItemPriceLevel.ITEM.getCode());
            } else {
                logger.info("Deleting future items for markup grid: {}", gfsCustomerId);
                futureItemDeleteHandler.deleteFutureItemWithAssignedItems(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemDesc);
            }
            markupOnSellUpdater.updateMarkupOnSell(contractPriceProfileSeq, userName);
        }
    }

    public ItemLevelMarkupDTO createDefaultItemLevelMarkup(int contractPriceProfileSeq, Date pricingExpirationDate, Date pricingEffectiveDate) {
        logger.info("Creating default markup for : {}", contractPriceProfileSeq);
        return markupDTOBuilder.buildDefaultItemLevelMarkupDTO(pricingExpirationDate, pricingEffectiveDate);
    }

    public List<MarkupWrapperDTO> fetchMarkups(int contractPriceProfileSeq, Date pricingExpirationDate, Date pricingEffectiveDate) {
        return markupFetcher.fetchAllMarkups(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate);
    }

    public Map<String, Boolean> fetchMarkupIndicators(int contractPriceProfileSeq) {
        Map<String, Boolean> markupIndicatorsMap = new HashMap<>();
        Integer expireLowerIndicator = contractPriceProfileRepository.fetchExpireLowerIndicator(contractPriceProfileSeq);
        if (expireLowerIndicator == null) {
            markupIndicatorsMap.put(CPPConstants.EXPIRE_LOWER, null);
        } else {
            boolean isExpireLower = expireLowerIndicator == 1 ? true : false;
            markupIndicatorsMap.put(CPPConstants.EXPIRE_LOWER, isExpireLower);
        }
        if (prcProfPricingRuleOvrdRepository.fetchSellPriceCount(contractPriceProfileSeq) > 0) {
            boolean markupOnSell = prcProfPricingRuleOvrdRepository.fetchPrcProfOvrdInd(contractPriceProfileSeq) == 1 ? true : false;
            markupIndicatorsMap.put(CPPConstants.MARKUP_ON_SELL, markupOnSell);
        }
        return markupIndicatorsMap;
    }

    public SubgroupResponseDTO findItemSubgroupInformation(String subgroupId, String gfsCustomerId, int gfsCustomerType,
            int contractPriceProfileSeq) {
        SubgroupResponseDTO subgroupResponseDTO = new SubgroupResponseDTO();
        try {
            subgroupValidator.validateIfSubgroupAlreadyExist(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, subgroupId);
            String subgroupDescription = itemConfigurationServiceProxy.getSubgroupDescriptionById(subgroupId);
            subgroupValidator.validateIfInvalidSubgroup(subgroupDescription, subgroupId);

            subgroupResponseDTO = subgroupResponseDTOBuilder.buildSubgroupResponseDTO(subgroupId, subgroupDescription, HttpStatus.OK.value(),
                    StringUtils.EMPTY);

        } catch (CPPRuntimeException cpp) {
            if (cpp.getType().equals(CPPExceptionType.INVALID_SUBGROUP) || cpp.getType().equals(CPPExceptionType.SUBGROUP_ALREADY_EXIST)) {
                subgroupResponseDTO = subgroupResponseDTOBuilder.buildSubgroupResponseDTO(subgroupId, StringUtils.EMPTY, cpp.getErrorCode(),
                        cpp.getMessage());
            } else
                throw cpp;
        }

        return subgroupResponseDTO;
    }

    public MarkupWrapperDTO fetchDefaultException(int contractPriceProfileSeq, Date pricingExpirationDate, Date pricingEffectiveDate,
            String exceptionName, String gfsCustomerId) {
        return markupWrapperBuilder.buildDefaultMarkupWrapper(contractPriceProfileSeq, gfsCustomerId, exceptionName, pricingEffectiveDate,
                pricingExpirationDate);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void deleteSubgroupMarkup(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerType, String subgroupId, String userName) {

        if (StringUtils.isNotBlank(subgroupId)) {
            contractPriceProfileStatusValidator.validateIfContractPricingEditableStatus(contractPriceProfileSeq);
            logger.info("Deleting existing subgroup for markup grid: {}", gfsCustomerId);
            List<String> subgroupIdList = new ArrayList<>();
            subgroupIdList.add(subgroupId);
            customerItemPriceRepository.deleteExistingItemOrSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, subgroupIdList,
                    ItemPriceLevel.SUBGROUP.getCode());
            markupOnSellUpdater.updateMarkupOnSell(contractPriceProfileSeq, userName);
        }

    }

    private CMGCustomerResponseDTO fetchDefaultCustomerDetail(int contractPriceProfileSeq) {
        return contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq);
    }

    private String buildGroupName(ExceptionMarkupRenameDTO renameExceptionDTO) {
        return renameExceptionDTO.getContractPriceProfileSeq() + CPPConstants.HYPHEN + renameExceptionDTO.getNewExceptionName();
    }

    private Integer deleteCPPCustomer(int contractPriceProfileSeq, String gfsCustomerId) {
        logger.info("Deleting customer for {}", contractPriceProfileSeq);
        return contractPriceProfCustomerRepository.deleteCPPCustomer(contractPriceProfileSeq, gfsCustomerId);
    }

}
