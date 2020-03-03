package com.gfs.cpp.component.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.service.authorization.AuthorizationDetailsDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;

@Component
public class AuthorizationService {

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private CppUserDetailsService cppUserDetailsService;

    @Autowired
    private CppFurtheranceRepository cppFurtheranceRepository;

    @Autowired
    private CPPDateUtils cppDateUtils;

    public AuthorizationDetailsDTO buildAuthorizationDetails(int contractPriceProfileSeq, boolean isAmendment, String clmContractStatus) {

        ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        AuthorizationDetailsDTO authorizationDetailsDTO = new AuthorizationDetailsDTO();

        ContractPriceProfileStatus contractPriceProfileStatus = fetchContractStatus(contractPricingResponseDTO);

        authorizationDetailsDTO.setCppStatus(contractPriceProfileStatus.desc);

        boolean isPowerUser = cppUserDetailsService.isPowerUser();
        boolean isPriceProfileEditable = isPriceProfileEditable(contractPriceProfileStatus);

        if ((contractPricingResponseDTO != null)
                && (contractPricingResponseDTO.getClmContractEndDate().before(cppDateUtils.getCurrentDateAsLocalDate().toDate())
                        && isPriceProfileEditable)) {
            throw new CPPRuntimeException(CPPExceptionType.EXPIRED_CONTRACT, "Pricing can't be created for an expired contract.");
        }

        authorizationDetailsDTO.setPowerUser(isPowerUser);

        authorizationDetailsDTO.setPriceProfileEditable(isPriceProfileEditable);

        boolean assignmentEditable = isAssignmentEditable(contractPriceProfileStatus, isAmendment, clmContractStatus, isPowerUser);

        authorizationDetailsDTO.setCustomerAssignmentEditable(assignmentEditable);
        authorizationDetailsDTO.setItemAssignmentEditable(assignmentEditable);
        authorizationDetailsDTO.setCostModelEditable(assignmentEditable);

        return authorizationDetailsDTO;
    }

    private boolean isPriceProfileEditable(ContractPriceProfileStatus contractPriceProfileStatus) {
        if (cppUserDetailsService.hasContractEditAccess()) {
            return isPriceProfileEditableStatus(contractPriceProfileStatus);
        }
        return false;
    }

    private boolean isPriceProfileEditableStatus(ContractPriceProfileStatus contractPriceProfileStatus) {
        return (contractPriceProfileStatus == ContractPriceProfileStatus.DRAFT);
    }

    private boolean isAssignmentEditable(ContractPriceProfileStatus contractPriceProfileStatus, boolean isAmendment, String clmContractStatus,
            boolean isPowerUser) {

        if (isPowerUser) {
            return isAssignmentEditableStatus(contractPriceProfileStatus, isAmendment, clmContractStatus);
        }

        return false;
    }

    private boolean isAssignmentEditableStatus(ContractPriceProfileStatus contractPriceProfileStatus, boolean isAmendment, String clmContractStatus) {

        if (isAmendment) {
            return (contractApproved(contractPriceProfileStatus) && contractExecutedInClm(clmContractStatus));
        }
        return contractApproved(contractPriceProfileStatus);
    }

    private boolean contractExecutedInClm(String clmContractStatus) {
        return ClmContractStatus.EXECUTED.value.equals(clmContractStatus) || ClmContractStatus.SUPERSEDED.value.equals(clmContractStatus);
    }

    private boolean contractApproved(ContractPriceProfileStatus contractPriceProfileStatus) {
        return contractPriceProfileStatus == ContractPriceProfileStatus.CONTRACT_APPROVED;
    }

    private ContractPriceProfileStatus fetchContractStatus(ContractPricingResponseDTO contractPricingResponseDTO) {

        ContractPriceProfileStatus contractPriceProfileStatus = ContractPriceProfileStatus.DRAFT;

        if (contractPricingResponseDTO != null) {
            contractPriceProfileStatus = ContractPriceProfileStatus.getStatusByCode(contractPricingResponseDTO.getContractPriceProfStatusCode());
        }
        return contractPriceProfileStatus;
    }

    public boolean canEditFurtherance(int furtheranceSeq) {

        if (cppUserDetailsService.hasContractEditAccess()) {
            FurtheranceInformationDTO furtheranceInformationDTO = cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(furtheranceSeq);

            return ((furtheranceInformationDTO == null) || (isFurtheranceEditableStatus(furtheranceInformationDTO.getFurtheranceStatusCode())));
        }

        return false;
    }

    private boolean isFurtheranceEditableStatus(int furtheranceStatusCode) {

        return (FurtheranceStatus.FURTHERANCE_INITIATED.code == furtheranceStatusCode
                || FurtheranceStatus.FURTHERANCE_SAVED.code == furtheranceStatusCode);
    }

    public Boolean isUserAuthorizedToViewOnly() {
        return !cppUserDetailsService.hasContractEditAccess();
    }

}
