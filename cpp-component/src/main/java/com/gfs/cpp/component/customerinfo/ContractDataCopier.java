package com.gfs.cpp.component.customerinfo;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.distributioncenter.ContractPriceProfShipDcRepository;

@Component
public class ContractDataCopier {

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private CppUserDetailsService gfsUserDetailsService;

    @Autowired
    private ContractDataCopyHelper contractDataCopyHelper;

    @Autowired
    private ContractPriceProfShipDcRepository contractPriceProfShipDcRepository;

    @Autowired
    private ContractItemCopier contractItemCopier;

    @Autowired
    private ContractCustomerCopier contractCustomerCopier;

    @Autowired
    private ContractPrcProfCopier contractPrcProfCopier;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private ClmContractTypeRepository clmContractTypeRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void copyContractDataToNewVersion(ContractPricingResponseDTO contractDetailsOfLatestVersion, ClmContractResponseDTO agreementData,
            CPPInformationDTO cppInformationDTO) {
        logger.info("Copying an existing contract with cpp sequence: {} to generate new contract with cpp sequence {}",
                contractDetailsOfLatestVersion.getContractPriceProfileSeq(), cppInformationDTO.getContractPriceProfileSeq());

        String userId = gfsUserDetailsService.getCurrentUserId();

        Date farOutDate = cppDateUtils.getFutureDate();

        copyContractPriceProfile(contractDetailsOfLatestVersion, userId, agreementData, cppInformationDTO);
        copyCPPShipDC(cppInformationDTO.getContractPriceProfileSeq(), contractDetailsOfLatestVersion.getContractPriceProfileSeq(), userId,
                agreementData.getAmendmentEffectiveDate(), agreementData.getContractExpirationDate());

        List<CMGCustomerResponseDTO> allCMGCustomersOfLatestVersion = contractCustomerCopier
                .fetchGFSCustomerDetailsListForLatestContractVersion(contractDetailsOfLatestVersion.getContractPriceProfileSeq());

        contractCustomerCopier.copyCustomerAndCustomerMapping(cppInformationDTO.getContractPriceProfileSeq(), userId, allCMGCustomersOfLatestVersion);

        contractItemCopier.copyAllItemAndMappings(cppInformationDTO.getContractPriceProfileSeq(),
                contractDetailsOfLatestVersion.getContractPriceProfileSeq(), userId, agreementData.getContractExpirationDate(), farOutDate);

        CMGCustomerResponseDTO defaultCMGForLatestContractVer = contractCustomerCopier
                .retrieveDefaultCMGForLatestContractVer(allCMGCustomersOfLatestVersion);

        if (defaultCMGForLatestContractVer != null) {
            logger.info("Found default CMG for existing contract {}", contractDetailsOfLatestVersion.getContractPriceProfileSeq());

            contractPrcProfCopier.copyPrcProfEntries(cppInformationDTO.getContractPriceProfileSeq(),
                    contractDetailsOfLatestVersion.getContractPriceProfileSeq(), userId, agreementData.getAmendmentEffectiveDate(),
                    agreementData.getContractExpirationDate(), defaultCMGForLatestContractVer, farOutDate);
        }

    }

    private void copyContractPriceProfile(ContractPricingResponseDTO contractDetailsOfLatestVersion, String userId,
            ClmContractResponseDTO agreementData, CPPInformationDTO cppInformationDTO) {
        logger.info("Copying contract price profile from existing contract {}", contractDetailsOfLatestVersion.getContractPriceProfileSeq());

        ContractPricingDO contractPricingDO = contractDataCopyHelper.buildContractPricingDO(agreementData, contractDetailsOfLatestVersion,
                cppInformationDTO);

        int contractTypeSeq = clmContractTypeRepository.fetchContractTypeSequenceByTypeName(agreementData.getContractTypeName());
        contractPriceProfileRepository.saveContractPricingDetails(contractPricingDO, userId, contractTypeSeq,
                cppInformationDTO.getContractPriceProfileSeq());
    }

    void copyCPPShipDC(int newCPPSeq, int existingCPPSeq, String userId, Date effectiveDate, Date expirationDate) {
        logger.info("Copying ship DC for contract {}", newCPPSeq);

        List<String> shipDCNumberList = contractPriceProfShipDcRepository.fetchDistributionCentersbyContractPriceProfileSeq(existingCPPSeq);

        DistributionCenterDO distributionCenterDO = buildDistributionCenterDO(shipDCNumberList, newCPPSeq, userId, effectiveDate, expirationDate);
        contractPriceProfShipDcRepository.saveDistributionCenter(distributionCenterDO, userId);
    }

    private DistributionCenterDO buildDistributionCenterDO(List<String> shipDCNumberList, int newCPPSeq, String userId, Date effectiveDate,
            Date expirationDate) {
        DistributionCenterDO distributionCenterDO = new DistributionCenterDO();
        distributionCenterDO.setContractPriceProfileSeq(newCPPSeq);
        distributionCenterDO.setCreateUserID(userId);
        distributionCenterDO.setEffectiveDate(effectiveDate);
        distributionCenterDO.setExpirationDate(expirationDate);
        distributionCenterDO.setDcCodes(shipDCNumberList);

        return distributionCenterDO;
    }

}
