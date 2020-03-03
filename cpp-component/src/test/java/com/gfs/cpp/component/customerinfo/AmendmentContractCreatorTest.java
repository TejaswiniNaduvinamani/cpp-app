package com.gfs.cpp.component.customerinfo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.component.customerinfo.AmendmentContractCreator;
import com.gfs.cpp.component.customerinfo.ContractDataCopier;
import com.gfs.cpp.component.customerinfo.CppVersionCreator;
import com.gfs.cpp.component.furtherance.FurtheranceService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;

@RunWith(MockitoJUnitRunner.class)
public class AmendmentContractCreatorTest {

    @InjectMocks
    private AmendmentContractCreator target;

    @Mock
    private ContractDataCopier contractDataCopier;

    @Mock
    private CppVersionCreator cppVersionCreator;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private CppFurtheranceRepository cppFurtheranceRepository;

    @Mock
    private FurtheranceService furtheranceService;

    @Test
    public void shouldThrowRuntimeExceptionWhileCreateNewContractVersion() throws Exception {
        ClmContractResponseDTO clmContractResponseDTO = buildClmContractResponseDTO();

        when(contractPriceProfileRepository.fetchInProgressContractVersionCount(clmContractResponseDTO.getParentAgreementId())).thenReturn(1);
        try {
            target.createNewContractVersion(clmContractResponseDTO);
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.IN_PROGREESS_VERSION_FOUND));
        }

        verify(contractPriceProfileRepository).fetchInProgressContractVersionCount(clmContractResponseDTO.getParentAgreementId());
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenContractStatusInCLMIsNotInDraft() throws Exception {
        ClmContractResponseDTO clmContractResponseDTO = buildClmContractResponseDTO();
        clmContractResponseDTO.setContractStatus(ContractPriceProfileStatus.WAITING_FOR_APPROVAL.getDesc());

        try {
            target.createNewContractVersion(clmContractResponseDTO);
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.CLM_STATUS_NOT_DRAFT));
        }

        verify(contractPriceProfileRepository, never()).fetchInProgressContractVersionCount(clmContractResponseDTO.getParentAgreementId());
    }

    @Test
    public void shouldThrowRuntimeExceptionWhileAmendmentCreationIfInProgressFurtheranceExist() throws Exception {
        ClmContractResponseDTO clmContractResponseDTO = buildClmContractResponseDTO();

        when(furtheranceService.hasInProgressFurtherance(clmContractResponseDTO.getParentAgreementId())).thenReturn(true);

        try {
            target.createNewContractVersion(clmContractResponseDTO);
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.IN_PROGRESS_FURTHERANCE_FOUND));
        }

        verify(furtheranceService).hasInProgressFurtherance(clmContractResponseDTO.getParentAgreementId());
    }

    
    @Test
    public void shouldThrowRuntimeExceptionWhileAmendmentEffectiveDatePastContractEndDate() throws Exception {
        ClmContractResponseDTO clmContractResponseDTO = buildClmContractResponseDTO();
        clmContractResponseDTO.setContractExpirationDate( new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("12/31/2019"));

        try {
            target.createNewContractVersion(clmContractResponseDTO);
            fail("expected cpp runtime exception");
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.INVALID_AMENDMENT_EFFECTIVE_DATE));
        }

    }
    
    @Test
    public void shouldCreateNewContractVersion() throws Exception {
        int latestVersionNumber = 3;
        int existingContractPriceProfileSeq = 1289;

        CPPInformationDTO cppInformationDTO = buildCPPInformationDTO();

        ClmContractResponseDTO clmContractResponseDTO = buildClmContractResponseDTO();
        clmContractResponseDTO.setContractStatus(ContractPriceProfileStatus.DRAFT.getDesc());

        ContractPricingResponseDTO contractDetailsOfLatestVersion = buildContractPricingResponseDTO(existingContractPriceProfileSeq);

        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceInformationDTO(existingContractPriceProfileSeq, clmContractResponseDTO);

        when(contractPriceProfileRepository.fetchLatestContractVersionNumber(clmContractResponseDTO.getParentAgreementId())).thenReturn(latestVersionNumber);
        when(contractPriceProfileRepository.fetchContractDetailsForLatestActivatedContractVersion(clmContractResponseDTO.getParentAgreementId()))
                .thenReturn(contractDetailsOfLatestVersion);
        when(cppVersionCreator.createNextCppVersion(latestVersionNumber, contractDetailsOfLatestVersion.getContractPriceProfileId()))
                .thenReturn(cppInformationDTO);
        when(contractPriceProfileRepository.fetchInProgressContractVersionCount(clmContractResponseDTO.getParentAgreementId())).thenReturn(0);
        doReturn(furtheranceInformationDTO).when(cppFurtheranceRepository)
                .fetchInProgressFurtheranceDetailsByParentAgreementId(clmContractResponseDTO.getParentAgreementId());
        when(furtheranceService.hasInProgressFurtherance(clmContractResponseDTO.getParentAgreementId())).thenReturn(false);

        CPPInformationDTO actual = target.createNewContractVersion(clmContractResponseDTO);

        assertThat(actual.getContractPriceProfileId(), equalTo(1));
        assertThat(actual.getContractPriceProfileSeq(), equalTo(1));
        assertThat(actual.getVersionNumber(), equalTo(1));

        verify(furtheranceService).hasInProgressFurtherance(clmContractResponseDTO.getParentAgreementId());
        verify(contractPriceProfileRepository).fetchLatestContractVersionNumber(clmContractResponseDTO.getParentAgreementId());
        verify(contractPriceProfileRepository).fetchInProgressContractVersionCount(clmContractResponseDTO.getParentAgreementId());
        verify(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(clmContractResponseDTO.getParentAgreementId());
        verify(cppVersionCreator).createNextCppVersion(latestVersionNumber, contractDetailsOfLatestVersion.getContractPriceProfileId());

    }

    private FurtheranceInformationDTO buildFurtheranceInformationDTO(int existingContractPriceProfileSeq,
            ClmContractResponseDTO clmContractResponseDTO) {

        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setContractPriceProfileSeq(existingContractPriceProfileSeq);
        furtheranceInformationDTO.setCppFurtheranceSeq(2000);
        furtheranceInformationDTO.setParentCLMAgreementId(clmContractResponseDTO.getParentAgreementId());
        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_ACTIVATED.getCode());
        return furtheranceInformationDTO;
    }

    private ContractPricingResponseDTO buildContractPricingResponseDTO(int existingContractPriceProfileSeq) {

        ContractPricingResponseDTO contractDetailsOfLatestVersion = new ContractPricingResponseDTO();
        contractDetailsOfLatestVersion.setClmAgreementId("clmAgreementId");
        contractDetailsOfLatestVersion.setClmContractTypeSeq(6);
        contractDetailsOfLatestVersion.setContractPriceProfileId(1889779);
        contractDetailsOfLatestVersion.setContractPriceProfileSeq(existingContractPriceProfileSeq);
        contractDetailsOfLatestVersion.setContractPriceProfStatusCode(20);
        contractDetailsOfLatestVersion.setExpireLowerLevelInd(1);
        contractDetailsOfLatestVersion.setPricingEffectiveDate(new LocalDate(9999, 01, 01).toDate());
        contractDetailsOfLatestVersion.setPricingExhibitSysId("pricingExhibitSysId");
        contractDetailsOfLatestVersion.setPricingExpirationDate(new LocalDate(9999, 01, 01).toDate());
        contractDetailsOfLatestVersion.setVersionNumber(2);
        return contractDetailsOfLatestVersion;
    }

    private CPPInformationDTO buildCPPInformationDTO() {

        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileId(1);
        cppInformationDTO.setContractPriceProfileSeq(1);
        cppInformationDTO.setVersionNumber(1);
        return cppInformationDTO;
    }

    private ClmContractResponseDTO buildClmContractResponseDTO() throws ParseException {

        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractAgreementId("Test");
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999");
        clmContractResponseDTO.setContractEffectiveDate(effectiveDate);
        clmContractResponseDTO.setAmendment(false);
        Date endDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999");
        clmContractResponseDTO.setContractExpirationDate(endDate);
        clmContractResponseDTO.setContractName("Test");
        clmContractResponseDTO.setContractStatus("Draft");
        clmContractResponseDTO.setContractTypeName("Test");
        clmContractResponseDTO.setParentAgreementId("Test");
        clmContractResponseDTO.setContractAgreementId("contractAgreementId");
        clmContractResponseDTO.setAmendmentEffectiveDate( new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2020"));
        return clmContractResponseDTO;
    }
}
