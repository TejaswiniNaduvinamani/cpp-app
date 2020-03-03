package com.gfs.cpp.component.customerinfo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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
import com.gfs.cpp.common.dto.clm.ClmContractDTO;
import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.customerinfo.AmendmentContractCreator;
import com.gfs.cpp.component.customerinfo.CPPInformationService;
import com.gfs.cpp.component.customerinfo.CppVersionCreator;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@RunWith(MockitoJUnitRunner.class)
public class CPPInformationServiceTest {

    @InjectMocks
    private CPPInformationService target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private CppUserDetailsService cppUserDetailsService;

    @Mock
    private ClmApiProxy clmApiProxy;

    @Mock
    private CppVersionCreator cppVersionCreator;

    @Mock
    private AmendmentContractCreator amendmentContractCreator;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldFetchContractPriceProfileInformation() throws Exception {
        String agreementId = "test";
        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileId(1);
        cppInformationDTO.setContractPriceProfileSeq(1);
        cppInformationDTO.setVersionNumber(1);
        String contractType = "test";
        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractAgreementId("Test");
        Date effectiveDate = new LocalDate(2018, 1, 1).toDate();
        clmContractResponseDTO.setContractEffectiveDate(effectiveDate);
        clmContractResponseDTO.setAmendment(false);
        Date endDate = new LocalDate(2019, 1, 1).toDate();
        clmContractResponseDTO.setContractExpirationDate(endDate);
        clmContractResponseDTO.setContractName("Test");
        clmContractResponseDTO.setContractStatus("Draft");
        clmContractResponseDTO.setContractTypeName("Test");
        clmContractResponseDTO.setParentAgreementId("Test");
        clmContractResponseDTO.setContractAgreementId("contractAgreementId");

        when(contractPriceProfileRepository.fetchCPPInformation(agreementId)).thenReturn(cppInformationDTO);
        when(clmApiProxy.getAgreementData(agreementId, contractType)).thenReturn(clmContractResponseDTO);

        ClmContractDTO actual = target.fetchContractPriceProfileInfo(agreementId, contractType);

        assertThat(actual.getContractEndDate(), equalTo(endDate));
        assertThat(actual.getContractName(), equalTo("Test"));
        assertThat(actual.getContractStartDate(), equalTo(effectiveDate));

        assertThat(actual.getContractStatus(), equalTo("Draft"));
        assertThat(actual.getContractType(), equalTo("Test"));
        assertThat(actual.getAgreementId(), equalTo("contractAgreementId"));
        assertThat(actual.getIsAmendment(), equalTo(false));
        assertThat(actual.getParentAgreementId(), equalTo("Test"));
        assertThat(actual.getCppInformationDto().getContractPriceProfileId(), equalTo(1));
        assertThat(actual.getCppInformationDto().getContractPriceProfileSeq(), equalTo(1));
        assertThat(actual.getCppInformationDto().getVersionNumber(), equalTo(1));

        verify(contractPriceProfileRepository).fetchCPPInformation(agreementId);
        verify(clmApiProxy).getAgreementData(agreementId, contractType);

    }

    @Test
    public void shouldFetchContractPriceProfileInformationForNull() throws Exception {
        String agreementId = "test";
        String contractType = "test";

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

        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileId(1);
        cppInformationDTO.setContractPriceProfileSeq(1);
        cppInformationDTO.setVersionNumber(1);

        when(cppVersionCreator.createInitialVersion()).thenReturn(cppInformationDTO);
        when(cppUserDetailsService.hasContractEditAccess()).thenReturn(true);
        when(contractPriceProfileRepository.fetchCPPInformation(agreementId)).thenReturn(null);
        when(clmApiProxy.getAgreementData(agreementId, contractType)).thenReturn(clmContractResponseDTO);
        when(cppDateUtils.getCurrentDateAsLocalDate()).thenReturn(new LocalDate());

        ClmContractDTO actual = target.fetchContractPriceProfileInfo(agreementId, contractType);

        assertThat(actual.getContractEndDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999")));
        assertThat(actual.getContractName(), equalTo("Test"));
        assertThat(actual.getContractStartDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999")));

        assertThat(actual.getContractStatus(), equalTo("Draft"));
        assertThat(actual.getContractType(), equalTo("Test"));
        assertThat(actual.getAgreementId(), equalTo("contractAgreementId"));
        assertThat(actual.getIsAmendment(), equalTo(false));
        assertThat(actual.getParentAgreementId(), equalTo("Test"));

        assertThat(actual.getCppInformationDto().getContractPriceProfileId(), equalTo(1));
        assertThat(actual.getCppInformationDto().getContractPriceProfileSeq(), equalTo(1));
        assertThat(actual.getCppInformationDto().getVersionNumber(), equalTo(1));

        verify(contractPriceProfileRepository).fetchCPPInformation(agreementId);
        verify(cppVersionCreator).createInitialVersion();
        verify(cppUserDetailsService).hasContractEditAccess();
        verify(contractPriceProfileRepository).fetchCPPInformation(agreementId);
        verify(clmApiProxy).getAgreementData(agreementId, contractType);
        verify(cppDateUtils).getCurrentDateAsLocalDate();

    }

    @Test
    public void shouldFetchContractPriceProfileInformationForAmendment() throws Exception {
        String agreementId = "test";
        String contractType = "test";
        Date endDate = new LocalDate(2019, 01, 01).toDate();
        Date effectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date amendmentEffectiveDate = new LocalDate(2018, 01, 01).toDate();

        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractAgreementId("Test");
        clmContractResponseDTO.setContractEffectiveDate(effectiveDate);
        clmContractResponseDTO.setAmendmentEffectiveDate(amendmentEffectiveDate);
        clmContractResponseDTO.setAmendment(true);
        clmContractResponseDTO.setContractExpirationDate(endDate);
        clmContractResponseDTO.setContractName("Test");
        clmContractResponseDTO.setContractStatus("Draft");
        clmContractResponseDTO.setContractTypeName("Test");
        clmContractResponseDTO.setParentAgreementId("Test");
        clmContractResponseDTO.setContractAgreementId("contractAgreementId");

        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileId(1);
        cppInformationDTO.setContractPriceProfileSeq(1);
        cppInformationDTO.setVersionNumber(1);

        when(amendmentContractCreator.createNewContractVersion(clmContractResponseDTO)).thenReturn(cppInformationDTO);
        when(cppUserDetailsService.hasContractEditAccess()).thenReturn(true);
        when(contractPriceProfileRepository.fetchCPPInformation(agreementId)).thenReturn(null);
        when(clmApiProxy.getAgreementData(agreementId, contractType)).thenReturn(clmContractResponseDTO);
        when(cppDateUtils.getCurrentDateAsLocalDate()).thenReturn(new LocalDate());

        ClmContractDTO actual = target.fetchContractPriceProfileInfo(agreementId, contractType);

        assertThat(actual.getContractEndDate(), equalTo(endDate));
        assertThat(actual.getContractName(), equalTo("Test"));
        assertThat(actual.getContractStartDate(), equalTo(amendmentEffectiveDate));

        assertThat(actual.getContractStatus(), equalTo("Draft"));
        assertThat(actual.getContractType(), equalTo("Test"));
        assertThat(actual.getAgreementId(), equalTo("contractAgreementId"));
        assertThat(actual.getIsAmendment(), equalTo(true));
        assertThat(actual.getParentAgreementId(), equalTo("Test"));

        assertThat(actual.getCppInformationDto().getContractPriceProfileId(), equalTo(1));
        assertThat(actual.getCppInformationDto().getContractPriceProfileSeq(), equalTo(1));
        assertThat(actual.getCppInformationDto().getVersionNumber(), equalTo(1));

        verify(amendmentContractCreator).createNewContractVersion(clmContractResponseDTO);
        verify(cppUserDetailsService).hasContractEditAccess();
        verify(contractPriceProfileRepository).fetchCPPInformation(agreementId);
        verify(clmApiProxy).getAgreementData(agreementId, contractType);
        verify(cppDateUtils).getCurrentDateAsLocalDate();

    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldFetchContractPriceProfileInformationException() throws Exception {
        String agreementId = "test";
        String contractType = "test";
        when(cppUserDetailsService.hasContractEditAccess()).thenReturn(false);
        when(contractPriceProfileRepository.fetchCPPInformation(agreementId)).thenReturn(null);
        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractName("contract-name");
        when(clmApiProxy.getAgreementData(agreementId, contractType)).thenReturn(clmContractResponseDTO);
        ClmContractDTO actual = target.fetchContractPriceProfileInfo(agreementId, contractType);

        assertThat(actual.getCppInformationDto().getContractPriceProfileId(), equalTo(0));
        assertThat(actual.getCppInformationDto().getContractPriceProfileSeq(), equalTo(0));
        assertThat(actual.getCppInformationDto().getVersionNumber(), equalTo(1));

        verify(cppUserDetailsService).hasContractEditAccess();
        verify(contractPriceProfileRepository).fetchCPPInformation(agreementId);

    }

    @Test
    public void shouldFetchContractPriceProfileInfoForNewContractWithValidClmStatus() throws Exception {
        String agreementId = "test";
        String contractType = "test";

        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileId(1);
        cppInformationDTO.setContractPriceProfileSeq(1);
        cppInformationDTO.setVersionNumber(1);

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

        when(contractPriceProfileRepository.fetchCPPInformation(agreementId)).thenReturn(cppInformationDTO);
        when(clmApiProxy.getAgreementData(agreementId, contractType)).thenReturn(clmContractResponseDTO);

        ClmContractDTO actual = target.fetchContractPriceProfileInfo(agreementId, contractType);

        assertThat(actual.getContractEndDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999")));
        assertThat(actual.getContractName(), equalTo("Test"));
        assertThat(actual.getContractStartDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999")));
        assertThat(actual.getContractStatus(), equalTo("Draft"));
        assertThat(actual.getContractType(), equalTo("Test"));
        assertThat(actual.getAgreementId(), equalTo("contractAgreementId"));
        assertThat(actual.getIsAmendment(), equalTo(false));
        assertThat(actual.getParentAgreementId(), equalTo("Test"));
        assertThat(actual.getCppInformationDto().getContractPriceProfileId(), equalTo(1));
        assertThat(actual.getCppInformationDto().getContractPriceProfileSeq(), equalTo(1));
        assertThat(actual.getCppInformationDto().getVersionNumber(), equalTo(1));

        verify(contractPriceProfileRepository).fetchCPPInformation(agreementId);
        verify(clmApiProxy).getAgreementData(agreementId, contractType);

    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldNotFetchContractPriceProfileInfoForNewContractWithInValidClmStatus() throws Exception {
        String agreementId = "test";
        String contractType = "test";

        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractAgreementId("Test");
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999");
        clmContractResponseDTO.setContractEffectiveDate(effectiveDate);
        Date endDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999");
        clmContractResponseDTO.setContractExpirationDate(endDate);
        clmContractResponseDTO.setAmendment(false);
        clmContractResponseDTO.setContractName("Test");
        clmContractResponseDTO.setContractStatus("Waiting For Approval");
        clmContractResponseDTO.setContractTypeName("Test");
        clmContractResponseDTO.setParentAgreementId("Test");
        clmContractResponseDTO.setContractAgreementId("contractAgreementId");

        when(cppUserDetailsService.hasContractEditAccess()).thenReturn(true);
        when(contractPriceProfileRepository.fetchCPPInformation(agreementId)).thenReturn(null);
        when(clmApiProxy.getAgreementData(agreementId, contractType)).thenReturn(clmContractResponseDTO);

        ClmContractDTO actual = target.fetchContractPriceProfileInfo(agreementId, contractType);

        assertThat(actual.getContractEndDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999")));
        assertThat(actual.getContractName(), equalTo("Test"));
        assertThat(actual.getContractStartDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999")));
        assertThat(actual.getContractStatus(), equalTo("Waiting For Approval"));
        assertThat(actual.getContractType(), equalTo("Test"));
        assertThat(actual.getAgreementId(), equalTo("contractAgreementId"));
        assertThat(actual.getIsAmendment(), equalTo(false));
        assertThat(actual.getParentAgreementId(), equalTo("Test"));
        assertThat(actual.getCppInformationDto().getContractPriceProfileId(), equalTo(1));
        assertThat(actual.getCppInformationDto().getContractPriceProfileSeq(), equalTo(1));
        assertThat(actual.getCppInformationDto().getVersionNumber(), equalTo(1));

        verify(cppUserDetailsService).hasContractEditAccess();
        verify(contractPriceProfileRepository).fetchCPPInformation(agreementId);
        verify(clmApiProxy).getAgreementData(agreementId, contractType);
    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldThrowRuntimeExceptionForExpiredContract() throws Exception {
        String agreementId = "test";
        String contractType = "test";

        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractAgreementId("Test");
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999");
        clmContractResponseDTO.setContractEffectiveDate(effectiveDate);
        Date endDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2000");
        clmContractResponseDTO.setContractExpirationDate(endDate);
        clmContractResponseDTO.setAmendment(false);
        clmContractResponseDTO.setContractName("Test");
        clmContractResponseDTO.setContractStatus("Draft");
        clmContractResponseDTO.setContractTypeName("Test");
        clmContractResponseDTO.setParentAgreementId("Test");
        clmContractResponseDTO.setContractAgreementId("contractAgreementId");

        when(cppUserDetailsService.hasContractEditAccess()).thenReturn(true);
        when(contractPriceProfileRepository.fetchCPPInformation(agreementId)).thenReturn(null);
        when(clmApiProxy.getAgreementData(agreementId, contractType)).thenReturn(clmContractResponseDTO);
        when(cppDateUtils.getCurrentDateAsLocalDate()).thenReturn(new LocalDate());

        ClmContractDTO actual = target.fetchContractPriceProfileInfo(agreementId, contractType);

        assertThat(actual.getContractEndDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999")));
        assertThat(actual.getContractName(), equalTo("Test"));
        assertThat(actual.getContractStartDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999")));
        assertThat(actual.getContractStatus(), equalTo("Draft"));
        assertThat(actual.getContractType(), equalTo("Test"));
        assertThat(actual.getAgreementId(), equalTo("contractAgreementId"));
        assertThat(actual.getIsAmendment(), equalTo(false));
        assertThat(actual.getParentAgreementId(), equalTo("Test"));
        assertThat(actual.getCppInformationDto().getContractPriceProfileId(), equalTo(1));
        assertThat(actual.getCppInformationDto().getContractPriceProfileSeq(), equalTo(1));
        assertThat(actual.getCppInformationDto().getVersionNumber(), equalTo(1));

        verify(cppUserDetailsService).hasContractEditAccess();
        verify(contractPriceProfileRepository).fetchCPPInformation(agreementId);
        verify(clmApiProxy).getAgreementData(agreementId, contractType);
        verify(cppDateUtils).getCurrentDateAsLocalDate();
    }
    
    @Test
    public void shouldFetchContractPriceProfileInfoForContractNameGreaterThenMaxLimit() throws Exception {
        String agreementId = "test";
        String contractType = "test";

        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileId(1);
        cppInformationDTO.setContractPriceProfileSeq(1);
        cppInformationDTO.setVersionNumber(1);

       
        when(contractPriceProfileRepository.fetchCPPInformation(agreementId)).thenReturn(cppInformationDTO);
        when(clmApiProxy.getAgreementData(agreementId, contractType)).thenReturn(buildContractPriceProfileDetailsWithContractNameGreaterThenMaxLimit());

        ClmContractDTO actual = target.fetchContractPriceProfileInfo(agreementId, contractType);

        assertThat(actual.getContractEndDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999")));
        assertThat(actual.getContractName().length(), equalTo(CPPConstants.MAX_CONTRACT_NAME_LENGTH));
        assertThat(actual.getContractStartDate(), equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999")));
        assertThat(actual.getContractStatus(), equalTo("Draft"));
        assertThat(actual.getContractType(), equalTo("Test"));
        assertThat(actual.getAgreementId(), equalTo("contractAgreementId"));
        assertThat(actual.getIsAmendment(), equalTo(false));
        assertThat(actual.getParentAgreementId(), equalTo("Test"));
        assertThat(actual.getCppInformationDto().getContractPriceProfileId(), equalTo(1));
        assertThat(actual.getCppInformationDto().getContractPriceProfileSeq(), equalTo(1));
        assertThat(actual.getCppInformationDto().getVersionNumber(), equalTo(1));

        verify(contractPriceProfileRepository).fetchCPPInformation(agreementId);
        verify(clmApiProxy).getAgreementData(agreementId, contractType);

    }
    
    private ClmContractResponseDTO buildContractPriceProfileDetailsWithContractNameGreaterThenMaxLimit() throws ParseException {
  
        String clmContractName = "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_"
                + "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_";

        ClmContractResponseDTO clmContractResponseDTO = new ClmContractResponseDTO();
        clmContractResponseDTO.setContractAgreementId("Test");
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999");
        clmContractResponseDTO.setContractEffectiveDate(effectiveDate);
        clmContractResponseDTO.setAmendment(false);
        Date endDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999");
        clmContractResponseDTO.setContractExpirationDate(endDate);
        clmContractResponseDTO.setContractName(clmContractName);
        clmContractResponseDTO.setContractStatus("Draft");
        clmContractResponseDTO.setContractTypeName("Test");
        clmContractResponseDTO.setParentAgreementId("Test");
        clmContractResponseDTO.setContractAgreementId("contractAgreementId");

        return clmContractResponseDTO;
    }

}
