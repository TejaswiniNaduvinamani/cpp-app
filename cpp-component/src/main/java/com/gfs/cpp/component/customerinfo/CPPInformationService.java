package com.gfs.cpp.component.customerinfo;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.clm.ClmContractDTO;
import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@Service("cppInformationService")
public class CPPInformationService {

    @Autowired
    private ClmApiProxy clmApiProxy;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    CppVersionCreator cppVersionCreator;

    @Autowired
    private AmendmentContractCreator amendmentContractCreator;

    @Autowired
    private CppUserDetailsService cppUserDetailsService;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public ClmContractDTO fetchContractPriceProfileInfo(String agreementId, String contractType) {
        CPPInformationDTO cppInformationDTO = contractPriceProfileRepository.fetchCPPInformation(agreementId);
        ClmContractResponseDTO clmContractResponseDTO = clmApiProxy.getAgreementData(agreementId, contractType);
        
        String contractName = clmContractResponseDTO.getContractName();
        contractName = contractName.substring(0, Math.min(contractName.length(), CPPConstants.MAX_CONTRACT_NAME_LENGTH));
        clmContractResponseDTO.setContractName(contractName);

        if (cppInformationDTO == null) {
            cppInformationDTO = createNewContractVersion(clmContractResponseDTO);
        }
        return buildClmContractDTO(clmContractResponseDTO, cppInformationDTO);

    }

    private CPPInformationDTO createNewContractVersion(ClmContractResponseDTO agreementData) {

        validateUserHasAccessToCreateNewContract();
        validateClmStatusToCreateNewContract(agreementData.getContractStatus());
        validateContractIfExpired(agreementData.getContractExpirationDate());

        if (agreementData.isAmendment()) {
            return amendmentContractCreator.createNewContractVersion(agreementData);
        }

        return cppVersionCreator.createInitialVersion();
    }

    private void validateClmStatusToCreateNewContract(String clmContractStatus) {

        if (!clmContractStatus.equals(ClmContractStatus.DRAFT.value)) {
            throw new CPPRuntimeException(CPPExceptionType.NEW_CONTRACT_INVALID_STATUS, "Invalid clm status for new contract.");
        }
    }

    private void validateContractIfExpired(Date expirationDate) {

        if (expirationDate.before(cppDateUtils.getCurrentDateAsLocalDate().toDate())) {
            throw new CPPRuntimeException(CPPExceptionType.EXPIRED_CONTRACT, "Pricing cannot be created for an expired contract.");
        }
    }

    private void validateUserHasAccessToCreateNewContract() {

        if (!cppUserDetailsService.hasContractEditAccess()) {
            throw new CPPRuntimeException(CPPExceptionType.ACCESS_DENIED, "User does not have privileges to create new contract.");
        }

    }

    private ClmContractDTO buildClmContractDTO(ClmContractResponseDTO clmContractResponseDTO, CPPInformationDTO cppInformationDto) {
        ClmContractDTO clmContractDTO = new ClmContractDTO();
        clmContractDTO.setAgreementId(clmContractResponseDTO.getContractAgreementId());
        clmContractDTO.setIsAmendment(clmContractResponseDTO.isAmendment());
        clmContractDTO.setContractStartDate(getEffectiveStartDate(clmContractResponseDTO));
        clmContractDTO.setContractEndDate(clmContractResponseDTO.getContractExpirationDate());
        clmContractDTO.setContractStatus(clmContractResponseDTO.getContractStatus());
        clmContractDTO.setContractName(clmContractResponseDTO.getContractName());
        clmContractDTO.setParentAgreementId(clmContractResponseDTO.getParentAgreementId());
        clmContractDTO.setContractType(clmContractResponseDTO.getContractTypeName());
        clmContractDTO.setCppInformationDto(cppInformationDto);
        return clmContractDTO;
    }

    private Date getEffectiveStartDate(ClmContractResponseDTO clmContractResponseDTO) {
        if (clmContractResponseDTO.isAmendment()) {
            return clmContractResponseDTO.getAmendmentEffectiveDate();
        }
        return clmContractResponseDTO.getContractEffectiveDate();
    }

}
