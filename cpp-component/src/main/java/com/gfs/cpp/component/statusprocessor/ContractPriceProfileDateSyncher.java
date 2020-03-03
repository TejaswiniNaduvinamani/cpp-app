package com.gfs.cpp.component.statusprocessor;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;

@Component
public class ContractPriceProfileDateSyncher {

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;
    @Autowired
    private PrcProfVerificationPrivlgRepository prcProfVerificationPrivlgRepository;

    public void synchClmContractDatesWithCpp(ClmContractResponseDTO latestAgreementDataFromClm, ContractPricingResponseDTO contractDataInCpp, String lastUpdateUserId) {

        boolean hasStartDateChanged = hasStartDateChanged(latestAgreementDataFromClm, contractDataInCpp.getClmContractStartDate());

        boolean hasEndDateChanged = hasEndDateChanged(latestAgreementDataFromClm.getContractExpirationDate(),
                contractDataInCpp.getClmContractEndDate());

        if (hasStartDateChanged || hasEndDateChanged) {

            Date pricingEffectiveDate = contractDataInCpp.getPricingEffectiveDate();
            Date contractStartDate = contractDataInCpp.getClmContractStartDate();
            Date contractEndDate = contractDataInCpp.getClmContractEndDate();

            if (hasStartDateChanged) {
                Date effectiveStartDate = getEffectiveStartDate(latestAgreementDataFromClm);
                pricingEffectiveDate = effectiveStartDate;
                contractStartDate = effectiveStartDate;
                prcProfVerificationPrivlgRepository.updatePriceProfileVerficationPrivlgEffectiveDate(contractDataInCpp.getContractPriceProfileSeq(),
                        effectiveStartDate, lastUpdateUserId);
            }

            if (hasEndDateChanged) {
                contractEndDate = latestAgreementDataFromClm.getContractExpirationDate();
            }

            contractPriceProfileRepository.updateContractPriceProfileDates(contractDataInCpp.getContractPriceProfileSeq(), pricingEffectiveDate,
                    contractStartDate, contractEndDate, lastUpdateUserId);
        }

    }

    private boolean hasEndDateChanged(Date contractExpirationDate, Date clmContractStartDate) {
        return !clmContractStartDate.equals(contractExpirationDate);
    }

    private boolean hasStartDateChanged(ClmContractResponseDTO latestAgreementDataFromClm, Date clmContractStartDate) {
        return !clmContractStartDate.equals(getEffectiveStartDate(latestAgreementDataFromClm));
    }

    private Date getEffectiveStartDate(ClmContractResponseDTO latestAgreementDataFromClm) {
        if (latestAgreementDataFromClm.isAmendment()) {
            return latestAgreementDataFromClm.getAmendmentEffectiveDate();
        }
        return latestAgreementDataFromClm.getContractEffectiveDate();

    }

}
