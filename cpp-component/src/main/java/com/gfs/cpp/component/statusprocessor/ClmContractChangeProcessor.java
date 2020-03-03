package com.gfs.cpp.component.statusprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.cpp.common.dto.clm.ClmContractAction;
import com.gfs.cpp.common.dto.clm.ClmContractChangeEventDTO;
import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@Component
public class ClmContractChangeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ClmContractChangeProcessor.class);

    @Autowired
    private PricingContractCreateProcessor pricingContractCreateProcessor;
    @Autowired
    private DeletedStatusProcessor deletedStatusProcessor;
    @Autowired
    private ContractPriceProfileStatusMaintainer contractPriceProfileStatusMaintainer;
    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;
    @Autowired
    private ContractPriceProfileDateSyncher contractPriceProfileDateSyncher;
    @Autowired
    private ContractPriceProfileNameSyncher contractPriceProfileNameSyncher;
    @Autowired
    private ClmApiProxy clmApiProxy;

    static final String LAST_UDDATE_USER_ID = "Cpp Data Syncher";

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void processChangeEvent(ClmContractChangeEventDTO contractChangeEvent) {

        logger.info("Processing contract change event {} for contract {} of type {}", contractChangeEvent.getAction(),
                contractChangeEvent.getAgreementId(), contractChangeEvent.getContractType());

        ClmContractAction contractAction = ClmContractAction.getByValue(contractChangeEvent.getAction());

        if (contractAction != null) {
            processClmChangeEvent(contractChangeEvent, contractAction);
        }
    }

    private void processClmChangeEvent(ClmContractChangeEventDTO contractChangeEvent, ClmContractAction clmContractAction) {

        if (ClmContractAction.PUBLISHED == clmContractAction || ClmContractAction.AMENDMENT_PUBLISHED == clmContractAction) {
            pricingContractCreateProcessor.process(contractChangeEvent);
        } else {
            ContractPricingResponseDTO contractDataInCpp = contractPriceProfileRepository
                    .fetchContractDetailsByAgreementId(contractChangeEvent.getAgreementId());
            if (contractDataInCpp != null) {
                syncClmDataWithCpp(contractChangeEvent, contractDataInCpp, clmContractAction);
            }
        }

    }

    private void syncClmDataWithCpp(ClmContractChangeEventDTO contractChangeEvent, ContractPricingResponseDTO contractDataInCpp,
            ClmContractAction clmContractAction) {

        if (ClmContractAction.DELETED == clmContractAction || ClmContractAction.AMENDMENT_DELETED == clmContractAction) {
            deletedStatusProcessor.process(contractChangeEvent.getAgreementId());
            return;
        }

        if (ClmContractAction.EXECUTED == clmContractAction) {
            clmApiProxy.updateFurtheranceUrlForPricingContract(contractChangeEvent.getAgreementId(), contractChangeEvent.getContractType());
        }

        ClmContractResponseDTO latestAgreementDataFromClm = clmApiProxy.getAgreementData(contractChangeEvent.getAgreementId(),
                contractChangeEvent.getContractType());

        ContractPriceProfileStatus cppStatus = ContractPriceProfileStatus.getStatusByCode(contractDataInCpp.getContractPriceProfStatusCode());

        processContractDataChange(contractDataInCpp, clmContractAction, latestAgreementDataFromClm, cppStatus);

        contractPriceProfileStatusMaintainer.syncWithClmStatus(contractDataInCpp, latestAgreementDataFromClm.getContractStatus());
    }

    private void processContractDataChange(ContractPricingResponseDTO contractDataInCpp, ClmContractAction clmContractAction,
            ClmContractResponseDTO latestAgreementDataFromClm, ContractPriceProfileStatus cppStatus) {
        if (ClmContractAction.UPDATED == clmContractAction && ContractPriceProfileStatus.PRICING_ACTIVATED != cppStatus) {
            contractPriceProfileDateSyncher.synchClmContractDatesWithCpp(latestAgreementDataFromClm, contractDataInCpp, LAST_UDDATE_USER_ID);
            contractPriceProfileNameSyncher.synchCLMContractNameWithCPPContractName(latestAgreementDataFromClm.getContractName(),
                    contractDataInCpp.getContractPriceProfileSeq(), LAST_UDDATE_USER_ID);
        }
    }

}
